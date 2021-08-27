package com.example.myapplication.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("foto_wajah")
    @Expose
    private String foto_wajah;

    @SerializedName("foto_ktp")
    @Expose
    private String foto_ktp;

    @SerializedName("foto_baru")
    @Expose
    private String foto_baru;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getfoto_wajah() {
        return foto_wajah;
    }

    public void setFoto_wajah(String foto_wajah) {
        this.foto_wajah = foto_wajah;
    }

    public String getFoto_ktp() {
        return foto_ktp;
    }

    public void setFoto_ktp(String foto_ktp) {
        this.foto_ktp = foto_ktp;
    }

    public String getFoto_baru() {
        return foto_baru;
    }

    public void setFoto_baru(String foto_baru) {
        this.foto_baru = foto_baru;
    }

}
