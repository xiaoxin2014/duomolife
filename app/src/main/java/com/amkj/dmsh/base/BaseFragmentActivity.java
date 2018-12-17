package com.amkj.dmsh.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.TotalPersonalTrajectory;
import com.gyf.barlibrary.ImmersionBar;
import com.kaopiz.kprogresshud.KProgressHUD;
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
        setStatusBar();
    }

    /**
     * 设置状态栏颜色
     */
    public void setStatusBar() {
        ImmersionBar.with(this).statusBarColor(R.color.colorPrimary).keyboardEnable(true)
                .statusBarDarkFont(true) .fitsSystemWindows(true).init();
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
        AutoSize.autoConvertDensityOfGlobal(this);
//        友盟统计
        MobclickAgent.onResume(this);
//        腾讯分析
        StatService.onResume(this);
        totalPersonalTrajectory = new TotalPersonalTrajectory(this);
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

    @Override
    protected void onPause() {
        super.onPause();
        //        友盟统计
        MobclickAgent.onPause(this);
//        腾讯移动分析
        StatService.onPause(this);

        Jzvd.releaseAllVideos();
        //        避免播放 置于后台，释放滚动
        EventBus.getDefault().post(new EventMessage(START_AUTO_PAGE_TURN,START_AUTO_PAGE_TURN));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
        if (EventBus.getDefault().isRegistered(BaseFragmentActivity.this)) {
            EventBus.getDefault().unregister(this);
        }
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
