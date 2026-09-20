import { useEffect, useId, useRef, useState } from "react"
import type { CSSProperties, KeyboardEvent } from "react"
import {
  affectedCells,
  createPuzzle,
  flipTiles,
  isSolved,
} from "../core/puzzle"
import type { PuzzleDefinition } from "../core/puzzle"
import lightTile from "../assets/full_cell.png"
import darkTile from "../assets/empty_cell.png"
import awakeFox from "../assets/awake1.png"
import happyFox from "../assets/stand1.png"

export interface EyefoxCompletion {
  level: number
  moves: number
  size: number
}
export interface EyefoxPuzzleProps {
  locale?: "fr" | "en"
  theme?: "light" | "dark" | "system"
  initialLevel?: number
  onComplete?: (result: EyefoxCompletion) => void
}
const copy = {
  fr: {
    title: "À vous de jouer",
    intro: "Reproduisez le modèle en retournant les cases de votre grille.",
    rule: "Un clic retourne aussi les cases autour, diagonales comprises.",
    target: "Modèle",
    board: "Votre grille",
    difficulty: "Difficulté",
    retry: "Recommencer",
    next: "Autre puzzle",
    loading: "Préparation du puzzle",
    left: "Coups restants",
    win: "Bravo, les deux grilles sont identiques",
    lost: "Plus de coups. Réessayez avec la même grille.",
    keyboard:
      "Flèches pour se déplacer, Entrée ou Espace pour retourner une case.",
    light: "jaune",
    dark: "brune",
    row: "Ligne",
    column: "colonne",
    level: "Niveau",
    moves: (count: number) => (count === 1 ? "1 coup" : count + " coups"),
  },
  en: {
    title: "Your turn",
    intro: "Match the target by flipping tiles on your grid.",
    rule: "A click also flips the surrounding tiles, including diagonals.",
    target: "Target",
    board: "Your grid",
    difficulty: "Difficulty",
    retry: "Retry",
    next: "Another puzzle",
    loading: "Preparing the puzzle",
    left: "Moves left",
    win: "Well done, the grids match",
    lost: "No moves left. Try the same grid again.",
    keyboard: "Arrow keys to move, Enter or Space to flip a tile.",
    light: "yellow",
    dark: "brown",
    row: "Row",
    column: "column",
    level: "Level",
    moves: (count: number) => (count === 1 ? "1 move" : count + " moves"),
  },
}
interface Round {
  puzzle: PuzzleDefinition
  board: readonly boolean[]
  moves: number
  changed: number[]
}
function newRound(level: number): Round {
  const puzzle = createPuzzle(level)
  return { puzzle, board: puzzle.initial, moves: 0, changed: [] }
}
export function EyefoxPuzzle({
  locale = "en",
  theme = "system",
  initialLevel = 0,
  onComplete,
}: EyefoxPuzzleProps) {
  const m = copy[locale],
    helpId = useId(),
    difficultyId = useId()
  const [level, setLevel] = useState(initialLevel)
  const [round, setRound] = useState<Round | null>(null)
  const [systemDark, setSystemDark] = useState(false)
  const [active, setActive] = useState(0)
  const buttons = useRef<Array<HTMLButtonElement | null>>([])
  useEffect(() => {
    const media = window.matchMedia("(prefers-color-scheme: dark)")
    const update = () => setSystemDark(media.matches)
    update()
    media.addEventListener("change", update)
    return () => media.removeEventListener("change", update)
  }, [])
  useEffect(() => {
    setLevel(initialLevel)
    setRound(newRound(initialLevel))
    setActive(0)
  }, [initialLevel])
  const dark = theme === "dark" || (theme === "system" && systemDark)
  const won = round !== null && isSolved(round.board, round.puzzle.target)
  const left = round ? round.puzzle.moveLimit - round.moves : 0
  const locked = !round || won || left === 0
  function play(index: number) {
    if (!round || locked) return
    const board = flipTiles(round.board, round.puzzle.size, index)
    const moves = round.moves + 1
    setRound({
      ...round,
      board,
      moves,
      changed: affectedCells(round.puzzle.size, index),
    })
    if (isSolved(board, round.puzzle.target))
      onComplete?.({ level, moves, size: round.puzzle.size })
  }
  function restart() {
    if (round)
      setRound({ ...round, board: round.puzzle.initial, moves: 0, changed: [] })
  }
  function changeLevel(next: number) {
    setLevel(next)
    setRound(newRound(next))
    setActive(0)
  }
  function navigate(event: KeyboardEvent<HTMLButtonElement>, index: number) {
    if (!round) return
    const size = round.puzzle.size,
      row = Math.floor(index / size),
      col = index % size
    let next = index
    switch (event.key) {
      case "ArrowRight":
        next = row * size + ((col + 1) % size)
        break
      case "ArrowLeft":
        next = row * size + ((col + size - 1) % size)
        break
      case "ArrowDown":
        next = ((row + 1) % size) * size + col
        break
      case "ArrowUp":
        next = ((row + size - 1) % size) * size + col
        break
      case "Home":
        next = event.ctrlKey ? 0 : row * size
        break
      case "End":
        next = event.ctrlKey ? size * size - 1 : row * size + size - 1
        break
      default:
        return
    }
    event.preventDefault()
    setActive(next)
    buttons.current[next]?.focus()
  }
  const label = (index: number, value: boolean) =>
    m.row +
    " " +
    (Math.floor(index / round!.puzzle.size) + 1) +
    ", " +
    m.column +
    " " +
    ((index % round!.puzzle.size) + 1) +
    ", " +
    (value ? m.light : m.dark)
  return (
    <section
      className="eyefox"
      data-theme={dark ? "dark" : "light"}
      data-locale={locale}
      role="region"
      aria-label="Eyefox Puzzle"
    >
      <div className="eyefox-intro">
        <img
          className="eyefox-fox"
          src={won ? happyFox : awakeFox}
          alt=""
          width="110"
          height="80"
        />
        <div>
          <h2>{m.title}</h2>
          <p>
            {m.intro}
            <br />
            {m.rule}
          </p>
        </div>
      </div>
      <div className="eyefox-toolbar">
        <div className="eyefox-difficulty">
          <label htmlFor={difficultyId}>{m.difficulty}</label>
          <select
            id={difficultyId}
            value={level}
            onChange={(e) => changeLevel(Number(e.target.value))}
          >
            <option value="0">{m.moves(1)}</option>
            <option value="9">{m.moves(2)}</option>
            <option value="39">{m.moves(3)}</option>
            {![0, 9, 39].includes(level) && (
              <option value={level}>
                {m.level} {level + 1}
              </option>
            )}
          </select>
        </div>
        <div className="eyefox-actions">
          <button
            type="button"
            onClick={restart}
            disabled={!round || round.moves === 0}
          >
            {m.retry}
          </button>
          <button type="button" onClick={() => changeLevel(level)}>
            {m.next}
            <span aria-hidden="true">↗</span>
          </button>
        </div>
      </div>
      {round ? (
        <div
          className="eyefox-boards"
          style={{ "--eyefox-size": round.puzzle.size } as CSSProperties}
        >
          <div className="eyefox-board-wrap">
            <h3>
              <span aria-hidden="true">01</span>
              {m.target}
            </h3>
            <div
              className="eyefox-grid eyefox-target"
              role="img"
              aria-label={
                m.target +
                ": " +
                round.puzzle.target
                  .map((value, index) => label(index, value))
                  .join("; ")
              }
            >
              {round.puzzle.target.map((value, index) => (
                <img
                  key={index}
                  src={value ? lightTile : darkTile}
                  alt=""
                  draggable="false"
                />
              ))}
            </div>
          </div>
          <span className="eyefox-between" aria-hidden="true">
            →
          </span>
          <div className="eyefox-board-wrap">
            <h3>
              <span aria-hidden="true">02</span>
              {m.board}
            </h3>
            <div
              className="eyefox-grid eyefox-play"
              role="grid"
              aria-label={m.board}
              aria-rowcount={round.puzzle.size}
              aria-colcount={round.puzzle.size}
              aria-describedby={helpId}
            >
              {Array.from({ length: round.puzzle.size }, (_, row) => (
                <div role="row" className="eyefox-row" key={row}>
                  {Array.from({ length: round.puzzle.size }, (_, col) => {
                    const index = row * round.puzzle.size + col,
                      value = round.board[index]
                    return (
                      <div role="gridcell" key={index}>
                        <button
                          type="button"
                          ref={(button) => {
                            buttons.current[index] = button
                          }}
                          aria-label={label(index, value)}
                          aria-pressed={value}
                          aria-disabled={locked}
                          tabIndex={active === index ? 0 : -1}
                          onFocus={() => {
                            setActive(index)
                          }}
                          onKeyDown={(e) => navigate(e, index)}
                          onClick={() => play(index)}
                        >
                          <img
                            key={
                              round.changed.includes(index)
                                ? round.moves
                                : "still"
                            }
                            className={
                              round.changed.includes(index)
                                ? "eyefox-flipped"
                                : ""
                            }
                            src={value ? lightTile : darkTile}
                            alt=""
                            draggable="false"
                          />
                        </button>
                      </div>
                    )
                  })}
                </div>
              ))}
            </div>
          </div>
        </div>
      ) : (
        <div className="eyefox-loading">{m.loading}</div>
      )}
      <div
        className="eyefox-feedback"
        role="status"
        aria-live="polite"
        aria-atomic="true"
        data-result={won ? "won" : left === 0 && round ? "lost" : "playing"}
      >
        <span aria-hidden="true" className="eyefox-state-icon">
          {won ? "✓" : left === 0 && round ? "↻" : "·"}
        </span>
        <span>
          {won ? (
            m.win
          ) : round && left === 0 ? (
            m.lost
          ) : round ? (
            <>
              {m.left}{" "}
              <strong>
                {left} / {round.puzzle.moveLimit}
              </strong>
            </>
          ) : (
            m.loading
          )}
        </span>
      </div>
      <p id={helpId} className="eyefox-keyboard">
        {m.keyboard}
      </p>
    </section>
  )
}
