package com.uof.uof_mobile.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.dialog.BasketDialog;
import com.uof.uof_mobile.manager.UsefulFuncManager;
import com.uof.uof_mobile.other.Global;

public class BasketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final BasketDialog basketDialog;
    private BasketAdapter.OnUpdateListener onUpdateListener = null;

    public BasketAdapter(BasketDialog basketDialog) {
        this.basketDialog = basketDialog;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        return new BasketItemViewHolder(layoutInflater.inflate(R.layout.item_basket, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int itemType = Global.basketManager.getOrderingItemArrayList().get(position).getType();
        ((BasketItemViewHolder) viewHolder).position = viewHolder.getAdapterPosition();

        ((BasketItemViewHolder) viewHolder).tvBasketItemPrice.setText(UsefulFuncManager.convertToCommaPattern(Global.basketManager.getOrderingItemArrayList().get(position).getPrice()) + "원");
        ((BasketItemViewHolder) viewHolder).tvBasketItemTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(Global.basketManager.getOrderingItemArrayList().get(position).getTotalPrice()) + "원");
        ((BasketItemViewHolder) viewHolder).tilBasketItemCount.getEditText().setText(String.valueOf(Global.basketManager.getOrderingItemArrayList().get(position).getCount()));

        if (itemType == Global.ItemType.SET || itemType == Global.ItemType.PRODUCT) {
            ((BasketItemViewHolder) viewHolder).tvBasketItemMenu.setText(Global.basketManager.getOrderingItemArrayList().get(position).getMenu());
            String tempText = Global.basketManager.getOrderingItemArrayList().get(position).getSubMenu().replace("&", "\n");
            ((BasketItemViewHolder) viewHolder).tvBasketItemSubMenu.setText(tempText);
        } else if (itemType == Global.ItemType.MOVIE_TICKET) {
            ((BasketItemViewHolder) viewHolder).tvBasketItemMenu.setText(Global.basketManager.getOrderingItemArrayList().get(position).getMenu().replace("&", " - "));
            String tempText = Global.basketManager.getOrderingItemArrayList().get(position).getSubMenu().replace("&", ", ");
            ((BasketItemViewHolder) viewHolder).tvBasketItemSubMenu.setText(tempText);
            ((BasketItemViewHolder) viewHolder).tilBasketItemCount.getEditText().setEnabled(false);
            ((BasketItemViewHolder) viewHolder).ibtnBasketItemCountDown.setVisibility(View.GONE);
            ((BasketItemViewHolder) viewHolder).ibtnBasketItemCountUp.setVisibility(View.GONE);
        }
    }

    public void setOnUpdateListener(BasketAdapter.OnUpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
    }

    @Override
    public int getItemCount() {
        return Global.basketManager.getOrderingItemArrayList().size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    // 아이템 업데이트 리스너 인터페이스
    public interface OnUpdateListener {
        void onUpdate();
    }

    // 상품 뷰 관리자
    public class BasketItemViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout clBasketItem;
        public AppCompatTextView tvBasketItemMenu;
        public AppCompatImageButton ibtnBasketItemRemove;
        public AppCompatTextView tvBasketItemPrice;
        public AppCompatTextView tvBasketItemSubMenu;
        public AppCompatTextView tvBasketItemTotalPrice;
        public AppCompatImageButton ibtnBasketItemCountDown;
        public TextInputLayout tilBasketItemCount;
        public AppCompatImageButton ibtnBasketItemCountUp;
        public int position;

        public BasketItemViewHolder(View view) {
            super(view);

            clBasketItem = view.findViewById(R.id.cl_basketitem);
            tvBasketItemMenu = view.findViewById(R.id.tv_basketitem_menu);
            ibtnBasketItemRemove = view.findViewById(R.id.ibtn_basketitem_remove);
            tvBasketItemPrice = view.findViewById(R.id.tv_basketitem_price);
            tvBasketItemSubMenu = view.findViewById(R.id.tv_basketitem_submenu);
            tvBasketItemTotalPrice = view.findViewById(R.id.tv_basketitem_totalprice);
            ibtnBasketItemCountDown = view.findViewById(R.id.ibtn_basketitem_countdown);
            tilBasketItemCount = view.findViewById(R.id.til_basketitem_count);
            ibtnBasketItemCountUp = view.findViewById(R.id.ibtn_basketitem_countup);

            // 삭품 제거 버튼 클릭 시
            ibtnBasketItemRemove.setOnClickListener(v -> {
                if (Global.basketManager.getOrderingItemArrayList().get(position).getType() == Global.ItemType.MOVIE_TICKET) {
                    Global.basketManager.getOrderingItemArrayList().get(position).getMovieItem().setAllSeatSelected(false);
                }

                Global.basketManager.getOrderingItemArrayList().remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, Global.basketManager.getOrderingItemArrayList().size());

                if (position != RecyclerView.NO_POSITION) {
                    if (onUpdateListener != null) {
                        onUpdateListener.onUpdate();
                    }
                }

                if (Global.basketManager.getOrderCount() == 0) {
                    new Handler().postDelayed(() -> basketDialog.dismiss(), 300);
                }
            });

            // 상품 개수 감소 버튼 클릭 시
            ibtnBasketItemCountDown.setOnClickListener(v -> {
                int currentCount = Global.basketManager.getOrderingItemArrayList().get(position).getCount();
                if (currentCount > 1) {
                    Global.basketManager.getOrderingItemArrayList().get(position).setCount(currentCount - 1);
                    updatePriceInfo();
                }
            });

            // 상품 개수 입력 영역 변경 시
            tilBasketItemCount.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.toString().equals("") || Integer.valueOf(editable.toString()) < 1) {
                        Global.basketManager.getOrderingItemArrayList().get(position).setCount(1);
                    } else {
                        Global.basketManager.getOrderingItemArrayList().get(position).setCount(Integer.valueOf(tilBasketItemCount.getEditText().getText().toString()));
                    }
                }
            });

            // 키보드에서 Done(완료) 버튼 누를 시
            tilBasketItemCount.getEditText().setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    updatePriceInfo();
                    tilBasketItemCount.getEditText().clearFocus();
                }
                return false;
            });

            // 상품 개수 증가 버튼 클릭 시
            ibtnBasketItemCountUp.setOnClickListener(v -> {
                Global.basketManager.getOrderingItemArrayList().get(position).setCount(Global.basketManager.getOrderingItemArrayList().get(position).getCount() + 1);
                updatePriceInfo();
            });
        }

        private void updatePriceInfo() {
            tilBasketItemCount.getEditText().setText(String.valueOf(Global.basketManager.getOrderingItemArrayList().get(position).getCount()));
            tvBasketItemTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(Global.basketManager.getOrderingItemArrayList().get(position).getTotalPrice()) + "원");

            if (position != RecyclerView.NO_POSITION) {
                if (onUpdateListener != null) {
                    onUpdateListener.onUpdate();
                }
            }
        }
    }
}