package com.ideal.manage.dsp.util;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jin on 2017/4/17.
 */
public class HttpUtils {
    public static String post(String url,String content,String contentType) throws Exception{
        URL postUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection)postUrl.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        // Post 请求不能使用缓存
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        // 进行编码
        connection.setRequestProperty("Content-Type",contentType);
        connection.connect();
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(content);
        out.flush();
        out.close();
        //读取返回内容
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null){
            sb.append(line);
        }
        reader.close();
        connection.disconnect();
        return sb.toString();
    }
}
