import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.spider.core.browser.Browser;
import com.spider.core.engine.BlockedUniQueue;
import com.spider.core.engine.WebSpider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijb on 2018/8/16.
 */
public class Blog extends WebSpider{
    public Blog(int maxSize, int numOfThread) {
        super(maxSize, numOfThread);
    }

    @Override
    protected String toGetHtmlPage(String url, String charset) throws IOException {
        return null;
    }

    @Override
    protected void allHrefEnQueue(String html, String url, BlockedUniQueue que) {
        try {
            HtmlPage page=getPage(url);
            List<DomElement> domElementList=getClassSelector("li","ant-pagination-item",page);
            for(DomElement ele:domElementList) {
                page=ele.click();
                System.out.println(ele.asText());
                html = page.asXml();
                List<String> hrefs = getAllHref(html);
                for (String href : hrefs) {
                    String trHref = Browser.relative2TrUri(href, url);
                    if (trHref.startsWith("https://www.cnblogs.com/news/")) {
                        que.enQueue(trHref);
                    }
                }
            }
        } catch (IOException e) {

        }


    }
    private List<DomElement> getClassSelector(String tagName,String classSelector,HtmlPage page){
        List<DomElement> list=new ArrayList<>();
        DomNodeList<DomElement>listEle= page.getElementsByTagName(tagName);
        for(DomElement de:listEle){
            if(de.getAttribute("class").contains(classSelector)){
                list.add(de);
            }
        }
        return list;
    }
}
