package games.wester.eyefoxpuzzle.save;

/*
 * Wester
 * This code is open source and free to use.
 *
 * You are free to copy, modify, and distribute this file without restriction.
 * No warranties are provided, and any use of this code is at your own risk.
 */

import android.content.Context;
import android.content.SharedPreferences;

public class OptionSave {

    private final String _key = "level";
    private final String _saveFile = "sound";
    private final Context _context;

    public OptionSave(Context context) {
        _context = context;
    }

    public void saveLevel(int level) {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(_saveFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(_key, level);
        editor.apply();
    }

    public int loadLevel() {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(_saveFile, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(_key, 93);
    }

}
