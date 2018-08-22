package com.spider.core.browser;

import com.spider.core.net.HttpClientUtil;
import com.spider.core.util.FileUtil;
import org.w3c.dom.Document;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.List;

/**
 * Created by lijb on 2018/8/16.
 * //todo 思考有没有写的必要
 * 本身执行js不复杂，主要是需要写自己的window对象，这是一个耗时的过程
 * 直接内置浏览器似乎是一个不错的选择
 */
public class JsExcuteEngine implements IJsExcuteEngine{
    public static void main(String[] args) {
        String html="<html><head><script>function test(){}<script></head></html>";
        JsExcuteEngine jsExcuteEngine=new JsExcuteEngine();
        List<String> jsSrcs=jsExcuteEngine.findExternJs(html);
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByName("JavaScript");
        //加载js
        for(String js:jsSrcs){
            try {
                String jsCode=HttpClientUtil.toDoGetUrlConnectStr(js);
                se.eval(js);
            } catch (IOException e) {
                //e.printStackTrace();
            } catch (ScriptException e) {
                //e.printStackTrace();
            }
        }
        Invocable inv = (Invocable) se;
        se.put("document", new Object());//比如加载你自己定义的document对象，window对象等
        try {
            String value = (String) inv.invokeFunction("function");//加载js
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String findAllJs(String html,String url) {
        return null;
    }

    @Override
    public List<String> findExternJs(String href) {
        return null;
    }

    @Override
    public String excuteMethod(String methodName, Object doc) {
        return null;
    }

    @Override
    public List<String> findAllMethods(String js) {
        return null;
    }
}
