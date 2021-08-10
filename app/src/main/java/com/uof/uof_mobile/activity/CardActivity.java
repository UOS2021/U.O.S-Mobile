package com.uof.uof_mobile.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.uof.uof_mobile.Constants;
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.dialog.AddCardDialog;
import com.uof.uof_mobile.manager.HttpManager;

import org.json.JSONObject;

public class CardActivity extends AppCompatActivity {
    private AppCompatImageButton ibtnCardClose;
    private AppCompatImageButton ibtnCardDelete;
    private AppCompatImageView ivCardBackground;
    private AppCompatTextView tvCardNoCard;
    private ConstraintLayout clCardUiGroup;
    private AppCompatTextView tvCardUserName;
    private AppCompatTextView tvCardCardNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        init();
    }

    private void init() {
        ibtnCardClose = findViewById(R.id.ibtn_card_close);
        ibtnCardDelete = findViewById(R.id.ibtn_card_delete);
        ivCardBackground = findViewById(R.id.iv_card_background);
        tvCardNoCard = findViewById(R.id.tv_card_nocard);
        clCardUiGroup = findViewById(R.id.cl_card_uigroup);
        tvCardUserName = findViewById(R.id.tv_card_username);
        tvCardCardNum = findViewById(R.id.tv_card_cardnum);

        // 초기 UI 상태 설정
        removeCardData();
        new GetCard().start();

        // 종료 버튼이 눌렸을 경우
        ibtnCardClose.setOnClickListener(view -> {
            finish();
        });

        // 삭제 버튼이 눌렸을 경우
        ibtnCardDelete.setOnClickListener(view -> {
            new AlertDialog.Builder(CardActivity.this)
                    .setTitle("카드 제거")
                    .setMessage("카드를 제거하시겠습니까?")
                    .setPositiveButton("제거", (dialogInterface, i) -> {
                        new RemoveCard().start();
                    })
                    .setNegativeButton("취소", null)
                    .show();
        });

        // 카드이미지가 눌렸을 경우
        ivCardBackground.setOnClickListener(view -> {
            // 카드이미지가 눌렸을 경우
            AddCardDialog addCardDialog = new AddCardDialog(CardActivity.this, true, true);
            addCardDialog.setOnDismissListener(dialogInterface -> {
                new GetCard().start();
            });
            addCardDialog.show();
        });
    }

    private class GetCard extends Thread {
        @Override
        public void run() {
            runOnUiThread(() -> {
                removeCardData();
                tvCardNoCard.setText("등록된 카드를 불러오는 중입니다...");
            });
            try {
                JSONObject sendData = new JSONObject();
                sendData.put("request_code", Constants.Network.Request.CARD_INFO);

                JSONObject message = new JSONObject();
                message.accumulate("id", Constants.User.id);

                sendData.accumulate("message", message);

                String temp = new HttpManager().execute(new String[]{"http://211.217.202.157:8080/post", sendData.toString()}).get();

                JSONObject recvData = new JSONObject(temp);

                String responseCode = recvData.getString("response_code");
                
                if (responseCode.equals(Constants.Network.Response.CARD_INFO)) {
                    // 카드 불러오기 성공
                    String cardNum = recvData.getJSONObject("message").getString("num");
                    runOnUiThread(() -> {
                        setCardData(cardNum);
                    });
                } else if (responseCode.equals(Constants.Network.Response.CARD_NOINFO)) {
                    // 카드 없음
                    runOnUiThread(() -> {
                        removeCardData();
                    });
                } else if (responseCode.equals(Constants.Network.Response.SERVER_NOT_ONLINE)) {
                    // 서버 연결 실패
                    runOnUiThread(() -> {
                        removeCardData();
                        Toast.makeText(CardActivity.this, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
                    });
                } else{
                    // 카드 불러오기 실패
                    runOnUiThread(() -> {
                        removeCardData();
                        tvCardNoCard.setText("카드를 불러올 수 없습니다");
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(() -> {
                    Toast.makeText(CardActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    removeCardData();
                });
            }
        }
    }

    private class RemoveCard extends Thread {
        @Override
        public void run() {
            runOnUiThread(() -> {
                removeCardData();
                tvCardNoCard.setText("카드 제거중입니다...");
            });
            try {
                JSONObject sendData = new JSONObject();
                sendData.put("request_code", Constants.Network.Request.CARD_REMOVE);

                JSONObject message = new JSONObject();
                message.accumulate("id", Constants.User.id);

                sendData.accumulate("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{"http://211.217.202.157:8080/post", sendData.toString()}).get());

                String responseCode = recvData.getString("response_code");
                
                if (responseCode.equals(Constants.Network.Response.CARD_REMOVE_SUCCESS)) {
                    // 카드 제거 성공
                    runOnUiThread(() -> {
                        removeCardData();
                    });
                } else if (responseCode.equals(Constants.Network.Response.CARD_REMOVE_FAILED)) {
                    // 카드 제거 실패
                    runOnUiThread(() -> {
                        new GetCard().start();
                    });
                } else if (responseCode.equals(Constants.Network.Response.SERVER_NOT_ONLINE)) {
                    // 서버 연결 실패
                    runOnUiThread(() -> {
                        removeCardData();
                        Toast.makeText(CardActivity.this, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
                    });
                }  else{
                    // 카드 제거 실패 - 기타
                    runOnUiThread(() -> {
                        new GetCard().start();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();

                new GetCard().start();
                runOnUiThread(() -> {
                    Toast.makeText(CardActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                });
            }
        }
    }

    private void setCardData(String cardNum) {
        tvCardNoCard.setVisibility(View.GONE);
        clCardUiGroup.setVisibility(View.VISIBLE);
        ibtnCardDelete.setVisibility(View.VISIBLE);
        ibtnCardDelete.setEnabled(true);

        tvCardUserName.setText(Constants.User.name);
        tvCardCardNum.setText(cardNum);
    }

    private void removeCardData() {
        tvCardNoCard.setText("등록된 카드가 없습니다");
        clCardUiGroup.setVisibility(View.GONE);
        tvCardNoCard.setVisibility(View.VISIBLE);
        ibtnCardDelete.setVisibility(View.INVISIBLE);
        ibtnCardDelete.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}