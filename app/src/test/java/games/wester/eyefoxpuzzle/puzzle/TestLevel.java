package games.wester.eyefoxpuzzle.puzzle;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Wester
 */
public class TestLevel {

    @Test
    public void test_level() {
        Puzzle puzzle = new Puzzle(5);
        puzzle.init();
        Puzzle clone = puzzle.copy();
        assertTrue(puzzle.equals(clone));
        puzzle.swap(0, 0);
        assertFalse(puzzle.equals(clone));
    }

}