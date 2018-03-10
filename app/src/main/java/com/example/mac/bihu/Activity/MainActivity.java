package com.example.mac.bihu.Activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.bihu.R;
import com.example.mac.bihu.Utils.NetUtils;
import com.example.mac.bihu.Utils.mLayoutManager;
import com.example.mac.bihu.adapter.MyCarouseAdapter;
import com.example.mac.bihu.adapter.mRecyclerViewAdapter;
import com.example.mac.bihu.mUser;
import com.example.mac.bihu.view.MyDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity   {
    private mUser user;


    private TextView usernameTv;
    private CircleImageView userAvatar;

    private List<String> datelist;
    private int[] answerCountlist;
    private List<String> authorNamelist;
    private List<String> titlelist;
    private List<String> contentlist;
    private List<String> authorAvatarlist;
    private List<String> imageList;
    private int[] exciting;
    private int[] naive;
    private List<String> recentlist;
    private boolean[] is_exciting;
    private boolean[] is_naive;
    private boolean[] is_favorite;
    public static int[] questionId;

    private mRecyclerViewAdapter adapter;
    private com.example.mac.bihu.Utils.mLayoutManager layoutManager;

    private boolean is_refresh = false;
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

    private ViewPager viewPager;
    private List ImageviewList;
    private int[] imageView_id;
    private String[] dataList;
    private TextView carouse_word;
    private LinearLayout point_container;
    private int prePosition = 0;
    boolean isRunning = false;
    private View header_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCenjin();
        init();
        initFloatButton();
        initHeader();
        setToggle();
        initView();
        initData();
        initAdapter();
        initThread();

        setListener();
        initNewThread();
        initSwipe();
        Toast.makeText(MainActivity.this, "欢迎来到Bihu", Toast.LENGTH_LONG).show();
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



    private void initView(){
        header_view = LayoutInflater.from(this).inflate(R.layout.recyclerview_header_item,null);
        viewPager = header_view.findViewById(R.id.viewpager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int newPosition = position % ImageviewList.size();
                carouse_word.setText(dataList[newPosition]);
                point_container.getChildAt(prePosition).setEnabled(false);
                point_container.getChildAt(newPosition).setEnabled(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        point_container = header_view.findViewById(R.id.ll_point_container);
        carouse_word = header_view.findViewById(R.id.carouse_tv1);
    }
    private void initData(){
        imageView_id =new int[]{R.drawable.e,R.drawable.b,R.drawable.e,R.drawable.d,R.drawable.c};
        dataList = new String[]{"欢迎来到逼乎社区",
                "找不到的可以点右下角按钮",
                "这是吉冈里帆",
                "这是她代言的一个广告",
                "这是Gakki啦"};
        ImageviewList = new ArrayList<>();
        ImageView imageView;
        View pointView;
        LinearLayout.LayoutParams layoutParams;
        for(int i = 0,len = imageView_id.length;i<len;i++){
            //加图片
            imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setBackgroundResource(imageView_id[i]);
            ImageviewList.add(imageView);
            //加白点
            pointView = new View(this);
            pointView.setBackgroundResource(R.drawable.point_select);
            layoutParams = new LinearLayout.LayoutParams(10,10);//设置小白点的大小
            if(i!=0)

                layoutParams.leftMargin = 10;
            pointView.setEnabled(false);
            point_container.addView(pointView,layoutParams);


        }
    }
    private void initAdapter(){
        point_container.getChildAt(0).setEnabled(true);
        //设置初始文字
        carouse_word.setText(dataList[0]);
        //设置初始的位置
        prePosition = 0;
        viewPager.setAdapter(new MyCarouseAdapter(ImageviewList));
//        int pos = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % imageViewList.size());
        viewPager.setCurrentItem(500000);//设置一个0-MAX_VALUE的中间值
    }
    private void initThread(){
        new Thread(){
            public void run() {
                isRunning = true;//每两秒销毁一次
                while (isRunning){//这里要把线程睡眠和更新UI线程都包括在循环内 不然只会更新一次UI
                    try {
                        Thread.sleep(4000);
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

    @Override//设置了后台
    public void onBackPressed() {
        moveTaskToBack(false);//转向后台

        Intent toHome = new Intent(Intent.ACTION_MAIN);//转向主屏幕
        toHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//如果设置，这个Activity会成为历史stack中一个新Task的开始。
        toHome.addCategory(Intent.CATEGORY_HOME);
        startActivity(toHome);
    }

    public void initFloatButton(){
        layout = findViewById(R.id.cloud);
        addQuestion = findViewById(R.id.main_addQuestion);
        addQuestion1 = findViewById(R.id.main_addQuestion1);
        addQuestion2 = findViewById(R.id.main_addQuestion2);
        addQuestion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMenu(addQuestion);
                Intent intent1 = new Intent(MainActivity.this, AskQuestionActivity.class);
                startActivity(intent1);
            }
        });
        addQuestion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMenu(addQuestion);
                Intent intent1 = new Intent(MainActivity.this, WebViewActivity.class);
                startActivity(intent1);
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fabOpened){
                    closeMenu(addQuestion);
                }else{

                }
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
            Log.d("test", "avatar1=" + user.getAvatar());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    userAvatar.setImageResource(R.mipmap.ic_launcher_round);
                }
            });
        } else {
            Log.d("test", "avatar2=" + user.getAvatar());
            NetUtils.getBitmap(user.getAvatar(), this, new NetUtils.getBitmapCallback() {
                @Override
                public void mBitmap(Bitmap mBitmap) {
                    userAvatar.setImageBitmap(mBitmap);
                }
            });
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
                        Intent intent4 = new Intent();
                        intent4.setClass(MainActivity.this,LoginActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("isBack",true);
                        intent4.putExtras(bundle);
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
                                imageList.add(object.getString("images"));
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
        exciting = new int[200];
        naive = new int[200];
        recentlist = new ArrayList<>();
        imageList = new ArrayList<>();
        answerCountlist = new int[200];
        authorAvatarlist = new ArrayList<>();
        authorNamelist = new ArrayList<>();
        is_exciting = new boolean[200];
        is_naive = new boolean[200];
        is_favorite = new boolean[200];
        questionId = new int[200];
    }

    public void initRecyclerView() {
        recyclerView = findViewById(R.id.main_rec);
        adapter = new mRecyclerViewAdapter(MainActivity.this,datelist,answerCountlist,authorNamelist,titlelist,contentlist,exciting,naive,
                recentlist,is_exciting,is_naive,is_favorite,authorAvatarlist,user.getToken(),imageList);
        recyclerView.setAdapter(adapter);
        adapter.setmHeaderView(header_view);
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
        layoutManager = new mLayoutManager(this);
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
                imageList.clear();
                authorAvatarlist.clear();
                for (int i = 0; i < titlelist.size(); i++) {
                    exciting[i] = 0;
                    naive[i] = 0;
                    answerCountlist[i] = 0;
                    is_exciting[i] = false;
                    is_naive[i] = false;
                    is_favorite[i] = false;
                    questionId[i] = 0;
                }
                recyclerView.removeAllViews();
                String url = "http://bihu.jay86.com/getQuestionList.php";
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
                                imageList.add(object.getString("images"));
                                authorAvatarlist.add(object.getString("authorAvatar"));
                                answerCountlist[i] = object.getInt("answerCount");
                                authorNamelist.add(object.getString("authorName"));
                                is_exciting[i] = object.getBoolean("is_exciting");
                                is_naive[i] = object.getBoolean("is_naive");
                                is_favorite[i] = object.getBoolean("is_favorite");
                                questionId[i] = object.getInt("id");
                            }
                            adapter.refreshData(datelist,answerCountlist,authorNamelist,titlelist,contentlist,exciting,naive,
                                    recentlist,is_exciting,is_naive,is_favorite,authorAvatarlist,questionId,imageList);
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


