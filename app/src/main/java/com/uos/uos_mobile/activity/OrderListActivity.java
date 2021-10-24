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
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * 주문내역을 보여주는 Activity.<br>
 * xml: activity_orderlist.xml<br><br>
 *
 * 사용자의 모든 주문내역을 보여줍니다. 목록에서 보이는 주문내용은 주문정보를 간략화해서 표시하고 있으며 주문을 누를
 * 시 해당 주문의 상세정보를 보여주는 OrderDetailDialog를 호출합니다.
 *
 * @author Yoon Jong Beom
 * @since 1.0.0
 */
public class OrderListActivity extends UosActivity {

    /**
     * OrderListActivity를 종료하는 AppCompatImageButton.
     */
    private AppCompatImageButton ibtnOrderListBack;

    /**
     * 대기중인 주문 수를 표시하는 AppCompatTextView.
     */
    private AppCompatTextView tvOrderListWaitingOrderCount;

    /**
     * 완료된 주문 수를 표시하는 AppCompatTextView.
     */
    private AppCompatTextView tvOrderListDoneOrderCount;

    /**
     * 주문내역을 스크롤로 새로고침 할 수 있는 SwipeRefreshLayout.
     */
    private SwipeRefreshLayout srlOrderList;

    /**
     * 주문내역을 표시하는 RecyclerView.
     */
    private RecyclerView rvOrderList;

    /**
     * 주문내역을 불러올 때 표시할 ProgressBar.
     */
    private ProgressBar pbOrderList;

    /**
     * 주문내역이 없을 경우 표시할 AppCompatTextView.
     */
    private AppCompatTextView tvOrderListNoOrderList;

    /**
     * 주문내역에 대한 목록을 관리하는 OrderAdapter.
     */
    private OrderAdapter orderAdapter;

    /**
     * 앱이 최초 실행인지 확인하는 변수. 해당 변수는 ProgressBar가 최초 1회만 보여지도록 하는데 사용.
     */
    private boolean isFirstLoad = true;


    /**
     * 주문내역을 불러오는 중인지 확인하는 변수.
     */
    private boolean isLoadingOrderList = false;

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

        /* 새로고침 스크롤 발생 시 */
        srlOrderList.setOnRefreshListener(() -> updateList());
    }

    /**
     * 사용자의 주문내역을 서버로부터 불러와 업데이트합니다.
     */
    public void updateList() {
        if (!isLoadingOrderList) {

            /* 주문내역을 불러오고 있지 않을 경우 */

            isLoadingOrderList = true;

            new Thread(() -> {
                runOnUiThread(() -> {
                    if (isFirstLoad) {

                        /* 최초 실행 시 ProgressBar 표시 */

                        pbOrderList.setVisibility(View.VISIBLE);
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

                        /* 주문내역 불러오기 성공 */

                        runOnUiThread(() -> {
                            rvOrderList.setVisibility(View.VISIBLE);
                            try {
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
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(OrderListActivity.this, "주문내역을 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {

                        /* 서버 연결 실패 */

                        runOnUiThread(() -> {
                            Toast.makeText(OrderListActivity.this, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
                        });
                    } else {

                        /* 주문내역 불러오기 실패 */

                        runOnUiThread(() -> {
                            Toast.makeText(OrderListActivity.this, "주문내역을 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (JSONException | ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(OrderListActivity.this, "주문내역을 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                    });
                }

                runOnUiThread(() -> {
                    if (isFirstLoad) {

                        /* 주문내역 불러오기 성공 */

                        pbOrderList.setVisibility(View.GONE);
                        isFirstLoad = false;
                    }

                    if (srlOrderList.isRefreshing()) {

                        /* 주문내역 새로고침 중일 경우 */

                        srlOrderList.setRefreshing(false);
                        srlOrderList.setEnabled(true);
                    }

                    if (orderAdapter.getItemCount() == 0) {
                        tvOrderListNoOrderList.setVisibility(View.VISIBLE);
                    } else {
                        tvOrderListNoOrderList.setVisibility(View.INVISIBLE);
                    }
                    isLoadingOrderList = false;
                });
            }).start();
        }
    }
}