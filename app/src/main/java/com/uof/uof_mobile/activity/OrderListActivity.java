package com.uof.uof_mobile.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.uof.uof_mobile.R;
import com.uof.uof_mobile.adapter.OrderListAdapter;
import com.uof.uof_mobile.dialog.OrderInfoDialog;
import com.uof.uof_mobile.manager.HttpManager;
import com.uof.uof_mobile.manager.SQLiteManager;
import com.uof.uof_mobile.other.Global;

import org.json.JSONObject;

public class OrderListActivity extends AppCompatActivity {
    private final String sampleData = "{\n" +
            "    \"response_code\": \"0013\"\n" +
            "    , \"message\": \n" +
            "    { \n" +
            "        \"order_list\": \n" +
            "        [\n" +
            "            { \n" +
            "                \"date\": \"2021-01-01\"\n" +
            "                , \"company_name\": \"버거킹\"\n" +
            "                , \"order\": \n" +
            "                [\n" +
            "                    { \"type\": 0, \"menu\": \"팝콘\", \"submenu\": \"\", \"count\": 1, \"price\": 1000 }\n" +
            "                    ,{ \"type\": 1, \"menu\": \"콜라\", \"submenu\": \"\", \"count\": 2, \"price\": 1000 }\n" +
            "                    ,{ \"type\": 0, \"menu\": \"불고기버거세트\", \"submenu\": \"감자튀김(R)&콜라(R)\", \"count\": 1, \"price\": 16000 }\n" +
            "                    ,{ \"type\": 2, \"menu\": \"분노의질주: 더 익스트림\", \"submenu\": \"A1&A3&A5\", \"count\": 3, \"price\": 5000 }\n" +
            "                    ,{ \"type\": 0, \"menu\": \"menu\", \"submenu\": \"submenu\", \"count\": 1, \"price\": 3000 }\n" +
            "                ] \n" +
            "            }\n" +
            "            , { \n" +
            "                \"date\": \"2021-01-01\"\n" +
            "                , \"company_name\": \"버거킹\"\n" +
            "                , \"order\": \n" +
            "                [\n" +
            "                    { \"type\": 0, \"menu\": \"팝콘\", \"submenu\": \"\", \"count\": 1, \"price\": 1000 }\n" +
            "                    ,{ \"type\": 1, \"menu\": \"콜라\", \"submenu\": \"\", \"count\": 2, \"price\": 1000 }\n" +
            "                    ,{ \"type\": 0, \"menu\": \"불고기버거세트\", \"submenu\": \"감자튀김(R)&콜라(R)\", \"count\": 1, \"price\": 16000 }\n" +
            "                    ,{ \"type\": 2, \"menu\": \"분노의질주: 더 익스트림\", \"submenu\": \"A1&A3&A5\", \"count\": 3, \"price\": 5000 }\n" +
            "                    ,{ \"type\": 0, \"menu\": \"menu\", \"submenu\": \"submenu\", \"count\": 1, \"price\": 3000 }\n" +
            "                ] \n" +
            "            }\n" +
            "            , { \n" +
            "                \"date\": \"2021-01-01\"\n" +
            "                , \"company_name\": \"버거킹\"\n" +
            "                , \"order\": \n" +
            "                [\n" +
            "                    { \"type\": 0, \"menu\": \"팝콘\", \"submenu\": \"\", \"count\": 1, \"price\": 1000 }\n" +
            "                    ,{ \"type\": 1, \"menu\": \"콜라\", \"submenu\": \"\", \"count\": 2, \"price\": 1000 }\n" +
            "                    ,{ \"type\": 0, \"menu\": \"불고기버거세트\", \"submenu\": \"감자튀김(R)&콜라(R)\", \"count\": 1, \"price\": 16000 }\n" +
            "                    ,{ \"type\": 2, \"menu\": \"분노의질주: 더 익스트림\", \"submenu\": \"A1&A3&A5\", \"count\": 3, \"price\": 5000 }\n" +
            "                    ,{ \"type\": 0, \"menu\": \"menu\", \"submenu\": \"submenu\", \"count\": 1, \"price\": 3000 }\n" +
            "                ] \n" +
            "            }\n" +
            "            , { \n" +
            "                \"date\": \"2021-01-01\"\n" +
            "                , \"company_name\": \"버거킹\"\n" +
            "                , \"order\": \n" +
            "                [\n" +
            "                    { \"type\": 0, \"menu\": \"팝콘\", \"submenu\": \"\", \"count\": 1, \"price\": 1000 }\n" +
            "                    ,{ \"type\": 1, \"menu\": \"콜라\", \"submenu\": \"\", \"count\": 2, \"price\": 1000 }\n" +
            "                    ,{ \"type\": 0, \"menu\": \"불고기버거세트\", \"submenu\": \"감자튀김(R)&콜라(R)\", \"count\": 1, \"price\": 16000 }\n" +
            "                    ,{ \"type\": 2, \"menu\": \"분노의질주: 더 익스트림\", \"submenu\": \"A1&A3&A5\", \"count\": 3, \"price\": 5000 }\n" +
            "                    ,{ \"type\": 0, \"menu\": \"menu\", \"submenu\": \"submenu\", \"count\": 1, \"price\": 3000 }\n" +
            "                ] \n" +
            "            }\n" +
            "            , { \n" +
            "                \"date\": \"2021-01-01\"\n" +
            "                , \"company_name\": \"버거킹\"\n" +
            "                , \"order\": \n" +
            "                [\n" +
            "                    { \"type\": 0, \"menu\": \"팝콘\", \"submenu\": \"\", \"count\": 1, \"price\": 1000 }\n" +
            "                    ,{ \"type\": 1, \"menu\": \"콜라\", \"submenu\": \"\", \"count\": 2, \"price\": 1000 }\n" +
            "                    ,{ \"type\": 0, \"menu\": \"불고기버거세트\", \"submenu\": \"감자튀김(R)&콜라(R)\", \"count\": 1, \"price\": 16000 }\n" +
            "                    ,{ \"type\": 2, \"menu\": \"분노의질주: 더 익스트림\", \"submenu\": \"A1&A3&A5\", \"count\": 3, \"price\": 5000 }\n" +
            "                    ,{ \"type\": 0, \"menu\": \"menu\", \"submenu\": \"submenu\", \"count\": 1, \"price\": 3000 }\n" +
            "                ] \n" +
            "            }\n" +
            "            , { \n" +
            "                \"date\": \"2021-01-01\"\n" +
            "                , \"company_name\": \"버거킹\"\n" +
            "                , \"order\": \n" +
            "                [\n" +
            "                    { \"type\": 0, \"menu\": \"팝콘\", \"submenu\": \"\", \"count\": 1, \"price\": 1000 }\n" +
            "                    ,{ \"type\": 1, \"menu\": \"콜라\", \"submenu\": \"\", \"count\": 2, \"price\": 1000 }\n" +
            "                    ,{ \"type\": 0, \"menu\": \"불고기버거세트\", \"submenu\": \"감자튀김(R)&콜라(R)\", \"count\": 1, \"price\": 16000 }\n" +
            "                    ,{ \"type\": 2, \"menu\": \"분노의질주: 더 익스트림\", \"submenu\": \"A1&A3&A5\", \"count\": 3, \"price\": 5000 }\n" +
            "                    ,{ \"type\": 0, \"menu\": \"menu\", \"submenu\": \"submenu\", \"count\": 1, \"price\": 3000 }\n" +
            "                ] \n" +
            "            }\n" +
            "        ] \n" +
            "    } \n" +
            "}";
    private AppCompatImageButton ibtnOrderListBack;
    private AppCompatTextView tvOrderListWaitingOrderCount;
    private AppCompatTextView tvOrderListDoneOrderCount;
    private SwipeRefreshLayout srlOrderList;
    private RecyclerView rvOrderList;
    private ProgressBar pbOrderList;
    private AppCompatTextView tvOrderListNoOrderList;
    private OrderListAdapter orderListAdapter;
    private boolean isFirstLoad = true;
    private SQLiteManager sqLiteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);

        init();
    }

    @Override
    protected void onDestroy() {
        Global.activities.remove(this);
        super.onDestroy();
    }

    private void init() {
        Global.activities.add(this);

        ibtnOrderListBack = findViewById(R.id.ibtn_orderlist_back);
        tvOrderListWaitingOrderCount = findViewById(R.id.tv_orderlist_waitingordercount);
        tvOrderListDoneOrderCount = findViewById(R.id.tv_orderlist_doneordercount);
        srlOrderList = findViewById(R.id.srl_orderlist);
        rvOrderList = findViewById(R.id.rv_orderlist);
        pbOrderList = findViewById(R.id.pb_orderlist);
        tvOrderListNoOrderList = findViewById(R.id.tv_orderlist_noorderlist);

        sqLiteManager = new SQLiteManager(OrderListActivity.this);

        orderListAdapter = new OrderListAdapter();
        rvOrderList.setLayoutManager(new LinearLayoutManager(OrderListActivity.this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(OrderListActivity.this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        rvOrderList.addItemDecoration(dividerItemDecoration);
        rvOrderList.setAdapter(orderListAdapter);

        doUpdateOrderScreen();

        // 뒤로가기 버튼 눌릴 시
        ibtnOrderListBack.setOnClickListener(view -> {
            finish();
        });

        // 주문목록 아이템이 눌릴 시
        orderListAdapter.setOnItemClickListener((view, position) -> new OrderInfoDialog(OrderListActivity.this, false, true, orderListAdapter.getItem(position)).show());

        // 새로고침 스크롤 발생 시
        srlOrderList.setOnRefreshListener(() -> doUpdateOrderScreen());
    }

    public void doUpdateOrderScreen() {
        new UpdateOrderScreen().start();
    }

    public class UpdateOrderScreen extends Thread {
        @Override
        public void run() {
            sqLiteManager.openDatabase();
            int waitingOrderCount = sqLiteManager.getWaitingOrderCount();
            sqLiteManager.closeDatabase();

            runOnUiThread(() -> {
                if (isFirstLoad) {
                    pbOrderList.setVisibility(View.VISIBLE);
                    isFirstLoad = false;
                }
                tvOrderListNoOrderList.setVisibility(View.INVISIBLE);
            });
            try {
                JSONObject sendData = new JSONObject();
                sendData.put("request_code", Global.Network.Request.ORDER_LIST);

                JSONObject message = new JSONObject();
                message.accumulate("id", Global.User.id);

                sendData.accumulate("message", message);

                String strRecvData = new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, sendData.toString()}).get();
                JSONObject recvData = new JSONObject(strRecvData);

                String responseCode = recvData.getString("response_code");

                if (responseCode.equals(Global.Network.Response.ORDER_LIST)) {
                    // 주문내역 불러오기 성공
                    runOnUiThread(() -> {
                        try {
                            rvOrderList.setVisibility(View.VISIBLE);
                            orderListAdapter.setJson(recvData.getJSONObject("message").getJSONArray("order_list"));
                            orderListAdapter.notifyDataSetChanged();

                            int waitingOrderCountDuration;
                            int doneOrderCountDuration;

                            if (waitingOrderCount < 5) {
                                waitingOrderCountDuration = 1000;
                            } else if (waitingOrderCount < 10) {
                                waitingOrderCountDuration = 1500;
                            } else {
                                waitingOrderCountDuration = 2000;
                            }

                            if (orderListAdapter.getItemCount() - waitingOrderCount < 5) {
                                doneOrderCountDuration = 1000;
                            } else if (orderListAdapter.getItemCount() - waitingOrderCount < 10) {
                                doneOrderCountDuration = 1500;
                            } else {
                                doneOrderCountDuration = 2000;
                            }


                            ValueAnimator waitingOrderCountAnimator = ValueAnimator.ofInt(0, waitingOrderCount);
                            waitingOrderCountAnimator.setDuration(waitingOrderCountDuration);
                            waitingOrderCountAnimator.addUpdateListener(valueAnimator -> tvOrderListWaitingOrderCount.setText(String.valueOf(valueAnimator.getAnimatedValue())));

                            ValueAnimator doneOrderCountAnimator = ValueAnimator.ofInt(0, orderListAdapter.getItemCount() - waitingOrderCount);
                            doneOrderCountAnimator.setDuration(doneOrderCountDuration);
                            doneOrderCountAnimator.addUpdateListener(valueAnimator -> tvOrderListDoneOrderCount.setText(String.valueOf(valueAnimator.getAnimatedValue())));

                            waitingOrderCountAnimator.start();
                            doneOrderCountAnimator.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(OrderListActivity.this, "주문내역을 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(OrderListActivity.this, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(OrderListActivity.this, "서버 점검 중입니다: " + e.toString(), Toast.LENGTH_SHORT).show();
                });
            }
            runOnUiThread(() -> {
                pbOrderList.setVisibility(View.GONE);

                if (srlOrderList.isRefreshing()) {
                    srlOrderList.setRefreshing(false);
                    srlOrderList.setEnabled(true);
                }
                if (orderListAdapter.getItemCount() == 0) {
                    tvOrderListNoOrderList.setVisibility(View.VISIBLE);
                } else {
                    tvOrderListNoOrderList.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}