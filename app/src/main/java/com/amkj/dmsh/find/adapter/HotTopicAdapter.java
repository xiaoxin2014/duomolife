package com.amkj.dmsh.find.adapter;

import android.app.Activity;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.find.bean.HotTopicEntity.HotTopicBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.skipTopicDetail;


/**
 * Created by xiaoxin on 2019/7/9
 * Version:v4.1.0
 * ClassDescription :发现-热门话题
 */

public class HotTopicAdapter extends BaseQuickAdapter<HotTopicBean, BaseViewHolder> {
    private final Activity context;
    private boolean isHot;

    public HotTopicAdapter(Activity context, List<HotTopicBean> hotTopicList, boolean isHot) {
        super(R.layout.adapter_find_hot_topic, hotTopicList);
        this.context = context;
        this.isHot = isHot;
    }

    @Override
    protected void convert(BaseViewHolder helper, HotTopicBean hotTopicBean) {
        String topicName = getStrings(hotTopicBean.getTitle());
        String end = "";
        if (isHot) {
            end = getIntegralFormat(context, R.string.participant_num, hotTopicBean.getParticipantNum());
        } else {
            end = getIntegralFormat(context, R.string.picked_num, hotTopicBean.getPickedCount());

        }
        String topicText = topicName + "   " + end;
        CharSequence spannableTopicText = ConstantMethod.getSpannableString(topicText, topicText.indexOf(end), topicText.length(), 0.73f, "#999999");
        helper.setText(R.id.tv_find_hot_topic_name, spannableTopicText)
                .setVisible(R.id.tv_get_integral, hotTopicBean.getScore() > 0);

        //跳转话题详情
        helper.itemView.setOnClickListener(v -> {
            skipTopicDetail(context, String.valueOf(hotTopicBean.getId()));
        });
    }
}
