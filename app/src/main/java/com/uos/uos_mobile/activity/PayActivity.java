package com.uos.uos_mobile.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

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
import com.uos.uos_mobile.dialog.PayResultDialog;
import com.uos.uos_mobile.item.CardItem;
import com.uos.uos_mobile.manager.BasketManager;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.manager.PatternManager;
import com.uos.uos_mobile.manager.UsefulFuncManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * QR코드를 인식 후 데이터를 추출하는 Activity.<br>
 * xml: activity_qrrecognition.xml<br><br>
 *
 * Intent로 전달된 BasketManager로 결제될 상품들에 대한 정보를 표시합니다. 장바구니 목록 아래에는 카드정보를
 * 입력하는 란이 있으며 하단에 있는 결제하기 버튼을 누를 시 주문이 진행됩니다.
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class PayActivity extends UosActivity {

    /**
     * 결제 Activity를 종료하는 AppCompatImageButton.
     */
    private AppCompatImageButton ibtnPayBack;

    /**
     * 주문하려는 매장명(회사명)을 표시하는 AppCompatTextView.
     */
    private AppCompatTextView tvPayCompanyName;

    /**
     * 장바구니에 있는 목록을 보여주는 RecyclerView.
     */
    private RecyclerView rvPayOrderList;

    /**
     * 결제할 총 금액을 표시하는 AppCompatTextView.
     */
    private AppCompatTextView tvPayTotalPrice;

    /**
     * 카드가 없을 때 보일 문자열을 담고 있는 AppCompatTextView.
     */
    private AppCompatTextView tvPayNoCard;

    /**
     * 터치 시 카드정보를 수정할 수 있는 ConstraintLayout.
     */
    private ConstraintLayout clPayCard;

    /**
     * 카드 배경을 표시하는 AppCompatImageView.
     */
    private AppCompatImageView ivPayCardBackground;

    /**
     * 사용자 이름을 표시하는 AppCompatTextView.
     */
    private AppCompatTextView tvPayUserName;

    /**
     * 카드번호를 표시하는 AppCompatTextView.
     */
    private AppCompatTextView tvPayCardNum;

    /**
     * 카드 비밀번호 입력란 TextInputLayout.
     */
    private TextInputLayout tilPayCardPw;

    /**
     * 터치 시 결제를 진행하는 ConstraintLayout.
     */
    private ConstraintLayout clPayPay;

    /**
     * 결제 전 버튼에 표시할 AppCompatTextView.
     */
    private AppCompatTextView tvPayPay;

    /**
     * 결제 진행 시 표시할 ContentLoadingProgressBar.
     */
    private ContentLoadingProgressBar pbPayLoading;

    /**
     * 결제할 장바구니 목록을 관리하는 PayAdapter.
     */
    private PayAdapter payAdapter;

    /**
     * 카드정보를 담고 있는 CardItem.
     */
    private CardItem cardItem;

    /**
     * Intent로 전달된 BasketManager.
     */
    private BasketManager basketManager;

    @Override
    protected void init() {
        setContentView(com.uos.uos_mobile.R.layout.activity_pay);

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

        basketManager = (BasketManager) getIntent().getSerializableExtra("basketManager");

        tvPayPay.setVisibility(View.VISIBLE);
        pbPayLoading.setVisibility(View.INVISIBLE);

        tvPayCompanyName.setText(basketManager.getCompanyName());

        tvPayTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(basketManager.getOrderPrice()) + "원");

        tvPayUserName.setText(Global.User.name);

        cardItem = new CardItem();
        removeCardData();
        new PayActivity.GetCard().start();

        payAdapter = new PayAdapter(basketManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(PayActivity.this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getDrawable(com.uos.uos_mobile.R.drawable.recyclerview_divider));
        rvPayOrderList.addItemDecoration(dividerItemDecoration);
        rvPayOrderList.setLayoutManager(new LinearLayoutManager(PayActivity.this, LinearLayoutManager.VERTICAL, false));
        rvPayOrderList.setAdapter(payAdapter);

        clPayPay.setEnabled(false);
        clPayPay.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));

        /* 뒤로가기 버튼이 눌릴 경우 */
        ibtnPayBack.setOnClickListener(view -> {
            finish();
        });

        /* 카드영역이 눌릴 경우 */
        ivPayCardBackground.setOnClickListener(view -> {
            CardDialog cardDialog = new CardDialog(PayActivity.this, true, true, cardItem);
            cardDialog.setOnDismissListener(dialogInterface -> {
                new PayActivity.GetCard().start();
            });
            cardDialog.show();
        });

        /* 카드 비밀번호를 입력할 경우 */
        tilPayCardPw.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(tilPayCardPw.getError() != null && tilPayCardPw.getError().toString().equals("비밀번호가 틀렸습니다")){
                    tilPayCardPw.setError("null");
                    tilPayCardPw.setErrorEnabled(false);
                }

                checkPayEnable();
            }
        });

        /* 결제하기 버튼이 눌릴 경우 */
        clPayPay.setOnClickListener(view -> {
            clPayPay.setEnabled(false);
            clPayPay.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
            tvPayPay.setVisibility(View.INVISIBLE);
            pbPayLoading.setVisibility(View.VISIBLE);

            new Thread(() -> {
                try {
                    JSONObject cardData = new JSONObject();
                    cardData.accumulate("num", cardItem.getNum());
                    cardData.accumulate("cvc", cardItem.getCvc());
                    cardData.accumulate("due_date", cardItem.getDueDate());
                    cardData.accumulate("pw", tilPayCardPw.getEditText().getText().toString());

                    JSONObject message = new JSONObject();
                    message.accumulate("uospartner_id", basketManager.getUosPartnerId());
                    message.accumulate("customer_id", Global.User.id);
                    message.accumulate("card", cardData);
                    message.accumulate("order", basketManager.getJson());

                    JSONObject sendData = new JSONObject();
                    sendData.accumulate("request_code", Global.Network.Request.ORDER);
                    sendData.accumulate("message", message);

                    JSONObject orderResult = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(HttpManager.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());

                    if (orderResult.getString("response_code").equals(Global.Network.Response.PAY_SUCCESS)) {

                        /* 결제 성공 */

                        runOnUiThread(() -> {
                            PayResultDialog payResultDialog = new PayResultDialog(PayActivity.this, true, false, basketManager);
                            payResultDialog.setOnDismissListener(dialogInterface -> {
                                UosActivity.revertToActivity(LobbyActivity.class);
                                finish();
                            });
                            payResultDialog.show();
                        });
                    } else if (orderResult.getString("response_code").equals(Global.Network.Response.PAY_FAIL_WRONG_PASSWORD)) {

                        /* 결제 실패 */

                        runOnUiThread(() -> {
                            tilPayCardPw.setError("비밀번호가 틀렸습니다");
                            tilPayCardPw.setErrorEnabled(true);
                        });

                    } else {

                        /* 주문 접수 실패 - 기타 오류 */

                        runOnUiThread(() -> {
                            Toast.makeText(PayActivity.this, "주문 접수 중 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (JSONException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(PayActivity.this, "주문 접수 중 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                    });
                }

                runOnUiThread(() -> {
                    clPayPay.setEnabled(true);
                    clPayPay.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
                    tvPayPay.setVisibility(View.VISIBLE);
                    pbPayLoading.setVisibility(View.INVISIBLE);
                });
            }).start();
        });
    }

    /**
     * 결제가 가능한 상태인지 확인합니다. 카드정보가 등록되어있는지, 카드 비밀번호가 입력 패턴에 맞는지 확인하며
     * 조건을 모두 만족시킬 경우 결제버튼을 활성화, 그렇지 않을 경우 비활성화시킵니다.
     */
    public void checkPayEnable() {
        int result = PatternManager.checkCardPw(tilPayCardPw.getEditText().getText().toString());

        if (result == PatternManager.OK) {

            /* 입력된 비밀번호가 조건에 맞을 경우 */

            tilPayCardPw.setError(null);
            tilPayCardPw.setErrorEnabled(false);

            if (tvPayNoCard.getVisibility() == View.GONE) {

                /* 카드정보가 등록되어있을 경우 */

                clPayPay.setEnabled(true);
                clPayPay.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.color_primary));
            } else {
                
                /* 카드정보가 등록되어있지 않을 경우 */

                clPayPay.setEnabled(false);
                clPayPay.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
            }
        } else if (result == PatternManager.LENGTH_SHORT) {

            /* 입력된 비밀번호가 조건에 맞지 않을 경우 - 비밀번호가 짧을 경우*/

            clPayPay.setEnabled(false);
            clPayPay.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
            tilPayCardPw.setError("카드 비밀번호는 네 자리 숫자입니다");
            tilPayCardPw.setErrorEnabled(true);
        } else if (result == PatternManager.NOT_ALLOWED_CHARACTER) {

            /* 입력된 비밀번호가 조건에 맞지 않을 경우 - 허용되지 않은 문자가 포함되어있을 경우 */
            
            clPayPay.setEnabled(false);
            clPayPay.setBackgroundColor(getResources().getColor(com.uos.uos_mobile.R.color.gray));
            tilPayCardPw.setError("숫자만 입력가능합니다");
            tilPayCardPw.setErrorEnabled(true);
        }
    }

    /**
     * 카드정보를 PayActivity에 표시합니다. 카드정보를 성공적으로 불러왔을 때 사용합니다.
     *
     * @param cardNum 카드번호.
     */
    private void setCardData(String cardNum) {
        tvPayNoCard.setVisibility(View.GONE);
        clPayCard.setVisibility(View.VISIBLE);
        tvPayUserName.setText(Global.User.name);
        tvPayCardNum.setText(cardNum);
        checkPayEnable();
    }

    /**
     * 카드정보를 PayActivity에서 표시하지 않습니다. 등록된 카드정보가 없거나 카드정보를 불러오는 도중 오류가
     * 발생했을 때 사용합니다.
     */
    private void removeCardData() {
        tvPayNoCard.setText("터치하여 카드를 등록하세요");
        clPayCard.setVisibility(View.GONE);
        tvPayNoCard.setVisibility(View.VISIBLE);
        cardItem.clear();
        checkPayEnable();
    }

    /**
     * 카드정보를 외부서버로부터 불러옵니다.
     */
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
                message.accumulate("customer_id", Global.User.id);

                sendData.accumulate("message", message);

                String temp = new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(HttpManager.DEFAULT_READ_TIMEOUT), sendData.toString()}).get();

                JSONObject recvData = new JSONObject(temp);

                String responseCode = recvData.getString("response_code");

                if (responseCode.equals(Global.Network.Response.CARD_INFO)) {

                    /* 카드 불러오기 성공 */

                    runOnUiThread(() -> {
                        try {
                            cardItem.setNum(recvData.getJSONObject("message").getString("num"));
                            cardItem.setCvc(recvData.getJSONObject("message").getString("cvc"));
                            cardItem.setDueDate(recvData.getJSONObject("message").getString("due_date"));
                            setCardData(cardItem.getNum());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                } else if (responseCode.equals(Global.Network.Response.CARD_NO_INFO)) {

                    /* 카드 없음 */

                    runOnUiThread(() -> {
                        removeCardData();
                    });
                } else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {

                    /* 서버 연결 실패 */

                    runOnUiThread(() -> {
                        Toast.makeText(PayActivity.this, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
                    });
                } else {

                    /* 카드 불러오기 실패 */

                    runOnUiThread(() -> {
                        removeCardData();
                        tvPayNoCard.setText("카드를 불러오는 도중 오류가 발생했습니다");
                    });
                }
            } catch (JSONException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(PayActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    removeCardData();
                });
            }
        }
    }
}