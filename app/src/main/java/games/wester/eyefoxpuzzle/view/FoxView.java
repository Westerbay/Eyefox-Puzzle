package games.wester.eyefoxpuzzle.view;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import android.widget.ImageView;

import games.wester.eyefoxpuzzle.R;
import games.wester.eyefoxpuzzle.core.Fox;
import games.wester.westerlib.core.Updatable;

/**
 * @author Wester
 */
public class FoxView implements Updatable {

    private final Fox _fox;
    private final ImageView _imageView;

    private final int[] _imagesEasyID = {
            R.drawable.sleeping1, R.drawable.sleeping2, R.drawable.sleeping3,
            R.drawable.sleeping4, R.drawable.sleeping5, R.drawable.sleeping6
    };
    private final int[] _imagesMediumID = {
            R.drawable.awake1, R.drawable.awake2, R.drawable.awake3,
            R.drawable.awake4, R.drawable.awake5
    };
    private final int[] _imagesHardID = {
            R.drawable.stand1, R.drawable.stand2, R.drawable.stand3,
            R.drawable.stand4, R.drawable.stand5
    };

    public FoxView(Fox fox, ImageView imageView) {
        _fox = fox;
        _imageView = imageView;
    }

    @Override
    public void update() {
        switch (_fox.getState()) {
            case EASY:
                _imageView.setImageResource(_imagesEasyID[_fox.getIndexOfImages() % 6]);
                break;
            case MEDIUM:
                _imageView.setImageResource(_imagesMediumID[_fox.getIndexOfImages() % 5]);
                break;
            case HARD:
                _imageView.setImageResource(_imagesHardID[_fox.getIndexOfImages() % 5]);
                break;
        }
    }

}
