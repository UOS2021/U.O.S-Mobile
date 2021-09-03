package com.uof.uof_mobile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.textfield.TextInputLayout;
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.item.BasketItem;
import com.uof.uof_mobile.item.OrderingProductItem;
import com.uof.uof_mobile.manager.UsefulFuncManager;
import com.uof.uof_mobile.other.Global;

public class SelectProductDialog extends Dialog {
    private final Context context;
    private final SelectProductDialogListener selectProductDialogListener;
    private final OrderingProductItem orderingProduct;
    private AppCompatImageButton ibtnDlgSelectProductClose;
    private AppCompatTextView tvDlgSelectProductName;
    private AppCompatImageView ivDlgSelectProductImage;
    private AppCompatTextView tvDlgSelectProductDesc;
    private AppCompatImageButton ibtnDlgSelectProductCountDown;
    private TextInputLayout tilDlgSelectProductCount;
    private AppCompatImageButton ibtnDlgSelectProductCountUp;
    private AppCompatTextView tvDlgSelectProductTotalPrice;
    private AppCompatTextView tvDlgSelectProductAdd;

    public SelectProductDialog(@NonNull Context context, OrderingProductItem orderingProduct, SelectProductDialogListener selectProductDialogListener) {
        super(context, R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        this.orderingProduct = orderingProduct;
        this.selectProductDialogListener = selectProductDialogListener;

        setCanceledOnTouchOutside(false);
        setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_selectproduct);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setWindowAnimations(R.style.Anim_FullScreenDialog);

        init();
    }

    private void init() {
        ibtnDlgSelectProductClose = findViewById(R.id.ibtn_dlgselectproduct_close);
        tvDlgSelectProductName = findViewById(R.id.tv_dlgselectproduct_name);
        ivDlgSelectProductImage = findViewById(R.id.iv_dlgselectproduct_image);
        tvDlgSelectProductDesc = findViewById(R.id.tv_dlgselectproduct_desc);
        ibtnDlgSelectProductCountDown = findViewById(R.id.ibtn_dlgselectproduct_countdown);
        tilDlgSelectProductCount = findViewById(R.id.til_dlgselectproduct_count);
        ibtnDlgSelectProductCountUp = findViewById(R.id.ibtn_dlgselectproduct_countup);
        tvDlgSelectProductTotalPrice = findViewById(R.id.tv_dlgselectproduct_totalprice);
        tvDlgSelectProductAdd = findViewById(R.id.tv_dlgselectproduct_add);

        tvDlgSelectProductName.setText(orderingProduct.getName());
        ivDlgSelectProductImage.setImageBitmap(orderingProduct.getImage());
        tvDlgSelectProductDesc.setText(orderingProduct.getDesc());
        tilDlgSelectProductCount.getEditText().setText("1");
        updatePriceInfo();


        // 다이얼로그 종료 버튼 클릭 시
        ibtnDlgSelectProductClose.setOnClickListener(view -> {
            dismiss();
        });

        // 상품 개수 감소 버튼 클릭 시
        ibtnDlgSelectProductCountDown.setOnClickListener(view -> {
            int currentCount = Integer.valueOf(tilDlgSelectProductCount.getEditText().getText().toString());
            if (currentCount > 1) {
                tilDlgSelectProductCount.getEditText().setText(String.valueOf(currentCount - 1));
                updatePriceInfo();
            }
        });

        // 상품 개수 입력 영역 변경 시
        tilDlgSelectProductCount.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    tilDlgSelectProductCount.getEditText().setText("0");
                } else if (editable.toString().equals("00")) {
                    tilDlgSelectProductCount.getEditText().setText("0");
                } else {
                    int currentCount = Integer.valueOf(editable.toString());
                    if (currentCount < 1) {
                        tilDlgSelectProductCount.getEditText().setText("1");
                    }
                }
                updatePriceInfo();
            }
        });

        // 키보드에서 Done(완료) 버튼 누를 시
        tilDlgSelectProductCount.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                updatePriceInfo();
                tilDlgSelectProductCount.getEditText().clearFocus();
            }
            return false;
        });

        // 상품 개수 증가 버튼 클릭 시
        ibtnDlgSelectProductCountUp.setOnClickListener(view -> {
            tilDlgSelectProductCount.getEditText().setText(String.valueOf(Integer.valueOf(tilDlgSelectProductCount.getEditText().getText().toString()) + 1));
            updatePriceInfo();
        });

        // 상품 추가 버튼 클릭 시
        tvDlgSelectProductAdd.setOnClickListener(view -> {
            this.selectProductDialogListener.onAddProductClicked(new BasketItem(Global.ItemType.PRODUCT, orderingProduct.getName(), "", orderingProduct.getPrice(), Integer.parseInt(tilDlgSelectProductCount.getEditText().getText().toString())));
            dismiss();
        });
    }

    private void updatePriceInfo() {
        tvDlgSelectProductTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(orderingProduct.getPrice() * Integer.valueOf(tilDlgSelectProductCount.getEditText().getText().toString())));
    }

    public interface SelectProductDialogListener {
        void onAddProductClicked(BasketItem basketItem);
    }
}
