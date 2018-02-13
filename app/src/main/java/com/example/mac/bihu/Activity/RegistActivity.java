package com.example.mac.bihu.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mac.bihu.R;
import com.example.mac.bihu.Utils.NetUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistActivity extends AppCompatActivity {
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        Button btn1 = findViewById(R.id.Reg_reg);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 这里实现注册逻辑 成功跳转登陆界面 失败Toast
                 */
                final EditText Reg_Username = findViewById(R.id.Reg_user);
                final EditText Reg_Password = findViewById(R.id.Reg_password);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = "http://bihu.jay86.com/register.php";
                        StringBuilder content = new StringBuilder();
                        content.append("username="+Reg_Username.getText().toString()
                        +"&password="+Reg_Password.getText().toString());
                        final String response = NetUtils.post(url,content.toString());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    int status = obj.getInt("status");
                                    if(status==200){
                                        Toast.makeText(RegistActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegistActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(RegistActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }
}
