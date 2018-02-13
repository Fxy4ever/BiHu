package com.example.mac.bihu.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mac.bihu.R;
import com.example.mac.bihu.adapter.MyInsideRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class itemTouchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyInsideRecyclerAdapter adapter;
    private LinearLayoutManager manager;

    private List<String> namelist;
    private List<String> contentlist;
    private List avatarList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_touch);
        initData();
        recyclerView = findViewById(R.id.inside_recyclerview);
        adapter = new MyInsideRecyclerAdapter(namelist,contentlist,avatarList);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        initItemButton();
    }
    public void initData(){
        namelist = new ArrayList<>();
        contentlist = new ArrayList<>();
        avatarList = new ArrayList();
        for(int i =0 ;i<8;i++){
            namelist.add("第"+i+"个评论员");
        }
        for(int i =0 ;i<8;i++){
            contentlist.add("第"+i+"条评论");
        }
        for(int i=0;i<8;i++){
          avatarList.add(R.drawable.a);
        }
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
