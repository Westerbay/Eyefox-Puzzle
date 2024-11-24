package games.wester.westerlib.util;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

/**
 * Container for grid positions
 *
 * @author Wester
 */
public class Cell {

    public int rowIndex;
    public int columnIndex;

    public Cell(int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Cell)) {
            return false;
        }
        Cell cell = (Cell) object;
        return cell.rowIndex == rowIndex && cell.columnIndex == columnIndex;
    }

    @Override
    public int hashCode() {
        return rowIndex ^ columnIndex;
    }

    @Override
    public String toString() {
        return "(" + rowIndex + ", " + columnIndex + ")";
    }

}
