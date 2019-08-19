package com.amkj.dmsh.find.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.find.bean.PostDetailEntity.PostDetailBean.RelatedGoodsBean;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.amkj.dmsh.constant.ConstantMethod.addShopCarGetSku;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2019/3/18 0018
 * Version：V4.1.0
 * ClassDescription :帖子插入单件商品
 */
public class PostGoodsView extends LinearLayout {
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.iv_add_car)
    ImageView mIvAddCar;
    @BindView(R.id.rl_goods)
    RelativeLayout mRlGoods;
    private BaseActivity context;
    private RelatedGoodsBean mRelatedGoodsBean;

    public PostGoodsView(RelatedGoodsBean relatedGoodsBean, BaseActivity activity) {
        this(activity, null);
        context = activity;
        mRelatedGoodsBean = relatedGoodsBean;
        updateData(activity, relatedGoodsBean);
    }


    public PostGoodsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View headView = LayoutInflater.from(context).inflate(R.layout.item_post_goods, this, true);
        ButterKnife.bind(this, headView);

    }

    public void updateData(BaseActivity activity, RelatedGoodsBean relatedGoodsBean) {
        GlideImageLoaderUtil.loadImage(activity, mIvCover, relatedGoodsBean.getPictureUrl());
        mTvTitle.setText(getStrings(relatedGoodsBean.getTitle()));
        mTvPrice.setText(("¥" + relatedGoodsBean.getPrice()));
        mIvAddCar.setOnClickListener(v -> {
            BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
            baseAddCarProInfoBean.setProductId(relatedGoodsBean.getProductId());
            baseAddCarProInfoBean.setProName(getStrings(relatedGoodsBean.getTitle()));
            baseAddCarProInfoBean.setProPic(getStrings(relatedGoodsBean.getPictureUrl()));
            addShopCarGetSku(activity, baseAddCarProInfoBean, activity.loadHud);
        });

        mRlGoods.setOnClickListener(v -> {
            if (ConstantMethod.isContextExisted(activity)) {
                Intent intent = new Intent(activity, ShopScrollDetailsActivity.class);
                intent.putExtra("productId", String.valueOf(relatedGoodsBean.getProductId()));
                activity.startActivity(intent);
            }
        });
    }
}



