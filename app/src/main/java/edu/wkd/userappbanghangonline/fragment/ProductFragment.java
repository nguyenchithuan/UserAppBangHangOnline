package edu.wkd.userappbanghangonline.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.adapter.ProductTypeAdapter;
import edu.wkd.userappbanghangonline.databinding.FragmentProductBinding;
import edu.wkd.userappbanghangonline.model.ProductType;


public class ProductFragment extends Fragment {
    private FragmentProductBinding binding;
    private ProductTypeAdapter productTypeAdapter;
    private ArrayList<ProductType> listProductType;
    public ProductFragment() {
        // Required empty public constructor
    }

    public static ProductFragment newInstance() {
        ProductFragment fragment = new ProductFragment();
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
        binding = FragmentProductBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        autoImageSlide();//Tạo ảnh chạy tự động
        getListProductType();
    }


    private void getListProductType() {
        listProductType = new ArrayList<>();
        listProductType.add(new ProductType(0, "Ốp lưng", "https://hatocase.com/wp-content/uploads/2022/02/in-op-lung-dien-thoai-thoi-thuong-voi-mau-veri-peri-02.jpg"));
        listProductType.add(new ProductType(1, "Kính cường lực", "https://tse2.mm.bing.net/th?id=OIP.sjeJse6u86yJmODzoc0J2gHaHa&pid=Api&P=0&h=180"));
        listProductType.add(new ProductType(2, "Sticker", "https://salt.tikicdn.com/cache/w1200/ts/product/fa/0d/96/e08bd94a1efb1d7170df8e7e109d5ad5.jpg"));
        listProductType.add(new ProductType(3, "Tai nghe", "https://tse4.mm.bing.net/th?id=OIP.lH8Le3XGQfjQo-RtmibylQHaGj&pid=Api&P=0&h=180"));
        listProductType.add(new ProductType(4, "Tay cầm chơi game", "https://tse2.mm.bing.net/th?id=OIP._3yNHrRW8OJaJiUb4D-jWwHaHo&pid=Api&P=0&h=180"));
        listProductType.add(new ProductType(5, "Giá đỡ điện thoại", "https://salt.tikicdn.com/ts/tmp/0c/b5/05/2a301b541199e2fd06c85608c6bfb9ef.jpg"));
        productTypeAdapter = new ProductTypeAdapter(listProductType);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        binding.rvTypeProduct.setLayoutManager(gridLayoutManager);
        binding.rvTypeProduct.setHasFixedSize(true);
        binding.rvTypeProduct.setAdapter(productTypeAdapter);
    }

    private void autoImageSlide() {
        ArrayList<SlideModel> listBanner = new ArrayList<>();
        listBanner.add(new SlideModel(R.drawable.banner7, ScaleTypes.FIT));
        listBanner.add(new SlideModel(R.drawable.banner8, ScaleTypes.FIT));
        listBanner.add(new SlideModel(R.drawable.banner9, ScaleTypes.FIT));
        listBanner.add(new SlideModel(R.drawable.banner10, ScaleTypes.FIT));
        listBanner.add(new SlideModel(R.drawable.banner11, ScaleTypes.FIT));
        listBanner.add(new SlideModel(R.drawable.banner12, ScaleTypes.FIT));
        binding.imageSlider2.setImageList(listBanner);
    }
}