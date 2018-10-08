package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.DMLThemeEntity.DMLThemeBean.DMLGoodsBean;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * Created by atd48 on 2016/6/28.
 */
public class DuMoLifeHorRecyclerAdapter extends BaseMultiItemQuickAdapter<DMLGoodsBean, DuMoLifeHorRecyclerAdapter.HorViewHolderHelper> {
    private final Context context;
    private final int screenWidth;

    public DuMoLifeHorRecyclerAdapter(Context context, List<DMLGoodsBean> goodList) {
        super(goodList);
        addItemType(ConstantVariable.TYPE_0, R.layout.adapter_duomolife_hor_recycle_item);
        addItemType(ConstantVariable.TYPE_1, R.layout.adapter_dml_hor_recycle_look_more);
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenWidth = (int) ((app.getScreenWidth() - AutoSizeUtils.mm2px(mAppContext,20 * 5)) / 2.5f);
        this.context = context;
    }

    @Override
    protected void convert(HorViewHolderHelper helper, DMLGoodsBean DMLGoodsBean) {
        if (helper.getItemViewType() == ConstantVariable.TYPE_0) {
            GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.img_duomolife_gv_item), DMLGoodsBean.getPicUrl());
            helper.setText(R.id.tv_duomolife_gv_item_content, getStrings(DMLGoodsBean.getName()))
                    .setText(R.id.tv_duomolife_gv_item_price, "￥" + DMLGoodsBean.getPrice());
        }
        helper.itemView.setTag(DMLGoodsBean);
    }

    public class HorViewHolderHelper extends BaseViewHolder {
        RelativeLayout rel_hor_welfare_img;
        LinearLayout ll_hor_welfare_text;
        RelativeLayout rel_dnl_hor_layout;

        public HorViewHolderHelper(View view) {
            super(view);
            rel_hor_welfare_img = (RelativeLayout) view.findViewById(R.id.rel_hor_welfare_img);
            ll_hor_welfare_text = (LinearLayout) view.findViewById(R.id.ll_hor_welfare_text);
            rel_dnl_hor_layout = (RelativeLayout) view.findViewById(R.id.rel_dnl_hor_layout);
            if (rel_hor_welfare_img != null) {
                rel_hor_welfare_img.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        rel_hor_welfare_img.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        ViewGroup.LayoutParams layoutParams = rel_hor_welfare_img.getLayoutParams();
                        layoutParams.width = screenWidth;
                        layoutParams.height = screenWidth;
                        rel_hor_welfare_img.setLayoutParams(layoutParams);
                    }
                });
                ll_hor_welfare_text.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        ll_hor_welfare_text.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        ViewGroup.LayoutParams layoutParams = ll_hor_welfare_text.getLayoutParams();
                        layoutParams.width = screenWidth;
                        ll_hor_welfare_text.setLayoutParams(layoutParams);
                        ll_hor_welfare_text.setLayoutParams(ll_hor_welfare_text.getLayoutParams());
                    }
                });
            } else if (rel_dnl_hor_layout != null) {
                rel_dnl_hor_layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        rel_dnl_hor_layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        ViewGroup.LayoutParams layoutParams = rel_dnl_hor_layout.getLayoutParams();
                        layoutParams.width = screenWidth;
                        layoutParams.height = screenWidth;
                        rel_dnl_hor_layout.setLayoutParams(layoutParams);
                        rel_dnl_hor_layout.setLayoutParams(rel_dnl_hor_layout.getLayoutParams());
                    }
                });
            }
        }
    }
}
