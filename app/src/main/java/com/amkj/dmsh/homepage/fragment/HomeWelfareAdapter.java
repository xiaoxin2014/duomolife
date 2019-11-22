package com.amkj.dmsh.homepage.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.CommonPagerAdapter;
import com.amkj.dmsh.base.ViewHolder;
import com.amkj.dmsh.bean.HomeWelfareEntity.HomeWelfareBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dominant.activity.DoMoLifeWelfareDetailsActivity;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.utils.ProductLabelCreateUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.dao.AddClickDao.totalWelfareProNum;

/**
 * Created by xiaoxin on 2019/4/14 0014
 * Version:v4.0.0
 * ClassDescription :首页福利社（精选专题）适配器
 */
public class HomeWelfareAdapter extends CommonPagerAdapter<HomeWelfareBean> {

    private final Activity mContext;

    public HomeWelfareAdapter(Activity context, List<HomeWelfareBean> datas) {
        super(context, datas, R.layout.item_home_walfare);
        mContext = context;
    }

    @Override
    public void convert(ViewHolder helper, int position, HomeWelfareBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadRoundImg(mContext, helper.getView(R.id.iv_welfare_cover), item.getPicUrl(), AutoSizeUtils.mm2px(mAppContext, 10));
        helper.getView(R.id.iv_welfare_cover).setOnClickListener(view -> {
            Intent intent = new Intent(mContext, DoMoLifeWelfareDetailsActivity.class);
            intent.putExtra("welfareId", String.valueOf(item.getId()));
            mContext.startActivity(intent);
        });
        RecyclerView rvTopicGoods = helper.getView(R.id.rv_topic_goods);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        rvTopicGoods.setLayoutManager(gridLayoutManager);
        if (rvTopicGoods.getTag() == null) {
            ItemDecoration itemDecoration = new ItemDecoration.Builder()
                    // 设置分隔线资源ID
                    .setDividerId(R.drawable.item_divider_five_dp)
                    .create();
            rvTopicGoods.addItemDecoration(itemDecoration);
            rvTopicGoods.setTag(item);
        }
        List<HomeWelfareBean.GoodsBean> goods = item.getGoods();
        List<HomeWelfareBean.GoodsBean> newGoods = new ArrayList<>();
        //最多显示三个
        if (goods != null) {
            for (int i = 0; i < (goods.size() > 3 ? 3 : goods.size()); i++) {
                newGoods.add(goods.get(i));
            }
        }

        //初始化福利社商品适配器
        BaseQuickAdapter topicGoodsAdapter = new BaseQuickAdapter<HomeWelfareBean.GoodsBean, BaseViewHolder>(R.layout.item_welfear_goods, newGoods) {
            @Override
            protected void convert(BaseViewHolder helper, HomeWelfareBean.GoodsBean goodsBean) {
                if (goodsBean == null) return;
                GlideImageLoaderUtil.loadImage(mContext, helper.getView(R.id.iv_goods_pic), goodsBean.getPicUrl());
                helper.setText(R.id.tv_price, ConstantMethod.getRmbFormat(mContext, goodsBean.getPrice()));
                FlexboxLayout fbl_label = helper.getView(R.id.fbl_market_label);
                if (!TextUtils.isEmpty(goodsBean.getActivityTag()) || (goodsBean.getMarketLabelList() != null
                        && goodsBean.getMarketLabelList().size() > 0)) {
                    fbl_label.setVisibility(View.VISIBLE);
                    fbl_label.removeAllViews();
                    if (!TextUtils.isEmpty(goodsBean.getActivityTag())) {
                        fbl_label.addView(ProductLabelCreateUtils.createLabelText(mContext, goodsBean.getActivityTag(), 1));
                    }
                    if (goodsBean.getMarketLabelList() != null
                            && goodsBean.getMarketLabelList().size() > 0) {
                        for (UserLikedProductEntity.LikedProductBean.MarketLabelBean marketLabelBean : goodsBean.getMarketLabelList()) {
                            if (!TextUtils.isEmpty(marketLabelBean.getTitle())) {
                                fbl_label.addView(ProductLabelCreateUtils.createLabelText(mContext, marketLabelBean.getTitle(), 0));
                            }
                        }
                    }
                } else {
                    fbl_label.setVisibility(View.GONE);
                }

                helper.itemView.setTag(goodsBean);
            }
        };
        topicGoodsAdapter.setOnItemClickListener((adapter, view, position1) -> {
            HomeWelfareBean.GoodsBean goodsBean = (HomeWelfareBean.GoodsBean) view.getTag();
            if (goodsBean != null) {
                Intent intent = new Intent(mContext, ShopScrollDetailsActivity.class);
                intent.putExtra("productId", String.valueOf(goodsBean.getId()));
                mContext.startActivity(intent);
                //记录埋点参数sourceId(福利社专题对应的ID)
                ConstantMethod.saveSourceId(HomeDefalutFragment.class.getSimpleName(), String.valueOf(item.getId()));
                //统计福利社点击商品
              totalWelfareProNum(mContext, goodsBean.getId(), item.getId());
            }
        });
        rvTopicGoods.setAdapter(topicGoodsAdapter);
        helper.itemView.setTag(item);
    }
}
