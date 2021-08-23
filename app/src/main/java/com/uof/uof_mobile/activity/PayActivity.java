package com.uof.uof_mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uof.uof_mobile.R;
import com.uof.uof_mobile.adapter.PayAdapter;
import com.uof.uof_mobile.manager.HttpManager;
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
    private RadioButton rbPayDirect;
    private LinearLayoutCompat llPayPay;
    private PayAdapter payAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        init();
    }

    private void init() {
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
        rbPayDirect = findViewById(R.id.rb_pay_direct);
        llPayPay = findViewById(R.id.ll_pay_pay);

        tvPayCompanyName.setText(Global.basketManager.getCompanyName());
        tvPayTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(Global.basketManager.getOrderPrice()) + "원");
        tvPayUserName.setText(Global.User.name);

        removeCardData();
        new PayActivity.GetCard().start();

        payAdapter = new PayAdapter();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(PayActivity.this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        rvPayOrderList.addItemDecoration(dividerItemDecoration);
        rvPayOrderList.setLayoutManager(new LinearLayoutManager(PayActivity.this, LinearLayoutManager.VERTICAL, false));
        rvPayOrderList.setAdapter(payAdapter);

        rbPayCard.setChecked(true);

        // 뒤로가기 버튼 눌릴 시
        ibtnPayBack.setOnClickListener(view -> {
            finish();
        });

        // 결제수단 변경 시
        rgPayPayment.setOnCheckedChangeListener((radioGroup, id) -> {
            if (id == R.id.rb_pay_card) {
                ivPayCardBackground.setBackground(getDrawable(R.drawable.ripple_cardimage));
            } else if (id == R.id.rb_pay_direct) {
                ivPayCardBackground.setBackground(getDrawable(R.drawable.background_pay_carddisabled));
            }
        });

        // 카드이미지 눌릴 시
        ivPayCardBackground.setOnClickListener(view -> {
            if (rbPayCard.isChecked()) {
                goToCardActivity.launch(new Intent(PayActivity.this, CardActivity.class));
            }
        });

        // 결제하기 버튼 눌릴 시
        llPayPay.setOnClickListener(view -> {

        });
    }

    ActivityResultLauncher<Intent> goToCardActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult()
            , result -> {
                new PayActivity.GetCard().start();
            }
    );

    private void setCardData(String cardNum) {
        tvPayNoCard.setVisibility(View.GONE);
        clPayCard.setVisibility(View.VISIBLE);

        tvPayUserName.setText(Global.User.name);
        tvPayCardNum.setText(cardNum);
    }

    private void removeCardData() {
        tvPayNoCard.setText("등록된 카드가 없습니다");
        clPayCard.setVisibility(View.GONE);
        tvPayNoCard.setVisibility(View.VISIBLE);
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
                    String cardNum = recvData.getJSONObject("message").getString("num");
                    runOnUiThread(() -> {
                        setCardData(cardNum);
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