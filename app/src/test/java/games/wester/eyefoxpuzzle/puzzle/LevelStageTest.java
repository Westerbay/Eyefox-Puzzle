package games.wester.eyefoxpuzzle.puzzle;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Wester
 */
public class LevelStageTest {

    private LevelStage levelStage;

    @Before
    public void setUp() {
        levelStage = new LevelStage(10);
    }

    @Test
    public void move_left_should_not_be_game_over() {
        assertFalse(levelStage.gameOver());
        levelStage.reset();
        assertFalse(levelStage.gameOver());
    }

    @Test
    public void no_more_move_left_should_be_game_over() {
        levelStage.reset();
        levelStage.reset();
        levelStage.reset();
        assertTrue(levelStage.gameOver());
    }

    @Test
    public void compute_good_number_of_moves() {
        assertEquals(1, levelStage.computeNumberOfMoves(0));
        assertEquals(2, levelStage.computeNumberOfMoves(9));
        assertEquals(1, levelStage.computeNumberOfMoves(10));

        assertEquals(1, levelStage.computeNumberOfMoves(28));
        assertEquals(2, levelStage.computeNumberOfMoves(29));
        assertEquals(2, levelStage.computeNumberOfMoves(30));

        assertEquals(2, levelStage.computeNumberOfMoves(88));
        assertEquals(3, levelStage.computeNumberOfMoves(89));
        assertEquals(3, levelStage.computeNumberOfMoves(90));
    }

}
