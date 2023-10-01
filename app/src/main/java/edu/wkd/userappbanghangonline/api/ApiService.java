package edu.wkd.userappbanghangonline.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import edu.wkd.userappbanghangonline.model.Product;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
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
}