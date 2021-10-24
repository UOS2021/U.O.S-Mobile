package com.uos.uos_mobile.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.uos.uos_mobile.adapter.WaitingOrderAdapter;
import com.uos.uos_mobile.dialog.OrderDetailDialog;
import com.uos.uos_mobile.item.OrderItem;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

/**
 * 일반고객의 메인화면을 담당하는 Activity.<br>
 * xml: activity_lobby.xml<br><br>
 *
 * 메인화면 중앙에는 현재 상품 준비 중이거나 수령 대기 중인 주문이 표시되며 해당 주문을 누를 시 주문 상세정보를
 * 표시하는 OrderDetailDialog를 호출합니다. 상단에는 QR코드 인식 버튼이, 하단에는 카드 관리, 주문내역, 설정 버튼이
 * 표시되어있습니다.
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class LobbyActivity extends UosActivity {

    /**
     * QR코드 인식창으로 넘어가는 ImageView.
     */
    private AppCompatImageView ivLobbyRecognizeQr;

    /**
     * 대기 중인 주문이 없다는 문구를 표시하는 AppCompatTextView.
     */
    private AppCompatTextView tvLobbyNoWaitingOrder;

    /**
     * 상품 준비 중, 수령 대기 중인 상품의 목록에서 왼쪽 항목으로 넘기는 ImageButton.
     */
    private AppCompatImageButton ibtnLobbyLeft;

    /**
     * 상품 준비 중, 수령 대기 중인 상품 목록을 표시하는 RecyclerView.
     */
    private RecyclerView rvLobbyWaitingOrder;

    /**
     * 상품 준비 중, 수령 대기 중인 상품의 목록에서 오른쪽 항목으로 넘기는 ImageButton.
     */
    private AppCompatImageButton ibtnLobbyRight;

    /**
     * 카드 관리 Activity로 넘어가는 ConstraintLayout.
     */
    private ConstraintLayout clLobbyCard;

    /**
     * 주문내역 Activity로 넘어가는 ConstraintLayout.
     */
    private ConstraintLayout clLobbyOrderList;

    /**
     * 설정 Activity로 넘어가는 ConstraintLayout.
     */
    private ConstraintLayout clLobbySetting;

    /**
     * 상품 준비 중, 수령 대기 중인 주문목록을 저장하고 관리하는 Adapter.
     */
    private WaitingOrderAdapter waitingOrderAdapter;

    @Override
    protected void init() {
        setContentView(com.uos.uos_mobile.R.layout.activity_lobby);

        /* xml 파일로부터 ui 불러오기 */
        ivLobbyRecognizeQr = findViewById(com.uos.uos_mobile.R.id.iv_lobby_recognizeqr);
        tvLobbyNoWaitingOrder = findViewById(com.uos.uos_mobile.R.id.tv_lobby_nowaitingorder);
        ibtnLobbyLeft = findViewById(com.uos.uos_mobile.R.id.ibtn_lobby_left);
        rvLobbyWaitingOrder = findViewById(com.uos.uos_mobile.R.id.rv_lobby_waitingorder);
        ibtnLobbyRight = findViewById(com.uos.uos_mobile.R.id.ibtn_lobby_right);
        clLobbyCard = findViewById(com.uos.uos_mobile.R.id.cl_lobby_card);
        clLobbyOrderList = findViewById(com.uos.uos_mobile.R.id.cl_lobby_orderlist);
        clLobbySetting = findViewById(com.uos.uos_mobile.R.id.cl_lobby_setting);

        /* 변수 초기화 */
        waitingOrderAdapter = new WaitingOrderAdapter(LobbyActivity.this);

        /* 주문상태 목록을 보여주는 RecyclerView 초기화 */
        rvLobbyWaitingOrder.setLayoutManager(new LinearLayoutManager(LobbyActivity.this, LinearLayoutManager.HORIZONTAL, false));
        new PagerSnapHelper().attachToRecyclerView(rvLobbyWaitingOrder);
        rvLobbyWaitingOrder.setAdapter(waitingOrderAdapter);

        /* LobbyActivity 첫 실행 시 주문 상태 업데이트 */
        ibtnLobbyLeft.setVisibility(View.INVISIBLE);
        ibtnLobbyRight.setVisibility(View.INVISIBLE);

        /* QR 인식 버튼이 눌렸을 경우 */
        ivLobbyRecognizeQr.setOnClickListener(view -> {
            Intent intent = new Intent(LobbyActivity.this, QRRecognitionActivity.class);
            startActivity(intent);
        });

        /* 좌측 버튼이 눌렸을 경우 */
        ibtnLobbyLeft.setOnClickListener(view -> {
            int currentPosition = ((LinearLayoutManager) rvLobbyWaitingOrder.getLayoutManager()).findFirstVisibleItemPosition();
            if (currentPosition != 0) {
                rvLobbyWaitingOrder.smoothScrollToPosition(currentPosition - 1);
            }
        });

        /* 리스트가 스크롤 될 경우 */
        rvLobbyWaitingOrder.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ibtnLobbyLeft.setVisibility(View.VISIBLE);
                ibtnLobbyRight.setVisibility(View.VISIBLE);

                if (waitingOrderAdapter.getItemCount() < 2) {

                    /* 주문목록이 1개 이하일 경우 */

                    ibtnLobbyLeft.setVisibility(View.INVISIBLE);
                    ibtnLobbyRight.setVisibility(View.INVISIBLE);
                } else if (((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition() == 0) {
                    ibtnLobbyLeft.setVisibility(View.INVISIBLE);
                } else if (((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition() == waitingOrderAdapter.getItemCount() - 1) {
                    ibtnLobbyRight.setVisibility(View.INVISIBLE);
                }
            }
        });

        /* 주문대기 목록 아이템이 눌렸을 경우 */
        waitingOrderAdapter.setOnItemClickListener((view, position) -> {
            OrderDetailDialog orderDetailDialog = new OrderDetailDialog(LobbyActivity.this, false, true, waitingOrderAdapter.getItem(position));
            orderDetailDialog.setOnDismissListener(dialogInterface -> {
                updateList(-1, false);
            });
            orderDetailDialog.show();
        });

        /* 우측 버튼이 눌렸을 경우 */
        ibtnLobbyRight.setOnClickListener(view -> {
            int currentPosition = ((LinearLayoutManager) rvLobbyWaitingOrder.getLayoutManager()).findLastVisibleItemPosition();
            if (currentPosition < waitingOrderAdapter.getItemCount() - 1) {
                rvLobbyWaitingOrder.smoothScrollToPosition(currentPosition + 1);
            }
        });

        /* 카드관리 버튼이 눌렸을 경우 */
        clLobbyCard.setOnClickListener(view -> {
            Intent intent = new Intent(LobbyActivity.this, CardActivity.class);
            startActivity(intent);
        });

        /* 주문내역 버튼이 눌렸을 경우 */
        clLobbyOrderList.setOnClickListener(view -> {
            Intent intent = new Intent(LobbyActivity.this, OrderListActivity.class);
            startActivity(intent);
        });

        /* 설정 버튼이 눌렸을 경우 */
        clLobbySetting.setOnClickListener(view -> {
            Intent intent = new Intent(LobbyActivity.this, SettingActivity.class);
            startActivity(intent);
        });

        Intent lobbyActivityIntent = getIntent();
        if (lobbyActivityIntent.getStringExtra("uosPartnerId") != null) {

            /* QR코드를 통해 앱을 접속했을 경우 */

            Intent intent = new Intent(LobbyActivity.this, QRRecognitionActivity.class);
            intent.putExtra("uosPartnerId", lobbyActivityIntent.getStringExtra("uosPartnerId"));
            startActivity(intent);
        }

        updateList(lobbyActivityIntent.getIntExtra("orderCode", -1), true);
    }

    /**
     * LobbyActivity에 있는 주문목록을 업데이트합니다. 매개변수로 전달된 주문코드가 -1이 아닐 경우 해당 주문으로
     * 리스트를 스크롤하며 showOrderDetail이 True일 경우 OrderDetailDialog에 전달된 주문코드에 해당하는 주문의
     * 세부정보를 수정합니다.
     *
     * @param orderCode       주문코드.
     * @param showOrderDetail 주문에 대한 세부정보 표시 여부.
     */
    public void updateList(int orderCode, boolean showOrderDetail) {
        new Thread(() -> {
            try {
                JSONObject message = new JSONObject();
                message.accumulate("customer_id", Global.User.id);

                JSONObject sendData = new JSONObject();
                sendData.accumulate("request_code", Global.Network.Request.WAITING_ORDER_LIST);
                sendData.accumulate("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{Global.Network.EXTERNAL_SERVER_URL, String.valueOf(HttpManager.DEFAULT_CONNECTION_TIMEOUT), String.valueOf(HttpManager.DEFAULT_READ_TIMEOUT), sendData.toString()}).get());
                String responseCode = recvData.getString("response_code");

                runOnUiThread(() -> {
                    if (responseCode.equals(Global.Network.Response.WAITING_ORDER_LIST)) {

                        /* 주문대기 목록을 성공적으로 불러왔을 경우  */

                        try {
                            waitingOrderAdapter.setJson(recvData.getJSONObject("message").getJSONArray("order_list"));

                            if (orderCode != -1) {

                                /* 주문코드가 있을 경우 - 지정한 주문으로 RecyclerView 스크롤 */

                                moveToOrderCode(orderCode);

                                if (showOrderDetail) {

                                    /* 주문 세부정보를 보여줘야할 경우 - 준비된 상품 */

                                    OrderItem orderItem = waitingOrderAdapter.getItemByOrderCode(orderCode);
                                    if (orderItem == null) {

                                        /* 주문코드에 해당하는 주문이 없을 경우 */

                                        Toast.makeText(LobbyActivity.this, "번호가 " + orderCode + "인 주문이 없습니다", Toast.LENGTH_SHORT).show();
                                    } else {

                                        /* 주문코드에 해당하는 주문이 있을 경우 */

                                        new OrderDetailDialog(LobbyActivity.this, false, true, orderItem).show();
                                    }
                                }
                            }

                            ibtnLobbyLeft.setVisibility(View.VISIBLE);
                            ibtnLobbyRight.setVisibility(View.VISIBLE);

                            if(waitingOrderAdapter.getItemCount() == 0){

                                /* 대기 중인 주문이 없을 경우 */

                                tvLobbyNoWaitingOrder.setVisibility(View.VISIBLE);
                                ibtnLobbyLeft.setVisibility(View.INVISIBLE);
                                ibtnLobbyRight.setVisibility(View.INVISIBLE);
                            }else{

                                /* 대기 중인 주문이 있을 경우 */

                                tvLobbyNoWaitingOrder.setVisibility(View.INVISIBLE);

                                if (waitingOrderAdapter.getItemCount() < 2) {

                                    /* 주문목록이 1개 이하일 경우 */

                                    ibtnLobbyLeft.setVisibility(View.INVISIBLE);
                                    ibtnLobbyRight.setVisibility(View.INVISIBLE);
                                } else if (((LinearLayoutManager) rvLobbyWaitingOrder.getLayoutManager()).findFirstVisibleItemPosition() == 0) {

                                    /* 현재 표시중인 주문목록이 첫 번째 주문일 경우 - 왼쪽 스크롤 버튼 숨기기 */

                                    ibtnLobbyLeft.setVisibility(View.INVISIBLE);
                                } else if (((LinearLayoutManager) rvLobbyWaitingOrder.getLayoutManager()).findLastVisibleItemPosition() == waitingOrderAdapter.getItemCount() - 1) {

                                    /* 현재 표시중인 주문목록이 마지막 주문일 경우 - 오른쪽 스크롤 버튼 숨기기 */

                                    ibtnLobbyRight.setVisibility(View.INVISIBLE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LobbyActivity.this, "대기 중인 주문을 불러오는 도중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                        }
                    }  else if (responseCode.equals(Global.Network.Response.SERVER_NOT_ONLINE)) {

                        /* 서버 연결 실패 */

                        Toast.makeText(LobbyActivity.this, "서버 점검 중입니다", Toast.LENGTH_SHORT).show();
                    } else {

                        /* 주문대기 목록을 불러오지 못했을 경우  */

                        Toast.makeText(LobbyActivity.this, "대기 중인 주문을 불러오는 도중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (JSONException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(LobbyActivity.this, "대기 중인 주문을 불러오는 도중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    /**
     * 매개변수로 전달된 주문코드를 가진 주문이 있을 경우 해당 주문으로 RecyclerView를 스크롤합니다.
     *
     * @param orderCode 주문코드.
     */
    public void moveToOrderCode(int orderCode) {
        int position = 0;
        for (OrderItem orderItem : waitingOrderAdapter.getOrderItemArrayList()) {
            if (orderItem.getOrderCode() == orderCode) {
                final int target = position;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(() -> {
                            rvLobbyWaitingOrder.smoothScrollToPosition(target);
                        });
                    }
                }, 500);
                break;
            }
            position++;
        }
    }
}