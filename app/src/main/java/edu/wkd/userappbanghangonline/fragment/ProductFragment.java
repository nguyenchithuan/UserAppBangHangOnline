package edu.wkd.userappbanghangonline.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.databinding.FragmentProductBinding;


public class ProductFragment extends Fragment {
    private FragmentProductBinding binding;
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