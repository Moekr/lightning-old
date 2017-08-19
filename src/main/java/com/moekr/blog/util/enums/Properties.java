package com.moekr.blog.util.enums;

public enum Properties {
    LOGO("logo", "网站LOGO", "Moekr"),
    NAME("name", "网站名称", "萌客"),
    ABOUT("about", "关于页面", "这里什么也没有~"),
    EOF("eof", "文章结束标志", "--End Of File--"),
    COPYRIGHT("copyright", "文章版权声明", "未经许可禁止转载本文内容，转载请注明文章出处。"),
    FOOTER("footer", "页脚内容", "Powered by SpringFramework"),
    EMAIL("email", "站长邮箱", "moekr@domain.com");

    private String id;
    private String name;
    private String value;

    Properties(String id, String name, String value){
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
