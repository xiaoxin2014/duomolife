package com.amkj.dmsh.find.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.homepage.bean.ScoreGoodsEntity.ScoreGoodsBean;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;

/**
 * Created by xiaoxin on 2019/7/17
 * Version:v4.1.0
 * ClassDescription :发现-评分商品适配器
 */
public class ScoreGoodsAdapter extends BaseQuickAdapter<ScoreGoodsBean, BaseViewHolder> {

    private final Activity activity;
    private String rewardReminder;

    public ScoreGoodsAdapter(Activity activity, @Nullable List<ScoreGoodsBean> data) {
        super(R.layout.item_score_goods, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, ScoreGoodsBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadImage(activity, helper.getView(R.id.iv_cover), item.getCover());
        helper.setText(R.id.tv_goods_name, getStrings(item.getProductName()))
                .setText(R.id.tv_max_reward, getStrings(rewardReminder))
                .setGone(R.id.tv_max_reward, !TextUtils.isEmpty(rewardReminder))
                .addOnClickListener(R.id.ll_write).setTag(R.id.ll_write, item);

        //跳转商品详情
        helper.itemView.setOnClickListener(v -> {
            if (isContextExisted(activity)) {
                Intent intent = new Intent(activity, ShopScrollDetailsActivity.class);
                intent.putExtra("productId", item.getProductId());
                activity.startActivity(intent);
            }
        });
    }

    public void setRewardReminder(String rewardReminder) {
        this.rewardReminder = rewardReminder;
    }
}
