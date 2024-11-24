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

/**
 * @author Wester
 */
public class StarsView {

    public StarsView(int level, ImageView... stars) {
        stars[0].setImageResource(R.drawable.star);
        for (int i = 0; i < (level - 1) % 10; i ++) {
            stars[i + 1].setImageResource(R.drawable.star);
        }
    }

}
