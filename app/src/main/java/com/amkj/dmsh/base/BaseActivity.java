package com.amkj.dmsh.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.BuildConfig;
import com.amkj.dmsh.R;
import com.amkj.dmsh.dominant.activity.QualityGroupShopDetailActivity;
import com.amkj.dmsh.find.activity.TopicDetailActivity;
import com.amkj.dmsh.homepage.activity.ArticleOfficialActivity;
import com.amkj.dmsh.netloadpage.NetEmptyCallback;
import com.amkj.dmsh.netloadpage.NetLoadCallback;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.utils.TimeUtils;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.gyf.barlibrary.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.kingja.loadsir.core.Transport;
import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.enums.ShowPattern;
import com.lzf.easyfloat.permission.PermissionUtils;
import com.melnykov.fab.FloatingActionButton;
import com.qiyukf.unicorn.api.msg.MsgTypeEnum;
import com.qiyukf.unicorn.api.msg.UnicornMessage;
import com.qiyukf.unicorn.ui.activity.ServiceMessageActivity;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jzvd.JzvdStd;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeCompat;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantVariable.RECEIVED_NEW_QY_MESSAGE;


public abstract class BaseActivity extends AppCompatActivity {
    public KProgressHUD loadHud;
    public LoadService loadService;
    public Map<String, Object> commonMap = new HashMap<>();
    private String sourceType;//广告位来源sourceType
    private String sourceId;//广告位来源sourceId
    private int scrollY;
    private AlertDialogHelper mAlertDialogQyMsg;
    private Unbinder mBind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        mBind = ButterKnife.bind(this);
        if (BuildConfig.DEBUG) Log.d("className", getSimpleName());
        // 注册当前Activity为订阅者
        EventBus eventBus = EventBus.getDefault();
        eventBus.register(this);
        loadHud = KProgressHUD.create(this)
                .setCancellable(true)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setSize(AutoSizeUtils.mm2px(mAppContext, 50), AutoSizeUtils.mm2px(mAppContext, 50));
//                .setDimAmount(0.5f)
        //Api通用参数初始化
        commonMap.put("reqId", UUID.randomUUID().toString().replaceAll("-", ""));
        //获取从广告位进入埋点参数
        if (getIntent() != null) {
            Intent intent = getIntent();
            sourceType = intent.getStringExtra("sourceType");
            sourceId = intent.getStringExtra("sourceId");
        }
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
            String hintText = "";
            int resId = R.drawable.net_page_bg;
            switch (getSimpleName()) {
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
                    resId = R.drawable.editor_message;
                    break;
                case "CouponProductActivity":
                    hintText = "暂无可用券商品";
                    break;
                case "ShopCarActivity":
                    resId = R.drawable.cart_empty_icon;
                    break;
                case "SearchCouponGoodsActivity":
                    hintText = "没有找到相关商品\n建议您换个搜索词试试";
                    resId = R.drawable.search_detail;
                    break;
                default:
                    hintText = "暂无数据，稍后重试";
                    break;
            }
            String finalHintText = hintText;
            int finalResId = resId;
            loadService.setCallBack(NetEmptyCallback.class, new Transport() {
                @Override
                public void order(Context context, View view) {
                    ImageView iv_communal_pic = view.findViewById(R.id.iv_communal_pic);
                    iv_communal_pic.setImageResource(finalResId);
                    TextView tv_communal_net_tint = view.findViewById(R.id.tv_communal_net_tint);
                    tv_communal_net_tint.setText(finalHintText);
                }
            });
        }
        setStatusBar();
        initViews();
        loadData();
    }

    /**
     * 设置状态栏颜色
     */
    public void setStatusBar() {
        if (this instanceof ShopScrollDetailsActivity || this instanceof TopicDetailActivity || this instanceof ArticleOfficialActivity || this instanceof QualityGroupShopDetailActivity) {
            ImmersionBar.with(this).keyboardEnable(true).navigationBarEnable(false).statusBarDarkFont(true).fullScreen(true).init();
        } else {
            //设置共同沉浸式样式
            ImmersionBar.with(this).statusBarColor(R.color.colorPrimary).keyboardEnable(true).navigationBarEnable(false).statusBarDarkFont(true).fitsSystemWindows(true).init();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AutoSize.autoConvertDensityOfGlobal(this);
//        友盟统计
        MobclickAgent.onResume(this);
//        腾讯分析
        StatService.onResume(this);
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
    }

    @Override
    protected void onStop() {
        super.onStop();

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

        try {
            //收到新的七鱼客服消息
            if (message.type.equals(RECEIVED_NEW_QY_MESSAGE) && message.result != null) {
                UnicornMessage unicornMessage = (UnicornMessage) message.result;
                //MsgTypeEnum.text
                MsgTypeEnum msgType = unicornMessage.getMsgType();
                long msgTime = System.currentTimeMillis() - unicornMessage.getTime();
                String content = unicornMessage.getContent();
                newQyMessageComming(msgType, msgTime, content, "app://ManagerServiceChat");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaseActivity.this.postEventResult(message);
    }

    protected void newQyMessageComming(MsgTypeEnum msgType, long msgTime, String content, String link) {
        if (!EasyFloat.appFloatIsShow()) {
            Activity last = ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getActivityLinkedList().getLast();
            if (getSimpleName().equals(last.getClass().getSimpleName())) {
                //检测是否获取悬浮窗权限
                if (!PermissionUtils.checkPermission(this)) {
                    if (mAlertDialogQyMsg == null) {
                        mAlertDialogQyMsg = new AlertDialogHelper(getActivity());
                        mAlertDialogQyMsg.setTitle("通知提示")
                                .setMsg("您有新的客服消息，打开“多么生活”悬浮窗功能可实时接收")
                                .setSingleButton(true)
                                .setConfirmText("去设置");
                    }
                    mAlertDialogQyMsg.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                        @Override
                        public void confirm() {
                            //申请悬浮窗权限
                            PermissionUtils.requestPermission(getActivity(), allow -> {
                                if (allow) {
                                    showQYMessage(msgType, msgTime, content, link);
                                }
                            });
                        }

                        @Override
                        public void cancel() {
                        }
                    });

                    mAlertDialogQyMsg.show();
                } else {
                    showQYMessage(msgType, msgTime, content, link);
                }
            }
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
        super.onConfigurationChanged(newConfig);
        // 如果你的app可以横竖屏切换，并且适配4.4或者emui3手机请务必在onConfigurationChanged方法里添加这句话
//        setStatusBar();
    }

    @Override
    public Resources getResources() {
        try {
            AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources());//如果没有自定义需求用这个方法
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.getResources();
    }

    protected abstract int getContentView();

    protected abstract void initViews();

    /**
     * 获取loadView
     */
    public View getLoadView() {
        return null;
    }


    /**
     * 获取顶部view(用于分享封面图)
     */
    public View getTopView() {
        return null;
    }

    /**
     * 是否默认加载
     */
    protected boolean isAddLoad() {
        return false;
    }


    /**
     * 添加悬浮置顶按钮
     */
    protected void setFloatingButton(FloatingActionButton floatingActionButton, View view) {
        int screenHeight = ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getScreenHeight();
        if (view instanceof RecyclerView) {
            ((RecyclerView) view).addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    scrollY += dy;
                    if (!recyclerView.canScrollVertically(-1)) {
                        scrollY = 0;
                    }
                    if (scrollY > screenHeight * 1.5 && dy < 0) {
                        if (floatingActionButton.getVisibility() == GONE) {
                            floatingActionButton.setVisibility(VISIBLE);
                            floatingActionButton.hide(false);
                        }
                        if (!floatingActionButton.isVisible()) {
                            floatingActionButton.show();
                        }
                    } else {
                        if (floatingActionButton.isVisible()) {
                            floatingActionButton.hide();
                        }
                    }
                }
            });
            floatingActionButton.setOnClickListener(v -> {
                scrollY = 0;
                ((RecyclerView) view).stopScroll();
                ((RecyclerView) view).scrollToPosition(0);
                floatingActionButton.hide();
            });
        }
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
        if (mBind != null) {
            mBind.unbind();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        // 必须调用该方法，防止内存泄漏
        ImmersionBar.with(this).destroy();
        if (loadHud != null) {
            loadHud.dismiss();
        }
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

    public String getSimpleName() {
        return getClass().getSimpleName();
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }

    public String getSourceType() {
        return sourceType;
    }

    public String getSourceId() {
        return sourceId;
    }

    protected void showQYMessage(MsgTypeEnum msgType, long msgTime, String content, String link) {
        EasyFloat.with(getActivity())
                // 设置浮窗xml布局文件，并可设置详细信息
                .setLayout(R.layout.layout_qy_float_msg, view -> {
                    TextView tvTime = view.findViewById(R.id.tv_time);
                    TextView tvContent = view.findViewById(R.id.tv_content);
                    int[] time = TimeUtils.getDayHourMinuteSecond(Math.abs(msgTime));
                    if (time != null) {
                        if (time[0] > 0) {
                            tvTime.setText(time[0] + "天前");
                        } else if (time[1] > 0) {
                            tvTime.setText(time[1] + "小时前");
                        } else if (time[2] > 0) {
                            tvTime.setText(time[2] + "分钟前");
                        } else if (time[3] >= 10) {
                            tvTime.setText(time[3] + "秒前");
                        } else {
                            tvTime.setText("刚刚");
                        }
                    }
                    tvContent.setText(msgType == MsgTypeEnum.text ? content : "您有新消息待查看！");
                    view.setOnClickListener(v -> {
                        EasyFloat.dismissAppFloat();
                        setSkipPath(getActivity(), link, false);
                    });
                })
                // 设置浮窗显示类型，默认只在当前Activity显示，可选一直显示、仅前台显示、仅后台显示
                .setShowPattern(ShowPattern.FOREGROUND)
                // 设置浮窗是否可拖拽，默认可拖拽
                .setDragEnable(true)
                // 设置浮窗的对齐方式和坐标偏移量
                .setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, AutoSizeUtils.mm2px(getActivity(), 120))
                // 设置宽高是否充满父布局，直接在xml设置match_parent属性无效
                .setMatchParent(false, false)
                //客服页面时不用显示弹窗
                .setFilter(ServiceMessageActivity.class)
                // 浮窗的一些状态回调，如：创建结果、显示、隐藏、销毁、touchEvent、拖拽过程、拖拽结束。
                // ps：通过Kotlin DSL实现的回调，可以按需复写方法，用到哪个写哪个
                .show();

        new Handler().postDelayed(EasyFloat::dismissAppFloat, 3000);
    }
}
