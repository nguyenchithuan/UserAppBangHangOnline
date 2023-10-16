package edu.wkd.userappbanghangonline.view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.wkd.userappbanghangonline.data.api.ApiService;
import edu.wkd.userappbanghangonline.databinding.FragmentConfirmationBinding;
import edu.wkd.userappbanghangonline.model.obj.Order;
import edu.wkd.userappbanghangonline.model.response.OrderResponse;
import edu.wkd.userappbanghangonline.ultil.DeleteOrderInterface;
import edu.wkd.userappbanghangonline.ultil.OrderInterface;
import edu.wkd.userappbanghangonline.view.activity.DetailsOrderActivity;
import edu.wkd.userappbanghangonline.view.activity.OrderActivity;
import edu.wkd.userappbanghangonline.view.adapter.OrderAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmationFragment extends Fragment implements OrderInterface{
    private static final String TAG = "Error";
    private Context context;
    private FragmentConfirmationBinding binding;
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
        OrderActivity orderActivity = (OrderActivity) getActivity();
        if (orderActivity != null){
            orderActivity.getOrderByStatus(0);
            orderActivity.setOrderInterface(this);
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
            binding.rvOrderConfirm.setLayoutManager(manager);
            binding.rvOrderConfirm.setAdapter(orderAdapter);
            orderAdapter.setDeleteOrderInterface(new DeleteOrderInterface() {
                @Override
                public void deleteOrderById(int id, int position) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Hủy đơn hàng")
                            .setIcon(android.R.drawable.ic_delete)
                            .setMessage("Bạn chắc chắn muốn hủy đơn hàng này?")
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ApiService.apiService.deleteOrderById(id).enqueue(new Callback<Order>() {
                                        @Override
                                        public void onResponse(Call<Order> call, Response<Order> response) {
                                            if (response.isSuccessful()){
                                                listOrder.remove(position);
                                                orderAdapter.notifyDataSetChanged();
                                                Toast.makeText(context.getApplicationContext(), "Hủy đơn hàng thành công.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<Order> call, Throwable t) {
                                            Toast.makeText(context.getApplicationContext(), "Hủy đơn hàng thất bại.", Toast.LENGTH_SHORT).show();
                                            Log.e(TAG, "onResponse: " + t);
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }

            });
            binding.layoutEmptyOrder.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}