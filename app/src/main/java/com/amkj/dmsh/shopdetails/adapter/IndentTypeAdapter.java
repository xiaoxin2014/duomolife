package com.amkj.dmsh.shopdetails.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.QualityTypeEntity;
import com.amkj.dmsh.bean.QualityTypeEntity.QualityTypeBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import q.rorbin.badgeview.Badge;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getTopBadge;

;

/**
 * Created by atd48 on 2016/9/13.
 */
public class IndentTypeAdapter extends BaseQuickAdapter<QualityTypeBean, IndentTypeAdapter.BaseViewHolderHelperBind> {
    private final Context context;

    public IndentTypeAdapter(Context context, List<QualityTypeEntity.QualityTypeBean> qualityTypeBeanList) {
        super(R.layout.adapter_intent_type, qualityTypeBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelperBind helper, QualityTypeBean qualityTypeBeanBean) {
        ImageView img_indent_icon = helper.getView(R.id.img_indent_icon);
        Badge badge = helper.badge;
        if (badge != null) {
            badge.setBadgeNumber(qualityTypeBeanBean.getType());
        }
        try {
            img_indent_icon.setImageResource(context.getResources().getIdentifier(qualityTypeBeanBean.getPicUrl(), "drawable", "com.amkj.dmsh"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        helper.setText(R.id.tv_intent_type_title, getStrings(qualityTypeBeanBean.getName()));
        helper.itemView.setTag(qualityTypeBeanBean);
    }

    public class BaseViewHolderHelperBind extends BaseViewHolder {
        Badge badge;
        FrameLayout fl_intent_mes_layout;

        public BaseViewHolderHelperBind(View view) {
            super(view);
            fl_intent_mes_layout = (FrameLayout) view.findViewById(R.id.fl_intent_mes_layout);
            badge = getTopBadge(context, fl_intent_mes_layout);
        }
    }
}
