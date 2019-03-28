package com.amkj.dmsh.base;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.BuildConfig;
import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.TotalPersonalTrajectory;
import com.amkj.dmsh.netloadpage.NetEmptyCallback;
import com.amkj.dmsh.netloadpage.NetLoadCallback;
import com.amkj.dmsh.network.NetLoadUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.kingja.loadsir.core.Transport;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.tencent.stat.StatService;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import cn.jzvd.JzvdStd;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.START_AUTO_PAGE_TURN;


public abstract class BaseActivity extends RxAppCompatActivity {
    public KProgressHUD loadHud;
    public TotalPersonalTrajectory totalPersonalTrajectory;
    public LoadService loadService;
    public Map<String, Object> commonMap = new HashMap<>();
    private String mSimpleName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        if (BuildConfig.DEBUG) Log.d("className", getClass().getSimpleName());
        // 注册当前Activity为订阅者
        EventBus eventBus = EventBus.getDefault();
        eventBus.register(this);
        loadHud = KProgressHUD.create(this)
                .setCancellable(true)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setSize(AutoSizeUtils.mm2px(mAppContext, 50), AutoSizeUtils.mm2px(mAppContext, 50));
//                .setDimAmount(0.5f)
        //Api通用参数初始化
        commonMap.put("reqId", String.valueOf(System.currentTimeMillis()));
        // 重新加载逻辑
        if (isAddLoad()) {
            loadService = LoadSir.getDefault().register(getLoadView() != null ? getLoadView() : this, new Callback.OnReloadListener() {
                @Override
                public void onReload(View v) {
                    // 重新加载逻辑
                    loadService.showCallback(NetLoadCallback.class);
                    loadData();
                }
            }, NetLoadUtils.getNetInstance().getLoadSirCover());
            String hintText;
            mSimpleName = getClass().getSimpleName();
            switch (mSimpleName) {
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
                case "MessageSysMesActivity":
                    hintText = "最近20天没有通知消息哦";
                    break;
                case "MessageIndentActivity":
                    hintText = "最近20天没有订单消息哦";
                    break;
                case "MessageHotActivity":
                    hintText = "最近20天没有活动消息哦";
                    break;
                case "MessageCommentActivity":
                    hintText = "最近20天没有评论消息哦";
                    break;
                case "MessageLikedActivity":
                    hintText = "最近20天没有赞消息哦";
                    break;
                case "MineProductBrowsingHistoryActivity":
                    hintText = "最近暂无浏览记录哦";
                    break;
                case "EditorCommentActivity":
                    hintText = "快来留言吧~";
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
                    if ("EditorCommentActivity".equals(mSimpleName)) {
                        ImageView iv_communal_pic = view.findViewById(R.id.iv_communal_pic);
                        iv_communal_pic.setImageResource(R.drawable.editor_message);
                    }
                }
            });
        }
        initViews();
        loadData();
        setStatusBar();
    }

    /**
     * 设置状态栏颜色
     */
    public void setStatusBar() {
        ImmersionBar.with(this).statusBarColor(R.color.colorPrimary).keyboardEnable(true).navigationBarEnable(false)
                .statusBarDarkFont(true).fitsSystemWindows(true).init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AutoSize.autoConvertDensityOfGlobal(this);
//        友盟统计
        MobclickAgent.onResume(this);
//        腾讯分析
        StatService.onResume(this);
        if (totalPersonalTrajectory == null) {
            totalPersonalTrajectory = new TotalPersonalTrajectory(this);
        }
        if (ToastUtils.getToast() == null) {
            // 因为吐司只有初始化的时候才会判断通知权限有没有开启，根据这个通知开关来显示原生的吐司还是兼容的吐司
            ToastUtils.init(TinkerManager.getApplication());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        友盟统计
        MobclickAgent.onPause(this);
//        腾讯移动分析
        StatService.onPause(this);

        JzvdStd.releaseAllVideos();
//        避免播放 置于后台，释放滚动
        EventBus.getDefault().post(new EventMessage(START_AUTO_PAGE_TURN, START_AUTO_PAGE_TURN));
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
        if (JzvdStd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetResult(EventMessage message) {
        if (message == null) {
            return;
        }
        BaseActivity.this.postEventResult(message);

    }


    // 传递EventBus事件类型结果，子类实现逻辑
    protected void postEventResult(@NonNull EventMessage message) {
    }

    // 传送其他结果，子类实现逻辑
    protected void postOtherResult(@NonNull Object message) {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1) {//非默认值
            getResources();
        }
        super.onConfigurationChanged(newConfig);
        // 如果你的app可以横竖屏切换，并且适配4.4或者emui3手机请务必在onConfigurationChanged方法里添加这句话
        setStatusBar();
    }

    @Override
    public Resources getResources() {
        return super.getResources();
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (loadService != null &&
                loadService.getCurrentCallback().getName().equals(NetLoadCallback.class.getName())) {
            loadService.showSuccess();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        // 必须调用该方法，防止内存泄漏
        ImmersionBar.with(this).destroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // 如果通知栏的权限被手动关闭了
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled() &&
                !"SupportToast".equals(ToastUtils.getToast().getClass().getSimpleName())) {
            // 因为吐司只有初始化的时候才会判断通知权限有没有开启，根据这个通知开关来显示原生的吐司还是兼容的吐司
            ToastUtils.init(TinkerManager.getApplication());
            recreate();
        }
    }

    protected BaseActivity getActivity() {
        return this;
    }

    protected String getSimpleName() {
        return mSimpleName;
    }
}
