package edu.wkd.userappbanghangonline.view.fragment;

import android.app.Dialog;
import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
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

import java.util.ArrayList;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.data.api.ApiService;
import edu.wkd.userappbanghangonline.databinding.FragmentHomeBinding;
import edu.wkd.userappbanghangonline.databinding.LayoutDialogSearchBinding;
import edu.wkd.userappbanghangonline.model.response.ProductTypeResponse;
import edu.wkd.userappbanghangonline.ultil.CheckConection;
import edu.wkd.userappbanghangonline.ultil.ItemProductTypeInterface;
import edu.wkd.userappbanghangonline.ultil.ProgressDialogLoading;
import edu.wkd.userappbanghangonline.view.activity.CartActivity;
import edu.wkd.userappbanghangonline.view.activity.ChatActivity;
import edu.wkd.userappbanghangonline.view.activity.MainActivity;
import edu.wkd.userappbanghangonline.view.activity.ProductByTypeActivity;
import edu.wkd.userappbanghangonline.view.adapter.ProductTypeAdapter;
import edu.wkd.userappbanghangonline.view.adapter.RecentSearchAdapter;
import edu.wkd.userappbanghangonline.model.obj.ProductType;
import edu.wkd.userappbanghangonline.model.obj.RecentSearch;
import edu.wkd.userappbanghangonline.ultil.UrlSomething;
import edu.wkd.userappbanghangonline.ultil.CartUltil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements ItemProductTypeInterface {

    private FragmentHomeBinding binding;
    private ProductTypeAdapter productTypeAdapter;
    private ArrayList<ProductType> listProductType;
    private ArrayList<RecentSearch> listRecentSearch = new ArrayList<>();
    private ProgressDialogLoading dialogLoading;
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
        initUI();
        autoImageSlide();//Tạo ảnh chạy tự động
        getListProductType();//Lấy danh sách loại sản phẩm
        callApiGetTypeProduct();
        openSearchDialog();//Mở dialog tìm kiếm
        eventBtnCart();
        initOnclick();

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
        binding.rvTypeProductHome.setLayoutManager(gridLayoutManager);
        binding.rvTypeProductHome.setHasFixedSize(true);
        productTypeAdapter = new ProductTypeAdapter(getActivity(), listProductType, this::onClickItemProductType);
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

    private void initOnclick() {
        binding.imgChat.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            startActivity(intent);
        });
    }

    private void initUI() {
        dialogLoading = new ProgressDialogLoading(getActivity());
    }

    @Override
    public void onClickItemProductType(ProductType producttype) {
        Intent intent = new Intent(getActivity(), ProductByTypeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type", producttype.getId());
        intent.putExtras(bundle);
        mActivityResultLauncher.launch(intent);
    }
}