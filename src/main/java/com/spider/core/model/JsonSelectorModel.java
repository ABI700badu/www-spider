package com.spider.core.model;

import com.spider.core.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by lijb on 2018/8/16.
 */
public class JsonSelectorModel extends ModelSchema implements IModel{
    private final String model;
    public JsonSelectorModel(String model){
        this.model=model;
    }
    @Override
    synchronized public String modify(String html, String url) {
        if(StringUtil.isEmpty(html)){
            return null;
        }
        html=super.preHandle(html,url);
        if(model==null){
            return html;
        }
        StringBuilder sb=new StringBuilder();
        List<String> tags=getRootTags(model);

        sb.append("{");
        for(int j=0;j<tags.size();j++){
            String tag=tags.get(j);
            String key=getKey(tag);
            if(StringUtil.isEmpty(key)){
                continue;
            }
            String selector= getSelector(tag);
            Document doc= Jsoup.parse(html);

            if(doc!=null){
                Elements es=doc.getElementsByClass(selector);
                if(es.size()>0)
                    sb.append("\"").append(key).append("\":");
                else {
                    continue;
                }
                sb.append("[");
                for(int i=0;i<es.size();i++){
                    List<String> sonList=getSonTags(tag);
                    if(sonList!=null&&sonList.size()>0){
                        Element sonDiv=es.get(i);
                        sb.append("{");
                        StringBuilder temp=new StringBuilder();
                        for(int s=0;s<sonList.size();s++){
                            String sKey=getSonKey(sonList.get(s));
                            String sSelector=getSonSelector(sonList.get(s));
                            Elements sEs=sonDiv.getElementsByClass(sSelector);
                            if(sEs!=null&&sEs.size()>0) {
                                temp.append("\"").append(sKey).append("\":");
                                temp.append("\"").append(sEs.get(0).html()).append("\"");
                            }
                            if(s!=sonList.size()-1&&temp.length()>0){
                                temp.append(",");
                            }
                        }
                        sb.append(temp);
                        sb.append("}");
                    }else {
                        sb.append("\"").append(es.get(i).html().trim()).append("\"");
                    }
                    if (i != es.size() - 1) {
                        sb.append(",");
                    }
                }
                sb.append("]");
            }
            if(j!=tags.size()-1){
                sb.append(",");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
