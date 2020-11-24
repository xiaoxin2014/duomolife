package com.amkj.dmsh.base;

/**
 * Created by xiaoxin on 2020/10/23
 * Version:v4.8.0
 */
public class BaseRealNameEntity extends BaseEntity {
    private String realName;
    private String idCard;
    private String showIdCard;
    private String idcardImg1;
    private String idcardImg2;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getShowIdCard() {
        return showIdCard;
    }

    public void setShowIdCard(String showIdCard) {
        this.showIdCard = showIdCard;
    }

    public String getIdcardImg1() {
        return idcardImg1;
    }

    public void setIdcardImg1(String idcardImg1) {
        this.idcardImg1 = idcardImg1;
    }

    public String getIdcardImg2() {
        return idcardImg2;
    }

    public void setIdcardImg2(String idcardImg2) {
        this.idcardImg2 = idcardImg2;
    }
}
