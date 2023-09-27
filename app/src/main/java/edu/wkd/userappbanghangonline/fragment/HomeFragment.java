package edu.wkd.userappbanghangonline.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.nio.file.Paths;
import java.util.ArrayList;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.activity.MainActivity;
import edu.wkd.userappbanghangonline.adapter.ProductTypeAdapter;
import edu.wkd.userappbanghangonline.adapter.RecentSearchAdapter;
import edu.wkd.userappbanghangonline.databinding.FragmentHomeBinding;
import edu.wkd.userappbanghangonline.databinding.LayoutDialogSearchBinding;
import edu.wkd.userappbanghangonline.model.ProductType;
import edu.wkd.userappbanghangonline.model.RecentSearch;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ProductTypeAdapter productTypeAdapter;
    private ArrayList<ProductType> listProductType;
    private ArrayList<RecentSearch> listRecentSearch = new ArrayList<>();
    private RecentSearchAdapter recentSearchAdapter;
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
        openSearchDialog();//Mở dialog tìm kiếm
    }

    private void openSearchDialog() {
        binding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutDialogSearchBinding bindingSearch;
                bindingSearch = LayoutDialogSearchBinding.inflate(getLayoutInflater());
                Dialog dialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(bindingSearch.getRoot());

                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                recentSearchAdapter = new RecentSearchAdapter(listRecentSearch);
                LinearLayoutManager managerRecentSearch = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
                bindingSearch.rvRecentSearch.setLayoutManager(managerRecentSearch);
                bindingSearch.rvRecentSearch.setAdapter(recentSearchAdapter);

                bindingSearch.imgSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = bindingSearch.edSearch.getText().toString().trim();
                        addToRecentSearch(title);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                });

                bindingSearch.edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        String title = bindingSearch.edSearch.getText().toString().trim();
                        if (actionId == EditorInfo.IME_ACTION_SEARCH){
                            addToRecentSearch(title);
                            hideKeyboard(bindingSearch.getRoot());
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        }
                        return false;
                    }
                });



                bindingSearch.arrowBackSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }
    //Ẩn bàn phím
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //Thêm tìm kiếm gần đây
    private void addToRecentSearch(String title) {
        boolean isExists = false;
        for(RecentSearch search : listRecentSearch){
            if (search.getTitle().equals(title)){
                isExists = true;
                break;
            }
        }
        if (isExists==false){
            listRecentSearch.add(new RecentSearch(title));
            recentSearchAdapter.setData(listRecentSearch);
        }
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