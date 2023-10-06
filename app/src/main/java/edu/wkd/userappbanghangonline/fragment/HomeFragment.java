package edu.wkd.userappbanghangonline.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.adapter.ProductTypeAdapter;
import edu.wkd.userappbanghangonline.databinding.FragmentHomeBinding;
import edu.wkd.userappbanghangonline.model.obj.ProductType;
import edu.wkd.userappbanghangonline.ultil.UrlSomething;



public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ProductTypeAdapter productTypeAdapter;
    private ArrayList<ProductType> listProductType;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        autoImageSlide();//Tạo ảnh chạy tự động
        getListProductType();//Lấy danh sách loại sản phẩm
    }


    //Ẩn bàn phím
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void getListProductType() {
        listProductType = new ArrayList<>();
        String []arrTypeName = {"Ốp lưng","Kính cường lực","Sticker","Tai nghe",
                "Tay cầm chơi game","Giá đỡ điện thoại"};
        for (int i = 0; i < arrTypeName.length ; i++) {
            listProductType.add(new ProductType(i, arrTypeName[i], UrlSomething.urlImage[i]));
        }
        productTypeAdapter = new ProductTypeAdapter(listProductType);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3,
                GridLayoutManager.VERTICAL, false);
        binding.rvTypeProductHome.setLayoutManager(gridLayoutManager);
        binding.rvTypeProductHome.setHasFixedSize(true);
        binding.rvTypeProductHome.setAdapter(productTypeAdapter);
    }

    private void autoImageSlide() {
        ArrayList<SlideModel> listBanner = new ArrayList<>();
        listBanner.add(new SlideModel(R.drawable.banner1, ScaleTypes.FIT));
        listBanner.add(new SlideModel(R.drawable.banner2, ScaleTypes.FIT));
        listBanner.add(new SlideModel(R.drawable.banner3, ScaleTypes.FIT));
        listBanner.add(new SlideModel(R.drawable.banner4, ScaleTypes.FIT));
        listBanner.add(new SlideModel(R.drawable.banner5, ScaleTypes.FIT));
        listBanner.add(new SlideModel(R.drawable.banner6, ScaleTypes.FIT));
        binding.imageSlider.setImageList(listBanner);
    }
}