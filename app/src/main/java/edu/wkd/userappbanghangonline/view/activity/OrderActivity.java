package edu.wkd.userappbanghangonline.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.data.api.ApiService;
import edu.wkd.userappbanghangonline.databinding.ActivityOrderBinding;

import edu.wkd.userappbanghangonline.model.response.OrderResponse;
import edu.wkd.userappbanghangonline.ultil.GetListOrderInterface;
import edu.wkd.userappbanghangonline.ultil.UserUltil;
import edu.wkd.userappbanghangonline.view.adapter.OrderAdapter;
import edu.wkd.userappbanghangonline.view.adapter.ViewPager2Adapter;
import edu.wkd.userappbanghangonline.view.fragment.CancelledFragment;
import edu.wkd.userappbanghangonline.view.fragment.ConfirmationFragment;
import edu.wkd.userappbanghangonline.view.fragment.DeliveredFragment;
import edu.wkd.userappbanghangonline.view.fragment.DeliveringFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderActivity extends AppCompatActivity{
    private static final String TAG = OrderActivity.class.toString();
    private ActivityOrderBinding binding;
    private ViewPager2Adapter viewPager2Adapter;
    private GetListOrderInterface getListOrderInterface;


    public void setGetListOrderInterface(GetListOrderInterface getListOrderInterface){
        this.getListOrderInterface = getListOrderInterface;
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
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new ConfirmationFragment());
        fragmentList.add(new DeliveringFragment());
        fragmentList.add(new DeliveredFragment());
        fragmentList.add(new CancelledFragment());
        viewPager2Adapter = new ViewPager2Adapter(OrderActivity.this, fragmentList);
        binding.viewPager2.setAdapter(viewPager2Adapter);
        binding.viewPager2.setOffscreenPageLimit(3);//Load trước 3 page
        binding.viewPager2.setCurrentItem(0);
        long itemId = viewPager2Adapter.getItemId(binding.viewPager2.getCurrentItem());
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("f"+itemId);
        TabLayoutMediator mediator = new TabLayoutMediator(binding.tabLayout, binding.viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Chờ xác nhận");
                        if (fragment instanceof ConfirmationFragment){
                            ((ConfirmationFragment)fragment).reloadData();
                        }
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

    public void getListOrderByStatus(int status){
        int idUser = UserUltil.user.getId();
        ApiService.apiService.getOrderByIdUserAndStatus(idUser, status).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()){
                    getListOrderInterface.getListOrder(response.body().getListOrder());
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "Lỗi server (chi tiết trong logcat)", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: " + t);
            }
        });
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