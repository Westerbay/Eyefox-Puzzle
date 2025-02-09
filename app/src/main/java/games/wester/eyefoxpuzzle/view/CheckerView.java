package games.wester.eyefoxpuzzle.view;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;

import games.wester.eyefoxpuzzle.R;
import games.wester.eyefoxpuzzle.core.LevelStageManager;
import games.wester.westerlib.core.Updatable;

/**
 * @author Wester
 */
public class CheckerView implements Updatable {

    private final LevelStageManager _levelStageManager;
    private final ImageView _correct;
    private final ImageView _wrong;
    private final SoundManager _soundManager;

    public CheckerView(Context context, LevelStageManager levelStageManager, ImageView correct, ImageView wrong) {
        _soundManager = SoundManager.create(context);

        _levelStageManager = levelStageManager;
        _correct = correct;
        _wrong = wrong;
        levelStageManager.setCorrectPlayer(this::playCorrect);
        levelStageManager.setWrongPlayer(this::playWrong);
    }

    @Override
    public void update() {
        if (_levelStageManager.getCorrect()) {
            _correct.setVisibility(View.VISIBLE);
        }
        else if (_levelStageManager.getWrong()) {
            _wrong.setVisibility(View.VISIBLE);
        }
        else {
            _wrong.setVisibility(View.GONE);
            _correct.setVisibility(View.GONE);
        }
    }

    public void playCorrect() {
        _soundManager.startCorrectSound();
    }

    public void playWrong() {
        _soundManager.startWrongSound();
    }

}
