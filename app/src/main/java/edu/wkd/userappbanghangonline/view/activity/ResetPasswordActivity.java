package edu.wkd.userappbanghangonline.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import edu.wkd.userappbanghangonline.databinding.ActivityResetPasswordBinding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class ResetPasswordActivity extends AppCompatActivity {
    private ActivityResetPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        goToSuccessActivity();

    }

    private void goToSuccessActivity() {
        binding.btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPasswordActivity.this, SuccessActivity.class));
            }
        });
    }
}