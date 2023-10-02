package edu.wkd.userappbanghangonline.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.wkd.userappbanghangonline.databinding.LayoutItemTypeProductBinding;
import edu.wkd.userappbanghangonline.model.obj.ProductType;


public class ProductTypeAdapter extends RecyclerView.Adapter<ProductTypeAdapter.ViewHolder>{
    private ArrayList<ProductType> list;

    public ProductTypeAdapter(ArrayList<ProductType> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemTypeProductBinding binding = LayoutItemTypeProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductType productType = list.get(position);
        holder.binding.tvNameTypeProduct.setText(productType.getTypeName());
        Glide.with(holder.itemView.getContext())
                .load(productType.getImage())
                .into(holder.binding.imgTypeProduct);
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutItemTypeProductBinding binding;
        public ViewHolder(LayoutItemTypeProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
