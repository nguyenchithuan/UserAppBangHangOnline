package edu.wkd.userappbanghangonline.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;

import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.data.api.ApiService;
import edu.wkd.userappbanghangonline.databinding.ActivitySettingAccountBinding;
import edu.wkd.userappbanghangonline.model.obj.User;
import edu.wkd.userappbanghangonline.model.response.ServerResponse;
import edu.wkd.userappbanghangonline.model.response.UserResponse;
import edu.wkd.userappbanghangonline.ultil.CheckConection;
import edu.wkd.userappbanghangonline.ultil.UserUltil;
import edu.wkd.userappbanghangonline.ultil.Validator;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingAccountActivity extends AppCompatActivity {

    private ActivitySettingAccountBinding binding;
    private ProgressDialog progressDialog;
    private Call<ResponseBody> call;
    private String mediaPath;
    private int user_id = UserUltil.user.getId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        settingPDialog();
        getInfoUser(); // lấy thông tin của user
        binding.tvChangeBirthday.setVisibility(View.GONE);
        binding.tvChangeEmail.setVisibility(View.GONE);
        binding.tvChangeUsername.setVisibility(View.GONE);
        binding.tvChangePhone.setVisibility(View.GONE);

        binding.arrowBackSettings.setOnClickListener(v -> {
            finish();
        });

        binding.tvChangeUsername.setOnClickListener(v -> {
            String userName = binding.inputChangeUsername.getText().toString().trim();
            updateProfile(userName, "userName");
        });

        binding.tvChangePhone.setOnClickListener(v -> {
            String phone = binding.inputChangePhone.getText().toString().trim();
            updateProfile(phone, "phone");
        });

        binding.tvChangeEmail.setOnClickListener(v -> {
            String email = binding.inputChangeEmail.getText().toString().trim();
            updateProfile(email,"email");
        });

        binding.inputChangeBirthday.setOnClickListener(v -> {
            showDatePickerDialog();
        });

        binding.tvChangeBirthday.setOnClickListener(v -> {
            String birthday = binding.inputChangeBirthday.getText().toString().trim();
            updateProfile(birthday,"birthday");
        });

        binding.imgChangeAvatar.setOnClickListener(v -> {
            changeAvatar();
        });

    }

    private void getInfoUser() {
        showPDialog();
        ApiService.apiService.getUser(user_id).enqueue(
                new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.isSuccessful()){
                            UserResponse userResponse = response.body();
                            if (userResponse.isSuccess()){
                                Log.d("userResponse", "Lay du lieu thanh cong!");
                                User user = userResponse.getResult().get(0);

                                setInfoUsertoUi(user.getUsername(),user.getPhone(),
                                        user.getEmail(),user.getBirthday(),user.getAvatar());
                                hidePDialog();
                            }else {
                                Log.d("userResponse", "Lay du lieu that bai!");
                            }
                        }else
                            CheckConection.ShowToast(SettingAccountActivity.this,
                                    "Lỗi khi lấy dữ liệu");
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {

                    }
                }
        );
    }

    private void setInfoUsertoUi(String username, String phone, String email, String birthday, String avatar) {
        binding.inputChangeUsername.setText(username);
        binding.inputChangePhone.setText(phone);
        binding.inputChangeEmail.setText(email);
        binding.inputChangeBirthday.setText(birthday);
        Glide.with(SettingAccountActivity.this)
                .load(ApiService.URL_MAIN + avatar)
                .error(R.drawable.baseline_person_24)
                .into(binding.imgChangeAvatar);

        binding.inputChangeUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String in_username = binding.inputChangeUsername.getText().toString().trim();
                if (in_username.equals(username)){
                    binding.tvChangeUsername.setVisibility(View.GONE);
                }else {
                    binding.tvChangeUsername.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.tvChangePhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String in_phone = binding.inputChangePhone.getText().toString().trim();
                if (in_phone.equals(phone)){
                    binding.tvChangePhone.setVisibility(View.GONE);
                }else {
                    binding.tvChangePhone.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.inputChangeEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String in_email = binding.inputChangeEmail.getText().toString().trim();
                if (email.equals(in_email)){
                    binding.tvChangeEmail.setVisibility(View.GONE);
                }else {
                    binding.tvChangeEmail.setVisibility(View.VISIBLE);
                }

                if (!Validator.isValidEmail(in_email)){
                    binding.inputChangeEmail.setError("Email không đúng định dạng");
                    binding.tvChangeEmail.setVisibility(View.INVISIBLE);
                }else {
                    binding.tvChangeEmail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.inputChangeBirthday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String in_birth = binding.inputChangeBirthday.getText().toString().trim();
                if (in_birth.equals(birthday)){
                    binding.tvChangeBirthday.setVisibility(View.GONE);
                }else {
                    binding.tvChangeBirthday.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void changeAvatar() {
        ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaPath = data.getDataString();
        if (mediaPath != null){
            uploadMultipleFiles();
        }else
            CheckConection.ShowToast(this, "Cập nhật ảnh lỗi!");
    }

    private String getPath(Uri uri){
        String result;
        Cursor cursor = getContentResolver()
                .query(uri, null,null,null,null);
        if (cursor == null){
            result = uri.getPath();
        }else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }

    // Uploading Image/Video
    private void uploadMultipleFiles() {
        progressDialog.show();
        Uri uri = Uri.parse(mediaPath);
        Log.d("zzzz", "uploadMultipleFiles: " + uri);
        File file = new File(getPath(uri));
        Log.d("zzzz", "file: " + file);
        // Parsing any Media type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
        Log.d("zzzz", "requestBody1: " + requestBody1);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody1);
        Log.d("zzzz", "fileToUpload: " + fileToUpload);
        Call <ServerResponse> call = ApiService.apiService.uploadFile(fileToUpload, 1); // số 21 là userID ở đây chuyền tạm là 21 sau khi đăng nhập phân quền thì sửa sau
        call.enqueue(new Callback < ServerResponse > () {
            @Override
            public void onResponse(Call < ServerResponse > call, Response < ServerResponse > response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        CheckConection.ShowToast(SettingAccountActivity.this, "Cập nhật ảnh thành công!");
                        getInfoUser();
                    } else {
                        CheckConection.ShowToast(SettingAccountActivity.this, "Cập nhật ảnh khônh thành công!");
                    }
                } else {
                    assert serverResponse != null;
                }
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call < ServerResponse > call, Throwable t) {}
        });
    }

    private void showDatePickerDialog() {
        // Lấy ngày, tháng và năm hiện tại
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        // Tạo DatePickerDialog và thiết lập ngày, tháng, năm mặc định
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        binding.inputChangeBirthday.setText(selectedDate);
                    }

                }, currentYear, currentMonth, currentDay);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }

    private void updateProfile(String content, String options) {
        showPDialog();
        if (options.equals("phone")){
            call = ApiService.apiService.updatePhone(content,user_id);
        }else if(options.equals("userName")){
            call = ApiService.apiService.updateUserName(content,user_id);
        }else if (options.equals("email")){
            call = ApiService.apiService.updateEmail(content,user_id);
        }else {
            call = ApiService.apiService.updateBirthday(content,user_id);
        }

        call.enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                String res = response.body().string();
                                JSONObject jsonObject = new JSONObject(res);
                                boolean success = jsonObject.getBoolean("success");
                                if (success == true){
                                    CheckConection.ShowToast(SettingAccountActivity.this
                                            ,"Sửa "+ options +" thành công!");
                                    hidePDialog();
                                    getInfoUser();
                                }else {
                                    CheckConection.ShowToast(SettingAccountActivity.this
                                            ,"Sửa "+ options +" thất bại!");
                                    hidePDialog();
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                }
        );
    }

    private void hidePDialog(){
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    private void settingPDialog() {
        progressDialog = new ProgressDialog(SettingAccountActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }

    private void showPDialog() {
        if (!progressDialog.isShowing()){
            progressDialog.show();
        }
    }
}