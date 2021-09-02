package com.uof.uof_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.uof.uof_mobile.R;
import com.uof.uof_mobile.listitem.BasketItem;
import com.uof.uof_mobile.manager.UsefulFuncManager;
import com.uof.uof_mobile.other.Global;

import java.util.ArrayList;

public class WaitingOrderInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<BasketItem> basketItemArrayList = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        return new WaitingOrderInfoItemViewHolder(layoutInflater.inflate(R.layout.item_pay, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int itemType = basketItemArrayList.get(position).getType();

        ((WaitingOrderInfoItemViewHolder) viewHolder).tvPayItemPrice.setText(UsefulFuncManager.convertToCommaPattern(basketItemArrayList.get(position).getPrice()) + "원");
        ((WaitingOrderInfoItemViewHolder) viewHolder).tvPayItemTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(basketItemArrayList.get(position).getPrice() * basketItemArrayList.get(position).getCount()) + "원");

        if (itemType == Global.ItemType.SET || itemType == Global.ItemType.PRODUCT) {
            ((WaitingOrderInfoItemViewHolder) viewHolder).tvPayItemMenu.setText(basketItemArrayList.get(position).getMenu());
            ((WaitingOrderInfoItemViewHolder) viewHolder).tvPayItemSubMenu.setText(basketItemArrayList.get(position).getSubMenu().replace("&", "\n"));
        } else if (itemType == Global.ItemType.MOVIE_TICKET) {
            ((WaitingOrderInfoItemViewHolder) viewHolder).tvPayItemMenu.setText(basketItemArrayList.get(position).getMenu().replace("&", " - "));
            ((WaitingOrderInfoItemViewHolder) viewHolder).tvPayItemSubMenu.setText(basketItemArrayList.get(position).getSubMenu().replace("&", ", "));
        }
        ((WaitingOrderInfoItemViewHolder) viewHolder).tvPayItemCount.setText("X  " + basketItemArrayList.get(position).getCount() + "개");
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
    public class WaitingOrderInfoItemViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView tvPayItemMenu;
        public AppCompatTextView tvPayItemCount;
        public AppCompatTextView tvPayItemPrice;
        public AppCompatTextView tvPayItemSubMenu;
        public AppCompatTextView tvPayItemTotalPrice;

        public WaitingOrderInfoItemViewHolder(View view) {
            super(view);

            tvPayItemMenu = view.findViewById(R.id.tv_payitem_menu);
            tvPayItemCount = view.findViewById(R.id.tv_payitem_count);
            tvPayItemPrice = view.findViewById(R.id.tv_payitem_price);
            tvPayItemSubMenu = view.findViewById(R.id.tv_payitem_submenu);
            tvPayItemTotalPrice = view.findViewById(R.id.tv_payitem_totalprice);
        }
    }
}
