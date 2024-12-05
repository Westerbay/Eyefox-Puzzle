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
import games.wester.eyefoxpuzzle.view.SoundManager;

/**
 * @author Wester
 */
public class EmptyTransition extends AppCompatActivity {

    private boolean _switch;
    private MediaPlayer _music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        _switch = false;
        _music = SoundManager.create(this).getMusic();
        setContentView(R.layout.black);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::switchScene, 1800);
    }

    public void switchScene() {
        _switch = true;
        Intent intent = new Intent(EmptyTransition.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        EmptyTransition.this.startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
