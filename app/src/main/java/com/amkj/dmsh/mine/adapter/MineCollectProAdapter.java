package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.mine.bean.CollectProEntity.CollectProBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/10
 * class description:收藏商品
 */

public class MineCollectProAdapter extends BaseQuickAdapter<CollectProBean, BaseViewHolder> {
    private final Context context;

    public MineCollectProAdapter(Context context, List<CollectProBean> collectProList) {
        super(R.layout.adapter_mine_collect_pro, collectProList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CollectProBean collectProBean) {
        CheckBox cb_collect_product = helper.getView(R.id.cb_collect_product);
        TextView tv_mine_col_pro_buy = helper.getView(R.id.tv_mine_col_pro_buy);
        if (collectProBean.isEditStatus()) {
            cb_collect_product.setVisibility(View.VISIBLE);
            tv_mine_col_pro_buy.setVisibility(View.GONE);
            cb_collect_product.setChecked(collectProBean.isCheckStatus());
        } else {
            cb_collect_product.setVisibility(View.GONE);
            tv_mine_col_pro_buy.setVisibility(View.VISIBLE);
        }
        GlideImageLoaderUtil.loadHeaderImg(context, (ImageView) helper.getView(R.id.iv_mine_col_pro), collectProBean.getPicUrl());
        helper.setText(R.id.tv_mine_col_pro_name, getStrings(collectProBean.getName()))
                .setText(R.id.tv_mine_col_pro_tag, getStrings(collectProBean.getPriceTag()))
                .setText(R.id.tv_mine_col_pro_price, "￥" + getStrings(collectProBean.getPrice()))
                .setGone(R.id.tv_mine_col_pro_tag, !TextUtils.isEmpty(collectProBean.getPriceTag()))
                .addOnClickListener(R.id.tv_mine_col_pro_buy).setTag(R.id.tv_mine_col_pro_buy, collectProBean)
                .setTag(R.id.cb_collect_product, collectProBean)
                .addOnClickListener(R.id.cb_collect_product)
                .setGone(R.id.tv_collect_pro_sack_tag, !collectProBean.isValid());
        helper.itemView.setTag(collectProBean);
    }
}
