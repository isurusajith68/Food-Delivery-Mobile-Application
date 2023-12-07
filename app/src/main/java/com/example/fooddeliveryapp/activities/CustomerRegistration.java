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

public class CustomerRegistration extends AppCompatActivity {

    private EditText nameEditText, addressEditText, cityEditText, emailEditText, passwordEditText, phoneEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);

        nameEditText = findViewById(R.id.editTextName);
        addressEditText = findViewById(R.id.editTextAddress);
        cityEditText = findViewById(R.id.editTextCity);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        phoneEditText = findViewById(R.id.editTextPhone);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String city = cityEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String phone = phoneEditText.getText().toString();

                registerUser(name, address, city, email, password, phone);
            }
        });
    }

    private void registerUser(String name, String address, String city, String email, String password, String phone) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (name.isEmpty() || address.isEmpty() || city.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            sendVerificationEmail(user);
                            storeUserDataInFirestore(db, user.getUid(), name,email, address, city, phone);
                        }
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(CustomerRegistration.this, "The email address is already in use by another account.", Toast.LENGTH_SHORT).show();
                        } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                            Toast.makeText(CustomerRegistration.this, "Password should be at least 6 characters.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CustomerRegistration.this, "Failed to create account. Please try again.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(CustomerRegistration.this, "Failed to send verification email. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void storeUserDataInFirestore(FirebaseFirestore db, String userId, String name,String email ,String address, String city, String phone) {

        DocumentReference userRef = db.collection("users").document(userId);

        Map<String, Object> userData = new HashMap<>();
        userData.put("email",email);
        userData.put("name", name);
        userData.put("address", address);
        userData.put("city", city);
        userData.put("phone", phone);
        userData.put("registrationType", "customer");

        userRef.set(userData)
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CustomerRegistration.this, "Failed to store user data. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }
}
