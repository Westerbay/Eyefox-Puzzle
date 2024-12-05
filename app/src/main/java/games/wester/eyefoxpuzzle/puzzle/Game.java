package games.wester.eyefoxpuzzle.puzzle;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

/**
 * @author Wester
 */
public class Game {

    private final ILevelSave _levelSave;
    private LevelStage _levelStage;
    private int _level;

    public Game(ILevelSave levelSave) {
        _levelSave = levelSave;
        _level = levelSave.loadLevel();
        _levelStage = new LevelStage(_level);
    }

    public int getLevel() {
        return _level;
    }

    public LevelStage getLevelStage() {
        return _levelStage;
    }

    public void loose() {
        if (_level % 10 != 0) {
            _level = (_level / 10) * 10;
        }
        else {
            _level -= 10;
        }
        if (_level < 0) {
            _level = 0;
        }
        _levelSave.saveLevel(_level);
        _levelStage = new LevelStage(_level);
    }

    public void nextLevel() {
        _levelSave.saveLevel(++ _level);
        _levelStage = new LevelStage(_level);
    }

    public void reset() {
        _level = 0;
        _levelSave.saveLevel(0);
        _levelStage = new LevelStage(_level);
    }

}
