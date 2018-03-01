package com.example.mac.bihu.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.mac.bihu.R;
import com.example.mac.bihu.Utils.NetUtils;
import com.example.mac.bihu.adapter.mFavoriteAdapter;
import com.example.mac.bihu.mUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    private mUser user;

    private RecyclerView recyclerView;
    private Toolbar toolbar;

    private List<String> imageslist;
    private List<String> datelist;
    private int[] answerCountlist;
    private List<String> authorNamelist;
    private List<String> authorAvatarlist;
    private List<String> titlelist;
    private List<String> contentlist;
    private int[] exciting;
    private int[] naive;
    private List<String> recentlist;
    private boolean[] is_exciting;
    private boolean[] is_naive;

    public static int[] fav_questionId;

    private LinearLayoutManager layoutManager;
    private mFavoriteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        initCenjin();
        initThread();
        init();
        initToolbar();
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
    public void initThread(){
        titlelist = new ArrayList<>();
        contentlist = new ArrayList<>();
        imageslist = new ArrayList<>();
        datelist = new ArrayList<>();
        exciting = new int[200];
        naive = new int[200];
        recentlist = new ArrayList<>();
        answerCountlist = new int[200];
        authorNamelist = new ArrayList<>();
        authorAvatarlist = new ArrayList<>();
        is_exciting = new boolean[200];
        is_naive = new boolean[200];
        fav_questionId = new int[200];
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://bihu.jay86.com/getFavoriteList.php";
                StringBuilder getItem = new StringBuilder();
                user =(mUser) getApplication();
                String token = user.getToken();
                getItem.append("page=0" +"count=20"+ "&token=" + token);
                NetUtils.post(url, getItem.toString(), new NetUtils.Callback() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            String data = jsonObject1.getString("data");
                            JSONObject jsonObject2 = new JSONObject(data);
                            String questions = jsonObject2.getString("questions");
                            JSONArray jsonArray = new JSONArray(questions);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                final JSONObject object = jsonArray.getJSONObject(i);
                                titlelist.add(object.getString("title"));
                                contentlist.add(object.getString("content"));
                                datelist.add(object.getString("date"));
                                exciting[i] = object.getInt("exciting");
                                naive[i] = object.getInt("naive");
                                authorAvatarlist.add(object.getString("authorAvatar"));
                                imageslist.add(object.getString("images"));
                                recentlist.add(object.getString("recent"));
                                answerCountlist[i] = object.getInt("answerCount");
                                authorNamelist.add(object.getString("authorName"));
                                is_exciting[i] = object.getBoolean("is_exciting");
                                is_naive[i] = object.getBoolean("is_naive");
                                fav_questionId[i] = object.getInt("id");
                            }
                            adapter = new mFavoriteAdapter(imageslist, datelist, recentlist,
                                    answerCountlist, authorNamelist, authorAvatarlist, titlelist,
                                    contentlist, exciting, naive, recentlist, is_exciting, is_naive,user.getToken());
                            recyclerView=findViewById(R.id.favorite_recyclerview);
                            layoutManager = new LinearLayoutManager(FavoriteActivity.this);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                            initButtonClick();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        }).start();
    }
    public void initToolbar(){
        toolbar = findViewById(R.id.favorite_toolbar);
        toolbar.setTitle("我的收藏");

    }
    public void init(){
        setTitle("我的收藏");
        setSupportActionBar(toolbar);
    }
    public void initButtonClick(){
        Button btn1 = findViewById(R.id.favorite_back_btn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavoriteActivity.this.finish();
            }
        });

        adapter.onItemClickListner(new mFavoriteAdapter.OnItemClickListener() {
            @Override
            public void OnClickItem(View view, int position) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                intent.setClass(FavoriteActivity.this,itemTouchActivity.class);
                bundle.putInt("qid",fav_questionId[position]);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        adapter.setOnItemCommentsClickListener(new mFavoriteAdapter.onItemCommentsListener() {
            @Override
            public void onCommentsClick(int i) {
                Intent intent = new Intent();
                intent.setClass(FavoriteActivity.this,AnswerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id",fav_questionId[i]);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

}
