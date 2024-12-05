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

import games.wester.eyefoxpuzzle.R;
import games.wester.eyefoxpuzzle.save.OptionSave;

/**
 * @author Wester
 */
public class SoundManager {

    private static SoundManager _soundManager = null;

    public static SoundManager create(Context context) {
        if (_soundManager == null) {
            _soundManager = new SoundManager(context);
        }
        return _soundManager;
    }

    private final MediaPlayer _music;
    private final OptionSave _optionSave;
    private final float _defaultMusicVolume = 0.8f;
    private int _actualLevel;

    private SoundManager(Context context) {
        _optionSave = new OptionSave(context);
        _music = MediaPlayer.create(context, R.raw.game);
        _music.setLooping(true);
        _actualLevel = _optionSave.loadLevel();
        changeVolume(_actualLevel);
        _music.start();
    }

    public void changeVolume(int value) {
        _optionSave.saveLevel(value);
        _actualLevel = value;
        float valueToFloat = value / 100.0f;
        float volume = _defaultMusicVolume * valueToFloat;
        _music.setVolume(volume, volume);
    }

    public MediaPlayer getMusic() {
        return _music;
    }

    public int getLevel() {
        return _actualLevel;
    }

}
