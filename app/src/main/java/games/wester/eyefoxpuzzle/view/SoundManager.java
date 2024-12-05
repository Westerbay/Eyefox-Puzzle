package games.wester.eyefoxpuzzle.view;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;

import games.wester.eyefoxpuzzle.R;
import games.wester.eyefoxpuzzle.save.OptionSave;

/**
 * @author Wester
 */
public class SoundManager {

    @SuppressLint("StaticFieldLeak")
    private static SoundManager _soundManager = null;

    public static SoundManager create(Context context) {
        if (_soundManager == null) {
            _soundManager = new SoundManager(context);
        }
        return _soundManager;
    }

    private final MediaPlayer _music;
    private final OptionSave _optionSave;
    private int _actualLevel;
    private final MediaPlayer _correctSound;
    private final MediaPlayer _wrongSound;
    private final Context _context;

    private SoundManager(Context context) {
        _correctSound = MediaPlayer.create(context, R.raw.correct);
        _wrongSound = MediaPlayer.create(context, R.raw.wrong);
        _optionSave = new OptionSave(context);
        _music = MediaPlayer.create(context, R.raw.game);
        _music.setLooping(true);
        _actualLevel = _optionSave.loadLevel();
        changeVolume(_actualLevel);
        _music.start();
        _context = context;
    }

    private float getFloatLevel() {
        return _actualLevel / 100.0f;
    }

    public void changeVolume(int value) {
        _optionSave.saveLevel(value);
        _actualLevel = value;
        float volume = 0.8f * getFloatLevel();
        _music.setVolume(volume, volume);
        volume = 0.2f * getFloatLevel();
        _wrongSound.setVolume(volume, volume);
        _correctSound.setVolume(volume, volume);
    }

    public void startCorrectSound() {
        _correctSound.start();
    }

    public void startWrongSound() {
        _wrongSound.start();
    }

    public MediaPlayer getMusic() {
        return _music;
    }

    public int getLevel() {
        return _actualLevel;
    }

    public void startInterchange1() {
        MediaPlayer mp = MediaPlayer.create(_context, R.raw.interchange1);
        float volume = 0.5f * getFloatLevel();
        if (mp != null) {
            mp.setVolume(volume, volume);
            mp.start();
        }
    }

    public void startInterchange2() {
        MediaPlayer mp = MediaPlayer.create(_context, R.raw.interchange2);
        float volume = 0.5f * getFloatLevel();
        if (mp != null) {
            mp.setVolume(volume, volume);
            mp.start();
        }
    }

}
