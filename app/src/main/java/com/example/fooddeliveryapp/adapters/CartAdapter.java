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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItemList;
    private Context context;

    public CartAdapter(List<CartItem> cartItemList , Context context) {
        this.cartItemList = cartItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        holder.productNameTextView.setText(cartItem.getProductName());
        holder.productPriceTextView.setText(String.format("%.2f", cartItem.getProductPrice()));
        holder.quantityTextView.setText(String.valueOf(cartItem.getQuantity()));
        holder.totalPriceTextView.setText(cartItem.getTotalPrice());

        Glide.with(context)
                .load(cartItem.getImageUrl())
                .into(holder.productImageView);

        holder.deleteProductImageView.setOnClickListener(v -> {
            showDeleteConfirmationDialog(cartItem.getDocumentId());

        });

        holder.itemView.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("selectedCartItemDocumentId", cartItem.getDocumentId());
            editor.apply();

            Intent intent = new Intent(context, PlaceOrder.class);
            context.startActivity(intent);
        });

        holder.editProductImageView.setOnClickListener(v -> {
            openEditDialog(cartItem, position);
        });
    }

    private void openEditDialog(CartItem cartItem, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.edit_cart_dialog, null);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        EditText quantityEditText = dialogView.findViewById(R.id.quantityEditText);
        Button updateButton = dialogView.findViewById(R.id.updateButton);

        updateButton.setOnClickListener(v -> {
            int newQuantity = Integer.parseInt(quantityEditText.getText().toString());

            cartItem.setQuantity(newQuantity);
            cartItem.calculateTotalPrice();

            cartItemList.set(position, cartItem);
            notifyItemChanged(position);

            dialog.dismiss();
        });

        dialog.show();
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

        db.collection("cart")
                .document(productId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    int position = getAdapterPosition(productId);
                    if (position != -1) {
                        cartItemList.remove(position);
                        notifyDataSetChanged();
                        showToast("Product deleted successfully");
                    }
                })
                .addOnFailureListener(e -> {
                    showToast("Failed to delete product");
                });
    }

    private int getAdapterPosition(String productId) {
        for (int i = 0; i < cartItemList.size(); i++) {
            if (cartItemList.get(i).getDocumentId().equals(productId)) {
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
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        public TextView productNameTextView;
        public TextView productPriceTextView;
        public TextView quantityTextView;
        public TextView totalPriceTextView;
        public ImageView productImageView;
        public ImageView deleteProductImageView;
        public ImageView editProductImageView;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            totalPriceTextView = itemView.findViewById(R.id.totalPriceTextView);
            productImageView = itemView.findViewById(R.id.productImageView);
            deleteProductImageView = itemView.findViewById(R.id.deleteProductImageView);
            editProductImageView = itemView.findViewById(R.id.editProductImageView);
        }
    }
}
