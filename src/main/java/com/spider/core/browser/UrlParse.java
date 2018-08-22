package com.spider.core.browser;


import com.spider.core.util.FileUtil;
import com.spider.core.util.RegexUtil;
import com.spider.core.util.StringUtil;

import java.util.EventListener;
import java.util.regex.Pattern;

public class UrlParse {
    private static final Pattern PORT=Pattern.compile(":([0-9]+)");

    /*public static void main(String[] args) {
        String url="http://lijianbo.net:8080/drivertool/sub/index.html";
        System.out.println("protocol:"+getProtocol(url));
        System.out.println("port:"+getPort(url));
        System.out.println("host:"+getHost(url));
        System.out.println("domain:"+getDomain(url));
        System.out.println("domain no port:"+getDomainWithoutPort(url));
        System.out.println("fileName:"+getFileName(url));
        System.out.println("del fileName:"+delFileName(url));
        System.out.println("up dir:"+upDir(url,1));
    }*/
    public static String upDir(String dir,int leve){
        String delUrl=delFileName(dir);
        delUrl= StringUtil.removeEndSeqChar(delUrl,"/");
        String prefix=getProtocolPrefix(delUrl);
        delUrl=delProtocolPrefix(delUrl);
        for(int i=leve;i>0;i--){
            if(delUrl.lastIndexOf("/")>0)
                delUrl=delUrl.substring(0,delUrl.lastIndexOf("/"));
            else{
                return prefix+delUrl;
            }
        }
        return prefix+delUrl;
    }
    public static String getFileName(String url){
        url= StringUtil.removeEndSeqChar(url,"/");
        url=delProtocolPrefix(url);
       return FileUtil.getFileName(url);
    }
    public static String delFileName(String url){
        String fileName=getFileName(url);
        if(!StringUtil.isEmpty(fileName)&&fileName.contains(".")){//确实是文件名
            return url.substring(0,url.indexOf(fileName));
        }
        return url;
    }
    public static String getPort(String url){
        String doMain=getDomain(url);
        return RegexUtil.getFirstMatcher(doMain,1,PORT);
    }
    public static String getDomainWithoutPort(String url){
        String domain=getDomain(url);
        String port= RegexUtil.getFirstMatcher(domain,0,PORT);
        return domain.substring(0,domain.indexOf(port));
    }
    public static String getDomain(String url){
        if(Browser.isRelativeUrl(url)){
            return null;
        }
        String prefix=getProtocolPrefix(url);
        String url0=delProtocolPrefix(url);
        if(url0.contains("/")){
            url0=url0.substring(0,url0.indexOf("/"));
        }
        return prefix+url0;
    }
    public static String getHost(String url){
        if(Browser.isRelativeUrl(url)){
            return null;
        }
        String url0=delProtocolPrefix(url);
        if(url0.contains(":")){
            url0=url0.substring(0,url0.indexOf(":"));
        }
        return url0;
    }
    public static String delProtocolPrefix(String url){
      UrlTypeEnum type= UrlTypeEnum.fromKey(url);
      String urlPrefix=type.getAbsoUrlPrefixFlag();
      if(urlPrefix==null){
          return url;
      }
      return url.substring(urlPrefix.indexOf(urlPrefix)+urlPrefix.length(),url.length());
    }
    public static String getProtocolPrefix(String url){
        UrlTypeEnum ut= UrlTypeEnum.fromKey(url);
        return ut.getAbsoUrlPrefixFlag();
    }
    public static String getProtocol(String url){
       UrlTypeEnum ut= UrlTypeEnum.fromKey(url);
       return ut.getProtocol();
    }
}
