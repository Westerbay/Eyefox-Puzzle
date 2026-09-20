# Eyefox Puzzle for the web

A small playable version of my Android puzzle game, with the original tiles and fox.
Flip your grid to match the target. A move flips the selected tile and all its
neighbours, **including diagonals**.

[Play the demo](https://westerbay.github.io/Eyefox-Puzzle/) ·
[Android source](https://github.com/Westerbay/Eyefox-Puzzle/tree/main)

This branch contains a React component, an optional custom element and the
standalone demo. The Android application remains on `main`.

## React

Install the versioned package:

```sh
pnpm add https://github.com/Westerbay/Eyefox-Puzzle/releases/download/web-v0.1.2/westerbay-eyefox-react-0.1.2.tgz
```

```tsx
import { EyefoxPuzzle } from "@westerbay/eyefox-react"
import "@westerbay/eyefox-react/styles.css"
;<EyefoxPuzzle locale="en" theme="dark" initialLevel={0} />
```

Props:

| Prop           | Values                                               | Default    |
| -------------- | ---------------------------------------------------- | ---------- |
| `locale`       | `"en"` or `"fr"`                                     | `"en"`     |
| `theme`        | `"light"`, `"dark"` or `"system"`                    | `"system"` |
| `initialLevel` | Non-negative safe integer, Android zero-based level  | `0`        |
| `onComplete`   | Receives `{ level, moves, size }` on a solved puzzle | Optional   |

The difficulty selector gives direct access to levels 0, 9 and 39: one, two or
three moves on a 4×4 board. Other initial levels use the Android size and move
budget formulas. Changing `initialLevel` starts a new puzzle. Locale and theme
changes preserve the current game.

## Custom element

From a JavaScript application using a bundler:

```js
import { defineEyefoxElement } from "@westerbay/eyefox-react/element"
defineEyefoxElement()
```

```html
<eyefox-puzzle locale="en" theme="dark" level="9"></eyefox-puzzle>
```

Styles are included inside the shadow root. Attributes `locale`, `theme` and
`level` are observed. The `puzzlecomplete` event exposes the same payload as
`onComplete` through `event.detail`. Removing the element unmounts its React
tree; reconnecting it starts a fresh game. Multiple instances are independent.
Imports are safe during server rendering; call registration in the browser.

Both grids keep the same size on desktop and mobile, so their tiles can be compared directly.

## Scope and controls

This is a playable example rather than the complete Android campaign. It keeps
the puzzle rules and original artwork, and provides retry, new puzzle and three
difficulty shortcuts. It has no lives, audio, saved progression, backend or
analytics. A matching grid wins immediately. A failed attempt stays visible
until Retry is selected.

Use the mouse or touch, or navigate the playable grid with arrow keys and flip a
tile with Enter or Space. Home/End select the row endpoints; Ctrl+Home/End select
the board endpoints. Reduced-motion preferences are respected.

The core was ported from `Puzzle.java`, `LevelStage.java` and `Grid.java`.
Generation starts from a target and applies legal moves, preserving a known
solution. Repeated random draws and pathological large levels are bounded.

## Development

Node 24 and pnpm 12:

```sh
pnpm install --frozen-lockfile
pnpm dev
pnpm check
pnpm exec playwright install chromium
pnpm test:e2e
```

`pnpm build` creates `dist/library` and `dist/site`. React is a peer dependency.
The package embeds the small image assets so consumers do not need a public
asset folder or a separate asset host.

The manual **Publish Eyefox demo** GitHub Actions workflow publishes
`dist/site` from the `web` branch. Releases use a `web-v*` tag to keep web
package versions separate from the Android project.

## Credits and privacy

Code: Mathis Dubuisson, GPL-3.0-only. See [LICENSE](LICENSE) and
[third-party asset credits](THIRD_PARTY_NOTICES.md). The artwork retains its
original colours and is not covered by a new licensing claim.

The historical [Android privacy policy](https://westerbay.github.io/Eyefox-Puzzle/privacy.html)
is preserved and linked from the demo. The browser demo runs locally without
saving game state or sending gameplay data.
