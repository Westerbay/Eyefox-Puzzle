package games.wester.eyefoxpuzzle.core;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import games.wester.eyefoxpuzzle.puzzle.Puzzle;
import games.wester.westerlib.core.Task;
import games.wester.westerlib.core.Timer;
import games.wester.westerlib.core.TimerLimit;
import games.wester.westerlib.util.Cell;
import games.wester.westerlib.util.Grid;

/**
 * @author Wester
 */
public class PuzzleManager implements Task {

    private final Puzzle _puzzle;
    private int _indexAnimation = 0;
    private final Timer _timer;
    private Set<Cell> _triggeredCell = new HashSet<>();
    private final LevelStageManager _levelStageManager;
    private final Stack<Cell> _moves = new Stack<>();
    private Player _soundEffect1;
    private Player _soundEffect2;

    public PuzzleManager(LevelStageManager levelStageManager, Puzzle puzzle) {
        _puzzle = puzzle;
        _timer = new TimerLimit(this, 100, 5);
        _levelStageManager = levelStageManager;
    }

    public Set<Cell> getTriggeredCell() {
        return _triggeredCell;
    }

    public int getSize() {
        return _puzzle.getSize();
    }

    public Grid<Boolean> getGrid() {
        return _puzzle;
    }

    public int getIndex() {
        return _indexAnimation;
    }

    public void setPlayerFirst(Player soundEffect) {
        _soundEffect1 = soundEffect;
    }

    public void setPlayerSecond(Player soundEffect) {
        _soundEffect2 = soundEffect;
    }

    public void trigger(Cell cell) {
        if (_levelStageManager.getLockedInput()) {
            return;
        }
        _soundEffect1.play();
        _levelStageManager.play();
        _moves.push(cell);
        _triggeredCell = _puzzle.computesNeighbors(cell);
        _triggeredCell.add(cell);
        _timer.setEndTask(() -> {
            _triggeredCell.clear();
            _indexAnimation = 0;
            _puzzle.swap(cell);
            _levelStageManager.check();
        });
        _timer.start();
    }

    public void reset(Task endTask) {
        while (!_moves.isEmpty()) {
            if (_timer.isRunning()) {
                continue;
            }
            Cell cell = _moves.pop();
            _soundEffect2.play();
            _triggeredCell = _puzzle.computesNeighbors(cell);
            _triggeredCell.add(cell);
            _timer.setEndTask(() -> {
                _triggeredCell.clear();
                _indexAnimation = 0;
                _puzzle.swap(cell);
            });
            _timer.start();
            new TimerLimit(() -> {}, 300).run();
        }
        endTask.run();
    }

    @Override
    public void run() {
        _indexAnimation ++;
    }

}
