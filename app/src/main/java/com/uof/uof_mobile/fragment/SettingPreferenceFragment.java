package com.uof.uof_mobile.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.uof.uof_mobile.R;


public class SettingPreferenceFragment extends PreferenceFragment {
    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_preference);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.OnSharedPreferenceChangeListener prefsListener = (sharedPreferences, key) -> {
            if (key.equals("vibration") && sharedPreferences.getBoolean("vibration", false) == true) {
                Vibrator vibrator = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(500);
            }
        };

        Preference btnChangePw = findPreference(getString(R.string.setting_change_pw));
        Preference btnChangePhone = findPreference(getString(R.string.setting_change_phone));
        Preference btnLogout = findPreference(getString(R.string.setting_logout));
        Preference btnWithdrawal = findPreference(getString(R.string.setting_withdrawal));

        // 비밀번호 변경이 눌렸을 경우
        btnChangePw.setOnPreferenceClickListener(preference -> {
            Toast.makeText(getActivity().getApplicationContext(), "비밀번호 변경", Toast.LENGTH_SHORT).show();

            return false;
        });

        // 전화번호 변경이 눌렸을 경우
        btnChangePhone.setOnPreferenceClickListener(preference -> {
            Toast.makeText(getActivity().getApplicationContext(), "핸드폰 번호 수정", Toast.LENGTH_SHORT).show();

            return false;
        });

        // 로그아웃이 눌렸을 경우
        btnLogout.setOnPreferenceClickListener(preference -> {
            Toast.makeText(getActivity().getApplicationContext(), "로그아웃", Toast.LENGTH_SHORT).show();

            return false;
        });

        // 회원탈퇴가 눌렸을 경우
        btnWithdrawal.setOnPreferenceClickListener(preference -> {
            Toast.makeText(getActivity().getApplicationContext(), "탈퇴", Toast.LENGTH_SHORT).show();

            return false;
        });
        prefs.registerOnSharedPreferenceChangeListener(prefsListener);
    }
}