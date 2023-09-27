package edu.wkd.userappbanghangonline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.wkd.userappbanghangonline.databinding.ActivitySplashBinding;


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