package com.myapplicationimage;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button camr,galry;
    ImageView choose_pic;
    private int PICK_IMAGE_REQUEST = 100;

    private Bitmap bitmap;

    private Uri filePath;

    private String selectedFilePath;

    public static final String TAG = "Upload Image";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camr= (Button) findViewById(R.id.camra_btn);
        galry= (Button) findViewById(R.id.galry_btn);
        choose_pic= (ImageView) findViewById(R.id.pic_image);

        galry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });



    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            selectedFilePath = getPath(filePath);
            Log.i(TAG, " File path : " + selectedFilePath);
            try {
                bitmap = android.provider.MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);

                       // MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploadImage(bitmap);
                choose_pic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void uploadImage(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
        byte[] byte_arr = stream.toByteArray();
        String imageStr = com.myapplicationimage.Base64.encodeBytes(byte_arr);

        Log.d("welcomeimge",imageStr);


       // return imageStr;
    }

    private String getPath(Uri filePath) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(filePath, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }
}
