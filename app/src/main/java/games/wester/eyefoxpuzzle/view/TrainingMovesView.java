package games.wester.eyefoxpuzzle.view;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import android.widget.TextView;

import games.wester.eyefoxpuzzle.core.GameManager;
import games.wester.westerlib.core.Updatable;

/**
 * @author Wester
 */
public class TrainingMovesView implements Updatable {

    private final TextView _textView;
    private final GameManager _gameManager;
    private final String _initText;

    public TrainingMovesView(GameManager gameManager, TextView textView) {
        _textView = textView;
        _gameManager = gameManager;
        _initText = _textView.getText().toString();
    }

    @Override
    public void update() {
        String text = _initText + " " + _gameManager.getNumberOfMoves();
        _textView.setText(text);
    }

}
