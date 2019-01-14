package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import com.amkj.dmsh.R;
import com.amkj.dmsh.mine.bean.MineBrowsHistoryEntity.MineBrowsHistoryBean;
import com.amkj.dmsh.mine.bean.MineBrowsHistoryEntity.MineBrowsHistoryBean.GoodsInfoListBean;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/1/10
 * version 3.2.0
 * class description:我的浏览记录
 */
public class MineBrowsingHistoryAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private final Context context;

    public MineBrowsingHistoryAdapter(Context context, List<MultiItemEntity> browsHistoryBeanList) {
        super(browsHistoryBeanList);
        addItemType(TYPE_0, R.layout.adapter_browsing_history_product);
        addItemType(TYPE_1, R.layout.adapter_browsing_history_header);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity multiItemEntity) {
        switch (helper.getItemViewType()) {
            case TYPE_1:
                MineBrowsHistoryBean mineBrowsHistoryBean = (MineBrowsHistoryBean) multiItemEntity;
                CheckBox cb_browse_history_header = helper.getView(R.id.cb_browse_history_header);
                cb_browse_history_header.setVisibility(mineBrowsHistoryBean.isEditStatus()? View.VISIBLE:View.GONE);
                cb_browse_history_header.setChecked(mineBrowsHistoryBean.isSelectStatus());
                helper.setText(R.id.tv_browse_history_header,getStrings(mineBrowsHistoryBean.getTime()))
                        .setTag(R.id.cb_browse_history_header,mineBrowsHistoryBean)
                .addOnClickListener(R.id.cb_browse_history_header);
                break;
            default:
                GoodsInfoListBean goodsInfoListBean = (GoodsInfoListBean)multiItemEntity;
                CheckBox cb_browse_history_product = helper.getView(R.id.cb_browse_history_product);
                cb_browse_history_product.setVisibility(goodsInfoListBean.isEditStatus()? View.VISIBLE:View.GONE);;
                cb_browse_history_product.setChecked(goodsInfoListBean.isSelectStatus());
                helper.setText(R.id.tv_browse_history_product,getStrings(goodsInfoListBean.getTitle()))
                        .setTag(R.id.cb_browse_history_product,goodsInfoListBean)
                        .addOnClickListener(R.id.cb_browse_history_product);
                break;
        }
    }
}
