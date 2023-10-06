package edu.wkd.userappbanghangonline.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import edu.wkd.userappbanghangonline.databinding.ActivityForgotPasswordBinding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class ForgotPasswordActivity extends AppCompatActivity {
    private ActivityForgotPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        goToVerificationActivity();
    }

    private void goToVerificationActivity() {
        binding.btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, VerificationActivity.class));
            }
        });
    }
}