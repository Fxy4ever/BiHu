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
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AskQuestionActivity extends AppCompatActivity {
    private mUser user;
    private Button picture;
    private int OPEN_PHOTO = 1 ;
    private Button back;
    private Button commit;

    private String Image_path;
    private ImageView showImage;

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
        setContentView(R.layout.activity_ask_question);
        initCenjin();
       initButton();
       initPicture();
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

    public void initButton(){
        back = findViewById(R.id.ask_back);
        commit = findViewById(R.id.ask_commit);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AskQuestionActivity.this.finish();
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commitWord();
            }
        });
    }

    private void commitWord(){
        uploadPicture();
            if(is_succeed==true){

                EditText title_edit = findViewById(R.id.ask_title);
                EditText content_edit = findViewById(R.id.ask_content);
                String title = null;
                String content = null;
                try {
                    title = URLEncoder.encode(title_edit.getText().toString(),"UTF-8");
                    content =  URLEncoder.encode(content_edit.getText().toString(),"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if(title.length()>0&&content.length()>0){
                    user = (mUser)getApplication();
                    String token = user.getToken();
                    String url = "http://bihu.jay86.com/question.php";
                    StringBuilder ask = new StringBuilder();

                    String fileName = "p4sze9l87.bkt.clouddn.com";
                    final String downUrl = "http://" +fileName + "/" + upKey;

                    ask.append("title="+title+"&content="+content+"&images="+downUrl+"?imageView2/2/w/200/h/200/q/75|imageslim"+"&token="+token);
                    NetUtils.post(url, ask.toString(), new NetUtils.Callback() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                int status = object.getInt("status");
                                if(status==200){
                                    Toast.makeText(AskQuestionActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                                    AskQuestionActivity.this.finish();
                                }else{

                                    Toast.makeText(AskQuestionActivity.this,String.valueOf(status),Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });



                }
                else if(title.length()==0||content.length()==0){
                    Toast.makeText(AskQuestionActivity.this,"标题或正文为空",
                            Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(AskQuestionActivity.this,"正在上传图片,请等待",Toast.LENGTH_SHORT).show();
            }
    }

    public void uploadPicture(){
        if(TextUtils.isEmpty(uptoken)){
            Log.d("fxy", "clickPost: ");
            Toast.makeText(AskQuestionActivity.this,"正在获取token,请等待",Toast.LENGTH_SHORT).show();
        }
        if(Image_path!=null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    uploadManager.put(Image_path, upKey, uptoken, new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            Log.d("qiniu", "path="+Image_path+"  upkey="+upKey+"  uptoken="+uptoken);
                            if(info.isOK()){
                                Log.d("qiniu", "complete:succeed ");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AskQuestionActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                                        is_succeed=true;
                                    }
                                });
                            }else{
                                Log.d("qiniu" ,"complete:failed ");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AskQuestionActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    },null);
                }
            }).start();
        }else {
            Toast.makeText(AskQuestionActivity.this,"请上传头像",Toast.LENGTH_SHORT).show();
        }
    }

    public void initPicture(){
        showImage = findViewById(R.id.ask_showPicture);
        picture = findViewById(R.id.ask_picture);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPicFromPhoto();
            }
        });
    }
    private void getPicFromPhoto(){
        if(ContextCompat.checkSelfPermission(AskQuestionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AskQuestionActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},OPEN_PHOTO);
        }else{
            openPhoto();
        }
    }
    private void openPhoto(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            if(requestCode==OPEN_PHOTO){
                if(Build.VERSION.SDK_INT>=19){
                    handleImageOnKitKat(data);
                }else{
                    handleImagebeforKitKat(data);
                }
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
        Image_path = imagePath;
        Log.d("fxy", "imagePath: "+ Image_path);
        displayImage(imagePath);
    }
    private void handleImagebeforKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }
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
    private void displayImage(String imagePath){
        if(imagePath!=null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            showImage.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode== OPEN_PHOTO){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openPhoto();
            }else{
                Toast.makeText(this, "请开启权限", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
