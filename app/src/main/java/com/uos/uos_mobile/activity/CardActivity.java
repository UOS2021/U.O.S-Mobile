package com.uos.uos_mobile.activity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.uos.uos_mobile.dialog.CardDialog;
import com.uos.uos_mobile.item.CardItem;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONObject;

/**
 * 사용자의 카드정보를 관리하는 Activity.<br>
 * xml: activity_card.xml<br><br>
 *
 * 사용자가 결제 시 사용할 카드정보를 관리하는 Activity입니다. 카드영역 터치 시 카드정보를 수정할 수 있는
 * CardDialog가 표시됩니다.
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class CardActivity extends UosActivity {

    /**
     * CardActivity를 종료하는 AppCompatImageButton.
     */
    private AppCompatImageButton ibtnCardBack;

    /**
     * 등록된 카드정보를 제거하는 AppCompatImageButton.
     */
    private AppCompatImageButton ibtnCardDelete;

    /**
     * 카드 배경 표시 AppCompatImageView.
     */
    private AppCompatImageView ivCardBackground;

    /**
     * 등록된 카드 없음을 표시하는 AppCompatTextView.
     */
    private AppCompatTextView tvCardNoCard;

    /**
     * 카드정보(사용자명, 카드번호, 만료날짜)를 표시하는 AppCompatTextView들을 묶어둔 ConstraintLayout.
     */
    private ConstraintLayout clCardUiGroup;

    /**
     * 사용자 이름을 표시하는 AppCompatTextView.
     */
    private AppCompatTextView tvCardUserName;

    /**
     * 만료날짜를 표시하는 AppCompatTextView.
     */
    private AppCompatTextView tvCardExpireDate;

    /**
     * 카드번호를 표시하는 AppCompatTextView.
     */
    private AppCompatTextView tvCardCardNum;

    /**
     * 카드정보를 담고 있는 CardItem 객체.
     */
    private CardItem cardItem;

    @Override
    protected void init() {
        setContentView(com.uos.uos_mobile.R.layout.activity_card);

        ibtnCardBack = findViewById(com.uos.uos_mobile.R.id.ibtn_card_back);
        ibtnCardDelete = findViewById(com.uos.uos_mobile.R.id.ibtn_card_delete);
        ivCardBackground = findViewById(com.uos.uos_mobile.R.id.iv_card_background);
        tvCardNoCard = findViewById(com.uos.uos_mobile.R.id.tv_card_nocard);
        clCardUiGroup = findViewById(com.uos.uos_mobile.R.id.cl_card_uigroup);
        tvCardUserName = findViewById(com.uos.uos_mobile.R.id.tv_card_username);
        tvCardExpireDate = findViewById(com.uos.uos_mobile.R.id.tv_card_expiredate);
        tvCardCardNum = findViewById(com.uos.uos_mobile.R.id.tv_card_cardnum);

        cardItem = new CardItem();

        /* 초기 UI 상태 설정 */
        removeCardData();
        new GetCard().start();

        /* 종료 버튼이 눌렸을 경우 */
        ibtnCardBack.setOnClickListener(view -> {
            finish();
        });

        /* 삭제 버튼이 눌렸을 경우 */
        ibtnCardDelete.setOnClickListener(view -> {
            AlertDialog alertDialog = new AlertDialog.Builder(CardActivity.this, com.uos.uos_mobile.R.style.AlertDialogTheme)
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

        /* 카드이미지가 눌렸을 경우 */
        ivCardBackground.setOnClickListener(view -> {
            CardDialog cardDialog = new CardDialog(CardActivity.this, true, true, cardItem);
            cardDialog.setOnDismissListener(dialogInterface -> {
                new GetCard().start();
            });
            cardDialog.show();
        });
    }

    /**
     * cardItem 객체에 저장된 정보를 토대로 UI를 업데이트합니다.
     */
    private void setCardData() {
        tvCardNoCard.setVisibility(View.GONE);
        clCardUiGroup.setVisibility(View.VISIBLE);
        ibtnCardDelete.setVisibility(View.VISIBLE);
        ibtnCardDelete.setEnabled(true);

        tvCardUserName.setText(Global.User.name);
        tvCardExpireDate.setText(cardItem.getDueDate());
        tvCardCardNum.setText(cardItem.getNum());
    }

    /**
     * cardItem 객체에 저장된 카드정보를 초기화하고 UI를 업데이트합니다.
     */
    private void removeCardData() {
        tvCardNoCard.setText("터치하여 카드를 등록하세요");
        clCardUiGroup.setVisibility(View.GONE);
        tvCardNoCard.setVisibility(View.VISIBLE);
        ibtnCardDelete.setVisibility(View.INVISIBLE);
        ibtnCardDelete.setEnabled(false);
        cardItem.clear();
    }

    /**
     * Thread 클래스를 상속받아 카드 정보 요청을 보내는 클래스.
     */
    private class GetCard extends Thread {
        @Override
        public void run() {

            runOnUiThread(() -> {
                removeCardData();
                tvCardNoCard.setText("등록된 카드를 불러오는 중입니다...");
            });

            try {
                JSONObject message = new JSONObject();
                message.accumulate("customer_id", Global.User.id);

                JSONObject sendData = new JSONObject();
                sendData.accumulate("request_code", Global.Network.Request.CARD_INFO);
                sendData.accumulate("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(HttpManager.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());
                String responseCode = recvData.getString("response_code");

                if (responseCode.equals(Global.Network.Response.CARD_INFO)) {

                    /* 카드 불러오기 성공 */

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
                    if (responseCode.equals(Global.Network.Response.CARD_NO_INFO)) {

                        /* 카드 없음 */

                        runOnUiThread(() -> {
                            removeCardData();
                        });
                    } else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {

                        /* 서버 연결 실패 */

                        runOnUiThread(() -> {
                            Toast.makeText(CardActivity.this, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
                        });
                    } else {

                        /* 카드 불러오기 실패 */

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

    /**
     * Thread 클래스를 상속받아 카드 제거 요청을 보내는 클래스.
     */
    private class RemoveCard extends Thread {
        @Override
        public void run() {

            runOnUiThread(() -> {
                removeCardData();
                tvCardNoCard.setText("카드 제거중입니다...");
            });

            try {
                JSONObject message = new JSONObject();
                message.accumulate("customer_id", Global.User.id);

                JSONObject sendData = new JSONObject();
                sendData.accumulate("request_code", Global.Network.Request.CARD_REMOVE);
                sendData.accumulate("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(HttpManager.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());
                String responseCode = recvData.getString("response_code");

                if (responseCode.equals(Global.Network.Response.CARD_REMOVE_SUCCESS)) {

                    /* 카드 제거 성공 */

                    runOnUiThread(() -> {
                        removeCardData();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(() -> {
                    Toast.makeText(CardActivity.this, "카드 제거 도중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                });
            }
        }
    }
}