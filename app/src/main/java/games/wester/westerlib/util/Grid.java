package games.wester.westerlib.util;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Manipulation of two dimensional arrays
 *
 * @author Wester
 */
public class Grid<E> implements Iterable<E> {

    private final int _numberOfRows;
    private final int _numberOfColumns;
    private final E[][] _matrix;

    public Grid(E[][] matrix) {
        _numberOfRows = matrix.length;
        _numberOfColumns = matrix[0].length;
        _matrix = matrix;
    }

    public Grid(E[][] matrix, E valueToFill) {
        this(matrix);
        fill(valueToFill);
    }

    public Grid(E[][] matrix, ClassGenerator<E> classGenerator) {
        this(matrix);
        fill(classGenerator);
    }

    public void fill(E valueToFill) {
        for (int i = 0; i < _numberOfRows; i ++) {
            for (int j = 0; j < _numberOfColumns; j ++) {
                _matrix[i][j] = valueToFill;
            }
        }
    }

    public void fill(ClassGenerator<E> classGenerator) {
        for (int i = 0; i < _numberOfRows; i ++) {
            for (int j = 0; j < _numberOfColumns; j ++) {
                _matrix[i][j] = classGenerator.generate();
            }
        }
    }

    public int getNumberOfRows() {
        return _numberOfRows;
    }

    public int getNumberOfColumns() {
        return _numberOfColumns;
    }

    public int getNumberOfElements() {
        return getNumberOfRows() * getNumberOfColumns();
    }

    public E[][] getMatrix() {
        return _matrix;
    }

    public E get(int numberOfCell) {
        return get(numberOfCell / _numberOfColumns, numberOfCell % _numberOfColumns);
    }

    public E get(int indexRow, int indexColumn) {
        if (!contains(indexRow, indexColumn)) {
            throw new IndexOutOfBoundsException();
        }
        return _matrix[indexRow][indexColumn];
    }

    public E get(Cell cell) {
        return get(cell.rowIndex, cell.columnIndex);
    }

    public void set(int numberOfCell, E value) {
        set(numberOfCell / _numberOfColumns, numberOfCell % _numberOfColumns, value);
    }

    public void set(int indexRow, int indexColumn, E value) {
        _matrix[indexRow][indexColumn] = value;
    }

    public void set(Cell cell, E value) {
        if (!contains(cell)) {
            throw new IndexOutOfBoundsException();
        }
        set(cell.rowIndex, cell.columnIndex, value);
    }

    public boolean contains(int indexRow, int indexColumn) {
        if (indexRow >= _numberOfRows || indexRow < 0) {
            return false;
        }
        return indexColumn < _numberOfColumns && indexColumn >= 0;
    }

    public boolean contains(Cell cell) {
        if (cell.rowIndex >= _numberOfRows || cell.rowIndex < 0) {
            return false;
        }
        return cell.columnIndex < _numberOfColumns && cell.columnIndex >= 0;
    }

    public Set<Cell> computesNeighbors(int rowIndex, int columnIndex) {
        Set<Cell> neighbors = new HashSet<>();
        for (int i = -1; i < 2; i ++) {
            for (int j = -1; j < 2; j ++) {
                int rowIndexNeighbor = rowIndex + i;
                int columnIndexNeighbor = columnIndex + j;
                if (i == 0 && j == 0) {
                    continue;
                }
                if (contains(rowIndexNeighbor, columnIndexNeighbor)) {
                    neighbors.add(new Cell(rowIndexNeighbor, columnIndexNeighbor));
                }
            }
        }
        return neighbors;
    }

    public Set<Cell> computesNeighbors(Cell cell) {
        return computesNeighbors(cell.rowIndex, cell.columnIndex);
    }

    @Override
    public Iterator<E> iterator() {
        return new GridIterator<>(this);
    }

    public Iterable<Cell> forEachCells() {
        return new CellGridIterator(this);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Grid)) {
            return false;
        }
        Grid<?> other = (Grid<?>) object;
        return Arrays.deepEquals(_matrix, other.getMatrix());
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(_matrix);
    }

    @Override
    public String toString() {
        StringBuilder representation = new StringBuilder();
        for (int i = 0; i < _numberOfRows; i ++) {
            representation.append("[");
            for (int j = 0; j < _numberOfColumns; j ++) {
                if (j == _numberOfColumns - 1) {
                    representation.append(_matrix[i][j]);
                }
                else {
                    representation.append(_matrix[i][j]).append(" ");
                }
            }
            representation.append("]\n");
        }
        return representation.toString();
    }

}
