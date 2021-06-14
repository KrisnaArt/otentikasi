package com.example.myapplication;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UploadApis {
    @FormUrlEncoded
    @POST("uploadfile.php")
    Call<Pesan>  uploadImage(@Field("username") RequestBody user,
                             @Field("email") RequestBody email,
                             @Field("password") RequestBody pass,
                             @Field("foto_wajah") RequestBody foto_wajah,
                             @Field("foto_ktp") RequestBody foto_ktp);
}
