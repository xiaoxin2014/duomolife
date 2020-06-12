package com.amkj.dmsh.views.JzVideo;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.AttributeSet;
import android.view.View;

import com.amkj.dmsh.R;

/**
 * Created by xiaoxin on 2020/6/11
 * Version:v4.6.1
 * ClassDescription :定制全屏播放
 */
public class JzVideoPlayerFullScrren extends JzVideoPlayerStatusDialog {
    public JzVideoPlayerFullScrren(Context context) {
        super(context);
    }

    public JzVideoPlayerFullScrren(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        //拦截返回和全屏点击事件，直接finish页面
        if (i == R.id.back || i == R.id.fullscreen) {
            if (getActivity() != null) {
                getActivity().finish();
            }
            return;
        }
        super.onClick(v);
    }

    //全屏播放完成，直接finish页面
    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        if (getActivity() != null) {
            getActivity().finish();
        }
    }


    //获取View的LifecycleOwner
    private Activity getActivity() {
        if (getContext() instanceof ContextWrapper) {
            Context baseContext = ((ContextWrapper) getContext()).getBaseContext();
            if (baseContext instanceof Activity) {
                return ((Activity) baseContext);
            }
        }


        return getContext() instanceof Activity ? ((Activity) getContext()) : null;
    }
}