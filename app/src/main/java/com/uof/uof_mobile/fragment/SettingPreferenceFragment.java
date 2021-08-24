package com.uof.uof_mobile.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uof.uof_mobile.R;
import com.uof.uof_mobile.activity.LoginActivity;
import com.uof.uof_mobile.activity.SettingActivity;
import com.uof.uof_mobile.dialog.ChangePhoneDialog;
import com.uof.uof_mobile.dialog.ChangePwDialog;
import com.uof.uof_mobile.manager.HttpManager;
import com.uof.uof_mobile.manager.SharedPreferenceManager;
import com.uof.uof_mobile.other.Global;

import org.json.JSONObject;


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
            AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                    .setTitle("U.O.F 탈퇴")
                    .setMessage("U.O.F를 탈퇴하시겠습니까?")
                    .setPositiveButton("예", (dialogInterface, i) -> {
                        try {
                            JSONObject sendData = new JSONObject();
                            sendData.put("request_code", Global.Network.Request.WITHDRAWAL);

                            JSONObject message = new JSONObject();
                            message.accumulate("id", Global.User.id);
                            message.accumulate("type", Global.User.type);

                            sendData.accumulate("message", message);

                            JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, sendData.toString()}).get());

                            String responseCode = recvData.getString("response_code");

                            if (responseCode.equals(Global.Network.Response.WITHDRAWAL_SUCCESS)) {
                                Toast.makeText(context, "탈퇴되었습니다", Toast.LENGTH_SHORT).show();
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
                            } else if (responseCode.equals(Global.Network.Response.WITHDRAWAL_FAILED)) {
                                // 전화번호 변경 실패
                                Toast.makeText(context, "탈퇴 실패: " + recvData.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                // 전화번호 변경 실패 - 기타 오류
                                Toast.makeText(context, "탈퇴 실패(기타): " + recvData.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("아니오", null).create();

            alertDialog.setOnShowListener(dialogInterface -> {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            });

            alertDialog.show();

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