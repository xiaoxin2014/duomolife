package com.amkj.dmsh.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.amkj.dmsh.constant.TotalPersonalTrajectory;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.message.bean.MessageTotalEntity;
import com.amkj.dmsh.message.bean.MessageTotalEntity.MessageTotalBean;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.views.SystemBarHelper;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;
import com.zhy.autolayout.AutoLayoutActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jzvd.JZVideoPlayer;
import me.jessyan.autosize.utils.AutoSizeUtils;
import me.leolin.shortcutbadger.ShortcutBadger;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.userId;

;

public abstract class BaseFragmentActivity extends AutoLayoutActivity {
    public KProgressHUD loadHud;
    private Unbinder mUnBinder;
    private BadgeDesktopReceiver badgeDesktopReceiver;
    private int uid;
    private TotalPersonalTrajectory totalPersonalTrajectory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        mUnBinder = ButterKnife.bind(this);
        loadHud = KProgressHUD.create(this)
                .setCancellable(true)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setSize(AutoSizeUtils.mm2px(mAppContext,50), AutoSizeUtils.mm2px(mAppContext,50));
//                .setDimAmount(0.5f)
        initViews();
        // 注册当前Activity为订阅者
        EventBus eventBus = EventBus.getDefault();
        eventBus.register(this);
        loadData();
//        设置状态栏
        setStatusColor();
    }

    public void setStatusColor() {
        SystemBarHelper.setStatusBarDarkMode(BaseFragmentActivity.this);
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetResult(EventMessage message) {
        if (message == null) {
            return;
        }

        // 是否为Event消息
        if (message instanceof EventMessage) {
            BaseFragmentActivity.this.postEventResult((EventMessage) message);
        } else {
            BaseFragmentActivity.this.postOtherResult(message);
        }
    }

    // 传递EventBus事件类型结果，子类实现逻辑
    protected void postEventResult(@NonNull EventMessage message) {

    }

    // 传送其他结果，子类实现逻辑
    protected void postOtherResult(@NonNull Object message) {

    }

    @Override
    protected void onResume() {
        super.onResume();
//        友盟统计
        MobclickAgent.onResume(this);
//        腾讯分析
        StatService.onResume(this);
        //创建广播
        badgeDesktopReceiver = new BadgeDesktopReceiver();
        //动态注册广播
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //启动广播
        registerReceiver(badgeDesktopReceiver, intentFilter);
        totalPersonalTrajectory = new TotalPersonalTrajectory(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //        友盟统计
        MobclickAgent.onPause(this);
//        腾讯移动分析
        StatService.onPause(this);

        JZVideoPlayer.releaseAllVideos();
        if (badgeDesktopReceiver != null) {
            try {
                unregisterReceiver(badgeDesktopReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        saveTotalData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveTotalData();
    }

    /**
     * 保存统计数据
     */
    private void saveTotalData() {
        String simpleName = getClass().getSimpleName();
        if (totalPersonalTrajectory != null) {
            switch (getStrings(simpleName)) {
                /**
                 * 商品详情 订单填写 多么定制 必买清单（历史清单） 福利社专题 种草营 自定义专区
                 * 海外直邮 营销活动 帖子详情 帖子文章 文章详情
                 */
                case "ShopScrollDetailsActivity":
                case "DmlOptimizedSelDetailActivity":
                case "QualityShopHistoryListActivity":
                case "DoMoLifeWelfareDetailsActivity":
                case "DmlLifeSearchDetailActivity":
                case "QualityCustomTopicActivity":
                case "QualityOverseasDetailsActivity":
                case "QualityProductActActivity":
                case "ArticleDetailsImgActivity":
                case "ArticleInvitationDetailsActivity":
                case "ArticleOfficialActivity":
                    break;
                default:
                    totalPersonalTrajectory.stopTotal();
                    break;
            }
        }
    }

    public class BadgeDesktopReceiver extends BroadcastReceiver {

        private final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        private final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)
                        /*||reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)*/) {
                        getLoginStatus();
                        getDesktopMesCount();
                    }
                }
            }
        }
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            uid = 0;
        }
    }

    /**
     * 获取消息信息
     * 配置桌面图标角标
     */
    private void getDesktopMesCount() {
        if (uid > 0) {
            String url = Url.BASE_URL + Url.H_MES_STATISTICS;
            Map<String, Object> params = new HashMap<>();
            params.put("uid", userId);
            NetLoadUtils.getQyInstance().loadNetDataPost(this, url
                    , params, new NetLoadUtils.NetLoadListener() {
                        @Override
                        public void onSuccess(String result) {
                            Gson gson = new Gson();
                            MessageTotalEntity messageTotalEntity = gson.fromJson(result, MessageTotalEntity.class);
                            if (messageTotalEntity != null) {
                                if (messageTotalEntity.getCode().equals("01")) {
                                    MessageTotalBean messageTotalBean = messageTotalEntity.getMessageTotalBean();
                                    int totalCount = messageTotalBean.getSmTotal() + messageTotalBean.getLikeTotal()
                                            + messageTotalBean.getCommentTotal() + messageTotalBean.getOrderTotal()
                                            + messageTotalBean.getCommOffifialTotal();
                                    if (!Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
                                        ShortcutBadger.applyCount(getApplicationContext(), totalCount);
                                    }
                                }
                            }
                        }

                        @Override
                        public void netClose() {

                        }

                        @Override
                        public void onError(Throwable throwable) {
                            ShortcutBadger.removeCount(getApplicationContext());
                        }
                    });
        } else {
            ShortcutBadger.removeCount(getApplicationContext());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
        if (EventBus.getDefault().isRegistered(BaseFragmentActivity.this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1) {
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    protected abstract int getContentView();

    protected abstract void initViews();

    protected abstract void loadData();
}
