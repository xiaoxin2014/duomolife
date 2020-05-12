package com.amkj.dmsh.homepage.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.EditorEntity.EditorBean;
import com.amkj.dmsh.bean.EditorEntity.EditorBean.AttachProductListBean;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_GOODS_IMG;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_GOODS_IMG_DIRECT_BUY;
import static com.amkj.dmsh.utils.TimeUtils.getDateFormat;

/**
 * Created by xiaoxin on 2019/3/16 0016
 * Version：V3.3.0
 * class description:小编精选适配器
 */
public class EditorSelectAdapter extends BaseQuickAdapter<EditorBean, BaseViewHolder> {
    private BaseActivity context;

    public EditorSelectAdapter(Activity context, @Nullable List<EditorBean> data) {
        super(R.layout.item_editor_introduce, data);
        this.context = (BaseActivity) context;
    }

    @Override
    protected void convert(BaseViewHolder helper, EditorBean item) {
        if (item == null) return;

        TextView tvLike = helper.getView(R.id.tv_com_art_like_count);
        tvLike.setSelected(item.getIsFavor());
        tvLike.setText(item.getLikeString());

        helper.setText(R.id.tv_goods_name, item.getTitle())//商品名称
                .setText(R.id.tv_time, getDateFormat(item.getPublishTime(), ""))//发布时间
                .setText(R.id.tv_com_art_comment_count, getStrings(String.valueOf(item.getMessageCount())))
                .addOnClickListener(R.id.tv_com_art_like_count).setTag(R.id.tv_com_art_like_count, item)
                .addOnClickListener(R.id.tv_com_art_comment_count).setTag(R.id.tv_com_art_comment_count, item);

        //初始化图文列表
        RecyclerView rvPicGoods = helper.getView(R.id.communal_recycler_wrap);
        rvPicGoods.setLayoutManager(new LinearLayoutManager(context));
        rvPicGoods.setNestedScrollingEnabled(false);
        //手动修改itemtype，显示立即购买按钮
        List<CommunalDetailObjectBean> dataList = CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(item.getContent());
        for (CommunalDetailObjectBean objectBean : dataList) {
            if (objectBean.getItemType() == TYPE_GOODS_IMG) {
                objectBean.setItemType(TYPE_GOODS_IMG_DIRECT_BUY);
            }
            //添加小编精选标志
            objectBean.setEditor(true);
        }
        CommunalDetailAdapter communalDetailAdapter = new CommunalDetailAdapter(context, dataList);
        communalDetailAdapter.setEnableLoadMore(false);
        rvPicGoods.setAdapter(communalDetailAdapter);

        //初始化商品列表
        RecyclerView rvGoods = helper.getView(R.id.rv_goods);
        rvGoods.setLayoutManager(new LinearLayoutManager(context));
        rvGoods.setNestedScrollingEnabled(false);
        BaseQuickAdapter<AttachProductListBean, BaseViewHolder> childAdapter = new BaseQuickAdapter<AttachProductListBean, BaseViewHolder>(R.layout.item_editor_goods, item.getAttachProductList()) {
            @Override
            protected void convert(BaseViewHolder helper, AttachProductListBean item) {
                GlideImageLoaderUtil.loadImage(context, helper.getView(R.id.iv_goods_pic), item.getProductImg());
                helper.setText(R.id.tv_goods_name2, getStrings(item.getProductName()));
                helper.itemView.setTag(item);
            }
        };
        childAdapter.setEnableLoadMore(false);
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
