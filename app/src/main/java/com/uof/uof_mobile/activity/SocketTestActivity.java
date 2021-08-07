package com.uof.uof_mobile.activity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.manager.SocketManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SocketTestActivity extends AppCompatActivity {
    private TextInputEditText tiedtSocketTestServerIp;
    private TextInputEditText tiedtSocketTestServerPort;
    private TextView tvSocketTestLog;
    private TextView tvSocketTestSendLog;
    private TextView tvSocketTestRecvLog;
    private EditText edtSocketTestType;
    private EditText edtSocketTestMessage;
    private Button btnSocketTestSend;
    private SocketManager socketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sockettest);

        init();
    }

    private void init() {
        tiedtSocketTestServerIp = findViewById(R.id.tiedt_sockettest_serverip);
        tiedtSocketTestServerPort = findViewById(R.id.tiedt_sockettest_serverport);
        tvSocketTestLog = findViewById(R.id.tv_sockettest_log);
        tvSocketTestSendLog = findViewById(R.id.tv_sockettest_sendlog);
        tvSocketTestRecvLog = findViewById(R.id.tv_sockettest_recvlog);
        edtSocketTestType = findViewById(R.id.edt_sockettest_type);
        edtSocketTestMessage = findViewById(R.id.edt_sockettest_message);
        btnSocketTestSend = findViewById(R.id.btn_sockettest_send);

        tvSocketTestLog.setMovementMethod(new ScrollingMovementMethod());
        tvSocketTestSendLog.setMovementMethod(new ScrollingMovementMethod());
        tvSocketTestRecvLog.setMovementMethod(new ScrollingMovementMethod());

        String targetIp = getIntent().getStringExtra("targetIp");
        int targetPort = getIntent().getIntExtra("targetPort", -1);
        if (targetIp != null) {
            tiedtSocketTestServerIp.setText(targetIp);
        }
        if (targetPort != -1) {
            tiedtSocketTestServerPort.setText(String.valueOf(targetPort));
        }

        socketManager = new SocketManager();

        btnSocketTestSend.setOnClickListener((v) -> {
            setInputAreaEnable(false);
            new Thread(() -> {
                socketManager.setSocket(tiedtSocketTestServerIp.getText().toString(), Integer.parseInt(tiedtSocketTestServerPort.getText().toString()));
                if (socketManager.connect(2000)) {
                    // 소켓 연결 성공 시
                    runOnUiThread(() -> {
                        tvSocketTestLog.append(new SimpleDateFormat("[HH:mm:ss] ").format(new Date(System.currentTimeMillis())) + "소켓 연결 성공\n");
                    });

                    JSONObject sendData = new JSONObject();
                    try {
                        sendData.put("type", edtSocketTestType.getText().toString());
                        sendData.put("message", edtSocketTestMessage.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (socketManager.send(sendData.toString() + "\n")) {
                        // 메세지 전송 성공 시
                        runOnUiThread(() -> {
                            tvSocketTestSendLog.append(new SimpleDateFormat("[HH:mm:ss] ").format(new Date(System.currentTimeMillis())) + "송신 완료: " + sendData.toString() + "\n");
                        });
                        String recvData = socketManager.recv();
                        if (recvData == null) {
                            runOnUiThread(() -> {
                                tvSocketTestRecvLog.append(new SimpleDateFormat("[HH:mm:ss] ").format(new Date(System.currentTimeMillis())) + "수신 실패" + "\n");

                            });
                        } else {
                            runOnUiThread(() -> {
                                tvSocketTestRecvLog.append(new SimpleDateFormat("[HH:mm:ss] ").format(new Date(System.currentTimeMillis())) + "수신 완료: " + recvData + "\n");
                            });
                        }
                    } else {
                        runOnUiThread(() -> {
                            tvSocketTestSendLog.setText(new SimpleDateFormat("[HH:mm:ss] ").format(new Date(System.currentTimeMillis())) + "송신 실패: " + sendData.toString() + "\n");
                        });
                    }

                    socketManager.disconnect();

                    runOnUiThread(() -> {
                        tvSocketTestLog.append(new SimpleDateFormat("[HH:mm:ss] ").format(new Date(System.currentTimeMillis())) + "소켓 연결 종료\n");
                        setInputAreaEnable(true);
                    });
                } else {
                    runOnUiThread(() -> {
                        tvSocketTestLog.append(new SimpleDateFormat("[HH:mm:ss] ").format(new Date(System.currentTimeMillis())) + "소켓 연결 실패\n");
                        setInputAreaEnable(true);
                    });
                }
            }).start();
        });
    }

    private void setInputAreaEnable(boolean enable) {
        edtSocketTestType.setEnabled(enable);
        edtSocketTestMessage.setEnabled(enable);
        btnSocketTestSend.setEnabled(enable);
    }
}