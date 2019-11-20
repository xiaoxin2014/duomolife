package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.homepage.bean.HomeCommonEntity.HomeCommonBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2019/4/13 0013
 * Version:v4.0.0
 * ClassDescription :新版首页Top活动区适配器
 */
public class HomeTopAdapter extends BaseQuickAdapter<HomeCommonBean, BaseViewHolder> {
    private final List<HomeCommonBean> communalADActivityBeanList;
    private Context context;

    public HomeTopAdapter(Context context, List<HomeCommonBean> homeTopBeanList) {
        super(R.layout.item_home_top, homeTopBeanList);
        this.context = context;
        this.communalADActivityBeanList = homeTopBeanList;
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeCommonBean homeTopBean) {
        if (homeTopBean == null) return;
        GlideImageLoaderUtil.loadRoundImg(context, holder.getView(R.id.iv_icon), homeTopBean.getIcon(), AutoSizeUtils.mm2px(
                mAppContext, 45));
        holder.setText(R.id.tv_name, getStrings(homeTopBean.getName()));
        holder.setText(R.id.tv_bubble, homeTopBean.getDescription());
        holder.setVisible(R.id.tv_bubble, !TextUtils.isEmpty(homeTopBean.getDescription()));

        View itemView = holder.itemView;
        itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int screenWidth = ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getScreenWidth();
                int width = (screenWidth / communalADActivityBeanList.size());
                ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
                layoutParams.width = width;
                itemView.setLayoutParams(layoutParams);

                TextView tvBubble = holder.getView(R.id.tv_bubble);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.getView(R.id.tv_bubble).getLayoutParams();
                lp.rightMargin = width / 2 - AutoSizeUtils.mm2px(mContext, 70);
                tvBubble.setLayoutParams(lp);
            }
        });


        itemView.setTag(homeTopBean);
    }
}
