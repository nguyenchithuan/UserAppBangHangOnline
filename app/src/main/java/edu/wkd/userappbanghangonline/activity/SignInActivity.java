package edu.wkd.userappbanghangonline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import edu.wkd.userappbanghangonline.databinding.ActivitySignInBinding;


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
                startActivity(new Intent(SignInActivity.this, MainActivity.class));
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