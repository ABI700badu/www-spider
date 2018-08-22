package com.spider.core.engine;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


import java.io.IOException;

/**
 * 此类基于htmlUnit 效率较低，一张网页，需要2-3秒，因为执行js就是请求网络，本来就比较耗时
 */
public class WebSpider extends Spider{
    private static  final WebClient WEB_CLIENT=new WebClient();

    public WebSpider(int maxSize, int numOfThread) {
        super(maxSize, numOfThread);
        // 设置webClient的相关参数
        WEB_CLIENT.setAjaxController(new NicelyResynchronizingAjaxController());
        WEB_CLIENT.getOptions().setJavaScriptEnabled(true);
        WEB_CLIENT.getOptions().setCssEnabled(true);
        WEB_CLIENT.getOptions().setTimeout(35000);
        WEB_CLIENT.getOptions().setThrowExceptionOnScriptError(false);
    }

    /**
     * 此方法必须要同步，这好像是htmlUnit的bug，不支持多线程
     * @param url
     * @return
     * @throws IOException
     */
    synchronized public HtmlPage  getPage(String url) throws IOException {
        return (HtmlPage)WEB_CLIENT.getPage(url);
    }

    @Override
    protected String toGetHtmlPage(String url, String charset) throws IOException {
        return getPage(url).asXml();
    }
    @Override
    protected void close() {
        super.close();
        WEB_CLIENT.close();
    }
}
