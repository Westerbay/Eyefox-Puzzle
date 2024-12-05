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

/**
 * @author Wester
 */
public class NewSave {

    private final String _key = "new";
    private final String _saveFile = "new";
    private final Context _context;

    public NewSave(Context context) {
        _context = context;
    }

    public void setNew(boolean isNew) {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(_saveFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(_key, isNew);
        editor.apply();
    }

    public boolean isNew() {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(_saveFile, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(_key, true);
    }

}
