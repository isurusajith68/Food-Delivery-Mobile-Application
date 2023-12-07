package com.example.fooddeliveryapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapp.R;
import com.example.fooddeliveryapp.models.AllProduct;

import java.util.List;

public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.ViewHolder> {

    private List<AllProduct> productList;
    private OnProductItemClickListener itemClickListener;

    public void setOnProductItemClickListener(OnProductItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnProductItemClickListener {
        void onProductItemClick(String product);
    }
    public AllProductAdapter(List<AllProduct> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllProduct product = productList.get(position);

        holder.productNameTextView.setText(product.getProductName());
        holder.productPriceTextView.setText(product.getProductPrice());
        holder.restaurantNameTextView.setText(product.getRestaurantName());


        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .into(holder.productImageView);

        holder.itemView.setOnClickListener(v -> {
            String productId = product.getDocumentId();
            if (itemClickListener != null) {
                itemClickListener.onProductItemClick(productId);
            }
        });


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productPriceTextView;
        TextView restaurantNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            restaurantNameTextView = itemView.findViewById(R.id.restaurantNameTextView);
        }
    }
}
