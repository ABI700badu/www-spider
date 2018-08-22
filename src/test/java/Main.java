import com.spider.core.model.JsonSelectorModel;
import com.spider.core.template.AbstractTemplate;
import com.spider.core.template.ICrawlTemplate;

public class Main {

    public static void main(String[] args) {

        //step1: 创建一个你自己定义的爬虫引擎,第一个参数表示队列的最大容量，第二个参数表示开启线程池中线程数量
        AliFundation aliFundtion=new AliFundation(50,6);
        //step2:定义你自己的站点模板
        ICrawlTemplate temp=new AbstractTemplate() {
            @Override
            public void crawlValue(String result, String url) {
                System.out.println("url:"+url+"<====>"+result);
            }
            @Override
            public String getBaseSite() {
                return "http://www.fund123.cn/fund";
            }
        };
        //step3:采用正则表达式进行数据建模
            //<_%></_%>这是一对表示这个一个正则表达式的标签
                //index 属性 就是该正则表达式需要提取的组
        StringBuilder sb=new StringBuilder();
        sb.append("<_% key=\"基金名称\" selector=\"fundmatiaria-title-fundname\">").append("</_%>").
                append("<_% key=\"基金代码\" selector=\"fundmatiaria-title-fundcode\"></_%>").
                append("<_% key=\"基金净值\" selector=\"fundmatiaria-fundinfo-value\"></_%>");
        JsonSelectorModel model=new JsonSelectorModel(sb.toString());
        model.getOptions().setAbsoJs(true);//表示将所有js路径转化为绝对路径
        model.getOptions().setAbsoImage(true);//表示将所有image路径转化为绝对路径
        model.getOptions().setAbsoCss(true);//表示将所有css路径转化为绝对路径
        aliFundtion.setModel(model);
        //step4:下载模板
        aliFundtion.downLoadArtcle(temp);
    }
}