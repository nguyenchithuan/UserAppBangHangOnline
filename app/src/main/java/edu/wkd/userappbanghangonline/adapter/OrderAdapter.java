package edu.wkd.userappbanghangonline.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.wkd.userappbanghangonline.databinding.LayoutItemOrderBinding;
import edu.wkd.userappbanghangonline.model.obj.Order;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    private ArrayList<Order> list;

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
        holder.binding.tvNameProductOrder.setText(order.getListProduct().get(0).getName());//Tên sản phẩm đầu tiên
        holder.binding.tvQuantityProductOrder.setText(order.getListProduct().get(0).getQuantity()+"");//Số lượng sản phẩm đầu tiên
        holder.binding.tvPriceProductOrder.setText(order.getListProduct().get(0).getPrice()+"");//Giá sản phẩm
        //Dưới đây là giá cũ
        holder.binding.tvOldPriceProductOrder.setPaintFlags(holder.binding.tvOldPriceProductOrder.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);//Tạo dấu gạch ngang
        int oldPriceProduct = order.getListProduct().get(0).getPrice() + 25000;//Giá cũ đang fix cứng = giá của sản phẩm đầu tiên trong danh sách order + 25k
        holder.binding.tvOldPriceProductOrder.setText(oldPriceProduct+"");
        holder.binding.tvAllPriceOrder.setText(order.getListProduct().get(0).getPrice()+"");//Giá sản phẩm sau khi đã qua phí vận chuyển, mã giảm giá
        //Hiển thị ảnh sản phẩm đầu tiên
        Glide.with(holder.itemView.getContext())
                .load(order.getListProduct().get(0).getImage())
                .into(holder.binding.imgProductOrder);
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
