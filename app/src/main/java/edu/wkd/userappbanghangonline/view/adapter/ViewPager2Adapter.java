package edu.wkd.userappbanghangonline.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import edu.wkd.userappbanghangonline.ultil.OrderInterface;
import edu.wkd.userappbanghangonline.view.fragment.CancelledFragment;
import edu.wkd.userappbanghangonline.view.fragment.ConfirmationFragment;
import edu.wkd.userappbanghangonline.view.fragment.DeliveredFragment;
import edu.wkd.userappbanghangonline.view.fragment.DeliveringFragment;

public class ViewPager2Adapter extends FragmentStateAdapter {
    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = ConfirmationFragment.newInstance();
                break;
            case 1:
                fragment = DeliveringFragment.newInstance();
                break;
            case 2:
                fragment = DeliveredFragment.newInstance();
                break;
            case 3:
                fragment = CancelledFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
