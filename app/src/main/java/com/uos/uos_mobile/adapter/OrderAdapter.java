package com.uos.uos_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.uos.uos_mobile.item.OrderItem;
import com.uos.uos_mobile.manager.UsefulFuncManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<OrderItem> orderItemArrayList = new ArrayList<>();
    private OrderAdapter.OnItemClickListener onItemClickListener = null;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        return new OrderViewHolder(layoutInflater.inflate(com.uos.uos_mobile.R.layout.item_order, parent, false));
    }

    public OrderItem getItem(int position) {
        return orderItemArrayList.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((OrderViewHolder) viewHolder).tvOrderDate.setText(
                Integer.valueOf(orderItemArrayList.get(position).getDate().substring(5, 7)) + "월 "
                        + Integer.valueOf(orderItemArrayList.get(position).getDate().substring(8, 10)) + "일 ("
                        + UsefulFuncManager.getWeekDayFromDate(orderItemArrayList.get(position).getDate()) + ")");

        if (orderItemArrayList.get(position).getState() == Global.Order.WAITING_ACCEPT) {
            ((OrderViewHolder) viewHolder).tvOrderState.setText("접수 대기중");
            ((OrderViewHolder) viewHolder).clOrderState.setBackgroundResource(com.uos.uos_mobile.R.drawable.border_orderstate_waitingaccept);
        } else if (orderItemArrayList.get(position).getState() == Global.Order.PREPARING) {
            ((OrderViewHolder) viewHolder).tvOrderState.setText("상품 준비중");
            ((OrderViewHolder) viewHolder).clOrderState.setBackgroundResource(com.uos.uos_mobile.R.drawable.border_orderstate_preparing);
        } else if (orderItemArrayList.get(position).getState() == Global.Order.PREPARED) {
            ((OrderViewHolder) viewHolder).tvOrderState.setText("수령 대기중");
            ((OrderViewHolder) viewHolder).clOrderState.setBackgroundResource(com.uos.uos_mobile.R.drawable.border_orderstate_prepared);
        } else if (orderItemArrayList.get(position).getState() == Global.Order.DONE) {
            ((OrderViewHolder) viewHolder).tvOrderState.setText("완료");
            ((OrderViewHolder) viewHolder).clOrderState.setBackgroundResource(com.uos.uos_mobile.R.drawable.border_orderstate_done);
        } else if (orderItemArrayList.get(position).getState() == Global.Order.CANCELED) {
            ((OrderViewHolder) viewHolder).tvOrderState.setText("취소");
            ((OrderViewHolder) viewHolder).clOrderState.setBackgroundResource(com.uos.uos_mobile.R.drawable.border_orderstate_canceled);
        } else if (orderItemArrayList.get(position).getState() == Global.Order.REFUSED) {
            ((OrderViewHolder) viewHolder).tvOrderState.setText("거절");
            ((OrderViewHolder) viewHolder).clOrderState.setBackgroundResource(com.uos.uos_mobile.R.drawable.border_orderstate_canceled);
        }

        ((OrderViewHolder) viewHolder).tvOrderCompanyName.setText(orderItemArrayList.get(position).getCompanyName());
        ((OrderViewHolder) viewHolder).tvOrderTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(orderItemArrayList.get(position).getTotalPrice()) + "원");
        ((OrderViewHolder) viewHolder).tvOrderOrderSimple.setText(orderItemArrayList.get(position).getOrderSimple());
    }

    @Override
    public int getItemCount() {
        return orderItemArrayList.size();
    }

    public int getItemCount(int state) {
        int count = 0;
        for (OrderItem orderItem : orderItemArrayList) {
            if (orderItem.getState() == state) {
                count++;
            }
        }
        return count;
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

    public void setOnItemClickListener(OrderAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // 아이템 클릭 리스너 인터페이스
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout clOrder;
        public AppCompatTextView tvOrderDate;
        public ConstraintLayout clOrderState;
        public AppCompatTextView tvOrderState;
        public AppCompatTextView tvOrderCompanyName;
        public AppCompatTextView tvOrderTotalPrice;
        public AppCompatTextView tvOrderOrderSimple;

        public OrderViewHolder(View view) {
            super(view);
            clOrder = view.findViewById(com.uos.uos_mobile.R.id.cl_order);
            tvOrderDate = view.findViewById(com.uos.uos_mobile.R.id.tv_order_date);
            clOrderState = view.findViewById(com.uos.uos_mobile.R.id.cl_order_state);
            tvOrderState = view.findViewById(com.uos.uos_mobile.R.id.tv_order_state);
            tvOrderCompanyName = view.findViewById(com.uos.uos_mobile.R.id.tv_order_companyname);
            tvOrderTotalPrice = view.findViewById(com.uos.uos_mobile.R.id.tv_order_totalprice);
            tvOrderOrderSimple = view.findViewById(com.uos.uos_mobile.R.id.tv_order_ordersimple);

            clOrder.setOnClickListener(view1 -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view1, position);
                    }
                }
            });
        }
    }
}
