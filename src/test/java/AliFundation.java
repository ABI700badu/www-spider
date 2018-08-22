import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.spider.core.engine.BlockedUniQueue;
import com.spider.core.browser.Browser;
import com.spider.core.engine.WebSpider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AliFundation extends WebSpider {

    public AliFundation(int maxSize, int threadSize){
        super(maxSize,threadSize);
    }


    @Override
    protected void allHrefEnQueue(String html, String url, BlockedUniQueue que) {
        if(url.startsWith("http://www.fund123.cn/fund")) {
            try {
                HtmlPage page = getPage(url);
                for (DomElement de : getClassSelector("li", "ant-pagination-item", page)) {
                    synchronized (this) {
                        page = de.click();
                    }
                    html = page.asXml();
                    System.out.println(de.asText()+">>>>>>>\n"+html);
                    List<String> hrefs = getAllHref(html);
                    for (String href : hrefs) {
                        String trHref = Browser.relative2TrUri(href, url);
                        if (trHref.startsWith("http://www.fund123.cn/matiaria?fundCode")) {
                            System.out.println("new:"+trHref);
                            que.enQueue(trHref);
                        }
                    }
                }
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }
    }
    private List<DomElement> getClassSelector(String tagName,String classSelector,HtmlPage page){
        List<DomElement> list=new ArrayList<>();
        DomNodeList<DomElement> listEle= page.getElementsByTagName(tagName);
        for(DomElement de:listEle){
            if(de.getAttribute("class").contains(classSelector)){
                list.add(de);
            }
        }
        return list;
    }
}
