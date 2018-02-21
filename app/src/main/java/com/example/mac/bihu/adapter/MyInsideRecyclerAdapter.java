package com.example.mac.bihu.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
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

import java.util.List;

/**
 * Created by mac on 2018/2/4.
 */

public class MyInsideRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<String> datelist;
    private List<String> authorNamelist;
    private List<Bitmap> authorAvatarlist;
    private List<String> contentlist;
    private int[]         exciting;
    private int[]         naive;
    private boolean[] is_exciting;
    private boolean[] is_naive;


    public MyInsideRecyclerAdapter(List<String> datelist, List<String> authorNamelist,List<String> contentlist, int[] exciting, int[] naive, boolean[] is_exciting, boolean[] is_naive) {
        this.datelist = datelist;
        this.authorNamelist = authorNamelist;
        this.contentlist = contentlist;
        this.exciting = exciting;
        this.naive = naive;
        this.is_exciting = is_exciting;
        this.is_naive = is_naive;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemtouch_item, parent, false);
            NormalViewHolder holder = new NormalViewHolder(view);
            return holder;
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
     * 采纳点击接口
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
        if(holder instanceof NormalViewHolder){

            /**
             * 绑定数据
             */
            ((NormalViewHolder) holder).content.setText(contentlist.get(position));
            ((NormalViewHolder) holder).date.setText(datelist.get(position));
            ((NormalViewHolder) holder).exciting.setText(String.valueOf(exciting[position]));
            ((NormalViewHolder) holder).naive.setText(String.valueOf(naive[position]));
            ((NormalViewHolder) holder).authorName.setText(authorNamelist.get(position));
            ((NormalViewHolder) holder).avatar.setImageBitmap(authorAvatarlist.get(position));


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
        Button good;
        Button bad;
        Button like;

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
            like = itemView.findViewById(R.id.favorite_like);
        }
    }

}
