package com.example.pictureview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected LinearLayoutManager mLayoutManager;

    private String INTENT_TYPE = "image/*";
    private final int REQUESTCODE = 100;
    private List<Bitmap> pictureList;

    private ImageView imageView;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 绑定组件
        button = (Button) findViewById(R.id.getPicture);

        // 写权限申请
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //没有授权进行权限申请
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

//        @SuppressLint("ResourceType")
//        InputStream is = getResources().openRawResource(R.drawable.girl1);
//        Bitmap mBitmap = BitmapFactory.decodeStream(is);
//        saveImage(mBitmap);

        pictureList = new ArrayList<>();

        // 启用相册
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUESTCODE);
            }
        });
    }

    private void saveImage(Bitmap toBitmap) {
        String insertImage = MediaStore.Images.Media.insertImage(getContentResolver(), toBitmap, "头像", "头像相关的图片");
        if (!TextUtils.isEmpty(insertImage)) {
            Toast.makeText(this, "图片保存成功!" + insertImage, Toast.LENGTH_SHORT).show();
            Log.e("打印保存路径", insertImage + "-");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Log.e("TAG--->onResult", "ActivityResult resultCode error");
            return;
        }

        Uri uri;
        List<String> fileList = new ArrayList<>();
        pictureList = new ArrayList<>();

        if (requestCode == REQUESTCODE && data != null) {
            ClipData imageNames = data.getClipData();
            if (imageNames != null) {
                for (int i = 0; i < imageNames.getItemCount(); i++) {
                    Uri imageUri = imageNames.getItemAt(i).getUri();
                    fileList.add(imageUri.toString());
                    uri = imageNames.getItemAt(i).getUri();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        pictureList.add(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                uri = data.getData();
                fileList.add(uri.toString());
            }
        } else {
            uri = data.getData();
            fileList.add(uri.toString());
        }

        // 等到选定了图片再进行显示
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CustomAdapter(pictureList);
        mRecyclerView.setAdapter(mAdapter);
    }

}
