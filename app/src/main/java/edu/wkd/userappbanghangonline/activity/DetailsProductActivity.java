package edu.wkd.userappbanghangonline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.databinding.ActivityDetailsProductActivityBinding;
import edu.wkd.userappbanghangonline.model.obj.Cart;
import edu.wkd.userappbanghangonline.model.obj.Product;
import edu.wkd.userappbanghangonline.ultil.CartUtil;


public class DetailsProductActivity extends AppCompatActivity {
    private ActivityDetailsProductActivityBinding binding;
    private Product product;
    private int productQuantity = 1; // so luong san pham
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsProductActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        onBack();//Quay trở lại sự kiện trước đó

        setDataDetailsProduct();
        onclickBtnProductQuantity();
        onclickBtnCart();
    }

    private void setDataDetailsProduct() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        product = (Product) bundle.get("product");

        Glide.with(this)
                .load(product.getImage())
                .error(R.mipmap.ic_launcher)
                .into(binding.imgProduct);
        binding.tvNameProduct.setText(product.getName());
        binding.ratingBar.setRating(product.getRating());
        binding.tvQuantityRating.setText("(" + product.getQuantityRating() +  ")");
        binding.tvDescription.setText(product.getDescription());
        DecimalFormat df = new DecimalFormat("###,###,###");
        binding.tvPrice.setText(df.format(product.getPrice()) + " đ");
        binding.tvProductQuantity.setText(productQuantity + "");
    }

    private void onclickBtnProductQuantity() {
        binding.btnPlus.setOnClickListener(view -> {
            if(productQuantity < 10) {
                productQuantity += 1;
                binding.tvProductQuantity.setText(productQuantity + "");
            }
        });

        binding.btnMinus.setOnClickListener(view -> {
            if(productQuantity > 1) {
                productQuantity -= 1;
                binding.tvProductQuantity.setText(productQuantity + "");
            }
        });
    }

    private void onclickBtnCart() {
        binding.btnCart.setOnClickListener(view -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
            addListCart();
        });
    }

    private void addListCart() {
        Boolean check = false;
        for (int i = 0; i < CartUtil.listCart.size(); i++) {
            if(product.getId() == CartUtil.listCart.get(i).getIdProduct()) {
                productQuantity += CartUtil.listCart.get(i).getQuantity();
                if(productQuantity > 10) {
                    productQuantity = 10;
                }
                CartUtil.listCart.get(i).setQuantity(productQuantity);
                check = true;
            }
        }
        if(check != true) {
            Cart cart = new Cart();
            cart.setIdProduct(product.getId());
            cart.setName(product.getName());
            cart.setImage(product.getImage());
            cart.setPrice(product.getPrice());
            cart.setQuantity(productQuantity);
            CartUtil.listCart.add(cart);
        }
    }

    private void onBack() {
        binding.arrowBackDetailProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}