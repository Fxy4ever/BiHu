package com.example.mac.bihu.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mac.bihu.R;
import com.example.mac.bihu.Utils.NetUtils;
import com.example.mac.bihu.Utils.NetWorkUtil;
import com.example.mac.bihu.mUser;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity  {
    private mUser user;
    private Handler handler = new Handler();

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private EditText  username;
    private EditText  password;
    private Button login_btn;
    private Button regist_btn;
    private CheckBox rememberPass;
    boolean isRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initCenjin();
        initView();
        RememberPass();
        initButton();
        if(!NetWorkUtil.isNetAvailable(this)){
            Toast.makeText(this,"网络阻塞 请检查网络连接",Toast.LENGTH_SHORT).show();
        }

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
    public void initView(){
        username = findViewById(R.id.login_user);
        password = findViewById(R.id.login_password);
        login_btn = findViewById(R.id.login_login);
        regist_btn = findViewById(R.id.login_register);
        rememberPass = findViewById(R.id.check_password);
    }
    public void RememberPass(){
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        isRemember = preferences.getBoolean("remember_password",false);
        if(isRemember){
            String account = preferences.getString("account","");
            String pass_word = preferences.getString("password","");
            username.setText(account);
            password.setText(pass_word);
            rememberPass.setChecked(true);//很重要
        }
    }

    
    public void initButton(){

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 这里实现登陆逻辑
                 */
                StringBuilder login = new StringBuilder();

                String url = "http://bihu.jay86.com/login.php";

                login.append("username="+username.getText().toString()
                        +"&password=" + password.getText().toString());

                editor = preferences.edit();
                if(rememberPass.isChecked()){
                    editor.putBoolean("remember_password",true);
                    editor.putString("account",username.getText().toString());
                    editor.putString("password",password.getText().toString());
                }else{
                    editor.clear();
                }
                editor.apply();

                NetUtils.post(url, login.toString(), new NetUtils.Callback() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            int status = obj.getInt("status");
                            if(status==200){
                                String data = obj.getString("data");
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

        regist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this,RegistActivity.class);
                startActivity(intent);
            }
        });
    }
}
