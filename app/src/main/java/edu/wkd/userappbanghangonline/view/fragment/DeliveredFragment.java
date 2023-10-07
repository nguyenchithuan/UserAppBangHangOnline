package edu.wkd.userappbanghangonline.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;


import edu.wkd.userappbanghangonline.view.activity.OrderActivity;
import edu.wkd.userappbanghangonline.view.adapter.OrderAdapter;


import edu.wkd.userappbanghangonline.databinding.FragmentDeliveredBinding;
import edu.wkd.userappbanghangonline.model.obj.Order;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeliveredFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeliveredFragment extends Fragment {
    private FragmentDeliveredBinding binding;
    public static final String TAG = "error";
    private OrderAdapter orderAdapter;
    public DeliveredFragment() {
        // Required empty public constructor
    }

    public static DeliveredFragment newInstance() {
        DeliveredFragment fragment = new DeliveredFragment();
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
        binding = FragmentDeliveredBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
//        listOrder = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        getListOrder();//Lấy danh sách trên api và hiển thị lên recycleView
        getData();
    }

    private void getData() {
        if (OrderActivity.listDelivered.isEmpty() || OrderActivity.listDelivered.size() == 0){
            binding.layoutEmptyOrder.setVisibility(View.VISIBLE);
//            binding.progressBar.setVisibility(View.INVISIBLE);
        }else{
            orderAdapter = new OrderAdapter(OrderActivity.listDelivered);
            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            binding.rvOrderDelivered.setLayoutManager(manager);
            binding.rvOrderDelivered.setAdapter(orderAdapter);
            binding.layoutEmptyOrder.setVisibility(View.INVISIBLE);
//            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

}