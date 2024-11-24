package games.wester.eyefoxpuzzle.puzzle;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import java.util.Random;

import games.wester.westerlib.util.Grid;
import games.wester.westerlib.util.Cell;

/**
 * @author Wester
 */
public class Puzzle extends Grid<Boolean> {

	private final int _size;
	
	public Puzzle(int size) {
		super(new Boolean[size][size]);
		_size = size;
	}

	public int getSize() {
		return _size;
	}

	public void init() {
		Random random = new Random();
		fill(() -> random.nextInt(2) == 1);
	}

	public void swap(int rowIndex, int columnIndex) {
		for (Cell cell: computesNeighbors(rowIndex, columnIndex)) {
			set(cell, !get(cell));
		}
		set(rowIndex, columnIndex, !get(rowIndex, columnIndex));
	}

	public void swap(Cell cell) {
		swap(cell.rowIndex, cell.columnIndex);
	}

	public Puzzle copy() {
		Puzzle clone = new Puzzle(getNumberOfRows());
		for (Cell cell: forEachCells()) {
			clone.set(cell, get(cell));
		}
		return clone;
	}

}

