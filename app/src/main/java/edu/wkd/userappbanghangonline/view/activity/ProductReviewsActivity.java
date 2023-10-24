package edu.wkd.userappbanghangonline.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RatingBar;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.databinding.ActivityProductReviewsActivityBinding;

public class ProductReviewsActivity extends AppCompatActivity {
    private ActivityProductReviewsActivityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductReviewsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitleWhenChooseStar();//Hiển thị tiêu đề phù hợp với từng sao được chọn
    }

    private void setTitleWhenChooseStar() {
        binding.rattingBarProductReviews.setRating(5);
        binding.rattingBarProductReviews.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                switch ((int) rating){
                    case 1:
                        binding.tvTitleByRating.setText("Tệ");
                        break;
                    case 2:
                        binding.tvTitleByRating.setText("Không hài lòng");
                        break;
                    case 3:
                        binding.tvTitleByRating.setText("Bình thường");
                        break;
                    case 4:
                        binding.tvTitleByRating.setText("Hài lòng");
                        break;
                    case 5:
                        binding.tvTitleByRating.setText("Tuyệt vời");
                        break;
                }
            }
        });
    }
}