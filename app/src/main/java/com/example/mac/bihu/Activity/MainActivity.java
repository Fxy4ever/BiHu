package com.example.mac.bihu.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.bihu.R;
import com.example.mac.bihu.Listener.MyOnScrollListener;
import com.example.mac.bihu.Utils.NetUtils;
import com.example.mac.bihu.adapter.mRecyclerViewAdapter;
import com.example.mac.bihu.view.MyDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private mUser user;

    private TextView usernameTv;
    private RoundedImageView userAvatar;
    private Handler handler = new Handler();
    private Handler handler1 = new Handler();
    private Handler handler2 = new Handler();


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
    private boolean[] is_favorite;


    private mRecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;
    /**
     * 定义recyclerview的东西
     */
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initHeader();
        setToggle();
        setListener();
        initRecyclerView();
        initSwipe();
        initButtonClick();
        Toast.makeText(MainActivity.this, "欢迎来到Bihu", Toast.LENGTH_LONG).show();
    }


    /**
     * 从这里开始初始轮播图下的东西
     */
    public void init() {
        user = (mUser) getApplication();
        drawerLayout = findViewById(R.id.main_drawerLayout);
        toolbar = findViewById(R.id.main_toolbar);
        navigationView = findViewById(R.id.main_navigation);
        //navigation需要加载才能获取里面的控件 并去掉app：headerlayout
        //去除滑动条
        navigationView.getChildAt(0).setVerticalScrollBarEnabled(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("逼乎");
    }

    public void initHeader() {
        View navigation_View = navigationView.inflateHeaderView(R.layout.header);
        usernameTv = navigation_View.findViewById(R.id.navigation_header_name);
        userAvatar = navigation_View.findViewById(R.id.navigation_header_avatar);
        String headerName = user.getUsername();
        usernameTv.setText(headerName);
        if (user.getAvatar() != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = NetUtils.getBitmap(user.getAvatar());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            userAvatar.setImageBitmap(bitmap);
                        }
                    });
                }
            }).start();
        } else {
            userAvatar.setBackgroundResource(R.drawable.b);
        }
    }


    public void setToggle() {
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }


    public void setListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    /**
                     * 这里实现页面转换逻辑
                     */
                    case R.id.item_1:
                        Toast.makeText(MainActivity.this, "已经是首页了!"
                                , Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_2:
                        Intent intent1 = new Intent(MainActivity.this, AskQuestionActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.item_3:
                        Intent intent2 = new Intent(MainActivity.this, FavoriteActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.little_item1:
                        Toast.makeText(MainActivity.this, "更换头像还没做呢"
                                , Toast.LENGTH_SHORT).show();
                        break;
                    /**
                     * 更改密码的dialog
                     */
                    case R.id.little_item2:
                        //这是dialog
                        final MyDialog ChangePasswordDialog = new MyDialog(MainActivity.this);
                        ChangePasswordDialog.setDialogLayout(LayoutInflater.from(MainActivity.this).
                                inflate(R.layout.changepassword_dialog, null));
                        ChangePasswordDialog.setDialogHeightWidth(600, 1000);
                        ChangePasswordDialog.show();
                        Window pass_window = ChangePasswordDialog.getWindow();

                        //返回button
                        Button pass_exit_Btn = pass_window.findViewById(R.id.password_dialog_exit);
                        pass_exit_Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ChangePasswordDialog.hide();
                            }
                        });


                        Button pass_makesure_Btn = pass_window.findViewById(R.id.password_dialog_makesure);
                        pass_makesure_Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Window pass_window1 = ChangePasswordDialog.getWindow();
                                EditText pass_firstPassword = pass_window1.findViewById(R.id.password_dialog_first);
                                EditText pass_secondpassword = pass_window1.findViewById(R.id.password_dialog_second);
                                String firstPassword = pass_firstPassword.getText().toString();
                                final String SecondPassword = pass_secondpassword.getText().toString();


                                if (firstPassword.length() > 0 && SecondPassword.length() > 0) {
                                    if (firstPassword.equals(SecondPassword)) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                user = (mUser) getApplication();
                                                String token = user.getToken();

                                                StringBuilder ask1 = new StringBuilder();
                                                String url = "http://bihu.jay86.com/changePassword.php";
                                                ask1.append("password=" + SecondPassword + "&token=" + token);

                                                final String response1 = NetUtils.post(url, ask1.toString());
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            JSONObject obj = new JSONObject(response1);
                                                            int status = obj.getInt("status");
                                                            if (status == 200) {
                                                                Toast.makeText(MainActivity.this, "更改成功", Toast.LENGTH_SHORT).show();
                                                                ChangePasswordDialog.hide();
                                                            } else {
                                                                Toast.makeText(MainActivity.this, status, Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                });
                                            }
                                        }).start();

                                    } else {
                                        Toast.makeText(MainActivity.this, "两次密码不同 更改失败", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "您还未输入密码", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        break;
                    case R.id.little_item3:
                        Intent intent3 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent3);
                        MainActivity.this.finish();
                        break;

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    public void initRecyclerView() {
        
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
        is_favorite = new boolean[40];
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://bihu.jay86.com/getQuestionList.php";
                StringBuilder getItem = new StringBuilder();
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
                                is_favorite[i] = object.getBoolean("is_favorite");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }).start();
        recyclerView = findViewById(R.id.main_rec);
        adapter = new mRecyclerViewAdapter(imageslist, datelist, recentlist, answerCountlist, authorNamelist
                , authorAvatarlist, titlelist, contentlist, exciting, naive, recentlist, is_exciting, is_naive, is_favorite);
        recyclerView.setAdapter(adapter);
        /**
         * item点击事件
         */
        adapter.onItemClickListner(new mRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void OnClickItem(View view, int position) {
                Toast.makeText(MainActivity.this, "点击了第" + position + "个item",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, itemTouchActivity.class);
                startActivity(intent);
            }
        });
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        /**
         * 上拉加载
         */

        recyclerView.addOnScrollListener(new MyOnScrollListener(layoutManager) {
            @Override
            public void onLoad(int curPage) {
                if (layoutManager.getItemCount() <40) {
                    loading();
                } else {
                    Toast.makeText(MainActivity.this, "无法加载更多了", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    int j = 1;
    public void loading(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = "http://bihu.jay86.com/getQuestionList.php";
                        StringBuilder getItem = new StringBuilder();
                        String token = user.getToken();
                        getItem.append("page=" +j+ "&token=" + token);
                        final String getItemrespon = NetUtils.post(url, getItem.toString());
                        Log.d("fxy", "loading start ");
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
                                        Log.e("fxy", "namelist.size = " + authorNamelist.size());
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
                                        is_favorite[i] = object.getBoolean("is_favorite");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                }).start();

                adapter.notifyDataSetChanged();
                j++;
            }
        },2000);
    }

    /**
     * item里面的button点击
     */
    public void initButtonClick() {
        adapter.setOnItemCommentsClickListener(new mRecyclerViewAdapter.onItemCommentsListener() {
            @Override
            public void onCommentsClick(int i) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,AnswerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id",i);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        adapter.setOnItemGoodClickListener(new mRecyclerViewAdapter.onItemGoodListener() {
            @Override
            public void onGoodClick(int i) {
                Toast.makeText(MainActivity.this, "第" + i + "个good", Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnItemBadClickListener(new mRecyclerViewAdapter.onItemBadListener() {
            @Override
            public void onBadClick(int i) {
                Toast.makeText(MainActivity.this, "第" + i + "个bad", Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnItemLikeClickListener(new mRecyclerViewAdapter.onItemLikeListener() {
            @Override
            public void onLikeClick(int i) {
                Toast.makeText(MainActivity.this, "第" + i + "个like", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*
    这部分是关于下拉刷新的
     */
    public void initSwipe() {
        swipeRefreshLayout = findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
    }




    public void onRefresh() {
        Log.d("fxy", "onRefresh");
        if (!isRefresh) {
            isRefresh = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                    titlelist.clear();
                    contentlist.clear();
                    imageslist.clear();
                    datelist.clear();
                    recentlist.clear();
                    authorNamelist.clear();
                    authorAvatarlist.clear();
                    for (int i = 0; i < titlelist.size(); i++) {
                        exciting[i] = 0;
                        naive[i] = 0;
                        answerCountlist[i] = 0;
                        is_exciting[i] = false;
                        is_naive[i] = false;
                        is_favorite[i] = false;
                    }
                    //加载
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String url = "http://bihu.jay86.com/getQuestionList.php";
                            StringBuilder getItem = new StringBuilder();
                            String token = user.getToken();
                            getItem.append("page=0"+"&token="+token);
                            final String getItemrespon = NetUtils.post(url,getItem.toString());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject1 = new JSONObject(getItemrespon);
                                        String data = jsonObject1.getString("data");
                                        JSONObject jsonObject2 = new JSONObject(data);
                                        String questions = jsonObject2.getString("questions");
                                        JSONArray jsonArray = new JSONArray(questions);
                                        for(int i = 0;i < jsonArray.length();i++){
                                            final JSONObject object = jsonArray.getJSONObject(i);
                                            titlelist.add(object.getString("title"));
                                            contentlist.add(object.getString("content"));
                                            datelist.add(object.getString("date"));
                                            exciting[i] = object.getInt("exciting");
                                            naive[i] = object.getInt("naive");
                                            recentlist.add(object.getString("recent"));
                                            answerCountlist[i] = object.getInt("answerCount");
                                            authorNamelist.add(object.getString("authorName"));
                                            Log.e("fxy", "namelist.size = "+ authorNamelist.size());
                                            if(object.getString("images")!=null){
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
                                            if(object.getString("authorAvatar")!=null){
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
                                            is_favorite[i] = object.getBoolean("is_favorite");
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }
                    }).start();


                    adapter.notifyDataSetChanged();
                    isRefresh = false;
                }
            }, 2000);
        }
    }
}


