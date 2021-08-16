package com.uof.uof_mobile.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.uof.uof_mobile.Constants;
import com.uof.uof_mobile.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RestaurantOrderingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<RestaurantOrderingProductItem> restaurantOrderingItemArrayList = new ArrayList<>();
    private OnItemClickListener onItemClickListener = null;

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        public LinearLayoutCompat llRestaurantOrderingProduct;
        public AppCompatTextView tvRestaurantOrderingProductName;
        public AppCompatTextView tvRestaurantOrderingProductPrice;
        public AppCompatImageView ivRestaurantOrderingProductImage;

        public ProductViewHolder(View view) {
            super(view);
            llRestaurantOrderingProduct = view.findViewById(R.id.ll_restaurantordering_product);
            tvRestaurantOrderingProductName = view.findViewById(R.id.tv_restaurantordering_productname);
            tvRestaurantOrderingProductPrice = view.findViewById(R.id.tv_restaurantordering_productprice);
            ivRestaurantOrderingProductImage = view.findViewById(R.id.iv_restaurantordering_productimage);

            llRestaurantOrderingProduct.setOnClickListener(view1 -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view1, position);
                    }
                }
            });
        }
    }

    public class SetViewHolder extends RecyclerView.ViewHolder {
        public LinearLayoutCompat llRestaurantOrderingSet;
        public AppCompatTextView tvRestaurantOrderingSetName;
        public AppCompatTextView tvRestaurantOrderingSetPrice;
        public AppCompatImageView ivRestaurantOrderingSetImage;

        public SetViewHolder(View view) {
            super(view);
            llRestaurantOrderingSet = view.findViewById(R.id.ll_restaurantordering_set);
            tvRestaurantOrderingSetName = view.findViewById(R.id.tv_restaurantordering_setname);
            tvRestaurantOrderingSetPrice = view.findViewById(R.id.tv_restaurantordering_setprice);
            ivRestaurantOrderingSetImage = view.findViewById(R.id.iv_restaurantordering_setimage);

            llRestaurantOrderingSet.setOnClickListener(view1 -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view1, position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        if (viewType == Constants.ItemType.SET) {
            return new SetViewHolder(layoutInflater.inflate(R.layout.item_restaurantordering_set, parent, false));
        } else if (viewType == Constants.ItemType.PRODUCT) {
            return new ProductViewHolder(layoutInflater.inflate(R.layout.item_restaurantordering_product, parent, false));
        } else {
            return new ProductViewHolder(layoutInflater.inflate(R.layout.item_restaurantordering_product, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof SetViewHolder) {
            ((SetViewHolder) viewHolder).tvRestaurantOrderingSetName.setText(restaurantOrderingItemArrayList.get(position).getName());
            ((SetViewHolder) viewHolder).tvRestaurantOrderingSetPrice.setText(String.valueOf(restaurantOrderingItemArrayList.get(position).getPrice()));
        } else if (viewHolder instanceof ProductViewHolder) {
            ((ProductViewHolder) viewHolder).tvRestaurantOrderingProductName.setText(restaurantOrderingItemArrayList.get(position).getName());
            ((ProductViewHolder) viewHolder).tvRestaurantOrderingProductPrice.setText(String.valueOf(restaurantOrderingItemArrayList.get(position).getPrice()));
        } else {
            ((ProductViewHolder) viewHolder).tvRestaurantOrderingProductName.setText(restaurantOrderingItemArrayList.get(position).getName());
            ((ProductViewHolder) viewHolder).tvRestaurantOrderingProductPrice.setText(String.valueOf(restaurantOrderingItemArrayList.get(position).getPrice()));
        }
    }

    public void addItem(RestaurantOrderingProductItem restaurantOrderingItem) {
        restaurantOrderingItemArrayList.add(restaurantOrderingItem);
    }

    public void setJson(JSONArray categoryList) {
        restaurantOrderingItemArrayList.clear();
        for (int loop1 = 0; loop1 < categoryList.length(); loop1++) {
            try {
                JSONObject categoryData = categoryList.getJSONObject(loop1);

                // 카테고리 내 세트 추가
                JSONArray setList = categoryData.getJSONArray("set_list");
                for (int loop2 = 0; loop2 < setList.length(); loop2++) {
                    addItem(new RestaurantOrderingSetItem(setList.getJSONObject(loop2)));
                }

                // 카테고리 내 상품 추가
                JSONArray productList = categoryData.getJSONArray("product_list");
                for (int loop2 = 0; loop2 < productList.length(); loop2++) {
                    addItem(new RestaurantOrderingProductItem(productList.getJSONObject(loop2)));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return restaurantOrderingItemArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return restaurantOrderingItemArrayList.get(position).getType();
    }

    public RestaurantOrderingProductItem getItem(int position) {
        return restaurantOrderingItemArrayList.get(position);
    }
}
