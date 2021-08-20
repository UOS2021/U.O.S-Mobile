package com.uof.uof_mobile.manager;

import android.os.AsyncTask;

import com.uof.uof_mobile.Global;

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
            httpURLConnection.setConnectTimeout(2000);
            httpURLConnection.setReadTimeout(2000);
            httpURLConnection.connect();

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(strings[1]);
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