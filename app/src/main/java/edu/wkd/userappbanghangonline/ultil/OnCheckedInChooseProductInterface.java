package edu.wkd.userappbanghangonline.ultil;

import android.widget.CheckBox;

import edu.wkd.userappbanghangonline.model.obj.Product;

public interface OnCheckedInChooseProductInterface {
    void addProductSelected(Product product);
    void removeProduct(Product product);

}
