package com.amkj.dmsh.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.BuildConfig;
import com.amkj.dmsh.R;
import com.amkj.dmsh.netloadpage.NetEmptyCallback;
import com.amkj.dmsh.netloadpage.NetLoadCallback;
import com.amkj.dmsh.network.NetLoadUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.ImmersionFragment;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.kingja.loadsir.core.Transport;
import com.melnykov.fab.FloatingActionButton;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jzvd.JzvdStd;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;

public abstract class BaseFragment extends ImmersionFragment {

    private Unbinder mUnBinder;

    /**
     * 构造BaseFragment子类实例对象,子类务必实现空参构造方法
     *
     * @param subFragmentCls 子类Fragment类型
     * @param params         参数，以Map的形式传递，可空
     * @param <T>
     * @return
     */
    /**
     * 懒加载机制
     */
    protected boolean isViewInitiated; //是否初始化过布局
    protected boolean isVisibleToUser; //当前界面对用户是否可见
    protected boolean isDataInitiated; //是否已经加载过数据（保证数据只会加载一次）
    protected boolean isLazy = true; //当前Fragment是否需要使用懒加载方式
    public LoadService loadService;
    protected int scrollY;

    public static <T extends BaseFragment> T newInstance(Class<?> subFragmentCls, Map<String, String> params, Map<String, Object> objectParams) {
        try {
            Constructor constructor =
                    subFragmentCls.getConstructor(new Class[]{});

            T fragment = (T) constructor.newInstance(new Object[]{});

            Bundle bundle = new Bundle();
            if (params != null && params.size() > 0) {
                for (String key : params.keySet()) {
                    String value = params.get(key);
                    bundle.putString(key, value);
                }
            }
            if (objectParams != null && objectParams.size() > 0) {
                for (String key : objectParams.keySet()) {
                    Object value = objectParams.get(key);
                    if (value instanceof List) {
                        bundle.putParcelableArrayList(key, (ArrayList<? extends Parcelable>) value);
                    } else if (value instanceof String) {
                        bundle.putString(key, (String) value);
                    } else if (value instanceof Boolean) {
                        bundle.putBoolean(key, (Boolean) value);
                    } else if (value instanceof Integer) {
                        bundle.putInt(key, (int) value);
                    } else {
                        bundle.putParcelable(key, (Parcelable) value);
                    }
                }
            }
            fragment.setArguments(bundle);
            return fragment;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public KProgressHUD loadHud;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentView(), null);
        mUnBinder = ButterKnife.bind(this, view);
        if (BuildConfig.DEBUG) Log.d("className", getClass().getSimpleName());
        loadHud = KProgressHUD.create(getActivity())
                .setCancellable(false)
                .setSize(AutoSizeUtils.mm2px(mAppContext, 50), AutoSizeUtils.mm2px(mAppContext, 50))
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        if (isAddLoad()) {
            // 重新加载逻辑
            loadService = LoadSir.getDefault().register(view, new Callback.OnReloadListener() {
                @Override
                public void onReload(View v) {
                    // 重新加载逻辑
                    loadService.showCallback(NetLoadCallback.class);
                    loadData();
                }
            }, NetLoadUtils.getNetInstance().getLoadSirCover());
            String hintText;
            int resId = R.drawable.net_page_bg;
            switch (getClass().getSimpleName()) {
                case "DoMoIndentDeliveredFragment":
                case "DoMoIndentWaitAppraiseFragment":
                case "DoMoIndentWaitPayFragment":
                case "DoMoIndentWaitSendFragment":
                case "DoMoSalesReturnRecordFragment":
                case "DoMoSalesReturnReplyFragment":
                case "DuMoIndentAllFragment":
                    hintText = "你还没有订单\n赶快买买买";
                    break;
                case "IntegralIndentFragment":
                    hintText = "你还没有积分订单\n赶快买买买";
                    break;
                case "DirectMyCouponFragment":
                    hintText = "你的优惠券空空如也";
                    break;
                case "CollectInvitationFragment":
                case "CollectSpecialFragment":
                case "CollectTopicFragment":
                    hintText = "你还没有收藏内容\n赶快去收藏";
                    break;
                case "IntegralProductFragment":
                case "SpringSaleFragment":
                    hintText = "暂时没有商品哦";
                    break;
                case "SearchDetailsProductNewFragment":
                    hintText = "没有找到相关商品\n建议您换个搜索词试试";
                    resId = R.drawable.search_detail;
                    break;
                case "SearchDetailsArticleFragment":
                    hintText = "没有找到相关种草\n建议您换个搜索词试试";
                    resId = R.drawable.search_detail;
                    break;
                case "SearchDetailsTopicFragment":
                    hintText = "没有找到相关话题\n建议您换个搜索词试试";
                    resId = R.drawable.search_detail;
                    break;
                case "SearchDetailsUserFragment":
                    hintText = "没有找到相关用户\n建议您换个搜索词试试";
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
        // 注册当前Fragment为订阅者
        EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
        AutoSize.autoConvertDensityOfGlobal(getActivity());
        return loadService != null ? loadService.getLoadLayout() : view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 获取参数，子类实现
        getReqParams(getArguments());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            prepareFetchData();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        initViews();
        if (!isLazy()) {
            loadData();
        }
        prepareFetchData();
    }

    //判断懒加载条件
    public void prepareFetchData() {
        if (isVisibleToUser && isViewInitiated && isLazy() && !isDataInitiated()) {
            loadData();
            isDataInitiated = true;
        }
    }

    //判断当前fragment是否启用懒加载
    protected boolean isLazy() {
        return isLazy;
    }

    //判断是否每次可见时都加载数据
    protected boolean isDataInitiated() {
        return isDataInitiated;
    }


    /**
     * 获取参数，在子类中定义逻辑
     *
     * @param bundle
     */
    protected void getReqParams(Bundle bundle) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetResult(EventMessage message) {
        if (message == null) {
            return;
        }
        // 是否为Event消息
        if (message instanceof EventMessage) {
            BaseFragment.this.postEventResult((EventMessage) message);
        }
        // 其他类型消息
        else {
            BaseFragment.this.postOtherResult(message);
        }
    }

    // 传递EventBus事件类型结果，子类实现逻辑
    protected void postEventResult(@NonNull EventMessage message) {
    }

    // 传送其他结果，子类实现逻辑
    protected void postOtherResult(@NonNull Object message) {
    }

    /**
     * 是否默认加载
     *
     * @return
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
                    if (scrollY > screenHeight * 1.5) {
                        if (floatingActionButton.getVisibility() == GONE) {
                            floatingActionButton.setVisibility(VISIBLE);
                            floatingActionButton.hide(false);
                        }
                        if (!floatingActionButton.isVisible()) {
                            floatingActionButton.show(true);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewInitiated = false;
        isDataInitiated = false;
        if (EventBus.getDefault().isRegistered(getActivity())) {
            EventBus.getDefault().unregister(this);
        }
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        if (getView() != null) {
            unbindDrawables(getView());
        }
        ImmersionBar.with(this).destroy();
    }

    private void unbindDrawables(View view) {
        if (view == null) {
            return;
        }
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    @Override
    public void onPause() {
        StatService.trackEndPage(getActivity(), getClass().getName());
//        友盟统计
        MobclickAgent.onPageEnd(getClass().getName());
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        AutoSize.autoConvertDensityOfGlobal(getActivity());
//       fragment标记页面的开始
        StatService.trackBeginPage(getActivity(), getClass().getName());
//        友盟统计
        MobclickAgent.onPageStart(getClass().getName());
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
    public void initImmersionBar() {

    }

    @Override
    public boolean immersionBarEnabled() {
        return false;
    }

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    private void stopLoadData() {
    }

    protected abstract int getContentView();

    protected abstract void initViews();

    protected abstract void loadData();

    @Override
    public void onInvisible() {
        JzvdStd.releaseAllVideos();
    }

    @Override
    public void onVisible() {
        AutoSize.autoConvertDensityOfGlobal(getActivity());
    }

    protected String getSimpleName() {
        return getClass().getSimpleName();
    }
}
