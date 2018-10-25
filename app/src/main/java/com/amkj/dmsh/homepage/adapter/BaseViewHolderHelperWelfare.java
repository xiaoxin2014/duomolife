package com.amkj.dmsh.homepage.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amkj.dmsh.R;
import com.chad.library.adapter.base.BaseViewHolder;
import com.amkj.dmsh.utils.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/3/20
 * class description:请输入类描述
 */

public class BaseViewHolderHelperWelfare extends BaseViewHolder {

    RecyclerView rv_welfare_header_item_horizontal;

    public BaseViewHolderHelperWelfare(View view) {
        super(view);
        rv_welfare_header_item_horizontal = (RecyclerView) view.findViewById(R.id.rv_wrap_bar_none);
        rv_welfare_header_item_horizontal.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_welfare_header_item_horizontal.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
    }
}
