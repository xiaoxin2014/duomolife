package com.amkj.dmsh.find.bean;

/**
 * Created by xiaoxin on 2019/7/26
 * Version:v4.1.0
 */
public class PostTypeBean {
    private String simpleName;
    private String title;

    public PostTypeBean(String simpleName, String title) {
        this.simpleName = simpleName;
        this.title = title;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
