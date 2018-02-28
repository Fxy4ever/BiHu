package com.example.mac.bihu.Utils;

import android.content.Context;
import android.os.Environment;

/**
 * Created by mac on 2018/2/28.
 */

public class getPathUtil {
    public static String getCachePath(Context context){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                ||!Environment.isExternalStorageRemovable()){
            return context.getExternalCacheDir().getPath();
        }else{
            return context.getCacheDir().getPath();
        }

    }
}
