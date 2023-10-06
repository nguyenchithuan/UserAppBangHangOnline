package edu.wkd.userappbanghangonline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.DecimalFormat;

import edu.wkd.userappbanghangonline.adapter.CartAdapter;
import edu.wkd.userappbanghangonline.databinding.ActivityCartBinding;
import edu.wkd.userappbanghangonline.ultil.CartInterface;
import edu.wkd.userappbanghangonline.ultil.CartUtil;
import okhttp3.internal.Util;

public class CartActivity extends AppCompatActivity implements CartInterface {
    private ActivityCartBinding binding;
    private CartAdapter cartAdapter;
    private int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setDataCart();
        setTotalPrice();
        onBack();

        binding.btnPurchase.setOnClickListener(view -> {
            if(totalPrice == 0) {
                Toast.makeText(this, "Mời bạn chọn sản phẩm", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, PayActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("totalPrice", totalPrice);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }


    private void setDataCart() {
        try {
            CartUtil.listBuyCart.clear(); // Vào lại trang thì xóa toàn bộ data cũ để ng ta chọn lại
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            binding.rcvCart.setLayoutManager(linearLayoutManager);
            Log.d("zzzz", "CartUtil.listCart: " + CartUtil.listCart);
            cartAdapter = new CartAdapter((Context) this, CartUtil.listCart, this);
            binding.rcvCart.setHasFixedSize(true);
            binding.rcvCart.setAdapter(cartAdapter);
        } catch (Exception e) {
            Log.d("zzzz", "setDataCart: " + e.toString());
        }
    }

    @Override
    public void setTotalPrice() {
        totalPrice = 0;
        for (int i = 0; i < CartUtil.listBuyCart.size(); i++) {
            totalPrice += CartUtil.listBuyCart.get(i).getPrice() * CartUtil.listBuyCart.get(i).getQuantity();
        }
        DecimalFormat df = new DecimalFormat("###,###,###");
        binding.tvTotalPrice.setText(df.format(totalPrice) + " đ");
    }

    private void onBack() {
        binding.arrowBackDetailProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        onBackActivity();
    }

    private void onBackActivity() {
        // Đoạn này chưa hiểu lắm nhưng kể cả truyền cho màn hình nào thì tất cả
        // registerForActivityResult onBack đểu đc chạy
        Intent intent = new Intent(CartActivity.this, MainActivity.class);
        intent.putExtra("data_cart_size", CartUtil.listCart.size());
        setResult(RESULT_OK, intent);
        finish();
    }
}