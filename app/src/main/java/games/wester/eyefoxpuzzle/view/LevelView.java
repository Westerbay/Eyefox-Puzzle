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
public class LevelView implements Updatable {

    private final TextView _textView;
    private final GameManager _gameAdaptation;
    private final String _text;

    public LevelView(TextView textView, GameManager gameAdaptation, String text) {
        _textView = textView;
        _gameAdaptation = gameAdaptation;
        _text = text;
    }

    @Override
    public void update() {
        StringBuilder textLevel = new StringBuilder(_text);
        textLevel.append(" : ").append(_gameAdaptation.getLevel());
        _textView.setText(textLevel);
    }

}
