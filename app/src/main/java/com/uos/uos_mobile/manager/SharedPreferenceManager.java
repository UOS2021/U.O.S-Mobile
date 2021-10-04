package com.uos.uos_mobile.manager;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {
    private static SharedPreferences sp;
    private static SharedPreferences.Editor spEditor;

    public static void open(Context context, String name) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public static void save(String key, int value) {
        spEditor.putInt(key, value);
        spEditor.commit();
    }

    public static void save(String key, long value) {
        spEditor.putLong(key, value);
        spEditor.commit();
    }

    public static void save(String key, float value) {
        spEditor.putFloat(key, value);
        spEditor.commit();
    }

    public static void save(String key, boolean value) {
        spEditor.putBoolean(key, value);
        spEditor.commit();
    }

    public static void save(String key, String value) {
        spEditor.putString(key, value);
        spEditor.commit();
    }

    public static int load(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public static long load(String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    public static float load(String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    public static boolean load(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    public static String load(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public static void close() {
        sp = null;
        spEditor = null;
    }
}
