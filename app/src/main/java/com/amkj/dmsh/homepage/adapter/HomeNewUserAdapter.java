package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.amkj.dmsh.R;
import com.amkj.dmsh.homepage.bean.HomeNewUserEntity.HomeNewUserBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by xiaoxin on 2019/4/14 0014
 * Version:v4.0.0
 * ClassDescription :
 */
public class HomeNewUserAdapter extends BaseQuickAdapter<HomeNewUserBean, BaseViewHolder> {

    private final Context mContext;

    public HomeNewUserAdapter(Context context, @Nullable List<HomeNewUserBean> data) {
        super(R.layout.item_home_new_user_goods, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeNewUserBean item) {
        GlideImageLoaderUtil.loadImage(mContext, helper.getView(R.id.iv_goods_pic), item.getPic());
        helper.setText(R.id.tv_goods_price, item.getPrice());
        helper.itemView.setTag(item);
    }
}
