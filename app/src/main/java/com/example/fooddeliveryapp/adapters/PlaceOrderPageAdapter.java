package com.example.fooddeliveryapp.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fooddeliveryapp.R;
import com.example.fooddeliveryapp.fragments.CashOnDeliveryFragment;
import com.example.fooddeliveryapp.fragments.OnlinePaymentFragment;

public class PlaceOrderPageAdapter extends FragmentStateAdapter {

    private static final int NUM_TABS = 2;
    private final Context context;

    public PlaceOrderPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        context = fragmentActivity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new CashOnDeliveryFragment();
        } else {
            return new OnlinePaymentFragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_TABS;
    }

    public CharSequence getTabTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.cash_on_delivery_tab_title);
        } else {
            return context.getString(R.string.online_payment_tab_title);
        }
    }
}
