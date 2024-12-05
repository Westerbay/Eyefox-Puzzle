package games.wester.eyefoxpuzzle.activities;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import games.wester.eyefoxpuzzle.R;
import games.wester.eyefoxpuzzle.view.FoxView;
import games.wester.eyefoxpuzzle.view.LevelView;
import games.wester.eyefoxpuzzle.core.GameManager;
import games.wester.eyefoxpuzzle.view.SoundManager;
import games.wester.westerlib.core.Updatable;

/**
 * @author Wester
 */
public class MainActivity extends AppCompatActivity {

    private GameManager _gameAdaptation;
    private Handler _handler;
    private Runnable _imageChanger;
    private AlertDialog _confirmation;
    private SoundManager _soundManager;
    private boolean _switch;
    private State _state = new InMainTitleState();

    abstract class State {
        public abstract void displayElements(int mode);
        void switchState(State state) {
            displayElements(View.GONE);
            state.displayElements(View.VISIBLE);
            _state = state;
        }
    }

    class InMainTitleState extends State {
        @Override
        public void displayElements(int mode) {
            findViewById(R.id.play).setVisibility(mode);
            findViewById(R.id.training).setVisibility(mode);
            findViewById(R.id.option).setVisibility(mode);
            findViewById(R.id.textPlay).setVisibility(mode);
            findViewById(R.id.textTraining).setVisibility(mode);
            findViewById(R.id.textOption).setVisibility(mode);
        }
    }

    class InOptionState extends State {
        @Override
        public void displayElements(int mode) {
            findViewById(R.id.textMusic).setVisibility(mode);
            findViewById(R.id.soundBar).setVisibility(mode);
            findViewById(R.id.reset).setVisibility(mode);
            findViewById(R.id.textReset).setVisibility(mode);
            findViewById(R.id.homeButton).setVisibility(mode);
            findViewById(R.id.textHome).setVisibility(mode);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.maintitle);
        _soundManager = SoundManager.create(this);
        _switch = false;
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
        _handler = new Handler(Looper.getMainLooper());
        GameManager.createInstance(this);
        _gameAdaptation = GameManager.getInstance();
        initConfirmation();
        initView();
        initButton();
        initProgressBar();
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
                v -> _state.switchState(new InOptionState())
        );
        findViewById(R.id.homeButton).setOnClickListener(
                v -> _state.switchState(new InMainTitleState())
        );
        findViewById(R.id.reset).setOnClickListener(
                v -> _confirmation.show()
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

    private void initProgressBar() {
        SeekBar seekBar = findViewById(R.id.soundBar);
        seekBar.setProgress(_soundManager.getLevel());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Bounds
                if (seekBar.getProgress() >= 93) {
                    seekBar.setProgress(93);
                    _soundManager.changeVolume(93);
                }
                else if (seekBar.getProgress() <= 7) {
                    seekBar.setProgress(7);
                    _soundManager.changeVolume(7);
                }
                else {
                    _soundManager.changeVolume(seekBar.getProgress());
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }

    private void initConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.confirmation));
        builder.setMessage(getString(R.string.resetMessage));
        builder.setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> _gameAdaptation.reset());
        builder.setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> _confirmation.cancel());
        _confirmation = builder.create();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (_soundManager.getMusic().isPlaying() && !_switch) {
            _soundManager.getMusic().pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!_soundManager.getMusic().isPlaying()) {
            _soundManager.getMusic().start();
        }
    }

}