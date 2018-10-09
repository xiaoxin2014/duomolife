package com.amkj.dmsh.netloadpage;

import android.content.Context;
import android.view.View;

import com.amkj.dmsh.R;
import com.kingja.loadsir.callback.Callback;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/31
 * version 3.1.7
 * class description:请求网络版半透明
 */
public class NetLoadTranslucenceCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.layout_communal_net_load;
    }

    @Override
    protected void onViewCreate(Context context, View view) {
        view.findViewById(R.id.fl_net_loading).setBackgroundColor(context.getResources().getColor(R.color.translucence));
    }
}
