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
import java.util.List;

import edu.wkd.userappbanghangonline.data.api.ApiService;
import edu.wkd.userappbanghangonline.databinding.FragmentCancelledBinding;
import edu.wkd.userappbanghangonline.model.obj.Order;
import edu.wkd.userappbanghangonline.model.response.OrderResponse;
import edu.wkd.userappbanghangonline.ultil.OrderInterface;
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
public class CancelledFragment extends Fragment implements OrderInterface {
    private Context context;
    private FragmentCancelledBinding binding;
    private OrderAdapter orderAdapter;
    private ArrayList<Order> listOrder;

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
        OrderActivity orderActivity = (OrderActivity) getActivity();
        if (orderActivity != null){
            orderActivity.getOrderByStatus(3);
            orderActivity.setOrderInterface(this);
        }
        return binding.getRoot();
    }

    @Override
    public void dataOrderReceiver(List<Order> list) {
        listOrder = (ArrayList<Order>) list;
        if (listOrder.isEmpty()){
            binding.layoutEmptyOrder.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }else{
            orderAdapter = new OrderAdapter(listOrder);
            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            binding.rvOrderCancelled.setLayoutManager(manager);
            binding.rvOrderCancelled.setAdapter(orderAdapter);
            binding.layoutEmptyOrder.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        OrderActivity orderActivity = (OrderActivity) getActivity();
        if (orderActivity != null){
            orderActivity.getOrderByStatus(3);
        }
    }
}