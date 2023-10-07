package edu.wkd.userappbanghangonline.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        goToPolicyAndPrivacyActivity();//Chuyển tới trang điều khoản và dịch vụ
        onBack();//Quay trở lại sự kiện trước đó
        goToSignInActivity();//Chuyển đến màn hình đăng nhập
    }

    private void goToSignInActivity() {
        binding.tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });
    }

    private void onBack() {
        binding.arrowBackSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void goToPolicyAndPrivacyActivity() {
        String fullText = getString(R.string.text_policy_and_privacy);
        SpannableString spannedString = new SpannableString(fullText);
        // Tạo một ClickableSpan để xử lý sự kiện khi phần từ "chính sách bảo mật" được nhấn
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(SignUpActivity.this, "Chuyển tới trang điều khoản và dịch vụ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                // Cấu hình các thuộc tính của phần từ được nhấn
                ds.setUnderlineText(false);// Loại bỏ gạch chân

                // Đặt màu văn bản cho phần từ
                int customColor = getColor(R.color.btn_yellow);
                ds.setColor(customColor);
            }
        };
        // Tìm vị trí của từ "chính sách bảo mật" trong chuỗi đầy đủ
        int startIndex = fullText.indexOf("chính sách bảo mật");
        // Áp dụng ClickableSpan vào phần từ "chính sách bảo mật" trong SpannableString
        spannedString.setSpan(clickableSpan, startIndex, startIndex + "chính sách bảo mật".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Đặt SpannableString làm văn bản cho TextView
        binding.tvPolicyAndPrivacy.setText(spannedString);
        binding.tvPolicyAndPrivacy.setMovementMethod(LinkMovementMethod.getInstance());// Đảm bảo TextView có thể nhận sự kiện click
    }
}