package com.uos.uos_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.uos.uos_mobile.item.OrderingCategoryItem;
import com.uos.uos_mobile.item.OrderingProductItem;
import com.uos.uos_mobile.item.OrderingSetItem;
import com.uos.uos_mobile.manager.UsefulFuncManager;
import com.uos.uos_mobile.other.Global;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<OrderingCategoryItem> orderingCategoryItemArrayList = new ArrayList<>();  // 카테고리별 상품 데이터
    private String selectedCategory;                                                    // 현재 선택된 카테고리
    private OnItemClickListener onItemClickListener = null;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        if (viewType == Global.ItemType.SET) {
            // 현재 생성할 뷰의 종류가 세트상품일 경우
            return new SetViewHolder(layoutInflater.inflate(com.uos.uos_mobile.R.layout.item_ordering_set, parent, false));
        } else if (viewType == Global.ItemType.PRODUCT) {
            // 현재 생성할 뷰의 종류가 단일상품일 경우
            return new ProductViewHolder(layoutInflater.inflate(com.uos.uos_mobile.R.layout.item_ordering_product, parent, false));
        } else {
            return new ProductViewHolder(layoutInflater.inflate(com.uos.uos_mobile.R.layout.item_ordering_product, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof SetViewHolder) {
            // 현재 표시할 뷰의 종류가 세트상품일 경우
            ((SetViewHolder) viewHolder).tvOrderingItemSetName.setText(getCategoryItems().get(position).getName());
            ((SetViewHolder) viewHolder).tvOrderingItemSetPrice.setText(UsefulFuncManager.convertToCommaPattern(getCategoryItems().get(position).getPrice()) + "원");
            ((SetViewHolder) viewHolder).ivOrderingItemSetImage.setImageBitmap(getCategoryItems().get(position).getImage());
        } else if (viewHolder instanceof ProductViewHolder) {
            // 현재 표시할 뷰의 종류가 단일상품일 경우
            ((ProductViewHolder) viewHolder).tvOrderingItemProductName.setText(getCategoryItems().get(position).getName());
            ((ProductViewHolder) viewHolder).tvOrderingItemProductPrice.setText(UsefulFuncManager.convertToCommaPattern(getCategoryItems().get(position).getPrice()) + "원");
            ((ProductViewHolder) viewHolder).ivOrderingItemProductImage.setImageBitmap(getCategoryItems().get(position).getImage());
        } else {
            ((ProductViewHolder) viewHolder).tvOrderingItemProductName.setText(getCategoryItems().get(position).getName());
            ((ProductViewHolder) viewHolder).tvOrderingItemProductPrice.setText(UsefulFuncManager.convertToCommaPattern(getCategoryItems().get(position).getPrice()) + "원");
            ((ProductViewHolder) viewHolder).ivOrderingItemProductImage.setImageBitmap(getCategoryItems().get(position).getImage());
        }
    }

    // category_list 내에 있는 데이터로 Adapter 데이터 설정
    public void setJson(JSONArray categoryList) {
        orderingCategoryItemArrayList.clear();
        for (int loop1 = 0; loop1 < categoryList.length(); loop1++) {
            try {
                JSONObject categoryData = categoryList.getJSONObject(loop1);

                ArrayList<OrderingProductItem> tempList = new ArrayList<>();

                // 카테고리 내 세트 추가
                JSONArray setList = categoryData.getJSONArray("set_list");
                for (int loop2 = 0; loop2 < setList.length(); loop2++) {
                    tempList.add(new OrderingSetItem(setList.getJSONObject(loop2)));
                }

                // 카테고리 내 상품 추가
                JSONArray productList = categoryData.getJSONArray("product_list");
                for (int loop2 = 0; loop2 < productList.length(); loop2++) {
                    tempList.add(new OrderingProductItem(productList.getJSONObject(loop2)));
                }

                orderingCategoryItemArrayList.add(new OrderingCategoryItem(categoryData.getString("category"), tempList));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // 현재 선택된 카테고리에 해당하는 상품들 반환
    public ArrayList<OrderingProductItem> getCategoryItems() {
        for (int loop = 0; loop < orderingCategoryItemArrayList.size(); loop++) {
            if (orderingCategoryItemArrayList.get(loop).getCategory().equals(selectedCategory)) {
                return orderingCategoryItemArrayList.get(loop).getOrderingProductItemArrayList();
            }
        }
        return null;
    }

    // 현재 선택된 카테고리 반환
    public String getSelectedCategory() {
        return selectedCategory;
    }

    // 매개변수로 들어온 카테고리를 현재 선택된 카테고리로 설정
    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
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

    // 아이템 클릭 리스너 인터페이스
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    // 단일상품 뷰 관리자
    public class ProductViewHolder extends RecyclerView.ViewHolder {
        public LinearLayoutCompat llOrderingItemProduct;
        public AppCompatTextView tvOrderingItemProductName;
        public AppCompatTextView tvOrderingItemProductPrice;
        public AppCompatImageView ivOrderingItemProductImage;

        public ProductViewHolder(View view) {
            super(view);
            llOrderingItemProduct = view.findViewById(com.uos.uos_mobile.R.id.ll_orderingitem_product);
            tvOrderingItemProductName = view.findViewById(com.uos.uos_mobile.R.id.tv_orderingitem_productname);
            tvOrderingItemProductPrice = view.findViewById(com.uos.uos_mobile.R.id.tv_orderingitem_productprice);
            ivOrderingItemProductImage = view.findViewById(com.uos.uos_mobile.R.id.iv_orderingitem_productimage);

            llOrderingItemProduct.setOnClickListener(view1 -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view1, position);
                    }
                }
            });
        }
    }

    // 세트상품 뷰 관리자
    public class SetViewHolder extends RecyclerView.ViewHolder {
        public LinearLayoutCompat llOrderingItemSet;
        public AppCompatTextView tvOrderingItemSetName;
        public AppCompatTextView tvOrderingItemSetPrice;
        public AppCompatImageView ivOrderingItemSetImage;

        public SetViewHolder(View view) {
            super(view);
            llOrderingItemSet = view.findViewById(com.uos.uos_mobile.R.id.ll_orderingitem_set);
            tvOrderingItemSetName = view.findViewById(com.uos.uos_mobile.R.id.tv_orderingitem_setname);
            tvOrderingItemSetPrice = view.findViewById(com.uos.uos_mobile.R.id.tv_orderingitem_setprice);
            ivOrderingItemSetImage = view.findViewById(com.uos.uos_mobile.R.id.iv_orderingitem_setimage);

            llOrderingItemSet.setOnClickListener(view1 -> {
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
