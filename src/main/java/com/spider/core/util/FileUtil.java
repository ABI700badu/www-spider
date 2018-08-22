package com.spider.core.util;


import com.spider.core.net.HttpClientUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.regex.Pattern;

/**
 * @author
 */
public class FileUtil {
    /**
     * The cache area cannot be too large or too small,
     * and too small to cause unnecessary memory overhead.
     * Too small may cause multiple IO
     */
    public static final int BUFFER_SIZE = 8192;//默认8KB的缓存大小

    /**
     *
     * @param in
     * @param saveFilePath
     * @param bufferSize
     * The cache area cannot be too large or too small,
     * and too small to cause unnecessary memory overhead.
     * Too small may cause multiple IO
     * @param isCreateNewFile
     * @throws IOException
     */
    public static void saveFileByInputStream(InputStream in, String saveFilePath, int bufferSize,boolean isCreateNewFile) throws IOException {
        BufferedInputStream bi=new BufferedInputStream(in,bufferSize);
        try {
            if (isCreateNewFile) {
                createNewFile(saveFilePath);
            }
            byte[] buffer = new byte[bufferSize];
            int count;
            int start = 0;
            while ((count = bi.read(buffer, start, bufferSize - start)) != -1) {
                if (count < bufferSize) {
                    start += count;
                } else {
                    start = 0;
                    bytes2AppendFile(saveFilePath, buffer);
                }
            }
            if (start > 0) {//将剩余字节保存到磁盘
                bytes2AppendFile(saveFilePath, buffer);
            }
        } finally {
            if (bi != null) {
                try {
                    bi.close();
                } catch (IOException e) {

                }
            }
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {

                }
            }
        }
    }

    /**
     * @param url
     * @param filePath File with path, if the path does not exist, create a path, and delete the file if the file name exists
     * @return
     * @throws IOException Includes file IO and network IO exceptions
     */
    public static void loadImageFromUrl(String url, String filePath) throws IOException {
        InputStream in = null;
        HttpURLConnection conn = null;
        try {
            conn = HttpClientUtil.initHttpNet(url, HttpClientUtil.POST, HttpClientUtil.CONNECT_TIMEOUT, HttpClientUtil.READ_TIMEOUT);
            if (conn != null) {
                conn.setDoInput(true);
                conn.setUseCaches(false);
                in = conn.getInputStream();
                saveFileByInputStream(in, filePath, BUFFER_SIZE,true);
            }
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * Use nio to turn bytes into a file
     *
     * @param filePath If the file path does not exist, create the path
     * @param bytes
     * @throws IOException
     */
    public static void bytes2NewsFile(String filePath, byte[] bytes) throws IOException {
        FileChannel fc = null;
        try {
            RandomAccessFile asf = new RandomAccessFile(createNewFile(filePath), "rw");
            fc = asf.getChannel();
            ByteBuffer buf = ByteBuffer.wrap(bytes);
            while (buf.hasRemaining()) {
                fc.write(buf);
            }
            buf.clear();
        } finally {
            if (fc != null)
                fc.close();

        }
    }

    public static void bytes2AppendFile(String filePath, byte[] bytes) throws IOException {
        FileChannel fc = null;
        try {
            RandomAccessFile asf = new RandomAccessFile(createJustOncesFile(filePath), "rw");
            fc = asf.getChannel();
            fc.position(asf.length());//移动到文件末尾
            ByteBuffer buf = ByteBuffer.wrap(bytes);
            while (buf.hasRemaining()) {
                fc.write(buf);
            }
            buf.clear();
        } finally {

            if (fc != null)
                fc.close();

        }
    }

    /**
     * @param fileStr
     * @param filePath
     * @param charset  fthe encode of fileStr
     * @throws IOException
     */
    public static void string2NewFile(String fileStr, String filePath, String charset) throws IOException {
        bytes2NewsFile(filePath, fileStr.getBytes(charset));
    }
    /**
     * @param fileStrValue
     * @param filePath
     * @param charset
     * @param rn
     * @return
     * @throws IOException
     */
    @Deprecated
    public static File appendString2File(String fileStrValue, String filePath, String charset, boolean rn) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            mkdirsPath(filePath);
            try {
                file.createNewFile();
            } catch (IOException e) {
                //e.printStackTrace();
                return null;
            }
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(file, true);
            if (rn) {
                fw.write("\r\n");
            }
            fw.write(fileStrValue);
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        }
        return file;
    }

    public static File createNewFile(String filePath) throws IOException {
        File f = new File(filePath);
        if (f.exists()) {
            f.delete();
        }
        if(f.getParentFile()!=null&&!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        f.createNewFile();
        return f;
    }

    public static File createJustOncesFile(String filePath) throws IOException {
        File f = new File(filePath);
        if (f.exists()) {
            return f;
        }
        f.getParentFile().mkdirs();
        f.createNewFile();
        return f;
    }

    /**
     * create dirs
     *
     * @param dir
     */
    public static void mkdirs(String dir) {
        File f = new File(dir);
        if (f.isDirectory() && f.exists()) {
            return;
        }
        f.mkdirs();
    }

    /**
     * This method is just for Linux,windows
     *
     * @param filePath
     */
    public static void mkdirsPath(String filePath) {
        if (filePath.contains("/")) {
            mkdirs(filePath.substring(0, filePath.lastIndexOf("/")));
        } else if (filePath.contains("\\")) {
            mkdirs(filePath.substring(0, filePath.lastIndexOf("\\")));
        } else {
            return;
        }
    }

    /**
     * just for linux
     * eg: aa/ /bb/ cc/
     * return aa/bb/cc/
     * @param relativeDirs
     * @return
     */
    public static String componseDir(String ...relativeDirs){
        StringBuilder stringBuilder=new StringBuilder();
        String []dirs=relativeDirs;
        for(int i=0;i<dirs.length;i++){
            String dir=dirs[i];
            if(i==0){//最前面一个
                if(dir.endsWith("/"))
                    stringBuilder.append(dir.substring(0,dir.lastIndexOf("/")));
                else
                    stringBuilder.append(dir);
                continue;
            }
            if(i==dirs.length-1){//最后一个
                if(dir.startsWith("/"))
                    stringBuilder.append(dir);
                else
                    stringBuilder.append("/").append(dir);
                break;
            }
            if(dir.endsWith("/")){
                dir=dir.substring(0,dir.lastIndexOf("/"));
            }
            if(dir.startsWith("/"))
                stringBuilder.append(dir);
            else
                stringBuilder.append("/").append(dir);
        }

        return stringBuilder.toString();
    }
    /**
     * just for linux
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath){
        if(filePath.contains("/")&&!filePath.endsWith("/"))
            return filePath.substring(filePath.lastIndexOf("/")+1,filePath.length());
        else
            return null;
    }
    public static String getFileDir(String filePath){
        String fileName=getFileName(filePath);
        if(fileName.contains(".")){//确实包含文件名
            if(filePath.contains("/")){
                return filePath.substring(0,filePath.lastIndexOf("/"));
            }
        }
        return filePath;
    }

}
