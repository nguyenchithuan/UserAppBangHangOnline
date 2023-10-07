package edu.wkd.userappbanghangonline.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.data.api.ApiService;
import edu.wkd.userappbanghangonline.databinding.ActivityCartBinding;
import edu.wkd.userappbanghangonline.databinding.ActivityChangePasswordBinding;
import edu.wkd.userappbanghangonline.model.obj.User;
import edu.wkd.userappbanghangonline.model.response.ServerResponse;
import edu.wkd.userappbanghangonline.model.response.UserResponse;
import edu.wkd.userappbanghangonline.ultil.CheckConection;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding binding;
    private String old_pass_to_server;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        settingPDialog();
        showPDialog();
        ApiService.apiService.getUser(1).enqueue(
                new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.isSuccessful()){
                            UserResponse userResponse = response.body();
                            if (userResponse.isSuccess()){
                                Log.d("userResponse", "Lay du lieu thanh cong!");
                                User user = userResponse.getResult().get(0);
                                old_pass_to_server = user.getPassword();
                                hidePDialog();
                            }else {
                                Log.d("userResponse", "Lay du lieu that bai!");
                            }
                        }else
                            CheckConection.ShowToast(ChangePasswordActivity.this,
                                    "Lỗi khi lấy dữ liệu");
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {

                    }
                }
        );

        binding.btnSave.setOnClickListener(v -> {
            String old_pass = binding.edOldPassword.getText().toString().trim();
            String new_pass = binding.edNewPassword.getText().toString().trim();
            String re_pass = binding.edRePassword.getText().toString().trim();
            if (!old_pass.isEmpty()){
                if (old_pass.equals(old_pass_to_server)){
                    if (!new_pass.isEmpty()){
                        if (!re_pass.isEmpty()){
                            if (re_pass.equals(new_pass)){
                                showPDialog();
                                ApiService.apiService.updatePassword(new_pass,1)
                                        .enqueue(new Callback<ServerResponse>() {
                                            @Override
                                            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                                ServerResponse serverResponse = response.body();
                                                if (serverResponse.getSuccess()){
                                                    CheckConection.ShowToast(ChangePasswordActivity.this,
                                                            "Cập nhật mật khẩu thành công");
                                                    hidePDialog();
                                                    startActivity(new Intent(ChangePasswordActivity.this, MainActivity.class));
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ServerResponse> call, Throwable t) {

                                            }
                                        });
                            }else {
                                binding.edRePassword.setError("Nhập lại chưa đúng");
                            }
                        }else {
                            binding.edRePassword.setError("Không để trống");
                        }
                    }else {
                        binding.edNewPassword.setError("Không để trống");
                    }
                }else {
                    binding.edOldPassword.setError("Mật khẩu cũ sai");
                }
            }else {
                binding.edOldPassword.setError("Không để trống");
            }

        });
    }

    private void hidePDialog(){
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    private void settingPDialog() {
        progressDialog = new ProgressDialog(ChangePasswordActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }

    private void showPDialog() {
        if (!progressDialog.isShowing()){
            progressDialog.show();
        }
    }
}