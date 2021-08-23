package com.uof.uof_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.uof.uof_mobile.R;
import com.uof.uof_mobile.listitem.OrderListProductItem;
import com.uof.uof_mobile.listitem.OrderingProductItem;
import com.uof.uof_mobile.manager.UsefulFuncManager;
import com.uof.uof_mobile.listitem.OrderListItem;
import com.uof.uof_mobile.other.OrderingCategory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<OrderListItem> orderListItemArrayList = new ArrayList<>();

    public void setJson(JSONArray data){
        orderListItemArrayList.clear();
        for (int loop1 = 0; loop1 < data.length(); loop1++) {
            try {
                JSONObject listData = data.getJSONObject(loop1);
                orderListItemArrayList.add(new OrderListItem(listData));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        return new OrderListViewHolder(layoutInflater.inflate(R.layout.item_orderlist_order, parent, false));
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((OrderListViewHolder)viewHolder).tvOrderListOrderdate.setText(orderListItemArrayList.get(position).getDate());
        ((OrderListViewHolder)viewHolder).tvOrderListOrderCompanyname.setText(orderListItemArrayList.get(position).getCompanyname());
        ((OrderListViewHolder)viewHolder).tvOrderListOrderOrderlist.setText(orderListItemArrayList.get(position).getStringProductItemList());
        ((OrderListViewHolder)viewHolder).tvOrderListOrderPrice.setText(UsefulFuncManager.convertToCommaPattern(orderListItemArrayList.get(position).getPrice()) + "원");
    }

    @Override
    public int getItemCount(){ return orderListItemArrayList.size();}


    public class OrderListViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView tvOrderListOrderdate;
        public AppCompatTextView tvOrderListOrderCompanyname;
        public AppCompatTextView tvOrderListOrderOrderlist;
        public AppCompatTextView tvOrderListOrderPrice;

        public OrderListViewHolder(View view) {
            super(view);
            tvOrderListOrderdate = view.findViewById(R.id.tv_orderlist_orderdate);
            tvOrderListOrderCompanyname = view.findViewById(R.id.tv_orderlist_ordercompanyname);
            tvOrderListOrderOrderlist = view.findViewById(R.id.tv_orderlist_orderlist);
            tvOrderListOrderPrice = view.findViewById(R.id.tv_orderlist_price);
        }
    }
}
