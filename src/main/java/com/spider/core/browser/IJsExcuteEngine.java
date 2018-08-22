package com.spider.core.browser;

import org.w3c.dom.Document;

import java.util.List;

/**
 * Created by lijb on 2018/8/16.
 */
public interface IJsExcuteEngine {
    /**
     * 找到所有js代码,不包括引用的外部js代码
     * @param html
     * @param url 该html对应的链接，解析相对链接需要用到
     * @return
     */
    String findAllJs(String html,String url);

    /**
     * 从外部链接获取js代码
     * @return
     */
    List<String> findExternJs(String href);

    /**
     * 执行js方法
     * @param methodName
     * @param doc
     * @return
     */
    String excuteMethod(String methodName, Object doc);

    /**
     * 找到js中的所有方法名
     * @param js
     * @return
     */
    List<String> findAllMethods(String js);
}
