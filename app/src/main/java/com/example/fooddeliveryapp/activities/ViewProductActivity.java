package com.example.fooddeliveryapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.fooddeliveryapp.R;
import com.example.fooddeliveryapp.adapters.ProductAdapter;
import com.example.fooddeliveryapp.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ViewProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userUid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();



        recyclerView = findViewById(R.id.recyclerView);
        productList = new ArrayList<>();

        productAdapter = new ProductAdapter(productList ,this);
        recyclerView.setAdapter(productAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", "");



        db.collection("products")
                .whereEqualTo("restaurantEmail", userEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Product product = document.toObject(Product.class);
                        String productId = document.getId();
                        product.setDocumentId(productId);
                        productList.add(product);
                    }

                    productAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ViewProductActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                });
    }

}