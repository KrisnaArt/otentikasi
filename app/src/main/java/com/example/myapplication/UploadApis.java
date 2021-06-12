package com.example.myapplication;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadApis {
//    @Headers("Content-Type: application/json")
    @FormUrlEncoded
    @POST("uploadfile.php")
    Call<Pesan> uploadImage(@Field("username") RequestBody user,
                                   @Field("email") RequestBody email,
                                   @Field("password") RequestBody pass);
}
