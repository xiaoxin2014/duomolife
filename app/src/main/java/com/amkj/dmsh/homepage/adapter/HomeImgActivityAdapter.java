package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/23
 * class description:图片广告
 */

public class HomeImgActivityAdapter extends BaseQuickAdapter<CommunalADActivityBean, BaseViewHolderHelper> {
    private final Context context;

    public HomeImgActivityAdapter(Context context, List<CommunalADActivityBean> activityBeanList) {
        super(R.layout.adapter_home_img_activity, activityBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, CommunalADActivityBean communalADActivityBean) {
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_home_img_activity), communalADActivityBean.getPicUrl());
        helper.itemView.setTag(communalADActivityBean);
    }
}
