package com.example.mac.bihu.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.mac.bihu.R;

/**
 * Created by mac on 2018/2/7.
 */

public class MyDialog extends Dialog {
    private Window dialogwindow;
    private WindowManager.LayoutParams dialogParams;

    public MyDialog(Context context) {
        this(context, R.style.Theme_AppCompat_Dialog);
    }

    public MyDialog(Context context, int themeResId) {
        super(context, themeResId);
        initDialog();
    }

    private void initDialog(){
        dialogwindow = getWindow();
        dialogParams = dialogwindow.getAttributes();
    }
    public void setDialogHeightWidth(int height,int width){
        dialogParams.height = height;
        dialogParams.width = width;
        dialogwindow.setAttributes(dialogParams);
    }
    public void setAbsolutePosition(int x,int y){
        dialogwindow.setGravity(Gravity.LEFT|Gravity.TOP);
        dialogParams.x = x;
        dialogParams.y = y;
        dialogwindow.setAttributes(dialogParams);
    }
    public void setDialogLayout(View view){
        dialogwindow.setContentView(view);
    }
}
