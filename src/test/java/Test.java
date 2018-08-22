import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

public class Test {
    private static  final WebClient WEB_CLIENT=new WebClient();
    public static void main(String[] args) {
        WEB_CLIENT.setAjaxController(new NicelyResynchronizingAjaxController());
        WEB_CLIENT.getOptions().setJavaScriptEnabled(true);
        WEB_CLIENT.getOptions().setCssEnabled(true);
        WEB_CLIENT.getOptions().setTimeout(35000);
        WEB_CLIENT.getOptions().setThrowExceptionOnScriptError(false);
        try {
            HtmlPage page=WEB_CLIENT.getPage("http://www.fund123.cn/matiaria?fundCode=003625");
            System.out.println(page.asXml());
            HtmlPage page2=WEB_CLIENT.getPage("http://www.fund123.cn/matiaria?fundCode=003625");
            System.out.println(page2.asXml());
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
}
