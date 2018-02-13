package com.example.mac.bihu.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.bihu.R;
import com.example.mac.bihu.Utils.NetUtils;
import com.example.mac.bihu.adapter.MyCarouseAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private mUser user;
    private Handler handler = new Handler();
    /**
     * 定义轮播图的东西
     */
    private ViewPager viewPager;
    private String[] datalist;
    private TextView carouse_tv;
    private int[] ImageId;
    private List<ImageView> imageViewList;
    private LinearLayout ll_point_container;
    private int prePosition = 0;
    boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
        initAdapter();
        initThread();


        Button login_btn = findViewById(R.id.login_login);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 这里实现登陆逻辑
                 */

                final EditText username = findViewById(R.id.login_user);
                final EditText password = findViewById(R.id.login_password);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        final StringBuilder login = new StringBuilder();

                        String url = "http://bihu.jay86.com/login.php";

                        login.append("username="+username.getText().toString()
                                +"&password=" + password.getText().toString());
                        final String response = NetUtils.post(url,login.toString());
                        Log.d("fxy", response);
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
                }).start();

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
    private void initView(){
        viewPager = findViewById(R.id.main_viewpager);
        viewPager.setOnPageChangeListener(this);
        carouse_tv = findViewById(R.id.carouse_tv);
        ll_point_container = findViewById(R.id.carouse_ll_pointContainer);
    }
    private void initData(){
        ImageId = new int[]{R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e};//这里传入图片
        datalist = new String[]{"长泽雅美","新垣结衣","桥本环奈",
                "渡边麻友","吉冈里帆"};
        imageViewList = new ArrayList<>();
        ImageView imageView;
        View pointView;
        LinearLayout.LayoutParams params;
        for(int i = 0;i<5;i++){
            imageView = new ImageView(this);
            imageView.setBackgroundResource(ImageId[i]);
            imageViewList.add(imageView);

            pointView = new View(this);
            pointView.setBackgroundResource(R.drawable.point);//这里传入小白点
            params = new LinearLayout.LayoutParams(5,5);
            if(i!=0)
                params.leftMargin = 10;
            pointView.setEnabled(false);
            ll_point_container.addView(pointView,params);

        }
    }
    private void initAdapter(){
        ll_point_container.getChildAt(0).setEnabled(true);
        carouse_tv.setText(datalist[0]);
        prePosition=0;
        viewPager.setAdapter(new MyCarouseAdapter(imageViewList));
        int pos = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % imageViewList.size());
        viewPager.setCurrentItem(500000);
    }
    private void initThread(){
        new Thread(){
            public void run(){
                isRunning = true;
                while (isRunning){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                        }
                    });
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int newPosition = position % imageViewList.size();
        carouse_tv.setText(datalist[newPosition]);
        ll_point_container.getChildAt(prePosition).setEnabled(false);
        ll_point_container.getChildAt(newPosition).setEnabled(true);
        prePosition=newPosition;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
