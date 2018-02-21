package com.example.mac.bihu.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mac.bihu.R;
import com.example.mac.bihu.Utils.NetUtils;
import com.example.mac.bihu.adapter.MyInsideRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class itemTouchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyInsideRecyclerAdapter adapter;
    private LinearLayoutManager manager;

    private List<String> datelist;
    private List<String> authorNamelist;
    private List<String> contentlist;
    private int[]         exciting;
    private int[]         naive;
    private boolean[] is_exciting;
    private boolean[] is_naive;

    int qid;

    private mUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_touch);
        initData();
        initThread();
        initRecyclerview();
        initItemButton();
    }
    public void initData(){
        datelist = new ArrayList<>();
        authorNamelist = new ArrayList<>();
        contentlist = new ArrayList<>();
        exciting = new int[40];
        naive = new int[40];
        is_exciting = new boolean[40];
        is_naive = new boolean[40];
        Bundle bundle = getIntent().getExtras();
        qid = bundle.getInt("qid");
        user = (mUser) getApplication();
    }
    public void initThread(){
                String url = "http://bihu.jay86.com/getAnswerList.php";
                StringBuilder request = new StringBuilder();
                request.append("page="+"0"+"&qid="+qid+"&token="+user.getToken());
                NetUtils.post(url, request.toString(), new NetUtils.Callback() {
                    @Override
                    public void onResponse(String response)  {
                        try {
                            Log.d("fxy", "onResponse: "+response);
                            JSONObject jsonObject = new JSONObject(response);
                            String data = jsonObject.getString("data");
                            JSONObject jsonObject1 = new JSONObject(data);
                            String answer = jsonObject1.getString("answers");
                            JSONArray jsonArray = new JSONArray(answer);
                            for(int i = 0 ; i < jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                datelist.add(object.getString("date"));
                                authorNamelist.add(object.getString("authorName"));
                                contentlist.add(object.getString("content"));
                                exciting[i] = object.getInt("exciting");
                                naive[i] = object.getInt("naive");
                                is_exciting[i] = object.getBoolean("is_exciting");
                                is_naive[i] = object.getBoolean("is_naive");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }
    public void initRecyclerview(){
        recyclerView = findViewById(R.id.inside_recyclerview);
        adapter = new MyInsideRecyclerAdapter(datelist,authorNamelist,contentlist,exciting,naive,
                is_exciting,is_naive);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }
    public void initItemButton(){
        Button btn_back = findViewById(R.id.inside_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(itemTouchActivity.this,MainActivity.class);
                startActivity(intent);
                itemTouchActivity.this.finish();
            }
        });


        adapter.setOnItemGoodClickListener(new MyInsideRecyclerAdapter.onItemGoodListener() {
            @Override
            public void onGoodClick(int i) {
                Toast.makeText(itemTouchActivity.this,"第"+i+"个good",Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnItemBadClickListener(new MyInsideRecyclerAdapter.onItemBadListener() {
            @Override
            public void onBadClick(int i) {
                Toast.makeText(itemTouchActivity.this,"第"+i+"个bad",Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnItemLikeClickListener(new MyInsideRecyclerAdapter.onItemLikeListener() {
            @Override
            public void onLikeClick(int i) {
                Toast.makeText(itemTouchActivity.this,"第"+i+"个like",Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnItemLikeClickListener(new MyInsideRecyclerAdapter.onItemLikeListener() {
            @Override
            public void onLikeClick(int i) {
                Toast.makeText(itemTouchActivity.this,"第"+i+"个item",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
