package com.example.fooddeliveryapp.models;

public class Order {
    private String documentId;
    private String imageUrl;
    private String productName;
    private String totalPrice;
    private int quantity;
    private String userEmail;
    private String restaurantEmail;
    private String state;
    private String city;
    private String province;
    private String paymentType;
    private String orderStatus;
    private String productId;
    public Order (){

    }

    public Order(String imageUrl, String productName, String totalPrice, int quantity,
                 String userEmail, String restaurantEmail, String state, String city, String province, String paymentType, String orderStatus,String productId) {
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.userEmail = userEmail;
        this.restaurantEmail = restaurantEmail;
        this.state = state;
        this.city = city;
        this.paymentType = paymentType;
        this.province = province;
        this.orderStatus = orderStatus;
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }


    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getRestaurantEmail() {
        return restaurantEmail;
    }

    public void setRestaurantEmail(String restaurantEmail) {
        this.restaurantEmail = restaurantEmail;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
