package com.example.fooddeliveryapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryapp.R;
import com.example.fooddeliveryapp.models.ProductReview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<ProductReview> reviewList;
    private FirebaseAuth mAuth;
    private String authUser;
    private Context context;
    public ReviewAdapter(List<ProductReview> reviewList  , Context context) {
        this.reviewList = reviewList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ProductReview review = reviewList.get(position);
        holder.usernameTextView.setText(review.getUsername());
        holder.reviewTextView.setText(review.getReview());

        mAuth = FirebaseAuth.getInstance();
        authUser = mAuth.getCurrentUser().getEmail();

        if (review.getUserEmail().equals(authUser)) {
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setOnClickListener(v -> {
                String reviewId = review.getDocumentId();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Review");
                builder.setMessage("Do you want to delete your review?");
                builder.setPositiveButton("Delete", (dialog, which) -> {
                    deleteReview(reviewId);
                });
                builder.setNegativeButton("Cancel", null);
                AlertDialog dialog = builder.create();
                dialog.show();

            });
        } else {
            holder.delete.setVisibility(View.GONE);
        }
    }

    private void deleteReview(String reviewId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reviews")
                .document(reviewId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    int position = getAdapterPosition(reviewId);
                    if (position != -1) {
                        reviewList.remove(position);
                        notifyDataSetChanged();
                        showToast("Review deleted successfully");
                    }

                })
                .addOnFailureListener(e -> {
                    showToast("Review delete Failed");
                });
    }
    private int getAdapterPosition(String reviewId) {
        for (int i = 0; i < reviewList.size(); i++) {
            if (reviewList.get(i).getDocumentId().equals(reviewId)) {
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
        return reviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        public TextView usernameTextView;
        public TextView reviewTextView;
        public ImageView delete;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            reviewTextView = itemView.findViewById(R.id.reviewTextView);
            delete = itemView.findViewById(R.id.deleteProductImageView);
        }
    }
}
