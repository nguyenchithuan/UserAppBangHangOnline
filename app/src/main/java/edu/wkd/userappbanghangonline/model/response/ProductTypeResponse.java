package edu.wkd.userappbanghangonline.model.response;

import java.util.List;


import edu.wkd.userappbanghangonline.model.obj.ProductType;

public class ProductTypeResponse {
    private boolean success;
    private String message;
    private List<ProductType> result;

    public ProductTypeResponse() {
    }

    public ProductTypeResponse(boolean success, String message, List<ProductType> result) {
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

    public List<ProductType> getResult() {
        return result;
    }

    public void setResult(List<ProductType> result) {
        this.result = result;
    }
}