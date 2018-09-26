package com.amkj.dmsh.dominant.adapter;

import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.dominant.bean.QualityHistoryListEntity.QualityHistoryListBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/10/10
 * class description:侧栏历史清单
 */

public class QualityHistoryAdapter extends BaseQuickAdapter<QualityHistoryListBean, BaseViewHolder> {

    public QualityHistoryAdapter(List<QualityHistoryListBean> historyListBeanList) {
        super(R.layout.adapter_quality_history_list, historyListBeanList);
    }

    @Override
    protected void convert(BaseViewHolder helper, QualityHistoryListBean qualityHistoryListBean) {
        helper.setText(R.id.tv_ql_his_title, getStrings(!TextUtils.isEmpty(qualityHistoryListBean.getName())
                ? qualityHistoryListBean.getName() : qualityHistoryListBean.getTitle()));
        helper.itemView.setTag(qualityHistoryListBean);
    }
}
