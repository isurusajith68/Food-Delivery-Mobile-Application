package com.example.fooddeliveryapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapp.R;
import com.example.fooddeliveryapp.models.AllProduct;
import com.example.fooddeliveryapp.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddReviewActivity extends AppCompatActivity {
    private ImageView productImageView;
    private TextView productNameTextView;
    private  TextView priceTextView;
    private  TextView stateTextView;
    private  TextView cityTextView;
    private  TextView paymentTypeTextView;
    private  TextView orderStatusTextView;
    private TextView restaurantNameTextView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String selectedOrderId;
    private String userEmail;
    private Order selectedProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        selectedOrderId = getIntent().getStringExtra("selectedOrderId");


        userEmail = mAuth.getCurrentUser().getEmail();

        fetchProductData(selectedOrderId, userEmail);
        productImageView = findViewById(R.id.productImageView);
        productNameTextView = findViewById(R.id.productName);

        priceTextView = findViewById(R.id.price);
        stateTextView = findViewById(R.id.state);
        cityTextView = findViewById(R.id.city);
        paymentTypeTextView = findViewById(R.id.paymentType);
        orderStatusTextView = findViewById(R.id.orderStatus);

//
//        Button addToCartButton = findViewById(R.id.addToCartButton);
//        addToCartButton.setOnClickListener(v -> {
//            addToCart(selectedProductId);
//        });
    }
    private void fetchProductData(String orderId , String userEmail) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();



        DocumentReference productRef = db.collection("orders").document(orderId);
        productRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        selectedProduct = documentSnapshot.toObject(Order.class);


                        productNameTextView.setText(selectedProduct.getProductName());

                        priceTextView.setText(selectedProduct.getTotalPrice());
                        stateTextView.setText(selectedProduct.getState());
                        cityTextView.setText(selectedProduct.getCity());
                        paymentTypeTextView.setText(selectedProduct.getPaymentType());
                        orderStatusTextView.setText(selectedProduct.getOrderStatus());

                        if ("Completed".equals(selectedProduct.getOrderStatus())) {
                            TextView reviewTitle = findViewById(R.id.reviewTitle);
                            EditText reviewEditText = findViewById(R.id.reviewEditText);
                            Button addReviewButton = findViewById(R.id.addReviewButton);
                            reviewEditText.setVisibility(View.VISIBLE);
                            addReviewButton.setVisibility(View.VISIBLE);
                            reviewTitle.setVisibility(View.VISIBLE);
                            addReviewButton.setOnClickListener(v -> {

                                db.collection("users")
                                        .whereEqualTo("email", userEmail)
                                        .get()
                                        .addOnSuccessListener(querySnapshot -> {
                                            if (!querySnapshot.isEmpty()) {
                                                DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                                String username = document.getString("name");
                                                String review = reviewEditText.getText().toString().trim();
                                                String productId = selectedProduct.getProductId();

                                                Map<String, Object> reviewData = new HashMap<>();
                                                reviewData.put("username", username);
                                                reviewData.put("productId", productId);
                                                reviewData.put("userEmail", userEmail);
                                                reviewData.put("review", review);

                                                db.collection("reviews")
                                                        .add(reviewData)
                                                        .addOnSuccessListener(documentReference -> {

                                                            Toast.makeText(AddReviewActivity.this, "Review added successfully", Toast.LENGTH_SHORT).show();
                                                            reviewEditText.setText("");
                                                        })
                                                        .addOnFailureListener(e -> {
                                                        Toast.makeText(AddReviewActivity.this, "Failed to add Review", Toast.LENGTH_SHORT).show();
                                                        });
                                            } else {
                                                Toast.makeText(AddReviewActivity.this, "User not found", Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(AddReviewActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        });
                            });
                        }

                        Glide.with(this)
                                .load(selectedProduct.getImageUrl())
                                .into(productImageView);
                    }

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddReviewActivity.this, "Failed Fetch orders", Toast.LENGTH_SHORT).show();

                });
    }
}