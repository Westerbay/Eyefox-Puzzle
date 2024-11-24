package games.wester.eyefoxpuzzle.save;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import static org.junit.Assert.assertEquals;

import android.app.Application;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LevelSaveTest {

    private LevelSave levelSave;

    @Before
    public void setUp() {
        levelSave = new LevelSave(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void load_default_level_should_be_zero() {
        assertEquals(0, levelSave.loadLevel());
    }

    @Test
    public void save_level_should_stay_after_new_application() {
        levelSave.saveLevel(50);
        levelSave = new LevelSave(ApplicationProvider.getApplicationContext());
        assertEquals(50, levelSave.loadLevel());
    }

}
