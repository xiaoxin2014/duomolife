package com.amkj.dmsh.find.adapter;

import androidx.annotation.Nullable;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.find.bean.TopicCaterGoryEntity.TopicCaterGoryBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2019/7/16
 * Version:v4.1.0
 * ClassDescription :话题分类-分类名称列表适配器
 */
public class TopicCatergoryAdapter extends BaseQuickAdapter<TopicCaterGoryBean, BaseViewHolder> {
    private int catergoryId;//选中的分类id

    public TopicCatergoryAdapter(@Nullable List<TopicCaterGoryBean> data) {
        super(R.layout.item_topic_catergory, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TopicCaterGoryBean item) {
        if (item == null) return;
        TextView tvCatergory = helper.getView(R.id.tv_catergory_name);
        tvCatergory.setText(getStrings(item.getTitle()));
        //不可重复点击
        tvCatergory.setEnabled(item.getId() != catergoryId);
        tvCatergory.setSelected(item.getId() == catergoryId);
        helper.itemView.setTag(item);
    }

    public int getCatergoryId() {
        return catergoryId;
    }

    public void setCatergoryId(int catergoryId) {
        this.catergoryId = catergoryId;
    }
}
