package com.amkj.dmsh.homepage.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.EditorEntity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.amkj.dmsh.constant.ConstantMethod.getDeviceAppNotificationStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.REQUEST_NOTIFICATION_STATUS;

/**
 * Created by xiaoxin on 2019/3/18 0018
 * Version：V3.3.0
 * ClassDescription :小编精选头布局
 */
public class EditorHeadView extends LinearLayout {

    @BindView(R.id.tv_subscriber)
    TextView mTvSubscriber;
    @BindView(R.id.tv_first_subscribe)
    TextView mTvFirstSubscribe;
    @BindView(R.id.tv_editor_tips)
    TextView mTvEditorTips;
    @BindView(R.id.tv_editor_title)
    TextView mTvEditorTitle;

    private BaseActivity mContext;
    private EditorEntity mEditorEntity;

    public EditorHeadView(Activity activity) {
        this(activity, null);
        mContext = ((BaseActivity) activity);
        updateData(mEditorEntity);
    }

    public EditorHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View headView = LayoutInflater.from(context).inflate(R.layout.item_edictor_head, this, true);
        ButterKnife.bind(this, headView);
        //订阅
        mTvSubscriber.setOnClickListener(view -> {
            //订阅成功隐藏mTvFirstSubscribe
            if (userId > 0) {
                openNotification();
            } else {
                getLoginStatus(mContext);
            }
        });
    }

    public void updateData(EditorEntity editorEntity) {
        mEditorEntity = editorEntity;
        if (mEditorEntity != null) {
            mTvEditorTitle.setText(getStrings(mEditorEntity.getTitle()));
            mTvEditorTips.setText(getStrings(mEditorEntity.getDescription()));
            mTvEditorTips.setText(mEditorEntity.getDescription());
            mTvSubscriber.setText(getResources().getText(mEditorEntity.getIsSubscribe() ? R.string.already_subscribe : R.string.subscriber));
            mTvSubscriber.setSelected(mEditorEntity.getIsSubscribe());
            mTvFirstSubscribe.setText(getStrings(String.format(getResources().getString(R.string.first_subsribe), mEditorEntity.getScore())));
            mTvFirstSubscribe.setVisibility(mEditorEntity.getIsSubscribe() ? GONE : VISIBLE);
        }
    }

    /**
     * 打开app通知
     */
    private void openNotification() {
        if (!getDeviceAppNotificationStatus(mContext)) {
            AlertDialogHelper notificationAlertDialogHelper = new AlertDialogHelper(mContext);
            notificationAlertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                @Override
                public void confirm() {
                    // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                    intent.setData(uri);
                    mContext.startActivityForResult(intent, REQUEST_NOTIFICATION_STATUS);
                    notificationAlertDialogHelper.dismiss();
                }

                @Override
                public void cancel() {
                    notificationAlertDialogHelper.dismiss();
                }
            });

            notificationAlertDialogHelper.setTitle("是否打开推送通知")
                    .setMsg("打开推送通知\n有新的精选会立即通知您哦！")
                    .setCancelText("不需要")
                    .setMsgTextGravity(Gravity.CENTER)
                    .setConfirmText("好的");
            notificationAlertDialogHelper.show();
        } else {
            //请求订阅接口进行订阅
            if (mContext.loadHud != null && mEditorEntity != null) {
                SubscribeEditor();
            }
        }
    }

    private void SubscribeEditor() {
        if (userId > 0) {
            mContext.loadHud.show();
            Map<String, Object> map = new HashMap<>();
            //选中(已订阅)  未选中(订阅)
            boolean selected = !mTvSubscriber.isSelected();
            map.put("operation", mEditorEntity.getSubscriberStatus(selected));
            map.put("uid", userId);
            NetLoadUtils.getNetInstance().loadNetDataPost(mContext, Url.SUBSCRIBER_EDITOR, map, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    mContext.loadHud.dismiss();
                    mTvSubscriber.setSelected(selected);
                    mTvSubscriber.setText(selected ? R.string.already_subscribe : R.string.subscriber);
                    mTvFirstSubscribe.setVisibility(View.GONE);
                    ConstantMethod.showToast(mContext, selected ? R.string.subscriber_sucess : R.string.subscriber_cancle);
                }

                @Override
                public void onNotNetOrException() {
                    mContext.loadHud.dismiss();
                }

                @Override
                public void netClose() {
                    showToast(mContext, R.string.unConnectedNetwork);
                }

                @Override
                public void onError(Throwable throwable) {
                    showToast(mContext, selected ? R.string.subscriber_failed : R.string.unsubscriber_failed);
                }
            });
        } else {
            getLoginStatus(mContext);
        }
    }
}



