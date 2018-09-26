package com.amkj.dmsh.release.adapter;

import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.find.bean.FindRelTopicEntity.FindRelTopicBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/11/27
 * class description:发现-关联话题
 */

public class FindRelTopicAdapter extends BaseQuickAdapter<FindRelTopicBean, BaseViewHolder> {
    public FindRelTopicAdapter(List<FindRelTopicBean> hotTopicBeanList) {
        super(R.layout.adapter_layout_find_rel_topic, hotTopicBeanList);
    }

    @Override
    protected void convert(BaseViewHolder helper, FindRelTopicBean findRelTopicBean) {
        TextView tv_find_rel_topic_title = helper.getView(R.id.tv_find_rel_topic_title);
        tv_find_rel_topic_title.setText(getStrings(findRelTopicBean.getTitle()));
        tv_find_rel_topic_title.setSelected(findRelTopicBean.isSelect());
        tv_find_rel_topic_title.setTag(findRelTopicBean);
        helper.itemView.setTag(tv_find_rel_topic_title);
    }
}
