package edu.wkd.userappbanghangonline.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
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
    public static ArrayList<Order> listConfirm;
    public static ArrayList<Order> listDelivering;
    public static ArrayList<Order> listDelivered;
    public static ArrayList<Order> listCancelled;
    public ArrayList<Order> listAll;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("my_shared", MODE_PRIVATE);
        onBack();//Quay trở lại sự kiện trước đó
        setTabLayoutAndViewPager2();
        getData();

    }

    private void getData() {
        listAll         = new ArrayList<>();
        listConfirm     = new ArrayList<>();
        listDelivered   = new ArrayList<>();
        listDelivering  = new ArrayList<>();
        listCancelled   = new ArrayList<>();
        int idUser = sharedPreferences.getInt("idUser", 0);
        ApiService.apiService.getOrderByIdUser(idUser).enqueue(new Callback<OrderResponse>() {
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

    private void checkStatus(ArrayList<Order> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getStatus() == 0){
                listConfirm.add(list.get(i));
            }else if (list.get(i).getStatus() == 1){
                listDelivering.add(list.get(i));
            }else if (list.get(i).getStatus() == 2){
                listDelivered.add(list.get(i));
            }else{
                listCancelled.add(list.get(i));
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