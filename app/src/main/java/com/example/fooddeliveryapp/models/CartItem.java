package com.example.fooddeliveryapp.models;
public class CartItem {
    private String documentId;
    private String productId;
    private String productName;
    private double productPrice;
    private int quantity;
    private String restaurantEmail;
    private String userEmail;
    private String totalPrice;
    private String imageUrl;

    public CartItem(String documentId, String productId, String productName, double productPrice, int quantity, String restaurantEmail, String userEmail, String totalPrice, String imageUrl) {
        this.documentId = documentId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.restaurantEmail = restaurantEmail;
        this.userEmail = userEmail;
        this.totalPrice = totalPrice;
        this.imageUrl = imageUrl;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public CartItem() {

    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRestaurantEmail() {
        return restaurantEmail;
    }

    public void setRestaurantEmail(String restaurantEmail) {
        this.restaurantEmail = restaurantEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
// Constructors, getters, and setters
public void calculateTotalPrice() {
    totalPrice = String.valueOf(quantity * productPrice);
}
}
