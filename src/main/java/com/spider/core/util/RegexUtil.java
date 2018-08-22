package com.spider.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
    public static   boolean isMathces(String str,Pattern pattern){
        return pattern.matcher(str).matches();
    }
    public static String getFirstMatcher(String str,int groupIndex,Pattern pattern){
        Matcher m=pattern.matcher(str);
        while (m.find()){
            String mstr=m.group(groupIndex);
            if(mstr!=null){
                return mstr;
            }
        }
        return null;
    }
    public static List<String> getAllMatcher(String str,int groupIndex,Pattern pattern){
        List<String> l=new ArrayList<>();
        Matcher m=pattern.matcher(str);
        while (m.find()){
            String mstr=m.group(groupIndex);
            if(mstr!=null){
                l.add(mstr);
            }
        }
        return l;
    }
}
