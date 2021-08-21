package com.uof.uof_mobile.adapter;

import android.content.Context;
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
import com.uof.uof_mobile.manager.BasketManager;
import com.uof.uof_mobile.manager.UsefulFuncManager;

public class BasketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final BasketManager basketManager;
    private BasketAdapter.OnUpdateListener onUpdateListener = null;

    public BasketAdapter(BasketManager basketManager) {
        this.basketManager = basketManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        return new BasketItemViewHolder(layoutInflater.inflate(R.layout.item_basket, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((BasketItemViewHolder) viewHolder).position = viewHolder.getAdapterPosition();
        ((BasketItemViewHolder) viewHolder).tvBasketItemMenu.setText(basketManager.getOrderingItemArrayList().get(position).getMenu());
        ((BasketItemViewHolder) viewHolder).tvBasketItemPrice.setText(UsefulFuncManager.convertToCommaPattern(basketManager.getOrderingItemArrayList().get(position).getPrice()) + "원");
        ((BasketItemViewHolder) viewHolder).tvBasketItemSubMenu.setText(basketManager.getOrderingItemArrayList().get(position).getSubMenu().replace("&", "\n"));
        ((BasketItemViewHolder) viewHolder).tvBasketItemTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(basketManager.getOrderingItemArrayList().get(position).getTotalPrice()) + "원");
        ((BasketItemViewHolder) viewHolder).tilBasketItemCount.getEditText().setText(String.valueOf(basketManager.getOrderingItemArrayList().get(position).getCount()));
    }

    public void setOnUpdateListener(BasketAdapter.OnUpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
    }

    @Override
    public int getItemCount() {
        return basketManager.getOrderingItemArrayList().size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    // 아이템 업데이트 리스너 인터페이스
    public interface OnUpdateListener {
        void onUpdate();
    }

    // 단일상품 뷰 관리자
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
                basketManager.getOrderingItemArrayList().remove(position);
                notifyItemRemoved(position);
                if (position != RecyclerView.NO_POSITION) {
                    if (onUpdateListener != null) {
                        onUpdateListener.onUpdate();
                    }
                }
            });

            // 상품 개수 감소 버튼 클릭 시
            ibtnBasketItemCountDown.setOnClickListener(v -> {
                int currentCount = basketManager.getOrderingItemArrayList().get(position).getCount();
                if (currentCount > 1) {
                    basketManager.getOrderingItemArrayList().get(position).setCount(currentCount - 1);
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
                        basketManager.getOrderingItemArrayList().get(position).setCount(1);
                    } else {
                        basketManager.getOrderingItemArrayList().get(position).setCount(Integer.valueOf(tilBasketItemCount.getEditText().getText().toString()));
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
                basketManager.getOrderingItemArrayList().get(position).setCount(basketManager.getOrderingItemArrayList().get(position).getCount() + 1);
                updatePriceInfo();
            });
        }

        private void updatePriceInfo() {
            tilBasketItemCount.getEditText().setText(String.valueOf(basketManager.getOrderingItemArrayList().get(position).getCount()));
            tvBasketItemTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(basketManager.getOrderingItemArrayList().get(position).getTotalPrice()) + "원");

            if (position != RecyclerView.NO_POSITION) {
                if (onUpdateListener != null) {
                    onUpdateListener.onUpdate();
                }
            }
        }
    }
}