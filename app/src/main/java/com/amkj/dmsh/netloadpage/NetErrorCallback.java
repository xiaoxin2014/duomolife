package com.amkj.dmsh.netloadpage;

import android.content.Context;
import android.view.View;

import com.amkj.dmsh.R;
import com.kingja.loadsir.callback.Callback;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/31
 * version 3.1.6
 * class description:请求网络加载界面
 */
public class NetErrorCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.layout_communal_net_error;
    }

    @Override
    protected boolean onReloadEvent(Context context, View view) {
        return super.onReloadEvent(context, view);
    }
}
