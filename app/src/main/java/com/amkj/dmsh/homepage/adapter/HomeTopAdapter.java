package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2019/4/13 0013
 * Version:v4.0.0
 * ClassDescription :新版首页Top栏适配器
 */
public class HomeTopAdapter extends BaseQuickAdapter<CommunalADActivityBean, HomeTopAdapter.HotViewHolderHelper> {
    private final List<CommunalADActivityBean> communalADActivityBeanList;
    private Context context;
    private final int screenWidth;

    public HomeTopAdapter(Context context, List<CommunalADActivityBean> communalADActivityBeanList) {
        super(R.layout.adapter_homepage_hot_activity, communalADActivityBeanList);
        this.context = context;
        this.communalADActivityBeanList = communalADActivityBeanList;
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenWidth = app.getScreenWidth();
    }

    @Override
    protected void convert(HotViewHolderHelper holder, CommunalADActivityBean communalADActivityBean) {
        GlideImageLoaderUtil.loadFitCenter(context, holder.getView(R.id.iv_home_hot_img), communalADActivityBean.getIcon());
        holder.setText(R.id.tv_home_hot_title, getStrings(communalADActivityBean.getName()));
        if (!TextUtils.isEmpty(communalADActivityBean.getDescription())) {
            holder.setVisible(R.id.tv_bubble, true);
            holder.setText(R.id.tv_bubble, communalADActivityBean.getDescription());
        }

        holder.itemView.setTag(communalADActivityBean);
    }

    public class HotViewHolderHelper extends BaseViewHolder {
        LinearLayout ll_hot_layout;

        public HotViewHolderHelper(View view) {
            super(view);
            ll_hot_layout = view.findViewById(R.id.ll_hot_layout);
            ll_hot_layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ll_hot_layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int width = (screenWidth / (communalADActivityBeanList.size() > 5 ? 5 : communalADActivityBeanList.size()) + 1);
                    ViewGroup.LayoutParams layoutParams = ll_hot_layout.getLayoutParams();
                    layoutParams.width = width;
                    ll_hot_layout.setLayoutParams(layoutParams);
                }
            });
        }
    }
}
