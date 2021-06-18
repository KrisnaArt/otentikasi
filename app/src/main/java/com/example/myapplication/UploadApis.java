package com.example.myapplication;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UploadApis {
    @FormUrlEncoded
    @POST("uploadfile1.php")
    Call<Pesan>  uploadImage(@Field("username") String user,
                             @Field("email") String email,
                             @Field("password") String pass,
                             @Field("foto_wajah") String foto_wajah,
                             @Field("foto_ktp") String foto_ktp);
}
