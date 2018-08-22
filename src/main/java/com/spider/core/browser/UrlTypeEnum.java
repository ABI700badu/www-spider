package com.spider.core.browser;

public enum UrlTypeEnum {
    HTTP("http://","","http"),HTTPS("https://","","https"),ABSO("//","http:","http"),WWW("www.","http://","http"),RELATIVE;
    //绝对路径url本身的前缀,也就是判断该链接是不是绝对路径的标准
    private String absoUrlPrefixFlag =null;
    //即便是绝对路径，也需要添加一些前缀，这个就是需要添加的前缀
    private String addedPrefix ="";
    //url所用到的协议
    private String protocol=null;
    UrlTypeEnum(String prefix){
        absoUrlPrefixFlag =prefix;
    }
    UrlTypeEnum(String prefix,String trPrefix,String protocol){
        absoUrlPrefixFlag =prefix;
        this.addedPrefix =trPrefix;
        this.protocol=protocol;
    }
    UrlTypeEnum(){};
    public static UrlTypeEnum fromKey(String url){
            url = url.toLowerCase();
            for (UrlTypeEnum ut : UrlTypeEnum.values()) {
                if (ut.getAbsoUrlPrefixFlag() != null && url.startsWith(ut.getAbsoUrlPrefixFlag())) {
                    return ut;
                }
            }
        return RELATIVE;
    }
    public String getAbsoUrlPrefixFlag(){
        return absoUrlPrefixFlag;
    }

    public String getAddedPrefix() {
        return addedPrefix;
    }
    public String getProtocol(){
        return protocol;
    }
}
