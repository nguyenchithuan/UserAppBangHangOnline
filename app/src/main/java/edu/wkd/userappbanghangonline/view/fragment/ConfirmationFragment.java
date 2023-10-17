package edu.wkd.userappbanghangonline.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import edu.wkd.userappbanghangonline.databinding.FragmentConfirmationBinding;
import edu.wkd.userappbanghangonline.model.obj.Order;
import edu.wkd.userappbanghangonline.model.response.OrderResponse;
import edu.wkd.userappbanghangonline.ultil.UpdateStatusOrderInterface;
import edu.wkd.userappbanghangonline.ultil.UserUltil;
import edu.wkd.userappbanghangonline.view.adapter.OrderAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmationFragment extends Fragment{
    private static final String TAG = "Error";
    private FragmentConfirmationBinding binding;
    private OrderAdapter orderAdapter;
    private ArrayList<Order> listOrder;


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
        getData();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
    }

    private void getData() {
        int idUser = UserUltil.user.getId();
        ApiService.apiService.getOrderByIdUserAndStatus(idUser, 0).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()){
                    listOrder = response.body().getListOrder();
                    if (listOrder.isEmpty() || listOrder.size() == 0){
                        binding.layoutEmptyOrder.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.INVISIBLE);
                    }else{
                        binding.layoutEmptyOrder.setVisibility(View.INVISIBLE);
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        orderAdapter = new OrderAdapter(listOrder);
                        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        binding.rvOrderConfirm.setLayoutManager(manager);
                        binding.rvOrderConfirm.setAdapter(orderAdapter);
                        orderAdapter.setUpdateStatusOrderInterface(new UpdateStatusOrderInterface() {
                            @Override
                            public void updateStatusOrderById(int id, int position) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Hủy đơn hàng")
                                        .setIcon(android.R.drawable.ic_delete)
                                        .setMessage("Bạn chắc chắn muốn hủy đơn hàng này?")
                                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ApiService.apiService.updateStatusOrder(id,3).enqueue(new Callback<Order>() {
                                                    @Override
                                                    public void onResponse(Call<Order> call, Response<Order> response) {
                                                        if (response.isSuccessful()){
                                                            listOrder.remove(position);
                                                            orderAdapter.notifyDataSetChanged();
                                                            Toast.makeText(getContext().getApplicationContext(), "Hủy đơn hàng thành công.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    @Override
                                                    public void onFailure(Call<Order> call, Throwable t) {
                                                        Toast.makeText(getContext().getApplicationContext(), "Hủy đơn hàng thất bại.", Toast.LENGTH_SHORT).show();
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