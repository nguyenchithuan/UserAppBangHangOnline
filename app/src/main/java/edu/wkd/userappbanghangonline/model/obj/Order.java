package edu.wkd.userappbanghangonline.model.obj;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("address")
    private String address;
    @SerializedName("phone_number")
    private String phoneNumber;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("total_price")
    private int totalPrice;
    @SerializedName("status")
    private int status;
    @SerializedName("datetime")
    private String dateTime;
    @SerializedName("item")
    private ArrayList<Product> listProduct;

    @SerializedName("is_rating")
    private int isRating;

    public Order(int userId, String address, String phoneNumber, int quantity, int totalPrice, int status, String dateTime, ArrayList<Product> listProduct, int isRating) {
        this.userId = userId;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
        this.dateTime = dateTime;
        this.listProduct = listProduct;
        this.isRating = isRating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ArrayList<Product> getListProduct() {
        return listProduct;
    }

    public void setListProduct(ArrayList<Product> listProduct) {
        this.listProduct = listProduct;
    }

    public int getIsRating() {
        return isRating;
    }

    public void setIsRating(int isRating) {
        this.isRating = isRating;
    }
}
