package com.example.mac.bihu.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by mac on 2018/3/4.
 */

public class NetWorkUtil {
    public static boolean isNetAvailable(Context context){
        //获得网络管理器
        ConnectivityManager connM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connM.getActiveNetworkInfo();//得到网络详情
        if(netInfo == null || !netInfo.isAvailable())
            return false;
        return true;
    }
}
