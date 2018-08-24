package com.amkj.dmsh.mine.bean;


import com.contrarywind.interfaces.IPickerViewData;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/1/3
 * version 3.0.6
 * class description:时间选择器 事项
 */

public class TimeSexOptionsBean implements IPickerViewData {
    private String sexText;
    private int sexCode;

    public TimeSexOptionsBean(int sexCode, String sexText) {
        this.sexCode = sexCode;
        this.sexText = sexText;
    }

    public void setSexText(String sexText) {
        this.sexText = sexText;
    }

    public int getSexCode() {
        return sexCode;
    }

    public void setSexCode(int sexCode) {
        this.sexCode = sexCode;
    }

    @Override
    public String getPickerViewText() {
        return sexText;
    }
}
