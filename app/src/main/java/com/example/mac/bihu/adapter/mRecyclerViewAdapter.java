package com.example.mac.bihu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mac.bihu.R;
import com.example.mac.bihu.Utils.NetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.mac.bihu.Activity.MainActivity.questionId;

/**
 * Created by mac on 2018/2/4.
 */

public class mRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private Context context;

    private View mHeaderView;

    private List<String> datelist;
    private int[]        answerCountlist;
    private List<String> authorNamelist;
    private List<String> authorAvatarlist;
    private List<String> titlelist;
    private List<String> contentlist;
    private List<String> imageList;
    private int[]         exciting;
    private int[]         naive;
    private List<String> recentlist;
    private boolean[] is_exciting;
    private boolean[] is_naive;
    private boolean[] is_favorite;
    public static String token;

    private int FOOTER = 3;
    private int HEADER = 1;
    private int NORMAL = 0;

    int page = 1;





    public mRecyclerViewAdapter(Context context,List<String> datelist, int[] answerCountlist, List<String> authorNamelist,
                                List<String> titlelist, List<String> contentlist, int[] exciting, int[] naive,
                                List<String> recentlist, boolean[] is_exciting, boolean[] is_naive, boolean[] is_favorite
    ,List<String> authorAvatarlist ,String token,List<String> imageList) {
        this.context = context;
        this.datelist = datelist;
        this.answerCountlist = answerCountlist;
        this.authorNamelist = authorNamelist;
        this.titlelist = titlelist;
        this.contentlist = contentlist;
        this.exciting = exciting;
        this.naive = naive;
        this.recentlist = recentlist;
        this.is_exciting = is_exciting;
        this.is_naive = is_naive;
        this.is_favorite = is_favorite;
        this.authorAvatarlist = authorAvatarlist;
        this.imageList = imageList;
        this.token = token;
    }

    public void setmHeaderView(View mHeaderView) {
        this.mHeaderView = mHeaderView;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount()-1)
            return FOOTER;
        else if (position==0)
            return HEADER;
        else
            return NORMAL;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == FOOTER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerveiw_foot_item,parent,false);
            FooterViewHolder holder = new FooterViewHolder(view);
            return holder;
        }else if(viewType == HEADER){
            return new HeaderViewHolder(mHeaderView);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder,int position) {
       if(holder instanceof NormalViewHolder){
            final int Normal_position = position-1;
           /**
            * 绑定数据
            */
           Log.d(TAG, authorAvatarlist.get(Normal_position));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ((NormalViewHolder) holder).avatar.setTag(authorAvatarlist.get(Normal_position));
                    if(!authorAvatarlist.get(Normal_position).equals("http://p4wk0ha8y.bkt.clouddn.com/1520408915146")){
                        NetUtils.getBitmap(authorAvatarlist.get(Normal_position), context,new NetUtils.getBitmapCallback() {
                            @Override
                            public void mBitmap(final Bitmap mBitmap) {
                                if(mBitmap==null){
                                    ((NormalViewHolder) holder).avatar.setImageResource(R.mipmap.ic_launcher_round);
                                }else{
                                    ((NormalViewHolder) holder).avatar.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(authorAvatarlist.get(Normal_position)==((NormalViewHolder) holder).avatar.getTag()){
                                                ((NormalViewHolder) holder).avatar.setImageBitmap(mBitmap);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }else{
                        ((NormalViewHolder) holder).avatar.setImageResource(R.mipmap.ic_launcher_round);
                    }
                    Log.d(TAG, "run: "+imageList.get(Normal_position));
                    ((NormalViewHolder) holder).images.setTag(imageList.get(Normal_position));
                    NetUtils.getBitmap(imageList.get(Normal_position),context,new NetUtils.getBitmapCallback() {
                        @Override
                        public void mBitmap(final Bitmap mBitmap) {
                            if(imageList.get(Normal_position).equals("null")){
                                ((NormalViewHolder) holder).images.setImageResource(R.drawable.zhanwei);
                                ViewGroup.LayoutParams params = ((NormalViewHolder) holder).images.getLayoutParams();
                                params.height=1;
                                params.width=1;
                                ((NormalViewHolder) holder).images.setLayoutParams(params);
                            }else{
                                ((NormalViewHolder) holder).images.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(imageList.get(Normal_position)==((NormalViewHolder) holder).images.getTag()){
                                            ((NormalViewHolder) holder).images.setImageBitmap(mBitmap);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }).start();

            ((NormalViewHolder) holder).title.setText(titlelist.get(Normal_position));
            ((NormalViewHolder) holder).content.setText(contentlist.get(Normal_position));
            ((NormalViewHolder) holder).date.setText(datelist.get(Normal_position)+"发布");
            ((NormalViewHolder) holder).exciting.setText(String.valueOf(exciting[Normal_position]));
            ((NormalViewHolder) holder).naive.setText(String.valueOf(naive[Normal_position]));
            ((NormalViewHolder) holder).recent.setText(recentlist.get(Normal_position)+"更新");
            ((NormalViewHolder) holder).answerCount.setText(String.valueOf(answerCountlist[Normal_position]));
            ((NormalViewHolder) holder).authorName.setText(authorNamelist.get(Normal_position));
            if(is_exciting[Normal_position]){
                ((NormalViewHolder) holder).good.setBackgroundResource(R.drawable.good_blue);
            }
            if(is_naive[Normal_position]){
                ((NormalViewHolder) holder).bad.setBackgroundResource(R.drawable.bad_blue);
            }
            if(is_favorite[Normal_position]){
                ((NormalViewHolder) holder).like.setBackgroundResource(R.drawable.like_blue);
            }

           ((NormalViewHolder) holder).good.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if(!is_exciting[Normal_position]){
                       view.setBackgroundResource(R.drawable.good_blue);
                       exciting[Normal_position]+=1;
                       ((NormalViewHolder) holder).exciting.setText(String.valueOf((exciting[Normal_position])));
                       is_exciting[Normal_position]=true;
                       String url = "http://bihu.jay86.com/exciting.php";
                       StringBuilder ask = new StringBuilder();
                       ask.append("id="+questionId[Normal_position]+"&type=1"+"&token="+token);
                       NetUtils.post(url, ask.toString(), new NetUtils.Callback() {
                           @Override
                           public void onResponse(String response) {
                               try {
                                   JSONObject jsonObject = new JSONObject(response);
                                   int status = jsonObject.getInt("status");
                                   if(status == 200){
                                       if(status==200){
                                           Log.d("点赞", "good: succeed!");
                                       }else{
                                           Log.d("点赞", "good: failed!");
                                       }
                                   }
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                           }
                       });
                   }else{
                       is_exciting[Normal_position]=false;
                       view.setBackgroundResource(R.drawable.good);
                       exciting[Normal_position]-=1;
                       ((NormalViewHolder) holder).exciting.setText(String.valueOf((exciting[Normal_position])));
                       String url = "http://bihu.jay86.com/cancelExciting.php";
                       StringBuilder ask = new StringBuilder();
                       ask.append("id="+questionId[Normal_position]+"&type=1"+"&token="+token);
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
                    if(!is_naive[Normal_position]){
                        view.setBackgroundResource(R.drawable.bad_blue);
                        naive[Normal_position]+=1;
                        ((NormalViewHolder) holder).naive.setText(String.valueOf((naive[Normal_position])));
                        is_naive[Normal_position]=true;
                        String url = "http://bihu.jay86.com/naive.php";
                        StringBuilder ask = new StringBuilder();
                        ask.append("id="+questionId[Normal_position]+"&type=1"+"&token="+token);
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
                        is_naive[Normal_position]=false;
                        view.setBackgroundResource(R.drawable.bad);
                        naive[Normal_position]-=1;
                        ((NormalViewHolder) holder).naive.setText(String.valueOf((naive[Normal_position])));
                        String url = "http://bihu.jay86.com/cancelNaive.php";
                        StringBuilder ask = new StringBuilder();
                        ask.append("id="+questionId[Normal_position]+"&type=1"+"&token="+token);
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
            ((NormalViewHolder) holder).like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!is_favorite[Normal_position]){
                        is_favorite[Normal_position]=true;
                        view.setBackgroundResource(R.drawable.like_blue);
                        String url = "http://bihu.jay86.com/favorite.php";
                        StringBuilder ask = new StringBuilder();
                        ask.append("qid="+questionId[Normal_position]+"&token="+token);
                        NetUtils.post(url, ask.toString(), new NetUtils.Callback() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int status = jsonObject.getInt("status");
                                    if(status==200){
                                        Log.d("点赞", "like: succeed!");
                                    }else{
                                        Log.d("点赞", "like: failed!");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }else{
                        is_favorite[Normal_position]=false;
                        view.setBackgroundResource(R.drawable.like);
                        String url = "http://bihu.jay86.com/cancelFavorite.php";
                        StringBuilder ask = new StringBuilder();
                        ask.append("qid="+questionId[Normal_position]+"&token="+token);
                        NetUtils.post(url, ask.toString(), new NetUtils.Callback() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int status = jsonObject.getInt("status");
                                    if(status==200){
                                        Log.d("点赞", "cancel_like: succeed!");
                                    }else{
                                        Log.d("点赞", "cancel_like: failed!");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });

           ((NormalViewHolder) holder).comments.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   MyonItemCommentsListener.onCommentsClick(Normal_position);
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
                      int position = holder.getLayoutPosition()-1;
                      MyonItemClickListener.OnClickItem(view,position);
                  }
              });
          }
      }else if(holder instanceof HeaderViewHolder){

       }else{

       }
        if((getItemViewType(position)==FOOTER)){
            loadmore();
        }
    }


    public void loadmore(){
        Log.d(TAG, "loadmore: ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://bihu.jay86.com/getQuestionList.php";
                StringBuilder getItem = new StringBuilder();
                getItem.append("page="+ page +"&count=20"+"&token=" + token);
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
                                recentlist.add(object.getString("recent"));
                                imageList.add(object.getString("images"));
                                authorAvatarlist.add(object.getString("authorAvatar"));
                                answerCountlist[i+20*page] = object.getInt("answerCount");
                                authorNamelist.add(object.getString("authorName"));
                                is_exciting[i+20*page] = object.getBoolean("is_exciting");
                                is_naive[i+20*page] = object.getBoolean("is_naive");
                                is_favorite[i+20*page] = object.getBoolean("is_favorite");
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
        return authorNamelist.size()+2;
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
        ImageButton good;
        ImageButton bad;
        ImageButton like;

        public NormalViewHolder(View itemView) {
            super(itemView);
            authorName = itemView.findViewById(R.id.main_user_name);
            date = itemView.findViewById(R.id.main_time);
            title = itemView.findViewById(R.id.main_title);
            content = itemView.findViewById(R.id.main_content);
            recent = itemView.findViewById(R.id.main_fresh_time);
            answerCount = itemView.findViewById(R.id.main_comments_number);
            exciting = itemView.findViewById(R.id.main_good_number);
            naive = itemView.findViewById(R.id.main_bad_number);
            avatar = itemView.findViewById(R.id.main_user_avatar);
            images = itemView.findViewById(R.id.main_showPicture);
            comments = itemView.findViewById(R.id.main_comments);
            good = itemView.findViewById(R.id.main_good);
            bad = itemView.findViewById(R.id.main_bad);
            like = itemView.findViewById(R.id.main_like);
        }
    }
    public class FooterViewHolder extends RecyclerView.ViewHolder{
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void refreshData(List<String> datelist1, int[] answerCountlist1, List<String> authorNamelist1,
                            List<String> titlelist1, List<String> contentlist1, int[] exciting1, int[] naive1,
                            List<String> recentlist1, boolean[] is_exciting1, boolean[] is_naive1, boolean[] is_favorite1
            , final List<String> authorAvatarlist1, int[] questionId1,List<String> imagelist1) {
        Log.d("fxy", "Refresh");
            titlelist = titlelist1;
            contentlist = contentlist1;
            datelist = datelist1;
            recentlist = recentlist1;
            authorNamelist = authorNamelist1;
            authorAvatarlist = authorAvatarlist1;
            exciting= exciting1;
            naive = naive1;
            answerCountlist= answerCountlist1;
            is_exciting = is_exciting1;
            is_naive = is_naive1;
            is_favorite = is_favorite1;
            questionId = questionId1;
            imageList = imagelist1;
    }

}
