package com.uof.uof_mobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.uof.uof_mobile.R;
import com.uof.uof_mobile.activity.ChangePhoneActivity;
import com.uof.uof_mobile.activity.ChangePwActivity;
import com.uof.uof_mobile.activity.WithdrawalActivity;


public class SettingPreferenceFragment extends PreferenceFragment {
    private Context context;
    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity().getApplicationContext();

        addPreferencesFromResource(R.xml.settings_preference);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.OnSharedPreferenceChangeListener prefsListener = (sharedPreferences, key) -> {
            if (key.equals("vibration") && sharedPreferences.getBoolean("vibration", false) == true) {
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(500);
            }
        };

        Preference btnChangePw = findPreference(getString(R.string.setting_change_pw));
        Preference btnChangePhone = findPreference(getString(R.string.setting_change_phone));
        Preference btnLogout = findPreference(getString(R.string.setting_logout));
        Preference btnWithdrawal = findPreference(getString(R.string.setting_withdrawal));

        // 비밀번호 변경이 눌렸을 경우
        btnChangePw.setOnPreferenceClickListener(preference -> {
            startActivity(new Intent(context, ChangePwActivity.class));

            return false;
        });

        // 전화번호 변경이 눌렸을 경우
        btnChangePhone.setOnPreferenceClickListener(preference -> {
            startActivity(new Intent(context, ChangePhoneActivity.class));

            return false;
        });

        // 로그아웃이 눌렸을 경우
        btnLogout.setOnPreferenceClickListener(preference -> {


            return false;
        });

        // 회원탈퇴가 눌렸을 경우
        btnWithdrawal.setOnPreferenceClickListener(preference -> {
            startActivity(new Intent(context, WithdrawalActivity.class));

            return false;
        });
        prefs.registerOnSharedPreferenceChangeListener(prefsListener);
    }
}