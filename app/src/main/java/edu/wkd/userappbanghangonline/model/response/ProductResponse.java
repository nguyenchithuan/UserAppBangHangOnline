package edu.wkd.userappbanghangonline.model.response;

import java.util.List;

import edu.wkd.userappbanghangonline.model.obj.Product;

public class ProductResponse {
    private boolean success;
    private String message;
    private List<Product> result;

    public ProductResponse() {
    }

    public ProductResponse(boolean success, String message, List<Product> result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    @Override
    public String toString() {
        return "ProductResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
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

    public List<Product> getResult() {
        return result;
    }

    public void setResult(List<Product> result) {
        this.result = result;
    }
}
