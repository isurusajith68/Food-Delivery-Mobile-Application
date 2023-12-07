package com.example.fooddeliveryapp.fragments;

import android.content.Context;
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
import com.example.fooddeliveryapp.adapters.CartAdapter;
import com.example.fooddeliveryapp.models.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView cartRecyclerView;
    private List<CartItem> cartItemList;
    private CartAdapter cartAdapter;
    private FirebaseAuth mAuth;
    private String userUid;
    private FirebaseFirestore db;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        cartRecyclerView = rootView.findViewById(R.id.cartRecyclerView);
        cartItemList = new ArrayList<>();
        cartAdapter = new CartAdapter(cartItemList , requireContext());
        db = FirebaseFirestore.getInstance();

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        cartRecyclerView.setAdapter(cartAdapter);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs",  Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", "");


        fetchCartItems(userEmail);

        return rootView;
    }
    private void fetchCartItems(String userEmail) {

        db.collection("cart").whereEqualTo("userEmail", userEmail)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    cartItemList.clear();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        CartItem cartItem = document.toObject(CartItem.class);
                        String productId = document.getId();
                        cartItem.setDocumentId(productId);
                        cartItemList.add(cartItem);
                    }
                    cartAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Failed to retrieve data.", Toast.LENGTH_SHORT).show();
                });
    }

}