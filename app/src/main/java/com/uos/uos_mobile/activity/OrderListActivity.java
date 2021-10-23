package com.uos.uos_mobile.activity;

import android.animation.ValueAnimator;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.uos.uos_mobile.adapter.OrderAdapter;
import com.uos.uos_mobile.dialog.OrderDetailDialog;
import com.uos.uos_mobile.item.OrderItem;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONObject;

public class OrderListActivity extends UosActivity {
    private AppCompatImageButton ibtnOrderListBack;
    private AppCompatTextView tvOrderListWaitingOrderCount;
    private AppCompatTextView tvOrderListDoneOrderCount;
    private SwipeRefreshLayout srlOrderList;
    private RecyclerView rvOrderList;
    private ProgressBar pbOrderList;
    private AppCompatTextView tvOrderListNoOrderList;
    private OrderAdapter orderAdapter;
    private boolean isFirstLoad = true;

    /**
     * Activity 실행 시 최초 실행해야하는 코드 및 변수 초기화를 담당하고 있는 함수.
     */
    @Override
    protected void init() {
        setContentView(com.uos.uos_mobile.R.layout.activity_orderlist);

        ibtnOrderListBack = findViewById(com.uos.uos_mobile.R.id.ibtn_orderlist_back);
        tvOrderListWaitingOrderCount = findViewById(com.uos.uos_mobile.R.id.tv_orderlist_waitingordercount);
        tvOrderListDoneOrderCount = findViewById(com.uos.uos_mobile.R.id.tv_orderlist_doneordercount);
        srlOrderList = findViewById(com.uos.uos_mobile.R.id.srl_orderlist);
        rvOrderList = findViewById(com.uos.uos_mobile.R.id.rv_orderlist);
        pbOrderList = findViewById(com.uos.uos_mobile.R.id.pb_orderlist);
        tvOrderListNoOrderList = findViewById(com.uos.uos_mobile.R.id.tv_orderlist_noorderlist);

        orderAdapter = new OrderAdapter();
        rvOrderList.setLayoutManager(new LinearLayoutManager(OrderListActivity.this, LinearLayoutManager.VERTICAL, false));
        rvOrderList.setAdapter(orderAdapter);

        updateList();

        /* 뒤로가기 버튼 눌릴 시 */
        ibtnOrderListBack.setOnClickListener(view -> {
            finish();
        });

        /* 주문목록 아이템이 눌릴 시 */
        orderAdapter.setOnItemClickListener((view, position) -> new OrderDetailDialog(OrderListActivity.this, false, true, orderAdapter.getItem(position)).show());

        // 새로고침 스크롤 발생 시
        srlOrderList.setOnRefreshListener(() -> updateList());
    }

    public void updateList() {
        new Thread(() -> {
            runOnUiThread(() -> {
                if (isFirstLoad) {
                    pbOrderList.setVisibility(View.VISIBLE);
                    isFirstLoad = false;
                }
                tvOrderListNoOrderList.setVisibility(View.INVISIBLE);
            });
            try {
                JSONObject message = new JSONObject();
                message.accumulate("customer_id", Global.User.id);

                JSONObject sendData = new JSONObject();
                sendData.accumulate("request_code", Global.Network.Request.ORDER_LIST);
                sendData.accumulate("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(HttpManager.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());
                String responseCode = recvData.getString("response_code");

                if (responseCode.equals(Global.Network.Response.ORDER_LIST)) {
                    // 주문내역 불러오기 성공
                    runOnUiThread(() -> {
                        try {
                            rvOrderList.setVisibility(View.VISIBLE);
                            orderAdapter.setJson(recvData.getJSONObject("message").getJSONArray("order_list"));
                            orderAdapter.notifyDataSetChanged();

                            int waitingOrderCount = orderAdapter.getItemCount(Global.Order.PREPARING);
                            waitingOrderCount += orderAdapter.getItemCount(Global.Order.PREPARED);
                            waitingOrderCount += orderAdapter.getItemCount(Global.Order.WAITING_ACCEPT);

                            int waitingOrderCountDuration;
                            int doneOrderCountDuration;

                            if (waitingOrderCount < 5) {
                                waitingOrderCountDuration = 1000;
                            } else if (waitingOrderCount < 10) {
                                waitingOrderCountDuration = 1500;
                            } else {
                                waitingOrderCountDuration = 2000;
                            }

                            if (orderAdapter.getItemCount() - waitingOrderCount < 5) {
                                doneOrderCountDuration = 1000;
                            } else if (orderAdapter.getItemCount() - waitingOrderCount < 10) {
                                doneOrderCountDuration = 1500;
                            } else {
                                doneOrderCountDuration = 2000;
                            }

                            ValueAnimator waitingOrderCountAnimator = ValueAnimator.ofInt(0, waitingOrderCount);
                            waitingOrderCountAnimator.setDuration(waitingOrderCountDuration);
                            waitingOrderCountAnimator.addUpdateListener(valueAnimator -> tvOrderListWaitingOrderCount.setText(String.valueOf(valueAnimator.getAnimatedValue())));

                            ValueAnimator doneOrderCountAnimator = ValueAnimator.ofInt(0, orderAdapter.getItemCount() - waitingOrderCount - orderAdapter.getItemCount(Global.Order.CANCELED) - orderAdapter.getItemCount(Global.Order.REFUSED));
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
                if (orderAdapter.getItemCount() == 0) {
                    tvOrderListNoOrderList.setVisibility(View.VISIBLE);
                } else {
                    tvOrderListNoOrderList.setVisibility(View.INVISIBLE);
                }
            });
        }).start();
    }
}