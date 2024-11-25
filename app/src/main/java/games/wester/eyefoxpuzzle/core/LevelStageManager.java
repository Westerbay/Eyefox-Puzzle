package games.wester.eyefoxpuzzle.core;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import games.wester.eyefoxpuzzle.puzzle.LevelStage;
import games.wester.westerlib.core.Task;
import games.wester.westerlib.core.TimerLimit;

/**
 * @author Wester
 */
public class LevelStageManager {

    private final LevelStage _levelStage;
    private boolean _lockedInput = false;
    private PuzzleManager _leftPuzzleManager;
    private PuzzleManager _rightPuzzleManager;
    private boolean _wrong = false;
    private boolean _correct = false;
    private final Task _winTask;
    private final Task _looseTask;
    private Player _correctPlayer;
    private Player _wrongPlayer;

    public LevelStageManager(LevelStage levelStage, Task winTask, Task looseTask) {
        _levelStage = levelStage;
        _winTask = winTask;
        _looseTask = looseTask;
    }

    public boolean getLockedInput() {
        return _lockedInput;
    }

    public int getNumberOfMovesLeft() {
        return _levelStage.getNumberOfMovesLeft();
    }

    public int getNumberOfTries() {
        return _levelStage.getNumberOfTries();
    }

    public void setLeftPuzzleManager(PuzzleManager leftPuzzleManager) {
        _leftPuzzleManager = leftPuzzleManager;
    }

    public void setRightPuzzleManager(PuzzleManager rightPuzzleManager) {
        _rightPuzzleManager = rightPuzzleManager;
    }

    public void setCorrectPlayer(Player player) {
        _correctPlayer = player;
    }

    public void setWrongPlayer(Player player) {
        _wrongPlayer = player;
    }

    public boolean getWrong() {
        return _wrong;
    }

    public boolean getCorrect() {
        return _correct;
    }

    public void play() {
        _lockedInput = true;
        _levelStage.play();
    }

    public void check() {
        if (_levelStage.getNumberOfMovesLeft() == 0) {
            checkForWin();
        }
        else {
            _lockedInput = false;
        }
    }

    private void checkForWin() {
        new TimerLimit(() -> {}, 200).run();
        if (_levelStage.hasWon()) {
            _correctPlayer.play();
            _correct = true;
            new TimerLimit(_winTask, 400).start();
        }
        else {
            _wrongPlayer.play();
            _wrong = true;
            new TimerLimit(() -> {
                _wrong = false;
                _leftPuzzleManager.reset(() -> {});
                _rightPuzzleManager.reset(() -> {
                    _levelStage.reset();
                    new TimerLimit(this::checkGameOver, 350).start();
                });
            }, 700).start();
        }
    }

    public void checkGameOver() {
        if (_levelStage.gameOver()) {
            _looseTask.run();
        }
        else {
            _lockedInput = false;
        }
    }

}
