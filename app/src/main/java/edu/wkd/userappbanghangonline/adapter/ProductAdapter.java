package edu.wkd.userappbanghangonline.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.model.obj.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    private final List<Product> listProduct;

    public ProductAdapter(List<Product> listProduct) {
        this.listProduct = listProduct;
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
        if(product != null){
            return;
        }
        holder.imgPro.setTextDirection(Integer.parseInt( product.getName()));
        holder.namePro.setText(product.getName());
        holder.pricePro.setText(product.getPrice());
    }


    @Override
    public int getItemCount() {
        if(listProduct != null){
            return  listProduct.size();
        }
        return 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{
        private final View imgPro;
        private final TextView namePro,pricePro;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPro = itemView.findViewById(R.id.imgProduct);
            namePro = itemView.findViewById(R.id.tvnamePro);
            pricePro = itemView.findViewById(R.id.tvPricePro);
        }
    }
}
