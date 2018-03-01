package com.example.mac.bihu.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.bihu.R;
import com.example.mac.bihu.Utils.NetUtils;
import com.example.mac.bihu.mUser;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class ChangeAvatarActivity extends AppCompatActivity implements  View.OnClickListener {
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private ImageView showImage;
    private Uri imageUri;
    private String image_Path;

    private mUser user = (mUser) getApplication();

    private TextView title;//显示上传进度

    private UploadManager uploadManager;//七牛SDK管理者

    private final static String TOKEN_URL = "http://zzzia.net/qiniu/";//请求返回token
    private static String uptoken;//服务器请求的token
    private String accessKey="KBmqZoICK5wTbYCPLI934g_zv0Zitfbf3-6zRwT7";
    private String secretKey="R_JBA9UyV_zGohkjpyxnwfWaARsm1FkChvyVlMYr";
    private String bucket = "fxymine3ever";
    private String upKey;

    private boolean is_succeed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_avatar);
        initCenjin();
        clickPost();
        initView();
        initData();
    }
    private void initCenjin(){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(option);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView1 = getWindow().getDecorView();
            int option1 = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView1.setSystemUiVisibility(option1);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }
    private void getTokenFromService(){
        StringBuilder ask = new StringBuilder();
        ask.append("accessKey="+accessKey+"&secretKey="+secretKey+"&bucket="+bucket);
        NetUtils.post(TOKEN_URL, ask.toString(), new NetUtils.Callback() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("fxy", "getTokenFromService: "+response);
                    JSONObject jsonObject = new JSONObject(response);
                    uptoken = jsonObject.getString("token");
                    Log.d("fxy", "getTokenFromService: "+uptoken);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initData(){
        Log.d("fxy", "initData: ");
        getTokenFromService();
        upKey = "image" + String.valueOf(Math.random());
        Configuration config = new Configuration.Builder()
                .zone(Zone.zone0)
                .build();
        uploadManager = new UploadManager(config);
    }

    private void initView(){
        Log.d("fxy", "initView: ");
        findViewById(R.id.change_camera).setOnClickListener(this);
        findViewById(R.id.change_picture).setOnClickListener(this);
        showImage = findViewById(R.id.see_pic);
        title = findViewById(R.id.see_infor);
        Button back = findViewById(R.id.change_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeAvatarActivity.this.finish();
            }
        });
    }

    public void clickPost(){
        Log.d("fxy", "clickPost: ");
        Button post = findViewById(R.id.change_ask);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(uptoken)){
                    Log.d("fxy", "clickPost: ");
                    Toast.makeText(ChangeAvatarActivity.this,"正在获取token,请等待",Toast.LENGTH_SHORT).show();
                }
                    if(image_Path!=null){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                uploadManager.put(image_Path, upKey, uptoken, new UpCompletionHandler() {
                                    @Override
                                    public void complete(String key, ResponseInfo info, JSONObject response) {
                                        Log.d("qiniu", "path="+image_Path+"  upkey="+upKey+"  uptoken="+uptoken);
                                        if(info.isOK()){
                                            Log.d("qiniu", "complete:succeed ");
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    title.setText("上传成功");
                                                    is_succeed=true;
                                                }
                                            });
                                        }else{
                                            Log.d("qiniu" ,"complete:failed ");
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    title.setText("上传失败");
                                                    is_succeed=false;
                                                }
                                            });
                                        }
                                    }
                                },null);
                                    ChangeAvatarFromFuwuqi();


                            }
                        }).start();
                    }else {
                        Toast.makeText(ChangeAvatarActivity.this,"请上传头像",Toast.LENGTH_SHORT).show();
                    }

            }
        });
    }

//    public void clickCommit(){//没写数据库好像不用下载。。。
//        Button commit = findViewById(R.id.change_commit);
//        commit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String fileName = "p4lin3s6h.bkt.clouddn.com";
//                        String downUrl = "http://" +fileName + "/" + upKey;
//                        Log.d("fxy", "downUrl: "+downUrl);
//                        SyncHttpClient client = new SyncHttpClient();
//                        client.get(downUrl, new BinaryHttpResponseHandler() {
//                            @Override
//                            public void onSuccess(int statusCode, Header[] headers, final byte[] binaryData) {
//                                if(binaryData != null){
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            showImage.setImageBitmap(BitmapFactory.decodeByteArray(binaryData,0,binaryData.length));
//                                            title.setText("成功更换头像!");
//                                        }
//                                    });
//                                }
//                            }
//                            @Override
//                            public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        title.setText("更换头像失败!");
//                                    }
//                                });
//                            }
//                        });
//                    }
//                }).start();
//            }
//        });
//    }

    private void ChangeAvatarFromFuwuqi(){
           user = (mUser) getApplication();
           String token = user.getToken();
           StringBuilder ask = new StringBuilder();
           String fileName = "p4sze9l87.bkt.clouddn.com";
           final String downUrl = "http://" +fileName + "/" + upKey;
           ask.append("token="+token+"&avatar="+downUrl+"?imageView2/2/w/200/h/200/q/75|imageslim");
           String url = "http://bihu.jay86.com/modifyAvatar.php";
           NetUtils.post(url, ask.toString(), new NetUtils.Callback() {
               @Override
               public void onResponse(String response) {
                   try {
                       JSONObject jsonObject = new JSONObject(response);
                       int status = jsonObject.getInt("status");
                       if(status==200){
                           ChangeAvatarActivity.this.finish();
                           Toast.makeText(ChangeAvatarActivity.this,"更换成功 请等待图片上传10s钟",Toast.LENGTH_SHORT).show();
                           Intent intent = new Intent(ChangeAvatarActivity.this,LoginActivity.class);
                           startActivity(intent);
                       }else{
                           Toast.makeText(ChangeAvatarActivity.this,"头像更换失败 错误："+status,Toast.LENGTH_SHORT).show();
                       }
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }
           });
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
            default:
                break;
        }
    }

    private void getPicFromCamera(){
        //创建file对象 存储拍照后的图片 用getExternalCacheDir方法放在缓存目录 ／sdcard／Android/data/<packeageName>/cache
        //6.0后放在存储卡需要权限 放在缓存则不要
        File outputImage = new File(getExternalCacheDir(),"outputimg.jpg");

        image_Path = com.example.mac.bihu.Utils.getPathUtil.getCachePath(ChangeAvatarActivity.this)+ "/outputimg.jpg";
        if(outputImage.exists()){
            outputImage.delete();
        }
        try {
            outputImage.createNewFile();
            Log.d("fxy", "CameraPath=" + image_Path);
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
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads,documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads.public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {

            imagePath = uri.getPath();
        }
        image_Path = imagePath;
        Log.d("fxy", "imagePath: "+image_Path);
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
