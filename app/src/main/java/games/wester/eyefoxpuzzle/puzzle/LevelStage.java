package games.wester.eyefoxpuzzle.puzzle;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import games.wester.westerlib.util.Cell;

/**
 * @author Wester
 */
public class LevelStage {

    private Puzzle _originalPuzzle;
    private Puzzle _copyPuzzle;

    private int _numberOfTries = 3;
    private final int _level;
    private final int _numberOfMoves;
    private int _numberOfMovesLeft;

    public LevelStage(int level) {
        _level = level;
        _numberOfMoves = computeNumberOfMoves(level);
        _numberOfMovesLeft = _numberOfMoves;
        init();
    }

    public Puzzle getOriginalPuzzle() {
        return _originalPuzzle;
    }

    public Puzzle getCopyPuzzle() {
        return _copyPuzzle;
    }

    public int getNumberOfMoves() {
        return _numberOfMoves;
    }

    public int getNumberOfMovesLeft() {
        return _numberOfMovesLeft;
    }

    public int getNumberOfTries() {
        return _numberOfTries;
    }

    public void init() {
        Set<Cell> moves = new HashSet<>();
        Random random = new Random();
        int _landingAtLevel = 10;
        int size = (_level / _landingAtLevel) % 3 + 4;
        _originalPuzzle = new Puzzle(size);
        _originalPuzzle.init();
        _copyPuzzle = _originalPuzzle.copy();
        for (int i = 0; i < _numberOfMoves; i ++) {
            Cell cell;
            do {
                int rowIndex = random.nextInt(size);
                int rowColumn = random.nextInt(size);
                cell = new Cell(rowIndex, rowColumn);
            } while (moves.contains(cell));
            _copyPuzzle.swap(cell);
            moves.add(cell);
            if (moves.size() > 5) {
                moves.clear();
            }
        }
    }

    public void play() {
        _numberOfMovesLeft --;
    }

    public boolean hasWon() {
        return _originalPuzzle.equals(_copyPuzzle);
    }

    public boolean gameOver() {
        return _numberOfTries == 0;
    }

    public int computeNumberOfMoves(int level) {
        int nb = level / 30 + 1;
        if ((level + 1) % 10 == 0) {
            return nb + 1;
        }
        return nb;
    }

    public void reset() {
        _numberOfMovesLeft = _numberOfMoves;
        _numberOfTries --;
    }

}
