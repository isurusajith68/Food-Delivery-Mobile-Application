package com.example.fooddeliveryapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fooddeliveryapp.R;
import com.example.fooddeliveryapp.activities.AddReviewActivity;
import com.example.fooddeliveryapp.activities.SingleProductActivity;
import com.example.fooddeliveryapp.adapters.CartAdapter;
import com.example.fooddeliveryapp.adapters.OrderAdapter;
import com.example.fooddeliveryapp.models.CartItem;
import com.example.fooddeliveryapp.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YourOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YourOrdersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView orderRecyclerView;
    private List<Order> orderItemList;
    private OrderAdapter orderAdapter;
    private FirebaseAuth mAuth;
    private String userUid;
    private FirebaseFirestore db;
    public YourOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YourOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static YourOrdersFragment newInstance(String param1, String param2) {
        YourOrdersFragment fragment = new YourOrdersFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_your_orders, container, false);

        orderRecyclerView = rootView.findViewById(R.id.orderRecyclerView);
        orderItemList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderItemList , requireContext());
        db = FirebaseFirestore.getInstance();

        orderRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        orderRecyclerView.setAdapter(orderAdapter);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs",  Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", "");

        orderAdapter.setOnOrderItemClickListener(orderId -> {
            Intent intent = new Intent(getActivity(), AddReviewActivity.class);
            intent.putExtra("selectedOrderId", orderId);
            startActivity(intent);
        });
        fetchOrderItems(userEmail);

        return rootView;
    }
    private void fetchOrderItems(String userEmail) {

        db.collection("orders").whereEqualTo("userEmail", userEmail)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    orderItemList.clear();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Order orderItem = document.toObject(Order.class);
                        orderItem.setDocumentId(document.getId()); // Set the document ID
                        orderItemList.add(orderItem);
                    }
                    orderAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Failed to retrieve  data.", Toast.LENGTH_SHORT).show();
                });


    }


}


