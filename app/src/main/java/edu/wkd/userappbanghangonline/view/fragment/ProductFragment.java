package edu.wkd.userappbanghangonline.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.databinding.FragmentProductBinding;
import edu.wkd.userappbanghangonline.model.response.ProductTypeResponse;
import edu.wkd.userappbanghangonline.ultil.ItemProductTypeInterface;
import edu.wkd.userappbanghangonline.view.activity.CartActivity;
import edu.wkd.userappbanghangonline.view.activity.DetailsProductActivity;
import edu.wkd.userappbanghangonline.view.adapter.ProductAdapter;
import edu.wkd.userappbanghangonline.view.adapter.ProductTypeAdapter;
import edu.wkd.userappbanghangonline.data.api.ApiService;
import edu.wkd.userappbanghangonline.model.obj.Product;
import edu.wkd.userappbanghangonline.model.obj.ProductType;
import edu.wkd.userappbanghangonline.model.response.ProductResponse;
import edu.wkd.userappbanghangonline.ultil.CheckConection;
import edu.wkd.userappbanghangonline.ultil.UrlSomething;
import edu.wkd.userappbanghangonline.ultil.CartUltil;
import edu.wkd.userappbanghangonline.ultil.ItemProductInterface;
import edu.wkd.userappbanghangonline.ultil.ProgressDialogLoading;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductFragment extends Fragment implements ItemProductInterface, ItemProductTypeInterface {
   private RecyclerView recyclerView;
    private FragmentProductBinding binding;
    private ProductTypeAdapter productTypeAdapter;
    private List<ProductType> listProductType;
    private List<Product> listProduct;
    private List<Product> listProductSearch;
    private ProductAdapter productAdapter;
    private ProgressDialogLoading dialogLoading;
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
        initProduct();
        autoImageSlide();//Tạo ảnh chạy tự động
        getListProductType();
        getListProduct();
        callApiGetUsers();
        callApiGetTypeProduct();
        searchProduct();
        eventBtnCart();
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
        dialogLoading.show();
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
                                    dialogLoading.cancel();
                                }else {
                                    CheckConection.ShowToast(getContext(), "Không tìm thấy sản phẩm");
                                    dialogLoading.cancel();
                                }
                            }else {
                                dialogLoading.cancel();
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
        dialogLoading.show();
        ApiService.apiService.getListProduct().enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    if (productResponse.isSuccess()) {
                        productAdapter.setListProduct(productResponse.getResult()); // set dữ liệu lên rcv
                        dialogLoading.cancel();
                    } else {
                        CheckConection.ShowToast(getContext(), "Load danh sách sản phẩm lỗi!");
                    }
                } else {
                    CheckConection.ShowToast(getContext(), "Không có dữ liệu trả về");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.d("zzzz", "onResponse-product-error: " + t.toString());
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
                dialogLoading.cancel();
            }
        });
    }
    private void callApiGetTypeProduct(){
        dialogLoading.show();
        ApiService.apiService.getListTypeProduct().enqueue(new Callback<ProductTypeResponse>() {
            @Override
            public void onResponse(Call<ProductTypeResponse> call, Response<ProductTypeResponse> response) {
                if (response.isSuccessful()) {
                    ProductTypeResponse productTypeResponse = response.body();
                    if (productTypeResponse.isSuccess()) {
                        productTypeAdapter.setListTypeProduct(productTypeResponse.getResult());
                        dialogLoading.cancel();
                    } else {
                        CheckConection.ShowToast(getContext(), "Load danh sách sản phẩm lỗi!");
                    }
                } else {
                    CheckConection.ShowToast(getContext(), "Không có dữ liệu trả về");
                }
            }

            @Override
            public void onFailure(Call<ProductTypeResponse> call, Throwable t) {
                Log.d("zzzz", "onResponse-product-error: " + t.toString());
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
                dialogLoading.cancel();
            }
        });
    }
    private void getListProductType() {
        listProductType = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),
                3, GridLayoutManager.VERTICAL, false);
        binding.rvTypeProduct.setLayoutManager(gridLayoutManager);
        binding.rvTypeProduct.setHasFixedSize(true);
        productTypeAdapter = new ProductTypeAdapter(getActivity(), listProductType, this);
        binding.rvTypeProduct.setAdapter(productTypeAdapter);
    }

    private void getListProduct() {
        listProduct = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),
                2, GridLayoutManager.VERTICAL, false);
        binding.rcvProduct.setLayoutManager(gridLayoutManager);
        binding.rcvProduct.setHasFixedSize(true);
        productAdapter = new ProductAdapter(getActivity(), listProduct, this::onClickItemProduct);
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

    private void initProduct() {
        dialogLoading = new ProgressDialogLoading(getActivity());
    }

    // Hiển thị số lượng sản phẩm trong giỏ hàng khi back lại
    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == getActivity().RESULT_OK) {
                        Intent intent = result.getData();
                        int cartSize = intent.getIntExtra("data_cart_size", 0);
                        binding.tvQuantityCart.setText(cartSize + "");
                    }
                }
            });

    private void eventBtnCart() {
        binding.tvQuantityCart.setText(CartUltil.listCart.size() + "");

        binding.imgCart.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), CartActivity.class);
            mActivityResultLauncher.launch(intent);
        });
    }

    @Override
    public void onClickItemProduct(Product product) {
        Intent intent = new Intent(getActivity(), DetailsProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        intent.putExtras(bundle);
        mActivityResultLauncher.launch(intent);
        getActivity().overridePendingTransition(R.anim.slidle_in_left, R.anim.slidle_out_left);
    }

    @Override
    public void onClickItemProductType(ProductType producttype) {

    }
}