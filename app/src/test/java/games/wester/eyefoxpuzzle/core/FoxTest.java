package games.wester.eyefoxpuzzle.core;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import games.wester.eyefoxpuzzle.puzzle.Game;
import games.wester.eyefoxpuzzle.puzzle.GameTest;
import games.wester.eyefoxpuzzle.puzzle.ILevelSave;

/**
 * @author Wester
 */
public class FoxTest {

    private Fox fox = null;

    public void initWithLevel(int level) {
        ILevelSave levelSave = new GameTest.MockLevelStage();
        levelSave.saveLevel(level);
        Game game = new Game(levelSave);
        fox = new Fox(game);
        fox.updateState();
    }

    @Test
    public void number_of_moves_one_should_be_easy_state() {
        initWithLevel(8);
        assertEquals(Fox.State.EASY, fox.getState());
        initWithLevel(28);
        assertEquals(Fox.State.EASY, fox.getState());
    }

    @Test
    public void number_of_moves_at_least_3_should_be_hard_state() {
        initWithLevel(39);
        assertEquals(Fox.State.HARD, fox.getState());
        initWithLevel(100);
        assertEquals(Fox.State.HARD, fox.getState());
    }

    @Test
    public void number_of_moves_two_should_be_medium_state() {
        initWithLevel(9);
        assertEquals(Fox.State.MEDIUM, fox.getState());
        initWithLevel(35);
        assertEquals(Fox.State.MEDIUM, fox.getState());
    }

}
