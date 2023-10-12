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
import java.util.ArrayList;
import java.util.List;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.databinding.LayoutItemTypeProductBinding;
import edu.wkd.userappbanghangonline.model.obj.Product;
import edu.wkd.userappbanghangonline.model.obj.ProductType;
import edu.wkd.userappbanghangonline.ultil.ItemProductTypeInterface;


public class ProductTypeAdapter extends RecyclerView.Adapter<ProductTypeAdapter.ProductTypeViewHolder>{
    private Context context;
    private List<ProductType> listProductType;
    private ItemProductTypeInterface itemProductTypeInterface;


    public ProductTypeAdapter(Context context, List<ProductType> listProductType, ItemProductTypeInterface itemProductTypeInterface) {
        this.context = context;
        this.listProductType = listProductType;
        this.itemProductTypeInterface = itemProductTypeInterface;
    }

    public void setListTypeProduct(List<ProductType> listProductType) {
        this.listProductType = listProductType;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductTypeAdapter.ProductTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_type_product,parent,false);
        return new ProductTypeAdapter.ProductTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductTypeAdapter.ProductTypeViewHolder holder, int position) {
        ProductType producttype = listProductType.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###đ");
        if(producttype == null){
            return;
        }
        Glide.with(context)
                .load(producttype.getImage())
                .error(R.mipmap.ic_launcher)
                .into(holder.imgTypeProduct);
        holder.tvName.setText(producttype.getTypeName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemProductTypeInterface.onClickItemProduct(producttype);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listProductType != null){
            return  listProductType.size();
        }
        return 0;
    }

    public static class ProductTypeViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgTypeProduct;
        private TextView tvName;

        public ProductTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTypeProduct = itemView.findViewById(R.id.imgTypeProduct);
            tvName = itemView.findViewById(R.id.tvNameTypeProduct);
        }
    }
}