package com.uof.uof_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.uof.uof_mobile.R;
import com.uof.uof_mobile.manager.UsefulFuncManager;
import com.uof.uof_mobile.other.Global;

public class PayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        return new PayAdapter.PayItemViewHolder(layoutInflater.inflate(R.layout.item_pay, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((PayItemViewHolder) viewHolder).tvPayItemMenu.setText(Global.basketManager.getOrderingItemArrayList().get(position).getMenu());
        ((PayItemViewHolder) viewHolder).tvPayItemCount.setText("X  " + Global.basketManager.getOrderingItemArrayList().get(position).getCount() + "개");
        ((PayItemViewHolder) viewHolder).tvPayItemPrice.setText(UsefulFuncManager.convertToCommaPattern(Global.basketManager.getOrderingItemArrayList().get(position).getPrice()) + "원");
        ((PayItemViewHolder) viewHolder).tvPayItemSubMenu.setText(Global.basketManager.getOrderingItemArrayList().get(position).getSubMenu().replace("&", "\n"));
        ((PayItemViewHolder) viewHolder).tvPayItemTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(Global.basketManager.getOrderingItemArrayList().get(position).getPrice() * Global.basketManager.getOrderingItemArrayList().get(position).getCount()) + "원");
    }

    @Override
    public int getItemCount() {
        return Global.basketManager.getOrderingItemArrayList().size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    // 뷰 관리자
    public class PayItemViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView tvPayItemMenu;
        public AppCompatTextView tvPayItemCount;
        public AppCompatTextView tvPayItemPrice;
        public AppCompatTextView tvPayItemSubMenu;
        public AppCompatTextView tvPayItemTotalPrice;

        public PayItemViewHolder(View view) {
            super(view);

            tvPayItemMenu = view.findViewById(R.id.tv_payitem_menu);
            tvPayItemCount = view.findViewById(R.id.tv_payitem_count);
            tvPayItemPrice = view.findViewById(R.id.tv_payitem_price);
            tvPayItemSubMenu = view.findViewById(R.id.tv_payitem_submenu);
            tvPayItemTotalPrice = view.findViewById(R.id.tv_payitem_totalprice);
        }
    }
}