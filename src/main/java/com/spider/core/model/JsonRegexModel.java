package com.spider.core.model;

import com.spider.core.util.RegexUtil;
import com.spider.core.util.StringUtil;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by lijb on 2018/8/14.
 */
public class JsonRegexModel extends ModelSchema implements IModel {
    private  final String model;
    public JsonRegexModel(String model){
        this.model=model;
    }
    @Override
    public String modify(String html, String url) {
        html= super.preHandle(html, url);
        if(model==null){
            return html;
        }
        StringBuilder stringBuilder=new StringBuilder();
        //step1:找到文中所有匹配tag的字符串
        List<String> tags=getRootTags(model);
        stringBuilder.append("{");
        for(int i=0;i<tags.size();i++){
            String tag=tags.get(i);
            //step2:提取value,key与index属性
            int index= getIndex(tag);
            String key= getKey(tag);
            String tagValue= getTagValue(tag);
            //step3:构建返回结果
            List<String> values=RegexUtil.getAllMatcher(html,index,Pattern.compile(tagValue,Pattern.DOTALL));
            if(!StringUtil.isEmpty(key)&&values!=null&&values.size()>0) {
                stringBuilder.append("\"").append(key).append("\":");
                stringBuilder.append("[");
                for(int j=0;j<values.size();j++){
                    stringBuilder.append("\"").append(values.get(j).trim()).append("\"");
                    if(j!=values.size()-1){
                        stringBuilder.append(",");
                    }
                }
                stringBuilder.append("]");
                if (i != tags.size() - 1) {
                    stringBuilder.append(",");
                }
            }
        }
        stringBuilder.append("}");
        return stringBuilder.length()==2?null:stringBuilder.toString();
    }
}
