package com.amkj.dmsh.views.bottomdialog;

import java.util.ArrayList;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/5/9
 * class description:sku类型数据 id
 */
public class ProductParameterTypeBean {
    private String typeName;
    private int typeId;
    private ArrayList<ProductParameterValueBean> values;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public ArrayList<ProductParameterValueBean> getValues() {
        return values;
    }

    public void setValues(ArrayList<ProductParameterValueBean> values) {
        this.values = values;
    }
}

