package edu.wkd.userappbanghangonline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.wkd.userappbanghangonline.adapter.PayAdapter;
import edu.wkd.userappbanghangonline.api.ApiService;
import edu.wkd.userappbanghangonline.databinding.ActivityPayBinding;
import edu.wkd.userappbanghangonline.model.response.Response;
import edu.wkd.userappbanghangonline.ultil.CartUtil;
import edu.wkd.userappbanghangonline.ultil.ProgressDialogLoading;
import okhttp3.internal.Util;
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
        int userId = 1;
        String address = binding.edAddress.getText().toString();
        String phoneNumber = binding.edPhoneNumber.getText().toString();
        int quantity = CartUtil.listBuyCart.size();
        int status = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String datetime = sdf.format(Calendar.getInstance().getTime());
        String detail = getDataOrderDetail();
        ApiService.apiService.postOrderUser(userId, address, phoneNumber, quantity, totalPrice, status, datetime, detail).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Response res = response.body();
                Log.d("zzzzz", "onResponse: " + res.toString());
                Toast.makeText(PayActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PayActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
                dialogLoading.cancel();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d("zzzzz", "onResponse-error: " + t.toString());
                Toast.makeText(PayActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                dialogLoading.cancel();
            }
        });
    }

    private String getDataOrderDetail() { // chuyển thành json của order detail
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < CartUtil.listBuyCart.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("product_id", CartUtil.listBuyCart.get(i).getIdProduct());
                jsonObject.put("quantity", CartUtil.listBuyCart.get(i).getQuantity());
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
        Log.d("zzzz", "CartUtil.listCart: " + CartUtil.listBuyCart);
        payAdapter = new PayAdapter(this, CartUtil.listBuyCart);
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
            }
        });
    }
}