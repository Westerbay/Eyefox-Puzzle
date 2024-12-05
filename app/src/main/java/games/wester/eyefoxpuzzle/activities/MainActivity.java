package games.wester.eyefoxpuzzle.activities;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import games.wester.eyefoxpuzzle.R;
import games.wester.eyefoxpuzzle.view.FoxView;
import games.wester.eyefoxpuzzle.view.LevelView;
import games.wester.eyefoxpuzzle.core.GameManager;
import games.wester.westerlib.core.Updatable;

/**
 * @author Wester
 */
public class MainActivity extends AppCompatActivity {

    public static MediaPlayer _music = null;

    private GameManager _gameAdaptation;
    private Handler _handler;
    private Runnable _imageChanger;
    private boolean _switch;
    private State _state = new InMainTitleState();

    interface State {
        void switchState();
    }

    class InMainTitleState implements State {
        public void switchState() {
            findViewById(R.id.play).setVisibility(View.GONE);
            findViewById(R.id.training).setVisibility(View.GONE);
            findViewById(R.id.option).setVisibility(View.GONE);
        }
    }

    class InOptionState implements State {
        public void switchState() {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.maintitle);
        _switch = false;
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
        _handler = new Handler(Looper.getMainLooper());
        if (_music == null) {
            _music = MediaPlayer.create(this, R.raw.game);
            _music.setLooping(true);
            _music.setVolume(0.8f, 0.8f);
            _music.start();
        }
        GameManager.createInstance(this);
        _gameAdaptation = GameManager.getInstance();
        initView();
        initButton();
    }

    public void initView() {
        FoxView _foxView = new FoxView(_gameAdaptation.getFox(), findViewById(R.id.fox));
        LevelView _levelView = new LevelView(findViewById(R.id.level), _gameAdaptation, getString(R.string.level));
        renderView(_foxView, _levelView);

    }

    public void renderView(Updatable foxView, Updatable levelView) {
        int interval = 1000 / 30;
        _imageChanger = new Runnable() {
            @Override
            public void run() {
                foxView.update();
                levelView.update();
                _handler.postDelayed(this, interval);
            }
        };
        _handler.post(_imageChanger);
    }

    public void initButton() {
        findViewById(R.id.play).setOnClickListener(
                v -> switchScene()
        );
        findViewById(R.id.option).setOnClickListener(
                v -> switchOption()
        );
    }

    public void switchScene() {
        _switch = true;
        _handler.removeCallbacks(_imageChanger);
        Intent intent = new Intent(MainActivity.this, Transition.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        MainActivity.this.startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void switchOption() {
        _state.switchState();
    }

    @Override
    public void finishAndRemoveTask() {
        super.finishAndRemoveTask();
        _music.stop();
        _music = null;
        System.exit(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (_music != null && _music.isPlaying() && !_switch) {
            _music.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (_music != null && !_music.isPlaying()) {
            _music.start();
        }
    }

}