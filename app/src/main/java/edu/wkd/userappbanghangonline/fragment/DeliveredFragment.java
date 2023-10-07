package edu.wkd.userappbanghangonline.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import edu.wkd.userappbanghangonline.activity.OrderActivity;
import edu.wkd.userappbanghangonline.adapter.OrderAdapter;
import edu.wkd.userappbanghangonline.api.ApiService;
import edu.wkd.userappbanghangonline.databinding.FragmentDeliveredBinding;
import edu.wkd.userappbanghangonline.model.obj.Order;
import edu.wkd.userappbanghangonline.model.response.OrderResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            binding.progressBar.setVisibility(View.INVISIBLE);
        }else{
            orderAdapter = new OrderAdapter(OrderActivity.listDelivered);
            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            binding.rvOrderDelivered.setLayoutManager(manager);
            binding.rvOrderDelivered.setAdapter(orderAdapter);
            binding.layoutEmptyOrder.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

//    private void getListOrder() {
//            ApiService.apiService.getOrderByIdUser(1).enqueue(new Callback<OrderResponse>() {
//                @Override
//                public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
//                    if (response.body() != null){
//                        binding.progressBar.setVisibility(View.INVISIBLE);
//                        listOrder = response.body().getListOrder();
//                        showAndHideItem(listOrder);
//                        setLayoutOrderInRecycleView(listOrder);
//                    }else{
//                        Toast.makeText(getContext(), "Data empty", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<OrderResponse> call, Throwable t) {
//                    Toast.makeText(getContext().getApplicationContext(), "Call api error while get data order", Toast.LENGTH_SHORT).show();
//                    Log.e(TAG, "onFailure: " + t);
//                }
//            });
//    }

    private void showAndHideItem(ArrayList<Order> list) {
        if (list.size() == 0 || list.isEmpty()){
            binding.layoutEmptyOrder.setVisibility(View.VISIBLE);
            binding.rvOrderDelivered.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }else{
            binding.layoutEmptyOrder.setVisibility(View.INVISIBLE);
            binding.rvOrderDelivered.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

//    private void setLayoutOrderInRecycleView(ArrayList<Order> list){
//        orderAdapter = new OrderAdapter(list);
//        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        binding.rvOrderDelivered.setLayoutManager(manager);
//        binding.rvOrderDelivered.setAdapter(orderAdapter);
//    }

}