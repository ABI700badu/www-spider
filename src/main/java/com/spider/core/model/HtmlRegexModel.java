package com.spider.core.model;

import com.spider.core.util.RegexUtil;
import com.spider.core.util.StringUtil;

import java.util.List;
import java.util.regex.Pattern;

public class HtmlRegexModel extends ModelSchema implements IModel{
    private String model =null;
    public HtmlRegexModel(String model){
       this.model = model;
    }
    public HtmlRegexModel(){}
    @Override
    public String modify(String html,String url){
        html=super.preHandle(html,url);
        if(model !=null){
           List<String> tags= getRootTags(model);
           for(String tag:tags){
               String tagValue= getTagValue(tag);
               int index= getIndex(tag);
               List<String> fixHtml=RegexUtil.getAllMatcher(html,index,Pattern.compile(tagValue,Pattern.DOTALL));
               model=model.replace(tag,StringUtil.convertList2Str(fixHtml,"").trim());
           }
            return model;
        }else {
            return html;
        }
    }
}
