package edu.wkd.userappbanghangonline.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.databinding.LayoutItemChooseProductToCommentBinding;
import edu.wkd.userappbanghangonline.model.obj.Product;

public class ProductInCommentAdapter extends RecyclerView.Adapter<ProductInCommentAdapter.ViewHolder> {
    private ArrayList<Product> list;

    public ProductInCommentAdapter(ArrayList<Product> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemChooseProductToCommentBinding binding = LayoutItemChooseProductToCommentBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = list.get(position);
        if (product == null){
            return;
        }
        holder.binding.tvName.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.binding.tvPrice.setText(decimalFormat.format(product.getPrice())+"đ");
        if(product.getImage().contains("uploads")) {
            Glide.with(holder.itemView.getContext())
                    .load("https://guyinterns2003.000webhostapp.com/" + product.getImage())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(holder.binding.imgProduct);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(product.getImage())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(holder.binding.imgProduct);
        }
        holder.binding.chkPurchase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(holder.itemView.getContext(), "Position: "+holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutItemChooseProductToCommentBinding binding;
        public ViewHolder(@NonNull LayoutItemChooseProductToCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
