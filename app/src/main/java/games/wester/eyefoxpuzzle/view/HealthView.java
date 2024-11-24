package games.wester.eyefoxpuzzle.view;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import android.view.View;
import android.widget.ImageView;

import games.wester.eyefoxpuzzle.core.LevelStageManager;
import games.wester.westerlib.core.Updatable;

/**
 * @author Wester
 */
public class HealthView implements Updatable {

    private final LevelStageManager _levelStageManager;
    private final ImageView _firstHealth;
    private final ImageView _secondHealth;
    private final ImageView _thirdHealth;

    public HealthView(LevelStageManager levelStageManager, ImageView firstHealth, ImageView secondHealth, ImageView thirdHealth) {
        _levelStageManager = levelStageManager;
        _firstHealth = firstHealth;
        _secondHealth = secondHealth;
        _thirdHealth = thirdHealth;
    }

    @Override
    public void update() {
        switch (_levelStageManager.getNumberOfTries()) {
            case 1:
                _firstHealth.setVisibility(View.GONE);
                _secondHealth.setVisibility(View.GONE);
                _thirdHealth.setVisibility(View.VISIBLE);
                break;
            case 2:
                _firstHealth.setVisibility(View.GONE);
                _secondHealth.setVisibility(View.VISIBLE);
                _thirdHealth.setVisibility(View.VISIBLE);
                break;
            case 3:
                _firstHealth.setVisibility(View.VISIBLE);
                _secondHealth.setVisibility(View.VISIBLE);
                _thirdHealth.setVisibility(View.VISIBLE);
                break;
            default:
                _firstHealth.setVisibility(View.GONE);
                _secondHealth.setVisibility(View.GONE);
                _thirdHealth.setVisibility(View.GONE);
        }

    }

}
