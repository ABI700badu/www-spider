package com.spider.core.net;



import com.spider.core.util.StringUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Set;

public class HttpClientUtil {
    public static  int CONNECT_TIMEOUT=5000;
    public static  int READ_TIMEOUT=5000;
    public static final String GET="GET";
    public static final String POST="POST";
    private static  int bufferSize =1024*8;
    private static byte [] byteCache =new byte[bufferSize];//默认8kb的缓存空间
    public static String filterUrl(String url) {
        String[] params = url.split("\\?");
        if (params.length >= 2) {
            url = params[0] + "?" + params[1].replaceAll(" ", "%20").replaceAll("/", "%2F");
        }
        return url;
    }
    public static void setBufferSize(int size){
        bufferSize=size;
        byteCache=new byte[bufferSize];
    }
    public static HttpURLConnection initHttpNet(String url, String method,int connectTimeout,int readTimeout) throws IOException {
        url = filterUrl(url);
        URL u;
        HttpURLConnection conn = null;
        u = new URL(url);
        conn = (HttpURLConnection) u.openConnection();
        conn.setConnectTimeout(connectTimeout);
        conn.setReadTimeout(readTimeout);
        conn.setRequestMethod(method);
        return conn;
    }
    public static byte[] toDoGetUrlConnectBytes(String url) throws IOException {
        HttpURLConnection connection=null;
        InputStream in=null;
        BufferedInputStream bi=null;
        ByteArrayOutputStream baos=null;
        try {
            connection = initHttpNet(url, GET, CONNECT_TIMEOUT, READ_TIMEOUT);
            in = connection.getInputStream();
            bi = new BufferedInputStream(in, bufferSize);
            baos = new ByteArrayOutputStream();
            int count;
            while ((count = bi.read(byteCache)) != -1) {
                baos.write(byteCache, 0, count);
            }
        }finally {
            if(baos!=null){
                try {
                    baos.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                    return new byte[0];
                }
            }
            if(bi!=null){
                try {
                    bi.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
            if(connection!=null){
                connection.disconnect();
            }
        }
        return baos.toByteArray();
    }
    public static String toDoGetUrlConnectStr(String url) throws IOException {
        HttpURLConnection connection=null;
        InputStream in=null;
        BufferedInputStream bi=null;
        ByteArrayOutputStream baos=null;
        String charset=null;
        try {
            connection = initHttpNet(url, GET, CONNECT_TIMEOUT, READ_TIMEOUT);
            String ct=connection.getContentType();
            if(StringUtil.isNotBlank(ct)) {
                Set<String> set=Charset.availableCharsets().keySet();
                charset= StringUtil.getSubStrInArray(ct,set.toArray(new String[]{}),true);
                if(StringUtil.isEmpty(charset)){
                    charset=connection.getContentEncoding();
                }
            }
            in = connection.getInputStream();
            bi = new BufferedInputStream(in, bufferSize);
            baos = new ByteArrayOutputStream();
            int count;
            while ((count = bi.read(byteCache)) != -1) {
                baos.write(byteCache, 0, count);
            }
        }finally {
            if(baos!=null){
                try {
                    baos.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                    return null;
                }
            }
            if(bi!=null){
                try {
                    bi.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
            if(connection!=null){
                connection.disconnect();
            }
        }
        if(StringUtil.isNotBlank(charset)){
            return new String(baos.toByteArray(),charset);
        }
        return  new String(baos.toByteArray());
    }
    public static String toDoGetUrlConnectStr(String url,String charset) throws IOException {
        return new String(toDoGetUrlConnectBytes(url),charset);
    }
    public static byte[] toDoPostConnectBytes(String url,byte []sendBytes) throws IOException {
        HttpURLConnection conn= initHttpNet(url,POST,CONNECT_TIMEOUT,READ_TIMEOUT);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Connection", "Keep-Alive");
        BufferedOutputStream bo=new BufferedOutputStream(conn.getOutputStream(), bufferSize);
        bo.write(sendBytes,0,sendBytes.length);
        bo.flush();
        InputStream in=null;
        BufferedInputStream bi=null;
        ByteArrayOutputStream baos=null;
        try {
          in = conn.getInputStream();
            bi = new BufferedInputStream(in, bufferSize);
            baos = new ByteArrayOutputStream();
            int count;
            while ((count = bi.read(byteCache)) != -1) {
                baos.write(byteCache, 0, count);
            }
        }finally {
            if(bo!=null){
                bo.close();
            }
            if(baos!=null){
                try {
                    baos.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                    return new byte[0];
                }
            }
            if(bi!=null){
                try {
                    bi.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
            if(conn!=null){
                conn.disconnect();
            }
        }
        return baos.toByteArray();
    }
    public static String toDoPostConnectStr(String url,String sendMsg,String charset) throws IOException {
        return new String(toDoPostConnectBytes(url,sendMsg.getBytes(charset)),charset);
    }
}
