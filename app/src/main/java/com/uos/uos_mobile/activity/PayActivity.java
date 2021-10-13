package com.uos.uos_mobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import com.uos.uos_mobile.adapter.PayAdapter;
import com.uos.uos_mobile.dialog.CardDialog;
import com.uos.uos_mobile.dialog.WaitingOrderDialog;
import com.uos.uos_mobile.item.CardItem;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.manager.PatternManager;
import com.uos.uos_mobile.manager.UsefulFuncManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONObject;

public class PayActivity extends UosActivity {
    private AppCompatImageButton ibtnPayBack;
    private AppCompatTextView tvPayCompanyName;
    private RecyclerView rvPayOrderList;
    private AppCompatTextView tvPayTotalPrice;
    private AppCompatTextView tvPayNoCard;
    private ConstraintLayout clPayCard;
    private AppCompatImageView ivPayCardBackground;
    private AppCompatTextView tvPayUserName;
    private AppCompatTextView tvPayCardNum;
    private TextInputLayout tilPayCardPw;
    private ConstraintLayout clPayPay;
    private AppCompatTextView tvPayPay;
    private ContentLoadingProgressBar pbPayLoading;
    private WaitingOrderDialog waitingOrderDialog;

    private PayAdapter payAdapter;

    private CardItem cardItem;

    private String uosPartnerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.uos.uos_mobile.R.layout.activity_pay);

        init();
    }

    private void init() {
        ibtnPayBack = findViewById(com.uos.uos_mobile.R.id.ibtn_pay_back);
        tvPayCompanyName = findViewById(com.uos.uos_mobile.R.id.tv_pay_companyname);
        rvPayOrderList = findViewById(com.uos.uos_mobile.R.id.rv_pay_orderlist);
        tvPayTotalPrice = findViewById(com.uos.uos_mobile.R.id.tv_pay_totalprice);
        tvPayNoCard = findViewById(com.uos.uos_mobile.R.id.tv_pay_nocard);
        clPayCard = findViewById(com.uos.uos_mobile.R.id.cl_pay_card);
        ivPayCardBackground = findViewById(com.uos.uos_mobile.R.id.iv_pay_cardbackground);
        tvPayUserName = findViewById(com.uos.uos_mobile.R.id.tv_pay_username);
        tvPayCardNum = findViewById(com.uos.uos_mobile.R.id.tv_pay_cardnum);
        tilPayCardPw = findViewById(com.uos.uos_mobile.R.id.til_pay_cardpw);
        clPayPay = findViewById(com.uos.uos_mobile.R.id.cl_pay_pay);
        tvPayPay = findViewById(com.uos.uos_mobile.R.id.tv_pay_pay);
        pbPayLoading = findViewById(com.uos.uos_mobile.R.id.pb_pay_loading);

        tvPayPay.setVisibility(View.VISIBLE);
        pbPayLoading.setVisibility(View.INVISIBLE);

        tvPayCompanyName.setText(Global.basketManager.getCompanyName());

        tvPayTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(Global.basketManager.getOrderPrice()) + "원");

        tvPayUserName.setText(Global.User.name);

        cardItem = new CardItem();
        removeCardData();
        new PayActivity.GetCard().start();

        uosPartnerId = getIntent().getStringExtra("uosPartnerId");

        payAdapter = new PayAdapter();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(PayActivity.this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getDrawable(com.uos.uos_mobile.R.drawable.recyclerview_divider));
        rvPayOrderList.addItemDecoration(dividerItemDecoration);
        rvPayOrderList.setLayoutManager(new LinearLayoutManager(PayActivity.this, LinearLayoutManager.VERTICAL, false));
        rvPayOrderList.setAdapter(payAdapter);

        clPayPay.setEnabled(false);
        clPayPay.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));

        // 뒤로가기 버튼 눌릴 시
        ibtnPayBack.setOnClickListener(view -> {
            finish();
        });

        // 카드이미지 눌릴 시
        ivPayCardBackground.setOnClickListener(view -> {
            CardDialog cardDialog = new CardDialog(PayActivity.this, true, true, cardItem);
            cardDialog.setOnDismissListener(dialogInterface -> {
                new PayActivity.GetCard().start();
            });
            cardDialog.show();
        });

        // 카드 비밀번호 입력 시
        tilPayCardPw.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkPayEnable();
            }
        });

        // 결제하기 버튼 눌릴 시
        clPayPay.setOnClickListener(view -> {
            new Thread(() -> {
                runOnUiThread(() -> {
                    tvPayPay.setVisibility(View.INVISIBLE);
                    pbPayLoading.setVisibility(View.VISIBLE);
                });

                try {
                    JSONObject cardData = new JSONObject();
                    cardData.accumulate("num", cardItem.getNum());
                    cardData.accumulate("cvc", cardItem.getCvc());
                    cardData.accumulate("due_date", cardItem.getDueDate());
                    cardData.accumulate("pw", tilPayCardPw.getEditText().getText().toString());

                    JSONObject message = new JSONObject();
                    message.accumulate("uospartner_id", uosPartnerId);
                    message.accumulate("customer_id", Global.User.id);
                    message.accumulate("fcm_token", Global.Firebase.FCM_TOKEN);
                    message.accumulate("card", cardData);
                    message.accumulate("order", Global.basketManager.getJson());

                    JSONObject sendData = new JSONObject();
                    sendData.accumulate("request_code", Global.Network.Request.ORDER);
                    sendData.accumulate("message", message);

                    JSONObject orderResult = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(HttpManager.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());

                    if (orderResult.getString("response_code").equals(Global.Network.Response.ORDER_SUCCESS)) {
                        // 주문접수 성공 시
                        runOnUiThread(() -> {
                            clPayPay.setEnabled(false);
                            clPayPay.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
                            waitingOrderDialog = new WaitingOrderDialog(PayActivity.this, true, false, tvPayCompanyName.getText().toString(), sendData, uosPartnerId);
                            waitingOrderDialog.setOnDismissListener(dialogInterface -> {
                                UosActivity.revertToActivity(LobbyActivity.class);
                                finish();
                            });
                            waitingOrderDialog.show();
                        });
                    } else if (orderResult.getString("response_code").equals(Global.Network.Response.ORDER_FAILED)) {
                        // 주문접수 실패 시
                        runOnUiThread(() -> {
                            Toast.makeText(PayActivity.this, "주문 접수 중 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(PayActivity.this, "주문 접수 중 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(PayActivity.this, "매장 통신 중 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                    });
                }

                runOnUiThread(() -> {
                    tvPayPay.setVisibility(View.VISIBLE);
                    pbPayLoading.setVisibility(View.INVISIBLE);
                });
            }).start();
        });
    }

    public void checkPayEnable() {
        int result = PatternManager.checkCardPw(tilPayCardPw.getEditText().getText().toString());

        if (result == PatternManager.OK) {
            tilPayCardPw.setError(null);
            tilPayCardPw.setErrorEnabled(false);
            if (tvPayNoCard.getVisibility() == View.GONE) {
                clPayPay.setEnabled(true);
                clPayPay.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
            } else {
                clPayPay.setEnabled(false);
                clPayPay.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
            }
        } else if (result == PatternManager.LENGTH_SHORT) {
            clPayPay.setEnabled(false);
            clPayPay.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
            tilPayCardPw.setError("카드 비밀번호는 네 자리 숫자입니다");
            tilPayCardPw.setErrorEnabled(true);
        } else if (result == PatternManager.NOT_ALLOWED_CHARACTER) {
            clPayPay.setEnabled(false);
            clPayPay.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
            tilPayCardPw.setError("숫자만 입력가능합니다");
            tilPayCardPw.setErrorEnabled(true);
        }
    }

    private void setCardData(String cardNum) {
        tvPayNoCard.setVisibility(View.GONE);
        clPayCard.setVisibility(View.VISIBLE);
        tvPayUserName.setText(Global.User.name);
        tvPayCardNum.setText(cardNum);
        checkPayEnable();
    }

    private void removeCardData() {
        tvPayNoCard.setText("터치하여 카드를 등록하세요");
        clPayCard.setVisibility(View.GONE);
        tvPayNoCard.setVisibility(View.VISIBLE);
        cardItem.clear();
        checkPayEnable();
    }

    private class GetCard extends Thread {
        @Override
        public void run() {
            runOnUiThread(() -> {
                removeCardData();
                tvPayNoCard.setText("등록된 카드를 불러오는 중입니다...");
            });
            try {
                JSONObject sendData = new JSONObject();
                sendData.accumulate("request_code", Global.Network.Request.CARD_INFO);

                JSONObject message = new JSONObject();
                message.accumulate("id", Global.User.id);

                sendData.accumulate("message", message);

                String temp = new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(HttpManager.DEFAULT_READ_TIMEOUT), sendData.toString()}).get();

                JSONObject recvData = new JSONObject(temp);

                String responseCode = recvData.getString("response_code");

                if (responseCode.equals(Global.Network.Response.CARD_INFO)) {
                    // 카드 불러오기 성공
                    runOnUiThread(() -> {
                        try {
                            cardItem.setNum(recvData.getJSONObject("message").getString("num"));
                            cardItem.setCvc(recvData.getJSONObject("message").getString("cvc"));
                            cardItem.setDueDate(recvData.getJSONObject("message").getString("due_date"));
                            setCardData(cardItem.getNum());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } else if (responseCode.equals(Global.Network.Response.CARD_NOINFO)) {
                    // 카드 없음
                    runOnUiThread(() -> {
                        removeCardData();
                    });
                } else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {
                    // 서버 연결 실패
                    runOnUiThread(() -> {
                        removeCardData();
                        Toast.makeText(PayActivity.this, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // 카드 불러오기 실패
                    runOnUiThread(() -> {
                        removeCardData();
                        tvPayNoCard.setText("카드를 불러올 수 없습니다");
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(PayActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    removeCardData();
                });
            }
        }
    }
}