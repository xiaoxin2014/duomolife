package com.amkj.dmsh.views;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getDeviceAppNotificationStatus;

/**
 * Created by xiaoxin on 2020/3/26
 * Version:v4.5.0
 * ClassDescription :物流通知提醒组合式控件
 */
public class LogisticsNotifyView extends LinearLayout implements LifecycleObserver {

    @BindView(R.id.tv_open_notify)
    TextView mTvOpenNotify;
    @BindView(R.id.iv_close)
    ImageView mIvClose;
    @BindView(R.id.ll_notify)
    LinearLayout mLlNotify;

    public LogisticsNotifyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_logistics_notify, this, true);
        ButterKnife.bind(this, view);
        if (getLifeCycleOwner() != null) {
            getLifeCycleOwner().getLifecycle().addObserver(this);
        }

        //判断通知是否打开
        mLlNotify.setVisibility(!getDeviceAppNotificationStatus() ? View.VISIBLE : View.GONE);
        String string = context.getString(R.string.receive_express_msg);
        mTvOpenNotify.setText(ConstantMethod.getSpannableString(string, 16, string.length(), -1, "#0a88fa"));
    }

    @OnClick({R.id.tv_open_notify, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_open_notify:
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                intent.setData(uri);
                getContext().startActivity(intent);
                break;
            case R.id.iv_close:
                mLlNotify.setVisibility(View.GONE);
                break;
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onResume() {
        mLlNotify.setVisibility(!getDeviceAppNotificationStatus() ? View.VISIBLE : View.GONE);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestory() {
        if (getLifeCycleOwner() != null) {
            getLifeCycleOwner().getLifecycle().removeObserver(this);
        }
    }


    //获取View的LifecycleOwner
    private LifecycleOwner getLifeCycleOwner() {
        if (getContext() instanceof ContextWrapper) {
            Context baseContext = ((ContextWrapper) getContext()).getBaseContext();
            if (baseContext instanceof LifecycleOwner) {
                return ((LifecycleOwner) baseContext);
            }
        }

        return null;
    }
}
