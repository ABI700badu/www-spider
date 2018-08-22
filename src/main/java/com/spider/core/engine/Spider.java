package com.spider.core.engine;

import com.spider.core.model.IModel;
import com.spider.core.template.ICrawlTemplate;
import com.spider.core.net.HttpClientUtil;
import com.spider.core.util.StringUtil;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * 1，对于普通的，不需要执行js代码来动态获取网页内容的来讲，这个爬虫的速度是十分乐观的。
 * 2，对于需要执行js代码，异步加载页面的网页，那可就比较坑了，此类不支持.
 *      通常，可以通过重载{@link #toGetHtmlPage(String, String)}去获取你想要的html页面的样子
 * @see WebSpider 。 这是一个针对需要执行js代码的例子
 *
 */
public class Spider extends BasicSpider {
	private static Logger logger= LoggerFactory.getLogger(Spider.class);
	static {
		DOMConfigurator.configure( Spider.class.getClassLoader().getResource("www-spider.xml"));
	}
	private int mSize;
	private IModel model=null;
	private ExecutorService mPool;
	private BlockedUniQueue mQueue=new BlockedUniQueue();
	public Spider(int maxSize, int numOfThread){
		mSize=maxSize;
		mPool=Executors.newFixedThreadPool(numOfThread);
	}
	public final void downLoadArtcle(ICrawlTemplate t)  {
		final ICrawlTemplate temp =t;
		mQueue.setPosLimit(mSize);
		mQueue.enQueue(temp.getBaseSite());
		String site=null;
		try {
            while ((site = (String)mQueue.blockingTake()) != null) {
                final String url=site;
                mPool.execute(new Runnable() {
                    public void run() {
                        if(!temp.filterUrl(url)){
                            return;
                        }
                        try {
                            if(logger.isDebugEnabled()) {
                                logger.info("spider robot is crawing: {}", url);
                            }
                            String html = toGetHtmlPage(url, temp.getCharset());
                            if (StringUtil.isBlank(html)) {
                                html = toGetHtmlPage(switchHttpOrHttps(url), temp.getCharset());
                            }
                            if(StringUtil.isEmpty(html)){
                                if(logger.isDebugEnabled())
                                    logger.info("无效的url={}",url);
                            }else {
                                if (logger.isDebugEnabled()) {
                                    logger.info("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>\n{}\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>", html);
                                }
                            }
                            allHrefEnQueue(html, url, mQueue);
                            if(model!=null) {
                                html = model.modify(html, url);
                                if(logger.isDebugEnabled()) {
                                    logger.info("model处理返回：{}", html);
                                }
                            }
                            if(!StringUtil.isEmpty(html)) {
                                temp.crawlValue(html, url);
                            }
                        } catch (Exception e) {//由于子线程的异常，不去捕捉，是不会打印的，所以这里需要捕捉一下
                            logger.error("子线程异常ex:", e);
                        }
                    }
                });
            }
        }catch (Exception e){
		    logger.error("主线程异常：",e);
        }
        close();
	}
	public final void setModel(IModel model){
	    this.model=model;
    }

    /**
     * 此方法是基于url获取网页页面的方法，子类可以重载此方法,以获取想要的获取页面的方式效果
     * 比如子类想让爬虫爬取网页的时候，模仿浏览器执行js代码
     * 此方法的返回值为抓取到该url对应的html内容（含标签）
     * @param url
     * @param charset
     * @return
     * @throws IOException
     */
	protected String toGetHtmlPage(String url,String charset) throws IOException {
	    if(charset!=null)
            try {
                return HttpClientUtil.toDoGetUrlConnectStr(url,charset);
            } catch (IOException e) {
	            if(logger.isDebugEnabled()){
	                logger.info("网络请求异常:{}",e);
                }
                return null;
            }
        else
            try {
                return HttpClientUtil.toDoGetUrlConnectStr(url);
            } catch (IOException e) {
                if(logger.isDebugEnabled()){
                    logger.info("网络请求异常:{}",e);
                }
                return null;
            }
    }

    /**
     * 此方法用于关闭爬虫
     * 子类可重载此方法，关闭一些资源。需要注意子类的方法也必须调用父类的此方法，已释放一些资源
     */
    protected void close(){
	    mPool.shutdown();
    }
}
