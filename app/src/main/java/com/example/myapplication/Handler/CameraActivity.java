package com.example.myapplication.Handler;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;


import com.example.myapplication.loadingPage.LoadingDialog;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Network.NetworkClient;
import com.example.myapplication.Network.UploadApis;
import com.example.myapplication.POJO.Pesan;
import com.example.myapplication.R;
import com.example.myapplication.data;
import com.example.myapplication.login;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CameraActivity extends AppCompatActivity {
    private int REQUEST_CODE_PERMISSIONS = 101;
    private String username, address, kelas, email, password;
    //public String pat;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    public static final String TAG = CameraActivity.class.getSimpleName();
    TextureView textureView;
    private FusedLocationProviderClient fusedLocationClient;
    final LoadingDialog loadingDialog = new LoadingDialog(CameraActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameralayout);

        Bundle bundle = getIntent().getExtras();
        kelas = bundle.getString("kelas");
        if(kelas.equalsIgnoreCase("login")){
            username = bundle.getString("nama");
        }else{
            username = bundle.getString("nama");
            email = bundle.getString("email");
            password = bundle.getString("password");
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        textureView = findViewById(R.id.view_finder);

        if (allPermissionsGranted()) {
            startCamera(); //start camera if permission has been granted by user
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void startCamera() {
        CameraX.unbindAll();

        Rational aspectRatio = new Rational(textureView.getWidth(), textureView.getHeight());
        Size screen = new Size(textureView.getWidth(), textureView.getHeight()); //size of the screen


        PreviewConfig pConfig = new PreviewConfig.Builder()
                .setTargetAspectRatio(aspectRatio)
                .setTargetResolution(screen)
                .setLensFacing(CameraX.LensFacing.FRONT)
                .build();
        Preview preview = new Preview(pConfig);

        preview.setOnPreviewOutputUpdateListener(
                output -> {
                    ViewGroup parent = (ViewGroup) textureView.getParent();
                    parent.removeView(textureView);
                    parent.addView(textureView, 0);
                    textureView.setSurfaceTexture(output.getSurfaceTexture());
                    updateTransform();
                });


        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setLensFacing(CameraX.LensFacing.FRONT).setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();
        final ImageCapture imgCap = new ImageCapture(imageCaptureConfig);

        findViewById(R.id.imgCapture).setOnClickListener(v -> {
            loadingDialog.startLoadingDialog();
            String a = "";
            if (ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
            if(kelas.equalsIgnoreCase("register")){
                a = "learn";
            }else{
                a = "detect";
            }
            File file = new File(Environment.getExternalStorageDirectory(),"skripsi");
            file.mkdirs();
            File file1 = new File(Environment.getExternalStorageDirectory(),a);

            if(file1.exists()){
                String path = Environment.getExternalStorageDirectory() + "/skripsi/"+ a +"/" + System.currentTimeMillis() + ".jpg";
                File file3 = new File(path);

                imgCap.takePicture(file3, new ImageCapture.OnImageSavedListener() {
                    @Override
                    public void onImageSaved(@NonNull File file) {
                        String msg = "Pic captured at " + file3.getAbsolutePath();
                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                        sendData(kelas,username, getBitmap(kelas, path));
                    }

                    @Override
                    public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
                        String msg = "Pic capture failed : " + message;
                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                        if (cause != null) {
                            cause.printStackTrace();
                        }
                    }
                });
            }
            else{
                Toast.makeText(this,"Folder not found",Toast.LENGTH_SHORT).show();
            }
        });

        //bind to lifecycle:
        CameraX.bindToLifecycle((LifecycleOwner) this, preview, imgCap);
    }

    private void updateTransform() {
        Matrix mx = new Matrix();
        float w = textureView.getMeasuredWidth();
        float h = textureView.getMeasuredHeight();

        float cX = w / 2f;
        float cY = h / 2f;

        int rotationDgr;
        int rotation = (int) textureView.getRotation();

        switch (rotation) {
            case Surface.ROTATION_0:
                rotationDgr = 0;
                break;
            case Surface.ROTATION_90:
                rotationDgr = 90;
                break;
            case Surface.ROTATION_180:
                rotationDgr = 180;
                break;
            case Surface.ROTATION_270:
                rotationDgr = 270;
                break;
            default:
                return;
        }

        mx.postRotate((float) rotationDgr, cX, cY);
        textureView.setTransform(mx);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean allPermissionsGranted() {

        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void sendData(String a, String b, String c) {
        Retrofit retrofit = NetworkClient.getRetrofit();

        if(a.equalsIgnoreCase("register")){
            UploadApis uploadApis = retrofit.create(UploadApis.class);
            Call<Pesan> call = uploadApis.uploadImage(b, email, password, c, address);
            call.enqueue(new Callback<Pesan>() {
                @Override
                public void onResponse(Call<Pesan> call, Response<Pesan> response) {
                    if (response.isSuccessful()) {
                        Log.d("mullllll", response.body().toString());
                        Pesan resp = response.body();
                        if (resp.getMessage().equalsIgnoreCase("oke4")) {
                            loadingDialog.dismissDialog();
                            goToLogin();
                        }
                    } else {
                        loadingDialog.dismissDialog();
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
        else{
            UploadApis uploadApis = retrofit.create(UploadApis.class);
            Call<Pesan> call = uploadApis.recog(b, c, address);
            call.enqueue(new Callback<Pesan>() {
                @Override
                public void onResponse(Call<Pesan> call, Response<Pesan> response) {
                    if (response.isSuccessful()) {
                        Log.d("mullllll", response.body().toString());
                        Pesan resp = response.body();
                        if (resp.getMessage().equalsIgnoreCase("oke4")) {
                            loadingDialog.dismissDialog();
                            goToMain(b);
                        }
                    } else {
                        loadingDialog.dismissDialog();
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

    public void goToMain(String a) {
        Intent i = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("nama",a);
        i.putExtras(bundle);
        this.finish();
        startActivity(i);
    }

    public void goToLogin() {
        Intent i = new Intent(this, login.class);
        this.finish();
        startActivity(i);
    }

    public void getLocation() {
        /*LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
            Location location = lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            System.out.println(addresses);
            address = addresses.get(0).getAddressLine(0);
        }
        catch (IOException e){
            e.printStackTrace();
        }*/
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
        fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null){
                    try {
                        Geocoder geocoder = new Geocoder(CameraActivity.this, Locale.getDefault());
                        List<Address> addresses = null;
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        System.out.println(addresses.get(0).getAddressLine(0));
                        address = addresses.get(0).getAddressLine(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public String getBitmap(String a, String b){
        String fotoBaru = "";
        try {
            Uri selectedImage = Uri.fromFile(new File(b));
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            if(a.equalsIgnoreCase("register")){
                Bitmap foto = ImageCompression.reduceBitmapSize(bitmap, 40000);
                fotoBaru = encodeUri(foto);
            }else{
                Bitmap foto = ImageCompression.reduceBitmapSize(bitmap, 40000);
                fotoBaru = encodeUri(foto);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fotoBaru;
    }

    private String encodeUri(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        return image;
    }
}
