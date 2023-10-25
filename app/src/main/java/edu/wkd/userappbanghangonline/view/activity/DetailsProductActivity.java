package edu.wkd.userappbanghangonline.view.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.wkd.userappbanghangonline.R;
import edu.wkd.userappbanghangonline.data.api.ApiService;
import edu.wkd.userappbanghangonline.data.api.ApiServiceSendMessage;
import edu.wkd.userappbanghangonline.databinding.ActivityDetailsProductActivityBinding;
import edu.wkd.userappbanghangonline.model.obj.Cart;
import edu.wkd.userappbanghangonline.model.obj.Comment;
import edu.wkd.userappbanghangonline.model.obj.Product;
import edu.wkd.userappbanghangonline.model.request.FCMRequest;
import edu.wkd.userappbanghangonline.model.response.CommentResponse;
import edu.wkd.userappbanghangonline.model.response.ServerResponse;
import edu.wkd.userappbanghangonline.ultil.CartUltil;
import edu.wkd.userappbanghangonline.ultil.CheckConection;
import edu.wkd.userappbanghangonline.ultil.ProgressDialogLoading;
import edu.wkd.userappbanghangonline.ultil.Token;
import edu.wkd.userappbanghangonline.ultil.UserUltil;
import edu.wkd.userappbanghangonline.view.adapter.CommentAdapter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailsProductActivity extends AppCompatActivity {
    private ActivityDetailsProductActivityBinding binding;
    private Product product;
    private int productQuantity = 1; // so luong san pham
    private ProgressDialogLoading progressDialogLoading;
    private String mediaPath;
    private RequestBody requestBodyFile;
    private MultipartBody.Part fileToUpload;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private Comment commentObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsProductActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //init dialog loading
        progressDialogLoading = new ProgressDialogLoading(this);
        onBack();//Quay trở lại sự kiện trước đó
        setDataDetailsProduct();
        onclickBtnProductQuantity();
        onclickBtnCart();
        eventBtnCart();
        evenComments();
    }

    private void evenComments() {
        binding.btnComment.setOnClickListener(v -> {
            BottomSheetDialog commentDialog = new BottomSheetDialog(DetailsProductActivity.this);
            commentDialog.setContentView(R.layout.layout_dialog_bottom_comment);
            //init view
            EditText ed_note = commentDialog.findViewById(R.id.ed_note_cmt);
            ImageButton btn_send_comment = commentDialog.findViewById(R.id.btn_send_cmt);
            RecyclerView rcv_comments = commentDialog.findViewById(R.id.rcv_comments);
            ImageButton btn_camera_cmt = commentDialog.findViewById(R.id.btn_camera_cmt);
            ImageButton btn_close_image = commentDialog.findViewById(R.id.btn_close_image);
            TextView tv_uri_image = commentDialog.findViewById(R.id.tv_uri_image);

            LinearLayoutManager linearLayoutManager =
                    new LinearLayoutManager(DetailsProductActivity.this,
                            LinearLayoutManager.VERTICAL,false);
            rcv_comments.setLayoutManager(linearLayoutManager);
            //set comment
            getCommentData();
            setCommentInRecycleView(rcv_comments);
            //on start
            tv_uri_image.setVisibility(View.INVISIBLE);

            btn_camera_cmt.setOnClickListener(v1 -> {
                showImagePicker();
                if (mediaPath != null){
                    tv_uri_image.setText(mediaPath);
                    tv_uri_image.setVisibility(View.VISIBLE);
                }else {
                    tv_uri_image.setText("");
                    tv_uri_image.setVisibility(View.INVISIBLE);
                }
            });

            btn_close_image.setOnClickListener(v1 -> {
                mediaPath = null;
                tv_uri_image.setText("");
                tv_uri_image.setVisibility(View.INVISIBLE);
            });

            btn_send_comment.setOnClickListener(v1 -> {
                //loading
                progressDialogLoading.show();
                if (mediaPath != null){
                    Uri uri = Uri.parse(mediaPath);
                    File file = new File(getPath(uri));
                    // Parsing any Media type file
                    requestBodyFile = RequestBody.create(MediaType.parse("*/*"), file);
                    fileToUpload = MultipartBody.Part.createFormData("file",
                            file.getName(), requestBodyFile);
                }else {
                    fileToUpload = null;
                }
                String strNote = ed_note.getText().toString().trim();

                RequestBody product_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(product.getId()));
                RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(UserUltil.user.getId()));
                RequestBody note = RequestBody.create(MediaType.parse("text/plain"), strNote);
                RequestBody date_time = RequestBody.create(MediaType.parse("text/plain"), strDateTime());
                if (!strNote.isEmpty() || mediaPath != null){
                    ApiService.apiService.insert_comment(product_id, user_id, note, fileToUpload,date_time)
                            .enqueue(new Callback<ServerResponse>() {
                                @Override
                                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                    if(response.isSuccessful()){
                                        ServerResponse serverResponse = response.body();
                                        if (serverResponse.getSuccess()){
                                            getCommentData();
                                            setCommentInRecycleView(rcv_comments);
                                            //
                                            pushNotification(strNote);
                                            //clear
                                            mediaPath = null;
                                            ed_note.setText("");
                                            progressDialogLoading.hide();
                                        }else{
                                            CheckConection.ShowToast(DetailsProductActivity.this,
                                                    "Không thể bình luận được xin hãy thử lại!");
                                            progressDialogLoading.hide();
                                        }
                                    }else{
                                        CheckConection.ShowToast(DetailsProductActivity.this,
                                                "Bình luận thất bại!");
                                        progressDialogLoading.hide();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ServerResponse> call, Throwable t) {
                                    CheckConection.ShowToast(DetailsProductActivity.this,
                                            t.getMessage());
                                    progressDialogLoading.hide();
                                }
                            });
                }else {
                    CheckConection.ShowToast(DetailsProductActivity.this,
                            "Bạn chưa chọn ảnh hoặc viết bình luận");
                    progressDialogLoading.hide();
                }

            });

            commentDialog.show();
        });
    }

    private void pushNotification(String strNote) {
        Map<String,String> dataMessage = new HashMap<>();
        dataMessage.put("title", "Thong bao");
        dataMessage.put("body", strNote);
        FCMRequest fcmRequest = new FCMRequest(Token.TOKEN_DEVICE,dataMessage);
        ApiServiceSendMessage.apiServiceSenMessage.sendNotification(fcmRequest)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }

    private void setCommentInRecycleView(RecyclerView rcvComments) {
        ApiService.apiService.get_comments(product.getId())
                .enqueue(new Callback<CommentResponse>() {
                    @Override
                    public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                        if (response.isSuccessful()){
                            CommentResponse commentResponse = response.body();
                            if (commentResponse.isSuccess()) {
                                commentAdapter.setListComment(commentResponse.getResult());
                                rcvComments.setAdapter(commentAdapter);
                                progressDialogLoading.hide();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CommentResponse> call, Throwable t) {

                    }
                });
    }

    private void getCommentData() {
        ApiService.apiService.get_comments(product.getId())
                .enqueue(new Callback<CommentResponse>() {
                    @Override
                    public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                        if (response.isSuccessful()){
                            CommentResponse commentResponse = response.body();
                            if (commentResponse.isSuccess()) {
                                commentList = commentResponse.getResult();
                                progressDialogLoading.hide();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CommentResponse> call, Throwable t) {

                    }
                });
    }

    private String strDateTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        return currentDateTime;
    }


    private void showImagePicker() {
        ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    private boolean statusUpdateCmt;

    @Override
    protected void onStart() {
        super.onStart();
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(DetailsProductActivity.this,commentList);
        commentObj = new Comment();

        statusUpdateCmt = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaPath = data.getDataString();
        if (mediaPath != null && statusUpdateCmt == true ){ //update comment
            uploadMultipleFiles();
        }
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

    // Update Image/Video
    private void uploadMultipleFiles() {
        progressDialogLoading.show();
        Uri uri = Uri.parse(mediaPath);
        File file = new File(getPath(uri));
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody1);
    }

    private void setDataDetailsProduct() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        product = (Product) bundle.get("product");

        Glide.with(this)
                .load(product.getImage())
                .error(R.mipmap.ic_launcher)
                .into(binding.imgProduct);
        binding.tvNameProduct.setText(product.getName());
        binding.ratingBar.setRating(product.getRating());
        binding.tvQuantityRating.setText("(" + product.getQuantityRating() +  ")");
        binding.tvDescription.setText(product.getDescription());
        DecimalFormat df = new DecimalFormat("###,###,###");
        binding.tvPrice.setText(df.format(product.getPrice()) + " đ");
        binding.tvProductQuantity.setText(productQuantity + "");
    }

    private void onclickBtnProductQuantity() {
        binding.btnPlus.setOnClickListener(view -> {
            if(productQuantity < 10) {
                productQuantity += 1;
                binding.tvProductQuantity.setText(productQuantity + "");
            }
        });

        binding.btnMinus.setOnClickListener(view -> {
            if(productQuantity > 1) {
                productQuantity -= 1;
                binding.tvProductQuantity.setText(productQuantity + "");
            }
        });
    }

    private void onclickBtnCart() {
        binding.btnCart.setOnClickListener(view -> {
            Intent intent = new Intent(this, CartActivity.class);
            mActivityResultLauncher.launch(intent);
            addListCart();
            overridePendingTransition(R.anim.slidle_in_left, R.anim.slidle_out_left);
        });
    }

    private void addListCart() {
        Boolean check = false;
        for (int i = 0; i < CartUltil.listCart.size(); i++) {
            if(product.getId() == CartUltil.listCart.get(i).getIdProduct()) {
                productQuantity += CartUltil.listCart.get(i).getQuantity();
                if(productQuantity > 10) {
                    productQuantity = 10;
                }
                CartUltil.listCart.get(i).setQuantity(productQuantity);
                check = true;
            }
        }
        if(check != true) {
            Cart cart = new Cart();
            cart.setIdProduct(product.getId());
            cart.setName(product.getName());
            cart.setImage(product.getImage());
            cart.setPrice(product.getPrice());
            cart.setQuantity(productQuantity);
            CartUltil.listCart.add(cart);
        }
    }

    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                int cartSize = intent.getIntExtra("data_cart_size", 0);
                binding.tvQuantityCart.setText(cartSize + "");
            }
        }
    });
    private void eventBtnCart() {
        binding.tvQuantityCart.setText(CartUltil.listCart.size() + "");
        binding.imgCart.setOnClickListener(view -> {
            Intent intent = new Intent(this, CartActivity.class);
            mActivityResultLauncher.launch(intent);
            overridePendingTransition(R.anim.slidle_in_left, R.anim.slidle_out_left);
        });
    }

    private void onBack() {
        binding.arrowBackDetailProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        onBackActivity();
        overridePendingTransition(R.anim.slidle_in_right, R.anim.slidle_out_right);
    }

    private void onBackActivity() {
        // Truyền số lượng sản phẩm về màn hình trc đó
        Intent intent = new Intent(DetailsProductActivity.this, MainActivity.class);
        intent.putExtra("data_cart_size", CartUltil.listCart.size());
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.slidle_in_right, R.anim.slidle_out_right);
    }
}