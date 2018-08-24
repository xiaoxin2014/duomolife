package com.amkj.dmsh.views.bottomdialog;

/**
 * @id 属性Id
 * @value 属性值
 */
public class ProductParameterValueBean {
    private String valueName;
    private int propId;
    private String propValueUrl;
    private int parentId;
    private String parentTypeName;
    //    库存是否为空
    private boolean isNull = true;
    //    是否可设置提醒
    private int notice;
    //    是否被选中
    private boolean isSelected;

    public String getParentTypeName() {
        return parentTypeName;
    }

    public void setParentTypeName(String parentTypeName) {
        this.parentTypeName = parentTypeName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean aNull) {
        isNull = aNull;
    }

    public int getNotice() {
        return notice;
    }

    public void setNotice(int notice) {
        this.notice = notice;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getPropValueUrl() {
        return propValueUrl;
    }

    public void setPropValueUrl(String propValueUrl) {
        this.propValueUrl = propValueUrl;
    }

    public int getPropId() {
        return propId;
    }

    public void setPropId(int propId) {
        this.propId = propId;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }
}
