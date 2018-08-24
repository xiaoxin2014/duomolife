package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.DMLThemeEntity.DMLThemeBean;
import com.amkj.dmsh.bean.DMLThemeEntity.DMLThemeBean.DMLGoodsBean;
import com.amkj.dmsh.dominant.activity.DoMoLifeWelfareDetailsActivity;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atd48 on 2016/6/27.
 */
public class DoMoLifeWelfareHeaderAdapter extends BaseQuickAdapter<DMLThemeBean, BaseViewHolderHelperWelfare> {
    private final Context context;
    private List<DMLGoodsBean> goodsList;

    public DoMoLifeWelfareHeaderAdapter(Context context, List<DMLThemeBean> themeList) {
        super(R.layout.adapter_welfare_header_item, themeList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelperWelfare helper, DMLThemeBean DMLThemeBean) {
        ImageView img_domolife_welfare_header_img = helper.getView(R.id.iv_cover_bg_header_img);
        GlideImageLoaderUtil.loadCenterCrop(context, img_domolife_welfare_header_img, DMLThemeBean.getPicUrl());
        img_domolife_welfare_header_img.setTag(R.id.iv_avatar_tag, DMLThemeBean);
        img_domolife_welfare_header_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DMLThemeBean dmlThemeBean = (DMLThemeBean) v.getTag(R.id.iv_avatar_tag);
                if (dmlThemeBean != null) {
                    //跳转至厨房工具大作战
                    Intent intent = new Intent(context, DoMoLifeWelfareDetailsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("welfareId", String.valueOf(dmlThemeBean.getId()));
                    context.startActivity(intent);
                }
            }
        });
        goodsList = new ArrayList<>();
        if (DMLThemeBean.getGoods() != null && DMLThemeBean.getGoods().size() > 0) {
            goodsList.addAll(DMLThemeBean.getGoods());
        }
        DuMoLifeHorRecyclerAdapter horGvAdapter = new DuMoLifeHorRecyclerAdapter(context, goodsList);
        helper.rv_welfare_header_item_horizontal.setAdapter(horGvAdapter);
        horGvAdapter.setNewData(goodsList);
        horGvAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DMLGoodsBean dmlGoodsBean = (DMLGoodsBean) view.getTag();
                if (dmlGoodsBean != null) {
                    //跳转良品详情页
                    Intent intent = new Intent(context, ShopScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(dmlGoodsBean.getId()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }
}
