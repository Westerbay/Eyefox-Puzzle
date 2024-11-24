package games.wester.westerlib.util;

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
public class GridIteratorTest {

    private Grid<Integer> grid;

    @Before
    public void setUp() {
        grid = new Grid<>(new Integer[2][2], 3);
    }

    @Test
    public void iterator_iterate_every_values() {
        int nb = 0;
        for (int value: grid) {
            assertEquals(3, value);
            nb ++;
        }
        assertEquals(4, nb);
    }

    @Test
    public void iterator_cell_iterate_every_values() {
        Grid<Integer> other = new Grid<>(new Integer[2][2]);
        for (Cell cell: other.forEachCells()) {
            other.set(cell, 3);
        }
        assertEquals(other, grid);
    }

}
