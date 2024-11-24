package games.wester.eyefoxpuzzle.puzzle;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Wester
 */
public class GameTest {

    private Game game;

    public static class MockLevelStage implements ILevelSave {
        private int _level = 10;
        @Override
        public void saveLevel(int level) {
            _level = level;
        }
        @Override
        public int loadLevel() {
            return _level;
        }
    }

    @Before
    public void setUp() {
        game = new Game(new MockLevelStage());
    }

    @Test
    public void loose_should_ten_levels() {
        game.loose();
        assertEquals(0, game.getLevel());
    }

    @Test
    public void loose_while_behind_ten_should_not_go_to_negative() {
        game.loose();
        game.loose();
        assertEquals(0, game.getLevel());
    }

    @Test
    public void reset_should_save_zero_for_level() {
        game.reset();
        assertEquals(0, game.getLevel());
    }

}
