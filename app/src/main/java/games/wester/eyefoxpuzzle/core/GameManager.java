package games.wester.eyefoxpuzzle.core;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import android.content.Context;

import games.wester.eyefoxpuzzle.puzzle.Game;
import games.wester.eyefoxpuzzle.puzzle.LevelStage;
import games.wester.eyefoxpuzzle.save.LevelSave;

/**
 * @author Wester
 */
public class GameManager {

    private static GameManager _instance = null;

    public static void createInstance(Context context) {
        if (_instance == null) {
            _instance = new GameManager(context);
        }
    }

    public static GameManager getInstance() {
        return _instance;
    }

    private final Game _game;
    private final Fox _fox;

    public GameManager(Context context) {
        _game = new Game(new LevelSave(context));
        _fox = new Fox(_game);
        _fox.animate();
    }

    public Fox getFox() {
        return _fox;
    }

    public int getLevel() {
        return _game.getLevel() + 1;
    }

    public void reset() {
        _game.reset();
    }

    public LevelStage getLevelStage() {
        return _game.getLevelStage();
    }

    public void loose() {
        _game.loose();
    }

    public void win() {
        _game.nextLevel();
    }

}
