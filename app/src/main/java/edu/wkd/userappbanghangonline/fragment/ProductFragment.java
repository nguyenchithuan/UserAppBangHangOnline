package edu.wkd.userappbanghangonline.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.activity.DetailsProductActivity;
import edu.wkd.userappbanghangonline.adapter.ProductAdapter;
import edu.wkd.userappbanghangonline.adapter.ProductTypeAdapter;
import edu.wkd.userappbanghangonline.api.ApiService;
import edu.wkd.userappbanghangonline.databinding.FragmentProductBinding;
import edu.wkd.userappbanghangonline.model.obj.Product;
import edu.wkd.userappbanghangonline.model.obj.ProductType;
import edu.wkd.userappbanghangonline.model.response.ProductResponse;

import edu.wkd.userappbanghangonline.ultil.CheckConection;
import edu.wkd.userappbanghangonline.ultil.UrlSomething;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductFragment extends Fragment {
   private RecyclerView recyclerView;
    private FragmentProductBinding binding;
    private ProductTypeAdapter productTypeAdapter;
    private ArrayList<ProductType> listProductType;
    private List<Product> listProduct;
    private List<Product> listProductSearch;
    private ProductAdapter productAdapter;
    private ProgressDialog progressDialog;
    public ProductFragment() {
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
        settingPDialog();
        autoImageSlide();//Tạo ảnh chạy tự động
        getListProductType();
        getListProduct();
        callApiGetUsers();
        searchProduct();
    }

    private void searchProduct() {
        binding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //trước khi text thay đổi
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //trong khi text thay đổi
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Sau khi text thay đổi
                getDataSearch();
            }
        });
    }

    private void getDataSearch() {
        showPDialog();
        String product_name = binding.edSearch.getText().toString().trim();
        if (product_name != null) { //nếu có dữ liệu mới search
            ApiService.apiService.getProductSearch(product_name).enqueue(
                    new Callback<ProductResponse>() {
                        @Override
                        public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                            if (response.isSuccessful()){
                                ProductResponse productResponse = response.body();
                                if (productResponse.isSuccess()){
                                    productAdapter.setListProduct(productResponse.getResult()); // set dữ liệu lên rcv
                                    hidePDialog();
                                }else {
                                    CheckConection.ShowToast(getContext(), "Không tìm thấy sản phẩm");
                                    hidePDialog();
                                }
                            }else {
                                hidePDialog();
                            }
                        }

                        @Override
                        public void onFailure(Call<ProductResponse> call, Throwable t) {

                        }
                    }
            );
        }else { // không thì trả về list sản phẩm ban đầu
            getListProduct();
        }
    }

    private void callApiGetUsers(){
        showPDialog();
        ApiService.apiService.getListProduct().enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()){
                    ProductResponse productResponse = response.body();
                    if (productResponse.isSuccess()){
                        productAdapter.setListProduct(productResponse.getResult()); // set dữ liệu lên rcv
                        hidePDialog();
                    }else {
                        CheckConection.ShowToast(getContext(), "Load danh sách sản phẩm lỗi!");
                    }
                }else {
                    CheckConection.ShowToast(getContext(), "Không có dữ liệu trả về");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.d("zzzz", "onResponse-product-error: " + t.toString());
            }
        });
    }
    private void getListProductType() {
        listProductType = new ArrayList<>();
        String []arrTypeName = {"Ốp lưng","Kính cường lực","Sticker","Tai nghe",
                "Tay cầm chơi game","Giá đỡ điện thoại"};
        for (int i = 0; i < arrTypeName.length ; i++) {
            listProductType.add(new ProductType(i, arrTypeName[i], UrlSomething.urlImage[i]));
        }
        productTypeAdapter = new ProductTypeAdapter(listProductType);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),
                3, GridLayoutManager.VERTICAL, false);
        binding.rvTypeProduct.setLayoutManager(gridLayoutManager);
        binding.rvTypeProduct.setHasFixedSize(true);
        binding.rvTypeProduct.setAdapter(productTypeAdapter);
    }

    private void getListProduct() {
        listProduct = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),
                2, GridLayoutManager.VERTICAL, false);
        binding.rcvProduct.setLayoutManager(gridLayoutManager);
        binding.rcvProduct.setHasFixedSize(true);
        productAdapter = new ProductAdapter(getActivity(), listProduct);
        binding.rcvProduct.setAdapter(productAdapter);
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

    private void hidePDialog(){
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    private void settingPDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }

    private void showPDialog() {
        if (!progressDialog.isShowing()){
            progressDialog.show();
        }
    }
}