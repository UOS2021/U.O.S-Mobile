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

/**
 * Http 통신시 사용하는 Manager 클래스.<br><br>
 * 웹 서버와 Http 통신시 사용되는 클래스이며 AsyncTask를 상속받아 동작합니다.
 * 작합니다.
 *
 * <pre>
 * {@code
 * new HttpManager().execute(new String[]{"URL", "Connect Timeout", "Read Timeout", "Data"}).get());
 * }
 *
 * 위와 같이 사용할 수 있으며 반환값은 통신 결과에 대한 String 입니다.
 * </pre>
 *
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class HttpManager extends AsyncTask<String, String, String> {
    /**
     * Http 통신 시 기본 연결 Timeout 시간.
     */
    public static int DEFAULT_CONNECTION_TIMEOUT = 2000;

    /**
     * Http 통신 시 기본 수신 Timeout 시간.
     */
    public static int DEFAULT_READ_TIMEOUT = 2000;

    /**
     * HttpManager를 사용하여 통신하는 함수.<br>
     *
     * 매개변수로 전달되는 String 배열의 값은 다음과 같다.<br>
     * string[0]: 접속할 URL<br>
     * string[1]: connect timeout<br>
     * string[2]: read timeout<br>
     * string[3]: 전송할 데이터
     *
     * @param strings HttpManager 통신 시 사용하는 데이터.
     * @return String 통신에 대한 결과값.
     */
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