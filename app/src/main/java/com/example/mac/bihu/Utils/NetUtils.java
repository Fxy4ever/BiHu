package com.example.mac.bihu.Utils;

import android.accounts.NetworkErrorException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by HP on 2017/12/28.
 * 这是一个利用HttpURLConnection实现的工具类
 */

public class NetUtils{


    public interface  Callback{
        void onResponse(String response);
    }
    public interface  getBitmapCallback{
        void mBitmap(Bitmap mBitmap);
    }

    public static void post(final String url, final String content, final Callback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String response = NetUtils.post(url,content);
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(response);
                    }
                });
            }
        }).start();
    }


    public static String post(String url,String content){
        BufferedReader reader = null;
        HttpURLConnection conn = null;
        try{
            URL mURL = new URL(url);
            conn = (HttpURLConnection) mURL.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setDoInput(true);
            //post不用拼接
            String data = content;
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(data);
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            if(responseCode==200) {
                InputStream inputStream = conn.getInputStream();
                String line;
                StringBuilder response = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                while((line=reader.readLine())!=null){
                    response.append(line);
                }
                return response.toString();
            }else{
                throw new NetworkErrorException("网络连接错误"+responseCode);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NetworkErrorException e) {
            e.printStackTrace();
        }finally {
            if(reader!=null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }
    public static void getBitmap(final String url,final getBitmapCallback callback){

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = NetUtils.getBitmap(url);
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.mBitmap(bitmap);
                    }
                });
            }
        }).start();
    }

    public static Bitmap getBitmap(String url){
        URL mURL;
        Bitmap bitmap=null;
        try {
            mURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) mURL.openConnection();
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream in = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
