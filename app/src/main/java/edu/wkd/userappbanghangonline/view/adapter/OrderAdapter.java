package edu.wkd.userappbanghangonline.view.adapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;


import edu.wkd.userappbanghangonline.model.obj.adapter.ProductInOrderAdapter;
import edu.wkd.userappbanghangonline.databinding.LayoutItemOrderBinding;
import edu.wkd.userappbanghangonline.model.obj.Order;
import edu.wkd.userappbanghangonline.model.obj.Product;
import edu.wkd.userappbanghangonline.ultil.UpdateStatusOrderInterface;
import edu.wkd.userappbanghangonline.view.activity.DetailsOrderActivity;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Order> list;
    private ArrayList<Product> listProduct;
    private ProductInOrderAdapter productAdapter;

    //Tạo interface cho sự kiện delete
    private UpdateStatusOrderInterface updateStatusOrderInterface;
    public void setUpdateStatusOrderInterface(UpdateStatusOrderInterface updateStatusOrderInterface){
        this.updateStatusOrderInterface = updateStatusOrderInterface;
    }

    public OrderAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<Order> list){
        this.list = list;
        notifyDataSetChanged();
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
        listProduct = order.getListProduct();
        int totalProduct = 0;//Tổng số lượng sản phẩm đặt mua
        for (Product product : listProduct){
            totalProduct += product.getQuantity();
        }
//        holder.binding.tvIdOrder.setText("Đơn hàng số: "+order.getId());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.binding.tvAllProductInOrder.setText(totalProduct+" sản phẩm");
        holder.binding.tvAllPriceOrder.setText(decimalFormat.format(order.getTotalPrice())+"đ");
        productAdapter = new ProductInOrderAdapter(listProduct);
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.binding.rvOrder.setLayoutManager(manager);
        holder.binding.rvOrder.setAdapter(productAdapter);
        //Set layout phù hợp với từng trạng thái
        if (order.getStatus() == 0){
            holder.binding.tvStateDelivey.setText("Chờ xác nhận");
            holder.binding.layoutRatingAndReOrder.setVisibility(View.GONE);
            holder.binding.tvCancelOrderOrReOrder.setVisibility(View.VISIBLE);
            holder.binding.tvCancelOrderOrReOrder.setText("Hủy đơn hàng");
            holder.binding.tvCancelOrderOrReOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (updateStatusOrderInterface != null){
                        updateStatusOrderInterface.updateStatusOrderById(order.getId(), holder.getAdapterPosition());
                    }
                }
            });
        }else if (order.getStatus() == 1){
            holder.binding.tvStateDelivey.setText("Đang giao hàng");
            holder.binding.layoutRatingAndReOrder.setVisibility(View.GONE);
            holder.binding.tvCancelOrderOrReOrder.setVisibility(View.GONE);
        }else if (order.getStatus() == 2){
            holder.binding.tvStateDelivey.setText("Giao hàng thành công");
            holder.binding.layoutRatingAndReOrder.setVisibility(View.VISIBLE);
            holder.binding.tvCancelOrderOrReOrder.setVisibility(View.GONE);
        }else{
            holder.binding.tvStateDelivey.setText("Đơn hàng đã bị hủy");
            holder.binding.layoutRatingAndReOrder.setVisibility(View.GONE);
            holder.binding.tvCancelOrderOrReOrder.setVisibility(View.VISIBLE);
            holder.binding.tvCancelOrderOrReOrder.setText("Mua lại");
            holder.binding.tvCancelOrderOrReOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Mua lại đơn đã hủy", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //Dùng interface để hiển thị chi tiết đơn hàng (gọi bên order activity)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsOrderActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtra("position", holder.getAdapterPosition());
                bundle.putSerializable("listProduct", getListProduct(holder.getAdapterPosition()));
                bundle.putSerializable("infoOrder", order);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }


    private ArrayList<Product> getListProduct(int position){
        Order order = list.get(position);
        if (order != null){
            return order.getListProduct();
        }
        return new ArrayList<>();
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
