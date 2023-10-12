package edu.wkd.userappbanghangonline.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;


import edu.wkd.userappbanghangonline.databinding.ActivityDetailsOrderBinding;
import edu.wkd.userappbanghangonline.model.obj.Order;
import edu.wkd.userappbanghangonline.model.obj.Product;
import edu.wkd.userappbanghangonline.ultil.UserUltil;
import edu.wkd.userappbanghangonline.view.adapter.ProductInOrderAdapter;

public class DetailsOrderActivity extends AppCompatActivity {
    private ActivityDetailsOrderBinding binding;
    private ArrayList<Product> listProduct;
    private ProductInOrderAdapter productOrderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        onBack();//Quay trở lại sự kiện trước đó
        showData();//Hiển thị dữ liệu lên recycleView
    }

    private void showData() {
        Order order = (Order) getIntent().getExtras().getSerializable("infoOrder");
        //Lấy danh sách sản phẩm
        listProduct = (ArrayList<Product>) getIntent().getExtras().getSerializable("listProduct");
        int position = getIntent().getIntExtra("position",-1);
        //Hiển thị layout tương ứng với từng trạng thái giao hàng
        if (order.getStatus() == 0){
            binding.layoutExpectedDelivery.setVisibility(View.GONE);
            binding.btnDetailsOrder.setText("Hủy đơn hàng");
            binding.btnDetailsOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(DetailsOrderActivity.this)
                            .setTitle("Hủy đơn hàng")
                            .setIcon(android.R.drawable.ic_delete)
                            .setMessage("Bạn chắc chắn muốn hủy đơn hàng này?")
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    listProduct.remove(position);
                                    Toast.makeText(DetailsOrderActivity.this, "Hủy đơn hàng thành công.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    finish();
                                }
                            })
                            .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });
        }else if(order.getStatus() == 1){
            binding.layoutExpectedDelivery.setVisibility(View.VISIBLE);
            binding.btnDetailsOrder.setText("Tiếp tục mua hàng");
        }else if (order.getStatus() == 2){
            binding.layoutExpectedDelivery.setVisibility(View.GONE);
            binding.btnDetailsOrder.setText("Mua lại");
        }else{
            binding.layoutExpectedDelivery.setVisibility(View.GONE);
            binding.btnDetailsOrder.setText("Tiếp tục mua hàng");
        }
        //Hiển thị dữ liệu lên recycleView
        setLayoutInRecycleView(listProduct);
        //Set tên người dùng
        String userName = UserUltil.user.getUsername();
        if (userName == ""){
            binding.tvUsername.setText("Username");
        }else{
            binding.tvUsername.setText(userName);
        }
        binding.tvPhoneNumber.setText(order.getPhoneNumber());
        binding.tvAddress.setText(order.getAddress());
        binding.tvDateDetailOrder.setText(order.getDateTime());
        binding.tvQuantityProductDetailOrder.setText("Tổng tiền ("+listProduct.size()+" sản phẩm)");//Hiển thị số lượng sản phẩm

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        binding.tvAllPriceProductDetailOrder.setText(decimalFormat.format(order.getTotalPrice())+"đ");//Giá tất cả sản phẩm
        binding.tvTotalDetailOrder.setText(decimalFormat.format(order.getTotalPrice())+"đ");//Giá sau cùng khi tính thêm các chi phí khác


    }

    private void setLayoutInRecycleView(ArrayList<Product> listProduct) {
        productOrderAdapter = new ProductInOrderAdapter(listProduct);
        LinearLayoutManager manager = new LinearLayoutManager(DetailsOrderActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvProductInDetailOrder.setLayoutManager(manager);
        binding.rvProductInDetailOrder.setAdapter(productOrderAdapter);
    }

    private void onBack(){
        binding.arrowBackDetailOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}