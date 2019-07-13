package com.amkj.dmsh.dominant.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.DMLThemeEntity;
import com.amkj.dmsh.bean.DMLThemeEntity.DMLThemeBean;
import com.amkj.dmsh.bean.DMLThemeEntity.DMLThemeBean.DMLGoodsBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.dominant.activity.DoMoLifeWelfareDetailsActivity;
import com.amkj.dmsh.dominant.activity.QualityOverseasDetailsActivity;
import com.amkj.dmsh.homepage.adapter.DuMoLifeHorRecyclerAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.views.HorizontalLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.Url.H_DML_THEME;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/30
 * class description:请输入类描述
 */

public class QualityOsMailHeaderAdapter extends BaseQuickAdapter<DMLThemeBean, BaseViewHolder> {
    private final Activity context;
    private final String type;
    private final List<DMLThemeBean> mDMLThemeBeanList;
    private Map<Integer, Integer> pageMap = new HashMap<>();

    public QualityOsMailHeaderAdapter(Activity context, List<DMLThemeBean> dmlThemeBeanList, String type) {
        super(R.layout.adapter_welfare_header_item, dmlThemeBeanList);
        this.type = type;
        this.context = context;
        mDMLThemeBeanList = dmlThemeBeanList;
    }

    @Override
    protected void convert(BaseViewHolder helper, final DMLThemeBean dMLThemeBean) {
        ImageView iv_cover_bg_header_img = helper.getView(R.id.iv_cover_bg_header_img);
        GlideImageLoaderUtil.loadCenterCrop(context, iv_cover_bg_header_img, dMLThemeBean.getPicUrl());
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
        DuMoLifeHorRecyclerAdapter horGvAdapter = new DuMoLifeHorRecyclerAdapter(context, dMLThemeBean.getGoods());
        horGvAdapter.setLoadMoreView(new HorizontalLoadMoreView());
        rv_wrap_bar_none.setAdapter(horGvAdapter);
        horGvAdapter.setOnItemClickListener((adapter, view, position) -> {
            DMLGoodsBean dMLGoodsBean = (DMLGoodsBean) view.getTag();
            if (dMLGoodsBean != null) {
                //跳转良品详情页
                Intent intent = new Intent();
                intent.setClass(context, ShopScrollDetailsActivity.class);
                intent.putExtra("productId", String.valueOf(dMLGoodsBean.getId()));
                context.startActivity(intent);
                //记录埋点参数sourceId(福利社专题对应的ID)
                ConstantMethod.saveSourceId(context.getClass().getSimpleName(), String.valueOf(dMLThemeBean.getId()));
                //统计福利社点击商品
                ConstantMethod.totalWelfareProNum(context, dMLGoodsBean.getId(), dMLThemeBean.getId());
            }
        });
        horGvAdapter.setOnLoadMoreListener(() -> {
            Integer page = pageMap.get(helper.getAdapterPosition());
            pageMap.put(helper.getAdapterPosition(), page == null ? 2 : page + 1);
            getWelfareThemeData(horGvAdapter, helper.getAdapterPosition());
        }, rv_wrap_bar_none);
    }

    //关联商品分页处理
    private void getWelfareThemeData(DuMoLifeHorRecyclerAdapter horGvAdapter, int position) {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", 1);
        params.put("showCount", mDMLThemeBeanList.size());
        params.put("goodsCurrentPage", pageMap.get(position).intValue());
        params.put("goodsShowCount", ConstantVariable.TOTAL_COUNT_TEN);
        NetLoadUtils.getNetInstance().loadNetDataPost(context, H_DML_THEME
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();
                        DMLThemeEntity dmlTheme = gson.fromJson(result, DMLThemeEntity.class);
                        if (dmlTheme != null) {
                            List<DMLThemeBean> themeList = dmlTheme.getThemeList();
                            if (themeList != null && themeList.size() > 0) {
                                int id = mDMLThemeBeanList.get(position).getId();
                                DMLThemeBean dmlThemeBean = null;
                                for (int i = 0; i < themeList.size(); i++) {
                                    if (id == themeList.get(position).getId()) {
                                        dmlThemeBean = themeList.get(position);
                                        break;
                                    }
                                }
                                if (dmlThemeBean != null) {
                                    List<DMLGoodsBean> goods = dmlThemeBean.getGoods();
                                    if (goods != null && goods.size() > 0) {
                                        mDMLThemeBeanList.get(position).getGoods().addAll(goods);
                                        horGvAdapter.notifyItemChanged(position);
                                        horGvAdapter.loadMoreComplete();
                                    } else if (ERROR_CODE.equals(dmlTheme.getCode())) {
                                        ConstantMethod.showToast(dmlTheme.getMsg());
                                        horGvAdapter.loadMoreFail();
                                    } else {
                                        horGvAdapter.loadMoreEnd();
                                    }
                                } else {
                                    horGvAdapter.loadMoreEnd();
                                }
                            } else {
                                horGvAdapter.loadMoreEnd();
                            }
                        } else {
                            horGvAdapter.loadMoreEnd();
                        }
                    }

                    @Override
                    public void onNotNetOrException() {
                        horGvAdapter.loadMoreFail();
                    }
                });
    }

    public void refreshPage() {
        pageMap.clear();
    }
}
