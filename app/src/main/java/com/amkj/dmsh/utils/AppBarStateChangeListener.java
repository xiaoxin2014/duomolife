package com.amkj.dmsh.utils;

import android.support.design.widget.AppBarLayout;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/2
 * class description:AppBar滚动监听
 */

public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private State mCurrentState = State.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int scrollY) {
        if (scrollY == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED, scrollY);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(scrollY) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED, scrollY);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            onStateChanged(appBarLayout, State.IDLE, Math.abs(scrollY));
//            if (mCurrentState != State.IDLE) {
//            }
            mCurrentState = State.IDLE;
        }
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, State state, int scrollY);
}
