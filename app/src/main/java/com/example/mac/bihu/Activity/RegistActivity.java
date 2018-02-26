package com.example.mac.bihu.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
        Button back = findViewById(R.id.Reg_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistActivity.this.finish();
            }
        });
        Button btn1 = findViewById(R.id.Reg_reg);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 这里实现注册逻辑 成功跳转登陆界面 失败Toast
                 */
                       EditText Reg_Username = findViewById(R.id.Reg_user);
                       EditText Reg_Password = findViewById(R.id.Reg_password);

                        String url = "http://bihu.jay86.com/register.php";
                        StringBuilder content = new StringBuilder();
                        content.append("username="+Reg_Username.getText().toString()
                        +"&password="+Reg_Password.getText().toString());

                        NetUtils.post(url, content.toString(), new NetUtils.Callback() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    int status = obj.getInt("status");
                                    if(status==200){
                                        Toast.makeText(RegistActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                        RegistActivity.this.finish();
                                    }else{
                                        Toast.makeText(RegistActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
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
