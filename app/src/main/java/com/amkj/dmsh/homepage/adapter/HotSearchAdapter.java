package com.amkj.dmsh.homepage.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.HotSearchTagEntity.HotSearchTagBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/10/24
 * version 3.1.8
 * class description:热门搜索标签
 */
public class HotSearchAdapter extends BaseQuickAdapter<HotSearchTagBean,BaseViewHolder>{
    public HotSearchAdapter(List<HotSearchTagBean> hotSearchTagBeans) {
        super(R.layout.layoyt_hotsearch_tag, hotSearchTagBeans);
    }

    @Override
    protected void convert(BaseViewHolder helper, HotSearchTagBean hotSearchTagBean) {
        TextView textView = helper.getView(R.id.tv_search_tag);
        if (!TextUtils.isEmpty(hotSearchTagBean.getAndroid_link())) {
            textView.setSelected(true);
        } else {
            textView.setSelected(false);
        }
        textView.setText(getStrings(hotSearchTagBean.getTag_name()));
        helper.itemView.setTag(hotSearchTagBean);
    }
}
