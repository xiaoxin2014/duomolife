package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.homepage.activity.VideoDetailActivity;
import com.amkj.dmsh.homepage.bean.HomeCommonEntity.HomeCommonBean;
import com.amkj.dmsh.homepage.bean.HomeCommonEntity.ProductInfoListBean;
import com.amkj.dmsh.homepage.bean.HomeCommonEntity.HomeCommonBean.VideoInfoListBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

import static com.amkj.dmsh.constant.ConstantMethod.getRmbFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2019/4/18 0018
 * Version:v4.0.0
 * ClassDescription :首页专区适配器
 */
public class HomeZoneAdapter extends BaseMultiItemQuickAdapter<HomeCommonBean, BaseViewHolder> {

    private final Context mContext;

    public HomeZoneAdapter(Context context, @Nullable List<HomeCommonBean> data) {
        super(data);
        addItemType(0, R.layout.item_home_zone_normal);
        addItemType(1, R.layout.item_home_zone_video);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeCommonBean item) {
        if (item == null) return;
        switch (item.getItemType()) {
            case 0:
                //专区名称以及副标题
                helper.setText(R.id.tv_zone_name, getStrings(item.getName())).setText(R.id.tv_description, getStrings(item.getSubtitle()));
                List<ProductInfoListBean> productInfoList = item.getProductInfoList();

                //专区左边商品
                ProductInfoListBean productInfoListBean = productInfoList.get(0);
                if (productInfoListBean != null) {
                    GlideImageLoaderUtil.loadImage(mContext, helper.getView(R.id.iv_cover_left), productInfoListBean.getImg());
                    helper.setText(R.id.tv_price_left, getRmbFormat(mContext, productInfoListBean.getPrice()));
                    helper.setText(R.id.tv_market_price_left, "¥" + getStrings(productInfoListBean.getMarketPrice()));
                    ((TextView) helper.getView(R.id.tv_market_price_left)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }

                //专区右边商品
                helper.getView(R.id.ll_right).setVisibility(productInfoList.size() > 1 ? View.VISIBLE : View.GONE);
                if (productInfoList.size() > 1) {
                    ProductInfoListBean rightBean = productInfoList.get(1);
                    if (rightBean != null) {
                        GlideImageLoaderUtil.loadImage(mContext, helper.getView(R.id.iv_cover_right), rightBean.getImg());
                        helper.setText(R.id.tv_price_right, getRmbFormat(mContext, rightBean.getPrice()));
                        helper.setText(R.id.tv_market_price_right, "¥" + getStrings(rightBean.getMarketPrice()));
                        ((TextView) helper.getView(R.id.tv_market_price_right)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        ((TextView) helper.getView(R.id.tv_market_price_right)).getPaint().setAntiAlias(true);
                    }
                }
                break;
            case 1:
                helper.setText(R.id.tv_title, getStrings(item.getName()))
                        .setText(R.id.tv_subtitle, getStrings(item.getSubtitle()))
                        .setText(R.id.tv_description, getStrings(item.getDescription()));
                List<VideoInfoListBean> videoInfoList = item.getVideoInfoList();
                VideoInfoListBean videoInfoListBean = videoInfoList.get(0);
                if (videoInfoListBean != null) {
                    GlideImageLoaderUtil.loadCenterCrop(mContext, helper.getView(R.id.iv_cover_left), videoInfoListBean.getCoverPath());
                    helper.getView(R.id.iv_cover_left).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, VideoDetailActivity.class);
                            intent.putExtra("id", videoInfoListBean.getId());
                            mContext.startActivity(intent);
                        }
                    });
                }

                //专区右边商品
                helper.getView(R.id.ll_right).setVisibility(videoInfoList.size() > 1 ? View.VISIBLE : View.GONE);
                if (videoInfoList.size() > 1) {
                    VideoInfoListBean rightBean = videoInfoList.get(1);
                    if (rightBean != null) {
                        GlideImageLoaderUtil.loadCenterCrop(mContext, helper.getView(R.id.iv_cover_right), rightBean.getCoverPath());
                        helper.getView(R.id.iv_cover_right).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, VideoDetailActivity.class);
                                intent.putExtra("id", rightBean.getId());
                                mContext.startActivity(intent);
                            }
                        });
                    }
                }
                break;
        }

        helper.itemView.setTag(item);
    }
}
