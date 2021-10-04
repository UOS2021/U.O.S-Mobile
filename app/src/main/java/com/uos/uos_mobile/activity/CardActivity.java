package com.uos.uos_mobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.uos.uos_mobile.R;
import com.uos.uos_mobile.dialog.CardDialog;
import com.uos.uos_mobile.item.CardItem;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONObject;

public class CardActivity extends AppCompatActivity {
    private AppCompatImageButton ibtnCardClose;
    private AppCompatImageButton ibtnCardDelete;
    private AppCompatImageView ivCardBackground;
    private AppCompatTextView tvCardNoCard;
    private ConstraintLayout clCardUiGroup;
    private AppCompatTextView tvCardUserName;
    private AppCompatTextView tvCardCardNum;
    private CardItem cardItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        init();
    }

    @Override
    protected void onDestroy() {
        Global.activities.remove(this);
        super.onDestroy();
    }

    private void init() {
        for(Activity activity : Global.activities){
            if(activity instanceof CardActivity){
                activity.finish();
            }
        }
        Global.activities.add(this);

        ibtnCardClose = findViewById(R.id.ibtn_card_back);
        ibtnCardDelete = findViewById(R.id.ibtn_card_delete);
        ivCardBackground = findViewById(R.id.iv_card_background);
        tvCardNoCard = findViewById(R.id.tv_card_nocard);
        clCardUiGroup = findViewById(R.id.cl_card_uigroup);
        tvCardUserName = findViewById(R.id.tv_card_username);
        tvCardCardNum = findViewById(R.id.tv_card_cardnum);

        cardItem = new CardItem();

        // 초기 UI 상태 설정
        removeCardData();
        new GetCard().start();

        // 종료 버튼이 눌렸을 경우
        ibtnCardClose.setOnClickListener(view -> {
            finish();
        });

        // 삭제 버튼이 눌렸을 경우
        ibtnCardDelete.setOnClickListener(view -> {
            AlertDialog alertDialog = new AlertDialog.Builder(CardActivity.this, R.style.AlertDialogTheme)
                    .setTitle("카드 제거")
                    .setMessage("카드를 제거하시겠습니까?")
                    .setPositiveButton("제거", (dialogInterface, i) -> {
                        new RemoveCard().start();
                    })
                    .setNegativeButton("취소", null).create();

            alertDialog.setOnShowListener(dialogInterface -> {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            });

            alertDialog.show();
        });

        // 카드이미지가 눌렸을 경우
        ivCardBackground.setOnClickListener(view -> {
            // 카드이미지가 눌렸을 경우
            CardDialog cardDialog = new CardDialog(CardActivity.this, true, true, cardItem);
            cardDialog.setOnDismissListener(dialogInterface -> {
                new GetCard().start();
            });
            cardDialog.show();
        });
    }

    private void setCardData() {
        tvCardNoCard.setVisibility(View.GONE);
        clCardUiGroup.setVisibility(View.VISIBLE);
        ibtnCardDelete.setVisibility(View.VISIBLE);
        ibtnCardDelete.setEnabled(true);

        tvCardUserName.setText(Global.User.name);
        tvCardCardNum.setText(cardItem.getNum());
    }

    private void removeCardData() {
        tvCardNoCard.setText("터치하여 카드를 등록하세요");
        clCardUiGroup.setVisibility(View.GONE);
        tvCardNoCard.setVisibility(View.VISIBLE);
        ibtnCardDelete.setVisibility(View.INVISIBLE);
        ibtnCardDelete.setEnabled(false);
        cardItem.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                sendData.put("request_code", Global.Network.Request.CARD_INFO);

                JSONObject message = new JSONObject();
                message.accumulate("id", Global.User.id);

                sendData.accumulate("message", message);

                String temp = new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, sendData.toString()}).get();

                JSONObject recvData = new JSONObject(temp);

                String responseCode = recvData.getString("response_code");

                if (responseCode.equals(Global.Network.Response.CARD_INFO)) {
                    // 카드 불러오기 성공
                    runOnUiThread(() -> {
                        try {
                            cardItem.setNum(recvData.getJSONObject("message").getString("num"));
                            cardItem.setDueDate(recvData.getJSONObject("message").getString("due_date"));
                            cardItem.setCvc(recvData.getJSONObject("message").getString("cvc"));
                            setCardData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    if (responseCode.equals(Global.Network.Response.CARD_NOINFO)) {
                        // 카드 없음
                        runOnUiThread(() -> {
                            removeCardData();
                        });
                    } else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {
                        // 서버 연결 실패
                        runOnUiThread(() -> {
                            removeCardData();
                            Toast.makeText(CardActivity.this, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        // 카드 불러오기 실패
                        runOnUiThread(() -> {
                            removeCardData();
                            tvCardNoCard.setText("카드를 불러올 수 없습니다");
                        });
                    }
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
                sendData.put("request_code", Global.Network.Request.CARD_REMOVE);

                JSONObject message = new JSONObject();
                message.accumulate("id", Global.User.id);

                sendData.accumulate("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, sendData.toString()}).get());

                String responseCode = recvData.getString("response_code");

                if (responseCode.equals(Global.Network.Response.CARD_REMOVE_SUCCESS)) {
                    // 카드 제거 성공
                    runOnUiThread(() -> {
                        removeCardData();
                    });
                } else if (responseCode.equals(Global.Network.Response.CARD_REMOVE_FAILED)) {
                    // 카드 제거 실패
                    runOnUiThread(() -> {
                        new GetCard().start();
                    });
                } else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {
                    // 서버 연결 실패
                    runOnUiThread(() -> {
                        removeCardData();
                        Toast.makeText(CardActivity.this, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
                    });
                } else {
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
}