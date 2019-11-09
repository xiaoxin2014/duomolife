package com.amkj.dmsh.constant;

import android.app.Activity;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.homepage.bean.MarqueeTextEntity;
import com.amkj.dmsh.views.MarqueeTextView;
import com.bigkoo.convenientbanner.holder.Holder;


public class RollingMsgHolderView extends Holder<MarqueeTextEntity.MarqueeTextBean> {
    private final Activity context;
    private MarqueeTextView mMarqueeTextView;

    public RollingMsgHolderView(View itemView, Activity context) {
        super(itemView);
        this.context = context;
    }

    @Override
    protected void initView(View itemView) {
        mMarqueeTextView = itemView.findViewById(R.id.tv_roll_msg);
    }

    @Override
    public void updateUI(MarqueeTextEntity.MarqueeTextBean item) {
        if (item == null) return;
        mMarqueeTextView.setText(ConstantMethod.getStrings(item.getContent()));
        mMarqueeTextView.setMarqueeRepeatLimit(item.getShow_count());
    }
}
