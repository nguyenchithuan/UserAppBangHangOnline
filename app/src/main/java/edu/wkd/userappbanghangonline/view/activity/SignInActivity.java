package edu.wkd.userappbanghangonline.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.data.api.ApiService;
import edu.wkd.userappbanghangonline.databinding.ActivitySignInBinding;
import edu.wkd.userappbanghangonline.model.response.UserResponse;
import edu.wkd.userappbanghangonline.ultil.ProgressDialogLoading;
import edu.wkd.userappbanghangonline.ultil.UserUltil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private ProgressDialogLoading loading;
    FirebaseAuth firebaseAuth;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        dialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        onBack();//Quay trở lại sự kiện trước đó
        goToForgotPasswordActivity();//
        goToMainActivity();


    }

    private void goToMainActivity() {
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strUsername = binding.edEmailOrPhoneNumberSignIn.getText().toString().trim();
                String strPassword = binding.edPasswordSignIn.getText().toString().trim();
                firebaseAuth.signInWithEmailAndPassword(strUsername, strPassword)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        dialog.cancel();
                                        Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                dialog.cancel();
                                                Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
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
            loading.show();
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
                        UserUltil.user = response1.getResult().get(0);
                        Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Log.d("zzzz", "onResponse: " + UserUltil.user);
                    }else {
                        Toast.makeText(SignInActivity.this, "Email or UserName or PassWord invalid", Toast.LENGTH_SHORT).show();
                    }
                    loading.cancel();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(SignInActivity.this, "call api err", Toast.LENGTH_SHORT).show();
                loading.cancel();
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
                overridePendingTransition(R.anim.slidle_in_right, R.anim.slidle_out_right);
            }
        });
    }

    private void initView() {
        loading = new ProgressDialogLoading(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slidle_in_right, R.anim.slidle_out_right);
    }
}