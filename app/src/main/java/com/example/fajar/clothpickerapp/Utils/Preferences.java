package com.example.fajar.clothpickerapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by mis on 27-Sep-17.
 */

public class Preferences {

    public static final String USER_NAME = "USER_NAME";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_ID = "USER_ID";
    public static final String USER_FAVORITE_CLOTHS = "USER_FAVORITE_CLOTHS";
    public static final String FIRST_TIME = "FIRST_TIME";
    public static final String USER_PROFILE_PICTURE = "USER_PROFILE_PICTURE";

    public static void setValue(Context context, String Key, String Value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Key, Value);
        editor.commit();
    }

    public static String getValue_String(Context context, String Key) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getString(Key, "");
    }
}
