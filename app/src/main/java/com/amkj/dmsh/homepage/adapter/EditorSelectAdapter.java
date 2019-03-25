package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.EditorEntity.EditorBean.AttachProductListBean;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.bean.EditorEntity.EditorBean;
import static com.amkj.dmsh.constant.ConstantMethod.getDateFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2019/3/16 0016
 * Version：V3.3.0
 * Class description:小编精选适配器
 */
public class EditorSelectAdapter extends BaseQuickAdapter<EditorBean, BaseViewHolder> {
    private Context context;

    public EditorSelectAdapter(Context context, int layoutResId, @Nullable List<EditorBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, EditorBean item) {
        if (item == null) return;
        EditorBean.MainProductBean mainProductBean = item.getMainProduct();
        if (mainProductBean != null) {
            GlideImageLoaderUtil.loadImage(context, helper.getView(R.id.iv_big_pic), item.getCoverImg());
            TextView tvLike = helper.getView(R.id.tv_com_art_like_count);
            tvLike.setSelected(item.getIsFavor());
            tvLike.setText(getStrings(String.valueOf(item.getLikeNum())));

            helper.setText(R.id.tv_goods_name, mainProductBean.getProductName())//商品名称
                    .setText(R.id.tv_goods_introduce, getStrings(item.getSummary()))//商品介绍
                    .setText(R.id.tv_time, getDateFormat(item.getPublishTime(), ""))//发布时间
                    .setText(R.id.tv_com_art_comment_count, getStrings(String.valueOf(item.getMessageCount())))
                    .addOnClickListener(R.id.tv_com_art_like_count).setTag(R.id.tv_com_art_like_count, item)
                    .addOnClickListener(R.id.tv_com_art_comment_count).setTag(R.id.tv_com_art_comment_count, item)
                    .addOnClickListener(R.id.iv_big_pic).setTag(R.id.iv_big_pic, R.id.iv_tag, item);
        }


        //初始化子列表
        RecyclerView rvGoods = helper.getView(R.id.rv_goods);
        rvGoods.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvGoods.setNestedScrollingEnabled(false);
        BaseQuickAdapter<AttachProductListBean, BaseViewHolder> childAdapter = new BaseQuickAdapter<AttachProductListBean, BaseViewHolder>(R.layout.item_editor_goods, item.getAttachProductList()) {
            @Override
            protected void convert(BaseViewHolder helper, AttachProductListBean item) {
                GlideImageLoaderUtil.loadImage(context, helper.getView(R.id.iv_goods_pic), item.getProductImg());
                helper.setText(R.id.tv_goods_name2, getStrings(item.getProductName()));
                helper.itemView.setTag(item);
            }
        };
        //进入商品详情
        childAdapter.setOnItemClickListener((adapter, view, position) -> {
            AttachProductListBean attachProductBean = (AttachProductListBean) view.getTag();
            if (attachProductBean != null) {
                //进入商品详情
                Intent intent = new Intent(context, ShopScrollDetailsActivity.class);
                intent.putExtra("productId", String.valueOf(attachProductBean.getProductId()));
                context.startActivity(intent);
            }
        });
        rvGoods.setAdapter(childAdapter);
        helper.itemView.setTag(item);
    }
}
