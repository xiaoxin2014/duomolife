package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.homepage.bean.TopicSpecialEntity.TopicSpecialBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/31
 * class description:请输入类描述
 */

public class SpecialTopicAdapter extends BaseQuickAdapter<TopicSpecialBean, BaseViewHolderHelper> {
    private final Context context;

    public SpecialTopicAdapter(Context context, List<TopicSpecialBean> specialSearList) {
        super(R.layout.adapter_special_topic, specialSearList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, TopicSpecialBean topicSpecialBean) {
        TextView tv_sp_label = helper.getView(R.id.tv_sp_label);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(AutoUtils.getPercentWidth1px() * 8);
        gradientDrawable.setColor(0x7f000000);
        tv_sp_label.setBackground(gradientDrawable);
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_communal_cover_padding)
                , topicSpecialBean.getPic_url());
        helper.setText(R.id.tv_sp_label, getStrings(topicSpecialBean.getFlag()))
                .setText(R.id.tv_search_sp_title, getStrings(topicSpecialBean.getTitle()))
                .setText(R.id.tv_search_sp_descrip, getStrings(topicSpecialBean.getDescription()));
        helper.itemView.setTag(topicSpecialBean);
    }
}
