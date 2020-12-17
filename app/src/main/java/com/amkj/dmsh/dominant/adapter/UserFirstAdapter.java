package com.amkj.dmsh.dominant.adapter;

import android.content.Context;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.amkj.dmsh.time.bean.UserFirstEntity.UserFirstBean;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;

/**
 * Created by xiaoxin on 2020/12/15
 * Version:v4.8.2
 * ClassDescription :新人首单0元购商品适配器
 */
public class UserFirstAdapter extends BaseQuickAdapter<UserFirstBean, BaseViewHolder> {

    private Context mContext;

    public UserFirstAdapter(Context context, @Nullable List<UserFirstBean> data) {
        super(R.layout.item_user_first_product, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserFirstBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadCenterCrop(mContext, helper.getView(R.id.iv_cover), item.getPicUrl());
        helper.setText(R.id.tv_name, item.getProductName())
                .setText(R.id.tv_start_amount, ConstantMethod.getStringsFormat(mContext, R.string.uerr_first_start_amount, item.getStartAmount()))
                .setText(R.id.tv_price, getSpannableString("新人专享价¥0", 6, 7, 1.45f, ""));
    }
}
