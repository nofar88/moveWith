package com.example.movewith.Model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class MemoryAccess {
    static SharedPreferences.Editor editor;
    static SharedPreferences preferences;
    static public Activity activity;

    //מקבלת קיא וערך ותפקידה לשמור את זה בזיכרון
    static public void saveToMemory(String key, String value) {
        if (editor == null) {
            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
            editor = sharedPref.edit();
        }
        editor.putString(key, value);
        editor.apply();
    }
// לוקחת מהזיכון של הטלפון ומביאה לנו לשימוש
    static public String loadFromMemory(String key) {
        if (preferences == null) {
            preferences = activity.getPreferences(Context.MODE_PRIVATE);
        }
        return preferences.getString(key, "");
    }
}
