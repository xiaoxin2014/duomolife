package com.amkj.dmsh.views;


import com.amkj.dmsh.R;
import com.chad.library.adapter.base.loadmore.LoadMoreView;

/**
 * Created by BlingBling on 2016/10/11.
 */

public final class HorizontalLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.brvah_quick_view_horizontal_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
