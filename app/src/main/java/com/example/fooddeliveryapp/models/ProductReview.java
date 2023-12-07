package com.example.fooddeliveryapp.models;
public class ProductReview {
    private String documentId;
    private String username;
    private String review;
    private String userEmail;

    public ProductReview(String username, String review, String userEmail) {
        this.username = username;
        this.review = review;
        this.userEmail = userEmail;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }


}
