package com.amkj.dmsh.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.TotalPersonalTrajectory;
import com.gyf.barlibrary.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.tencent.stat.StatService;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jzvd.Jzvd;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeCompat;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.START_AUTO_PAGE_TURN;

;

public abstract class BaseFragmentActivity extends RxAppCompatActivity {
    public KProgressHUD loadHud;
    private Unbinder mUnBinder;
    private TotalPersonalTrajectory totalPersonalTrajectory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        mUnBinder = ButterKnife.bind(this);
        // 注册当前Activity为订阅者
        EventBus eventBus = EventBus.getDefault();
        eventBus.register(this);
        loadHud = KProgressHUD.create(this)
                .setCancellable(true)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setSize(AutoSizeUtils.mm2px(mAppContext, 50), AutoSizeUtils.mm2px(mAppContext, 50));
//                .setDimAmount(0.5f)
        initViews();
        loadData();
//        设置状态栏
        setStatusBar();
        if (ToastUtils.getToast() == null) {
            // 因为吐司只有初始化的时候才会判断通知权限有没有开启，根据这个通知开关来显示原生的吐司还是兼容的吐司
            ToastUtils.init(TinkerManager.getApplication());
        }
    }

    /**
     * 设置状态栏颜色
     */
    public void setStatusBar() {
        ImmersionBar.with(this).statusBarColor(R.color.colorPrimary).keyboardEnable(true).navigationBarEnable(false)
                .statusBarDarkFont(true).fitsSystemWindows(true).init();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
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
            this.postEventResult((EventMessage) message);
        } else {
            this.postOtherResult(message);
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
        AutoSize.autoConvertDensityOfGlobal(this);
//        友盟统计
        MobclickAgent.onResume(this);
//        腾讯分析
        StatService.onResume(this);
        totalPersonalTrajectory = new TotalPersonalTrajectory(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //        友盟统计
        MobclickAgent.onPause(this);
//        腾讯移动分析
        StatService.onPause(this);

        Jzvd.releaseAllVideos();
        //        避免播放 置于后台，释放滚动
        EventBus.getDefault().post(new EventMessage(START_AUTO_PAGE_TURN, START_AUTO_PAGE_TURN));
        saveTotalData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
        setStatusBar();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1) {
            getResources();
        }
        super.onConfigurationChanged(newConfig);
        // 如果你的app可以横竖屏切换，并且适配4.4或者emui3手机请务必在onConfigurationChanged方法里添加这句话
        ImmersionBar.with(this).init();
        //取消rxjava 订阅
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                })
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe();
    }

    protected abstract int getContentView();

    protected abstract void initViews();

    protected abstract void loadData();

    @Override
    protected void onRestart() {
        super.onRestart();
        // 如果通知栏的权限被手动关闭了
        if (ToastUtils.getToast() != null && !NotificationManagerCompat.from(this).areNotificationsEnabled() &&
                !"SupportToast".equals(ToastUtils.getToast().getClass().getSimpleName())) {
            // 因为吐司只有初始化的时候才会判断通知权限有没有开启，根据这个通知开关来显示原生的吐司还是兼容的吐司
            ToastUtils.init(TinkerManager.getApplication());
            recreate();
        }
    }
    @Override
    public Resources getResources() {
        AutoSizeCompat.autoConvertDensityOfGlobal((super.getResources()));//如果没有自定义需求用这个方法
        return super.getResources();
    }
}
