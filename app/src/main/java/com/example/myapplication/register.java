package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.myapplication.Handler.CameraActivity;
import com.example.myapplication.Handler.ImageCompression;
import com.example.myapplication.Network.NetworkClient;
import com.example.myapplication.Network.UploadApis;
import com.example.myapplication.POJO.Pesan;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class register extends AppCompatActivity {
    private final AppCompatActivity activity = register.this;
    private Button btn_reg, btn_up_foto;
    private EditText text_user, text_email, text_repassw, text_passw;
    private TextView text_foto;
    private Uri selectedImage;
    private Bitmap bitmap;
    private String part_image, address;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref", Name = "nameKey", Email = "emailKey", TAG = register.class.getSimpleName();
    private static final int PICK_IMAGE_REQUEST = 9544;

    // Permissions for accessing the storage
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /*class user {
        private String user, email, pass;

        public user(String username, String email, String pass) {
            this.user = username;
            this.email = email;
            this.pass = pass;
        }

        public String getUsername() {
            return user;
        }

        public String getEmail() {
            return email;
        }

        public String getPass() {
            return pass;
        }
    }

    // Method for starting the activity for selecting image from phone storage
    public void pick() {
        verifyStoragePermissions(register.this);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                selectedImage = data.getData();                                                         // Get the image file URI
                String[] imageProjection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, imageProjection, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int indexImage = cursor.getColumnIndex(imageProjection[0]);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        part_image = cursor.getString(indexImage);
                        int cut = part_image.lastIndexOf('/');
                        if (cut != -1) {
                            part_image = part_image.substring(cut + 1);
                        }
                        text_foto.setText(part_image);
                    } catch (Exception e) {
                        Toast.makeText(register.this, "Please pick an Image From Right Place, maybe Gallery or File Explorer so that we can get its path." + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void goToLogin() {
        Intent i = new Intent(this, login.class);
        this.finish();
        startActivity(i);
    }*/

    public boolean validate() {
        boolean valid = false;

        String UserName = text_user.getText().toString();
        String Email = text_email.getText().toString();
        String Password = text_passw.getText().toString();
        String RePassword = text_repassw.getText().toString();
        String nama_foto = text_foto.getText().toString();

        if (UserName.isEmpty()) {
            valid = false;
            text_user.setError("Please enter valid username!");
        } else {
            if (UserName.length() > 0) {
                valid = true;
                text_user.setError(null);
            } else {
                valid = false;
                text_user.setError("Username is to short!");
            }
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            valid = false;
            text_email.setError("Please enter valid email!");
        } else {
            valid = true;
            text_email.setError(null);
        }

        if (Password.isEmpty()) {
            valid = false;
            text_passw.setError("Please enter valid password!");
        } else {
            if (Password.length() >= 8) {
                valid = true;
                text_passw.setError(null);
            } else {
                valid = false;
                text_passw.setError("Password is to short!");
            }
        }

        if (RePassword.isEmpty()) {
            valid = false;
            text_repassw.setError("Please enter re password!");
        } else {
            if (Password.toString().equals(RePassword)) {
                valid = true;
                text_repassw.setError(null);
            } else {
                valid = false;
                text_repassw.setError("Password not same!");
            }
        }

        if (nama_foto.isEmpty()) {
            valid = false;
            text_repassw.setError("Pilih Gambar");
        } else {
            valid = true;
            text_email.setError(null);
        }

        return valid;
    }

    private void emptyInputEditText() {
        text_user.setText(null);
        text_email.setText(null);
        text_passw.setText(null);
        text_repassw.setText(null);
    }

    private String encodeUri(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        return image;
    }

    /*public void addData() {
        if (validate()) {
            String UserName = text_user.getText().toString();
            String Email = text_email.getText().toString();
            String Password = text_passw.getText().toString();
            String foto = getBitmap(text_foto.getText().toString());
            //String fotoWajah = encodeUri(foto);

            user us = new user(UserName, Email, Password);

            String user = us.getUsername();
            String email = us.getEmail();
            String pass = us.getPass();

            Retrofit retrofit = NetworkClient.getRetrofit();

            UploadApis uploadApis = retrofit.create(UploadApis.class);
            if(ActivityCompat.checkSelfPermission(register.this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                getLocation();
            }
            else{
                ActivityCompat.requestPermissions(register.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
            }
            Call<Pesan> call = uploadApis.uploadImage(user, email, pass, foto, address);
            call.enqueue(new Callback<Pesan>() {
                @Override
                public void onResponse(Call<Pesan> call, Response<Pesan> response) {
                    if (response.isSuccessful()) {
                        Log.d("mullllll", response.body().toString());
                        Pesan resp = response.body();
                        if (resp.getMessage().equalsIgnoreCase("oke4")) {
                            goToLogin();
                            emptyInputEditText();
                        }
                    } else {

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
    }

    public void getLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            System.out.println(addresses.get(0).getAddressLine(0));
            address = addresses.get(0).getAddressLine(0);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }*/

    public void gotoCamera(){
        Intent i = new Intent(activity, CameraActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("kelas","register");
        bundle.putString("nama",text_user.getText().toString());
        bundle.putString("email",text_email.getText().toString());
        bundle.putString("password",text_passw.getText().toString());
        i.putExtras(bundle);
        startActivity(i);
    }

    /*public String getBitmap(String a){
        String fotoBaru = "";
        try {
            Uri selectedImage = Uri.fromFile(new File(a));
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            Bitmap foto = ImageCompression.reduceBitmapSize(bitmap, 40000);
            fotoBaru = encodeUri(foto);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fotoBaru;
    }*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        btn_reg = findViewById(R.id.btn_register);
        text_user = findViewById(R.id.username);
        text_email = findViewById(R.id.email);
        text_passw = findViewById(R.id.pass);
        text_repassw = findViewById(R.id.passCopy);

        //data d = new data();

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Name)) {
            text_user.setText(sharedpreferences.getString(Name, " "));
        }
        if (sharedpreferences.contains(Email)) {
            text_email.setText(sharedpreferences.getString(Email, " "));
        }

        emptyInputEditText();

        btn_reg.setOnClickListener(v -> gotoCamera());
    }
}
