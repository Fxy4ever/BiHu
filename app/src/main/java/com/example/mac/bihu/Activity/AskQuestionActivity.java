package com.example.mac.bihu.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mac.bihu.R;
import com.example.mac.bihu.Utils.NetUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class AskQuestionActivity extends AppCompatActivity {
    private mUser user;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        Button back = findViewById(R.id.ask_back);
        final Button commit = findViewById(R.id.ask_commit);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_back = new Intent(AskQuestionActivity.this,MainActivity.class);
                startActivity(intent_back);
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText title_edit = findViewById(R.id.ask_title);
                EditText content_edit = findViewById(R.id.ask_content);
                final String title = title_edit.getText().toString();
                final String content = content_edit.getText().toString();
                if(title.length()>0&&content.length()>0){
                            user = (mUser)getApplication();
                            String token = user.getToken();
                            String url = "http://bihu.jay86.com/question.php";
                            StringBuilder ask = new StringBuilder();
                            ask.append("title="+title+"&content="+content+"&token="+token);
                            NetUtils.post(url, ask.toString(), new NetUtils.Callback() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject object = new JSONObject(response);
                                        int status = object.getInt("status");
                                        if(status==200){
                                            Toast.makeText(AskQuestionActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                                            Intent intent_back = new Intent(AskQuestionActivity.this,MainActivity.class);
                                            startActivity(intent_back);
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
            }
        });
    }
}
