package com.amkj.dmsh.qyservice;

import android.content.Context;
import android.os.Build;
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
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.google.gson.reflect.TypeToken;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.OnMessageItemClickListener;
import com.qiyukf.unicorn.api.ProductDetail;
import com.qiyukf.unicorn.api.QuickEntry;
import com.qiyukf.unicorn.api.QuickEntryListener;
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

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getVersionName;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;
import static com.amkj.dmsh.constant.ConstantMethod.isVip;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/27
 * version 3.1.6
 * class description:????????????
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
     * ?????????????????????
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
        //???????????????????????????
        statusBarNotificationConfig.notificationEntrance = QyServiceNotifyReceiver.class;
        ysfOptions.statusBarNotificationConfig = statusBarNotificationConfig;
        //??????????????????
        ysfOptions.onMessageItemClickListener = new OnMessageItemClickListener() {
            @Override
            public void onURLClicked(Context context, String url) {
                setSkipPath(context, url, false);
            }
        };
        //??????????????????
        ysfOptions.quickEntryListener = new QuickEntryListener() {
            @Override
            public void onClick(Context context, String shopId, QuickEntry quickEntry) {
                if (quickEntry.getId() == 1) {
                    if (orderListBeanList.size() > 0) {
                        createProductIndentDialog(context);
                    } else {
                        showToast("??????????????????");
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
     * ??????????????????
     *
     * @return
     */
    private void getYsOptions() {
        if (ysfOptions == null) {
            ysfOptions = new YSFOptions();
        }
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    private void getUICustomization() {
        if (uiCustomization == null) {
            uiCustomization = new UICustomization();
        }
    }

    /**
     * ????????????
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
     * ?????????????????? ??????
     */
    public void openQyServiceChat(Context context) {
        openQyServiceChat(context, null);
    }

    public void openQyServiceChat(Context context, String sourceTitle) {
        openQyServiceChat(context, sourceTitle, null);
    }

    /**
     * @param context     ?????????
     * @param sourceTitle ?????????????????????
     * @param sourceUrl   ?????????????????????????????????????????????url???title??????????????????
     */
    public void openQyServiceChat(Context context, String sourceTitle, String sourceUrl) {
        openQyServiceChat(context, sourceTitle, sourceUrl, null);
    }

    public void openQyServiceChat(Context context, String sourceTitle, String sourceUrl, QyProductIndentInfo qyProductIndentInfo) {
        //??????????????????
        initQyService(context);
        ConsultSource pageSource;
        if (!TextUtils.isEmpty(sourceTitle) || !TextUtils.isEmpty(sourceUrl)) {
            /**
             * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
             * ???????????????????????????????????????url????????????????????????????????????????????????????????????????????????????????????
             * ??????????????????????????????????????????"????????????"???????????????????????????????????????????????????
             */
            pageSource = new ConsultSource(sourceUrl, sourceTitle, "");
        } else {
            pageSource = new ConsultSource(null, null, null);
        }
        //????????????vip??????
        if (userId > 0 && isVip()) {
            pageSource.vipLevel = (int) SharedPreUtils.getParam("vipLevel", 1);
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
        //????????????????????????????????????
        if (userId > 0) {
            pageSource.quickEntryList.add(new QuickEntry(1, "????????????", ""));
        }

        String quickEntry = (String) SharedPreUtils.getParam("QuickEntry", "");
        if (!TextUtils.isEmpty(quickEntry)) {
            List<QuickEntryBean> datas = GsonUtils.fromJson(quickEntry, new TypeToken<List<QuickEntryBean>>() {
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

        //????????????????????????????????????
        getCustomerServiceBar(context);
        //???????????????5?????????
        getIndentData(context);

        //????????????
        SessionLifeCycleOptions lifeCycleOptions = new SessionLifeCycleOptions();
        lifeCycleOptions.setCanCloseSession(true)
                .setCanQuitQueue(true)
                .setQuitQueuePrompt("???????????????????????????");
        pageSource.sessionLifeCycleOptions = lifeCycleOptions;
        /**
         * ???????????? ?????????????????????????????????Unicorn.isServiceAvailable()???
         * ???????????????false?????????????????????????????????
         *
         * @param context ?????????
         * @param title   ?????????????????????
         * @param source  ?????????????????????????????????????????????url???title??????????????????
         */
        if (isQyInit) {
            Unicorn.openServiceActivity(context, getStrings(sourceTitle), pageSource);
        } else {
            showToast("??????????????????");
        }
        productIndentHelper = null;
    }

    /**
     * ????????????????????????
     *
     * @param listener ??????????????????
     */
    public void getServiceCount(UnreadCountChangeListener listener) {
        this.listener = listener;
        Unicorn.addUnreadCountChangeListener(listener, true);
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    public int getServiceTotalCount() {
        return Unicorn.getUnreadCount();
    }

    /**
     * ????????????????????????
     */
    public void cancelServiceCount() {
        if (listener != null) {
            Unicorn.addUnreadCountChangeListener(listener, false);
        }
    }

    /**
     * ??????????????????????????????SDK???????????????????????????<br>
     * ??????????????????????????????????????????
     */
    public void clearQyCache() {
        if (isQyInit) {
            Unicorn.clearCache();
        }
    }

    public void loginQyUserInfo(Context context, int userId, String nickName, String mobile, String avatar) {
        //            ??????????????????
        initQyService(context);
        String osVersion = Build.VERSION.RELEASE;
//        ????????????
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
        array.put(userInfoDataItem("system_version", osVersion, 0, "????????????", null));
        array.put(userInfoDataItem("app_version", getVersionName(context), 1, "app??????", null));
        array.put(userInfoDataItem("mobile_model", mobileModel, 2, "????????????", null));
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
     * ????????????
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
                    // ?????????????????????ID
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
                            .setUrl(Url.BASE_SHARE_PAGE_TWO + "order_template/order.html?noid=" + mainOrderBean.getOrderNo())
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

    //??????????????????
    private void getCustomerServiceBar(Context context) {
        NetLoadUtils.getNetInstance().loadNetDataPost(context, Url.GET_CUSTOMER_SERVICE_BAR, null, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                QuickEntryEntity quickEntryEntity = GsonUtils.fromJson(result, QuickEntryEntity.class);
                if (quickEntryEntity != null) {
                    List<QuickEntryBean> list = quickEntryEntity.getList();
                    if (list != null && list.size() > 0) {
//                        List<QuickEntry> quickEntryList = new ArrayList<>();
                        SharedPreUtils.setParam("QuickEntry", GsonUtils.toJson(list));
                    }
                }
            }
        });
    }

    //????????????????????????
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

                MainOrderListEntity mOrderListNewEntity = GsonUtils.fromJson(result, MainOrderListEntity.class);
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
