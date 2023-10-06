package edu.wkd.userappbanghangonline.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import edu.wkd.userappbanghangonline.R;

import edu.wkd.userappbanghangonline.model.obj.Product;
import edu.wkd.userappbanghangonline.ultil.ItemProductInterface;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    private Context context;
    private List<Product> listProduct;
    private ItemProductInterface itemProductInterface;


    public ProductAdapter(Context context, List<Product> listProduct, ItemProductInterface itemProductInterface) {
        this.context = context;
        this.listProduct = listProduct;
        this.itemProductInterface = itemProductInterface;
    }

    public void setListProduct(List<Product> listProduct) {
        this.listProduct = listProduct;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_product,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = listProduct.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###đ");
        if(product == null){
            return;
        }
        Glide.with(context)
                .load(product.getImage())
                .error(R.mipmap.ic_launcher)
                .into(holder.imgProduct);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(decimalFormat.format(product.getPrice()));

        holder.ratingBar.setRating(product.getRating());
        holder.tvQuantityRating.setText("(" + product.getQuantityRating() + ")");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemProductInterface.onClickItemProduct(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listProduct != null){
            return  listProduct.size();
        }
        return 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgProduct;
        private TextView tvName;
        private TextView tvPrice;
        private RatingBar ratingBar;
        private TextView tvQuantityRating;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvQuantityRating = itemView.findViewById(R.id.tvQuantityRating);
        }
    }
}
