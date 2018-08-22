package com.spider.core.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class StringUtil {

    public static boolean isBlank(String str) {
        if (str == null||str.equals("")) {
            return true;
        } else {
            for (char ch : str.toCharArray()) {
                if (ch != ' ') {
                    return false;
                }
            }
            return true;
        }
    }
    public static boolean isNotBlank(String str){
        return !isBlank(str);
    }
    public static boolean isEmpty(String str) {
       return str==null||str.equals("");
    }
    public static String convertList2Str(List<String> l, String divider){
        return covertCollection2Str(l,divider);
    }
    public static String covertCollection2Str(Collection<String> l, String divider){
        StringBuffer sb=new StringBuffer();
        for(String s:l){
            if(!StringUtil.isEmpty(divider)){
                sb.append(divider);
            }
            sb.append(s);
        }
        String result=sb.toString();
        if(!StringUtil.isEmpty(divider)){
            return result.length()==0?"":sb.substring(divider.length(),result.length());
        }else {
            return result;
        }
    }

    /**
     * 截取字符串 在数组中有出现的部分
     * @param str
     * @param array
     * @return
     */
    public static String getSubStrInArray(String str,String []array,boolean isIgnoreCase){
        if(isIgnoreCase){
            for(String ele:array){
                if(str.toLowerCase().contains(ele.toLowerCase())){
                    return ele;
                }
            }
        }else {
            for(String ele:array){
                if(str.contains(ele)){
                    return ele;
                }
            }
        }
        return null;
    }

    /**
     * 找到字符串出现的次数
     * @param str
     * @param subStr
     * @return
     */
    public static int findStrReapeatTimes(String str,String subStr){
        int count=0;
        char []strC=str.toCharArray();
        char []subStrC=subStr.toCharArray();
        if(subStrC.length>1){
            for(int i=0;i<strC.length-subStrC.length+1;i++){
                if(strC[i]==subStrC[0]){
                    for(int j=1;j<subStrC.length;j++){
                        if(strC[i+j]==subStrC[j]){
                            if(j==subStrC.length-1){
                                i += j;
                                count++;
                            }
                        }else {
                            break;
                        }
                    }
                }
            }
        }
        else {
            for(int i=0;i<strC.length;i++){
                if(strC[i]==subStrC[0]){
                    count++;
                }
            }
        }
        return count;
    }
    public static String removeEndSeqChar(String url,String endChar){
        while (url.endsWith(endChar)){
            url=url.substring(0,url.lastIndexOf(endChar));
        }
        return url;
    }
}
