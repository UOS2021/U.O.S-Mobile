package com.uof.uof_mobile.activity;

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
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.adapter.PayAdapter;
import com.uof.uof_mobile.dialog.CardDialog;
import com.uof.uof_mobile.dialog.WaitingOrderDialog;
import com.uof.uof_mobile.item.CardItem;
import com.uof.uof_mobile.manager.HttpManager;
import com.uof.uof_mobile.manager.PatternManager;
import com.uof.uof_mobile.manager.UsefulFuncManager;
import com.uof.uof_mobile.other.Global;

import org.json.JSONObject;

public class PayActivity extends AppCompatActivity {
    private AppCompatImageButton ibtnPayBack;
    private AppCompatTextView tvPayCompanyName;
    private RecyclerView rvPayOrderList;
    private AppCompatTextView tvPayTotalPrice;
    private RadioGroup rgPayPayment;
    private RadioButton rbPayCard;
    private AppCompatTextView tvPayNoCard;
    private ConstraintLayout clPayCard;
    private AppCompatImageView ivPayCardBackground;
    private AppCompatTextView tvPayUserName;
    private AppCompatTextView tvPayCardNum;
    private TextInputLayout tilPayCardPw;
    private RadioButton rbPayDirect;
    private ConstraintLayout clPayPay;
    private AppCompatTextView tvPayPay;
    private ContentLoadingProgressBar pbPayLoading;
    private WaitingOrderDialog waitingOrderDialog;

    private PayAdapter payAdapter;

    private CardItem cardItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        init();
    }

    @Override
    protected void onDestroy() {
        Global.activities.remove(this);
        super.onDestroy();
    }

    private void init() {
        Global.activities.add(this);
        ibtnPayBack = findViewById(R.id.ibtn_pay_back);
        tvPayCompanyName = findViewById(R.id.tv_pay_companyname);
        rvPayOrderList = findViewById(R.id.rv_pay_orderlist);
        tvPayTotalPrice = findViewById(R.id.tv_pay_totalprice);
        rgPayPayment = findViewById(R.id.rg_pay_payment);
        rbPayCard = findViewById(R.id.rb_pay_card);
        tvPayNoCard = findViewById(R.id.tv_pay_nocard);
        clPayCard = findViewById(R.id.cl_pay_card);
        ivPayCardBackground = findViewById(R.id.iv_pay_cardbackground);
        tvPayUserName = findViewById(R.id.tv_pay_username);
        tvPayCardNum = findViewById(R.id.tv_pay_cardnum);
        tilPayCardPw = findViewById(R.id.til_pay_cardpw);
        rbPayDirect = findViewById(R.id.rb_pay_direct);
        clPayPay = findViewById(R.id.cl_pay_pay);
        tvPayPay = findViewById(R.id.tv_pay_pay);
        pbPayLoading = findViewById(R.id.pb_pay_loading);

        tvPayPay.setVisibility(View.VISIBLE);
        pbPayLoading.setVisibility(View.INVISIBLE);

        tvPayCompanyName.setText(Global.basketManager.getCompanyName());
        tvPayTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(Global.basketManager.getOrderPrice()) + "원");
        tvPayUserName.setText(Global.User.name);

        cardItem = new CardItem();
        removeCardData();
        new PayActivity.GetCard().start();

        payAdapter = new PayAdapter();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(PayActivity.this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.recyclerview_divider));
        rvPayOrderList.addItemDecoration(dividerItemDecoration);
        rvPayOrderList.setLayoutManager(new LinearLayoutManager(PayActivity.this, LinearLayoutManager.VERTICAL, false));
        rvPayOrderList.setAdapter(payAdapter);

        rbPayCard.setChecked(true);
        clPayPay.setEnabled(false);
        clPayPay.setBackgroundColor(getResources().getColor(R.color.gray));

        // 뒤로가기 버튼 눌릴 시
        ibtnPayBack.setOnClickListener(view -> {
            finish();
        });

        // 결제수단 변경 시
        rgPayPayment.setOnCheckedChangeListener((radioGroup, id) -> {
            if (id == R.id.rb_pay_card) {
                ivPayCardBackground.setBackground(getDrawable(R.drawable.ripple_cardimage));
                tilPayCardPw.getEditText().setEnabled(true);

                checkPayEnable();
            } else if (id == R.id.rb_pay_direct) {
                ivPayCardBackground.setBackground(getDrawable(R.drawable.background_pay_carddisabled));
                tilPayCardPw.getEditText().setEnabled(false);
                tilPayCardPw.setError(null);
                tilPayCardPw.setErrorEnabled(false);
                clPayPay.setEnabled(true);
                clPayPay.setBackgroundColor(getResources().getColor(R.color.color_primary));
            }
        });

        // 카드이미지 눌릴 시
        ivPayCardBackground.setOnClickListener(view -> {
            if (rbPayCard.isChecked()) {
                CardDialog cardDialog = new CardDialog(PayActivity.this, true, true, cardItem);
                cardDialog.setOnDismissListener(dialogInterface -> {
                    new PayActivity.GetCard().start();
                });
                cardDialog.show();
            }
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
            if (rbPayCard.isChecked()) {
                if (tvPayNoCard.getVisibility() == View.GONE) {
                    tvPayPay.setVisibility(View.INVISIBLE);
                    pbPayLoading.setVisibility(View.VISIBLE);
                    new Thread(() -> {
                        try {
                            JSONObject sendData = new JSONObject();

                            sendData.accumulate("request_code", Global.Network.Request.ORDER);

                            JSONObject message = new JSONObject();

                            message.accumulate("id", Global.User.id);
                            message.accumulate("fcm_token", Global.Firebase.FCM_TOKEN);

                            JSONObject cardData = new JSONObject();

                            cardData.accumulate("num", cardItem.getNum());
                            cardData.accumulate("cvc", cardItem.getCvc());
                            cardData.accumulate("due_date", cardItem.getDueDate());
                            cardData.accumulate("pw", tilPayCardPw.getEditText().getText().toString());

                            message.accumulate("cardItem", cardData);
                            message.accumulate("order", Global.basketManager.getJson());

                            sendData.accumulate("message", message);

                            if (Global.socketManager.isSocketConnected()) {
                                Global.socketManager.send(sendData.toString());
                                String recvData = Global.socketManager.recv();
                                JSONObject orderResult = new JSONObject(recvData);

                                if (orderResult.getString("response_code").equals(Global.Network.Response.ORDER_SUCCESS)) {
                                    // 주문접수 성공 시
                                    runOnUiThread(() -> {
                                        clPayPay.setEnabled(false);
                                        clPayPay.setBackgroundColor(getResources().getColor(R.color.gray));
                                        waitingOrderDialog = new WaitingOrderDialog(PayActivity.this, true, false, tvPayCompanyName.getText().toString(), sendData);
                                        waitingOrderDialog.setOnDismissListener(dialogInterface -> {
                                            for (int loop = 0; loop < Global.activities.size(); loop++) {
                                                if (Global.activities.get(loop) instanceof OrderingActivity || Global.activities.get(loop) instanceof MovieOrderingActivity || Global.activities.get(loop) instanceof QRRecognitionActivity) {
                                                    Global.activities.get(loop).finish();
                                                    finish();
                                                }
                                            }
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
                            } else {
                                runOnUiThread(() -> {
                                    Toast.makeText(PayActivity.this, "매장 통신 중 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                                });
                            }
                        } catch (Exception e) {
                            runOnUiThread(() -> {
                                Toast.makeText(PayActivity.this, "매장 통신 중 문제가 발생했습니다" + e.toString(), Toast.LENGTH_SHORT).show();
                            });
                            e.printStackTrace();
                        }
                    }).start();
                } else {
                    Toast.makeText(PayActivity.this, "등록된 카드가 없습니다. 카드 등록 후 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                }
                tvPayPay.setVisibility(View.VISIBLE);
                pbPayLoading.setVisibility(View.INVISIBLE);
            } else if (rbPayDirect.isChecked()) {

            }
        });
    }

    public void checkPayEnable() {
        int result = PatternManager.checkCardPw(tilPayCardPw.getEditText().getText().toString());

        if (result == Global.Pattern.OK) {
            tilPayCardPw.setError(null);
            tilPayCardPw.setErrorEnabled(false);
            if (tvPayNoCard.getVisibility() == View.GONE) {
                clPayPay.setEnabled(true);
                clPayPay.setBackgroundColor(getResources().getColor(R.color.color_primary));
            } else {
                clPayPay.setEnabled(false);
                clPayPay.setBackgroundColor(getResources().getColor(R.color.gray));
            }
        } else if (result == Global.Pattern.LENGTH_SHORT) {
            clPayPay.setEnabled(false);
            clPayPay.setBackgroundColor(getResources().getColor(R.color.gray));
            tilPayCardPw.setError("카드 비밀번호는 네 자리 숫자입니다");
            tilPayCardPw.setErrorEnabled(true);
        } else if (result == Global.Pattern.NOT_ALLOWED_CHARACTER) {
            clPayPay.setEnabled(false);
            clPayPay.setBackgroundColor(getResources().getColor(R.color.gray));
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