package com.example.myapplication;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadApis {
    @Multipart
    @POST("uploadfile.php")
    Call<String> uploadImage(@Part("username") RequestBody user,
                                   @Part("email") RequestBody email,
                                   @Part("password") RequestBody pass,
                                   @Part MultipartBody.Part part,
                                   @Part("filename") RequestBody name,
                                   @Part MultipartBody.Part part1,
                                   @Part("filename1") RequestBody name1);
}
