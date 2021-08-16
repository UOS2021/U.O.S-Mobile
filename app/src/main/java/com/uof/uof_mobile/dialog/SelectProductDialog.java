package com.uof.uof_mobile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.textfield.TextInputLayout;
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.recyclerview.RestaurantOrderingProductItem;

public class SelectProductDialog extends Dialog {
    private final Context context;
    private AppCompatImageButton ibtnDlgSelectProductClose;
    private AppCompatTextView tvDlgSelectProductName;
    private AppCompatButton btnDlgSelectProductAddProduct;
    private AppCompatImageButton ibtnDlgSelectProductCountDown;
    private TextInputLayout tilDlgSelectProductProductCount;
    private AppCompatImageButton ibtnDlgSelectProductCountUp;
    private final SelectProductDialogListener selectProductDialogListener;
    private RestaurantOrderingProductItem restaurantOrderingProduct;

    public SelectProductDialog(@NonNull Context context, RestaurantOrderingProductItem restaurantOrderingProduct, SelectProductDialogListener selectProductDialogListener) {
        super(context, R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        this.restaurantOrderingProduct = restaurantOrderingProduct;
        this.selectProductDialogListener = selectProductDialogListener;

        setCanceledOnTouchOutside(false);
        setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_selectproduct);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        init();
    }

    private void init() {
        ibtnDlgSelectProductClose = findViewById(R.id.ibtn_dlgselectproduct_close);
        tvDlgSelectProductName = findViewById(R.id.tv_dlgselectproduct_productname);
        btnDlgSelectProductAddProduct = findViewById(R.id.btn_dlgselectproduct_addproduct);
        ibtnDlgSelectProductCountDown = findViewById(R.id.ibtn_dlgselectproduct_countdown);
        tilDlgSelectProductProductCount = findViewById(R.id.til_dlgselectproduct_productcount);
        ibtnDlgSelectProductCountUp = findViewById(R.id.ibtn_dlgselectproduct_countup);

        tvDlgSelectProductName.setText(restaurantOrderingProduct.getName());
        tilDlgSelectProductProductCount.getEditText().setText("1");

        // 다이얼로그 종료 버튼 클릭 시
        ibtnDlgSelectProductClose.setOnClickListener(view -> {
            dismiss();
        });

        // 상품 개수 감소 버튼 클릭 시
        ibtnDlgSelectProductCountDown.setOnClickListener(view -> {
            int currentCount = Integer.valueOf(tilDlgSelectProductProductCount.getEditText().getText().toString());
            if (currentCount > 1) {
                tilDlgSelectProductProductCount.getEditText().setText(String.valueOf(currentCount - 1));
            }
        });

        // 상품 개수 입력 영역 변경 시
        tilDlgSelectProductProductCount.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    tilDlgSelectProductProductCount.getEditText().setText("1");
                } else {
                    int currentCount = Integer.valueOf(editable.toString());
                    if (currentCount < 1) {
                        tilDlgSelectProductProductCount.getEditText().setText("1");
                    }
                }
            }
        });

        // 상품 개수 증가 버튼 클릭 시
        ibtnDlgSelectProductCountUp.setOnClickListener(view -> {
            tilDlgSelectProductProductCount.getEditText().setText(String.valueOf(Integer.valueOf(tilDlgSelectProductProductCount.getEditText().getText().toString()) + 1));
        });

        // 상품 추가 버튼 클릭 시
        btnDlgSelectProductAddProduct.setOnClickListener(view -> {
            this.selectProductDialogListener.onAddProductClicked(Integer.parseInt(tilDlgSelectProductProductCount.getEditText().getText().toString()));
            dismiss();
        });
    }

    public interface SelectProductDialogListener {
        void onAddProductClicked(int count);
    }
}
