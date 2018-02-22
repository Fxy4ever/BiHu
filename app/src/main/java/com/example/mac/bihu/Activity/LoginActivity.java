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
import com.example.mac.bihu.mUser;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity  {
    private mUser user;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login_btn = findViewById(R.id.login_login);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 这里实现登陆逻辑
                 */

                EditText username = findViewById(R.id.login_user);
                EditText password = findViewById(R.id.login_password);

                StringBuilder login = new StringBuilder();

                String url = "http://bihu.jay86.com/login.php";

                login.append("username="+username.getText().toString()
                        +"&password=" + password.getText().toString());
                NetUtils.post(url, login.toString(), new NetUtils.Callback() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            int status = obj.getInt("status");
                            if(status==200){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject obj = new JSONObject(response);
                                            int status = obj.getInt("status");
                                            String data = obj.getString("data");


                                            if(status==200){
                                                JSONObject obj2 = new JSONObject(data);
                                                String username = obj2.getString("username");
                                                String avatar = obj2.getString("avatar");
                                                String token = obj2.getString("token");
                                                user = (mUser) getApplication();
                                                user.setUsername(username);
                                                user.setAvatar(avatar);
                                                user.setToken(token);
                                                Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                                startActivity(intent);
                                                LoginActivity.this.finish();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this,"登陆失败",Toast.LENGTH_SHORT).show();
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

        Button regist_btn = findViewById(R.id.login_register);
        regist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this,RegistActivity.class);
                startActivity(intent);
            }
        });
    }

}
