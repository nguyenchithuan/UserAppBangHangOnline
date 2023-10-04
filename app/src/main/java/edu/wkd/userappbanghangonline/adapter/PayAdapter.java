package edu.wkd.userappbanghangonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.model.obj.Cart;

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.PayViewHolder> {
    private Context context;
    private List<Cart> listCart;

    public PayAdapter(Context context, List<Cart> listCart) {
        this.context = context;
        this.listCart = listCart;
    }

    public void setListCart(List<Cart> listCart) {
        this.listCart = listCart;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_pay, parent, false);
        return new PayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PayViewHolder holder, int position) {
        Cart cart = listCart.get(position);
        if(cart == null) {
            return;
        }

        holder.tvName.setText(cart.getName());
        DecimalFormat df = new DecimalFormat("###,###,###");
        holder.tvPrice.setText(df.format(cart.getPrice()) + " đ");
        holder.tvQuantity.setText("x" + cart.getQuantity());
        Glide.with(context)
                .load(cart.getImage())
                .error(R.mipmap.ic_launcher)
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        if(listCart != null) {
            return listCart.size();
        }
        return 0;
    }

    public class PayViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView tvName;
        private TextView tvPrice;
        private TextView tvQuantity;

        public PayViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }
}
