package com.uos.uos_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.uos.uos_mobile.item.OrderingCategoryItem;
import com.uos.uos_mobile.item.OrderingProductItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SetProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<OrderingCategoryItem> orderingCategoryItemArrayList = new ArrayList<>();    // 세트창 내 카테고리별 상품 데이터
    private String selectedCategory;                                                            // 세트창 내 현재 선택된 카테고리
    private SetProductAdapter.OnItemClickListener onItemClickListener = null;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        return new SetProductAdapter.SetProductViewHolder(layoutInflater.inflate(com.uos.uos_mobile.R.layout.item_setproduct, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((SetProductViewHolder) viewHolder).tvSetProductName.setText(getCategoryItems().get(position).getName());
        ((SetProductViewHolder) viewHolder).tvSetProductPrice.setText(new DecimalFormat("+###,###원").format(getCategoryItems().get(position).getPrice()));
        ((SetProductViewHolder) viewHolder).tvSetProductDesc.setText(getCategoryItems().get(position).getDesc());
        ((SetProductViewHolder) viewHolder).rbSetProductSelected.setChecked(getCategoryItems().get(position).getSelected());
    }

    // category_list JSONArray 데이터를 Adapter 데이터로 저장
    public void setJson(JSONArray categoryList) {
        orderingCategoryItemArrayList.clear();
        for (int loop1 = 0; loop1 < categoryList.length(); loop1++) {
            try {
                JSONObject categoryData = categoryList.getJSONObject(loop1);

                ArrayList<OrderingProductItem> tempList = new ArrayList<>();

                // 카테고리 내 상품 추가
                JSONArray productList = categoryData.getJSONArray("product_list");
                for (int loop2 = 0; loop2 < productList.length(); loop2++) {
                    OrderingProductItem orderingProductItem = new OrderingProductItem(productList.getJSONObject(loop2));
                    tempList.add(orderingProductItem);
                    if (loop2 == 0) {
                        orderingProductItem.setSelected(true);
                    }
                }

                orderingCategoryItemArrayList.add(new OrderingCategoryItem(categoryData.getString("category"), tempList));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return getCategoryItems().size();
    }

    @Override
    public int getItemViewType(int position) {
        return getCategoryItems().get(position).getType();
    }

    public OrderingProductItem getItem(int position) {
        return getCategoryItems().get(position);
    }

    public ArrayList<OrderingProductItem> getCategoryItems() {
        for (int loop = 0; loop < orderingCategoryItemArrayList.size(); loop++) {
            if (orderingCategoryItemArrayList.get(loop).getCategory().equals(selectedCategory)) {
                return orderingCategoryItemArrayList.get(loop).getOrderingProductItemArrayList();
            }
        }
        return null;
    }

    // 현재 선택된 카테고리 반환
    public OrderingCategoryItem getSelectedCategory() {
        for (OrderingCategoryItem orderingCategoryItem : orderingCategoryItemArrayList) {
            if (orderingCategoryItem.getCategory().equals(selectedCategory)) {
                return orderingCategoryItem;
            }
        }

        return null;
    }

    // 매개변수로 들어온 카테고리를 현재 선택된 카테고리로 설정
    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    // 모든 카테고리 선택 상태 설정
    public void setCategoryProductAllChecked(boolean checked) {
        for (int loop = 0; loop < orderingCategoryItemArrayList.size(); loop++) {
            if (orderingCategoryItemArrayList.get(loop).getCategory().equals(selectedCategory)) {
                for (OrderingProductItem orderingProductItem : orderingCategoryItemArrayList.get(loop).getOrderingProductItemArrayList()) {
                    orderingProductItem.setSelected(checked);
                }
                break;
            }
        }
    }

    // 추가 비용 반환
    public int getAdditionalPrice() {
        int additionalPrice = 0;
        for (OrderingCategoryItem ordering : orderingCategoryItemArrayList) {
            for (OrderingProductItem orderingProductItem : ordering.getOrderingProductItemArrayList()) {
                if (orderingProductItem.getSelected()) {
                    additionalPrice += orderingProductItem.getPrice();
                }
            }
        }

        return additionalPrice;
    }

    // 서브메뉴 반환
    public String getSubMenu() {
        String subMenu = "";
        for (OrderingCategoryItem ordering : orderingCategoryItemArrayList) {
            for (OrderingProductItem orderingProductItem : ordering.getOrderingProductItemArrayList()) {
                if (orderingProductItem.getSelected()) {
                    subMenu += orderingProductItem.getName() + "&";
                }
            }
        }

        return subMenu.substring(0, subMenu.length() - 1);
    }

    public void setOnItemClickListener(SetProductAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class SetProductViewHolder extends RecyclerView.ViewHolder {
        public LinearLayoutCompat llSetProduct;
        public AppCompatImageView ivSetProductSelected;
        public AppCompatTextView tvSetProductName;
        public AppCompatTextView tvSetProductPrice;
        public AppCompatTextView tvSetProductDesc;
        public AppCompatRadioButton rbSetProductSelected;

        public SetProductViewHolder(View view) {
            super(view);
            llSetProduct = view.findViewById(com.uos.uos_mobile.R.id.ll_setproduct);
            tvSetProductName = view.findViewById(com.uos.uos_mobile.R.id.tv_setproduct_name);
            tvSetProductPrice = view.findViewById(com.uos.uos_mobile.R.id.tv_setproduct_price);
            tvSetProductDesc = view.findViewById(com.uos.uos_mobile.R.id.tv_setproduct_desc);
            rbSetProductSelected = view.findViewById(com.uos.uos_mobile.R.id.rb_setproduct_selected);

            llSetProduct.setOnClickListener(view1 -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view1, position);
                    }
                }
            });
        }
    }
}
