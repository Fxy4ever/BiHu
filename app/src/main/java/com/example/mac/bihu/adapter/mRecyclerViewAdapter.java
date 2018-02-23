package com.example.mac.bihu.adapter;

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

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by mac on 2018/2/4.
 */

public class mRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<Bitmap> imageslist;
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
    private boolean[] is_favorite;

    private int FOOTER = 3;
    private int HEADER = 1;
    private int NORMAL = 0;
    private int END = 2;


    public mRecyclerViewAdapter(List<String> datelist, int[] answerCountlist, List<String> authorNamelist,
                                List<String> titlelist, List<String> contentlist, int[] exciting, int[] naive,
                                List<String> recentlist, boolean[] is_exciting, boolean[] is_naive, boolean[] is_favorite
    ,List<String> authorAvatarlist ) {
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
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "list.size= "+titlelist.size());
        if(position == authorNamelist.size())
            return FOOTER;
        else if (position==0)
            return HEADER;
        else
            return NORMAL;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "viewtype="+viewType);
        if(viewType == END) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerveiw_end_item,parent,false);
            EndViewHolder holder = new EndViewHolder(view);
            return holder;
        }else if(viewType == FOOTER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerveiw_foot_item,parent,false);
            FooterViewHolder holder = new FooterViewHolder(view);
            return holder;
        }else if(viewType == HEADER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_header_item,parent,false);
            HeaderViewHolder holder = new HeaderViewHolder(view);
            return holder;
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
    /**
     * good点击接口
     */
    public interface onItemGoodListener {
        void onGoodClick(int i);
    }
    private onItemGoodListener MyonItemGoodListener;
    public void setOnItemGoodClickListener(onItemGoodListener mOnItemGoodListener) {
        this.MyonItemGoodListener = mOnItemGoodListener;
    }

    /**
     * bad点击接口
     */
    public interface onItemBadListener {
        void onBadClick(int i);
    }
    private onItemBadListener MyonItemBadListener;
    public void setOnItemBadClickListener(onItemBadListener mOnItemBadListener) {
        this.MyonItemBadListener = mOnItemBadListener;
    }

    /**
     * like点击接口
     */
    public interface onItemLikeListener {
        void onLikeClick(int i);
    }
    private onItemLikeListener MyonItemLikeListener;
    public void setOnItemLikeClickListener(onItemLikeListener mOnItemLikeListener) {
        this.MyonItemLikeListener = mOnItemLikeListener;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Log.d(TAG, "presentPosition="+position);
       if(holder instanceof NormalViewHolder){
          /**
           * 绑定数据
           */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "onBindeViewHolder:" +position+ authorAvatarlist );
                    NetUtils.getBitmap(authorAvatarlist.get(position), new NetUtils.getBitmapCallback() {
                        @Override
                        public void mBitmap(Bitmap mBitmap) {
                            if(authorAvatarlist.get(position)==null){
                                System.out.println("123");
                            }else{
                                ((NormalViewHolder) holder).avatar.setImageBitmap(mBitmap);
                            }
                        }
                    });
                }
            }).start();
            ((NormalViewHolder) holder).title.setText(titlelist.get(position));
            ((NormalViewHolder) holder).content.setText(contentlist.get(position));
            ((NormalViewHolder) holder).date.setText(datelist.get(position));
            ((NormalViewHolder) holder).exciting.setText(String.valueOf(exciting[position]));
            ((NormalViewHolder) holder).naive.setText(String.valueOf(naive[position]));
            ((NormalViewHolder) holder).recent.setText(recentlist.get(position));
            ((NormalViewHolder) holder).answerCount.setText(String.valueOf(answerCountlist[position]));
            ((NormalViewHolder) holder).authorName.setText(authorNamelist.get(position));
            if(is_exciting[position]){
                ((NormalViewHolder) holder).good.setBackgroundResource(R.drawable.good_blue);
            }
            if(is_naive[position]){
                ((NormalViewHolder) holder).bad.setBackgroundResource(R.drawable.bad_blue);
            }
            if(is_favorite[position]){
                ((NormalViewHolder) holder).like.setBackgroundResource(R.drawable.like_blue);
            }




          //comments绑定
          ((NormalViewHolder) holder).comments.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                    MyonItemCommentsListener.onCommentsClick(position);
              }
          });
          //good绑定
            ((NormalViewHolder) holder).good.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyonItemGoodListener.onGoodClick(position);
                }
            });
            //bad绑定
            ((NormalViewHolder) holder).bad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyonItemBadListener.onBadClick(position);
                }
            });
            //like绑定
            ((NormalViewHolder) holder).like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyonItemLikeListener.onLikeClick(position);
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
//        ImageView images;
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
    public class EndViewHolder extends RecyclerView.ViewHolder{

        public EndViewHolder(View itemView) {
            super(itemView);
        }
    }



}
