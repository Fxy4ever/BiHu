package com.example.mac.bihu.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mac.bihu.R;
import com.example.mac.bihu.Utils.NetUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.example.mac.bihu.Activity.itemTouchActivity.qid;
import static com.example.mac.bihu.adapter.mRecyclerViewAdapter.token;

/**
 * Created by mac on 2018/2/4.
 */

public class MyInsideRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<String> datelist;
    private List<String> authorNamelist;
    private List<String> authorAvatarlist;
    private List<String> contentlist;
    private int[]         exciting;
    private int[]         best;
    private int[]         naive;
    private int[]        answerId;
    private boolean[] is_exciting;
    private boolean[] is_naive;


    public MyInsideRecyclerAdapter(List<String> datelist, List<String> authorNamelist,
                                   List<String> authorAvatarlist,List<String> contentlist,
                                   int[] exciting, int[] naive, boolean[] is_exciting
                                , boolean[] is_naive,int[] best,int[] answerId) {
        this.datelist = datelist;
        this.authorNamelist = authorNamelist;
        this.authorAvatarlist = authorAvatarlist;
        this.contentlist = contentlist;
        this.exciting = exciting;
        this.naive = naive;
        this.is_exciting = is_exciting;
        this.is_naive = is_naive;
        this.best = best;
        this.answerId=answerId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemtouch_item, parent, false);
            NormalViewHolder holder = new NormalViewHolder(view);
            return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof NormalViewHolder){

            /**
             * 绑定数据
             */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NetUtils.getBitmap(authorAvatarlist.get(position), new NetUtils.getBitmapCallback() {
                        @Override
                        public void mBitmap(Bitmap mBitmap) {
                            if(mBitmap==null){

                            }else{
                                ((NormalViewHolder) holder).avatar.setImageBitmap(mBitmap);
                            }
                        }
                    });
                }
            }).start();
            ((NormalViewHolder) holder).content.setText(contentlist.get(position));
            ((NormalViewHolder) holder).date.setText(datelist.get(position));
            ((NormalViewHolder) holder).exciting.setText(String.valueOf(exciting[position]));
            ((NormalViewHolder) holder).naive.setText(String.valueOf(naive[position]));
            ((NormalViewHolder) holder).authorName.setText(authorNamelist.get(position));

            if(is_exciting[position]){
                ((NormalViewHolder) holder).good.setBackgroundResource(R.drawable.good_blue);
            }
            if(is_naive[position]){
                ((NormalViewHolder) holder).bad.setBackgroundResource(R.drawable.bad_blue);
            }
            if(best[position]==1){
                ((NormalViewHolder) holder).accept.setBackgroundResource(R.drawable.like_red);
            }
            ((NormalViewHolder) holder).good.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!is_exciting[position]){
                        is_exciting[position]=true;
                        view.setBackgroundResource(R.drawable.good_blue);
                        exciting[position]+=1;
                        ((NormalViewHolder) holder).exciting.setText(String.valueOf((exciting[position])));
                        is_exciting[position]= true;
                        String url = "http://bihu.jay86.com/exciting.php";
                        StringBuilder ask = new StringBuilder();
                        ask.append("id="+answerId[position]+"&type=2"+"&token="+token);
                        NetUtils.post(url, ask.toString(), new NetUtils.Callback() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int status = jsonObject.getInt("status");
                                    if(status==200){
                                        Log.d("点赞", "good: succeed!");
                                    }else{
                                        Log.d("点赞", "good: failed!");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else{
                        is_exciting[position] = false;
                        view.setBackgroundResource(R.drawable.good);
                        exciting[position]-=1;
                        ((NormalViewHolder) holder).exciting.setText(String.valueOf((exciting[position])));
                        String url = "http://bihu.jay86.com/cancelExciting.php";
                        StringBuilder ask = new StringBuilder();
                        ask.append("id="+answerId[position]+"&type=2"+"&token="+token);
                        NetUtils.post(url, ask.toString(), new NetUtils.Callback() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int status = jsonObject.getInt("status");
                                    if(status==200){
                                        Log.d("点赞", "cancel_good: succeed!");
                                    }else{
                                        Log.d("点赞", "cancel_good: failed!");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });
            ((NormalViewHolder) holder).bad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!is_naive[position]){
                        is_naive[position]=true;
                        view.setBackgroundResource(R.drawable.bad_blue);
                        ((NormalViewHolder) holder).naive.setText(String.valueOf((naive[position]+1)));
                        is_naive[position]=true;
                        String url = "http://bihu.jay86.com/naive.php";
                        StringBuilder ask = new StringBuilder();
                        ask.append("id="+answerId[position]+"&type=2"+"&token="+token);
                        NetUtils.post(url, ask.toString(), new NetUtils.Callback() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int status = jsonObject.getInt("status");
                                    if(status == 200){
                                        if(status==200){
                                            Log.d("点赞", "bad: succeed!");
                                        }else{
                                            Log.d("点赞", "bad: failed!");
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else{
                        is_naive[position]=false;
                        view.setBackgroundResource(R.drawable.bad);
                        ((NormalViewHolder) holder).naive.setText(String.valueOf((naive[position]-1)));
                        String url = "http://bihu.jay86.com/cancelNaive.php";
                        StringBuilder ask = new StringBuilder();
                        ask.append("id="+answerId[position]+"&type=2"+"&token="+token);
                        NetUtils.post(url, ask.toString(), new NetUtils.Callback() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int status = jsonObject.getInt("status");
                                    if(status==200){
                                        Log.d("点赞", "cancel_bad: succeed!");
                                    }else{
                                        Log.d("点赞", "cancel_bad: failed!");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                }
            });
            ((NormalViewHolder) holder).accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setBackgroundResource(R.drawable.like_red);
                    String url = "http://bihu.jay86.com/accept.php";
                    StringBuilder ask = new StringBuilder();
                    ask.append("qid="+qid+"&aid="+answerId[position]+"&token="+token);
                    NetUtils.post(url, ask.toString(), new NetUtils.Callback() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int status = jsonObject.getInt("status");
                                if(status==200){
                                    Log.d("fxy", "accept: succeed!");
                                }else{
                                    Log.d("fxy", "accept: failed!");
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



    @Override
    public int getItemCount() {
        return authorNamelist.size();
    }


    public class NormalViewHolder extends RecyclerView.ViewHolder{
        TextView authorName;
        TextView date;
        TextView content;
        TextView exciting ;
        TextView naive;


        ImageView avatar;
        //        ImageView images;
        ImageButton good;
        ImageButton bad;
        ImageButton accept;

        public NormalViewHolder(View itemView) {
            super(itemView);
            authorName = itemView.findViewById(R.id.ins_recy_username);
            date = itemView.findViewById(R.id.ins_recy_time);
            content = itemView.findViewById(R.id.ins_recy_content);
            exciting = itemView.findViewById(R.id.ins_recy_good_number);
            naive = itemView.findViewById(R.id.ins_recy_bad_number);
            avatar = itemView.findViewById(R.id.ins_recy_user_avatar);

            good = itemView.findViewById(R.id.ins_recy_good);
            bad = itemView.findViewById(R.id.ins_recy_bad);
            accept = itemView.findViewById(R.id.ins_recy_accept);
        }
    }

}
