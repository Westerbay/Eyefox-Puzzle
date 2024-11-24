package games.wester.westerlib.util;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

public class CellTest {

    private Cell cell;

    @Before
    public void setUp() {
        cell = new Cell(1, 2);
    }

    @Test
    public void different_position_should_not_be_equals() {
        Cell other = new Cell(1, 1);
        assertNotEquals(other, cell);
    }

    @Test
    public void same_position_should_be_equals() {
        Cell other = new Cell(1, 2);
        assertEquals(other, cell);
    }

}
