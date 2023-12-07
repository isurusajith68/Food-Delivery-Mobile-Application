package com.example.fooddeliveryapp.adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapp.R;
import com.example.fooddeliveryapp.activities.PlaceOrder;
import com.example.fooddeliveryapp.models.CartItem;
import com.example.fooddeliveryapp.models.Order;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderItemList;
    private Context context;
    private OnOrderItemClickListener itemClickListener;

    public void setOnOrderItemClickListener(OnOrderItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnOrderItemClickListener {
        void OnOrderItemClick(String order);
    }

    public OrderAdapter(List<Order> orderItemList , Context context) {
        this.orderItemList = orderItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order orderItem = orderItemList.get(position);
        holder.productNameTextView.setText(orderItem.getProductName());
        holder.quantityTextView.setText(String.valueOf(orderItem.getQuantity()));
        holder.totalPriceTextView.setText(orderItem.getTotalPrice());
        holder.orderStatusTextView.setText(orderItem.getOrderStatus());
        holder.paymentMethod.setText(orderItem.getPaymentType());


        Glide.with(context)
                .load(orderItem.getImageUrl())
                .into(holder.productImageView);

        holder.deleteProductImageView.setOnClickListener(v -> {
            showDeleteConfirmationDialog(orderItem.getDocumentId());
        });

        holder.itemView.setOnClickListener(v -> {
            String productId = orderItem.getDocumentId();
            if (itemClickListener != null) {
                itemClickListener.OnOrderItemClick(productId);
            }
        });
    }
    private void showDeleteConfirmationDialog(String productId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Product");
        builder.setMessage("Are you sure you want to delete this product?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            deleteProductFromFirestore(productId);
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();


    }
    private void deleteProductFromFirestore(String productId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("orders")
                .document(productId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    int position = getAdapterPosition(productId);
                    if (position != -1) {
                        orderItemList.remove(position);
                        notifyDataSetChanged();
                        showToast("Order Cancel");
                    }
                })
                .addOnFailureListener(e -> {
                    showToast("Failed Cancel Order");
                });
    }

    private int getAdapterPosition(String productId) {
        for (int i = 0; i < orderItemList.size(); i++) {
            if (orderItemList.get(i).getDocumentId().equals(productId)) {
                return i;
            }
        }
        return -1;
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView productNameTextView;
        public TextView orderStatusTextView;
        public TextView quantityTextView;
        public TextView totalPriceTextView;
        public TextView paymentMethod;
        public ImageView productImageView;
        public ImageView deleteProductImageView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            orderStatusTextView = itemView.findViewById(R.id.productPriceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);
            totalPriceTextView = itemView.findViewById(R.id.totalPriceTextView);
            productImageView = itemView.findViewById(R.id.productImageView);
            deleteProductImageView = itemView.findViewById(R.id.deleteProductImageView);
        }
    }
}

