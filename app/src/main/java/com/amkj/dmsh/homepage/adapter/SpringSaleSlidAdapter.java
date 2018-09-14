package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.dominant.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.homepage.bean.ShowTimeNewHoriHelperBean;
import com.amkj.dmsh.homepage.bean.TimeForeShowEntity.SpringSaleBean;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/5/3
 * class description:请输入类描述
 */

public class SpringSaleSlidAdapter extends BaseQuickAdapter<ShowTimeNewHoriHelperBean, SpringSaleSlidAdapter.SlidHoriViewHolder> {
    private final Context context;
    private ArrayList<SpringSaleBean> springSaleBeanList = new ArrayList<>();
    public static boolean isRefresh = true;

    public SpringSaleSlidAdapter(Context context, List<ShowTimeNewHoriHelperBean> saleTimeHoriProductList) {
        super(R.layout.adapter_layout_spring_slid, saleTimeHoriProductList);
        this.context = context;
    }

    @Override
    protected void convert(final SlidHoriViewHolder helper, final ShowTimeNewHoriHelperBean showTimeNewHoriHelperBean) {
        springSaleBeanList = new ArrayList<>();
        springSaleBeanList.addAll(showTimeNewHoriHelperBean.getShowTimeGoods());
        final SpringSaleHorizonAdapter springSaleHoriAdapter = (SpringSaleHorizonAdapter) helper.communal_recycler_wrap.getAdapter();
        springSaleHoriAdapter.setNewData(springSaleBeanList);
        helper.setText(R.id.tv_spring_time_shaft, showTimeNewHoriHelperBean.getTimeNumber() + "点上新");
        springSaleHoriAdapter.setOnItemClickListener((adapter, view, position) -> {
            SpringSaleBean springSaleBean = (SpringSaleBean) view.getTag();
            if (springSaleBean != null) {
                Intent intent = new Intent();
                intent.setClass(context, ShopTimeScrollDetailsActivity.class);
                intent.putExtra("productId", String.valueOf(springSaleBean.getId()));
                context.startActivity(intent);
            }
        });
        springSaleHoriAdapter.setEnableLoadMore(showTimeNewHoriHelperBean.isLoadMore());
        springSaleHoriAdapter.setOnLoadMoreListener(() -> {
            if (showTimeNewHoriHelperBean.isLoadMore()
                    && springSaleHoriAdapter.getItemCount() >= showTimeNewHoriHelperBean.getPageIndex()
                    * ConstantVariable.TOTAL_COUNT_TWENTY && isRefresh) {
                springSaleHoriAdapter.loadMoreComplete();
                EventBus.getDefault().post(new EventMessage("loadMore", showTimeNewHoriHelperBean));
                isRefresh = false;
            } else {
                springSaleHoriAdapter.loadMoreComplete();
                springSaleHoriAdapter.setEnableLoadMore(false);
            }
        }, helper.communal_recycler_wrap);
    }

    public class SlidHoriViewHolder extends BaseViewHolderHelper {
        public RecyclerView communal_recycler_wrap;
        private SpringSaleHorizonAdapter springSaleHoriAdapter;

        public SlidHoriViewHolder(View view) {
            super(view);
            communal_recycler_wrap = (RecyclerView) view.findViewById(R.id.communal_recycler_wrap);
            communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            springSaleHoriAdapter = new SpringSaleHorizonAdapter(context, springSaleBeanList);
            communal_recycler_wrap.setAdapter(springSaleHoriAdapter);
        }
    }
}
