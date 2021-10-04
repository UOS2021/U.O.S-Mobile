package com.uos.uos_mobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import androidx.appcompat.app.AppCompatActivity;

import com.uos.uos_mobile.R;
import com.uos.uos_mobile.activity.LoginActivity;
import com.uos.uos_mobile.activity.SettingActivity;
import com.uos.uos_mobile.dialog.ChangePhoneDialog;
import com.uos.uos_mobile.dialog.ChangePwDialog;
import com.uos.uos_mobile.dialog.WithdrawalDialog;
import com.uos.uos_mobile.manager.SharedPreferenceManager;
import com.uos.uos_mobile.other.Global;


public class SettingPreferenceFragment extends PreferenceFragment {
    private Context context;
    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();

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
        PreferenceScreen btnAccountSetting = getPreferenceScreen();

        // 비밀번호 변경이 눌렸을 경우
        btnChangePw.setOnPreferenceClickListener(preference -> {
            new ChangePwDialog(context, false, true).show();

            return false;
        });

        // 전화번호 변경이 눌렸을 경우
        btnChangePhone.setOnPreferenceClickListener(preference -> {
            //startActivity(new Intent(context, ChangePhoneActivity.class));
            new ChangePhoneDialog(context, false, true).show();

            return false;
        });

        // 로그아웃이 눌렸을 경우
        btnLogout.setOnPreferenceClickListener(preference -> {
            SharedPreferenceManager.open(context, Global.SharedPreference.APP_DATA);
            SharedPreferenceManager.save(Global.SharedPreference.IS_LOGINED, false);
            SharedPreferenceManager.save(Global.SharedPreference.USER_ID, "");
            SharedPreferenceManager.save(Global.SharedPreference.USER_PW, "");
            SharedPreferenceManager.save(Global.SharedPreference.USER_TYPE, "");
            SharedPreferenceManager.close();

            for (AppCompatActivity activity : Global.activities) {
                if (!(activity instanceof SettingActivity)) {
                    activity.finish();
                }
            }
            startActivity(new Intent(context, LoginActivity.class));
            getActivity().finish();

            return false;
        });

        // 회원탈퇴가 눌렸을 경우
        btnWithdrawal.setOnPreferenceClickListener(preference -> {
            new WithdrawalDialog(context, false, true).show();
            return false;
        });
        prefs.registerOnSharedPreferenceChangeListener(prefsListener);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference.getKey().equals(getString(R.string.setting_account))) {
            return false;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}