package games.wester.eyefoxpuzzle.core;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import games.wester.eyefoxpuzzle.puzzle.Game;
import games.wester.westerlib.core.Timer;
import games.wester.westerlib.core.TimerIndefinite;

/**
 * @author Wester
 */
public class Fox {

    private final Timer _animation;
    private int _indexOfImages = 0;
    private State _state = State.EASY;
    private final Game _game;

    public enum State {
        EASY, MEDIUM, HARD
    }

    public Fox(Game game) {
        _animation = new TimerIndefinite(this::switchAnimation, 200);
        _game = game;
    }

    public int getIndexOfImages() {
        return _indexOfImages;
    }

    public State getState() {
        return _state;
    }

    public void switchAnimation() {
        updateState();
        _indexOfImages = (_indexOfImages + 1) % 30;
    }

    public void animate() {
        _animation.start();
    }

    public void updateState() {
        switch (_game.getLevelStage().getNumberOfMoves()) {
            case 1:
                _state = State.EASY;
                break;
            case 2:
                _state = State.MEDIUM;
                break;
            default:
                _state = State.HARD;
        }
    }

}
