package edu.wkd.userappbanghangonline.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.databinding.LayoutItemChooseProductToReviewsBinding;
import edu.wkd.userappbanghangonline.databinding.LayoutItemProductInReviewsBinding;
import edu.wkd.userappbanghangonline.model.obj.Product;
import edu.wkd.userappbanghangonline.ultil.OnCheckedInChooseProductInterface;

public class ProductInReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Product> list;
    public static final int LAYOUT_CHOOSE_PRODUCT = 1;
    private int mCurrentLayout = LAYOUT_CHOOSE_PRODUCT;
    private OnCheckedInChooseProductInterface chooseProductInterface;
    public void setChooseProductInterface(OnCheckedInChooseProductInterface chooseProductInterface){
        this.chooseProductInterface = chooseProductInterface;
    }

    private HashMap<Integer, Boolean> checkedMap = new HashMap<>();
    private boolean isAllChecked  = false;

    public void setAllItemsChecked(boolean isChecked) {
        isAllChecked  = isChecked;
        for (int i = 0; i < list.size(); i++) {
            checkedMap.put(i, isChecked);
        }
        notifyDataSetChanged();
    }

    public ProductInReviewsAdapter(ArrayList<Product> list, int mCurrentLayout) {
        this.list = list;
        this.mCurrentLayout = mCurrentLayout;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mCurrentLayout == LAYOUT_CHOOSE_PRODUCT){
            LayoutItemChooseProductToReviewsBinding binding = LayoutItemChooseProductToReviewsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new ViewHolderChooseProduct(binding);
        }else{
            LayoutItemProductInReviewsBinding binding = LayoutItemProductInReviewsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new ViewHolderProductInReviews(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Product product = list.get(position);
        if (product == null){
            return;
        }
        //Hiển thị layout chọn sản phẩm đánh giá nếu có từ 2 sản phẩm trở lên
        if (mCurrentLayout == LAYOUT_CHOOSE_PRODUCT){
            ViewHolderChooseProduct holderChooseProduct = (ViewHolderChooseProduct) holder;
            holderChooseProduct.binding.tvName.setText(product.getName());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            holderChooseProduct.binding.tvPrice.setText(decimalFormat.format(product.getPrice())+"đ");
            if(product.getImage().contains("uploads")) {
                Glide.with(holder.itemView.getContext())
                        .load("https://guyinterns2003.000webhostapp.com/" + product.getImage())
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.error)
                        .into(holderChooseProduct.binding.imgProduct);
            } else {
                Glide.with(holderChooseProduct.itemView.getContext())
                        .load(product.getImage())
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.error)
                        .into(holderChooseProduct.binding.imgProduct);
            }

            Boolean isChecked = checkedMap.get(position);
            if (isChecked != null && isChecked) {
                holderChooseProduct.binding.chkPurchase.setChecked(true);
            } else {
                holderChooseProduct.binding.chkPurchase.setChecked(false);
            }
            if (isAllChecked) {
                holderChooseProduct.binding.chkPurchase.setEnabled(false);
            } else {
                holderChooseProduct.binding.chkPurchase.setEnabled(true);
            }
            holderChooseProduct.binding.chkPurchase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (chooseProductInterface != null){
                        if (isChecked){
                            chooseProductInterface.addProductSelected(product);
                        }else{
                            chooseProductInterface.removeProduct(product);
                        }
                    }
                }
            });

        }else{ //Hiển thị sản phẩm bên màn hình đánh giá
            ViewHolderProductInReviews holderProductInReviews = (ViewHolderProductInReviews) holder;
            holderProductInReviews.binding.tvNameProduct.setText(product.getName());
            if(product.getImage().contains("uploads")) {
                Glide.with(holder.itemView.getContext())
                        .load("https://guyinterns2003.000webhostapp.com/" + product.getImage())
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.error)
                        .into(holderProductInReviews.binding.imgProductReviews);
            } else {
                Glide.with(holderProductInReviews.itemView.getContext())
                        .load(product.getImage())
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.error)
                        .into(holderProductInReviews.binding.imgProductReviews);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    //Hiển thị layout khi đánh giá đơn hàng có từ 2 sản phẩm trở lên
    public class ViewHolderChooseProduct extends RecyclerView.ViewHolder {
        private LayoutItemChooseProductToReviewsBinding binding;
        public ViewHolderChooseProduct(@NonNull LayoutItemChooseProductToReviewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    //Hiển thị sản phẩm được đánh giá bên màn hình reviews
    public class ViewHolderProductInReviews extends RecyclerView.ViewHolder {
        private LayoutItemProductInReviewsBinding binding;
        public ViewHolderProductInReviews(@NonNull LayoutItemProductInReviewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
