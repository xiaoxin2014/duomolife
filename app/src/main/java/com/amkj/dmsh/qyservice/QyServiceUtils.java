package com.amkj.dmsh.qyservice;

import android.content.Context;
import android.content.Intent;
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
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.activity.QualityNewProActivity;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
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

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getVersionName;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/27
 * version 3.1.6
 * class description:七鱼客服
 */
public class QyServiceUtils {

    private static QyServiceUtils qyServiceUtils;
    private boolean isQyInit;
    private UnreadCountChangeListener listener;
    private List<OrderListBean> orderListBeanList = new ArrayList<>();
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
        isQyInit = Unicorn.init(context, qyAppKey, QyOptions(), new QYGlideImageLoader(context.getApplicationContext()));
    }

    private YSFOptions QyOptions() {
        getYsOptions();
        getUICustomization();
        StatusBarNotificationConfig statusBarNotificationConfig = new StatusBarNotificationConfig();
//        配置七鱼点击通知栏
        statusBarNotificationConfig.notificationEntrance = QyServiceNotifyReceiver.class;
        ysfOptions.statusBarNotificationConfig = statusBarNotificationConfig;
        //        链接点击设置
        ysfOptions.onMessageItemClickListener = new OnMessageItemClickListener() {
            @Override
            public void onURLClicked(Context context, String url) {
                setSkipPath(context, url, false);
            }
        };
//        快捷入口点击
        ysfOptions.quickEntryListener = new QuickEntryListener() {
            @Override
            public void onClick(Context context, String shopId, QuickEntry quickEntry) {
                if (quickEntry.getId() == 1) {
                    if (orderListBeanList.size() > 0) {
                        createProductIndentDialog(context);
                    } else {
                        showToast(context, "暂无订单数据");
                    }
                } else if (quickEntry.getId() == 2) {
                    Intent intent = new Intent(context, QualityNewProActivity.class);
                    context.startActivity(intent);
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
                    .setPicture(qyProductIndentInfo.getPicUrl())
                    .setTitle(getStrings(qyProductIndentInfo.getTitle()))
                    .setUrl(qyProductIndentInfo.getUrl())
                    .setDesc(qyProductIndentInfo.getDesc())
                    .setNote(qyProductIndentInfo.getNote())
                    .setAlwaysSend(true)
                    .setUrl(getStrings(qyProductIndentInfo.getUrl()))
                    .setShow(1)
                    .setSendByUser(false)
                    .build();
        }
        pageSource.quickEntryList = new ArrayList<>();
        if (userId > 0) {
            pageSource.quickEntryList.add(new QuickEntry(1, "订单查询", ""));
        }
        pageSource.quickEntryList.add(new QuickEntry(2, "新品发布", ""));
        getIndentProductData();

//        排队设置
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
            showToast(context, "客服信息错误");
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
        Unicorn.clearCache();
    }

    public void loginQyUserInfo(Context context, int userId, String nickName, String mobile, String avatar) {
        logoutQyUser();
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
            uiCustomization.rightAvatar = avatar;
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
     */
    public void logoutQyUser() {
        uiCustomization.rightAvatar = "";
        Unicorn.logout();
    }

    /**
     * 上传订单数据
     *
     * @param indentJson 订单数据
     */
    public void upIndentData(Context context, String indentJson, String currentTime) {
//        String url = "https://qiyukf.com/openapi/order/upload";
//        long lTime = getDateMilliSecondSystemTime(currentTime) / 1000;
//        Map<String, Object> params = new HashMap<>();
//        params.put("appKey", qyAppKey);
//        params.put("time", lTime);
//        params.put("checksum", encodeMd5(qySerectKey,toMD5(encrypt(indentJson, qySerectKey)).toLowerCase(),String.valueOf(lTime)));
//        NetLoadUtils.getQyInstance().loadNetDataPost(context, url, params, new NetLoadUtils.NetLoadListener() {
//            @Override
//            public void onSuccess(String result) {
//                int code = 0;
//                String msg = "";
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    code = jsonObject.getInt("code");
//                    msg = (String) jsonObject.get("message");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                showToast(context, code + msg);
//            }
//
//            @Override
//            public void netClose() {
//
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                showToast(context, "订单上传失败");
//            }
//        });
    }

    /**
     * 使用AES算法对content加密
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     */
    public static String encrypt(String content, String encryptKey) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(encryptKey.getBytes());
            kgen.init(128, random);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
            return byteArr2HexStr(cipher.doFinal(content.getBytes("utf-8")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将byte数组转换为16进制值的字符串
     *
     * @param b 需要转换的byte数组
     * @return 转换后的字符串
     */
    private static String byteArr2HexStr(byte[] b) {
        int length = b.length;
        StringBuffer sb = new StringBuffer(length * 2);
        for (int i = 0; i < length; i++) {
            int temp = b[i];
            while (temp < 0) {
                temp = temp + 256;
            }
            if (temp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(temp, 16));
        }
        return sb.toString();
    }

    private final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private String encodeMd5(String appSecret, String nonce, String time) {
        String content = appSecret + nonce + time;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("sha1");
            messageDigest.update(content.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    class ProductIndentHelper {
        private ProductIndentAdapter productIndentAdapter;
        private final AlertDialog imageAlertDialog;

        public ProductIndentHelper(Context context) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.service_dialog_theme);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_communal_recycler, null, false);
            RecyclerView communal_recycler = view.findViewById(R.id.communal_recycler);
            communal_recycler.setLayoutManager(new LinearLayoutManager(context));
            productIndentAdapter = new ProductIndentAdapter(context, orderListBeanList);
            communal_recycler.setAdapter(productIndentAdapter);
            View indentHeaderView = LayoutInflater.from(context).inflate(R.layout.dialog_service_indent_header, null, false);
//            AutoUtils.auto(indentHeaderView);
            ImageView iv_close_dialog = indentHeaderView.findViewById(R.id.iv_close_dialog);
            iv_close_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                    // 设置分隔线资源ID
                    .setDividerId(R.drawable.item_divider_gray_f_two_px)
                    // 开启绘制分隔线，默认关闭
                    .enableDivider(true)
                    // 是否关闭标签点击事件，默认开启
                    .disableHeaderClick(false)
                    // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                    .setHeaderClickListener(null)
                    .create());
            productIndentAdapter.addHeaderView(indentHeaderView);
            productIndentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    OrderListBean orderListBean = (OrderListBean) view.getTag();
                    if (orderListBean != null) {
                        String picUrl = "";
                        String title = "";
                        if (orderListBean.getGoods() != null && orderListBean.getGoods().size() > 0) {
                            OrderListBean.GoodsBean goodsBean = orderListBean.getGoods().get(0);
                            picUrl = goodsBean.getPicUrl();
                            title = goodsBean.getName();
                        }
                        ProductDetail productDetail = new ProductDetail.Builder()
                                .setPicture(picUrl)
                                .setTitle(title)
                                .setUrl(Url.BASE_SHARE_PAGE_TWO + "m/template/order_template/order.html?noid=" + orderListBean.getNo())
                                .setDesc(INDENT_PRO_STATUS.get(String.valueOf(orderListBean.getStatus())))
                                .setNote(String.format(context.getResources().getString(R.string.money_price_chn), orderListBean.getAmount()))
                                .setShow(1)
                                .setSendByUser(false)
                                .build();
                        MessageService.sendProductMessage(productDetail);
                        productIndentHelper.dismiss();
                    }
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
//                params.height = AutoUtils.getPercentHeightSize(700);
                window.setGravity(Gravity.BOTTOM);
                window.setAttributes(params);
                window.setContentView(view);
            }
        }

        public void show() {
            if (!imageAlertDialog.isShowing()) {
                imageAlertDialog.show();
            }
        }

        public void dismiss() {
            imageAlertDialog.dismiss();
        }
    }

    /**
     * 获取订单数据
     */
    private void getIndentProductData() {
        String url = Url.BASE_URL + Url.Q_INQUIRY_ALL_ORDER;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("showCount", 5);
        params.put("currentPage", 1);
        params.put("orderType", "currency");
//        版本号控制 3 组合商品赠品
        params.put("version", 3);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                orderListBeanList.clear();
                String code = "";
                String msg = "";
                try {
                    JSONObject jsonObject = new org.json.JSONObject(result);
                    code = (String) jsonObject.get("code");
                    msg = (String) jsonObject.get("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code.equals(SUCCESS_CODE)) {
                    InquiryOrderEntry inquiryOrderEntry = new Gson().fromJson(result, InquiryOrderEntry.class);
                    INDENT_PRO_STATUS = inquiryOrderEntry.getOrderInquiryDateEntry().getStatus();
                    orderListBeanList.addAll(inquiryOrderEntry.getOrderInquiryDateEntry().getOrderList());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }

}
