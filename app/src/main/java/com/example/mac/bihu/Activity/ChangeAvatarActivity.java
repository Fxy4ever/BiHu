
package com.example.mac.bihu.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mac.bihu.R;
import com.qiniu.android.storage.UploadManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ChangeAvatarActivity extends AppCompatActivity implements  View.OnClickListener {
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private ImageView showImage;
    private Uri imageUri;

    private ImageView imageView;
    private ProgressDialog progressDialog;
    private boolean isProgressCancel;
    private UploadManager uploadManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_avatar);
        initView();
    }














    private void initView(){
        findViewById(R.id.change_camera).setOnClickListener(this);
        findViewById(R.id.change_picture).setOnClickListener(this);
        showImage = findViewById(R.id.see_pic);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.change_camera:
                getPicFromCamera();
                break;
            case R.id.change_picture:
                getPicFromPhoto();
                break;
        }
    }

    private void getPicFromCamera(){
        //创建file对象 存储拍照后的图片 用getExternalCacheDir方法放在缓存目录 ／sdcard／Android/data/<packeageName>/cache
        //6.0后放在存储卡需要权限 放在缓存则不要
        File outputImage = new File(getExternalCacheDir(),"outputimg.jpg");
        if(outputImage.exists()){
            outputImage.delete();
        }
        try {
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * 内容提供者要注册
         */
        if(Build.VERSION.SDK_INT>=24){
        //7.0直接使用本地真实uri是不安全的 FileProvider是一种特殊内容提供器
            // 可以选择性地将封装过后的uri提供给外部 提高安全性
            //第二个为任意唯一字符 第三个为刚刚的file
            imageUri = FileProvider.getUriForFile(this,"com.example.mac.bihu,fileprovider",outputImage);
        }else{
            imageUri = Uri.fromFile(outputImage);//真实路径
        }
        //判断相机权限
        if(Build.VERSION.SDK_INT>=23){
            if(ContextCompat.checkSelfPermission(ChangeAvatarActivity.this,Manifest.permission.CAMERA)
                    !=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(ChangeAvatarActivity.this,new String[]{Manifest.permission.CAMERA},TAKE_PHOTO);
            }else{
               openCamera();
            }
        }else{
            openCamera();
        }
    }

    private void openCamera(){
        //打开相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        指定图片输出地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        隐示意图启动
        startActivityForResult(intent, TAKE_PHOTO);
    }

    private void getPicFromPhoto(){
        if(ContextCompat.checkSelfPermission(ChangeAvatarActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ChangeAvatarActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},CHOOSE_PHOTO);
        }else{
            openPhoto();
        }
    }

    private void openPhoto(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(resultCode == RESULT_OK){
           switch (requestCode){
               case TAKE_PHOTO:
                   Log.d("fxy", "Take photo");
                   try {
                       Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                       showImage.setImageBitmap(bitmap);
                   } catch (FileNotFoundException e) {
                       e.printStackTrace();
                   }
                   break;
               case CHOOSE_PHOTO:
                   Log.d("fxy", "choose photo ");
                   if(Build.VERSION.SDK_INT>=19){
                       handleImageOnKitKat(data);
                   }else{
                       handleImagebeforKitKat(data);
                   }
                   break;
               default:
                   break;
           }
       }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
//        如果uri是document类型，就通过读取document id进行处理
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
//            如果Uri的Authority是media格式的话，document id还需要进行解析，然后通过字符串
//            分割方式取出后半部分才能得到真正的数字id
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads,documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads.public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            如果是file类型，直接获取图片路径
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImagebeforKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    /**
     * 获取图片真实路径
     */
    private String getImagePath(Uri uri,String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(null != cursor){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 根据图片路径显示图片
     */

    private void displayImage(String imagePath){
        if(imagePath!=null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            showImage.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 判断是否有相册权限
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openPhoto();
                }else{
                    Toast.makeText(this, "请开启权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
