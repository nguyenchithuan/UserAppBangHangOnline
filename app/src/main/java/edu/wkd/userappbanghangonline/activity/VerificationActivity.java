package edu.wkd.userappbanghangonline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import edu.wkd.userappbanghangonline.databinding.ActivityVerificationBinding;


public class VerificationActivity extends AppCompatActivity {
    private ActivityVerificationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        goToResetPasswordActivity();
    }

    private void goToResetPasswordActivity() {
        binding.btnVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VerificationActivity.this, ResetPasswordActivity.class));
            }
        });
    }
}