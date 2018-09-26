package com.amkj.dmsh.find.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.find.bean.FindHotTopicEntity.FindHotTopicBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/4
 * class description:热门话题
 */

public class FindHotTopicAdapter extends BaseQuickAdapter<FindHotTopicBean, BaseViewHolder> {
    private final Context context;

    public FindHotTopicAdapter(Context context, List<FindHotTopicBean> hotTopicList) {
        super(R.layout.adapter_find_hot_topic, hotTopicList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, FindHotTopicBean hotTopicBean) {
        GlideImageLoaderUtil.loadRoundImg(context, (ImageView) helper.getView(R.id.iv_find_hot_topic)
                , getStrings(hotTopicBean.getImg_url()),4);
        helper.setText(R.id.tv_find_hot_topic_name,
                String.format(context.getResources().getString(R.string.topic_format)
                ,getStrings(hotTopicBean.getTitle())))
                .setText(R.id.tv_find_hot_topic_join, hotTopicBean.getParticipants_number() + "人参与")
                .itemView.setTag(hotTopicBean);
    }
}
