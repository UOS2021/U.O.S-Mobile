package com.uos.uos_mobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import com.uos.uos_mobile.activity.LoginActivity;
import com.uos.uos_mobile.activity.UosActivity;
import com.uos.uos_mobile.dialog.ChangePhoneDialog;
import com.uos.uos_mobile.dialog.ChangePwDialog;
import com.uos.uos_mobile.dialog.WithdrawalDialog;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.manager.SharedPreferencesManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class SettingPreferenceFragment extends PreferenceFragment {
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();

        addPreferencesFromResource(com.uos.uos_mobile.R.xml.settings_preference);
        Preference btnChangePw = findPreference(getString(com.uos.uos_mobile.R.string.setting_change_pw));
        Preference btnChangePhone = findPreference(getString(com.uos.uos_mobile.R.string.setting_change_phone));
        Preference btnLogout = findPreference(getString(com.uos.uos_mobile.R.string.setting_logout));
        Preference btnWithdrawal = findPreference(getString(com.uos.uos_mobile.R.string.setting_withdrawal));

        /* 비밀번호 변경이 눌렸을 경우 */
        btnChangePw.setOnPreferenceClickListener(preference -> {
            new ChangePwDialog(context, false, true).show();

            return false;
        });

        /* 전화번호 변경이 눌렸을 경우 */
        btnChangePhone.setOnPreferenceClickListener(preference -> {
            new ChangePhoneDialog(context, false, true).show();

            return false;
        });

        /* 로그아웃이 눌렸을 경우 */
        btnLogout.setOnPreferenceClickListener(preference -> {
            if (Global.User.type.equals("customer")) {
                try {
                    JSONObject message = new JSONObject();
                    message.accumulate("customer_id", Global.User.id);

                    JSONObject sendData = new JSONObject();
                    sendData.accumulate("request_code", Global.Network.Request.CUSTOMER_LOGOUT);
                    sendData.accumulate("message", message);

                    JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(HttpManager.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());
                    String responseCode = recvData.getString("response_code");

                    if (responseCode.equals(Global.Network.Response.CUSTOMER_LOGOUT_SUCCESS)) {

                        /* 로그아웃 성공 시 */

                        Toast.makeText(context, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
                    } else {

                        /* 로그아웃 실패 시 */

                        Toast.makeText(context, "로그아웃 도중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "로그아웃 도중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                }
            }

            SharedPreferencesManager.open(context, Global.SharedPreference.APP_DATA);
            SharedPreferencesManager.save(Global.SharedPreference.IS_LOGINED, false);
            SharedPreferencesManager.save(Global.SharedPreference.USER_ID, "");
            SharedPreferencesManager.save(Global.SharedPreference.USER_PW, "");
            SharedPreferencesManager.save(Global.SharedPreference.USER_TYPE, "");
            SharedPreferencesManager.close();

            UosActivity.startFromActivity(new Intent(context.getApplicationContext(), LoginActivity.class));

            return false;
        });

        /* 회원탈퇴가 눌렸을 경우 */
        btnWithdrawal.setOnPreferenceClickListener(preference -> {
            new WithdrawalDialog(context, false, true).show();
            return false;
        });
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference.getKey().equals(getString(com.uos.uos_mobile.R.string.setting_account))) {
            return false;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}