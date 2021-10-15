package com.uos.uos_mobile.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences를 쉽게 사용할 수 있도록 도와주는 클래스.<br><br>
 * <p>
 * SharedPreferences에 데이터 저장 및 불러오기를 쉽게 할 수 있도록 도와주는 클래스입니다. 사용시 open() 함수를 통
 * 해 SharedPreferences에 접근, 사용 후에는 close() 함수를 통해 SharedPreferences 연결을 해제해줍니다.
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class SharedPreferencesManager {
    /**
     * SharedPreferencesManager에서 사용되는 SharedPreferences 객체.
     */
    private static SharedPreferences sp;

    /**
     * SharedPreferencesManager에서 데이터 삽입 시 사용되는 SharedPreferences.Editor 객체.
     */
    private static SharedPreferences.Editor spEditor;

    /**
     * 매개변수로 전달된 이름의 SharedPreferences에 접근할 수 있도록 설정하는 함수.
     *
     * @param context SharedPreferences에 접근 시 필요한 Context 객체.
     * @param name    접근할 SharedPreferences의 이름.
     */
    public static void open(Context context, String name) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    /**
     * 연결된 SharedPreferences의 연결을 해제하는 함수. 잘못된 접근을 막기 위해 SharedPreferences 사용 후 반
     * 드시 close() 함수를 호출하는 것을 권장.
     */
    public static void close() {
        sp = null;
        spEditor = null;
    }

    /**
     * 연결된 SharedPreferences에 데이터 저장하는 함수.
     *
     * @param key   저장할 데이터의 키값.
     * @param value 저장할 데이터 값.
     */
    public static void save(String key, Object value) {
        if (value instanceof Integer) {
            spEditor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            spEditor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            spEditor.putFloat(key, (Float) value);
        } else if (value instanceof Boolean) {
            spEditor.putBoolean(key, (Boolean) value);
        } else if (value instanceof String) {
            spEditor.putString(key, (String) value);
        }
        spEditor.commit();
    }

    /**
     * 연결된 SharedPreferences에서 데이터를 불러오는 함수.
     *
     * @param key          불러올 데이터의 키값.
     * @param defaultValue 데이터가 없을 경우의 기본값.
     */
    public static Object load(String key, Object defaultValue) {
        if (defaultValue instanceof Integer) {
            return sp.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Long) {
            return sp.getLong(key, (Long) defaultValue);
        } else if (defaultValue instanceof Float) {
            return sp.getFloat(key, (Float) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof String) {
            return sp.getString(key, (String) defaultValue);
        }

        return null;
    }
}
