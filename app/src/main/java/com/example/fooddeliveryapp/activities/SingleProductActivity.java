package com.example.fooddeliveryapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapp.R;
import com.example.fooddeliveryapp.adapters.ReviewAdapter;
import com.example.fooddeliveryapp.models.AllProduct;
import com.example.fooddeliveryapp.models.CartItem;
import com.example.fooddeliveryapp.models.ProductReview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SingleProductActivity extends AppCompatActivity {
    private ImageView productImageView;
    private TextView productNameTextView;
    private TextView restaurantNameTextView;
    private TextView productPriceTextView;
    private TextView quantityTextView;
    private TextView totalPriceTextView;
    private TextView description;
    private Button increaseQuantityButton;
    private Button decreaseQuantityButton;
    private FirebaseFirestore db;
    private String selectedProductId;
    private String userUid;
    private AllProduct selectedProduct;
    private int currentQuantity = 1;
    private FirebaseAuth mAuth;
    private List<ProductReview> reviewList;
    private RecyclerView recyclerView;
    private ReviewAdapter reviewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);

        reviewList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewReview);
        reviewAdapter = new ReviewAdapter(reviewList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(reviewAdapter);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        selectedProductId = getIntent().getStringExtra("selectedProductId");
        fetchProductData(selectedProductId);
        fetchProductReviews(selectedProductId);
        userUid = mAuth.getCurrentUser().getUid();
        Log.d("YourTag", "User UID: " + userUid);

        productImageView = findViewById(R.id.productImageView);
        productNameTextView = findViewById(R.id.productNameTextView);
        restaurantNameTextView = findViewById(R.id.restaurantNameTextView);
        productPriceTextView = findViewById(R.id.productPriceTextView);
        quantityTextView = findViewById(R.id.quantityTextView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        increaseQuantityButton = findViewById(R.id.increaseQuantityButton);
        decreaseQuantityButton = findViewById(R.id.decreaseQuantityButton);
        description = findViewById(R.id.Description);

        Button addToCartButton = findViewById(R.id.addToCartButton);
        addToCartButton.setOnClickListener(v -> {
            addToCart(selectedProductId);
        });
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            selectedProduct = extras.getParcelable("selectedProduct");
//        }
//            productNameTextView.setText(selectedProduct.getProductName());
//            restaurantNameTextView.setText(selectedProduct.getRestaurantName());
//            productPriceTextView.setText(selectedProduct.getProductPrice());
//            quantityTextView.setText(String.valueOf(currentQuantity));
//
//
//            increaseQuantityButton.setOnClickListener(v -> {
//                currentQuantity++;
//                updateTotalPrice();
//            });
//
//            decreaseQuantityButton.setOnClickListener(v -> {
//                if (currentQuantity > 1) {
//                    currentQuantity--;
//                    updateTotalPrice();
//                }
//            });
//
//            // Load product image using Glide or another image loading library
//            Glide.with(this)
//                    .load(selectedProduct.getImageUrl())
//                    .into(productImageView);
//
//
//
//
//    }
    }

        private void fetchProductReviews(String productId) {
            db.collection("reviews")
                    .whereEqualTo("productId", productId)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        reviewList.clear(); // Clear existing reviews
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String username = document.getString("username");
                            String reviewText = document.getString("review");
                            String userEmail = document.getString("userEmail");
                            ProductReview productReview = new ProductReview(username, reviewText,userEmail);
                            productReview.setDocumentId(document.getId());
                            reviewList.add(productReview);
                        }
                        TextView reviewsTextView = findViewById(R.id.reviewsTextView);
                        if (reviewList.size() >= 1) {
                            reviewsTextView.setText("Reviews");
                        } else {
                            reviewsTextView.setText("No Product Reviews");
                        }
                        reviewAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FetchReviews", "Error fetching reviews", e);
                        TextView reviewsTextView = findViewById(R.id.reviewsTextView);
                        reviewsTextView.setText("No Product Reviews");
                    });


    }
    private void fetchProductData(String productId) {
        DocumentReference productRef = db.collection("products").document(productId);
        productRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        selectedProduct = documentSnapshot.toObject(AllProduct.class);


                        productNameTextView.setText(selectedProduct.getProductName());
                        restaurantNameTextView.setText(selectedProduct.getRestaurantName());
                        productPriceTextView.setText(selectedProduct.getProductPrice());
                        quantityTextView.setText(String.valueOf(currentQuantity));
                        totalPriceTextView.setText(selectedProduct.getProductPrice());
                        description.setText(selectedProduct.getProductDescription());
                        increaseQuantityButton.setOnClickListener(v -> {
                            currentQuantity++;
                            updateTotalPrice();
                        });

                        decreaseQuantityButton.setOnClickListener(v -> {
                            if (currentQuantity > 1) {
                                currentQuantity--;
                                updateTotalPrice();
                            }
                        });




                        Glide.with(this)
                                .load(selectedProduct.getImageUrl())
                                .into(productImageView);
                    }

                })
                .addOnFailureListener(e -> {

                });
    }
    private void addToCart(String selectedProductId) {
        CartItem cartItem = new CartItem();
        cartItem.setProductId(selectedProductId);
        cartItem.setProductName(selectedProduct.getProductName());
        cartItem.setImageUrl(selectedProduct.getImageUrl());
        cartItem.setProductPrice(Double.parseDouble(selectedProduct.getProductPrice()));
        cartItem.setQuantity(currentQuantity);
        cartItem.setRestaurantEmail(selectedProduct.getRestaurantEmail());
        cartItem.setUserEmail(mAuth.getCurrentUser().getEmail());
       cartItem.setTotalPrice((String) totalPriceTextView.getText());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("cart")
                .add(cartItem)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                });
    }


    private void updateTotalPrice() {
        double total = currentQuantity * Double.parseDouble(selectedProduct.getProductPrice());
        totalPriceTextView.setText(String.format(Locale.getDefault(), "%.2f", total));
        quantityTextView.setText(String.valueOf(currentQuantity));
    }
}