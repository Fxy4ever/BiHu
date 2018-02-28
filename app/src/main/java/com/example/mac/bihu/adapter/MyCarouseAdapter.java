package com.example.mac.bihu.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by mac on 2018/2/28.
 */

public class MyCarouseAdapter extends PagerAdapter {
    private List imageViewList;

    public MyCarouseAdapter(List imageViewList) {
        this.imageViewList = imageViewList;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int newPosition = position % imageViewList.size();
        View view = (View) imageViewList.get(position%5);
        ViewGroup parent = (ViewGroup) view.getParent();
        if(parent!=null){
            parent.removeAllViews();
        }
        ImageView imageView = (ImageView) imageViewList.get(newPosition);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
