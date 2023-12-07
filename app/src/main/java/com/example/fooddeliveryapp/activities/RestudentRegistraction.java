package com.example.fooddeliveryapp.activities;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fooddeliveryapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class RestudentRegistraction extends AppCompatActivity {

    private EditText nameEditText, cityEditText, emailEditText, passwordEditText, phoneEditText;
    private Button registerButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_registration);

        mAuth = FirebaseAuth.getInstance();

        nameEditText = findViewById(R.id.editTextRestaurantName);
        cityEditText = findViewById(R.id.editTextRestaurantCity);
        emailEditText = findViewById(R.id.editTextRestaurantEmail);
        passwordEditText = findViewById(R.id.editTextRestaurantPassword);
        phoneEditText = findViewById(R.id.editTextRestaurantPhone);

        registerButton = findViewById(R.id.registerRestaurantButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String city = cityEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String phone = phoneEditText.getText().toString();

                registerRestaurant(name, city, email, password, phone);
            }
        });
    }

    private void registerRestaurant(String name, String city, String email, String password, String phone) {
        if (name.isEmpty() || city.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            sendVerificationEmail(user);
                            storeRestaurantDataInFirestore(user.getUid(), name, city, email, phone);
                        }
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(RestudentRegistraction.this, "The email address is already in use by another account.", Toast.LENGTH_SHORT).show();
                        } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                            Toast.makeText(RestudentRegistraction.this, "Password should be at least 6 characters.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RestudentRegistraction.this, "Failed to create restaurant account. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendVerificationEmail(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent verificationIntent = new Intent(this, VerificationActivity.class);
                        startActivity(verificationIntent);
                        finish();
                    } else {
                        Toast.makeText(RestudentRegistraction.this, "Failed to send verification email. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void storeRestaurantDataInFirestore(String userId, String name, String city, String email, String phone) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference restaurantRef = db.collection("users").document(userId); // Store in "users" collection

        Map<String, Object> restaurantData = new HashMap<>();
        restaurantData.put("name", name);
        restaurantData.put("city", city);
        restaurantData.put("email", email);
        restaurantData.put("phone", phone);
        restaurantData.put("registrationType", "restaurant");

        restaurantRef.set(restaurantData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RestudentRegistraction.this, "Restaurant registration successful", Toast.LENGTH_SHORT).show();

                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RestudentRegistraction.this, "Failed to store restaurant data. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }

}
