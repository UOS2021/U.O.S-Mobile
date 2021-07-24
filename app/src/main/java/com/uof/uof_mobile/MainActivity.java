package com.uof.uof_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        Intent socketTestActivity = new Intent(MainActivity.this, SocketTestActivity.class);

        if(Intent.ACTION_VIEW.equals(getIntent().getAction())){
            Uri uri = getIntent().getData();

            if(uri != null){
                String targetIp = uri.getQueryParameter("targetIp");
                int targetPort = Integer.parseInt(uri.getQueryParameter("targetPort"));

                socketTestActivity.putExtra("targetIp", targetIp);
                socketTestActivity.putExtra("targetPort", targetPort);
            }
        }
        startActivity(socketTestActivity);
    }
}