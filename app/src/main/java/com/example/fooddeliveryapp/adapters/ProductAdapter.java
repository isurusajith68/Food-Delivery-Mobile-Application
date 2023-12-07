package com.example.fooddeliveryapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapp.R;
import com.example.fooddeliveryapp.models.Product;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final Context context;
    private List<Product> productList;

    private FirebaseFirestore db;

    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productNameTextView.setText(product.getProductName());
        holder.productDescriptionTextView.setText(product.getProductDescription());
        holder.productPriceTextView.setText(String.valueOf(product.getProductPriceAsDouble()));
        db = FirebaseFirestore.getInstance();
        holder.editProductImageView.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Edit Product");

            View dialogView = LayoutInflater.from(context).inflate(R.layout.edit_product_dialog, null);
            builder.setView(dialogView);

            EditText productNameEditText = dialogView.findViewById(R.id.editProductNameEditText);
            EditText productDescriptionEditText = dialogView.findViewById(R.id.editProductDescriptionEditText);
            EditText productPriceEditText = dialogView.findViewById(R.id.editProductPriceEditText);

            productNameEditText.setText(product.getProductName());
            productDescriptionEditText.setText(product.getProductDescription());
            productPriceEditText.setText(String.valueOf(product.getProductPrice()));

            builder.setPositiveButton("Update", (dialog, which) -> {
                String updatedProductName = productNameEditText.getText().toString();
                String updatedProductDescription = productDescriptionEditText.getText().toString();
                String updatedProductPrice =productPriceEditText.getText().toString();

                product.setProductName(updatedProductName);
                product.setProductDescription(updatedProductDescription);
                product.setProductPrice(updatedProductPrice);

                DocumentReference productRef = db.collection("products").document(product.getDocumentId());

                Map<String, Object> updatedData = new HashMap<>();
                updatedData.put("productName", updatedProductName);
                updatedData.put("productDescription", updatedProductDescription);
                updatedData.put("productPrice", updatedProductPrice);

                productRef.update(updatedData)
                        .addOnSuccessListener(aVoid -> {
                            showToast("Product update successfully");
                            if (position != -1) {
                                productList.set(position, product);
                                notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(e -> {
                            showToast("Product update unsuccessfully");
                        });
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> {
                dialog.dismiss();
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });



        holder.deleteProductImageView.setOnClickListener(v -> {
            showDeleteConfirmationDialog(product.getDocumentId());

        });

        Glide.with(holder.productImageView.getContext())
                .load(product.getImageUrl())
                .placeholder(R.drawable.border)
                .error(R.drawable.border)
                .into(holder.productImageView);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productDescriptionTextView;
        TextView productPriceTextView;
        ImageView editProductImageView;
        ImageView deleteProductImageView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productDescriptionTextView = itemView.findViewById(R.id.productDescriptionTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            editProductImageView = itemView.findViewById(R.id.editProductImageView);
            deleteProductImageView = itemView.findViewById(R.id.deleteProductImageView);
        }
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

        db.collection("products")
                .document(productId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    int position = getAdapterPosition(productId);
                    if (position != -1) {
                        productList.remove(position);
                        notifyDataSetChanged();
                        showToast("Product deleted successfully");
                    }
                })
                .addOnFailureListener(e -> {
                    showToast("Failed to delete product");
                });
    }

    private int getAdapterPosition(String productId) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getDocumentId().equals(productId)) {
                return i;
            }
        }
        return -1;
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
