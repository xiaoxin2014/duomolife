package com.amkj.dmsh.find.adapter;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.find.bean.RelevanceTagInfoEntity.RelevanceTagInfoBean.TopTagListBean;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/1/24
 * version 3.7
 * class description:标签
 */

public class TagDetailHorizontalAdapter extends BaseQuickAdapter<TopTagListBean, BaseViewHolderHelper> {
    public TagDetailHorizontalAdapter(List<TopTagListBean> topTagList) {
        super(R.layout.adapter_tag_detail, topTagList);
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, TopTagListBean topTagListBean) {
        helper.setText(R.id.tv_tag_detail, getStrings(topTagListBean.getTag_name()))
                .itemView.setTag(topTagListBean);
    }
}
