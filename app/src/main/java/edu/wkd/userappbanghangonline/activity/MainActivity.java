package edu.wkd.userappbanghangonline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.navigation.NavigationBarView;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.databinding.ActivityMainBinding;
import edu.wkd.userappbanghangonline.fragment.HomeFragment;
import edu.wkd.userappbanghangonline.fragment.NotificationFragment;
import edu.wkd.userappbanghangonline.fragment.ProductFragment;
import edu.wkd.userappbanghangonline.fragment.UserFragment;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setOnClickIconInBottomNav();//Xử lí sự kiện khi người dùng nhấn vào bottom nav
    }

    private void setOnClickIconInBottomNav() {
        //Đặt trang home là trang mặc định
        chooseFragment(HomeFragment.newInstance());
        binding.bottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
        binding.bottomNavigationView.getMenu().findItem(R.id.menu_home).setIcon(R.drawable.home_black);

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            private MenuItem selectedItem;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (selectedItem != null) {
                    selectedItem.setIcon(getUnselectedIcon(selectedItem.getItemId()));
                }

                selectedItem = item;
                item.setIcon(getSelectedIcon(item.getItemId()));

                int itemId = item.getItemId();
                if (itemId == R.id.menu_home){
                    chooseFragment(HomeFragment.newInstance());
                } else if (itemId == R.id.menu_product){
                    chooseFragment(ProductFragment.newInstance());
                } else if (itemId == R.id.menu_notification){
                    chooseFragment(NotificationFragment.newInstance());
                } else if (itemId == R.id.menu_user){
                    chooseFragment(UserFragment.newInstance());
                }

                return true;
            }
        });
    }

    private int getSelectedIcon(int itemId) {
        if (itemId == R.id.menu_home){
            return R.drawable.home_black;
        }else if (itemId == R.id.menu_product){
            return R.drawable.list_black;
        }else if (itemId == R.id.menu_notification){
            return R.drawable.bell_black;
        }else if (itemId == R.id.menu_user){
            return R.drawable.user_black;
        }
        return R.drawable.home_black;
    }

    private int getUnselectedIcon(int itemId) {
        if (itemId == R.id.menu_home){
            return R.drawable.home;
        }else if (itemId == R.id.menu_product){
            return R.drawable.list;
        }else if (itemId == R.id.menu_notification){
            return R.drawable.bell;
        }else if (itemId == R.id.menu_user){
            return R.drawable.user;
        }
        return R.drawable.home;
    }

    //Thay thế fragment
    private void chooseFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.frameLayout.getId(), fragment);
        transaction.commit();
    }
}