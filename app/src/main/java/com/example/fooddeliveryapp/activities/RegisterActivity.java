package com.example.fooddeliveryapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fooddeliveryapp.MainActivity;
import com.example.fooddeliveryapp.R;

public class RegisterActivity extends AppCompatActivity {

    private Button customerregistration;
    private Button restaurantsregistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        customerregistration= findViewById(R.id.customer);
        restaurantsregistration= findViewById(R.id.chef);
        customerregistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegisterActivity.this, CustomerRegistration.class);
                startActivity(intent);
            }
        });
        restaurantsregistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegisterActivity.this, RestudentRegistraction.class);
                startActivity(intent);
            }
        });
    }
}