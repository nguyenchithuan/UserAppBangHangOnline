package edu.wkd.userappbanghangonline.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.wkd.userappbanghangonline.databinding.FragmentConfirmationBinding;
import edu.wkd.userappbanghangonline.view.activity.OrderActivity;
import edu.wkd.userappbanghangonline.view.adapter.OrderAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmationFragment extends Fragment {
    private FragmentConfirmationBinding binding;
    private OrderAdapter orderAdapter;
    public ConfirmationFragment() {
        // Required empty public constructor
    }


    public static ConfirmationFragment newInstance() {
        ConfirmationFragment fragment = new ConfirmationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentConfirmationBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
    }

    private void getData() {
        if (OrderActivity.listConfirm.isEmpty() || OrderActivity.listConfirm.size() == 0){
            binding.layoutEmptyOrder.setVisibility(View.VISIBLE);
        }else{
            orderAdapter = new OrderAdapter(OrderActivity.listConfirm);
            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            binding.rvOrderConfirmation.setLayoutManager(manager);
            binding.rvOrderConfirmation.setAdapter(orderAdapter);
            binding.layoutEmptyOrder.setVisibility(View.INVISIBLE);
        }
    }
}