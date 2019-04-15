package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.homepage.bean.HomeCommonEntity.HomeCommonBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2019/4/13 0013
 * Version:v4.0.0
 * ClassDescription :新版首页Top活动区适配器
 */
public class HomeTopAdapter extends BaseQuickAdapter<HomeCommonBean, BaseViewHolder> {
    private final List<HomeCommonBean> communalADActivityBeanList;
    private Context context;
    private final int screenWidth;

    public HomeTopAdapter(Context context, List<HomeCommonBean> homeTopBeanList) {
        super(R.layout.item_home_top, homeTopBeanList);
        this.context = context;
        this.communalADActivityBeanList = homeTopBeanList;
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenWidth = app.getScreenWidth();
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeCommonBean homeTopBean) {
        GlideImageLoaderUtil.loadRoundImg(context, holder.getView(R.id.iv_icon), homeTopBean.getIcon(), AutoSizeUtils.mm2px(mContext, 45));
        holder.setText(R.id.tv_name, getStrings(homeTopBean.getName()));
        if (!TextUtils.isEmpty(homeTopBean.getDescription())) {
            holder.setVisible(R.id.tv_bubble, true);
            holder.setText(R.id.tv_bubble, homeTopBean.getDescription());
        }

        holder.itemView.setTag(homeTopBean);
    }

//    public class HotViewHolderHelper extends BaseViewHolder {
//        LinearLayout ll_hot_layout;
//
//        public HotViewHolderHelper(View view) {
//            super(view);
//            ll_hot_layout = view.findViewById(R.id.ll_hot_layout);
//            ll_hot_layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    ll_hot_layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    int width = (screenWidth / (communalADActivityBeanList.size() > 5 ? 5 : communalADActivityBeanList.size()) + 1);
//                    ViewGroup.LayoutParams layoutParams = ll_hot_layout.getLayoutParams();
//                    layoutParams.width = width;
//                    ll_hot_layout.setLayoutParams(layoutParams);
//                }
//            });
//        }
//    }
}
