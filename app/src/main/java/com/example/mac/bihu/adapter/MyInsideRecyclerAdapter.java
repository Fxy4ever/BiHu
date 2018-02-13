package com.example.mac.bihu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mac.bihu.R;

import java.util.List;

/**
 * Created by mac on 2018/2/7.
 */

public class MyInsideRecyclerAdapter extends RecyclerView.Adapter<MyInsideRecyclerAdapter.ViewHolder> {
    private List<String> namelist;
    private List<String> contentlist;
    private List avatarList;

    public MyInsideRecyclerAdapter(List<String> namelist, List<String> contentlist, List avatarList) {
        this.namelist = namelist;
        this.contentlist = contentlist;
        this.avatarList = avatarList;
    }

    @Override
    public MyInsideRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemtouch_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
        /**
         * item点击接口
         */
        public interface OnItemClickListener{
            void OnClickItem(View view, int position);
        }
        private MyInsideRecyclerAdapter.OnItemClickListener MyonItemClickListener;
        public void onItemClickListner(OnItemClickListener onItemClickListener){
            this.MyonItemClickListener = onItemClickListener;
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
    public void onBindViewHolder(final MyInsideRecyclerAdapter.ViewHolder holder,final int position) {
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
            holder.username.setText(namelist.get(position));
//        holder.commitTime.setText();
            holder.content.setText(contentlist.get(position));
            holder.avatar.setImageResource((int) avatarList.get(position));
            //good绑定
            holder.good.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyonItemGoodListener.onGoodClick(position);
                }
            });
            //bad绑定
            holder.bad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyonItemBadListener.onBadClick(position);
                }
            });
            //like绑定
            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyonItemLikeListener.onLikeClick(position);
                }
            });


    }


            @Override
            public int getItemCount () {
                return namelist.size();
            }


       public class ViewHolder extends RecyclerView.ViewHolder {
                TextView username;
                //        TextView commitTime;
                TextView content;
                ImageView avatar;
                Button good;
                Button bad;
                Button like;

                //        TextView freshTime;
//        TextView comments_number;
//        TextView good_number ;
//        TextView bad_number;
                public ViewHolder(View itemView) {
                    super(itemView);
                    //Button
                    good = itemView.findViewById(R.id.ins_recy_good);
                    bad = itemView.findViewById(R.id.ins_recy_bad);
                    like = itemView.findViewById(R.id.ins_recy_like);
                    //TextView
                    username = itemView.findViewById(R.id.ins_recy_username);
//          commitTime = itemView.findViewById(R.id.ins_recy_time);
                    content = itemView.findViewById(R.id.ins_recy_content);
//          freshTime = itemView.findViewById(R.id.inside_fresh_time);
                    avatar = itemView.findViewById(R.id.ins_recy_user_avatar);
                }
            }
        }
