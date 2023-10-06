package edu.wkd.userappbanghangonline.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.databinding.ActivityMainBinding;
import edu.wkd.userappbanghangonline.view.fragment.HomeFragment;
import edu.wkd.userappbanghangonline.view.fragment.NotificationFragment;
import edu.wkd.userappbanghangonline.view.fragment.ProductFragment;
import edu.wkd.userappbanghangonline.view.fragment.UserFragment;
import edu.wkd.userappbanghangonline.ultil.CheckConection;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setOnClickIconInBottomNav();//Xử lí sự kiện khi người dùng nhấn vào bottom nav

        if (CheckConection.HaveConnection(this)){
            CheckConection.ShowToast(this, "Kết nối thành công!");
        }else
            CheckConection.ShowToast(this,"Kết nối thất bại!");
    }

    private void setOnClickIconInBottomNav() {
        //Đặt trang home là trang mặc định
        chooseFragment(HomeFragment.newInstance());
        binding.bottomNavigationView.add(new MeowBottomNavigation.Model(1, R.drawable.home_black));
        binding.bottomNavigationView.add(new MeowBottomNavigation.Model(2, R.drawable.list_black));
        binding.bottomNavigationView.add(new MeowBottomNavigation.Model(3, R.drawable.bell_black));
        binding.bottomNavigationView.add(new MeowBottomNavigation.Model(4, R.drawable.user_black));
        binding.bottomNavigationView.show(1, true);

        binding.bottomNavigationView.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()){
                    case 1:
                        chooseFragment(HomeFragment.newInstance());
                        break;
                    case 2:
                        chooseFragment(ProductFragment.newInstance());
                        break;
                    case 3:
                        chooseFragment(NotificationFragment.newInstance());
                        break;
                    case 4:
                        chooseFragment(UserFragment.newInstance());
                        break;
                }
                return null;
            }
        });
    }
    //Thay thế fragment
    private void chooseFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.frameLayout.getId(), fragment);
        transaction.commit();
    }
}