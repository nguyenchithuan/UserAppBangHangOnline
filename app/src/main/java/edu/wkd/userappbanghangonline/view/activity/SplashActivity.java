package edu.wkd.userappbanghangonline.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import edu.wkd.userappbanghangonline.databinding.ActivitySplashBinding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Xử lí sự kiện khi nhấn nút
        setOnItemClick();
    }

    private void setOnItemClick() {
        binding.btnGoToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, SignInActivity.class));
            }
        });

        binding.tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, SignUpActivity.class));
            }
        });
    }

}