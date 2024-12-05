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
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import games.wester.eyefoxpuzzle.R;
import games.wester.eyefoxpuzzle.core.LevelStageManager;
import games.wester.eyefoxpuzzle.core.PuzzleManager;
import games.wester.eyefoxpuzzle.puzzle.LevelStage;
import games.wester.eyefoxpuzzle.view.CheckerView;
import games.wester.eyefoxpuzzle.view.FoxView;
import games.wester.eyefoxpuzzle.view.GridView;
import games.wester.eyefoxpuzzle.view.HealthView;
import games.wester.eyefoxpuzzle.view.LevelView;
import games.wester.eyefoxpuzzle.core.GameManager;
import games.wester.eyefoxpuzzle.view.NumberOfMovesView;
import games.wester.eyefoxpuzzle.view.SoundManager;
import games.wester.eyefoxpuzzle.view.StarsView;
import games.wester.westerlib.core.Updatable;

/**
 * @author Wester
 */
public class GameActivity extends AppCompatActivity  {

    private static LevelStageManager _levelStageManager = null;
    private static PuzzleManager _leftPuzzleManager = null;
    private static PuzzleManager _rightPuzzleManager = null;
    private GameManager _gameAdaptation;
    private Handler _handler;
    private Runnable _imageChanger;
    private boolean _switch;
    private MediaPlayer _music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        _music = SoundManager.create(this).getMusic();
        setContentView(R.layout.level_stage);
        _switch = false;
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
        _handler = new Handler(Looper.getMainLooper());
        GameManager.createInstance(this);
        _gameAdaptation = GameManager.getInstance();
        initManager();
        initView();
        initButton();
    }

    public void initManager() {
        LevelStage levelStage = _gameAdaptation.getLevelStage();
        if (_levelStageManager == null) {
            _levelStageManager = new LevelStageManager(
                    levelStage, () -> {_gameAdaptation.win(); switchScene();},
                    () -> {_gameAdaptation.loose(); switchHome();}
            );
        }
        if (_leftPuzzleManager == null) {
            _leftPuzzleManager = new PuzzleManager(_levelStageManager, levelStage.getCopyPuzzle());
        }
        if (_rightPuzzleManager == null) {
            _rightPuzzleManager = new PuzzleManager(_levelStageManager, levelStage.getOriginalPuzzle());
        }
        _levelStageManager.setLeftPuzzleManager(_leftPuzzleManager);
        _levelStageManager.setRightPuzzleManager(_rightPuzzleManager);
    }

    public void initView() {
        FoxView foxView = new FoxView(_gameAdaptation.getFox(), findViewById(R.id.fox));

        GridView leftGridView = new GridView(
                this, _leftPuzzleManager, findViewById(R.id.leftGridLayout), false
        );
        GridView rightGridView = new GridView(
                this, _rightPuzzleManager, findViewById(R.id.rightGridLayout), true
        );
        NumberOfMovesView numberOfMovesView = new NumberOfMovesView(
                _levelStageManager, findViewById(R.id.numberOfMoves)
        );
        HealthView healthView = new HealthView(
                _levelStageManager, findViewById(R.id.firstLife),
                findViewById(R.id.secondLife), findViewById(R.id.thirdLife)
        );
        CheckerView checkerView = new CheckerView(
                this, _levelStageManager, findViewById(R.id.correct), findViewById(R.id.wrong)
        );
        if (!_gameAdaptation.isTraining()) {
            new LevelView(findViewById(R.id.level), _gameAdaptation, getString(R.string.level)).update();
            new StarsView(
                    _gameAdaptation.getLevel(),
                    findViewById(R.id.empty_star1), findViewById(R.id.empty_star2),
                    findViewById(R.id.empty_star3), findViewById(R.id.empty_star4),
                    findViewById(R.id.empty_star5), findViewById(R.id.empty_star6),
                    findViewById(R.id.empty_star7), findViewById(R.id.empty_star8),
                    findViewById(R.id.empty_star9), findViewById(R.id.empty_star10)
            );
        }
        else {
            TextView trainingView = findViewById(R.id.level);
            trainingView.setText(R.string.training);
        }
        findViewById(R.id.cross).setVisibility(View.VISIBLE);
        renderView(foxView, leftGridView, rightGridView, numberOfMovesView, healthView, checkerView);
    }

    public void renderView(Updatable... elements) {
        int interval = 1000 / 30;
        _imageChanger = new Runnable() {
            @Override
            public void run() {
                for (Updatable element: elements) {
                    element.update();
                }
                _handler.postDelayed(this, interval);
            }
        };
        _handler.post(_imageChanger);
    }

    public void initButton() {
        findViewById(R.id.homeButton).setOnClickListener(
                v -> switchHome()
        );
    }

    public void switchHome() {
        _switch = true;
        _handler.removeCallbacks(_imageChanger);
        _levelStageManager = null;
        _leftPuzzleManager = null;
        _rightPuzzleManager = null;
        _handler.postDelayed(() -> {
            Intent intent = new Intent(GameActivity.this, EmptyTransition.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            GameActivity.this.startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }, 100);
    }

    public void switchScene() {
        _switch = true;
        _handler.removeCallbacks(_imageChanger);
        _levelStageManager = null;
        _leftPuzzleManager = null;
        _rightPuzzleManager = null;
        _handler.postDelayed(() -> {
            Intent intent;
            if (_gameAdaptation.isTraining()) {
                intent = new Intent(GameActivity.this, TransitionTraining.class);
            }
            else {
                intent = new Intent(GameActivity.this, Transition.class);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            GameActivity.this.startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }, 100);
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
