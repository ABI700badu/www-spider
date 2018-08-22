package com.spider.core.engine;


import com.spider.core.browser.Browser;
import com.spider.core.util.RegexUtil;
import com.spider.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicSpider {
	private Logger logger= LoggerFactory.getLogger(BasicSpider.class);
	private static final Pattern HREF=Pattern.compile("href=\"(.*?)\"",Pattern.DOTALL);
	/**
	 * To determine resource location, it is the relative or absolute address, and the general // or HTTP begins with an absolute address
	 * @param uri
	 * @return if is Realtive Uri return true 
	 */
	protected boolean isRelativeUri(String uri){
		return Browser.isRelativeUrl(uri);
	}
	/**
	 * Including (. / (. /)? (./), the beginning of the non-http letter, the four kinds of uris processing
	 * @param uri
	 * @param host  The actual host of the relative address
	 * @return Post-processing uri
	 */
	protected String relative2TrUri(String uri,String host){
		return Browser.relative2TrUri(uri,host);
	}
	/**
	 *子类可以重载此方法，因为有的网页含有按钮，单纯的直观的链接也许并不够，通过按钮点击后还有其他的链接
	 * 在这里，由你完全决定是否要将相关链接放到队列里面
	 * @param html
	 * @param url
	 * @param que
	 */
	protected void allHrefEnQueue(String html , String url, BlockedUniQueue que){
	    if(!que.isBeyondLimit()) {
            if (que.isBeyondLimit() || StringUtil.isBlank(html)) {
                return;
            }
            Matcher m = HREF.matcher(html);
            while (m.find()) {
                String href = m.group(1);
                if (href == null || href.isEmpty()) {
                    continue;
                } else {
                    String href1=relative2TrUri(href, url);
						if(logger.isDebugEnabled())
							logger.info("url入队：{}",relative2TrUri(href, url));
                        que.enQueue(href1);
                }
            }
        }
	}

	/**
	 * 有的网站可能会从http变为https
	 * @param url
	 * @return
	 */
	protected String switchHttpOrHttps(String url){
		if(url.startsWith("https"))
			return url.replace("https","http");
		else
			return url.replace("http","https");
	}
	public Pattern getHref(){
		return HREF;
	}
	public List<String> getAllHref(String html){
		return RegexUtil.getAllMatcher(html,1,HREF);
	}
}
