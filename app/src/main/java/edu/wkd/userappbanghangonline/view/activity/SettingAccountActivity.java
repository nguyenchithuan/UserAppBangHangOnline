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
import android.widget.DatePicker;

import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;

import edu.wkd.userappbanghangonline.data.api.ApiService;
import edu.wkd.userappbanghangonline.databinding.ActivitySettingAccountBinding;
import edu.wkd.userappbanghangonline.model.response.ServerResponse;
import edu.wkd.userappbanghangonline.ultil.CheckConection;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        settingPDialog();

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

        binding.inputChangeBithday.setOnClickListener(v -> {
            showDatePickerDialog();
        });

        binding.tvChangeBirthday.setOnClickListener(v -> {
            String birthday = binding.inputChangeBithday.getText().toString().trim();
            updateProfile(birthday,"birthday");
        });

        binding.imgChangeAvatar.setOnClickListener(v -> {
            changeAvatar();
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
        File file = new File(getPath(uri));
        // Parsing any Media type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody1);
        Call <ServerResponse> call = ApiService.apiService.uploadFile(fileToUpload, 21); // số 21 là userID ở đây chuyền tạm là 21 sau khi đăng nhập phân quền thì sửa sau
        call.enqueue(new Callback < ServerResponse > () {
            @Override
            public void onResponse(Call < ServerResponse > call, Response < ServerResponse > response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.isSuccess()) {
                        CheckConection.ShowToast(SettingAccountActivity.this, "Cập nhật ảnh thành công!");
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
                        binding.inputChangeBithday.setText(selectedDate);
                    }

                }, currentYear, currentMonth, currentDay);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }

    private void updateProfile(String content, String options) {
        showPDialog();
        if (options.equals("phone")){
            call = ApiService.apiService.updatePhone(content,21);
        }else if(options.equals("userName")){
            call = ApiService.apiService.updateUserName(content,21);
        }else if (options.equals("email")){
            call = ApiService.apiService.updateEmail(content,21);
        }else {
            call = ApiService.apiService.updateBirthday(content,21);
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
                                    finish();
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