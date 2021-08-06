package com.uof.uof_mobile;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.BaseAdapter;
import android.widget.Toast;


public class SettingPreferenceFragment extends PreferenceFragment {

    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_preference);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.OnSharedPreferenceChangeListener prefsListener = (sharedPreferences, key) -> {
            if(key.equals("vibration") && sharedPreferences.getBoolean("vibration", false) == true){
                    Vibrator vibrator = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(500);
            }
        };
        Preference changepw_button = findPreference("changepw");
        changepw_button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity().getApplicationContext(), "비밀번호 변경", Toast.LENGTH_SHORT).show();

                return false;
            }
        });
        Preference changephone_button = findPreference("changephone");
        changephone_button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity().getApplicationContext(), "핸드폰 번호 수정", Toast.LENGTH_SHORT).show();

                return false;
            }
        });
        Preference logout_button = findPreference("logout");
        logout_button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity().getApplicationContext(), "로그아웃", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        Preference secession_button = findPreference("secession");
        secession_button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity().getApplicationContext(), "탈퇴", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        prefs.registerOnSharedPreferenceChangeListener(prefsListener);
    }
}