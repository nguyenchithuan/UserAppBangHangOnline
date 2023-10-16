package edu.wkd.userappbanghangonline.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import edu.wkd.userappbanghangonline.data.api.ApiService;
import edu.wkd.userappbanghangonline.databinding.ActivityOrderBinding;

import edu.wkd.userappbanghangonline.model.obj.Order;
import edu.wkd.userappbanghangonline.model.response.OrderResponse;

import edu.wkd.userappbanghangonline.ultil.OrderInterface;
import edu.wkd.userappbanghangonline.ultil.UserUltil;
import edu.wkd.userappbanghangonline.view.adapter.ViewPager2Adapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderActivity extends AppCompatActivity{
    public static final String TAG = OrderActivity.class.toString();
    private ActivityOrderBinding binding;
    private ViewPager2Adapter viewPager2Adapter;
    public  ArrayList<Order> listOrder;
    private OrderInterface orderInterface;
    public void setOrderInterface(OrderInterface orderInterface){
        this.orderInterface = orderInterface;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onBack();//Quay trở lại sự kiện trước đó
        setTabLayoutAndViewPager2();
    }



    private void setTabLayoutAndViewPager2() {
        viewPager2Adapter = new ViewPager2Adapter(OrderActivity.this);
        binding.viewPager2.setAdapter(viewPager2Adapter);

        TabLayoutMediator mediator = new TabLayoutMediator(binding.tabLayout, binding.viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Chờ xác nhận");
                        break;
                    case 1:
                        tab.setText("Đang giao hàng");
                        break;
                    case 2:
                        tab.setText("Đã giao");
                        break;
                    case 3:
                        tab.setText("Đã hủy");
                        break;
                }
            }
        });
        mediator.attach();
    }

    private void onBack(){
        binding.arrowBackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void getOrderByStatus(int status) {
        int idUser = UserUltil.user.getId();
        ApiService.apiService.getOrderByIdUserAndStatus(idUser, status).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()){
                    listOrder = response.body().getListOrder();
                    if (orderInterface != null){
                        orderInterface.dataOrderReceiver(listOrder);
                    }
                }
            }
            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "Lỗi server (chi tiết trong logcat)", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: " + t);
            }
        });
    }

}