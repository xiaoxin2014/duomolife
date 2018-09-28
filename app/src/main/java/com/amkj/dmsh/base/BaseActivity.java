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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.TotalPersonalTrajectory;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.message.bean.MessageTotalEntity;
import com.amkj.dmsh.message.bean.MessageTotalEntity.MessageTotalBean;
import com.amkj.dmsh.netloadpage.NetEmptyCallback;
import com.amkj.dmsh.netloadpage.NetLoadCallback;
import com.amkj.dmsh.views.SystemBarHelper;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.kingja.loadsir.core.Transport;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import cn.jzvd.JZVideoPlayer;
import me.jessyan.autosize.utils.AutoSizeUtils;
import me.leolin.shortcutbadger.ShortcutBadger;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.userId;

;

public abstract class BaseActivity extends AppCompatActivity {
    public KProgressHUD loadHud;
    private BadgeDesktopReceiver badgeDesktopReceiver;
    public TotalPersonalTrajectory totalPersonalTrajectory;
    public LoadService loadService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        loadHud = KProgressHUD.create(this)
                .setCancellable(true)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setSize(AutoSizeUtils.mm2px(mAppContext,50), AutoSizeUtils.mm2px(mAppContext,50));
//                .setDimAmount(0.5f)
        // 重新加载逻辑
        if (isAddLoad()) {
            loadService = LoadSir.getDefault().register(getLoadView() != null ? getLoadView() : this, new Callback.OnReloadListener() {
                @Override
                public void onReload(View v) {
                    // 重新加载逻辑
                    loadService.showCallback(NetLoadCallback.class);
                    loadData();
                }
            }, NetLoadUtils.getQyInstance().getLoadSirCover());
            String hintText;
            switch (getClass().getSimpleName()) {
                case "ShopScrollDetailsActivity":
                case "IntegralScrollDetailsActivity":
                case "ShopTimeScrollDetailsActivity":
                case "QualityGroupShopDetailActivity":
                    hintText = "暂时没有商品哦";
                    break;
                case "MineInvitationListActivity":
                    hintText = "你还没有发过帖子\n赶快去发布优质内容吧";
                    break;
                case "ShopTimeMyWarmActivity":
                    hintText = "你还没有设置提醒";
                    break;
                case "MineCollectProductActivity":
                    hintText = "你还没有收藏商品\n赶快去收藏";
                    break;
                default:
                    hintText = "暂无数据，稍后重试";
                    break;
            }
            String finalHintText = hintText;
            loadService.setCallBack(NetEmptyCallback.class, new Transport() {
                @Override
                public void order(Context context, View view) {
                    TextView tv_communal_net_tint = view.findViewById(R.id.tv_communal_net_tint);
                    tv_communal_net_tint.setText(finalHintText);
                }
            });
        }
        initViews();

        // 注册当前Activity为订阅者
        EventBus eventBus = EventBus.getDefault();
        eventBus.register(this);
        loadData();
//        设置状态栏
        setStatusColor();
    }

    public void setStatusColor() {
        SystemBarHelper.setStatusBarDarkMode(BaseActivity.this);
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
        if (totalPersonalTrajectory == null) {
            totalPersonalTrajectory = new TotalPersonalTrajectory(this);
        }
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
                 * 海外直邮 营销活动 帖子详情 帖子文章 文章详情 分类详情
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
                case "QualityTypeProductActivity":
                    break;
                default:
                    totalPersonalTrajectory.stopTotal();
                    break;
            }
        }
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
            BaseActivity.this.postEventResult((EventMessage) message);
        } else {
            BaseActivity.this.postOtherResult(message);
        }
    }

    // 传递EventBus事件类型结果，子类实现逻辑
    protected void postEventResult(@NonNull EventMessage message) {
    }

    // 传送其他结果，子类实现逻辑
    protected void postOtherResult(@NonNull Object message) {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 设置系统字体不会导致app布局改变
     *
     * @return
     */
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

    /**
     * 获取loadView
     *
     * @return
     */
    protected View getLoadView() {
        return null;
    }

    /**
     * 是否默认加载
     *
     * @return
     */
    protected boolean isAddLoad() {
        return false;
    }

    protected abstract void loadData();

    //    获取数据
    protected void getData() {
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(BaseActivity.this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    public class BadgeDesktopReceiver extends BroadcastReceiver {

        private final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                        getDesktopMesCount();
                    }
                }
            }
        }
    }

    /**
     * 获取消息信息
     * 配置桌面图标角标
     */
    private void getDesktopMesCount() {
        if (userId > 0) {
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
        }
    }
}
