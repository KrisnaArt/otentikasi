package com.example.myapplication.Network;

import com.example.myapplication.POJO.Pesan;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UploadApis {
    @FormUrlEncoded
    @POST("/input")
    Call<Pesan>  uploadImage(@Field("username") String user,
                             @Field("email") String email,
                             @Field("password") String pass,
                             @Field("foto_wajah") String fotoWajah,
                             @Field("alamat") String alamat);

    @POST("/recog")
    @FormUrlEncoded
    Call<Pesan>  recog(@Field("username") String user,
                       @Field("foto_baru") String fotoBaru,
                       @Field("alamat_login") String alamat);

    @POST("/login")
    @FormUrlEncoded
    Call<Pesan> login(@Field("username") String user,
                      @Field("password") String pass);
}
