package com.spider.core.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by lijb on 2018/8/16.
 */
public class HtmlSelectorModel extends ModelSchema implements IModel{
    private final String model;
    public HtmlSelectorModel(String model){
        this.model=model;
    }
    @Override
    public String modify(String html, String url) {
        html=super.preHandle(html,url);
        if(model==null){
            return html;
        }
        List<String> tags=getRootTags(model);
        for(String tag:tags){
            String selector= getSelector(tag);
            Document doc=Jsoup.parse(html);
            if(doc!=null){
                Elements es=doc.getElementsByClass(selector);
                if(es!=null&&es.size()>0){
                    model.replace(tag,es.get(0).html());
                }
            }
        }
        return model;
    }
}
