package com.amkj.dmsh.homepage.adapter;

import android.content.Context;

import com.amkj.dmsh.R;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/23
 * class description:图片广告
 */

public class HomeImgActivityAdapter extends BaseQuickAdapter<CommunalADActivityBean, BaseViewHolder> {
    private final Context context;

    public HomeImgActivityAdapter(Context context, List<CommunalADActivityBean> activityBeanList) {
        super(R.layout.adapter_home_img_activity, activityBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommunalADActivityBean communalADActivityBean) {
        GlideImageLoaderUtil.loadImage(context, helper.getView(R.id.iv_home_img_activity), communalADActivityBean.getPicUrl());
        helper.itemView.setTag(communalADActivityBean);
    }
}
