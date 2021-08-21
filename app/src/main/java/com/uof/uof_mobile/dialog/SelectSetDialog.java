package com.uof.uof_mobile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.adapter.SetProductAdapter;
import com.uof.uof_mobile.listitem.OrderingItem;
import com.uof.uof_mobile.listitem.OrderingProductItem;
import com.uof.uof_mobile.listitem.OrderingSetItem;
import com.uof.uof_mobile.manager.UsefulFuncManager;

public class SelectSetDialog extends Dialog {
    private final Context context;
    private final SelectSetDialog.SelectSetDialogListener selectSetDialogListener;
    private final OrderingSetItem setData;
    private AppCompatImageButton ibtnDlgSelectSetClose;
    private AppCompatTextView tvDlgSelectSetName;
    private AppCompatImageView ivDlgSelectSetImage;
    private AppCompatImageButton ibtnDlgSelectSetCountDown;
    private TextInputLayout tilDlgSelectSetCount;
    private AppCompatImageButton ibtnDlgSelectSetCountUp;
    private AppCompatTextView tvDlgSelectSetConf;
    private AppCompatTextView tvDlgSelectSetDesc;
    private ChipGroup cgDlgSelectSetCategoryList;
    private RecyclerView rvDlgSelectSetProductList;
    private LinearLayoutCompat llDlgSelectSetAdd;
    private AppCompatTextView tvDlgSelectSetTotalPrice;
    private SetProductAdapter setProductAdapter;
    private String selectedCategory;

    public SelectSetDialog(@NonNull Context context, OrderingSetItem setData, SelectSetDialogListener selectSetDialogListener) {
        super(context, R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        this.selectSetDialogListener = selectSetDialogListener;
        this.setData = setData;

        setCanceledOnTouchOutside(false);
        setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_selectset);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setWindowAnimations(R.style.Anim_FullScreenDialog);

        init();
    }

    private void init() {
        ibtnDlgSelectSetClose = findViewById(R.id.ibtn_dlgselectset_close);
        tvDlgSelectSetName = findViewById(R.id.tv_dlgselectset_name);
        ivDlgSelectSetImage = findViewById(R.id.iv_dlgselectset_image);
        ibtnDlgSelectSetCountDown = findViewById(R.id.ibtn_dlgselectset_countdown);
        tilDlgSelectSetCount = findViewById(R.id.til_dlgselectset_count);
        ibtnDlgSelectSetCountUp = findViewById(R.id.ibtn_dlgselectset_countup);
        tvDlgSelectSetConf = findViewById(R.id.tv_dlgselectset_conf);
        tvDlgSelectSetDesc = findViewById(R.id.tv_dlgselectset_desc);
        cgDlgSelectSetCategoryList = findViewById(R.id.cg_dlgselectset_categorylist);
        rvDlgSelectSetProductList = findViewById(R.id.rv_dlgselectset_productlist);
        llDlgSelectSetAdd = findViewById(R.id.ll_dlgselectset_add);
        tvDlgSelectSetTotalPrice = findViewById(R.id.tv_dlgselectset_totalprice);

        try {
            tvDlgSelectSetName.setText(setData.getName());
            ivDlgSelectSetImage.setImageBitmap(setData.getImage());
            tvDlgSelectSetConf.setText(setData.getConf());
            tvDlgSelectSetDesc.setText(setData.getDesc());
            tilDlgSelectSetCount.getEditText().setText("1");

            // 세트 내 상품 목록 Adapter 설정
            setProductAdapter = new SetProductAdapter();
            setProductAdapter.setJson(setData.getProductList());
            rvDlgSelectSetProductList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
            dividerItemDecoration.setDrawable(context.getResources().getDrawable(R.drawable.recyclerview_divider));
            rvDlgSelectSetProductList.addItemDecoration(dividerItemDecoration);
            rvDlgSelectSetProductList.setAdapter(setProductAdapter);

            updatePriceInfo();

            // 카테고리를 chipgroup에 추가
            for (int loop = 0; loop < setData.getProductList().length(); loop++) {
                Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_category, cgDlgSelectSetCategoryList, false);
                try {
                    chip.setText(setData.getProductList().getJSONObject(loop).getString("category"));
                    chip.setOnClickListener(view -> {
                        selectedCategory = chip.getText().toString();
                        setProductAdapter.setSelectedCategory(selectedCategory);
                        rvDlgSelectSetProductList.setAdapter(setProductAdapter);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cgDlgSelectSetCategoryList.addView(chip);
                if (loop == 0) {
                    selectedCategory = chip.getText().toString();
                    setProductAdapter.setSelectedCategory(selectedCategory);
                    rvDlgSelectSetProductList.setAdapter(setProductAdapter);
                    chip.setChecked(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }

        // 다이얼로그 종료 버튼 클릭 시
        ibtnDlgSelectSetClose.setOnClickListener(view -> {
            dismiss();
        });

        // 상품 개수 감소 버튼 클릭 시
        ibtnDlgSelectSetCountDown.setOnClickListener(view -> {
            int currentCount = Integer.valueOf(tilDlgSelectSetCount.getEditText().getText().toString());
            if (currentCount > 0) {
                tilDlgSelectSetCount.getEditText().setText(String.valueOf(currentCount - 1));
                updatePriceInfo();
            }
        });

        // 상품 개수 입력 영역 변경 시
        tilDlgSelectSetCount.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    tilDlgSelectSetCount.getEditText().setText("0");
                } else {
                    int currentCount = Integer.valueOf(editable.toString());
                    if (currentCount < 0) {
                        tilDlgSelectSetCount.getEditText().setText("0");
                    }
                }
                updatePriceInfo();
            }
        });

        // 키보드에서 Done(완료) 버튼 누를 시
        tilDlgSelectSetCount.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                updatePriceInfo();
                tilDlgSelectSetCount.getEditText().clearFocus();
            }
            return false;
        });

        // 상품 개수 증가 버튼 클릭 시
        ibtnDlgSelectSetCountUp.setOnClickListener(view -> {
            tilDlgSelectSetCount.getEditText().setText(String.valueOf(Integer.valueOf(tilDlgSelectSetCount.getEditText().getText().toString()) + 1));
            updatePriceInfo();
        });

        // 리스트 아이템이 눌렸을 경우
        setProductAdapter.setOnItemClickListener((view, position) -> {
            OrderingProductItem orderingProductItem = setProductAdapter.getItem(position);

            if (!orderingProductItem.getSelected()) {
                // 현재 아이템이 선택되어있지 않았을 경우
                setProductAdapter.setCategoryProductAllChecked(false);
                orderingProductItem.setSelected(true);
                updatePriceInfo();
            }
            setProductAdapter.notifyDataSetChanged();
        });

        // 상품 추가 버튼 클릭 시
        llDlgSelectSetAdd.setOnClickListener(view -> {
            this.selectSetDialogListener.onAddProductClicked(new OrderingItem(setData.getName(), setProductAdapter.getSubMenu(), setData.getPrice() + setProductAdapter.getAdditionalPrice(), Integer.valueOf(tilDlgSelectSetCount.getEditText().getText().toString()), setData.getImage()));
            dismiss();
        });
    }

    private void updatePriceInfo() {
        tvDlgSelectSetTotalPrice.setText(UsefulFuncManager.convertToCommaPattern(getTotalPrice()) + "원");
    }

    private int getTotalPrice() {
        return (setData.getPrice() + setProductAdapter.getAdditionalPrice()) * Integer.valueOf(tilDlgSelectSetCount.getEditText().getText().toString());
    }

    public interface SelectSetDialogListener {
        void onAddProductClicked(OrderingItem orderingItem);
    }
}
