package edu.wkd.userappbanghangonline.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import edu.wkd.userappbanghangonline.model.obj.Product;
import edu.wkd.userappbanghangonline.model.response.ProductResponse;
import edu.wkd.userappbanghangonline.model.response.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://guyinterns2003.000webhostapp.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @GET("get_product.php")
    Call<ProductResponse> getListProduct();

    @FormUrlEncoded
    @POST("post_order.php")
    Call<Response> postOrderUser(@Field("user_id") int user_id,
                                 @Field("address") String address,
                                 @Field("phone_number") String phone_number,
                                 @Field("quantity") int quantity,
                                 @Field("total_price") int total_price,
                                 @Field("status") int status,
                                 @Field("datetime") String datetime,
                                 @Field("detail") String detail);
}
