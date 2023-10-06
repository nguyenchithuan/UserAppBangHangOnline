package edu.wkd.userappbanghangonline.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import edu.wkd.userappbanghangonline.databinding.ActivityVerificationBinding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


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