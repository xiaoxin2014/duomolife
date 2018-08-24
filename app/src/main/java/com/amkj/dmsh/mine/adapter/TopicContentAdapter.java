package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.mine.bean.TopicContentEntity.TopicContentBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/12
 * class description:请输入类描述
 */

public class TopicContentAdapter extends BaseQuickAdapter<TopicContentBean, BaseViewHolderHelper> {
    private final Context context;

    public TopicContentAdapter(Context context, List<TopicContentBean> topicContentList) {
        super(R.layout.adapter_topic_content, topicContentList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, TopicContentBean topicContentBean) {
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_topic_content), topicContentBean.getPicUrl());
        TextView tv_topic_content_title = helper.getView(R.id.tv_topic_content_title);
        tv_topic_content_title.setText(getStrings(topicContentBean.getName()));
        helper.setText(R.id.tv_topic_content_join, topicContentBean.getParticipants_number() + "参与");
    }
}
