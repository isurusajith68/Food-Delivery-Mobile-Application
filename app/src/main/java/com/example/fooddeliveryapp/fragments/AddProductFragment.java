package com.example.fooddeliveryapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fooddeliveryapp.R;
import com.example.fooddeliveryapp.activities.RegisterActivity;
import com.example.fooddeliveryapp.activities.RestudentRegistraction;
import com.example.fooddeliveryapp.activities.ViewProductActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class AddProductFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGE_REQUEST =1 ;

    private EditText productNameEditText;
    private EditText productDescriptionEditText;
    private EditText productPriceEditText;
    private String mParam1;
    private String mParam2;
    private Uri imageUri;
    private String userEmail;
    private String userId;
    private String userName;
    private ImageView productImageView;
    private Button viewproduct;
    public AddProductFragment() {
        // Required empty public constructor
    }

    public static AddProductFragment newInstance(String param1, String param2) {
        AddProductFragment fragment = new AddProductFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs",  Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", "");


        productNameEditText = view.findViewById(R.id.productNameEditText);
        productDescriptionEditText = view.findViewById(R.id.descriptionEditText);
        productPriceEditText = view.findViewById(R.id.priceEditText);
        productImageView = view.findViewById(R.id.productImageView);
        viewproduct = view.findViewById(R.id.viewproduct);

        viewproduct.setOnClickListener(v -> {

            Intent intent = new Intent(requireContext(), ViewProductActivity.class);
            startActivity(intent);
        });

        Button uploadImageButton = view.findViewById(R.id.uploadImageButton);
        uploadImageButton.setOnClickListener(v -> openImagePicker());

        Button saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveProductData(db ,  userEmail ,imageUri ));



        return view;


    }



    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            productImageView.setImageURI(imageUri);
        }
    }


    private void saveProductData(FirebaseFirestore db, String userEmail, Uri imageUri) {
        String productName = productNameEditText.getText().toString();
        String productDescription = productDescriptionEditText.getText().toString();
        String productPrice = productPriceEditText.getText().toString();

        if (imageUri == null || productName.isEmpty() || productDescription.isEmpty() || productPrice.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields and select an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("product_images/" + System.currentTimeMillis() + ".jpg");

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {

                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        db.collection("users")
                                .whereEqualTo("email", userEmail)
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        DocumentSnapshot userDocument = queryDocumentSnapshots.getDocuments().get(0);

                                        String userId = userDocument.getId();
                                        String userName = userDocument.getString("name");

                                        Map<String, Object> productData = new HashMap<>();
                                        productData.put("productName", productName);
                                        productData.put("productDescription", productDescription);
                                        productData.put("productPrice", productPrice);
                                        productData.put("imageUrl", imageUrl);  // Add the image URL here
                                        productData.put("restaurantEmail", userEmail);
                                        productData.put("restaurantId", userId);
                                        productData.put("restaurantName", userName);

                                        db.collection("products")
                                                .add(productData)
                                                .addOnSuccessListener(documentReference -> {
                                                    Toast.makeText(requireContext(), "Product added successfully.", Toast.LENGTH_SHORT).show();
                                                    clearFields();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(requireContext(), "Failed to add product.", Toast.LENGTH_SHORT).show();
                                                });
                                    } else {
                                        Toast.makeText(requireContext(), "User not found.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(requireContext(), "Failed to retrieve user data.", Toast.LENGTH_SHORT).show();
                                });
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Failed to upload image.", Toast.LENGTH_SHORT).show();
                });
    }

    private void clearFields() {
        productNameEditText.getText().clear();
        productDescriptionEditText.getText().clear();
        productPriceEditText.getText().clear();
        productImageView.setImageDrawable(null);
        imageUri = null;
    }
}