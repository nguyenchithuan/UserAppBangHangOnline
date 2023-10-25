package edu.wkd.userappbanghangonline.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

import edu.wkd.userappbanghangonline.view.fragment.CancelledFragment;
import edu.wkd.userappbanghangonline.view.fragment.ConfirmationFragment;
import edu.wkd.userappbanghangonline.view.fragment.DeliveredFragment;
import edu.wkd.userappbanghangonline.view.fragment.DeliveringFragment;

public class ViewPager2Adapter extends FragmentStateAdapter {
    private List<Fragment> fragmentList;
    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragmentList) {
        super(fragmentActivity);
        this.fragmentList = fragmentList;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
