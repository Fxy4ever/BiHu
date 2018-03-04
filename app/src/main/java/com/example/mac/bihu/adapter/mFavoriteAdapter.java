package com.example.mac.bihu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mac.bihu.R;
import com.example.mac.bihu.Utils.NetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.example.mac.bihu.Activity.MainActivity.questionId;

/**
 * Created by mac on 2018/2/4.
 */

public class mFavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    Context context;

    private List<String> imageslist;
    private List<String> datelist;
    private int[]        answerCountlist;
    private List<String> authorNamelist;
    private List<String> authorAvatarlist;
    private List<String> titlelist;
    private List<String> contentlist;
    private int[]         exciting;
    private int[]         naive;
    private List<String> recentlist;
    private boolean[] is_exciting;
    private boolean[] is_naive;
    private String token;

    private int FOOTER = 1;
    private int NORMAL = 2;

    private int page = 1;

    public mFavoriteAdapter(List<String> imageslist, List<String> datelist, List<String> recentlist,
                                int[] answerCountlist, List<String> authorNamelist, List<String> authorAvatarlist,
                                List<String> titlelist, List<String> contentlist, int[] exciting, int[] naive, List<String> recentlist1,
                                boolean[] is_exciting, boolean[] is_naive,String token,Context context) {
        this.imageslist = imageslist;
        this.datelist = datelist;
        this.recentlist = recentlist;
        this.answerCountlist = answerCountlist;
        this.authorNamelist = authorNamelist;
        this.authorAvatarlist = authorAvatarlist;
        this.titlelist = titlelist;
        this.contentlist = contentlist;
        this.exciting = exciting;
        this.naive = naive;
        this.recentlist = recentlist1;
        this.is_exciting = is_exciting;
        this.is_naive = is_naive;
        this.token = token;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount()-1){
            return FOOTER;
        }
        else{
            return NORMAL;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == FOOTER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerveiw_foot_item,parent,false);
            FooterViewHolder holder = new FooterViewHolder(view);
            return holder;
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
            NormalViewHolder holder = new NormalViewHolder(view);
            return holder;
        }
    }

    /**
     * 实现这个item点击事件接口
     */
    public interface OnItemClickListener {
        void OnClickItem(View view, int position);
    }
    private OnItemClickListener MyonItemClickListener;
    public void onItemClickListner(OnItemClickListener onItemClickListener){
        MyonItemClickListener = onItemClickListener;
    }

    /**
     * comments点击接口
     */
    public interface onItemCommentsListener {
        void onCommentsClick(int i);
    }
    private onItemCommentsListener MyonItemCommentsListener;
    public void setOnItemCommentsClickListener(onItemCommentsListener mOnItemCommentsListener) {
        this.MyonItemCommentsListener = mOnItemCommentsListener;
    }



    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Log.d("test", "position="+position);
        if(holder instanceof NormalViewHolder){
            /**
             * 绑定数据
             */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NetUtils.getBitmap(authorAvatarlist.get(position),context,new NetUtils.getBitmapCallback() {
                        @Override
                        public void mBitmap(final Bitmap mBitmap) {
                            if(mBitmap==null){
                                ((NormalViewHolder) holder).avatar.setImageResource(R.mipmap.ic_launcher_round);
                            }else{
                                ((NormalViewHolder) holder).avatar.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((NormalViewHolder) holder).avatar.setImageBitmap(mBitmap);
                                    }
                                });
                            }
                        }
                    });
                    NetUtils.getBitmap(imageslist.get(position),context, new NetUtils.getBitmapCallback() {
                        @Override
                        public void mBitmap(final Bitmap mBitmap) {
                            if(mBitmap==null){
                                ((NormalViewHolder) holder).images.setImageResource(R.drawable.zhanwei);
                                ViewGroup.LayoutParams params = ((NormalViewHolder) holder).images.getLayoutParams();
                                params.height=10;
                                params.width=10;
                                ((NormalViewHolder) holder).images.setLayoutParams(params);
                            }else{
                               ((NormalViewHolder) holder).images.post(new Runnable() {
                                   @Override
                                   public void run() {
                                       ((NormalViewHolder) holder).images.setImageBitmap(mBitmap);
                                   }
                               });
                            }
                        }
                    });
                }
            }).start();

            ((NormalViewHolder) holder).title.setText(titlelist.get(position));
            ((NormalViewHolder) holder).content.setText(contentlist.get(position));
            ((NormalViewHolder) holder).date.setText(datelist.get(position)+"发布");
            ((NormalViewHolder) holder).exciting.setText(String.valueOf(exciting[position]));
            ((NormalViewHolder) holder).naive.setText(String.valueOf(naive[position]));
            ((NormalViewHolder) holder).recent.setText(recentlist.get(position)+"更新");
            ((NormalViewHolder) holder).answerCount.setText(String.valueOf(answerCountlist[position]));
            ((NormalViewHolder) holder).authorName.setText(authorNamelist.get(position));
            if(is_exciting[position]){
                ((NormalViewHolder) holder).good.setBackgroundResource(R.drawable.good_blue);
            }
            if(is_naive[position]){
                ((NormalViewHolder) holder).bad.setBackgroundResource(R.drawable.bad_blue);
            }
            ((NormalViewHolder) holder).good.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!is_exciting[position]){
                        is_exciting[position]=true;
                        view.setBackgroundResource(R.drawable.good_blue);
                        exciting[position]+=1;
                        ((NormalViewHolder) holder).exciting.setText(String.valueOf(exciting[position]));
                        is_exciting[position]= true;
                        String url = "http://bihu.jay86.com/exciting.php";
                        StringBuilder ask = new StringBuilder();
                        ask.append("id="+questionId[position]+"&type=1"+"&token="+token);
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
                        ask.append("id="+questionId[position]+"&type=1"+"&token="+token);
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
                        naive[position]+=1;
                        ((NormalViewHolder) holder).naive.setText(String.valueOf((naive[position])));
                        is_naive[position]=true;
                        String url = "http://bihu.jay86.com/naive.php";
                        StringBuilder ask = new StringBuilder();
                        ask.append("id="+questionId[position]+"&type=1"+"&token="+token);
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
                        naive[position]-=1;
                        ((NormalViewHolder) holder).naive.setText(String.valueOf((naive[position])));
                        String url = "http://bihu.jay86.com/cancelNaive.php";
                        StringBuilder ask = new StringBuilder();
                        ask.append("id="+questionId[position]+"&type=1"+"&token="+token);
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


            //comments绑定
            ((NormalViewHolder) holder).comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyonItemCommentsListener.onCommentsClick(position);
                }
            });



            /**
             * 实现item点击绑定
             */
            View view = ((LinearLayout)holder.itemView).getChildAt(0);
            if(MyonItemClickListener != null){
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holder.getLayoutPosition();
                        MyonItemClickListener.OnClickItem(view,position);
                    }
                });
            }
        }
        if(getItemViewType(position)==FOOTER){
            loadmore();
        }
    }
    public void loadmore(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://bihu.jay86.com/getFavoriteList.php";
                StringBuilder getItem = new StringBuilder();
                getItem.append("page="+page +"&count=20"+ "&token=" + token);
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
                                exciting[i+20*page] = object.getInt("exciting");
                                naive[i+20*page] = object.getInt("naive");
                                authorAvatarlist.add(object.getString("authorAvatar"));
                                recentlist.add(object.getString("recent"));
                                imageslist.add(object.getString("images"));
                                answerCountlist[i+20*page] = object.getInt("answerCount");
                                authorNamelist.add(object.getString("authorName"));
                                is_exciting[i+20*page] = object.getBoolean("is_exciting");
                                is_naive[i+20*page] = object.getBoolean("is_naive");
                                questionId[i+20*page] = object.getInt("id");
                            }
                            notifyDataSetChanged();
                            page++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return authorNamelist.size()+1;
    }


    public class NormalViewHolder extends RecyclerView.ViewHolder{
        TextView authorName;
        TextView date;
        TextView title;
        TextView content;
        TextView recent;
        TextView answerCount;
        TextView exciting ;
        TextView naive;


        ImageView avatar;
        ImageView images;
        Button comments;
        Button good;
        Button bad;
        Button like;

        public NormalViewHolder(View itemView) {
            super(itemView);
            authorName = itemView.findViewById(R.id.favorite_user_name);
            date = itemView.findViewById(R.id.favorite_time);
            title = itemView.findViewById(R.id.favorite_title);
            content = itemView.findViewById(R.id.favorite_content);
            recent = itemView.findViewById(R.id.favorite_fresh_time);
            answerCount = itemView.findViewById(R.id.favorite_comments_number);
            exciting = itemView.findViewById(R.id.favorite_good_number);
            naive = itemView.findViewById(R.id.favorite_bad_number);
            avatar = itemView.findViewById(R.id.favorite_user_avatar);
            images = itemView.findViewById(R.id.favorite_showPicture);
            comments = itemView.findViewById(R.id.favorite_comments);
            good = itemView.findViewById(R.id.favorite_good);
            bad = itemView.findViewById(R.id.favorite_bad);
            like = itemView.findViewById(R.id.favorite_like);
        }
    }
    public class FooterViewHolder extends RecyclerView.ViewHolder{
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }




}
