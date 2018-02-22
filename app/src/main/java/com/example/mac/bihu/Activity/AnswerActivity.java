package com.example.mac.bihu.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                Intent intent = new Intent(AnswerActivity.this,MainActivity.class);
                startActivity(intent);
                AnswerActivity.this.finish();
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = editText.getText().toString();
                user = (mUser) getApplication();
                String token = user.getToken();
                String url = "http://bihu.jay86.com/answer.php";
                Bundle bundle = getIntent().getExtras();
                int id = bundle.getInt("id");
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
                                       Intent intent = new Intent(AnswerActivity.this,MainActivity.class);
                                       startActivity(intent);
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
    }
}
