package edu.wkd.userappbanghangonline.model.obj;

import com.google.gson.annotations.SerializedName;

public class Comment {
    private int id;
    @SerializedName("product_id")
    private int productId;
    @SerializedName("user_id")
    private int userId;
    private String note;
    private String image;
    @SerializedName("datetime")
    private String dateTime;

    private String username;
    private String avatar;

    public Comment(int productId, int userId, String note, String image, String dateTime) {
        this.productId = productId;
        this.userId = userId;
        this.note = note;
        this.image = image;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
