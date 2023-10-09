package edu.wkd.userappbanghangonline.view.fragment;

import android.content.Context;
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
import edu.wkd.userappbanghangonline.databinding.FragmentDeliveringBinding;
import edu.wkd.userappbanghangonline.model.obj.Order;
import edu.wkd.userappbanghangonline.model.response.OrderResponse;
import edu.wkd.userappbanghangonline.view.activity.OrderActivity;
import edu.wkd.userappbanghangonline.view.adapter.OrderAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeliveringFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeliveringFragment extends Fragment {
    private Context context;
    private FragmentDeliveringBinding binding;
    private OrderAdapter orderAdapter;
    private ArrayList<Order> list;
    public static final String TAG = "Fragment";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context == null){
            throw new NullPointerException("Fragment chưa được gắn vào một hoạt động");
        }
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
    }
    private void getData() {
        ApiService.apiService.getOrderByIdUserAndStatus(1, 1).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()){
                    list = response.body().getListOrder();
                    if (list != null){
                        if (list.isEmpty() || list == null){
                            binding.layoutEmptyOrder.setVisibility(View.VISIBLE);
                            binding.progressBar.setVisibility(View.INVISIBLE);
                        }else{
                            orderAdapter = new OrderAdapter(list);
                            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            binding.rvOrderDelivering.setLayoutManager(manager);
                            binding.rvOrderDelivering.setAdapter(orderAdapter);
                            binding.layoutEmptyOrder.setVisibility(View.INVISIBLE);
                            binding.progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                if (context != null){
                    Toast.makeText(context.getApplicationContext(), "Lỗi server (chi tiết trong logcat)", Toast.LENGTH_SHORT).show();
                }
                Log.e(TAG, "onFailure: " + t);
            }
        });
    }

}