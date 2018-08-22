package com.spider.core.template;


import com.spider.core.browser.Browser;
import com.spider.core.util.DateUtil;
import com.spider.core.util.FileUtil;
import com.spider.core.util.RegexUtil;
import com.spider.core.util.StringUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public abstract class AbstractTemplate implements ICrawlTemplate{
    String baseSite;
    private static final Pattern META=Pattern.compile("<meta.*?content=\"(.*?)\">",Pattern.DOTALL);
    public AbstractTemplate(String site){
        baseSite=site;
    }
    public AbstractTemplate(){}

    @Override
    public String getCharset() {
        return null;
    }

    @Override
    public boolean filterUrl(String url) {
        return true;
    }

    @Override
    public String getBaseSite() {
        return Browser.relative2TrUri(baseSite,baseSite);
    }
    protected void saveHtml(String html, String filePath, String charset) throws IOException {
        if(StringUtil.isEmpty(html)){
            return;
        }
        if(StringUtil.isEmpty(charset)){
           List<String> contents = RegexUtil.getAllMatcher(html,1,META);
           for(String content:contents){
               String charencoding=StringUtil.getSubStrInArray(content, Charset.availableCharsets().keySet().toArray(new String[]{}),true);
                if(!StringUtil.isEmpty(charencoding)){
                    charset=charencoding;
                    break;
                }
           }
        }
        if(StringUtil.isEmpty(charset)){
            charset="UTF-8";
        }
        FileUtil.string2NewFile(html,filePath,charset);
    }
    protected String getUniqueId(){
        return DateUtil.getCurrentDateStr(DateUtil.MSEC_FORMAT)+new Random().nextInt(1000000);
    }
    protected String getHashCode(String url){
        return hashKeyForDisk(url);
    }
    public  String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private  String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
