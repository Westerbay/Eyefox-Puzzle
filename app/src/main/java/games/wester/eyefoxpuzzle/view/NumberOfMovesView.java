package games.wester.eyefoxpuzzle.view;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import android.view.View;
import android.widget.TextView;

import games.wester.eyefoxpuzzle.core.LevelStageManager;
import games.wester.westerlib.core.Updatable;

/**
 * @author wester
 */
public class NumberOfMovesView implements Updatable {

    private final TextView _textView;
    private final LevelStageManager _levelStageManager;

    public NumberOfMovesView(LevelStageManager levelStageManager, TextView textView) {
        _textView = textView;
        _levelStageManager = levelStageManager;
        textView.setVisibility(View.VISIBLE);
    }

    @Override
    public void update() {
        String text = _levelStageManager.getNumberOfMovesLeft() + "";
        _textView.setText(text);
    }

}
