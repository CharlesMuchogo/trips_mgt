package com.example.tripsmanagement.export;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiConfig {
    @POST("upload")
    Call<ServerResponse> uploadJson(@Body JSONObject json);

}
