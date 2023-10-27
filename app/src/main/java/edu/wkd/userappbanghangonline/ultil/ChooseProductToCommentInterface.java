package edu.wkd.userappbanghangonline.ultil;

import java.util.List;

import edu.wkd.userappbanghangonline.model.obj.Product;

public interface ChooseProductToCommentInterface {
    void getListProductToComment(List<Product> list, int orderId);
}
