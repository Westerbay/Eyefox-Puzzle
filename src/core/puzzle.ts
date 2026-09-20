/**
 * Rules ported from Mathis Dubuisson's Android Puzzle, Grid and LevelStage.
 * A move flips the selected cell AND all eight surrounding cells, diagonals
 * included. At an edge the 3 × 3 neighbourhood is clipped, never wrapped.
 */
export type Board = readonly boolean[]

export interface PuzzleDefinition {
  size: number
  target: Board
  initial: Board
  /** A valid solution within moveLimit; not necessarily the shortest one. */
  solution: readonly number[]
  moveLimit: number
}

function cellCount(size: number): number {
  const count = size * size
  if (!Number.isSafeInteger(size) || size < 1 || !Number.isSafeInteger(count)) {
    throw new RangeError(
      "Grid size must be a positive safe integer with a safe cell count.",
    )
  }
  return count
}

/** Row-major indices in the clipped 3 × 3 neighbourhood, including the centre. */
export function affectedCells(size: number, index: number): number[] {
  const count = cellCount(size)
  if (!Number.isSafeInteger(index) || index < 0 || index >= count) {
    throw new RangeError("Cell index is outside the grid.")
  }
  const row = Math.floor(index / size)
  const column = index % size
  const cells: number[] = []
  for (
    let nextRow = Math.max(0, row - 1);
    nextRow <= Math.min(size - 1, row + 1);
    nextRow++
  ) {
    for (
      let nextColumn = Math.max(0, column - 1);
      nextColumn <= Math.min(size - 1, column + 1);
      nextColumn++
    ) {
      cells.push(nextRow * size + nextColumn)
    }
  }
  return cells
}

/** Return a new board; applying the same move twice restores the input. */
export function flipTiles(
  board: Board,
  size: number,
  index: number,
): boolean[] {
  if (board.length !== cellCount(size)) {
    throw new RangeError("Board length does not match the grid size.")
  }
  for (const tile of board) {
    if (typeof tile !== "boolean")
      throw new TypeError("Board cells must be booleans.")
  }
  const cells = affectedCells(size, index)
  const next = [...board]
  for (const cell of cells) next[cell] = !next[cell]
  return next
}

export function isSolved(board: Board, target: Board): boolean {
  if (board.length !== target.length) return false
  for (let index = 0; index < board.length; index++) {
    if (board[index] !== target[index]) return false
  }
  return true
}

/**
 * Levels are zero-based, as in LevelStage.java:
 * sizes cycle 4 → 5 → 6 every ten levels; budgets increase every thirty levels,
 * with one extra move at levels 9, 19, 29, and so on.
 */
export function createPuzzle(
  level: number,
  random: () => number = Math.random,
): PuzzleDefinition {
  if (!Number.isSafeInteger(level) || level < 0) {
    throw new RangeError("Level must be a non-negative safe integer.")
  }
  const size = (Math.floor(level / 10) % 3) + 4
  const count = size * size
  const moveLimit = Math.floor(level / 30) + 1 + (level % 10 === 9 ? 1 : 0)
  const pick = (length: number): number => {
    const value = random()
    if (!Number.isFinite(value))
      throw new RangeError("Random source must return a finite number.")
    // Clamp injected sources safely, including a source returning exactly 1.
    return Math.min(
      length - 1,
      Math.floor(Math.max(0, Math.min(1, value)) * length),
    )
  }
  const target = Array.from({ length: count }, () => pick(2) === 1)
  let initial = [...target]
  const solution: number[] = []
  let available: number[] = []
  // Commuting, self-inverse moves need at most one occurrence per board cell.
  // Bound the work even for extremely high imported levels; keep the Java budget.
  const scrambleLength = Math.min(moveLimit, count)
  for (let move = 0; move < scrambleLength; move++) {
    // Java clears its set after six moves. Picking from the remaining cells
    // preserves that rule without rejection loops when the RNG is constant.
    if (move % 6 === 0)
      available = Array.from({ length: count }, (_, index) => index)
    const [index] = available.splice(pick(available.length), 1)
    initial = flipTiles(initial, size, index!)
    solution.push(index!)
  }
  if (isSolved(initial, target)) {
    // Removing one self-inverse move breaks a cancellation without adding a
    // retry or exceeding the budget. One move always changes at least one cell.
    const lastMove = solution.pop()!
    initial = flipTiles(initial, size, lastMove)
  }
  return { size, target, initial, solution, moveLimit }
}
