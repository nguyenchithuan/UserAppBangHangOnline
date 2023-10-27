package edu.wkd.userappbanghangonline.model.response;

import java.util.List;

import edu.wkd.userappbanghangonline.model.obj.Comment;
import edu.wkd.userappbanghangonline.model.obj.Product;

public class CommentResponse {
    private boolean success;
    private String message;
    private List<Comment> result;

    public CommentResponse(boolean success, String message, List<Comment> result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public CommentResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Comment> getResult() {
        return result;
    }

    public void setResult(List<Comment> result) {
        this.result = result;
    }
}
