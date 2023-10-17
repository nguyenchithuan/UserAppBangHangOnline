package edu.wkd.userappbanghangonline.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.databinding.ActivityOrderBinding;

import edu.wkd.userappbanghangonline.view.adapter.ViewPager2Adapter;
import edu.wkd.userappbanghangonline.view.fragment.CancelledFragment;
import edu.wkd.userappbanghangonline.view.fragment.ConfirmationFragment;
import edu.wkd.userappbanghangonline.view.fragment.DeliveredFragment;
import edu.wkd.userappbanghangonline.view.fragment.DeliveringFragment;


public class OrderActivity extends AppCompatActivity{
    private ActivityOrderBinding binding;
    private ViewPager2Adapter viewPager2Adapter;
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
        binding.viewPager2.setCurrentItem(0);
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
                Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                intent.putExtra("onBack", "OrderToMain");
                startActivity(intent);
                overridePendingTransition(R.anim.slidle_in_left, R.anim.slidle_out_left);
                finish();
            }
        });
    }

}