package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class register extends AppCompatActivity {
    private Button btn_reg, btn_up_foto, btn_up_video;
    private EditText text_user, text_email, text_repassw, text_passw;
    private TextView text_foto, text_video;
    private Uri selectedImage, selectedImage1;
    private Bitmap bitmap, bitmap1;
    String part_image;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref", Name = "nameKey", Email = "emailKey", TAG = register.class.getSimpleName();
    private static final int PICK_IMAGE_REQUEST = 9544,PICK_IMAGE_REQUEST1 = 9543;
    //private static final String DATA_UPLOAD_URL="http://192.168.1.3/skripsi/index.php";

    // Permissions for accessing the storage
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private void initObjects() {
        AndroidNetworking.initialize(this);
    }

    class user{
        private String user, email, pass;
        public user(String username, String email, String pass) {
            this.user = username;
            this.email = email;
            this.pass = pass;
        }
        public String getUsername() {return user;}
        public String getEmail() {return email;}
        public String getPass() {return pass;}
    }

    // Method for starting the activity for selecting image from phone storage
    public void pick() {
        verifyStoragePermissions(register.this);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Open Gallery"), PICK_IMAGE_REQUEST);

    }

    public void pick1() {
        verifyStoragePermissions(register.this);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Open Gallery"), PICK_IMAGE_REQUEST1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                selectedImage = data.getData();                                                         // Get the image file URI
                String[] imageProjection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, imageProjection, null, null, null);
                if(cursor != null) {
                    cursor.moveToFirst();
                    int indexImage = cursor.getColumnIndex(imageProjection[0]);
                    try{
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        part_image = cursor.getString(indexImage);
                        int cut = part_image.lastIndexOf('/');
                        if (cut != -1) {
                            part_image = part_image.substring(cut + 1);
                        }
                        text_foto.setText(part_image);
                    } catch (Exception e) {
                        Toast.makeText(register.this, "Please pick an Image From Right Place, maybe Gallery or File Explorer so that we can get its path."+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        } else if(requestCode == PICK_IMAGE_REQUEST1){
            if (resultCode == RESULT_OK) {
                selectedImage1 = data.getData();                                                         // Get the image file URI
                String[] imageProjection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage1, imageProjection, null, null, null);
                if(cursor != null) {
                    cursor.moveToFirst();
                    int indexImage = cursor.getColumnIndex(imageProjection[0]);
                    try{
                        bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage1);
                        part_image = cursor.getString(indexImage);
                        int cut = part_image.lastIndexOf('/');
                        if (cut != -1) {
                            part_image = part_image.substring(cut + 1);
                        }
                        text_video.setText(part_image);
                    } catch (Exception e) {
                        Toast.makeText(register.this, "Please pick an Image From Right Place, maybe Gallery or File Explorer so that we can get its path."+e.getMessage(), Toast.LENGTH_LONG).show();
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

    /*
    Get Image Path propvided its  android.net.Uri
     */
    public String getImagePath(Uri uri)
    {
        String[] projection={MediaStore.Images.Media.DATA};
        Cursor cursor=getContentResolver().query(uri,projection,null,null,null);
        if(cursor == null){
            return null;
        }
        int columnIndex= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(columnIndex);
        cursor.close();
        return s;
    }

    public void goToLogin() {
        Intent i = new Intent(this, login.class);
        this.finish();
        startActivity(i);
    }

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

        if (nama_foto.isEmpty()){
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

    public void startService() {
        Intent serviceIntent = new Intent(this, service.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    public void stopService() {
        Intent serviceIntent = new Intent(this, service.class);
        stopService(serviceIntent);
    }

    public void addData() {
        //startService();
        //if (validate()) {
        String imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());
            String UserName = text_user.getText().toString();
            String Email = text_email.getText().toString();
            String Password = text_passw.getText().toString();

            user us = new user(UserName,Email, Password);

            File imageFile;
            File imageFile1;
            try {
                imageFile = new File(getImagePath(selectedImage));
                imageFile1 = new File(getImagePath(selectedImage1));

            }catch (Exception e){
                Toast.makeText(register.this, "Please pick an Image From Right Place, maybe Gallery or File Explorer so that we can get its path."+e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            System.out.println("Username : "+us.getUsername());
            System.out.println("Email : "+us.getEmail());
            System.out.println("Pass : "+us.getPass());
            System.out.println("Imgae 1 : "+imageFile);
            System.out.println("Imgae 2 : "+imageFile1);

            Retrofit retrofit = NetworkClient.getRetrofit();

            RequestBody user = RequestBody.create(MediaType.parse("text/plain"), us.getUsername());
            RequestBody email = RequestBody.create(MediaType.parse("text/plain"), us.getEmail());
            RequestBody pass = RequestBody.create(MediaType.parse("text/plain"), us.getPass());

            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), imageFile);
            MultipartBody.Part parts = MultipartBody.Part.createFormData("foto_wajah", imageFile.getName(), requestBody);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), imgname);

            RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), imageFile1);
            MultipartBody.Part parts1 = MultipartBody.Part.createFormData("foto_ktp", imageFile1.getName(), requestBody1);
            RequestBody filename1 = RequestBody.create(MediaType.parse("text/plain"), imgname);

            UploadApis uploadApis = retrofit.create(UploadApis.class);
            Call call = uploadApis.uploadImage(user,email,pass,parts,filename,parts1,filename1);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        Log.d("mullllll", response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            jsonObject.toString().replace("\\\\","");
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray dataArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                }
                                goToLogin();
                                emptyInputEditText();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        System.out.println("Gagal");
/*
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(this, jObjError, Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                        ResponseBody errorBody = response.errorBody();
                        Gson gson = new Gson();
                        Response errorResponse = gson.fromJson(errorBody.toString(), Response.class);
                        Snackbar.make(findViewById(R.id.content), errorResponse.message(),Snackbar.LENGTH_SHORT).show();

*/
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
                    if(call.isCanceled())
                    {
                        System.out.println("Call was cancelled forcefully");
                    }
                    else
                    {
                        //Generic error handling
                        System.out.println("Network Error : " + t.getLocalizedMessage());
                    }
                }
            });

            /*String path = getImagePath(selectedImage);
            String path1 = getImagePath(selectedImage1);

            try {

                String uploadId = UUID.randomUUID().toString();
                new MultipartUploadRequest(this, uploadId, DATA_UPLOAD_URL)
                        .addFileToUpload(path, "foto_wajah")
                        .addFileToUpload(path1, "foto_ktp")
                        .addParameter("username", us.getUsername())
                        .addParameter("email", us.getEmail())
                        .addParameter("pass", us.getPass())
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(3)
                        .startUpload();

            } catch (Exception ex) {
                System.out.println("Eror : " + ex.getMessage());

            }
            AndroidNetworking.upload(DATA_UPLOAD_URL)
                    .addMultipartFile("foto_wajah",imageFile)
                    .addMultipartFile("foto_ktp",imageFile1)
                    .addMultipartParameter("username",us.getUsername())
                    .addMultipartParameter("email",us.getEmail())
                    .addMultipartParameter("pass",us.getPass())
                    .setTag("MYSQL_UPLOAD")
                    .setPriority(Priority.HIGH)
                    .build()
                    .setUploadProgressListener(new UploadProgressListener() {
                        @Override
                        public void onProgress(long bytesUploaded, long totalBytes) {
                            System.out.println("byte : "+bytesUploaded);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {

                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("respon :"+response.toString());
                            if(response != null) {
                                try{
                                    //SHOW RESPONSE FROM SERVER
                                    String responseString = response.get("message").toString();
                                    Toast.makeText(register.this, "PHP SERVER RESPONSE : " + responseString, Toast.LENGTH_LONG).show();
                                    emptyInputEditText();

                                    if (responseString.equalsIgnoreCase("Success")) {
                                        //RESET VIEWS
                                        emptyInputEditText();
                                        goToLogin();

                                    } else {
                                        Toast.makeText(register.this, "PHP WASN'T SUCCESSFUL. ", Toast.LENGTH_LONG).show();
                                    }
                                }catch(Exception e)
                                {
                                    e.printStackTrace();
                                    Toast.makeText(register.this, "JSONException "+e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(register.this, "NULL RESPONSE. ", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            error.printStackTrace();
                            Toast.makeText(register.this, "UNSUCCESSFUL :  ERROR IS : \n"+error.getMessage(), Toast.LENGTH_LONG).show();
                            System.out.println("Error is : "+error.getMessage());
                        }
                    });*/
        //}
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        btn_reg = findViewById(R.id.btn_register);
        btn_up_foto = findViewById(R.id.btn_input);
        btn_up_video = findViewById(R.id.btn_input1);
        text_user = findViewById(R.id.username);
        text_email = findViewById(R.id.email);
        text_passw = findViewById(R.id.pass);
        text_repassw = findViewById(R.id.passCopy);
        text_foto = findViewById(R.id.text_poto);
        text_video = findViewById(R.id.text_video);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Name)) {
            text_user.setText(sharedpreferences.getString(Name, ""));
        }
        if (sharedpreferences.contains(Email)) {
            text_email.setText(sharedpreferences.getString(Email, ""));
        }

        //initObjects();

        btn_up_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick();
            }
        });

        btn_up_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick1();
            }
        });
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });
    }
}
