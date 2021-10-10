package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button btn_logout;
    private TextView user;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_logout = findViewById(R.id.btn_logout);
        user = findViewById(R.id.text_masuk);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("nama");

        user.setText("Halo, "+username);

        btn_logout.setOnClickListener(view -> gotoLogin());
    }

    private void gotoLogin(){
        Intent i = new Intent(this, login.class);
        this.finish();
        startActivity(i);
    }
}