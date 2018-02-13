package com.example.mac.bihu.Listener;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Created by mac on 2018/2/7.
 */

public abstract class MyOnScrollListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager layoutManager;

    private int curPage=0;
    private int TotalItem;
    private int preTotalItem=0;
    private int visibalItem;
    private int firstVisibalItem;
    private boolean loading = true;

    public MyOnScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        visibalItem = recyclerView.getChildCount();
        TotalItem = layoutManager.getItemCount();
        firstVisibalItem = layoutManager.findFirstVisibleItemPosition();
        if(loading){
            if(TotalItem > preTotalItem){
                loading = false;
                preTotalItem = TotalItem;
                Log.d("fxy", "刷新前"+String.valueOf(preTotalItem));
                Log.d("fxy", "刷新前"+String.valueOf(TotalItem));
            }
            if(TotalItem < preTotalItem){//刷新之后pretotal不变 需要修正
                Log.d("fxy", "刷新后"+String.valueOf(preTotalItem));
                Log.d("fxy", "刷新后"+String.valueOf(TotalItem));
                preTotalItem = TotalItem;
                loading = true;
            }
        }

        if (!loading&&TotalItem - visibalItem <= firstVisibalItem){//这里以后看看
            curPage++;
            onLoad(curPage);
            loading = true;
        }
    }
    public abstract void onLoad(int curPage);
}
