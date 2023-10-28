package edu.wkd.userappbanghangonline.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.databinding.ActivityPayBinding;
import edu.wkd.userappbanghangonline.ultil.UserUltil;
import edu.wkd.userappbanghangonline.view.adapter.PayAdapter;
import edu.wkd.userappbanghangonline.data.api.ApiService;
import edu.wkd.userappbanghangonline.model.response.ServerResponse;
import edu.wkd.userappbanghangonline.ultil.CartUltil;
import edu.wkd.userappbanghangonline.ultil.ProgressDialogLoading;
import retrofit2.Call;
import retrofit2.Callback;

public class PayActivity extends AppCompatActivity {
    private ActivityPayBinding binding;
    private int totalPrice;
    private PayAdapter payAdapter;
    private ProgressDialogLoading dialogLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initPay();
        setDataPay();
        onBack();

        binding.btnOrder.setOnClickListener(view -> {
            if(validate()) {
                dialogLoading.show();
                postOrderUser();
            }
        });
    }


    private boolean validate() {
        if(TextUtils.isEmpty(binding.edAddress.getText().toString())
                && TextUtils.isEmpty(binding.edPhoneNumber.getText().toString())) {
            Toast.makeText(this, "Mời nhập đầy đủ dữ liệu", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void postOrderUser() {
        int userId = UserUltil.user.getId();
        String address = binding.edAddress.getText().toString();
        String phoneNumber = binding.edPhoneNumber.getText().toString();
        int quantity = CartUltil.listBuyCart.size();
        int status = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String datetime = sdf.format(Calendar.getInstance().getTime());
        String detail = getDataOrderDetail();
        ApiService.apiService.postOrderUser(userId, address, phoneNumber, quantity, totalPrice, status, datetime, detail,0).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse res = response.body();
                removeListBuyCart();
                Toast.makeText(PayActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PayActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
                dialogLoading.cancel();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d("zzzzz", "onResponse-error: " + t.toString());
                Toast.makeText(PayActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                dialogLoading.cancel();
            }
        });
    }

    private void removeListBuyCart() {
        CartUltil.listCart.removeAll(CartUltil.listBuyCart);
    }

    private String getDataOrderDetail() { // chuyển thành json của order detail
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < CartUltil.listBuyCart.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("product_id", CartUltil.listBuyCart.get(i).getIdProduct());
                jsonObject.put("quantity", CartUltil.listBuyCart.get(i).getQuantity());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }

    private void setDataPay() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        totalPrice = bundle.getInt("totalPrice");
        DecimalFormat df = new DecimalFormat("###,###,###");
        binding.tvTotalPrice.setText(df.format(totalPrice) + " đ");
        binding.tvTotalPriceProduct.setText(df.format(totalPrice) + " đ");
        setDataRcv();
    }

    private void setDataRcv() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rcvPay.setLayoutManager(linearLayoutManager);
        Log.d("zzzz", "CartUtil.listCart: " + CartUltil.listBuyCart);
        payAdapter = new PayAdapter(this, CartUltil.listBuyCart);
        binding.rcvPay.setHasFixedSize(true);
        binding.rcvPay.setAdapter(payAdapter);
    }

    private void initPay() {
        dialogLoading = new ProgressDialogLoading(this);
    }

    private void onBack() {
        binding.arrowBackDetailProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slidle_in_right, R.anim.slidle_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slidle_in_right, R.anim.slidle_out_right);
    }
}