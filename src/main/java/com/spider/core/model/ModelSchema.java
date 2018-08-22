package com.spider.core.model;

import com.spider.core.browser.Browser;
import com.spider.core.util.RegexUtil;

import java.util.List;
import java.util.regex.Pattern;

public class ModelSchema {
    /**
     * 定义顶级标签组
     */
    protected static  final Pattern ROOT_TAG =Pattern.compile("<_%.*?>(.*?)</_%>",Pattern.DOTALL);
    /**
     * 定义子标签
     * 说明：1，因为要求标签能够随意嵌入，像jsp一样，所以基于XML解析便不可能
     *       2，但是可以将所有父亲标签提取出来，父标签与子标签采用xml解析
     */
    protected static final Pattern SON_TAG =Pattern.compile("<_s.*?>(.*?)</_s>",Pattern.DOTALL);
    /**
     * 取标签index属性值
     */
    protected static final Pattern TAG_INDEX =Pattern.compile("<_%.*?index=\"(.*?)\"");
    /**
     * 取标签的KEY属性
     */
    protected static final  Pattern TAG_KEY =Pattern.compile("<_%.*?key=\"(.*?)\"");
    /**
     * 取标签的SELECTOR属性
     */
    protected static final Pattern TAG_SELECTOR =Pattern.compile("<_%.*?selector=\"(.*?)\"");
    /**
     * 取标签index属性值
     */
    protected static final Pattern SON_TAG_INDEX =Pattern.compile("<_s.*?index=\"(.*?)\"");
    /**
     * 取标签的KEY属性
     */
    protected static final  Pattern SON_TAG_KEY =Pattern.compile("<_s.*?key=\"(.*?)\"");
    /**
     * 取标签的SELECTOR属性
     */
    protected static final Pattern SON_TAG_SELECTOR =Pattern.compile("<_s.*?selector=\"(.*?)\"");
    /**
     * 创建一个单列modeloptions
     */
    private ModelOptions options=null;
    /**
     * 处理image标签组
     */
    private static final Pattern PATTERN_IMAGE=Pattern.compile("<img(.*?)>",Pattern.DOTALL);
    private static final Pattern PATTERN_IMAGE_SRC=Pattern.compile("src=\"(.*?)\"",Pattern.DOTALL);
    /**
     * 处理css标签
     */
    private static final Pattern PATTER_CSS=Pattern.compile("<link(.*?)>",Pattern.DOTALL);
    private static final Pattern PATTERN_CSS_HREF=Pattern.compile("href=\"(.*?)\"",Pattern.DOTALL);
    /**
     * 处理js标签
     */
    private static final Pattern PATTERN_JS=Pattern.compile("<script(.*?)>||<script(.*?)/>",Pattern.DOTALL);
    private static final Pattern PATTERN_JS_SRC=Pattern.compile("src=\"(.*?)\"",Pattern.DOTALL);
    public ModelOptions getOptions() {
        synchronized (this){
            if(options==null){
                options=new ModelOptions();
            }
        }
        return options;
    }
    protected String preHandle(String html,String url){
        if(getOptions().getAbsoImage()){
            List<String> images= RegexUtil.getAllMatcher(html,0,PATTERN_IMAGE);
            for(String image:images){
                String src=RegexUtil.getFirstMatcher(image,1,PATTERN_IMAGE_SRC);
                if(src!=null){
                    String newimage=image.replace(src, Browser.relative2TrUri(src,url));//注意这里的层级替换
                    html=html.replace(image,newimage);
                }
            }
        }
        if(getOptions().getAbsoCss()){
            List<String> csss=RegexUtil.getAllMatcher(html,0,PATTER_CSS);
            for(String css:csss){
                String href=RegexUtil.getFirstMatcher(css,1,PATTERN_CSS_HREF);
                if(href!=null) {
                    String newsCss = css.replace(href, Browser.relative2TrUri(href, url));
                    html=html.replace(css,newsCss);
                }
            }
        }
        if(getOptions().getAbsoJs()){
            List<String> jss=RegexUtil.getAllMatcher(html,0,PATTERN_JS);
            for(String js:jss){
                String src=RegexUtil.getFirstMatcher(js,1,PATTERN_JS_SRC);
                if(src!=null) {
                    String newJs = js.replace(src, Browser.relative2TrUri(src, url));
                    html = html.replace(js, newJs);
                }
            }
        }
        return html;
    }
    public List<String> getRootTags(String model){
        return RegexUtil.getAllMatcher(model,0, ROOT_TAG);
    }
    public int getIndex(String tag){
        return Integer.parseInt(RegexUtil.getFirstMatcher(tag,1, TAG_INDEX));
    }
    public String getKey(String tag){
        return RegexUtil.getFirstMatcher(tag,1, TAG_KEY);
    }
    public String getSelector(String tag){
        return RegexUtil.getFirstMatcher(tag,1, TAG_SELECTOR);
    }
    public String getTagValue(String tag){
        return RegexUtil.getFirstMatcher(tag,1, ROOT_TAG);
    }

    public List<String> getSonTags(String model){
        return RegexUtil.getAllMatcher(model,0, SON_TAG);
    }
    public int getSonIndex(String tag){
        return Integer.parseInt(RegexUtil.getFirstMatcher(tag,1, SON_TAG_INDEX));
    }
    public String getSonKey(String tag){
        return RegexUtil.getFirstMatcher(tag,1, SON_TAG_KEY);
    }
    public String getSonSelector(String tag){
        return RegexUtil.getFirstMatcher(tag,1, SON_TAG_SELECTOR);
    }
    public String getSonTagValue(String tag){
        return RegexUtil.getFirstMatcher(tag,1, SON_TAG);
    }
}
