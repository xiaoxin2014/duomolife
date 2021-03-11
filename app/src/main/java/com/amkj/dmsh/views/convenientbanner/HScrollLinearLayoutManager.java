package com.amkj.dmsh.views.convenientbanner;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/5/15
 * version 1.0
 * class description:自定义滑动界面
 */
public class HScrollLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public HScrollLinearLayoutManager(Context context) {
        super(context);
    }

    public HScrollLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public HScrollLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnabled && super.canScrollHorizontally();
    }
}
