package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.mine.bean.MineTypeEntity.MineTypeBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import q.rorbin.badgeview.Badge;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getTopBadge;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/7
 * class description:我的-底栏
 */

public class MineTypeAdapter extends BaseQuickAdapter<MineTypeBean, MineTypeAdapter.TypeViewHolderHelper> {
    private final Context context;

    public MineTypeAdapter(Context context, List<MineTypeBean> mineTypeList) {
        super(R.layout.adapter_mine_bottom, mineTypeList);
        this.context = context;
    }

    @Override
    protected void convert(TypeViewHolderHelper helper, MineTypeBean mineTypeBean) {
        Badge badge = helper.badge;
        if (badge != null) {
//            if (!"ShopCarActivity".equals(mineTypeBean.getAndroidUrl()) || mineTypeBean.isGetCartTip()) {
            badge.setBadgeNumber(mineTypeBean.getMesCount());
//            }
        }
        GlideImageLoaderUtil.loadFitCenter(context, helper.iv_mine_type_icon, mineTypeBean.getIconUrl());
        helper.setText(R.id.tv_mine_type_title, getStrings(mineTypeBean.getName()))
                .itemView.setTag(mineTypeBean);
    }

    public class TypeViewHolderHelper extends BaseViewHolder {
        Badge badge;
        ImageView iv_mine_type_icon;

        public TypeViewHolderHelper(View view) {
            super(view);
            iv_mine_type_icon = (ImageView) view.findViewById(R.id.iv_mine_type_icon);
            badge = getTopBadge(context, iv_mine_type_icon);
        }
    }
}
