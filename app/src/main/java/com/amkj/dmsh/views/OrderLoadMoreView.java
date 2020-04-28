package com.amkj.dmsh.views;

import android.content.Intent;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.activity.OrderSearchHelpActivity;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;

/**
 * Created by xiaoxin on 2020/4/17
 * Version:v4.5.0
 * ClassDescription :订单列表自定义loadMoreView
 */
public class OrderLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.brvah_quick_view_order_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_end_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }

    @Override
    public void setLoadMoreStatus(int loadMoreStatus) {
        super.setLoadMoreStatus(loadMoreStatus);
    }

    @Override
    public void convert(BaseViewHolder holder) {
        holder.itemView.setOnClickListener(v -> {
            if (getLoadMoreStatus() != LoadMoreView.STATUS_LOADING) {
                Intent intent = new Intent(mAppContext, OrderSearchHelpActivity.class);
                mAppContext.startActivity(intent);
            }
        });
        switch (getLoadMoreStatus()) {
            case STATUS_LOADING:
                holder.setGone(getLoadingViewId(), true);
                holder.setGone(getLoadEndViewId(), false);
                break;
            case STATUS_DEFAULT:
                holder.setGone(getLoadingViewId(), false);
                holder.setGone(getLoadEndViewId(), false);
                break;
            default:
                holder.setGone(getLoadingViewId(), false);
                holder.setGone(getLoadEndViewId(), true);
                break;
        }
    }
}

