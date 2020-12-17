package com.amkj.dmsh.time.adapter;

import android.content.Context;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.time.bean.TimeAxisEntity.TimeAxisBean;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by xiaoxin on 2020/12/10
 * Version:v4.8.0
 */
public class TimeAxisAdapter extends BaseQuickAdapter<TimeAxisBean, BaseViewHolder> {


    private final Context mContext;

    public TimeAxisAdapter(Context context, @Nullable List<TimeAxisBean> data) {
        super(R.layout.item_time_axis, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, TimeAxisBean item) {
        RecyclerView mRvSingle = helper.getView(R.id.rv_single);
        RecyclerView mRvBrand = helper.getView(R.id.rv_brand);
        List<TimeAxisBean.BrandProductBean> productList = item.getProductList();
        List<TimeAxisBean.BrandBean> topicList = item.getTopicList();
        if (productList != null && productList.size() > 0) {
            mRvSingle.setVisibility(View.VISIBLE);
            ItemDecoration newGridItemDecoration = new ItemDecoration.Builder()
                    .setDividerId(R.drawable.item_divider_five_gray_f)
                    .create();
            //初始化单品列表
            mRvSingle.setLayoutManager(new GridLayoutManager(mContext, 2));
            if (mRvSingle.getItemDecorationCount() == 0) {
                mRvSingle.addItemDecoration(newGridItemDecoration);
            }
            SingleProductAdapter singleProductAdapter = new SingleProductAdapter(mContext, productList);
            mRvSingle.setAdapter(singleProductAdapter);
        } else {
            mRvSingle.setVisibility(View.GONE);
        }

        if (topicList != null && topicList.size() > 0) {
            //初始化品牌团列表
            mRvBrand.setVisibility(View.VISIBLE);
            mRvBrand.setLayoutManager(new LinearLayoutManager(mContext));
            BrandAdapter brandAdapter = new BrandAdapter(mContext, topicList);
            mRvBrand.setAdapter(brandAdapter);
        } else {
            mRvBrand.setVisibility(View.GONE);
        }
    }
}
