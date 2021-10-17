package com.uos.uos_mobile.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.uos.uos_mobile.item.OrderItem;
import com.uos.uos_mobile.other.Global;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class WaitingOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<OrderItem> orderItemArrayList = new ArrayList<>();  // 주문대기 목록
    private final Context context;
    private WaitingOrderAdapter.OnItemClickListener onItemClickListener = null;

    public WaitingOrderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        return new WaitingOrderAdapter.WaitingOrderViewHolder(layoutInflater.inflate(com.uos.uos_mobile.R.layout.item_waitingorder, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((WaitingOrderViewHolder) viewHolder).tvWaitingOrderCompanyName.setText(orderItemArrayList.get(position).getCompanyName());
        ((WaitingOrderViewHolder) viewHolder).tvWaitingOrderOrderCode.setText(String.valueOf(orderItemArrayList.get(position).getOrderCode()));

        if (orderItemArrayList.get(position).getState() == Global.Order.PREPARING) {

            /* 상품이 준비 중일 경우 */

            ((WaitingOrderViewHolder) viewHolder).tvWaitingOrderMessage.setText("상품이 준비 중입니다");
            ((WaitingOrderViewHolder) viewHolder).clWaitingOrder.setBackgroundColor(context.getResources().getColor(com.uos.uos_mobile.R.color.gray));
        } else if (orderItemArrayList.get(position).getState() == Global.Order.PREPARED) {

            /* 상품이 준비되었을 경우 */

            ((WaitingOrderViewHolder) viewHolder).tvWaitingOrderMessage.setText("상품이 준비되었습니다");
            ((WaitingOrderViewHolder) viewHolder).startAnimation();
        }
    }

    public void setOnItemClickListener(WaitingOrderAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return orderItemArrayList.size();
    }

    public OrderItem getItem(int position) {
        return orderItemArrayList.get(position);
    }

    public void setJson(JSONArray orderListData) {
        orderItemArrayList.clear();

        for (int loop = 0; loop < orderListData.length(); loop++) {
            try {
                orderItemArrayList.add(new OrderItem(orderListData.getJSONObject(loop)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public ArrayList<OrderItem> getOrderItemArrayList() {
        return orderItemArrayList;
    }

    public OrderItem getItemByOrderCode(int orderCode) {
        for (OrderItem orderItem : orderItemArrayList) {
            if (orderItem.getOrderCode() == orderCode) {
                return orderItem;
            }
        }

        return null;
    }

    // 아이템 클릭 리스너 인터페이스
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    // 주문 뷰 관리자
    public class WaitingOrderViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout clWaitingOrder;
        public AppCompatTextView tvWaitingOrderCompanyName;
        public AppCompatTextView tvWaitingOrderOrderCode;
        public AppCompatTextView tvWaitingOrderMessage;

        public WaitingOrderViewHolder(View view) {
            super(view);
            clWaitingOrder = view.findViewById(com.uos.uos_mobile.R.id.cl_waitingorder);
            tvWaitingOrderCompanyName = view.findViewById(com.uos.uos_mobile.R.id.tv_waitingorder_companyname);
            tvWaitingOrderOrderCode = view.findViewById(com.uos.uos_mobile.R.id.tv_waitingorder_ordercode);
            tvWaitingOrderMessage = view.findViewById(com.uos.uos_mobile.R.id.tv_waitingorder_message);

            clWaitingOrder.setOnClickListener(view1 -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view1, position);
                    }
                }
            });
        }

        public void startAnimation() {
            clWaitingOrder.setBackground(context.getResources().getDrawable(com.uos.uos_mobile.R.drawable.anim_waitingorder_state));
            ((AnimationDrawable) clWaitingOrder.getBackground()).start();
        }
    }
}
