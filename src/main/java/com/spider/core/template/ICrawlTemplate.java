package com.spider.core.template;

import java.io.Serializable;
/**
 *
 * 
 * @author
 *
 */
public abstract  interface ICrawlTemplate extends Serializable{
	/**
	 * 
	 */
	 final long serialVersionUID = 1L;

	/**
	 *
	 * @param result 该链接对应文档内容
	 * @param url  抓取的链接
	 */
	 void crawlValue(String result, String url);

	/**
	 * To climb the template site
	 * this method is invoked before crawlValue() method is invoked
	 * 当然可以返回 null, 此时引擎会尝试从http协议头部解析该文档编码
	 * @return
	 */
	 String getBaseSite();
	/**
	 * The encoding of the template
	 * this method is invoked before crawlValue() method is invoked
	 * default utf-8
	 * @return
	 */
	 String getCharset();

	/**
	 *对抓取的URL 添加一些约束
	 * 注意这里的URL会占用队列的空间，只不过他不会被网络请求而已
	 * @param url
	 * @return
	 */
	 boolean filterUrl(String url);
}
