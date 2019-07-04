package com.amkj.dmsh.mine.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.mine.bean.ShopCarEntity.ShopCartBean.CartBean.CartInfoBean;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by xiaoxin on 2019/7/1
 * Version:v4.1.0
 * ClassDescription :购物车商品活动组信息
 */
public class ActivityInfoBean extends AbstractExpandableItem<CartInfoBean> implements Parcelable, MultiItemEntity {
    /**
     * activityCode : LJ1507778006
     * limitBuy : 2
     * activityTag : 立减震撼来
     * activityType : 2
     * activityRule : 立减20.0元
     */

    private String activityCode;
    private int limitBuy;
    private String activityTag;
    private int activityType;
    private String activityRule;
    private String preActivityRule;

    public String getPreActivityRule() {
        return preActivityRule;
    }

    public void setPreActivityRule(String preActivityRule) {
        this.preActivityRule = preActivityRule;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public int getLimitBuy() {
        return limitBuy;
    }

    public void setLimitBuy(int limitBuy) {
        this.limitBuy = limitBuy;
    }

    public String getActivityTag() {
        return activityTag;
    }

    public void setActivityTag(String activityTag) {
        this.activityTag = activityTag;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public String getActivityRule() {
        return activityRule;
    }

    public void setActivityRule(String activityRule) {
        this.activityRule = activityRule;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.activityCode);
        dest.writeInt(this.limitBuy);
        dest.writeString(this.activityTag);
        dest.writeInt(this.activityType);
        dest.writeString(this.activityRule);
        dest.writeString(this.preActivityRule);
    }

    public ActivityInfoBean() {
    }

    protected ActivityInfoBean(Parcel in) {
        this.activityCode = in.readString();
        this.limitBuy = in.readInt();
        this.activityTag = in.readString();
        this.activityType = in.readInt();
        this.activityRule = in.readString();
        this.preActivityRule = in.readString();
    }

    public static final Parcelable.Creator<ActivityInfoBean> CREATOR = new Parcelable.Creator<ActivityInfoBean>() {
        @Override
        public ActivityInfoBean createFromParcel(Parcel source) {
            return new ActivityInfoBean(source);
        }

        @Override
        public ActivityInfoBean[] newArray(int size) {
            return new ActivityInfoBean[size];
        }
    };

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public int getItemType() {
        return ConstantVariable.TITLE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActivityInfoBean that = (ActivityInfoBean) o;
        if (!TextUtils.isEmpty(activityCode)) {
            return activityCode.equals(that.getActivityCode());
        } else {
            return TextUtils.isEmpty(that.getActivityCode());
        }
    }

    @Override
    public int hashCode() {
        return activityCode.hashCode();
    }
}
