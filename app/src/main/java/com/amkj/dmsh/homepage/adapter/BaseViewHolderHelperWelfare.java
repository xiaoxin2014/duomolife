package com.amkj.dmsh.homepage.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amkj.dmsh.R;
import com.chad.library.adapter.base.BaseViewHolder;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;

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
        rv_welfare_header_item_horizontal.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)






                .create());
    }
}
