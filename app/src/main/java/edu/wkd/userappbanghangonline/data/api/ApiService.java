package edu.wkd.userappbanghangonline.data.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import edu.wkd.userappbanghangonline.model.obj.Order;
import edu.wkd.userappbanghangonline.model.obj.Product;
import edu.wkd.userappbanghangonline.model.response.CommentResponse;
import edu.wkd.userappbanghangonline.model.response.LogupUserResponse;
import edu.wkd.userappbanghangonline.model.response.OrderResponse;

import edu.wkd.userappbanghangonline.model.response.ProductTypeResponse;
import edu.wkd.userappbanghangonline.model.response.ServerResponse;


import edu.wkd.userappbanghangonline.model.response.UserResponse;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import edu.wkd.userappbanghangonline.model.response.ProductResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Multipart;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    public static final String URL_MAIN = "https://guyinterns2003.000webhostapp.com/";
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
    ApiService apiService = new Retrofit.Builder()
            .baseUrl(URL_MAIN)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @GET("get_product.php")
    Call<ProductResponse> getListProduct();
    @GET("get_product_type.php")
    Call<ProductTypeResponse> getListTypeProduct();
    @FormUrlEncoded
    @POST("get_user.php")
    Call<UserResponse> getUser(@Field("user_id") int userId);

    @FormUrlEncoded
    @POST("get_user_orders.php")
    Call<OrderResponse> getOrderByIdUser(@Field("user_id") int userId);

    @FormUrlEncoded
    @POST("get_user_orders_status.php")
    Call<OrderResponse> getOrderByIdUserAndStatus(@Field("user_id") int userId, @Field("status") int status);

    @FormUrlEncoded
    @POST("update_order_status.php")
    Call<Order> updateStatusOrder(@Field("id_order") int idOrder, @Field("status") int status);

    @FormUrlEncoded
    @POST("post_order.php")
    Call<ServerResponse> postOrderUser(@Field("user_id") int user_id,
                                       @Field("address") String address,
                                       @Field("phone_number") String phone_number,
                                       @Field("quantity") int quantity,
                                       @Field("total_price") int total_price,
                                       @Field("status") int status,
                                       @Field("datetime") String datetime,
                                       @Field("detail") String detail);

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

    @FormUrlEncoded
    @POST("update_password.php")
    Call<ServerResponse> updatePassword(@Field("new_pass") String pass, @Field("user_id") int userId);

    @Multipart
    @POST("upload_avatar.php") // cập nhật avatar theo id
    Call<ServerResponse> uploadFile(@Part MultipartBody.Part file, @Part("user_id") int userId);

    @FormUrlEncoded
    @POST("search_product.php")
    Call<ProductResponse> getProductSearch(@Field("product_name") String product_name);
    @FormUrlEncoded
    @POST("login.php")
    Call <UserResponse> loginUser(@Field("email") String email, @Field("password") String password);
    @FormUrlEncoded
    @POST("logup.php")
    Call<LogupUserResponse> logUpUser(@Field("email") String email, @Field("password") String password, @Field("username") String username);

    @FormUrlEncoded
    @POST("get_product_detail.php")
    Call<ProductResponse> getProductByType(@Field("product_type") int product_type,
                                             @Field("page") int page);

    @FormUrlEncoded
    @POST("get_comments.php")
    Call<CommentResponse> getComment(@Field("product_id") int productId);
}
