package com.example.fooddeliveryapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fooddeliveryapp.R;
import com.example.fooddeliveryapp.activities.SingleProductActivity;
import com.example.fooddeliveryapp.adapters.AllProductAdapter;
import com.example.fooddeliveryapp.models.AllProduct;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {

    private RecyclerView recyclerView;
    private AllProductAdapter allProductAdapter;
    private List<AllProduct> allProductList;
    private FirebaseFirestore db;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_product, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        allProductList = new ArrayList<>();
        allProductAdapter = new AllProductAdapter(allProductList);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(allProductAdapter);

        db = FirebaseFirestore.getInstance();

        db.collection("products")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allProductList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        AllProduct product = document.toObject(AllProduct.class);
                        product.setDocumentId(document.getId()); // Set the document ID
                        allProductList.add(product);
                    }

                    allProductAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Failed to retrieve  data.", Toast.LENGTH_SHORT).show();
                });
        allProductAdapter.setOnProductItemClickListener(productId -> {
            Intent intent = new Intent(getActivity(), SingleProductActivity.class);
            intent.putExtra("selectedProductId", productId);
            startActivity(intent);
        });

        return rootView;
    }
}