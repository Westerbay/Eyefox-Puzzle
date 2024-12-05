package games.wester.eyefoxpuzzle.view;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import android.content.Context;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.gridlayout.widget.GridLayout;

import games.wester.eyefoxpuzzle.R;
import games.wester.eyefoxpuzzle.core.PuzzleManager;
import games.wester.westerlib.core.Updatable;
import games.wester.westerlib.util.Cell;
import games.wester.westerlib.util.Grid;

/**
 * @author Wester
 */
public class GridView implements Updatable {

    private final int[] _transition1To0 = {
            R.drawable.full_cell, R.drawable.full_cell1, R.drawable.full_cell2,
            R.drawable.empty_cell2, R.drawable.empty_cell1, R.drawable.empty_cell
    };
    private final int[] _transition0To1 = {
            R.drawable.empty_cell, R.drawable.empty_cell1, R.drawable.empty_cell2,
            R.drawable.full_cell2, R.drawable.full_cell1, R.drawable.full_cell
    };

    private final Grid<Button> _grid;
    private final Grid<Boolean> _referredGrid;
    private final PuzzleManager _puzzleManager;
    private final SoundManager _soundManager;

    public GridView(Context context, PuzzleManager puzzleManager, GridLayout gridLayout, boolean interact) {
        _puzzleManager = puzzleManager;
        _soundManager = SoundManager.create(context);
        _referredGrid = puzzleManager.getGrid();
        int _size = puzzleManager.getSize();
        _grid = new Grid<>(new Button[_size][_size]);
        gridLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gridLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                gridLayout.getLayoutParams().height = gridLayout.getWidth();
                gridLayout.requestLayout();
            }
        });
        fill(context, puzzleManager, gridLayout, interact);
    }

    private void fill(Context context, PuzzleManager puzzleManager, GridLayout gridLayout, boolean interact) {
        for (Cell cell: _referredGrid.forEachCells()) {
            Button newButton = new Button(context);
            if (interact) {
                newButton.setOnClickListener(
                        v -> puzzleManager.trigger(cell)
                );
            }
            else {
                newButton.setFocusable(false);
            }
            if (_referredGrid.get(cell)) {
                newButton.setBackgroundResource(R.drawable.full_cell);
            }
            else {
                newButton.setBackgroundResource(R.drawable.empty_cell);
            }
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = 0;
            params.rowSpec = GridLayout.spec(cell.rowIndex, 1, 1f);
            params.columnSpec = GridLayout.spec(cell.columnIndex, 1, 1f);
            newButton.setLayoutParams(params);
            _grid.set(cell, newButton);
            Animation blinkAnimation = AnimationUtils.loadAnimation(context, R.anim.blink_animation);
            newButton.setOnFocusChangeListener((view, hasFocus) -> {
                if (hasFocus) {
                    view.startAnimation(blinkAnimation);
                } else {
                    view.clearAnimation();
                }
            });
            gridLayout.addView(newButton);
        }
        _puzzleManager.setPlayerFirst(this::play1);
        _puzzleManager.setPlayerSecond(this::play2);
    }

    @Override
    public void update() {
        int index = _puzzleManager.getIndex();
        for (Cell cell: _puzzleManager.getTriggeredCell()) {
            if (_referredGrid.get(cell)) {
                _grid.get(cell).setBackgroundResource(_transition1To0[index]);
            }
            else {
                _grid.get(cell).setBackgroundResource(_transition0To1[index]);
            }
        }
    }

    public void play1() {
        _soundManager.startInterchange1();
    }

    public void play2() {
        _soundManager.startInterchange2();
    }
}
