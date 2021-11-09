package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Handler.CameraActivity;
import com.example.myapplication.Network.NetworkClient;
import com.example.myapplication.Network.UploadApis;
import com.example.myapplication.POJO.Pesan;
import com.example.myapplication.POJO.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class login extends AppCompatActivity {

    private final AppCompatActivity activity = login.this;

    private EditText username;
    private EditText pass;
    private Button btn_login,btn_daftar;
    public static final String TAG = login.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        username    = findViewById(R.id.username);
        pass        = findViewById(R.id.pass);
        btn_login   = findViewById(R.id.btn_login);
        btn_daftar = findViewById(R.id.btn_register);

        btn_login.setOnClickListener(v -> {
            goToMainActivity();
        });

        btn_daftar.setOnClickListener(v -> {
            goToRegister();
        });
    }

    private void goToMainActivity(){
        String Username = username.getText().toString();
        String Password = pass.getText().toString();

        Retrofit retrofit = NetworkClient.getRetrofit();

        UploadApis uploadApis = retrofit.create(UploadApis.class);
        Call<Pesan> call = uploadApis.login(Username, Password);
        call.enqueue(new Callback<Pesan>() {
            @Override
            public void onResponse(Call<Pesan> call, Response<Pesan> response) {
                if (response.isSuccessful()) {
                    Log.d("mullllll", response.body().getMessage());
                    Pesan resp = response.body();
                    if(resp.getMessage().equals("oke")){
                        Intent i = new Intent(activity, CameraActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("kelas","login");
                        bundle.putString("nama",Username);
                        i.putExtras(bundle);
                        activity.finish();
                        startActivity(i);
                        emptyInputEditText();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Username/Password Salah",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
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

    private void goToRegister(){
        Intent i = new Intent(activity,register.class);
        activity.finish();
        startActivity(i);
        emptyInputEditText();
    }

    private void emptyInputEditText() {
        username.setText(null);
        pass.setText(null);
    }
}