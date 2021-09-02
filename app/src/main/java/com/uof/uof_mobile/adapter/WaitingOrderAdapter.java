package com.uof.uof_mobile.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.uof.uof_mobile.R;
import com.uof.uof_mobile.listitem.WaitingOrderItem;
import com.uof.uof_mobile.manager.SQLiteManager;

import java.util.ArrayList;

public class WaitingOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<WaitingOrderItem> waitingOrderItemArrayList = new ArrayList<>();  // 주문대기 목록
    private WaitingOrderAdapter.OnItemClickListener onItemClickListener = null;
    private final Context context;
    private SQLiteManager sqLiteManager;

    public WaitingOrderAdapter(Context context) {
        this.context = context;
        this.sqLiteManager = new SQLiteManager(this.context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        return new WaitingOrderAdapter.WaitingOrderViewHolder(layoutInflater.inflate(R.layout.item_waitingorder, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((WaitingOrderViewHolder) viewHolder).tvWaitingOrderCompanyName.setText(waitingOrderItemArrayList.get(position).getCompanyName());
        ((WaitingOrderViewHolder) viewHolder).tvWaitingOrderOrderNumber.setText(String.valueOf(waitingOrderItemArrayList.get(position).getOrderNumber()));

        if (waitingOrderItemArrayList.get(position).getState().equals("wait")) {
            ((WaitingOrderViewHolder) viewHolder).tvWaitingOrderMessage.setText("상품이 준비 중입니다");
            ((WaitingOrderViewHolder) viewHolder).clWaitingOrder.setBackgroundColor(context.getResources().getColor(R.color.gray));
        } else if (waitingOrderItemArrayList.get(position).getState().equals("prepared")) {
            ((WaitingOrderViewHolder) viewHolder).tvWaitingOrderMessage.setText("상품이 준비되었습니다");
            ((WaitingOrderViewHolder) viewHolder).startAnimation();
        }
    }

    public void setOnItemClickListener(WaitingOrderAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return waitingOrderItemArrayList.size();
    }

    public WaitingOrderItem getItem(int position) {
        return waitingOrderItemArrayList.get(position);
    }

    public void updateItem(ArrayList<WaitingOrderItem> waitingOrderItemArrayList) {
        this.waitingOrderItemArrayList.clear();
        this.waitingOrderItemArrayList.addAll(waitingOrderItemArrayList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    // 아이템 클릭 리스너 인터페이스
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    // 주문 뷰 관리자
    public class WaitingOrderViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout clWaitingOrder;
        public AppCompatTextView tvWaitingOrderCompanyName;
        public AppCompatTextView tvWaitingOrderOrderNumber;
        public AppCompatTextView tvWaitingOrderMessage;

        public WaitingOrderViewHolder(View view) {
            super(view);
            clWaitingOrder = view.findViewById(R.id.cl_waitingorder);
            tvWaitingOrderCompanyName = view.findViewById(R.id.tv_waitingorder_companyname);
            tvWaitingOrderOrderNumber = view.findViewById(R.id.tv_waitingorder_ordernumber);
            tvWaitingOrderMessage = view.findViewById(R.id.tv_waitingorder_message);

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
            clWaitingOrder.setBackground(context.getResources().getDrawable(R.drawable.anim_waitingorder_state));
            ((AnimationDrawable) clWaitingOrder.getBackground()).start();
        }
    }

    public ArrayList<WaitingOrderItem> getWaitingOrderItemArrayList(){
        return waitingOrderItemArrayList;
    }
}
