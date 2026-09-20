import assert from "node:assert/strict"
import test from "node:test"
import {
  affectedCells,
  createPuzzle,
  flipTiles,
  isSolved,
} from "../src/core/puzzle.ts"
import type { Board } from "../src/core/puzzle.ts"

function seededRandom(seed: number): () => number {
  let state = seed >>> 0
  return () => {
    state = (Math.imul(state, 1664525) + 1013904223) >>> 0
    return state / 0x1_0000_0000
  }
}

function solve(board: Board, size: number, moves: readonly number[]): Board {
  return moves.reduce<Board>(
    (current, index) => flipTiles(current, size, index),
    board,
  )
}

test("a corner flips itself and its three neighbours, including the diagonal", () => {
  assert.deepEqual(affectedCells(4, 0), [0, 1, 4, 5])
  assert.deepEqual(affectedCells(4, 3), [2, 3, 6, 7])
  assert.deepEqual(affectedCells(4, 12), [8, 9, 12, 13])
  assert.deepEqual(affectedCells(4, 15), [10, 11, 14, 15])
})

test("edge and centre moves flip a clipped 3 × 3 square, never a cross or wrapped rows", () => {
  assert.deepEqual(affectedCells(4, 1), [0, 1, 2, 4, 5, 6])
  assert.deepEqual(affectedCells(4, 7), [2, 3, 6, 7, 10, 11])
  assert.deepEqual(affectedCells(4, 5), [0, 1, 2, 4, 5, 6, 8, 9, 10])
  assert.deepEqual(affectedCells(1, 0), [0])
})

test("flips copy the input, only change affected cells, and are self-inverse", () => {
  for (const size of [4, 5, 6]) {
    const input = Object.freeze(
      Array.from({ length: size * size }, (_, index) => index % 3 === 0),
    )
    for (let index = 0; index < input.length; index++) {
      const changed = flipTiles(input, size, index)
      const affected = new Set(affectedCells(size, index))
      assert.notEqual(changed, input)
      for (let tile = 0; tile < input.length; tile++) {
        assert.equal(
          changed[tile],
          affected.has(tile) ? !input[tile] : input[tile],
        )
      }
      assert.deepEqual(flipTiles(changed, size, index), input)
    }
  }
})

test("level sizes and move budgets match the original LevelStage boundaries", () => {
  const stages = [
    [0, 4, 1],
    [8, 4, 1],
    [9, 4, 2],
    [10, 5, 1],
    [19, 5, 2],
    [20, 6, 1],
    [28, 6, 1],
    [29, 6, 2],
    [30, 4, 2],
    [39, 4, 3],
    [59, 6, 3],
    [60, 4, 3],
    [89, 6, 4],
    [90, 4, 4],
  ] as const
  for (const [level, size, budget] of stages) {
    const puzzle = createPuzzle(level, seededRandom(level))
    assert.equal(puzzle.size, size)
    assert.equal(puzzle.moveLimit, budget)
  }
})

test("generated puzzles are unsolved and their known solution reaches the target within budget", () => {
  for (let level = 0; level < 240; level++) {
    const puzzle = createPuzzle(level, seededRandom(level + 1))
    assert.equal(puzzle.target.length, puzzle.size ** 2)
    assert.equal(puzzle.initial.length, puzzle.target.length)
    assert.notEqual(puzzle.initial, puzzle.target)
    assert.ok(!isSolved(puzzle.initial, puzzle.target))
    assert.ok(
      puzzle.solution.length >= 1 && puzzle.solution.length <= puzzle.moveLimit,
    )
    assert.ok(
      isSolved(
        solve(puzzle.initial, puzzle.size, puzzle.solution),
        puzzle.target,
      ),
    )
    assert.deepEqual(
      solve(puzzle.target, puzzle.size, puzzle.solution),
      puzzle.initial,
    )
    for (let start = 0; start < puzzle.solution.length; start += 6) {
      const block = puzzle.solution.slice(start, start + 6)
      assert.equal(new Set(block).size, block.length)
    }
  }
})

test("constant and boundary random sources terminate, including a cancelling scramble", () => {
  for (const value of [-1, 0, 0.5, 1, 2]) {
    for (const level of [0, 9, 39, 179, 330, Number.MAX_SAFE_INTEGER]) {
      let calls = 0
      const puzzle = createPuzzle(level, () => {
        calls++
        return value
      })
      assert.ok(calls <= puzzle.size ** 2 * 2)
      assert.ok(!isSolved(puzzle.initial, puzzle.target))
      assert.ok(puzzle.solution.length <= puzzle.moveLimit)
      assert.deepEqual(
        solve(puzzle.initial, puzzle.size, puzzle.solution),
        puzzle.target,
      )
    }
  }
  // Twelve constant-RNG moves repeat the same six cells twice. Removing the
  // final move produces an unsolved puzzle rather than retrying forever.
  const cancellation = createPuzzle(330, () => 0)
  assert.equal(cancellation.moveLimit, 12)
  assert.equal(cancellation.solution.length, 11)
})

test("seeded generation is deterministic and board equality compares shape and contents", () => {
  assert.deepEqual(
    createPuzzle(39, seededRandom(123)),
    createPuzzle(39, seededRandom(123)),
  )
  assert.ok(isSolved([true, false], [true, false]))
  assert.ok(!isSolved([true, false], [true, true]))
  assert.ok(!isSolved([true], [true, false]))
})

test("invalid levels, dimensions, indices, boards and non-finite random values fail immediately", () => {
  for (const level of [-1, 0.5, NaN, Infinity, Number.MAX_SAFE_INTEGER + 1]) {
    assert.throws(() => createPuzzle(level), RangeError)
  }
  for (const size of [0, -1, 2.5, Infinity])
    assert.throws(() => affectedCells(size, 0), RangeError)
  for (const index of [-1, 16, 0.5, NaN])
    assert.throws(() => affectedCells(4, index), RangeError)
  assert.throws(() => flipTiles([true], 4, 0), RangeError)
  assert.throws(() => flipTiles(new Array<boolean>(16), 4, 0), TypeError)
  for (const value of [NaN, Infinity, -Infinity]) {
    assert.throws(() => createPuzzle(0, () => value), RangeError)
  }
})
