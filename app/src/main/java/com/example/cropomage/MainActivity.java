package com.example.cropomage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button button,button1;
    Uri photoUri;
    String per[]={Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.ImageView);
        button = findViewById(R.id.btnAdd);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(per, 404);
            }

            }
        });
        button1=findViewById(R.id.camerabtn);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(per, 303);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imageView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
            if (resultCode == RESULT_OK) {
                if (requestCode == 101) {
                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "pickImageResult.jpeg");

                    Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                    if (uri != null) {
                        CropImage.activity(uri)
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(this);
                    }
                }
                if (requestCode == 202) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        CropImage.activity(uri)
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(this);
                    }
                }

            }

}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int cnt = 0;
        for (int i = 0; i < per.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                cnt++;
            }
        }
        if (cnt == 0) {
            if (requestCode == 303) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.resolveActivity(this.getPackageManager());
                File photoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "pickImageResult.jpeg");
                 photoUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, 101);

            }
            if (requestCode == 404) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 202);


            }
        }
    }
}