package com.spider.core.browser;


import com.spider.core.util.FileUtil;
import com.spider.core.util.StringUtil;

/**
 * 浏览器 解析链接地址的行为
 * 1 若该页面地址是文件,则去掉文件名
 * 2 若相对地址包括../,则页面地址去掉文件名后，还需要向上跳几个级别
 * 3 若是/开头，直接就是原路径的域名+url
 */

public class Browser {
    private static final String RELATIVE_URL_FLAG="../";//相对路径标志
    private static final String CURRENT_URL_FLAG="./";//当前路径标识
    public static  boolean isRelativeUrl(String url){
        return UrlTypeEnum.fromKey(url) == UrlTypeEnum.RELATIVE;
    }

  /*  public static void main(String[] args) {
        System.out.println(relative2TrUri("../../lijianbo/wo/index.html","http://www.baidu.com"));
    }*/
    public static String relative2TrUri(String reUrl, String oriUrl){
        UrlTypeEnum ut= UrlTypeEnum.fromKey(reUrl);
        if(ut== UrlTypeEnum.RELATIVE){
            if(reUrl.startsWith("/")){
                return FileUtil.componseDir(UrlParse.getDomain(oriUrl), reUrl);
            }else if(reUrl.startsWith(CURRENT_URL_FLAG)){
                if(reUrl.contains(RELATIVE_URL_FLAG)){
                    int level= StringUtil.findStrReapeatTimes(reUrl,RELATIVE_URL_FLAG);
                    return FileUtil.componseDir(UrlParse.upDir(oriUrl,level),
                            reUrl.substring(reUrl.lastIndexOf(RELATIVE_URL_FLAG)+RELATIVE_URL_FLAG.length(), reUrl.length()));
                }else {
                    return FileUtil.componseDir(UrlParse.delFileName(oriUrl), reUrl.substring(reUrl.lastIndexOf(CURRENT_URL_FLAG)+CURRENT_URL_FLAG.length(), reUrl.length()));
                }
            }else if(reUrl.startsWith(RELATIVE_URL_FLAG)){
                int level= StringUtil.findStrReapeatTimes(reUrl,RELATIVE_URL_FLAG);
                return FileUtil.componseDir(UrlParse.upDir(oriUrl,level),
                        reUrl.substring(reUrl.lastIndexOf(RELATIVE_URL_FLAG)+RELATIVE_URL_FLAG.length(), reUrl.length()));
            }else {
                return FileUtil.componseDir(UrlParse.delFileName(oriUrl), reUrl);
            }
        }
        else
            return ut.getAddedPrefix()+ reUrl;
    }

}
