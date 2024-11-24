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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Wester
 */
public class PuzzleTest {

    private Puzzle puzzle;

    @Before
    public void setUp() {
        puzzle = new Puzzle(4);
        puzzle.init();
    }

    @Test
    public void copy_should_be_equals_in_values() {
        Puzzle copy = puzzle.copy();
        assertEquals(copy, puzzle);
    }

    @Test
    public void copy_should_be_different_in_reference() {
        Puzzle copy = puzzle.copy();
        assertNotSame(copy, puzzle);
    }

    @Test
    public void swap_should_revert_neighbors() {
        Puzzle copy = puzzle.copy();
        copy.swap(0, 0);
        assertNotEquals(copy.get(0, 0), puzzle.get(0, 0));
        assertNotEquals(copy.get(1, 0), puzzle.get(1, 0));
        assertNotEquals(copy.get(0, 1), puzzle.get(0, 1));
        assertNotEquals(copy.get(1, 1), puzzle.get(1, 1));
    }

}
