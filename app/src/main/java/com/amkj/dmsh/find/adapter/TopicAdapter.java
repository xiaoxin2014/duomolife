package com.amkj.dmsh.find.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.amkj.dmsh.R;
import com.amkj.dmsh.find.bean.HotTopicEntity.HotTopicBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.skipTopicDetail;

/**
 * Created by xiaoxin on 2019/7/16
 * Version:v4.1.0
 * ClassDescription :话题—分类-话题列表适配器
 */
public class TopicAdapter extends BaseQuickAdapter<HotTopicBean, BaseViewHolder> {
    private Activity activity;

    public TopicAdapter(Activity activity, @Nullable List<HotTopicBean> data) {
        super(R.layout.item_topic, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, HotTopicBean item) {
        if (item == null) return;
        helper.setText(R.id.tv_topic_name, getStrings(item.getTitle()));
        helper.setText(R.id.tv_join_num, String.valueOf(item.getParticipantNum()) + "人参与");
        helper.itemView.setOnClickListener(v -> {
            skipTopicDetail(activity, String.valueOf(item.getId()));
        });
    }
}
