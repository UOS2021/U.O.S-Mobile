package com.uof.uof_mobile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.textfield.TextInputLayout;
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.manager.UsefulFuncManager;
import com.uof.uof_mobile.recyclerview.RestaurantOrderingProductItem;

public class SelectProductDialog extends Dialog {
    private final Context context;
    private AppCompatImageButton ibtnDlgSelectProductClose;
    private AppCompatTextView tvDlgSelectProductName;
    private AppCompatImageView ivDlgSelectProductImage;
    private AppCompatTextView tvDlgSelectProductDesc;
    private AppCompatImageButton ibtnDlgSelectProductCountDown;
    private TextInputLayout tilDlgSelectProductProductCount;
    private AppCompatImageButton ibtnDlgSelectProductCountUp;
    private AppCompatTextView tvDlgSelectProductPayAmount;
    private AppCompatTextView tvDlgSelectProductAdd;
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
        getWindow().setWindowAnimations(R.style.Anim_FullScreenDialog);

        init();
    }

    private void init() {
        ibtnDlgSelectProductClose = findViewById(R.id.ibtn_dlgselectproduct_close);
        tvDlgSelectProductName = findViewById(R.id.tv_dlgselectproduct_name);
        ivDlgSelectProductImage = findViewById(R.id.iv_dlgselectproduct_image);
        tvDlgSelectProductDesc = findViewById(R.id.tv_dlgselectproduct_desc);
        ibtnDlgSelectProductCountDown = findViewById(R.id.ibtn_dlgselectproduct_countdown);
        tilDlgSelectProductProductCount = findViewById(R.id.til_dlgselectproduct_count);
        ibtnDlgSelectProductCountUp = findViewById(R.id.ibtn_dlgselectproduct_countup);
        tvDlgSelectProductPayAmount = findViewById(R.id.tv_dlgselectproduct_payamount);
        tvDlgSelectProductAdd = findViewById(R.id.tv_dlgselectproduct_add);

        tvDlgSelectProductName.setText(restaurantOrderingProduct.getName());
        ivDlgSelectProductImage.setImageBitmap(restaurantOrderingProduct.getImage());
        tvDlgSelectProductDesc.setText(restaurantOrderingProduct.getDesc());
        tilDlgSelectProductProductCount.getEditText().setText("0");
        tvDlgSelectProductPayAmount.setText("0");

        // 다이얼로그 종료 버튼 클릭 시
        ibtnDlgSelectProductClose.setOnClickListener(view -> {
            dismiss();
        });

        // 상품 개수 감소 버튼 클릭 시
        ibtnDlgSelectProductCountDown.setOnClickListener(view -> {
            int currentCount = Integer.valueOf(tilDlgSelectProductProductCount.getEditText().getText().toString());
            if (currentCount > 0) {
                tilDlgSelectProductProductCount.getEditText().setText(String.valueOf(currentCount - 1));
                tvDlgSelectProductPayAmount.setText(UsefulFuncManager.convertToCommaPattern(restaurantOrderingProduct.getPrice() * Integer.valueOf(tilDlgSelectProductProductCount.getEditText().getText().toString())));
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
                    tilDlgSelectProductProductCount.getEditText().setText("0");
                } else {
                    int currentCount = Integer.valueOf(editable.toString());
                    if (currentCount < 0) {
                        tilDlgSelectProductProductCount.getEditText().setText("0");
                    }
                }
            }
        });

        // 키보드에서 Done(완료) 버튼 누를 시
        tilDlgSelectProductProductCount.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if(actionId== EditorInfo.IME_ACTION_DONE){
                tvDlgSelectProductPayAmount.setText(UsefulFuncManager.convertToCommaPattern(restaurantOrderingProduct.getPrice() * Integer.valueOf(tilDlgSelectProductProductCount.getEditText().getText().toString())));
                tilDlgSelectProductProductCount.getEditText().clearFocus();
            }
            return false;
        });

        // 상품 개수 증가 버튼 클릭 시
        ibtnDlgSelectProductCountUp.setOnClickListener(view -> {
            tilDlgSelectProductProductCount.getEditText().setText(String.valueOf(Integer.valueOf(tilDlgSelectProductProductCount.getEditText().getText().toString()) + 1));
            tvDlgSelectProductPayAmount.setText(UsefulFuncManager.convertToCommaPattern(restaurantOrderingProduct.getPrice() * Integer.valueOf(tilDlgSelectProductProductCount.getEditText().getText().toString())));
        });

        // 상품 추가 버튼 클릭 시
        tvDlgSelectProductAdd.setOnClickListener(view -> {
            this.selectProductDialogListener.onAddProductClicked(Integer.parseInt(tilDlgSelectProductProductCount.getEditText().getText().toString()));
            dismiss();
        });
    }

    public interface SelectProductDialogListener {
        void onAddProductClicked(int count);
    }
}
