package com.example.mac.bihu.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mac.bihu.R;
import com.example.mac.bihu.Utils.NetUtils;
import com.example.mac.bihu.adapter.mFavoriteAdapter;
import com.example.mac.bihu.adapter.mRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    private mUser user;

    private RecyclerView recyclerView;
    private Toolbar toolbar;

    private List<Bitmap> imageslist;
    private List<String> datelist;
    private int[] answerCountlist;
    private List<String> authorNamelist;
    private List<Bitmap> authorAvatarlist;
    private List<String> titlelist;
    private List<String> contentlist;
    private int[] exciting;
    private int[] naive;
    private List<String> recentlist;
    private boolean[] is_exciting;
    private boolean[] is_naive;

    private Handler handler = new Handler();

    private LinearLayoutManager layoutManager;
    private mFavoriteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        init();
        initButtonClick();
    }
    public void init(){
        toolbar = findViewById(R.id.favorite_toolbar);
        toolbar.setTitle("我的收藏");
        titlelist = new ArrayList<>();
        contentlist = new ArrayList<>();
        imageslist = new ArrayList<>();
        datelist = new ArrayList<>();
        exciting = new int[40];
        naive = new int[40];
        recentlist = new ArrayList<>();
        answerCountlist = new int[40];
        authorNamelist = new ArrayList<>();
        authorAvatarlist = new ArrayList<>();
        is_exciting = new boolean[40];
        is_naive = new boolean[40];

        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://bihu.jay86.com/getFavoriteList.php";
                StringBuilder getItem = new StringBuilder();
                user =(mUser) getApplication();
                String token = user.getToken();
                getItem.append("page=0" + "&token=" + token);
                final String getItemrespon = NetUtils.post(url, getItem.toString());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject1 = new JSONObject(getItemrespon);
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
                                recentlist.add(object.getString("recent"));
                                answerCountlist[i] = object.getInt("answerCount");
                                authorNamelist.add(object.getString("authorName"));
                                Log.d("fxy", ""+titlelist.size());
                                if (object.getString("images") != null) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Bitmap images = null;
                                            try {
                                                images = NetUtils.getBitmap(object.getString("images"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            imageslist.add(images);
                                        }
                                    }).start();
                                }
                                if (object.getString("authorAvatar") != null) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Bitmap avatar = null;
                                            try {
                                                avatar = NetUtils.getBitmap(object.getString("authorAvatar"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            authorAvatarlist.add(avatar);
                                        }
                                    }).start();
                                }
                                is_exciting[i] = object.getBoolean("is_exciting");
                                is_naive[i] = object.getBoolean("is_naive");
                            }
                            Log.d("fxy", ""+titlelist.size());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }).start();

        Log.d("fxy", "即将要绑定的size"+titlelist.size());
        recyclerView=findViewById(R.id.favorite_recyclerview);
        layoutManager = new LinearLayoutManager(this);

        adapter = new mFavoriteAdapter(imageslist, datelist, recentlist,
                answerCountlist, authorNamelist, authorAvatarlist, titlelist,
                contentlist, exciting, naive, recentlist, is_exciting, is_naive);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        setTitle("我的收藏");
        setSupportActionBar(toolbar);


    }
    public void initButtonClick(){
        Button btn1 = findViewById(R.id.favorite_back_btn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FavoriteActivity.this,MainActivity.class);
                startActivity(intent);
                FavoriteActivity.this.finish();
            }
        });

        adapter.onItemClickListner(new mFavoriteAdapter.OnItemClickListener() {
            @Override
            public void OnClickItem(View view, int position) {
                Toast.makeText(FavoriteActivity.this,"第"+position+"个item",Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnItemCommentsClickListener(new mFavoriteAdapter.onItemCommentsListener() {
            @Override
            public void onCommentsClick(int i) {
                Toast.makeText(FavoriteActivity.this,"第"+i+"个comments",Toast.LENGTH_SHORT).show();

            }
        });
        adapter.setOnItemGoodClickListener(new mFavoriteAdapter.onItemGoodListener() {
            @Override
            public void onGoodClick(int i) {
                Toast.makeText(FavoriteActivity.this,"第"+i+"个good",Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnItemBadClickListener(new mFavoriteAdapter.onItemBadListener() {
            @Override
            public void onBadClick(int i) {
                Toast.makeText(FavoriteActivity.this,"第"+i+"个bad",Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnItemLikeClickListener(new mFavoriteAdapter.onItemLikeListener() {
            @Override
            public void onLikeClick(int i) {
                Toast.makeText(FavoriteActivity.this,"第"+i+"个like",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
