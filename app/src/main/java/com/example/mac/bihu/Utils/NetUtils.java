package com.example.mac.bihu.Utils;

import android.accounts.NetworkErrorException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by HP on 2017/12/28.
 * 这是一个利用HttpURLConnection实现的工具类
 */

public class NetUtils  {

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
    public static Bitmap getBitmap(String url){
        URL mURL;
        Bitmap bitmap=null;
        try {
            mURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) mURL.openConnection();
            conn.setConnectTimeout(8000);
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            InputStream in = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
        return bitmap;
    }
}
