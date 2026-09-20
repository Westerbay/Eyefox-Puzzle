# Agent instructions

The `web` branch maintains the reusable React Eyefox Puzzle component and its standalone demo. The Android source stays on `main`.

Preserve the original rules from `Puzzle.java`, `LevelStage.java` and `Grid.java`: a move flips the selected tile and all adjacent tiles, including diagonals. Keep generation solvable and bounded. The web demo omits the Android campaign, lives, audio and persistence.

Keep styles scoped, imports safe during server rendering, French and English strings, keyboard controls and light/dark support. The optional custom element must isolate styles and clean up React on disconnect. Preserve asset credits, the GPL license and the Android privacy-policy page.

Run formatting, types, core tests, library/demo builds and browser checks. Verify the packed package in the portfolio before publishing. Documentation and scoped commits are in English. Pages publication uses the manual workflow on this branch.

Do not preview affected tiles on hover or focus. Players should infer the move's effect themselves. Keep the keyboard focus ring on the active tile for navigation.
