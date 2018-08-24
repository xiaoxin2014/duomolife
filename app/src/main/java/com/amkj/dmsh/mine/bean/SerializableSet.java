package com.amkj.dmsh.mine.bean;


import java.io.Serializable;
import java.util.Set;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/3
 * class description:请输入类描述
 */

public class SerializableSet implements Serializable {
    private Set<String> tagSet;
    private String alias;

    public Set<String> getTagSet() {
        return tagSet;
    }

    public void setTagSet(Set<String> tagSet) {
        this.tagSet = tagSet;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
