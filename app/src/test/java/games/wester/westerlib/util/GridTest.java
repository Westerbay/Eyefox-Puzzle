package games.wester.westerlib.util;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

/**
 * @author Wester
 */
public class GridTest {

    private Grid<Integer> grid;

    @Before
    public void setUp() {
        grid = new Grid<>(new Integer[2][2]);
    }

    @Test
    public void fill_should_fill_every_cells() {
        grid.fill(3);
        assertEquals(Integer.valueOf(3), grid.get(0));
        assertEquals(Integer.valueOf(3), grid.get(3));
    }

    @Test
    public void fill_with_class_generator_should_fill_every_cells() {
        grid.fill(() -> 3);
        assertEquals(Integer.valueOf(3), grid.get(0));
        assertEquals(Integer.valueOf(3), grid.get(3));
    }

    @Test
    public void cell_outside_grid_is_not_contained() {
        assertFalse(grid.contains(-3, 1));
        assertFalse(grid.contains(0, 2));
    }

    @Test
    public void cell_inside_grid_is_contained() {
        assertTrue(grid.contains(0, 0));
        assertTrue(grid.contains(1, 1));
    }

    @Test
    public void computes_neighbor_should_not_contains_the_cell() {
        Set<Cell> neighbors = grid.computesNeighbors(0, 0);
        assertTrue(neighbors.contains(new Cell(0, 1)));
        assertTrue(neighbors.contains(new Cell(1, 1)));
        assertTrue(neighbors.contains(new Cell(1, 0)));
        assertFalse(neighbors.contains(new Cell(0, 0)));
    }

    @Test
    public void equality_is_matrix_equality() {
        grid.fill(9);
        Grid<Integer> other = new Grid<>(new Integer[2][2], 9);
        assertEquals(grid, other);
        assertNotSame(grid, other);
    }

}
