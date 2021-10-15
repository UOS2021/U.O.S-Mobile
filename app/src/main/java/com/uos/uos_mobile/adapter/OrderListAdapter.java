package com.uos.uos_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.uos.uos_mobile.item.BasketItem;
import com.uos.uos_mobile.item.OrderListItem;
import com.uos.uos_mobile.manager.UsefulFuncManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<OrderListItem> orderListItemArrayList = new ArrayList<>();
    private OrderListAdapter.OnItemClickListener onItemClickListener = null;

    public void setJson(JSONArray data) {
        orderListItemArrayList.clear();

        for (int loop1 = 0; loop1 < data.length(); loop1++) {
            try {
                JSONObject orderData = data.getJSONObject(loop1);

                ArrayList<BasketItem> basketItemArrayList = new ArrayList<>();
                JSONArray order = orderData.getJSONArray("order");

                for (int loop2 = 0; loop2 < order.length(); loop2++) {
                    JSONObject productData = order.getJSONObject(loop2);

                    basketItemArrayList.add(
                            new BasketItem(
                                    productData.getInt("type")
                                    , productData.getString("menu")
                                    , productData.getString("submenu")
                                    , productData.getInt("price")
                                    , productData.getInt("count")));
                }

                orderListItemArrayList.add(
                        new OrderListItem(
                                orderData.getString("company_name")
                                , orderData.getString("date")
                                , orderData.getInt("state")
                                , basketItemArrayList));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        return new OrderListViewHolder(layoutInflater.inflate(com.uos.uos_mobile.R.layout.item_orderlist, parent, false));
    }

    public OrderListItem getItem(int position) {
        return orderListItemArrayList.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((OrderListViewHolder) viewHolder).tvOrderListItemDate.setText(orderListItemArrayList.get(position).getDate());
        ((OrderListViewHolder) viewHolder).tvOrderListItemCompanyName.setText(orderListItemArrayList.get(position).getCompanyName());
        ((OrderListViewHolder) viewHolder).tvOrderListItemTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(orderListItemArrayList.get(position).getTotalPrice()) + "원");
        ((OrderListViewHolder) viewHolder).tvOrderListItemOrderSimple.setText(orderListItemArrayList.get(position).getOrderSimple());
    }

    @Override
    public int getItemCount() {
        return orderListItemArrayList.size();
    }

    public int getItemCount(int state) {
        int count = 0;
        for (OrderListItem orderListItem : orderListItemArrayList) {
            if (orderListItem.getState() == state) {
                count++;
            }
        }
        return count;
    }

    public void setOnItemClickListener(OrderListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // 아이템 클릭 리스너 인터페이스
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class OrderListViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout clOrderListItem;
        public AppCompatTextView tvOrderListItemDate;
        public AppCompatTextView tvOrderListItemCompanyName;
        public AppCompatTextView tvOrderListItemTotalPrice;
        public AppCompatTextView tvOrderListItemOrderSimple;

        public OrderListViewHolder(View view) {
            super(view);
            clOrderListItem = view.findViewById(com.uos.uos_mobile.R.id.cl_orderlistitem);
            tvOrderListItemDate = view.findViewById(com.uos.uos_mobile.R.id.tv_orderlistitem_date);
            tvOrderListItemCompanyName = view.findViewById(com.uos.uos_mobile.R.id.tv_orderlistitem_companyname);
            tvOrderListItemTotalPrice = view.findViewById(com.uos.uos_mobile.R.id.tv_orderlistitem_totalprice);
            tvOrderListItemOrderSimple = view.findViewById(com.uos.uos_mobile.R.id.tv_orderlistitem_ordersimple);

            clOrderListItem.setOnClickListener(view1 -> {
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
