package games.wester.westerlib.util;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import java.util.Iterator;

/**
 * Iterate grid with cells
 *
 * @author Wester
 */
public class CellGridIterator implements Iterator<Cell>, Iterable<Cell> {

    private final Grid<?> _grid;
    private int currentIndex = 0;

    public CellGridIterator(Grid<?> grid) {
        _grid = grid;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < _grid.getNumberOfElements();
    }

    @Override
    public Cell next() {
        int rowIndex = currentIndex / _grid.getNumberOfColumns();
        int columnIndex = currentIndex % _grid.getNumberOfColumns();
        currentIndex ++;
        return new Cell(rowIndex, columnIndex);
    }

    @Override
    public Iterator<Cell> iterator() {
        return this;
    }

}
