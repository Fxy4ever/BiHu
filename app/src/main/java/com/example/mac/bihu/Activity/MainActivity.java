package com.example.mac.bihu.Activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.bihu.R;
import com.example.mac.bihu.Utils.NetUtils;
import com.example.mac.bihu.adapter.mRecyclerViewAdapter;
import com.example.mac.bihu.mUser;
import com.example.mac.bihu.view.MyDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private mUser user;


    private TextView usernameTv;
    private CircleImageView userAvatar;


    private List<String> datelist;
    private int[] answerCountlist;
    private List<String> authorNamelist;
    private List<String> titlelist;
    private List<String> contentlist;
    private List<String> authorAvatarlist;
    private int[] exciting;
    private int[] naive;
    private List<String> recentlist;
    private boolean[] is_exciting;
    private boolean[] is_naive;
    private boolean[] is_favorite;
    public static int[] questionId;

    private mRecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;
    /**
     * 定义recyclerview的东西
     */
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    static RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isRefresh;
    private boolean fabOpened;
    private RelativeLayout layout;//写个蒙版
    private FloatingActionButton addQuestion;
    private FloatingActionButton addQuestion1;
    private FloatingActionButton addQuestion2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initFloatButton();
        initHeader();
        setToggle();
        setListener();
        initNewThread();
        initSwipe();
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
    public void initFloatButton(){
        layout = findViewById(R.id.cloud);
        addQuestion = findViewById(R.id.main_addQuestion);
        addQuestion1 = findViewById(R.id.main_addQuestion1);
        addQuestion2 = findViewById(R.id.main_addQuestion2);
        addQuestion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, AskQuestionActivity.class);
                startActivity(intent1);
            }
        });
        addQuestion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, WebViewActivity.class);
                startActivity(intent1);
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMenu(addQuestion);
            }
        });

        assert addQuestion != null;
        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!fabOpened){
                    openMenu(view);
                }else{
                    closeMenu(view);
                }
            }
        });
    }
    public void openMenu(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"rotation",0,-155,-135);//实现惯性
        animator.setDuration(500);
        animator.start();
        layout.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,0.7f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);
        layout.startAnimation(alphaAnimation);
        fabOpened = true;
    }
    public void closeMenu(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"rotation",-135,20,0);
        animator.setDuration(500);
        animator.start();
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.7f,0);
        alphaAnimation.setDuration(500);
        layout.startAnimation(alphaAnimation);
        layout.setVisibility(View.GONE);
        fabOpened=false;
    }

    public void initHeader() {
        View navigation_View = navigationView.inflateHeaderView(R.layout.header);
        usernameTv = navigation_View.findViewById(R.id.navigation_header_name);
        userAvatar = navigation_View.findViewById(R.id.navigation_header_avatar);
        String headerName = user.getUsername();
        usernameTv.setText(headerName);
        if (user.getAvatar().equals("null")) {
            Log.d("test", "avatar1="+user.getAvatar());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    userAvatar.setImageResource(R.mipmap.ic_launcher_round);
                }
            });
        } else {
            Log.d("test", "avatar2="+user.getAvatar());
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
                        Intent intent3 = new Intent(MainActivity.this, ChangeAvatarActivity.class);
                        startActivity(intent3);
                        break;
                    /**
                     * 更改密码的dialog
                     */
                    case R.id.little_item2:
                        //这是dialog
                        final MyDialog ChangePasswordDialog = new MyDialog(MainActivity.this);
                        ChangePasswordDialog.setDialogLayout(LayoutInflater.from(MainActivity.this).
                                inflate(R.layout.changepassword_dialog, null));
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
                                        user = (mUser) getApplication();
                                        String token = user.getToken();

                                        StringBuilder ask1 = new StringBuilder();
                                        String url = "http://bihu.jay86.com/changePassword.php";
                                        ask1.append("password=" + SecondPassword + "&token=" + token);
                                        NetUtils.post(url, ask1.toString(), new NetUtils.Callback() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject obj = new JSONObject(response);
                                                    int status = obj.getInt("status");
                                                    if (status == 200) {
                                                        Toast.makeText(MainActivity.this, "更改成功 请重新登陆", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                                                        startActivity(intent);
                                                    } else {
                                                        Toast.makeText(MainActivity.this, status, Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

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
                        Intent intent4 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent4);
                        MainActivity.this.finish();
                        break;

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    public void initNewThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadData();
                String url = "http://bihu.jay86.com/getQuestionList.php";
                StringBuilder getItem = new StringBuilder();
                String token = user.getToken();
                getItem.append("page=0" +"&count=20"+ "&token=" + token);
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
                                recentlist.add(object.getString("recent"));
                                authorAvatarlist.add(object.getString("authorAvatar"));
                                answerCountlist[i] = object.getInt("answerCount");
                                authorNamelist.add(object.getString("authorName"));
                                is_exciting[i] = object.getBoolean("is_exciting");
                                is_naive[i] = object.getBoolean("is_naive");
                                is_favorite[i] = object.getBoolean("is_favorite");
                                questionId[i] = object.getInt("id");
                                Log.d("fxy", "qid: "+questionId[i]);
                            }
                            initRecyclerView();
                            initButtonClick();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    public void loadData(){
        titlelist = new ArrayList<>();
        contentlist = new ArrayList<>();
        datelist = new ArrayList<>();
        exciting = new int[40];
        naive = new int[40];
        recentlist = new ArrayList<>();
        answerCountlist = new int[40];
        authorAvatarlist = new ArrayList<>();
        authorNamelist = new ArrayList<>();
        is_exciting = new boolean[40];
        is_naive = new boolean[40];
        is_favorite = new boolean[40];
        questionId = new int[40];
    }

    public void initRecyclerView() {
        recyclerView = findViewById(R.id.main_rec);
        adapter = new mRecyclerViewAdapter(datelist,answerCountlist,authorNamelist,titlelist,contentlist,exciting,naive,
                recentlist,is_exciting,is_naive,is_favorite,authorAvatarlist,user.getToken());
        recyclerView.setAdapter(adapter);
        /**
         * item点击事件
         */
        adapter.onItemClickListner(new mRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void OnClickItem(View view, int position) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, itemTouchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("qid",questionId[position]);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }
//    public void setScrollListner(){
//        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
//            @Override
//            public void onLoadMore(int currentPage) {
//                recyclerView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        adapter.notifyDataSetChanged();
//                    }
//                });
//            }
//        });
//    }



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
               bundle.putInt("id",questionId[i]);
               intent.putExtras(bundle);
               startActivity(intent);
           }
       });


    }


    /*
    这部分是关于下拉刷新的
     */
    public void initSwipe() {
        swipeRefreshLayout = findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    public void refresh(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                titlelist.clear();
                contentlist.clear();
                datelist.clear();
                recentlist.clear();
                authorNamelist.clear();
                for (int i = 0; i < titlelist.size(); i++) {
                    exciting[i] = 0;
                    naive[i] = 0;
                    answerCountlist[i] = 0;
                    is_exciting[i] = false;
                    is_naive[i] = false;
                    is_favorite[i] = false;
                    questionId[i] = 0;
                }String url = "http://bihu.jay86.com/getQuestionList.php";
                StringBuilder getItem = new StringBuilder();
                String token = user.getToken();
                getItem.append("page=0"+"&count=20"+"&token="+token);
                //加载
                NetUtils.post(url, getItem.toString(), new NetUtils.Callback() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
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
                                authorAvatarlist.add(object.getString("authorAvatar"));
                                answerCountlist[i] = object.getInt("answerCount");
                                authorNamelist.add(object.getString("authorName"));
                                is_exciting[i] = object.getBoolean("is_exciting");
                                is_naive[i] = object.getBoolean("is_naive");
                                is_favorite[i] = object.getBoolean("is_favorite");
                                questionId[i] = object.getInt("id");
                            }
                            adapter.refreshData(datelist,answerCountlist,authorNamelist,titlelist,contentlist,exciting,naive,
                                    recentlist,is_exciting,is_naive,is_favorite,authorAvatarlist,questionId);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

}


