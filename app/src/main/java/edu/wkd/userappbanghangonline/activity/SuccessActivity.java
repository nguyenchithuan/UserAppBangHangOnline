package edu.wkd.userappbanghangonline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import edu.wkd.userappbanghangonline.databinding.ActivitySuccessBinding;


public class SuccessActivity extends AppCompatActivity {
    private ActivitySuccessBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        goToSignInActivity();
    }

    private void goToSignInActivity() {
        binding.btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SuccessActivity.this, SignInActivity.class));
            }
        });
    }
}