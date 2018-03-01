package com.example.mac.bihu.Activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mac.bihu.R;
import com.example.mac.bihu.Utils.NetUtils;
import com.example.mac.bihu.mUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AnswerActivity extends AppCompatActivity {
    private EditText editText;
    mUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        final Button back = findViewById(R.id.answer_back);
        Button commit = findViewById(R.id.answer_commit);
        editText = findViewById(R.id.answer_content);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnswerActivity.this.finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        final int id = bundle.getInt("id");
        Log.d("fxy", "id= "+ id);

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = null;
                try {
                    content = URLEncoder.encode(editText.getText().toString(),"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                user = (mUser) getApplication();
                String token = user.getToken();
                String url = "http://bihu.jay86.com/answer.php";
                StringBuilder answer = new StringBuilder();
                answer.append("qid="+id+"&content="+content+"&token="+token);
                Log.d("fxy", "answer: "+answer.toString());
               NetUtils.post(url, answer.toString(), new NetUtils.Callback() {
                   @Override
                   public void onResponse(String response) {
                       try {
                           JSONObject jsonObject = new JSONObject(response);
                           int status = jsonObject.getInt("status");
                           if(status==200){
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       Toast.makeText(AnswerActivity.this,"回答成功",Toast.LENGTH_SHORT).show();
                                       AnswerActivity.this.finish();
                                   }
                               });
                           }else{
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       Toast.makeText(AnswerActivity.this,"网络错误 回答失败",Toast.LENGTH_SHORT).show();
                                   }
                               });
                           }
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               });
            }
        });
        initCenjin();
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
}
