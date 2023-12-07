package com.example.fooddeliveryapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.fooddeliveryapp.R;
import com.example.fooddeliveryapp.fragments.CartFragment;
import com.example.fooddeliveryapp.fragments.ProductFragment;
import com.example.fooddeliveryapp.fragments.ProfileCoustomer;
import com.example.fooddeliveryapp.fragments.YourOrdersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        String userEmail = getIntent().getStringExtra("userEmail");
        String restaurantId = getIntent().getStringExtra("Id");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationViewCustomer);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        Bundle bundle = new Bundle();
        bundle.putString("userEmail", userEmail);
        bundle.putString("Id", restaurantId);

        ProductFragment ProductFragment = new ProductFragment();
        ProductFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerCustomer, ProductFragment)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        switch (item.getItemId()) {
            case R.id.action_product:
                selectedFragment = new ProductFragment();
                break;
            case R.id.action_profile:
                selectedFragment = new ProfileCoustomer();
                break;
            case R.id.action_your_order:
                selectedFragment = new YourOrdersFragment();
                break;
            case R.id.action_cart:
                selectedFragment = new CartFragment();
                break;
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerCustomer, selectedFragment)
                    .commit();
            return true;
        }

        return false;

    }
}