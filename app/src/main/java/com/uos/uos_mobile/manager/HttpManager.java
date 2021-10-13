package com.uos.uos_mobile.manager;

import android.os.AsyncTask;

import com.uos.uos_mobile.other.Global;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class HttpManager extends AsyncTask<String, String, String> {
    /**
     * Http 통신 시 기본 연결 Timeout 시간
     */
    public static int DEFAULT_CONNECTION_TIMEOUT = 2000;

    /**
     * Http 통신 시 기본 수신 Timeout 시간
     */
    public static int DEFAULT_READ_TIMEOUT = 2000;

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setConnectTimeout(Integer.valueOf(strings[1]));
            httpURLConnection.setReadTimeout(Integer.valueOf(strings[2]));
            httpURLConnection.connect();

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(strings[3]);
            bufferedWriter.flush();
            bufferedWriter.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            httpURLConnection.disconnect();

            String result = stringBuffer.toString();

            result = result.substring(1, result.length() - 1).replace("\\", "");

            return result;
        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            return "{ response_code: \"" + Global.Network.Response.SERVER_NOT_ONLINE + "\" }";
        } catch (Exception e) {
            e.printStackTrace();

            return "{ response_code: \"-1\", message: \"" + e.toString() + "\" }";
        }
    }
}