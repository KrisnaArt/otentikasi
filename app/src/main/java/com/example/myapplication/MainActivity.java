package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.Handler.CameraActivity;
import com.example.myapplication.Network.NetworkClient;
import com.example.myapplication.Network.UploadApis;
import com.example.myapplication.POJO.Pesan;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private Button btn_logout;
    private TextView user;
    private String username;
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_logout = findViewById(R.id.btn_logout);
        user = findViewById(R.id.text_masuk);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("nama");

        user.setText("Halo, "+username);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(username);
            }
        });
    }

    private void gotoLogin(){
        Intent i = new Intent(this, login.class);
        this.finish();
        startActivity(i);
    }

    private void update(String a) {
        Retrofit retrofit = NetworkClient.getRetrofit();

        UploadApis uploadApis = retrofit.create(UploadApis.class);
        Call<Pesan> call = uploadApis.logout(a);
        call.enqueue(new Callback<Pesan>() {
            @Override
            public void onResponse(Call<Pesan> call, Response<Pesan> response) {
                if (response.isSuccessful()) {
                    Log.d("mullllll", response.body().toString());
                    Pesan resp = response.body();
                    if (resp.getMessage().equalsIgnoreCase("oke")) {
                        gotoLogin();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<Pesan> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                if (call.isCanceled()) {
                    System.out.println("Call was cancelled forcefully");
                } else {
                    //Generic error handling
                    System.out.println("Network Error : " + t.getLocalizedMessage());
                }
            }
        });
    }
}