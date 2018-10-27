package com.amkj.dmsh.mine.adapter;

import com.amkj.dmsh.R;
import com.amkj.dmsh.mine.bean.SuggestionTypeEntity.FeedBackTypeBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/10/25
 * version 3.1.8
 * class description:意见反馈类型
 */
public class SuggestionFeedBackTypeAdapter extends BaseQuickAdapter<FeedBackTypeBean, BaseViewHolder> {
    public SuggestionFeedBackTypeAdapter(List<FeedBackTypeBean> data) {
        super(R.layout.adapter_layout_communal_only_text, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FeedBackTypeBean feedBackTypeBean) {
        helper.setText(R.id.tv_communal_only_text, getStrings(feedBackTypeBean.getTitle()))
                .itemView.setTag(feedBackTypeBean);
    }
}
