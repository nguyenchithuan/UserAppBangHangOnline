package edu.wkd.userappbanghangonline.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.databinding.ActivityProductReviewsActivityBinding;

public class ProductReviewsActivity extends AppCompatActivity {
    private ActivityProductReviewsActivityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductReviewsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}