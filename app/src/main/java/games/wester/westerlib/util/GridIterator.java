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
 * Grid iterator
 *
 * @author Wester
 */
public class GridIterator<E> implements Iterator<E> {

    private final Grid<E> _grid;
    private int currentIndex = 0;

    public GridIterator(Grid<E> grid) {
        _grid = grid;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < _grid.getNumberOfElements();
    }

    @Override
    public E next() {
        return _grid.get(currentIndex ++);
    }

}
