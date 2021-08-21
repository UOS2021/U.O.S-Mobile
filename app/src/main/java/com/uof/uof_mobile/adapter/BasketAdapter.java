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
    private BasketManager basketManager;
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
        ((BasketItemViewHolder) viewHolder).tvBasketMenu.setText(basketManager.getOrderingItemArrayList().get(position).getMenu());
        ((BasketItemViewHolder) viewHolder).tvBasketPrice.setText(UsefulFuncManager.convertToCommaPattern(basketManager.getOrderingItemArrayList().get(position).getPrice()) + "원");
        ((BasketItemViewHolder) viewHolder).tvBasketSubMenu.setText(basketManager.getOrderingItemArrayList().get(position).getSubMenu().replace("&", "\n"));
        ((BasketItemViewHolder) viewHolder).tvBasketTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(basketManager.getOrderingItemArrayList().get(position).getTotalPrice()) + "원");
        ((BasketItemViewHolder) viewHolder).tilBasketCount.getEditText().setText(String.valueOf(basketManager.getOrderingItemArrayList().get(position).getCount()));
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
        public ConstraintLayout clBasket;
        public AppCompatTextView tvBasketMenu;
        public AppCompatImageButton ibtnBasketRemove;
        public AppCompatTextView tvBasketPrice;
        public AppCompatTextView tvBasketSubMenu;
        public AppCompatTextView tvBasketTotalPrice;
        public AppCompatImageButton ibtnBasketCountDown;
        public TextInputLayout tilBasketCount;
        public AppCompatImageButton ibtnBasketCountUp;
        public int position;

        public BasketItemViewHolder(View view) {
            super(view);

            clBasket = view.findViewById(R.id.cl_basket);
            tvBasketMenu = view.findViewById(R.id.tv_basket_menu);
            ibtnBasketRemove = view.findViewById(R.id.ibtn_basket_remove);
            tvBasketPrice = view.findViewById(R.id.tv_basket_price);
            tvBasketSubMenu = view.findViewById(R.id.tv_basket_submenu);
            tvBasketTotalPrice = view.findViewById(R.id.tv_basket_totalprice);
            ibtnBasketCountDown = view.findViewById(R.id.ibtn_basket_countdown);
            tilBasketCount = view.findViewById(R.id.til_basket_count);
            ibtnBasketCountUp = view.findViewById(R.id.ibtn_basket_countup);

            // 삭품 제거 버튼 클릭 시
            ibtnBasketRemove.setOnClickListener(v -> {
                basketManager.getOrderingItemArrayList().remove(position);
                notifyItemRemoved(position);
                if (position != RecyclerView.NO_POSITION) {
                    if (onUpdateListener != null) {
                        onUpdateListener.onUpdate();
                    }
                }
            });

            // 상품 개수 감소 버튼 클릭 시
            ibtnBasketCountDown.setOnClickListener(v -> {
                int currentCount = basketManager.getOrderingItemArrayList().get(position).getCount();
                if (currentCount > 1) {
                    basketManager.getOrderingItemArrayList().get(position).setCount(currentCount - 1);
                    updatePriceInfo();
                }
            });

            // 상품 개수 입력 영역 변경 시
            tilBasketCount.getEditText().addTextChangedListener(new TextWatcher() {
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
                        basketManager.getOrderingItemArrayList().get(position).setCount(Integer.valueOf(tilBasketCount.getEditText().getText().toString()));
                    }
                }
            });

            // 키보드에서 Done(완료) 버튼 누를 시
            tilBasketCount.getEditText().setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    updatePriceInfo();
                    tilBasketCount.getEditText().clearFocus();
                }
                return false;
            });

            // 상품 개수 증가 버튼 클릭 시
            ibtnBasketCountUp.setOnClickListener(v -> {
                basketManager.getOrderingItemArrayList().get(position).setCount(basketManager.getOrderingItemArrayList().get(position).getCount() + 1);
                updatePriceInfo();
            });
        }

        private void updatePriceInfo() {
            tilBasketCount.getEditText().setText(String.valueOf(basketManager.getOrderingItemArrayList().get(position).getCount()));
            tvBasketTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(basketManager.getOrderingItemArrayList().get(position).getTotalPrice()) + "원");

            if (position != RecyclerView.NO_POSITION) {
                if (onUpdateListener != null) {
                    onUpdateListener.onUpdate();
                }
            }
        }
    }
}