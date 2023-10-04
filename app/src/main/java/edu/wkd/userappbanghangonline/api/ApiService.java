package edu.wkd.userappbanghangonline.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import edu.wkd.userappbanghangonline.model.obj.Product;

import edu.wkd.userappbanghangonline.model.response.ServerResponse;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

import edu.wkd.userappbanghangonline.model.response.ProductResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://guyinterns2003.000webhostapp.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @GET("get_product.php")

    Call<List<Product>> getListCall(@Query("id") int id);

    @FormUrlEncoded
    @POST("update_user_name.php")
    Call<ResponseBody> updateUserName(@Field("new_name") String newName, @Field("user_id") int userId);

    @FormUrlEncoded
    @POST("update_phone.php")
    Call<ResponseBody> updatePhone(@Field("new_phone") String phone, @Field("user_id") int userId);

    @FormUrlEncoded
    @POST("update_email.php")
    Call<ResponseBody> updateEmail(@Field("new_email") String email, @Field("user_id") int userId);

    @FormUrlEncoded
    @POST("update_birthday.php")
    Call<ResponseBody> updateBirthday(@Field("new_birthday") String birthday, @Field("user_id") int userId);

    @Multipart
    @POST("upload_avatar.php")// cập nhật avatar theo id
    Call<ServerResponse> uploadFile(@Part MultipartBody.Part file,@Part("user_id") int userId);

    Call<ProductResponse> getListProduct();

}
