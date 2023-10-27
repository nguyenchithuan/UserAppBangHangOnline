package edu.wkd.userappbanghangonline.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.wkd.userappbanghangonline.databinding.LayoutChooseProductToReviewsBinding;
import edu.wkd.userappbanghangonline.model.obj.Product;
import edu.wkd.userappbanghangonline.ultil.ChooseProductToCommentInterface;
import edu.wkd.userappbanghangonline.ultil.GetListOrderInterface;
import edu.wkd.userappbanghangonline.ultil.OnCheckedInChooseProductInterface;
import edu.wkd.userappbanghangonline.view.activity.OrderActivity;
import edu.wkd.userappbanghangonline.view.activity.ProductReviewsActivity;
import edu.wkd.userappbanghangonline.view.adapter.OrderAdapter;


import edu.wkd.userappbanghangonline.databinding.FragmentDeliveredBinding;
import edu.wkd.userappbanghangonline.model.obj.Order;
import edu.wkd.userappbanghangonline.view.adapter.ProductInReviewsAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeliveredFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeliveredFragment extends Fragment implements GetListOrderInterface {
    private static final String TAG = "Error";
    private FragmentDeliveredBinding binding;
    private OrderAdapter orderAdapter;
    private ArrayList<Order> listOrder;
    private final int idOrder = 0;



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
        return view;
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
            orderActivity.setGetListOrderInterface(this::getListOrder);
            orderActivity.getListOrderByStatus(2);
            if (orderAdapter != null && listOrder != null){
                orderAdapter.setData(listOrder);
                binding.rvOrderDelivered.setAdapter(orderAdapter);
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
            binding.rvOrderDelivered.setVisibility(View.VISIBLE);
            binding.layoutEmptyOrder.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
            orderAdapter = new OrderAdapter(getContext().getApplicationContext());
            orderAdapter.setData(listOrder);
            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            binding.rvOrderDelivered.setLayoutManager(manager);
            binding.rvOrderDelivered.setAdapter(orderAdapter);
            showChooseProductToComment(orderAdapter);
        }
    }
    private void showChooseProductToComment(OrderAdapter orderAdapter) {
        orderAdapter.setChooseProductToCommentInterface(new ChooseProductToCommentInterface() {
            @Override
            public void getListProductToComment(List<Product> list, int orderId) {
                //Nếu có từ 2 sản phẩm trở lên hiển thị dialog cho người dùng chọn sản phẩm muốn đánh giá
                if (list.size() > 1){
                    ArrayList<Product> listSelectedProduct = new ArrayList<>();
                    LayoutChooseProductToReviewsBinding bindingProduct = LayoutChooseProductToReviewsBinding.inflate(getLayoutInflater());
                    Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Light);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(bindingProduct.getRoot());
                    Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
                    ProductInReviewsAdapter productAdapter = new ProductInReviewsAdapter((ArrayList<Product>) list,1);
                    LinearLayoutManager manager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    bindingProduct.rvProductToComment.setLayoutManager(manager);
                    bindingProduct.rvProductToComment.setAdapter(productAdapter);
                    //Thêm sản phẩm vào danh sách đánh giá khi người dụng chọn CheckBox
                    productAdapter.setChooseProductInterface(new OnCheckedInChooseProductInterface() {
                        @Override
                        public void addProductSelected(Product product) {
                            if (product != null){
                                if (bindingProduct.chkChooseAllProductToComment.isChecked() == false){
                                    listSelectedProduct.add(product);
                                }
                            }
                        }

                        @Override
                        public void removeProduct(Product product) {
                            if (product != null){
                                if (bindingProduct.chkChooseAllProductToComment.isChecked() == false){
                                    listSelectedProduct.remove(product);
                                }
                            }
                        }
                    });

                    //Xử lí sự kiện khi chọn tất cả sản phẩm
                    bindingProduct.chkChooseAllProductToComment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked){
                                listSelectedProduct.clear();//Xóa toàn bộ phần tử trong mảng nếu không sẽ thừa dữ liệu
                                for (int i = 0; i < list.size(); i++) {
                                    listSelectedProduct.add(list.get(i));
                                }
                            }else{
                                listSelectedProduct.clear();

                            }
                            productAdapter.setAllItemsChecked(isChecked);
                        }
                    });

                    //Chuyển đến màn hình đánh giá sản phẩm
                    bindingProduct.btnGoToProductReviews.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (list.size() == 0){
                                Toast.makeText(getContext().getApplicationContext(), "Vui lòng chọn sản phẩm bạn muốn đánh giá!", Toast.LENGTH_SHORT).show();
                            }else{
                                goToActivityProductReviews((ArrayList<Product>) listSelectedProduct, orderId);
                                dialog.dismiss();
                            }
                        }
                    });
                    //Hủy dialog
                    bindingProduct.arrowBackChooseProductToComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }else{
                    goToActivityProductReviews((ArrayList<Product>) list, orderId);
                }
            }
        });
    }

    private void goToActivityProductReviews(ArrayList<Product> list, int orderId){
        Intent intent = new Intent(getActivity(), ProductReviewsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("list_product_rating",list);
        bundle.putInt("id_order", orderId);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}