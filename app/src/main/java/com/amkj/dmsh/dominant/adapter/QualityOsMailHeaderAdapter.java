package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.DMLThemeEntity.DMLThemeBean;
import com.amkj.dmsh.bean.DMLThemeEntity.DMLThemeBean.DMLGoodsBean;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.dominant.activity.DoMoLifeWelfareDetailsActivity;
import com.amkj.dmsh.dominant.activity.QualityOverseasDetailsActivity;
import com.amkj.dmsh.homepage.adapter.DuMoLifeHorRecyclerAdapter;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/30
 * class description:请输入类描述
 */

public class QualityOsMailHeaderAdapter extends BaseQuickAdapter<DMLThemeBean, BaseViewHolder> {
    private final Context context;
    private final String type;
    private List<DMLGoodsBean> goodsList;

    public QualityOsMailHeaderAdapter(Context context, List<DMLThemeBean> dmlThemeBeanList, String type) {
        super(R.layout.adapter_welfare_header_item, dmlThemeBeanList);
        this.type = type;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final DMLThemeBean dMLThemeBean) {
        ImageView iv_cover_bg_header_img = helper.getView(R.id.iv_cover_bg_header_img);
        GlideImageLoaderUtil.loadCenterCrop(context, iv_cover_bg_header_img, dMLThemeBean.getPicUrl());
        goodsList = new ArrayList<>();
        if (dMLThemeBean.getGoods() != null && dMLThemeBean.getGoods().size() > 0) {
            goodsList.addAll(dMLThemeBean.getGoods());
        }
        iv_cover_bg_header_img.setTag(R.id.iv_tag, dMLThemeBean);
        iv_cover_bg_header_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DMLThemeBean dMLThemeBean = (DMLThemeBean) view.getTag(R.id.iv_tag);
                if (dMLThemeBean != null) {
                    Intent intent = new Intent();
                    if (type.equals("welfare")) {
                        intent.putExtra("welfareId", String.valueOf(dMLThemeBean.getId()));
                        intent.setClass(context, DoMoLifeWelfareDetailsActivity.class);
                        context.startActivity(intent);
                    } else if (type.equals("overseas")) {
                        intent.putExtra("overseasId", String.valueOf(dMLThemeBean.getId()));
                        intent.setClass(context, QualityOverseasDetailsActivity.class);
                        context.startActivity(intent);
                    }
                }
            }
        });
        RecyclerView rv_wrap_bar_none = helper.getView(R.id.rv_wrap_bar_none);
        rv_wrap_bar_none.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        DuMoLifeHorRecyclerAdapter horGvAdapter = new DuMoLifeHorRecyclerAdapter(context, goodsList);
        rv_wrap_bar_none.setAdapter(horGvAdapter);
        horGvAdapter.setNewData(goodsList);
        horGvAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DMLGoodsBean dMLGoodsBean = (DMLGoodsBean) view.getTag();
                if (dMLGoodsBean != null) {
                    //跳转良品详情页
                    Intent intent = new Intent();
                    if (dMLGoodsBean.getItemType() == ConstantVariable.TYPE_0) {
                        intent.setClass(context, ShopScrollDetailsActivity.class);
                        intent.putExtra("productId", String.valueOf(dMLGoodsBean.getId()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else if (dMLGoodsBean.getItemType() == ConstantVariable.TYPE_1) {
                        switch (type) {
                            case "welfare":
                                intent.setClass(context, DoMoLifeWelfareDetailsActivity.class);
                                intent.putExtra("welfareId", String.valueOf(dMLGoodsBean.getId()));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                break;
                            case "overseas":
                                intent.setClass(context, QualityOverseasDetailsActivity.class);
                                intent.putExtra("overseasId", String.valueOf(dMLGoodsBean.getId()));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                break;
                        }
                    }
                }
            }
        });
    }
}
