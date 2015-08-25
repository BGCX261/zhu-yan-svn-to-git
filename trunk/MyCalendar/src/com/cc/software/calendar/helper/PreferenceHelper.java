package com.cc.software.calendar.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceHelper {

    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPreferences.Editor getSharedPrefenceEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    public static void savePreferenceStringValue(Context context, String key, String value) {
        getSharedPrefenceEditor(context).putString(key, value).commit();
    }

    public static void savePreferenceIntegerValue(Context context, String key, int value) {
        getSharedPrefenceEditor(context).putInt(key, value).commit();
    }

    public static void savePreferenceBooleanValue(Context context, String key, Boolean value) {
        getSharedPrefenceEditor(context).putBoolean(key, value).commit();
    }

    public static void savePreferenceLongValue(Context context, String key, Long value) {
        getSharedPrefenceEditor(context).putLong(key, value).commit();
    }

    public static String getPreferenceStringValue(Context context, String key, String defaultValue) {
        return getSharedPreferences(context).getString(key, defaultValue);
    }

    public static int getPreferenceIntValue(Context context, String key, int defaultValue) {
        return getSharedPreferences(context).getInt(key, defaultValue);
    }

    public static Boolean getPreferencePreferenceBooleanValue(Context context, String key, Boolean defaultValue) {
        return getSharedPreferences(context).getBoolean(key, defaultValue);
    }
}
