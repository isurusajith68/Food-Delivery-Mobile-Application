package com.example.fooddeliveryapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapp.R;
import com.example.fooddeliveryapp.models.CartItem;
import com.example.fooddeliveryapp.models.Order;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.admin.v1.Index;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CashOnDeliveryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CashOnDeliveryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageView cartItemImage;
    private TextView cartItemName;
    private TextView cartItemTotalPrice;
    private EditText stateEditText;
    private EditText cityEditText;
    private EditText provinceEditText;
    private Button placeOrderButton;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CashOnDeliveryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CashOnDeliveryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CashOnDeliveryFragment newInstance(String param1, String param2) {
        CashOnDeliveryFragment fragment = new CashOnDeliveryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cash_on_delivery, container, false);
        cartItemImage = rootView.findViewById(R.id.cartItemImage);
        cartItemName = rootView.findViewById(R.id.cartItemName);
        cartItemTotalPrice = rootView.findViewById(R.id.cartItemTotalPrice);
        stateEditText = rootView.findViewById(R.id.stateEditText);
        cityEditText = rootView.findViewById(R.id.cityEditText);
        provinceEditText = rootView.findViewById(R.id.provinceEditText);
        placeOrderButton = rootView.findViewById(R.id.placeOrderButton);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String cartDocumentId = sharedPreferences.getString("selectedCartItemDocumentId", "");

        Bundle bundle = getArguments();
        if (bundle == null) {

            fetchCartDetails(cartDocumentId);
        }

        placeOrderButton.setOnClickListener(v -> {
            String state = stateEditText.getText().toString();
            String city = cityEditText.getText().toString();
            String province = provinceEditText.getText().toString();

            if (state.trim().isEmpty()) {
                stateEditText.setError("State is required");
                stateEditText.requestFocus();
                return;
            }

            if (city.trim().isEmpty()) {
                cityEditText.setError("City is required");
                cityEditText.requestFocus();
                return;
            }

            if (province.trim().isEmpty()) {
                provinceEditText.setError("Province is required");
                provinceEditText.requestFocus();
                return;
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference cartRef = db.collection("cart").document(cartDocumentId);
            cartRef.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            CartItem cart = documentSnapshot.toObject(CartItem.class);
                            String imageUrl = cart.getImageUrl();
                            String productName = cart.getProductName();
                            String totalPrice = cart.getTotalPrice();
                            String productId = cart.getProductId();
                            int quantity = cart.getQuantity();
                            String userEmail = cart.getUserEmail();
                            String restaurantEmail = cart.getRestaurantEmail();
                            String paymentType = "Cash On Delivery";
                            String orderStatus = "Pending";

                          Order order = new Order(imageUrl, productName, totalPrice, quantity, userEmail, restaurantEmail, state, city, province ,paymentType,orderStatus,productId);

                            db.collection("orders")
                                    .add(order)
                                    .addOnSuccessListener(documentReference -> {
//                                        Fragment newFragment = new YourOrdersFragment();
//                                        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
//                                        fragmentTransaction.replace(R.id.fragmentContainerCustomer, newFragment);
//                                        fragmentTransaction.commit();
                                        Toast.makeText(requireContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {

                                        Toast.makeText(requireContext(), "Failed to place order.", Toast.LENGTH_SHORT).show();
                                    });
                        }


                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Failed.", Toast.LENGTH_SHORT).show();
                    });


        });
        return rootView;
    }
    private void fetchCartDetails(String cartDocumentId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference cartRef = db.collection("cart").document(cartDocumentId);
        cartRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        CartItem cart = documentSnapshot.toObject(CartItem.class);
                        String imageUrl = cart.getImageUrl();
                        String productName = cart.getProductName();
                        String totalPrice = cart.getTotalPrice();
                        int quantity = cart.getQuantity();
                        String userEmail = cart.getUserEmail();
                        String restaurantEmail = cart.getRestaurantEmail();


                        ImageView productImageView = requireView().findViewById(R.id.cartItemImage);
                        TextView productNameTextView = requireView().findViewById(R.id.cartItemName);
                        TextView totalPriceTextView = requireView().findViewById(R.id.cartItemTotalPrice);

                        Glide.with(requireContext())
                                .load(imageUrl)
                                .into(productImageView);
                        productNameTextView.setText(productName);
                        totalPriceTextView.setText( totalPrice);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Failed.", Toast.LENGTH_SHORT).show();
                });

    }
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        placeOrderButton.setOnClickListener(v -> {
//            String state = stateEditText.getText().toString();
//            String city = cityEditText.getText().toString();
//            String province = provinceEditText.getText().toString();
//
//
//            Order order = new Order(imageUrl, productName, totalPrice, quantity, userEmail, restaurantEmail, state, city, province);
//
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            db.collection("orders")
//                    .add(order)
//                    .addOnSuccessListener(documentReference -> {
//                        // Order added successfully
//                        Toast.makeText(requireContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
//                    })
//                    .addOnFailureListener(e -> {
//                        // Failed to add order
//                        Toast.makeText(requireContext(), "Failed to place order.", Toast.LENGTH_SHORT).show();
//                    });
//        });
//    }

}