package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dominant.activity.QualityNewUserActivity;
import com.amkj.dmsh.time.bean.UserFirstEntity.UserFirstBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by xiaoxin on 2020/12/15
 * Version:v4.9.4
 * ClassDescription :首页-新人首单0元购商品适配器
 */
public class HomeUserFirstAdapter extends BaseQuickAdapter<UserFirstBean, BaseViewHolder> {

    private Context mContext;

    public HomeUserFirstAdapter(Context context, @Nullable List<UserFirstBean> data) {
        super(R.layout.item_home_user_first_product, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserFirstBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadCenterCrop(mContext, helper.getView(R.id.iv_cover), item.getPicUrl());
        TextView view = helper.getView(R.id.tv_market_price);
        view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //删除线
        helper.setText(R.id.tv_market_price, ConstantMethod.getStringsChNPrice(mContext, item.getMarketPrice()));
        helper.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, QualityNewUserActivity.class);
            mContext.startActivity(intent);
        });
    }
}
