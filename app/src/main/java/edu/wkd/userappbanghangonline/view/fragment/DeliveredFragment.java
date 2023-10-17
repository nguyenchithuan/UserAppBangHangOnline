package edu.wkd.userappbanghangonline.view.fragment;

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


import edu.wkd.userappbanghangonline.data.api.ApiService;
import edu.wkd.userappbanghangonline.model.response.OrderResponse;
import edu.wkd.userappbanghangonline.ultil.UserUltil;
import edu.wkd.userappbanghangonline.view.adapter.OrderAdapter;


import edu.wkd.userappbanghangonline.databinding.FragmentDeliveredBinding;
import edu.wkd.userappbanghangonline.model.obj.Order;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeliveredFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeliveredFragment extends Fragment{
    private static final String TAG = "Error";
    private FragmentDeliveredBinding binding;
    private OrderAdapter orderAdapter;
    private ArrayList<Order> listOrder;

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
        getData();
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getData() {
        int idUser = UserUltil.user.getId();
        ApiService.apiService.getOrderByIdUserAndStatus(idUser, 2).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()){
                    listOrder = response.body().getListOrder();
                    if (listOrder.isEmpty()){
                        binding.layoutEmptyOrder.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.INVISIBLE);
                    }else{
                        binding.layoutEmptyOrder.setVisibility(View.INVISIBLE);
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        orderAdapter = new OrderAdapter(listOrder);
                        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        binding.rvOrderDelivered.setLayoutManager(manager);
                        binding.rvOrderDelivered.setAdapter(orderAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(getContext().getApplicationContext(), "Lỗi server (chi tiết trong logcat)", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: " + t);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}