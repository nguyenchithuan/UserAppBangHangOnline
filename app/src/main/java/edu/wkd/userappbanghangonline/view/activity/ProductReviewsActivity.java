package edu.wkd.userappbanghangonline.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.protobuf.Api;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.wkd.userappbanghangonline.data.api.ApiService;
import edu.wkd.userappbanghangonline.databinding.ActivityProductReviewsActivityBinding;
import edu.wkd.userappbanghangonline.model.obj.Order;
import edu.wkd.userappbanghangonline.model.obj.Product;
import edu.wkd.userappbanghangonline.model.response.ServerResponse;
import edu.wkd.userappbanghangonline.ultil.CheckConection;
import edu.wkd.userappbanghangonline.ultil.ProgressDialogLoading;
import edu.wkd.userappbanghangonline.ultil.UserUltil;
import edu.wkd.userappbanghangonline.view.adapter.ProductInReviewsAdapter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductReviewsActivity extends AppCompatActivity {
    private static final String TAG = ProductReviewsActivity.class.toString();
    private ActivityProductReviewsActivityBinding binding;
    private ProgressDialogLoading progressDialog;
    private String mediaPath;
    private MultipartBody.Part fileToUpload;
    private ArrayList<Product> listProduct;
    private int idOrder = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductReviewsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listProduct = (ArrayList<Product>) getIntent().getExtras().getSerializable("list_product_rating");
        idOrder = getIntent().getExtras().getInt("id_order");
        onBack();
        setTitleWhenChooseStar();//Hiển thị tiêu đề phù hợp với từng sao được chọn
        showProductToReviews(listProduct);
        initController();
    }

    private void initController() {
        progressDialog = new ProgressDialogLoading(this);
        binding.layoutAddImgProductReviews.setOnClickListener(view -> {
            ImagePicker.with(ProductReviewsActivity.this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });

        binding.btnSendReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSendReviews();
            }
        });
    }

    private void onClickSendReviews() {
        String strNote = binding.tvNoteReview.getText().toString().trim();

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(UserUltil.user.getId()));
        RequestBody note = RequestBody.create(MediaType.parse("text/plain"), strNote);
        RequestBody date_time = RequestBody.create(MediaType.parse("text/plain"), strDateTime());
        if (TextUtils.isEmpty(strNote)){
            Toast.makeText(this, "Vui lòng điền đánh giá của bạn!", Toast.LENGTH_SHORT).show();
            binding.tvNoteReview.requestFocus();
            return;
        }
        //Chỉ đánh giá 1 sản phẩm
        progressDialog.show();
        if (listProduct.size() > 1){
            for (int i = 0; i < listProduct.size(); i++) {
                RequestBody product_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(listProduct.get(i).getId()));
                apiSendComment(product_id, user_id, note, date_time);
            }
        }else{
            RequestBody product_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(listProduct.get(0).getId()));
            apiSendComment(product_id, user_id, note, date_time);
        }

    }

    private void apiSendComment(RequestBody product_id, RequestBody user_id, RequestBody note, RequestBody date_time) {
        ApiService.apiService.insert_comment(product_id,user_id, note, fileToUpload, date_time).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()){
                    Toast.makeText(ProductReviewsActivity.this, "Đánh giá thành công!", Toast.LENGTH_SHORT).show();
                    updateStatusRating();
                    progressDialog.dismiss();

                }else{
                    Toast.makeText(ProductReviewsActivity.this, "Đánh giá không thành công!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(ProductReviewsActivity.this, "Lỗi server(chi tiết trong logcat)", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: " + t);
                progressDialog.dismiss();
            }
        });
    }

    private void updateStatusRating() {
        ApiService.apiService.updateStatusRatingOrder(idOrder,1).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(ProductReviewsActivity.this, "Cập nhật trạng thái đánh giá không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(ProductReviewsActivity.this, "Lỗi server(chi tiết trong logcat)", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: " + t);
                progressDialog.dismiss();
            }
        });
    }

    private void showProductToReviews(ArrayList<Product> listProduct) {
        if (listProduct != null){
            ProductInReviewsAdapter adapter = new ProductInReviewsAdapter(listProduct,2);
            LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            binding.rvProductInReviews.setLayoutManager(manager);
            binding.rvProductInReviews.setAdapter(adapter);
        }
    }

    private void setTitleWhenChooseStar() {
        binding.rattingBarProductReviews.setRating(5);
//        binding.rattingBarProductReviews.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                switch ((int) rating){
//                    case 1:
//                        binding.tvTitleByRating.setText("Tệ");
//                        break;
//                    case 2:
//                        binding.tvTitleByRating.setText("Không hài lòng");
//                        break;
//                    case 3:
//                        binding.tvTitleByRating.setText("Bình thường");
//                        break;
//                    case 4:
//                        binding.tvTitleByRating.setText("Hài lòng");
//                        break;
//                    case 5:
//                        binding.tvTitleByRating.setText("Tuyệt vời");
//                        break;
//                }
//            }
//        });
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

    private void uploadMultipleFiles() {
        Uri uri = Uri.parse(mediaPath);
        if (uri != null){
            binding.imgUploadToRating.setVisibility(View.VISIBLE);
            binding.imgUploadToRating.setImageURI(uri);
        }else{
            binding.imgUploadToRating.setVisibility(View.GONE);
        }
        Log.d("zzzz", "uploadMultipleFiles: " + uri);
        File file = new File(getPath(uri));
        Log.d("zzzz", "file: " + file);
        // Parsing any Media type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
        Log.d("zzzz", "requestBody1: " + requestBody1);
        fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody1);
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

    private void onBack() {
        binding.arrowBackProductReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String strDateTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        return currentDateTime;
    }
}