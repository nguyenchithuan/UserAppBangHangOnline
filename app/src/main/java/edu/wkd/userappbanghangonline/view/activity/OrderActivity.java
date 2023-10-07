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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import edu.wkd.userappbanghangonline.view.adapter.ViewPager2Adapter;


public class OrderActivity extends AppCompatActivity {
    private ActivityOrderBinding binding;
    public static final String TAG = OrderActivity.class.toString();
    private ViewPager2Adapter viewPager2Adapter;
    public static ArrayList<Order> listConfirm = new ArrayList<>();
    public static ArrayList<Order> listDelivering = new ArrayList<>();
    public static ArrayList<Order> listDelivered = new ArrayList<>();
    public static ArrayList<Order> listCancelled = new ArrayList<>();
    public ArrayList<Order> listAll = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onBack();//Quay trở lại sự kiện trước đó
        getData();
        setTabLayoutAndViewPager2();
    }

    private void getData() {
        ApiService.apiService.getOrderByIdUser(1).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.body() != null){
                    listAll = response.body().getListOrder();
                    checkStatus(listAll);
                }else{
                    Toast.makeText(OrderActivity.this, "Data empty", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "Call api error while get data order", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: " + t);
            }
        });
    }

    private void checkStatus(ArrayList<Order> listAll) {
        for (int i = 0; i < listAll.size(); i++) {
            if (listAll.get(i).getStatus() == 0){
                listConfirm.add(listAll.get(i));
            }else if (listAll.get(i).getStatus() == 1){
                listDelivering.add(listAll.get(i));
            }else if (listAll.get(i).getStatus() == 2){
                listDelivered.add(listAll.get(i));
            }else{
                listCancelled.add(listAll.get(i));
            }
        }
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
}