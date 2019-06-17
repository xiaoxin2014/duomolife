package com.amkj.dmsh.homepage.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.HomeWelfareEntity.HomeWelfareBean;
import com.amkj.dmsh.dominant.activity.DoMoLifeWelfareDetailsActivity;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.utils.ProductLabelCreateUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2019/4/18 0018
 * Version:v4.0.0
 * ClassDescription :新版首页福利社Item
 */
public class HomeWlfareItemView extends LinearLayout {
    @BindView(R.id.iv_welfare_cover)
    ImageView mIvWelfareCover;
    @BindView(R.id.tv_topic_name)
    TextView mTvTopicName;
    @BindView(R.id.rv_topic_goods)
    RecyclerView mRvTopicGoods;
    @BindView(R.id.ll_welfare)
    LinearLayout mLlWelfare;
    private int mWidth;
    private Context mContext;

    public HomeWlfareItemView(Context context) {
        super(context);
    }

//    public HomeWlfareItemView(HomeWelfareBean homeWelfareBean, Context context) {
//        this(homeWelfareBean, context, null);
//        mContext = context;
//    }

    public HomeWlfareItemView(HomeWelfareBean homeWelfareBean, Context context, AttributeSet attrs) {
        super(context, attrs);
        View headView = LayoutInflater.from(context).inflate(R.layout.item_home_walfare, this, true);
        ButterKnife.bind(this, headView);
        initView(homeWelfareBean);
    }

    private void initView(HomeWelfareBean homeWelfareBean) {
        if (homeWelfareBean == null) return;
        int screenWidth = ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getScreenWidth();
        mWidth = (screenWidth / 5 * 4);
        GlideImageLoaderUtil.loadRoundImg(mContext, mIvWelfareCover, homeWelfareBean.getPicUrl(), AutoSizeUtils.mm2px(mAppContext, 10));
        mTvTopicName.setText(getStrings(homeWelfareBean.getTitle()));
        mIvWelfareCover.setOnClickListener(view -> {
            if (mContext != null) {
                Intent intent = new Intent(mContext, DoMoLifeWelfareDetailsActivity.class);
                intent.putExtra("welfareId", String.valueOf(homeWelfareBean.getId()));
                mContext.startActivity(intent);
            }
        });
//        helper.setText(R.id.tv_topic_desc, homeWelfareBean.get());
        //动态设置福利社主题Item宽度
//        mLlWelfare.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                mLlWelfare.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                ViewGroup.LayoutParams layoutParams = mLlWelfare.getLayoutParams();
//                layoutParams.width = mWidth;
//                mLlWelfare.setLayoutParams(layoutParams);
//            }
//        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mRvTopicGoods.setLayoutManager(linearLayoutManager);
        List<HomeWelfareBean.GoodsBean> goods = homeWelfareBean.getGoods();
        List<HomeWelfareBean.GoodsBean> newGoods = new ArrayList<>();
        //最多显示三个
        for (int i = 0; i < (goods.size() > 3 ? 3 : goods.size()); i++) {
            newGoods.add(goods.get(i));
        }

        //初始化福利社商品适配器
        BaseQuickAdapter topicGoodsAdapter = new BaseQuickAdapter<HomeWelfareBean.GoodsBean, BaseViewHolder>(R.layout.item_welfear_goods, newGoods) {
            @Override
            protected void convert(BaseViewHolder helper, HomeWelfareBean.GoodsBean goodsBean) {
                GlideImageLoaderUtil.loadImage(mContext, helper.getView(R.id.iv_goods_pic), goodsBean.getPicUrl());
                helper.setText(R.id.tv_price, "¥" + goodsBean.getPrice());
                FlexboxLayout fbl_label = helper.getView(R.id.fbl_label);
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

                View childItemView = helper.itemView;
                //动态设置商品宽度
                childItemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        childItemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        ViewGroup.LayoutParams layoutParams = childItemView.getLayoutParams();
                        layoutParams.width = (mWidth - AutoSizeUtils.mm2px(mContext, 10) * 4) / newGoods.size();
                        childItemView.setLayoutParams(layoutParams);
                    }
                });
                childItemView.setTag(goodsBean);

            }
        };
        topicGoodsAdapter.setOnItemClickListener((adapter, view, position) -> {
            HomeWelfareBean.GoodsBean goodsBean = (HomeWelfareBean.GoodsBean) view.getTag();
            Intent intent = new Intent(mContext, ShopScrollDetailsActivity.class);
            intent.putExtra("productId", String.valueOf(goodsBean.getId()));
            mContext.startActivity(intent);
        });
        mRvTopicGoods.setAdapter(topicGoodsAdapter);
    }
}
