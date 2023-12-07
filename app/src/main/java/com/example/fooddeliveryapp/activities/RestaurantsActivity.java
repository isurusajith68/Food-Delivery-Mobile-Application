package com.example.fooddeliveryapp.activities;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.fooddeliveryapp.R;
import com.example.fooddeliveryapp.fragments.AddProductFragment;
import com.example.fooddeliveryapp.fragments.ProductOrdersFragment;
import com.example.fooddeliveryapp.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RestaurantsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        String userEmail = getIntent().getStringExtra("userEmail");
        String restaurantId = getIntent().getStringExtra("Id");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        Bundle bundle = new Bundle();
        bundle.putString("userEmail", userEmail);
        bundle.putString("Id", restaurantId);

        AddProductFragment addProductFragment = new AddProductFragment();
        addProductFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, addProductFragment)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        switch (item.getItemId()) {
            case R.id.action_add_product:
                selectedFragment = new AddProductFragment();
                break;
            case R.id.action_profile:
                selectedFragment = new ProfileFragment();
                break;
            case R.id.action_product_orders:
                selectedFragment = new ProductOrdersFragment();
                break;
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, selectedFragment)
                    .commit();
            return true;
        }

        return false;
    }


}
