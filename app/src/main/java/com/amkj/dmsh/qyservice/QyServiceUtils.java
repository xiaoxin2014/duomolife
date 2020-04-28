package com.amkj.dmsh.qyservice;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.OrderProductNewBean;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.bean.QuickEntryEntity;
import com.amkj.dmsh.mine.bean.QuickEntryEntity.QuickEntryBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.MainOrderListEntity;
import com.amkj.dmsh.shopdetails.bean.MainOrderListEntity.MainOrderBean;
import com.amkj.dmsh.utils.SharedPreUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.OnMessageItemClickListener;
import com.qiyukf.unicorn.api.ProductDetail;
import com.qiyukf.unicorn.api.QuickEntry;
import com.qiyukf.unicorn.api.QuickEntryListener;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.UICustomization;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.UnreadCountChangeListener;
import com.qiyukf.unicorn.api.YSFOptions;
import com.qiyukf.unicorn.api.YSFUserInfo;
import com.qiyukf.unicorn.api.lifecycle.SessionLifeCycleOptions;
import com.qiyukf.unicorn.api.msg.MessageService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getVersionName;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/27
 * version 3.1.6
 * class description:七鱼客服
 */
public class QyServiceUtils {

    private static volatile QyServiceUtils qyServiceUtils;
    private boolean isQyInit;
    private UnreadCountChangeListener listener;
    private List<MainOrderBean> orderListBeanList = new ArrayList<>();
    private Map<String, String> linkMap = new HashMap<>();
    private ProductIndentHelper productIndentHelper;
    private YSFOptions ysfOptions;
    private UICustomization uiCustomization;
    private final String qyAppKey = "ef251a87b903f9fd6938caafbdf0a9de";
    private final String qySerectKey = "1280d1127f99ac5ad360e79561a390b9";


    private QyServiceUtils() {
    }

    public static QyServiceUtils getQyInstance() {
        if (qyServiceUtils == null) {
            synchronized (QyServiceUtils.class) {
                if (qyServiceUtils == null) {
                    qyServiceUtils = new QyServiceUtils();
                }
            }
        }
        return qyServiceUtils;
    }

    /**
     * 初始化网易七鱼
     */
    public void initQyService(Context context) {
        if (!isQyInit) {
            isQyInit = Unicorn.init(context.getApplicationContext(), qyAppKey, QyOptions(), new QYGlideImageLoader(context.getApplicationContext()));
        }
    }

    private YSFOptions QyOptions() {
        getYsOptions();
        getUICustomization();
        StatusBarNotificationConfig statusBarNotificationConfig = new StatusBarNotificationConfig();
        //配置七鱼点击通知栏
        statusBarNotificationConfig.notificationEntrance = QyServiceNotifyReceiver.class;
        ysfOptions.statusBarNotificationConfig = statusBarNotificationConfig;
        //链接点击设置
        ysfOptions.onMessageItemClickListener = new OnMessageItemClickListener() {
            @Override
            public void onURLClicked(Context context, String url) {
                setSkipPath(context, url, false);
            }
        };
        //快捷入口点击
        ysfOptions.quickEntryListener = new QuickEntryListener() {
            @Override
            public void onClick(Context context, String shopId, QuickEntry quickEntry) {
                if (quickEntry.getId() == 1) {
                    if (orderListBeanList.size() > 0) {
                        createProductIndentDialog(context);
                    } else {
                        showToast("暂无订单数据");
                    }
                } else {
                    setSkipPath(context, linkMap.get(String.valueOf(quickEntry.getId())), false);
                }
            }
        };
        uiCustomization.hideKeyboardOnEnterConsult = true;
        uiCustomization.avatarShape = 0;
        ysfOptions.uiCustomization = uiCustomization;
        return ysfOptions;
    }

    /**
     * 获取七鱼配置
     *
     * @return
     */
    private void getYsOptions() {
        if (ysfOptions == null) {
            ysfOptions = new YSFOptions();
        }
    }

    /**
     * 获取自定义布局
     *
     * @return
     */
    private void getUICustomization() {
        if (uiCustomization == null) {
            uiCustomization = new UICustomization();
        }
    }

    /**
     * 订单弹窗
     *
     * @param context
     */
    private void createProductIndentDialog(Context context) {
        if (productIndentHelper == null) {
            productIndentHelper = new ProductIndentHelper(context);
        }
        productIndentHelper.show();
    }

    /**
     * 打开客服页面 默认
     */
    public void openQyServiceChat(Context context) {
        openQyServiceChat(context, null);
    }

    public void openQyServiceChat(Context context, String sourceTitle) {
        openQyServiceChat(context, sourceTitle, null);
    }

    /**
     * @param context     上下文
     * @param sourceTitle 聊天窗口的标题
     * @param sourceUrl   咨询的发起来源，包括发起咨询的url，title，描述信息等
     */
    public void openQyServiceChat(Context context, String sourceTitle, String sourceUrl) {
        openQyServiceChat(context, sourceTitle, sourceUrl, null);
    }

    public void openQyServiceChat(Context context, String sourceTitle, String sourceUrl, QyProductIndentInfo qyProductIndentInfo) {
//            是否已初始化
        initQyService(context);
        ConsultSource pageSource;
        if (!TextUtils.isEmpty(sourceTitle) || !TextUtils.isEmpty(sourceUrl)) {
            /**
             * 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入。
             * 三个参数分别为：来源页面的url，来源页面标题，来源页面额外信息（保留字段，暂时无用）。
             * 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
             */
            pageSource = new ConsultSource(sourceUrl, sourceTitle, "");
        } else {
            pageSource = new ConsultSource(null, null, null);
        }

        if (qyProductIndentInfo != null) {
            pageSource.productDetail = new ProductDetail.Builder()
                    .setPicture(getStrings(qyProductIndentInfo.getPicUrl()))
                    .setTitle(getStrings(qyProductIndentInfo.getTitle()))
                    .setUrl(getStrings(qyProductIndentInfo.getUrl()))
                    .setDesc(getStrings(qyProductIndentInfo.getDesc()))
                    .setNote(getStrings(qyProductIndentInfo.getNote()))
                    .setAlwaysSend(true)
                    .setShow(1)
                    .setSendByUser(false)
                    .build();
        }
        pageSource.quickEntryList = new ArrayList<>();
        //默认添加订单查询快捷入口
        if (userId > 0) {
            pageSource.quickEntryList.add(new QuickEntry(1, "订单查询", ""));
        }

        String quickEntry = (String) SharedPreUtils.getParam("QuickEntry", "");
        if (!TextUtils.isEmpty(quickEntry)) {
            List<QuickEntryBean> datas = new Gson().fromJson(quickEntry, new TypeToken<List<QuickEntryBean>>() {
            }.getType());

            if (datas != null && datas.size() > 0) {
                linkMap.clear();
                for (int i = 0; i < datas.size(); i++) {
                    QuickEntryBean quickEntryBean = datas.get(i);
                    pageSource.quickEntryList.add(new QuickEntry(i + 2, quickEntryBean.getTitle(), ""));
                    linkMap.put(String.valueOf(i + 2), quickEntryBean.getLink());
                }
            }
        }
//        } else {
//            pageSource.quickEntryList.add(new QuickEntry(2, "热销爆品", ""));
//            pageSource.quickEntryList.add(new QuickEntry(3, "精选专题", ""));
//            pageSource.quickEntryList.add(new QuickEntry(4, "优惠特价", ""));
//            pageSource.quickEntryList.add(new QuickEntry(5, "小编推荐", ""));
//            pageSource.quickEntryList.add(new QuickEntry(6, "新品发布", ""));
//        }

        //获取快捷入口并保存到本地
        getCustomerServiceBar(context);
        //获取订单前5条数据
        getIndentData(context);

        //排队设置
        SessionLifeCycleOptions lifeCycleOptions = new SessionLifeCycleOptions();
        lifeCycleOptions.setCanCloseSession(true)
                .setCanQuitQueue(true)
                .setQuitQueuePrompt("是否确定退出排队？");
        pageSource.sessionLifeCycleOptions = lifeCycleOptions;
        /**
         * 请注意： 调用该接口前，应先检查Unicorn.isServiceAvailable()，
         * 如果返回为false，该接口不会有任何动作
         *
         * @param context 上下文
         * @param title   聊天窗口的标题
         * @param source  咨询的发起来源，包括发起咨询的url，title，描述信息等
         */
        if (isQyInit) {
            Unicorn.openServiceActivity(context, getStrings(sourceTitle), pageSource);
        } else {
            showToast("客服信息错误");
        }
        productIndentHelper = null;
    }

    /**
     * 监听客服消息条数
     *
     * @param listener 未读消息监听
     */
    public void getServiceCount(UnreadCountChangeListener listener) {
        this.listener = listener;
        Unicorn.addUnreadCountChangeListener(listener, true);
    }

    /**
     * 获取客服未读消息
     *
     * @return
     */
    public int getServiceTotalCount() {
        return Unicorn.getUnreadCount();
    }

    /**
     * 取消客服消息通知
     */
    public void cancelServiceCount() {
        if (listener != null) {
            Unicorn.addUnreadCountChangeListener(listener, false);
        }
    }

    /**
     * 清除文件缓存，将删除SDK接收过的所有文件。<br>
     * 建议在工作线程中执行该操作。
     */
    public void clearQyCache() {
        if (isQyInit) {
            Unicorn.clearCache();
        }
    }

    public void loginQyUserInfo(Context context, int userId, String nickName, String mobile, String avatar) {
        //            是否已初始化
        initQyService(context);
        String osVersion = Build.VERSION.RELEASE;
//        手机型号
        String mobileModel = Build.MODEL;
        YSFUserInfo userInfo = new YSFUserInfo();
        JSONArray array = new JSONArray();
        if (userId > 0) {
            userInfo.userId = String.valueOf(userId);
            array.put(userInfoDataItem("real_name", getStrings(nickName), -1, null, null));
            array.put(userInfoDataItem("mobile_phone", getStrings(mobile), -1, null, null));
//            array.add(userInfoDataItem("email", email, -1,null, null)); // email
            array.put(userInfoDataItem("avatar", getStrings(avatar), -1, null, null));
            if (!TextUtils.isEmpty(avatar)) {
                uiCustomization.rightAvatar = avatar;
            }
        }
        array.put(userInfoDataItem("system_version", osVersion, 0, "系统版本", null));
        array.put(userInfoDataItem("app_version", getVersionName(context), 1, "app版本", null));
        array.put(userInfoDataItem("mobile_model", mobileModel, 2, "手机型号", null));
        userInfo.data = array.toString();
        Unicorn.setUserInfo(userInfo);
    }

    private JSONObject userInfoDataItem(String key, Object value, int index, String label, String href) {
        JSONObject userInfo = null;
        try {
            userInfo = new JSONObject();
            userInfo.put("key", key);
            userInfo.put("value", value);
            if (index >= 0) {
                userInfo.put("index", index);
            }
            if (!TextUtils.isEmpty(label)) {
                userInfo.put("label", label);
            }
            if (!TextUtils.isEmpty(href)) {
                userInfo.put("href", href);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    /**
     * 注销用户
     *
     * @param context
     */
    public void logoutQyUser(Context context) {
        initQyService(context);
        uiCustomization.rightAvatar = "";
        Unicorn.logout();
    }


    class ProductIndentHelper {
        private ProductIndentAdapter productIndentAdapter;
        private final AlertDialog imageAlertDialog;
        private View indentDialogView;
        private Context context;

        public ProductIndentHelper(Context context) {
            this.context = context;
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.service_dialog_theme);
            indentDialogView = LayoutInflater.from(context).inflate(R.layout.alert_qy_indent, null, false);
            RecyclerView communal_recycler = indentDialogView.findViewById(R.id.communal_recycler);
            communal_recycler.setLayoutManager(new LinearLayoutManager(context));
            productIndentAdapter = new ProductIndentAdapter(context, orderListBeanList);
            communal_recycler.setAdapter(productIndentAdapter);
            ImageView iv_close_dialog = indentDialogView.findViewById(R.id.iv_close_dialog);
            iv_close_dialog.setOnClickListener(v -> dismiss());
            communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                    // 设置分隔线资源ID
                    .setDividerId(R.drawable.item_divider_gray_f_one_px)
                    .create());
            productIndentAdapter.setOnItemClickListener((adapter, view, position) -> {
                MainOrderBean mainOrderBean = (MainOrderBean) view.getTag();
                if (mainOrderBean != null) {
                    String picUrl = "";
                    String title = "";
                    List<OrderProductNewBean> orderProductList = mainOrderBean.getOrderProductList();
                    if (orderProductList != null && orderProductList.size() > 0) {
                        OrderProductNewBean orderProductNewBean = orderProductList.get(0);
                        picUrl = orderProductNewBean.getPicUrl();
                        title = orderProductNewBean.getProductName();
                    }
                    ProductDetail productDetail = new ProductDetail.Builder()
                            .setPicture(picUrl)
                            .setTitle(title)
                            .setUrl(Url.BASE_SHARE_PAGE_TWO + "m/template/order_template/order.html?noid=" + mainOrderBean.getOrderNo())
                            .setDesc(mainOrderBean.getStatusText())
                            .setNote(String.format(context.getResources().getString(R.string.money_price_chn), mainOrderBean.getPayAmount()))
                            .setShow(1)
                            .setSendByUser(false)
                            .build();
                    MessageService.sendProductMessage(productDetail);
                    productIndentHelper.dismiss();
                }
            });
            builder.setCancelable(true);
            imageAlertDialog = builder.create();
            imageAlertDialog.show();
            Window window = imageAlertDialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(R.color.translucence);
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = AutoSizeUtils.mm2px(mAppContext, 600);
                window.setGravity(Gravity.BOTTOM);
                window.setAttributes(params);
                window.setContentView(indentDialogView);
            }
        }

        public void show() {
            if (imageAlertDialog != null && !imageAlertDialog.isShowing()) {
                imageAlertDialog.show();
            }
        }

        public void dismiss() {
            if (imageAlertDialog != null && isContextExisted(context)) {
                imageAlertDialog.dismiss();
            }
        }
    }

    //获取快捷入口
    private void getCustomerServiceBar(Context context) {
        NetLoadUtils.getNetInstance().loadNetDataPost(context, Url.GET_CUSTOMER_SERVICE_BAR, null, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                QuickEntryEntity quickEntryEntity = new Gson().fromJson(result, QuickEntryEntity.class);
                if (quickEntryEntity != null) {
                    List<QuickEntryBean> list = quickEntryEntity.getList();
                    if (list != null && list.size() > 0) {
//                        List<QuickEntry> quickEntryList = new ArrayList<>();
                        SharedPreUtils.setParam("QuickEntry", new Gson().toJson(list));
                    }
                }
            }
        });
    }

    //获取订单列表数据
    private void getIndentData(Context context) {
        if (userId == 0) return;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", 1);
        params.put("showCount", 5);
        params.put("orderStatusText", "all");
        NetLoadUtils.getNetInstance().loadNetDataPost(context, Url.Q_GET_ORDER_LIST, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                orderListBeanList.clear();
                Gson gson = new Gson();
                MainOrderListEntity mOrderListNewEntity = gson.fromJson(result, MainOrderListEntity.class);
                if (mOrderListNewEntity != null) {
                    List<MainOrderListEntity.MainOrderBean> orderList = mOrderListNewEntity.getResult();
                    if (orderList != null && orderList.size() > 0) {
                        orderListBeanList.addAll(orderList);
                    }
                }
            }
        });
    }
}
