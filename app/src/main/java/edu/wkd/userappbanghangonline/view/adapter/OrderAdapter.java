package edu.wkd.userappbanghangonline.view.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.wkd.userappbanghangonline.adapter.ProductInOrderAdapter;
import edu.wkd.userappbanghangonline.databinding.LayoutItemOrderBinding;
import edu.wkd.userappbanghangonline.model.obj.Order;
import edu.wkd.userappbanghangonline.model.obj.Product;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    private ArrayList<Order> list;
    private ArrayList<Product> listProduct;
    private ProductInOrderAdapter productAdapter;

    public OrderAdapter(ArrayList<Order> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemOrderBinding binding = LayoutItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = list.get(position);
        if (order == null){
            return;
        }
//        listProduct = new ArrayList<>();
        listProduct = order.getListProduct();
        int totalProduct = 0;//Tổng số lượng sản phẩm đặt mua
        for (Product product : listProduct){
            totalProduct += product.getQuantity();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.binding.tvAllProductInOrder.setText(totalProduct+" sản phẩm");
        holder.binding.tvAllPriceOrder.setText(decimalFormat.format(order.getTotalPrice())+"đ");
        productAdapter = new ProductInOrderAdapter(listProduct);
        LinearLayoutManager manager = new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL, false);
        holder.binding.rvOrder.setLayoutManager(manager);
        holder.binding.rvOrder.setAdapter(productAdapter);
        if (order.getStatus() == 0){
            holder.binding.tvStateDelivey.setText("Chờ xác nhận");
            holder.binding.layoutRatingAndReOrder.setVisibility(View.GONE);
        }else if (order.getStatus() == 1){
            holder.binding.tvStateDelivey.setText("Đang giao hàng");
            holder.binding.layoutRatingAndReOrder.setVisibility(View.GONE);
        }else if (order.getStatus() == 2){
            holder.binding.tvStateDelivey.setText("Giao hàng thành công");
            holder.binding.layoutRatingAndReOrder.setVisibility(View.VISIBLE);
        }else{
            holder.binding.tvStateDelivey.setText("Đơn hàng đã bị hủy");
            holder.binding.layoutRatingAndReOrder.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutItemOrderBinding binding;
        public ViewHolder(@NonNull LayoutItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
