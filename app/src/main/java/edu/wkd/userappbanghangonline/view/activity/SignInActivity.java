package edu.wkd.userappbanghangonline.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import edu.wkd.userappbanghangonline.data.api.ApiService;
import edu.wkd.userappbanghangonline.databinding.ActivitySignInBinding;
import edu.wkd.userappbanghangonline.model.response.UserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;


public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onBack();//Quay trở lại sự kiện trước đó
        goToForgotPasswordActivity();//
        goToMainActivity();
    }

    private void goToMainActivity() {
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckValidate();
            }
        });
    }
    public boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public void CheckValidate(){
        String email = binding.edEmailOrPhoneNumberSignIn.getText().toString().trim();
        if(isValidEmail(email)){
            loginUser();
        }else {
            Toast.makeText(this, "Email invalid", Toast.LENGTH_SHORT).show();
        }
    }
    public void loginUser(){
        String strUsername = binding.edEmailOrPhoneNumberSignIn.getText().toString().trim();
        String strPassword = binding.edPasswordSignIn.getText().toString().trim();
        ApiService.apiService.loginUser(strUsername, strPassword).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful()){
                    UserResponse response1 = response.body();

                    if(response1.isSuccess()){
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(SignInActivity.this, "Email or UserName or PassWord invalid", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(SignInActivity.this, "call api err", Toast.LENGTH_SHORT).show();

            }
        });





    }

    private void goToForgotPasswordActivity() {
        binding.tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    private void onBack() {
        binding.arrowBackSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}