package com.amkj.dmsh.constant;

import com.amkj.dmsh.BuildConfig;

import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/3/11
 * class description:变量
 */

public class ConstantVariable {

    public static final boolean isDebugTag = BuildConfig.DEBUG;

    public static final String I_TAG = "iTag";
    public static final String TAOBAO_PID = "mm_113346569_43964046_400008826";
    //    adzoneid
    public static final String TAOBAO_ADZONEID = "400008826";
    //    淘宝联盟appKey
    public static final String TAOBAO_APPKEY = "24840848";
    //   积分详情
    public static final String TAG_DETAILS_INTEG = "Integration_Details";
    //    默认图片名称
    public static final String DEFAULT_ADD_IMG = "plus_icon_nor.png";
    //    收到消息广播，发送刷新，更新消息
    public static final String REFRESH_MESSAGE_TOTAL = "RefreshMessageTotal";
    //    轮播广告视频播放停止翻页
    public static final String STOP_AUTO_PAGE_TURN = "stopAutoPageTurn";
    //    启动自动翻页
    public static final String START_AUTO_PAGE_TURN = "startAutoPageTurn";
    //    是否跳转
    public static final String SKIP_PAGE = "isSkipPage";
    //    首页
    public static final String MAIN_HOME = "homePage";
    //    发现
    public static final String MAIN_FIND = "find";
    //    良品
    public static final String MAIN_QUALITY = "quality";
    //    我
    public static final String MAIN_MINE = "mine";
    //    限时特惠
    public static final String MAIN_TIME = "time";
    //    营销活动 web 跳转
    public static final String MAIN_WEB = "webActivity";
    //    关注回调请求码 请求登录
    public static final int IS_LOGIN_CODE = 10;
    //    默认加载条目
    public static final int DEFAULT_TOTAL_COUNT = 10;
    //    子评论默认加载条数
    public static final int DEFAULT_COMMENT_TOTAL_COUNT = 6;
    //    加载条目20
    public static final int TOTAL_COUNT_TWENTY = 20;
    //    加载条目40
    public static final int TOTAL_COUNT_FORTY = 40;
    //  关联商品请求码
    public static final int RELEVANCE_PRO_REQ = 201;
    //    申请权限
    public static final int REQUEST_CODE_PERMISSION = 196;
    //    背景图片请求码
    public static final int REQ_MINE_BG = 107;
    //    默认
    public static final int TYPE_0 = 0;
    //    为空||品牌团
    public static final int TYPE_1 = 1;
    //    没有更多
    public static final int TYPE_2 = 2;
    public static final int TYPE_3 = 3;
    //    访问资源文件前缀
    public static final String BASE_RESOURCE_DRAW = "android.resource://com.amkj.dmsh/drawable/";
    //    订单操作
//    重新购买
    public static final String BUY_AGAIN = "buyAgain";
    //    删除
    public static final String DEL = "del";
    //    取消订单 待付款
    public static final String CANCEL_ORDER = "cancelOrder";
    //    取消订单 待发货
    public static final String CANCEL_PAY_ORDER = "cancelPayOrder";
    //    拼团 邀请参团
    public static final String INVITE_GROUP = "inviteGroup";
    //    部分发货
    public static final String LITTER_CONSIGN = "litterConsignment";
    //    付款
    public static final String PAY = "pay";
    //    整单退款查询去向
    public static final String REFUND_TO_WHERE = "refundToWhere";
    //    查看物流
    public static final String CHECK_LOG = "checkLogistics";
    //    确认订单
    public static final String CONFIRM_ORDER = "confirmOrder";
    //    申请退款
    public static final String REFUND_ORDER = "refundOrder";
    //    商品评价
    public static final String PRO_APPRAISE = "productAppraise";
    //    发票详情
    public static final String PRO_INVOICE = "invoice";
    //    评价晒单
    public static final String BASK_READER = "baskReader";
    /**
     * 积分
     */
//    申请售后
    public static final String REFUND_APPLY = "applyRefund";
    //    售后反馈
    public static final String REFUND_FEEDBACK = "feedbackRefund";
    //    售后维修
    public static final String REFUND_INTEGRAL_REPAIR = "repairIntegralRefund";

    //    积分订单专属 - 虚拟商品 - 查看优惠券
    public static final String VIRTUAL_COUPON = "checkCoupon";
    //    收藏类型
//    多么优选
    public static final String TYPE_C_SEARCH = "domoyx";
    //    多么福利社
    public static final String TYPE_C_WELFARE = "domofls";
    //    文章
    public static final String TYPE_C_ARTICLE = "document";
    //    评论类型
    public static final String COMMENT_TYPE = "doc";
    //    福利社评论
    public static final String COMMENT_TOPIC_TYPE = "topic";
    //    限时特惠评论
    public static final String PRO_COMMENT = "goods";
    //    文章专题 类型 商品详情推荐
    public static final String PRO_TOPIC = "topic";
    //    消息-评论-留言
    public static final String MES_ADVISE = "advise";
    //    消息-评论-反馈意见
    public static final String MES_FEEDBACK = "feedback";
    //    <a标签 识别地址跟图片地址
    public static String REGEX_TEXT = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
    //    网址识别
    public static String REGEX_URL = "((http[s]{0,1}|ftp)://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)|(www.[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)";
    //    图片识别
    public static final String IMG_REGEX_TAG = "<img.*src=(.*?)[^>]*?>";
    //    匹配是否是图片链接 匹配a标签
    public static final String regexATextUrl = "<a [\\s\\S]*?href=\"[\\s\\S]*?\"[\\s\\S]*?>";
    //    数字
    public static final String REGEX_NUM = "[+|-]*\\d+(\\.\\d+)?";
    //
    public static final String DEFAULT_SERVICE_PAGE_URL = "http://domolifeAndroid.com";
    //    订单商品状态
    public static Map<String, String> INDENT_PRO_STATUS;
    //    购物车活动状态
    public static Map<String, String> CAR_PRO_STATUS;
    /**
     * 小能客服咨询页面
     */
    public static final String BASE_SERVICE_URL = "http://app.domolife.";
    //    小能客服消息广播
    public static final String BROADCAST_RECEIVER_XN_SERVICE = "cn.broadcast.message.service";
    /**
     * 三方授权登录
     */
    public static final String OTHER_WECHAT = "wechat";
    public static final String OTHER_QQ = "qq";
    public static final String OTHER_SINA = "sina";
    /**
     * 支付类型
     */
//    支付宝
    public static final String PAY_ALI_PAY = "aliPay";
    //    微信支付
    public static final String PAY_WX_PAY = "wechatPay";

    /**
     * EventBus type类型
     */
    public static final String TIME_REFRESH = "timeRefresh";
    /**
     * 商品 赠品 颜色区别
     */
    public static final String PRESENT_BLACK = "blackFont";
    public static final String PRESENT_RED = "redFont";

    /**
     * 访问网络回调
     */
    //    请求成功
    public static final String SUCCESS_CODE = "01";
    //    请求失败
    public static final String ERROR_CODE = "00";
    //    数据为空
    public static final String EMPTY_CODE = "02";
    /**
     * 搜索类型
     */
    public static final String SEARCH_TYPE = "searchType";
    //    正常搜索
    public static final String SEARCH_ALL = "allSearch";
    //    订单搜索
    public static final String SEARCH_INDENT = "indentSearch";
    /**
     * 售后 退款 退货 维修
     */
//    售后类型参数
    public static final String REFUND_TYPE = "refundType";
    //    维修类型
    public static final String REFUND_REPAIR = "refundRepair";
    /**
     * 广播action
     */
//    广播接收
    public static final String BROADCAST_NOTIFY = "cn.broadcast.notify.click";
    /**
     * 关联商品类型
     */
//    关联类型
    public static final String RELEVANCE_TYPE = "relevanceType";
    //    已购买
    public static final String BOUGHT_PRODUCT = "boughtProduct";
    //    收藏商品
    public static final String COLLECT_PRODUCT = "collectProduct";
    //    购物车商品
    public static final String CART_PRODUCT = "cartProduct";
    /**
     * 良品分类参数
     */
    public static final String CATEGORY_TYPE = "categoryType";
    public static final String CATEGORY_ID = "categoryId";
    public static final String CATEGORY_NAME = "categoryName";
    public static final String CATEGORY_CHILD = "childCategory";
    /**
     * 存储类型引用
     */
//    app版本
    public static final String APP_VERSION_INFO = "appVersionInfo";
    //    后台更新时间 updateTime
    public static final String UPDATE_TIME = "U_T";
    //    弹窗间隔时间
    public static final String INTERVAL_TIME = "I_T";
    //    上一次弹窗时间
    public static final String LAST_UPDATE_TIME = "L_U_T";
    //    下载链接
    public static final String VERSION_DOWN_LINK = "V_D_L";
    //    更新详情
    public static final String VERSION_UPDATE_DESCRIPTION = "V_U_D";
    //    强制更新版本
    public static final String VERSION_UPDATE_LOW = "V_U_L";
    //    当前更新版本
    public static final String APP_CURRENT_UPDATE_VERSION = "A_C_U_V";

    /**
     * 推送通知开关
     */
    public static final String PUSH_CHECK = "P_C";
    //    检查时间
    public static final String PUSH_CHECK_TIME = "P_C_T";
    /**
     * 统计 map参数
     */
//    统计Id
    public static final String TOTAL_ID = "totalId";
    //    统计名称
    public static final String TOTAL_NAME = "totalName";
    //    统计大小默认值
    public static int UP_TOTAL_SIZE = 50;
    //    是否正在上传数据
    public static boolean isUpTotalFile = false;

    /**
     * 商品详情 推荐分类
     */
    public static String TOTAL_NAME_TYPE = "totalNameType";
    //    推荐分类
    public static String RECOMMEND_TYPE = "recommendType";
    //    购物车推荐
    public static String RECOMMEND_CAR = "carRecommend";
    //    搜索推荐
    public static String RECOMMEND_SEARCH = "searchRecommend";
    //    商品详情推荐
    public static String RECOMMEND_PRODUCT = "productRecommend";
    //    支付推荐
    public static String RECOMMEND_PAY_SUCCESS = "paySuccessRecommend";
//    签到 双倍积分类型
    public static String DOUBLE_INTEGRAL_PREFECTURE = "50";
//    双倍积分 专区类型
    public static String DOUBLE_INTEGRAL_TYPE = "doubleIntegralType";
    /**
     * 分享提示展示
     */
    public static boolean isShowTint = true;

    /**
     * 积分订单
     */
//    积分订单类型
    public static String INDENT_PRODUCT_TYPE = "productType";
//    积分商品
    public static String INDENT_INTEGRAL_PRODUCT = "integralProduct";
//    自营商品
    public static String INDENT_PROPRIETOR_PRODUCT = "proprietorProduct";
}
