package edu.wkd.userappbanghangonline.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import edu.wkd.userappbanghangonline.model.obj.Comment;


public class CommentResponse {
    private boolean success;
    private String message;
    @SerializedName("result")
    private ArrayList<Comment> listComment;

    @Override
    public String toString() {
        return "OrderResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", listOrder=" + listComment +
                '}';
    }

    public ArrayList<Comment> getListComment() {
        return listComment;
    }

    public void setListComment(ArrayList<Comment> listComment) {
        this.listComment = listComment;
    }
}
