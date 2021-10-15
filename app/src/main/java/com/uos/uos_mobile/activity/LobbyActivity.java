package com.uos.uos_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.uos.uos_mobile.adapter.WaitingOrderAdapter;
import com.uos.uos_mobile.dialog.WaitingOrderDetailDialog;
import com.uos.uos_mobile.item.WaitingOrderItem;
import com.uos.uos_mobile.manager.HttpManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * U.O.S-Mobile 메인화면을 담당하는 Activity.<br>
 * xml: activity_lobby.xml<br><br>
 * 메인화면 중앙에는 현재 상품 준비 중이거나 수령 대기 중인 주문이 표시되며 상단에는 QR코드 인식 버튼이, 하단에는 카
 * 드 관리, 주문내역, 설정 버튼이 표시되어있습니다.
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.uos.uos_mobile.R.layout.activity_lobby);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ibtnLobbyLeft != null && ibtnLobbyRight != null && rvLobbyWaitingOrder != null) {

            /*
             * updateList() 메소드에서 사용하는 변수들이 초기화되지 않았을 경우(null) NullPointerException이
             * 발생하는 것을 방지하기 위해 만들어놓은 조건문.
             */

            updateList();
        }
    }

    private void init() {
        /* xml 파일로부터 ui 불러오기 */
        ivLobbyRecognizeQr = findViewById(com.uos.uos_mobile.R.id.iv_lobby_recognizeqr);
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
        updateList();

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
            WaitingOrderDetailDialog waitingOrderDetailDialog = new WaitingOrderDetailDialog(LobbyActivity.this, false, true, waitingOrderAdapter.getItem(position));
            waitingOrderDetailDialog.setOnDismissListener(dialogInterface -> {
                updateList();
            });
            waitingOrderDetailDialog.show();
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
        } else if (lobbyActivityIntent.getStringExtra("orderCode") != null) {

            /* Notification을 통해 앱을 실행했을 경우 */

            WaitingOrderItem waitingOrderItem = waitingOrderAdapter.getItemByOrderCode(lobbyActivityIntent.getStringExtra("orderCode"));
            if (waitingOrderItem == null) {

                /* Notification을 통해 전달받은 주문코드에 해당하는 주문이 없을 경우 */

                Toast.makeText(LobbyActivity.this, "번호가 " + lobbyActivityIntent.getStringExtra("orderCode") + "인 주문이 없습니다", Toast.LENGTH_SHORT).show();
            } else {

                /* Notification을 통해 전달받은 주문코드에 해당하는 주문이 있을 경우 */

                WaitingOrderDetailDialog waitingOrderDetailDialog = new WaitingOrderDetailDialog(LobbyActivity.this, false, true, waitingOrderItem);
                waitingOrderDetailDialog.setOnDismissListener(dialogInterface -> {
                    updateList();
                });
                waitingOrderDetailDialog.show();
            }
        }
    }

    /**
     * LobbyActivity에 있는 주문목록을 업데이트.
     */
    public void updateList() {
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

                        /*  */

                        try {
                            waitingOrderAdapter.updateItemWithJson(recvData.getJSONObject("message").getJSONArray("orders"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(LobbyActivity.this, "대기중인 주문을 불러오는 도중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                    }

                    ibtnLobbyLeft.setVisibility(View.VISIBLE);
                    ibtnLobbyRight.setVisibility(View.VISIBLE);

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
                });
            } catch (JSONException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(LobbyActivity.this, "대기중인 주문을 불러오는 도중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    /**
     * 전달받은 주문코드에 해당하는 주문이 있을 경우 해당 주문으로 RecyclerView 항목을 스크롤.
     *
     * @param orderCode 주문코드.
     */
    public void moveToOrderCode(String orderCode) {
        int position = 0;
        for (WaitingOrderItem waitingOrderItem : waitingOrderAdapter.getWaitingOrderItemArrayList()) {
            if (waitingOrderItem.getOrderCode().equals(orderCode)) {
                rvLobbyWaitingOrder.smoothScrollToPosition(position);
                break;
            }
            position++;
        }
    }
}