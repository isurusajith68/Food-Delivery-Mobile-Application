package com.example.fooddeliveryapp.models;

public class Product {
    private String documentId;
    private String productName;
    private String productDescription;
    private String  productPrice;
    private String imageUrl;
    private String userEmail;
    private String restaurantId;
    private String restaurantName;

    public Product() {

    }
    public double getProductPriceAsDouble() {
        try {
            return Double.parseDouble(productPrice);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public Product(String documentId,String productName, String productDescription, String productPrice, String imageUrl, String userEmail, String restaurantId, String restaurantName) {
       this.documentId=documentId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.imageUrl = imageUrl;
        this.userEmail = userEmail;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
    }
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }




}
