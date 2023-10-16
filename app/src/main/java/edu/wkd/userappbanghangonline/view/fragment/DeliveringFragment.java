package edu.wkd.userappbanghangonline.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.databinding.FragmentDeliveringBinding;
import edu.wkd.userappbanghangonline.model.obj.Order;
import edu.wkd.userappbanghangonline.ultil.OrderInterface;
import edu.wkd.userappbanghangonline.view.activity.OrderActivity;
import edu.wkd.userappbanghangonline.view.adapter.OrderAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeliveringFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeliveringFragment extends Fragment implements OrderInterface {
    private FragmentDeliveringBinding binding;
    private ArrayList<Order> listOrder;
    private OrderAdapter orderAdapter;

    public DeliveringFragment() {
        // Required empty public constructor
    }


    public static DeliveringFragment newInstance() {
        DeliveringFragment fragment = new DeliveringFragment();
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
        binding = FragmentDeliveringBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        OrderActivity orderActivity = (OrderActivity) getActivity();
        if (orderActivity != null){
            orderActivity.setOrderInterface(this);
            orderActivity.getOrderByStatus(1);
        }
        return view;
    }

    @Override
    public void dataOrderReceiver(List<Order> list) {
        listOrder = (ArrayList<Order>) list;
        if (listOrder.isEmpty() || listOrder == null){
            binding.layoutEmptyOrder.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }else{
            orderAdapter = new OrderAdapter(listOrder);
            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            binding.rvOrderDelivering.setLayoutManager(manager);
            binding.rvOrderDelivering.setAdapter(orderAdapter);
            binding.layoutEmptyOrder.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
}