package com.uos.uos_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.uos.uos_mobile.item.BasketItem;
import com.uos.uos_mobile.manager.UsefulFuncManager;
import com.uos.uos_mobile.other.Global;

import java.util.ArrayList;

public class OrderProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<BasketItem> basketItemArrayList = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        return new OrderProductViewHolder(layoutInflater.inflate(com.uos.uos_mobile.R.layout.item_pay, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int itemType = basketItemArrayList.get(position).getType();

        ((OrderProductViewHolder) viewHolder).tvOrderItemPrice.setText(UsefulFuncManager.convertToCommaPattern(basketItemArrayList.get(position).getPrice()) + "원");
        ((OrderProductViewHolder) viewHolder).tvOrderItemTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(basketItemArrayList.get(position).getPrice() * basketItemArrayList.get(position).getCount()) + "원");

        if (itemType == Global.ItemType.SET || itemType == Global.ItemType.PRODUCT) {
            ((OrderProductViewHolder) viewHolder).tvOrderItemMenu.setText(basketItemArrayList.get(position).getMenu());
            if (basketItemArrayList.get(position).getSubMenu() != null) {
                ((OrderProductViewHolder) viewHolder).tvOrderItemSubMenu.setText(basketItemArrayList.get(position).getSubMenu().replace("&", "\n"));
            }
        } else if (itemType == Global.ItemType.MOVIE_TICKET) {
            ((OrderProductViewHolder) viewHolder).tvOrderItemMenu.setText(basketItemArrayList.get(position).getMenu().replace("&", " - "));
            ((OrderProductViewHolder) viewHolder).tvOrderItemSubMenu.setText(basketItemArrayList.get(position).getSubMenu().replace("&", ", "));
        }
        ((OrderProductViewHolder) viewHolder).tvOrderItemCount.setText("X  " + basketItemArrayList.get(position).getCount() + "개");
    }

    public void setBasketItemArrayList(ArrayList<BasketItem> basketItemArrayList) {
        this.basketItemArrayList = basketItemArrayList;
    }

    @Override
    public int getItemCount() {
        return basketItemArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    // 뷰 관리자
    public class OrderProductViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView tvOrderItemMenu;
        public AppCompatTextView tvOrderItemCount;
        public AppCompatTextView tvOrderItemPrice;
        public AppCompatTextView tvOrderItemSubMenu;
        public AppCompatTextView tvOrderItemTotalPrice;

        public OrderProductViewHolder(View view) {
            super(view);

            tvOrderItemMenu = view.findViewById(com.uos.uos_mobile.R.id.tv_orderitem_menu);
            tvOrderItemCount = view.findViewById(com.uos.uos_mobile.R.id.tv_orderitem_count);
            tvOrderItemPrice = view.findViewById(com.uos.uos_mobile.R.id.tv_orderitem_price);
            tvOrderItemSubMenu = view.findViewById(com.uos.uos_mobile.R.id.tv_orderitem_submenu);
            tvOrderItemTotalPrice = view.findViewById(com.uos.uos_mobile.R.id.tv_orderitem_totalprice);
        }
    }
}
