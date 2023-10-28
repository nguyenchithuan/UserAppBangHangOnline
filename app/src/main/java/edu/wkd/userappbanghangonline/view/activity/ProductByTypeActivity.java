package edu.wkd.userappbanghangonline.view.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.data.api.ApiService;
import edu.wkd.userappbanghangonline.databinding.ActivityProductByTypeBinding;
import edu.wkd.userappbanghangonline.model.obj.Product;
import edu.wkd.userappbanghangonline.model.response.ProductResponse;
import edu.wkd.userappbanghangonline.ultil.CheckConection;
import edu.wkd.userappbanghangonline.ultil.ItemProductInterface;
import edu.wkd.userappbanghangonline.ultil.PaginationScrollListienner;
import edu.wkd.userappbanghangonline.ultil.ProgressDialogLoading;
import edu.wkd.userappbanghangonline.view.adapter.ProductAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductByTypeActivity extends AppCompatActivity implements ItemProductInterface {
    private ActivityProductByTypeBinding binding;
    private int productType;
    private List<Product> listProduct;
    private ProductAdapter productAdapter;
    private GridLayoutManager gridLayoutManager;
    private ProgressDialogLoading dialogLoading;
    private int currentPage = 1;
    private boolean isLoading;
    private boolean isLastPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductByTypeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        onBack();
        getData();
        initUI();
        initController();
        getListProductByType();
        callApiListProductByType(currentPage);
        addScrollViewRcvProductByType();
    }

    private void onBack() {
        binding.arrowBackDetailProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle == null) {
            return;
        }
        productType = bundle.getInt("type", 1);
    }

    private void getListProductByType() {
        listProduct = new ArrayList<>();
        gridLayoutManager = new GridLayoutManager(this,
                2, GridLayoutManager.VERTICAL, false);
        binding.rcvProduct.setLayoutManager(gridLayoutManager);
        binding.rcvProduct.setHasFixedSize(true);
        productAdapter = new ProductAdapter(this, listProduct, this::onClickItemProduct);
        binding.rcvProduct.setAdapter(productAdapter);
    }

    private void callApiListProductByType(int currentPage) {
        dialogLoading.show();
        ApiService.apiService.getProductByType(productType, currentPage).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    listProduct.addAll(productResponse.getResult());
                    if (productResponse.isSuccess()) {
                        productAdapter.setListProduct(listProduct); // set dữ liệu lên rcv
                    } else {
                        CheckConection.ShowToast(ProductByTypeActivity.this, "Load danh sách sản phẩm lỗi!");
                    }
                    dialogLoading.cancel();
                    if(productResponse.getResult().size() < 5) {
                        isLastPage = true;
                    }
                } else {
                    CheckConection.ShowToast(ProductByTypeActivity.this, "Không có dữ liệu trả về");
                    dialogLoading.cancel();
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.d("zzzz", "onResponse-product-error: " + t.toString());
                Toast.makeText(ProductByTypeActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                dialogLoading.cancel();
                isLoading = false;
            }
        });
    }

    private void addScrollViewRcvProductByType() {
        binding.rcvProduct.addOnScrollListener(new PaginationScrollListienner(gridLayoutManager) {
            @Override
            public void loadMoreItems() {
                isLoading = true;
                currentPage ++;
                loadNextPage(currentPage);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });
    }

    private void loadNextPage(int currentPage) {
        callApiListProductByType(currentPage);
    }

    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        int cartSize = intent.getIntExtra("data_cart_size", 0);
                        binding.tvQuantityCart.setText(cartSize + "");
                    }
                }
            });

    @Override
    public void onClickItemProduct(Product product) {
        Intent intent = new Intent(ProductByTypeActivity.this, DetailsProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        intent.putExtras(bundle);
        mActivityResultLauncher.launch(intent);
        overridePendingTransition(R.anim.slidle_in_left, R.anim.slidle_out_left);
    }

    private void initController() {

    }


    private void initUI() {
        dialogLoading = new ProgressDialogLoading(this);
    }
}