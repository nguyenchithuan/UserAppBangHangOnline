package edu.wkd.userappbanghangonline.data.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wkd.userappbanghangonline.model.request.FCMRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiServiceSendMessage {
    public static final String URL_MAIN_SEND_MESSAGE = "https://fcm.googleapis.com/fcm/";
    public static final String SERVER_KEY_FCM
            = "AAAA43nP7HE:APA91bEXkqdVEyYtrE1MVfmgCvvP5_EuqJGtz1La3K6C1-j-4btrAwkeaVpJol_u5YsvOXM8sZSXqq6r_xzaC7lORrJOAYSvoxU6OSJlSSm2DjDnFVpQPHutLXnOqFzefS8lq96lVKk6";

    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
    ApiServiceSendMessage apiServiceSenMessage = new Retrofit.Builder()
            .baseUrl(URL_MAIN_SEND_MESSAGE)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServiceSendMessage.class);

    @Headers({
            "Authorization: key= "+ SERVER_KEY_FCM,
            "Content-Type: application/json"
    })
    @POST("send")
    Call<ResponseBody> sendNotification(@Body FCMRequest fcmRequest);
}
