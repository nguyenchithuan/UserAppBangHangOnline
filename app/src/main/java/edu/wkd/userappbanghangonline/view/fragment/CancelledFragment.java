package edu.wkd.userappbanghangonline.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.wkd.userappbanghangonline.data.api.ApiService;
import edu.wkd.userappbanghangonline.databinding.FragmentCancelledBinding;
import edu.wkd.userappbanghangonline.model.obj.Order;
import edu.wkd.userappbanghangonline.model.response.OrderResponse;
import edu.wkd.userappbanghangonline.ultil.GetListOrderInterface;
import edu.wkd.userappbanghangonline.ultil.UserUltil;
import edu.wkd.userappbanghangonline.view.activity.OrderActivity;
import edu.wkd.userappbanghangonline.view.adapter.OrderAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CancelledFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CancelledFragment extends Fragment implements GetListOrderInterface {
    private static final String TAG = "Error";
    private FragmentCancelledBinding binding;
    private OrderAdapter orderAdapter;
    private ArrayList<Order> listOrder;

    public CancelledFragment() {
        // Required empty public constructor
    }


    public static CancelledFragment newInstance() {
        CancelledFragment fragment = new CancelledFragment();
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
        binding = FragmentCancelledBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        },1000);
    }

    private void getData(){
        OrderActivity orderActivity = (OrderActivity) getActivity();
        if (orderActivity != null){
            orderActivity.setGetListOrderInterface(this);
            orderActivity.getListOrderByStatus(3);
            if (orderAdapter != null && listOrder != null){
                orderAdapter.setData(listOrder);
                binding.rvOrderCancelled.setAdapter(orderAdapter);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        },500);
    }

    @Override
    public void getListOrder(List<Order> list) {
        listOrder = (ArrayList<Order>) list;
        if (listOrder.isEmpty()){
            binding.layoutEmptyOrder.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }else{
            binding.rvOrderCancelled.setVisibility(View.VISIBLE);
            binding.layoutEmptyOrder.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
            orderAdapter = new OrderAdapter(getContext().getApplicationContext());
            orderAdapter.setData(listOrder);
            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            binding.rvOrderCancelled.setLayoutManager(manager);
            binding.rvOrderCancelled.setAdapter(orderAdapter);
        }
    }
}