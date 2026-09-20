import { expect, test } from "@playwright/test"
import type { Locator, Page } from "@playwright/test"
import { flipTiles, isSolved } from "../../src/core/puzzle"
import type { Board } from "../../src/core/puzzle"

interface VisiblePuzzle {
  size: number
  board: boolean[]
  target: boolean[]
}
const errors = new WeakMap<Page, string[]>()

/** Read the rendered tiles, including in a custom element's open shadow root. */
async function readPuzzle(game: Locator): Promise<VisiblePuzzle> {
  const tiles = await game
    .locator(".eyefox-play button")
    .evaluateAll((buttons) =>
      buttons.map((button) => ({
        value: button.getAttribute("aria-pressed") === "true",
        source: button.querySelector("img")!.getAttribute("src"),
      })),
    )
  const yellow = tiles.find((tile) => tile.value)?.source
  const brown = tiles.find((tile) => !tile.value)?.source
  const sources = await game
    .locator(".eyefox-target img")
    .evaluateAll((images) => images.map((image) => image.getAttribute("src")))
  // Compare displayed artwork instead of relying on file names, which may be
  // hashed or inlined by Vite. The playable tiles expose the same colours.
  return {
    size: Number(
      await game.locator(".eyefox-play").getAttribute("aria-rowcount"),
    ),
    board: tiles.map((tile) => tile.value),
    target: sources.map((source) =>
      yellow !== undefined ? source === yellow : source !== brown,
    ),
  }
}

function solveVisiblePuzzle(puzzle: VisiblePuzzle, budget: number): number[] {
  function search(
    board: Board,
    remaining: number,
    start: number,
  ): number[] | undefined {
    if (isSolved(board, puzzle.target)) return []
    if (!remaining) return undefined
    // Moves commute and are self-inverse, so repeated cells are unnecessary.
    for (let index = start; index < board.length; index++) {
      const rest = search(
        flipTiles(board, puzzle.size, index),
        remaining - 1,
        index + 1,
      )
      if (rest) return [index, ...rest]
    }
    return undefined
  }
  for (let moves = 1; moves <= budget; moves++) {
    const solution = search(puzzle.board, moves, 0)
    if (solution) return solution
  }
  throw new Error(
    "The rendered challenge has no solution within its displayed budget.",
  )
}

async function win(game: Locator, budget: number): Promise<number> {
  const solution = solveVisiblePuzzle(await readPuzzle(game), budget)
  expect(solution.length).toBeGreaterThan(0)
  for (const index of solution)
    await game.locator(".eyefox-play button").nth(index).click()
  await expect(game.locator('[data-result="won"]')).toBeVisible()
  const state = await readPuzzle(game)
  expect(state.board).toEqual(state.target)
  return solution.length
}

function unsuccessfulMove(puzzle: VisiblePuzzle): number {
  return puzzle.board.findIndex(
    (_, index) =>
      !isSolved(flipTiles(puzzle.board, puzzle.size, index), puzzle.target),
  )
}

test.beforeEach(async ({ page }) => {
  const messages: string[] = []
  errors.set(page, messages)
  page.on("pageerror", (error) => messages.push(error.message))
  await page.emulateMedia({ colorScheme: "light", reducedMotion: "reduce" })
  // Make fresh-round checks deterministic; solutions are still derived solely
  // from the public DOM, never from component state or a production test hook.
  await page.addInitScript(() => {
    let seed = 0x5eed
    Math.random = () => {
      seed = (Math.imul(seed, 1664525) + 1013904223) >>> 0
      return seed / 0x1_0000_0000
    }
  })
  await page.goto("/")
  await expect(page.locator(".eyefox-play button")).toHaveCount(16)
})

test.afterEach(async ({ page }) => {
  expect(errors.get(page)).toEqual([])
})

test("the three visible difficulties can be solved using only the rendered grids", async ({
  page,
}) => {
  const game = page.locator(".eyefox")
  for (const [level, budget] of [
    ["0", 1],
    ["9", 2],
    ["39", 3],
  ] as const) {
    await game.getByLabel("Difficulty").selectOption(level)
    await expect(game.locator('[role="status"]')).toContainText(
      `${budget} / ${budget}`,
    )
    expect((await readPuzzle(game)).size).toBe(4)
    await win(game, budget)
    await expect(game.getByRole("status")).toHaveText(
      /Well done, the grids match/,
    )
    await expect(
      game.locator('.eyefox-play button[aria-disabled="true"]'),
    ).toHaveCount(16)
  }
})

test("corner and centre clicks include diagonal neighbours without changing the target", async ({
  page,
}) => {
  const game = page.locator(".eyefox")
  const initial = await readPuzzle(game)
  for (const [index, affected] of [
    [0, [0, 1, 4, 5]],
    [5, [0, 1, 2, 4, 5, 6, 8, 9, 10]],
  ] as const) {
    if (index !== 0)
      await game.getByRole("button", { name: "Retry", exact: true }).click()
    await game.locator(".eyefox-play button").nth(index).hover()
    await expect(
      game.locator('.eyefox-play button[data-near="true"]'),
    ).toHaveCount(affected.length)
    await game.locator(".eyefox-play button").nth(index).click()
    const changed = await readPuzzle(game)
    expect(changed.target).toEqual(initial.target)
    expect(changed.board).toEqual(
      initial.board.map((tile, cell) =>
        (affected as readonly number[]).includes(cell) ? !tile : tile,
      ),
    )
  }
})

test("the move budget locks a failed round, retry restores it and another puzzle starts fresh", async ({
  page,
}) => {
  const game = page.locator(".eyefox")
  await game.getByLabel("Difficulty").selectOption("39")
  const initial = await readPuzzle(game)
  const wrong = unsuccessfulMove(initial)
  expect(wrong).toBeGreaterThanOrEqual(0)
  for (let move = 1; move <= 3; move++) {
    await game.locator(".eyefox-play button").nth(wrong).click()
    if (move < 3)
      await expect(game.getByRole("status")).toContainText(`${3 - move} / 3`)
  }
  await expect(game.locator('[data-result="lost"]')).toContainText(
    "No moves left",
  )
  const failed = await readPuzzle(game)
  await game.locator(".eyefox-play button").nth(wrong).click({ force: true })
  expect(await readPuzzle(game)).toEqual(failed)
  await game.getByRole("button", { name: "Retry", exact: true }).click()
  expect(await readPuzzle(game)).toEqual(initial)
  await expect(game.getByRole("status")).toContainText("3 / 3")
  await expect(
    game.getByRole("button", { name: "Retry", exact: true }),
  ).toBeDisabled()
  await game.getByRole("button", { name: "Another puzzle" }).click()
  const fresh = await readPuzzle(game)
  expect(fresh.target).not.toEqual(initial.target)
  expect(isSolved(fresh.board, fresh.target)).toBe(false)
  await expect(game.getByLabel("Difficulty")).toHaveValue("39")
  await expect(game.getByRole("status")).toContainText("3 / 3")
})

test("one grid tab stop supports arrows, Home/End and Enter/Space moves", async ({
  page,
}) => {
  const game = page.locator(".eyefox")
  await game.getByLabel("Difficulty").selectOption("39")
  const tiles = game.locator(".eyefox-play button")
  await expect(game.locator('.eyefox-play button[tabindex="0"]')).toHaveCount(1)
  await tiles.nth(0).focus()
  for (const [key, index] of [
    ["ArrowLeft", 3],
    ["ArrowUp", 15],
    ["Home", 12],
    ["End", 15],
    ["Control+Home", 0],
    ["ArrowRight", 1],
    ["ArrowDown", 5],
    ["Control+End", 15],
  ] as const) {
    await page.keyboard.press(key)
    await expect(tiles.nth(index)).toBeFocused()
    await expect(game.locator('.eyefox-play button[tabindex="0"]')).toHaveCount(
      1,
    )
    await expect(tiles.nth(index)).toHaveAttribute("tabindex", "0")
  }
  const original = await readPuzzle(game)
  const index = unsuccessfulMove(original)
  await tiles.nth(index).focus()
  await page.keyboard.press("Enter")
  expect((await readPuzzle(game)).board).toEqual(
    flipTiles(original.board, 4, index),
  )
  await page.keyboard.press("Space")
  expect((await readPuzzle(game)).board).toEqual(original.board)
  await expect(game.getByRole("status")).toContainText("1 / 3")
})

test("French, English and theme controls preserve the active puzzle", async ({
  page,
}) => {
  const game = page.locator(".eyefox")
  const state = await readPuzzle(game)
  await expect(game.getByRole("heading", { name: "Your turn" })).toBeVisible()
  await expect(game).toHaveAttribute("data-theme", "light")
  await page.getByRole("button", { name: "Passer en français" }).click()
  await expect(
    game.getByRole("heading", { name: "À vous de jouer" }),
  ).toBeVisible()
  await expect(game.getByLabel("Difficulté")).toBeVisible()
  await expect(
    game.locator(".eyefox-play button").first(),
  ).toHaveAccessibleName(/Ligne 1, colonne 1/)
  await page.getByRole("button", { name: "Thème sombre", exact: true }).click()
  await expect(game).toHaveAttribute("data-theme", "dark")
  expect(await readPuzzle(game)).toEqual(state)
  await page.getByRole("button", { name: "Switch to English" }).click()
  await expect(game.getByRole("heading", { name: "Your turn" })).toBeVisible()
  await page.getByRole("button", { name: "Light theme", exact: true }).click()
  await expect(game).toHaveAttribute("data-theme", "light")
  expect(await readPuzzle(game)).toEqual(state)
})

test("the demo and six-by-six custom element fit small mobile screens", async ({
  page,
}) => {
  await page.evaluate(() => {
    const element = document.createElement("eyefox-puzzle")
    element.id = "mobile-game"
    element.setAttribute("level", "20")
    document.querySelector("main")!.append(element)
  })
  const mobile = page.locator("#mobile-game")
  await expect(mobile.locator(".eyefox-play button")).toHaveCount(36)
  for (const width of [320, 390]) {
    await page.setViewportSize({ width, height: 844 })
    await expect
      .poll(() =>
        page.evaluate(
          () =>
            document.documentElement.scrollWidth -
            document.documentElement.clientWidth,
        ),
      )
      .toBeLessThanOrEqual(1)
    for (const game of [
      page.locator("main > .eyefox"),
      mobile.locator(".eyefox"),
    ]) {
      const bounds = await game.boundingBox()
      expect(bounds).not.toBeNull()
      expect(bounds!.x).toBeGreaterThanOrEqual(0)
      expect(bounds!.x + bounds!.width).toBeLessThanOrEqual(width + 1)
    }
  }
})

test("custom elements isolate rounds, respond to attributes, reconnect and emit completion events", async ({
  page,
}) => {
  await page.evaluate(() => {
    const state = window as typeof window & { eyefoxEvents?: unknown[] }
    state.eyefoxEvents = []
    document.addEventListener("puzzlecomplete", (event) =>
      state.eyefoxEvents!.push({
        id: (event.target as HTMLElement).id,
        detail: (event as CustomEvent).detail,
        bubbles: event.bubbles,
        composed: event.composed,
      }),
    )
    for (const [id, level] of [
      ["game-a", "0"],
      ["game-b", "10"],
    ]) {
      const element = document.createElement("eyefox-puzzle")
      element.id = id!
      element.setAttribute("level", level!)
      element.setAttribute("locale", "en")
      element.setAttribute("theme", "light")
      document.querySelector("main")!.append(element)
    }
  })
  const first = page.locator("#game-a"),
    second = page.locator("#game-b")
  await expect(first.locator(".eyefox-play button")).toHaveCount(16)
  await expect(second.locator(".eyefox-play button")).toHaveCount(25)
  const other = await readPuzzle(second)
  const moves = await win(first, 1)
  expect(await readPuzzle(second)).toEqual(other)
  await expect
    .poll(() =>
      page.evaluate(
        () =>
          (window as typeof window & { eyefoxEvents: unknown[] }).eyefoxEvents,
      ),
    )
    .toEqual([
      {
        id: "game-a",
        detail: { level: 0, size: 4, moves },
        bubbles: true,
        composed: true,
      },
    ])
  await first.evaluate((element) => {
    element.setAttribute("locale", "fr")
    element.setAttribute("theme", "dark")
  })
  await expect(
    first.getByRole("heading", { name: "À vous de jouer" }),
  ).toBeVisible()
  await expect(first.locator(".eyefox")).toHaveAttribute("data-theme", "dark")
  await expect(first.locator('[data-result="won"]')).toBeVisible()
  await expect(second.locator(".eyefox")).toHaveAttribute("data-theme", "light")
  await first.evaluate((element) => element.setAttribute("level", "20"))
  await expect(first.locator(".eyefox-play button")).toHaveCount(36)
  await expect(first.locator('[data-result="playing"]')).toBeVisible()
  expect(await readPuzzle(second)).toEqual(other)
  await first.evaluate((element) => {
    ;(window as typeof window & { detachedEyefox?: Element }).detachedEyefox =
      element
    element.remove()
  })
  await expect(page.locator("#game-a")).toHaveCount(0)
  await page.evaluate(() =>
    document
      .querySelector("main")!
      .append(
        (window as typeof window & { detachedEyefox: Element }).detachedEyefox,
      ),
  )
  await expect(first.locator(".eyefox-play button")).toHaveCount(36)
  await expect(first.locator(".eyefox")).toHaveCount(1)
  await expect(
    first.getByRole("heading", { name: "À vous de jouer" }),
  ).toBeVisible()
  const secondMoves = await win(first, 1)
  await expect
    .poll(() =>
      page.evaluate(
        () =>
          (window as typeof window & { eyefoxEvents: unknown[] }).eyefoxEvents,
      ),
    )
    .toHaveLength(2)
  const completions = await page.evaluate(
    () => (window as typeof window & { eyefoxEvents: unknown[] }).eyefoxEvents,
  )
  expect(completions[1]).toEqual({
    id: "game-a",
    detail: { level: 20, size: 6, moves: secondMoves },
    bubbles: true,
    composed: true,
  })
  expect(await readPuzzle(second)).toEqual(other)
})
