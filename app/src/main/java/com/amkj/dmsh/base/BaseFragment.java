package com.amkj.dmsh.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.TotalPersonalTrajectory;
import com.amkj.dmsh.netloadpage.NetEmptyCallback;
import com.amkj.dmsh.netloadpage.NetLoadCallback;
import com.amkj.dmsh.network.NetLoadUtils;
import com.gyf.barlibrary.ImmersionFragment;
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jzvd.JzvdStd;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantVariable.START_AUTO_PAGE_TURN;

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
//    控件是否初始化完成
    protected boolean isInitView = false;
    //    是否加载过数据
    protected boolean isLoadData = false;
    public TotalPersonalTrajectory totalPersonalTrajectory;
    public LoadService loadService;

    public static <T extends BaseFragment> T newInstance(Class<?> subFragmentCls, Map<String, String> params, Map<String, Object> objectParams) {
        try {
            Constructor constructor =
                    subFragmentCls.getConstructor(new Class[]{});

            T fragment = (T) constructor.newInstance(new Object[]{});

            Bundle bundle = new Bundle();

            if (params != null && params.size() > 0 && objectParams != null && objectParams.size() > 0) {
                for (String key : params.keySet()) {
                    String value = params.get(key);
                    bundle.putString(key, value);
                }
                for (String key : objectParams.keySet()) {
                    Object value = objectParams.get(key);
                    if (value instanceof List) {
                        bundle.putParcelableArrayList(key, (ArrayList<? extends Parcelable>) value);
                    } else {
                        bundle.putParcelable(key, (Parcelable) value);
                    }
                }
            } else if (params != null && params.size() > 0) {
                for (String key : params.keySet()) {
                    String value = params.get(key);
                    bundle.putString(key, value);
                }
            } else if (objectParams != null && objectParams.size() > 0) {
                for (String key : objectParams.keySet()) {
                    Object value = objectParams.get(key);
                    if (value instanceof List) {
                        bundle.putParcelableArrayList(key, (ArrayList<? extends Parcelable>) value);
                    } else {
                        bundle.putParcelable(key, (Parcelable) value);
                    }
                }
            }
            fragment.setArguments(bundle);
            return fragment;

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
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
        loadHud = KProgressHUD.create(getActivity())
                .setCancellable(false)
                .setSize(AutoSizeUtils.mm2px(mAppContext,50), AutoSizeUtils.mm2px(mAppContext,50))
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
        // 注册当前Fragment为订阅者
        EventBus eventBus = EventBus.getDefault();
        if(eventBus.isRegistered(this)){
            eventBus.register(this);
        }
        isInitView = true;
        isCanLoadData();
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
        isCanLoadData();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInitView = false;
        isLoadData = false;
        if (EventBus.getDefault().isRegistered(getActivity())) {
            EventBus.getDefault().unregister(this);
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

    /**
     * 分类 海外直邮活动专区
     *
     * @param hidden 是否隐藏
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (totalPersonalTrajectory != null) {
                switch (getClass().getSimpleName()) {
                    case "QualityNormalFragment":
                    case "QualityOverseasMailFragment":
                        break;
                    default:
                        totalPersonalTrajectory.stopTotal();
                        break;
                }
            }
        } else {
            totalPersonalTrajectory = new TotalPersonalTrajectory(this.getContext(), this.getClass().getSimpleName());
        }
    }

    /**
     * 是否可加载数据
     */
    private void isCanLoadData() {
        if (!isInitView) {
            return;
        }
        if (getUserVisibleHint() && !isLoadData) {
            loadData();
            isLoadData = true;
        } else if (isLoadData) {
            stopLoadData();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(loadService != null&&
                loadService.getCurrentCallback().getName().equals(NetLoadCallback.class.getName())){
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
        //        避免播放 置于后台，释放滚动
        EventBus.getDefault().post(new EventMessage(START_AUTO_PAGE_TURN,START_AUTO_PAGE_TURN));
        if (totalPersonalTrajectory != null) {
            totalPersonalTrajectory.stopTotal();
        }
    }

    @Override
    public void onVisible() {
        AutoSize.autoConvertDensityOfGlobal(getActivity());
        totalPersonalTrajectory = new TotalPersonalTrajectory(getContext(), getClass().getSimpleName());
    }
}
