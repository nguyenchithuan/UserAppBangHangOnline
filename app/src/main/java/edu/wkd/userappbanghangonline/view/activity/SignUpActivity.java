package edu.wkd.userappbanghangonline.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.data.api.ApiService;
import edu.wkd.userappbanghangonline.databinding.ActivitySignUpBinding;
import edu.wkd.userappbanghangonline.model.response.LogupUserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                CheckValidate();
            }
        });
    }
    public boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public void CheckValidate(){
        String email = binding.edEmailOrPhoneNumberSignUp.getText().toString().trim();
        if(isValidEmail(email)){
            LogUpUser();
        }else {
            Toast.makeText(this, "Email invalid", Toast.LENGTH_SHORT).show();
        }
    }
    public void LogUpUser(){

        String strEmail = binding.edEmailOrPhoneNumberSignUp.getText().toString().trim();
        String strPassWord = binding.edPasswordSignUp.getText().toString().trim();
        String strRePassWord = binding.edRePasswordSignUp.getText().toString().trim();
        String strUserName = binding.edUserName.getText().toString().trim();
        if(strRePassWord.equals(strPassWord)){
            ApiService.apiService.logUpUser(strEmail, strPassWord, strUserName).enqueue(new Callback<LogupUserResponse>() {
                @Override
                public void onResponse(Call<LogupUserResponse> call, Response<LogupUserResponse> response) {
                    if(response.isSuccessful()){
                        LogupUserResponse response1 = response.body();
                        if(response1.isSuccess()){
                            Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else if (strEmail.equals(response)) {
                            Toast.makeText(SignUpActivity.this, "Email already exists ", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(SignUpActivity.this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LogupUserResponse> call, Throwable t) {

                }
            });

        }else {
            Toast.makeText(this, "Mat khau khong dung! ", Toast.LENGTH_SHORT).show();
        }





    }

    private void onBack() {
        binding.arrowBackSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slidle_in_right, R.anim.slidle_out_right);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slidle_in_right, R.anim.slidle_out_right);
    }
}