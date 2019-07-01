package com.amkj.dmsh.constant;

import com.amkj.dmsh.BuildConfig;

import java.util.HashMap;
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
    //    //    整点秒杀数据加载完成
//    public static final String POINT_SPIKE_LOAD_COMPLETE = "pointSpikeLoadComplete";
    //    token过期，强制登出
    public static final String TOKEN_EXPIRE_LOG_OUT = "tokenExpireLogOut";
    //    //    用户登录状态改变
//    public static final String LOGIN_STATUS_CHANGE = "loginStatusChange";
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
    //    关注回调请求码 请求登录
    public static final int IS_LOGIN_CODE = 10;
    //    注册 登录回调
    public static final String R_LOGIN_BACK_CODE = "0x101";
    public static final String R_LOGIN_BACK_DATA_CODE = "AccountInf";
    //    子评论默认加载条数
    public static final int DEFAULT_COMMENT_TOTAL_COUNT = 6;
    //    默认加载条目
    public static final int TOTAL_COUNT_TEN = 10;
    //    加载条目20
    public static final int TOTAL_COUNT_TWENTY = 20;
    //    加载条目30
    public static final int TOTAL_COUNT_THIRTY = 30;
    //    加载条目40
    public static final int TOTAL_COUNT_FORTY = 40;
    //  关联商品请求码
    public static final int RELEVANCE_PRO_REQ = 201;
    //    背景图片请求码
    public static final int REQ_MINE_BG = 107;
    //    请求通知状态
    public static final int REQUEST_NOTIFICATION_STATUS = 0x101;
    //    默认
    public static final int TYPE_0 = 0;
    //    为空||品牌团
    public static final int TYPE_1 = 1;
    //    没有更多
    public static final int TYPE_2 = 2;
    //TOP团品标题
    public static final int TYPE_3 = 3;
    //淘宝长期商品标题
    public static final int TYPE_4 = 4;
    //    访问资源文件前缀
    public static final String BASE_RESOURCE_DRAW = "android.resource://com.amkj.dmsh/drawable/";
    /**
     * 订单操作
     */
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
    //    提醒发货
    public static final String REMIND_DELIVERY = "remindDelivery";
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
    /**
     * 正则表达式
     */
    //    <a标签 识别地址跟图片地址
    public static String REGEX_TEXT = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
    //    网址识别
    public static String REGEX_URL = "((http[s]{0,1}|ftp)://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)|(www.[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)";
    //    图片识别
    public static final String IMG_REGEX_TAG = "<img.*src=(.*?)[^>]*?>";
    //    匹配是否是图片链接 匹配a标签
    public static final String regexATextUrl = "<a [\\s\\S]*?href=\"[\\s\\S]*?\"[\\s\\S]*?>";
    public static final String aRegex = "<a[^>]*href=(\\\"([^\\\"]*)\\\"|\\'([^\\']*)\\'|([^\\\\s>]*))[^>]*>(.*?)</a>";
    public static final String pRegex = "<p.*?>(.*?)</p>";


    //    数字
    public static final String REGEX_NUM = "[+|-]*\\d+(\\.\\d+)?";
    //    正则匹配空格 换行符
    public static final String REGEX_SPACE_CHAR = "\\s*|\\t|\\r|\\n";
    //    正则匹配数字跟-
    public static final String REGEX_NUMBER_BAR = "^[0-9]*-?[0-9]*$";
    //    正则匹配密码6-20 数字与字母结合
    public static final String REGEX_PW_ALPHABET_NUMBER = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";

    //    订单商品状态
    public static Map<String, String> INDENT_PRO_STATUS;
    /**
     * 三方授权登录
     */
    public static final String OTHER_WECHAT = "wechat";
    public static final String OTHER_QQ = "qq";
    public static final String OTHER_SINA = "sina";
    public static final String OTHER_MOBILE = "mobile";
    /**
     * 支付类型
     */
//    支付宝
    public static final String PAY_ALI_PAY = "aliPay";
    //    微信支付
    public static final String PAY_WX_PAY = "wechatPay";
    //    银联支付
    public static final String PAY_UNION_PAY = "abcPay";
    //    银联支付返回码
    public static final int UNION_RESULT_CODE = 0x8006;

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
    //    请求失败
    public static final String ERROR_CODE = "00";
    //    请求成功
    public static final String SUCCESS_CODE = "01";
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
    public static final String CATEGORY_PID = "categoryPid";
    public static final String CATEGORY_NAME = "categoryName";
    public static final String CATEGORY_CHILD = "childCategory";
    public static final String CATEGORY_TWO_LEVEL_LIST = "twoLevelCategoryList"; //二级分类集合
    /**
     * 公用web数据类型
     */
//    注册协议
    public static final String WEB_TYPE_REG_AGREEMENT = "register_agreement";
    //    隐私政策
    public static final String WEB_TYPE_PRIVACY_POLICY = "privacy_policy";
    //    web数据类型
    public static final String WEB_VALUE_TYPE = "webRuleType";

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

//    /**
//     * 活动Code
//     */
//    public static String XSG = "XSG";//限时购
//    public static String LJ = "LJ";//立减
//    public static String ZC = "ZC";//折扣
//    public static String BJ = "BJ";//第二件半价
//    public static String MJ = "MJ";//满减
//    public static String MM = "MM";//每满
//    public static String MZ = "MZ";//满赠

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

    /**
     * web 数据
     */
//    淘宝
    public static final String WEB_TAOBAO_SCHEME = "taobao://";
    public static final String WEB_TB_SCHEME = "tbopen://";
    //    京东
    public static final String WEB_JD_SCHEME = "jdmobile://";
    //    天猫
    public static final String WEB_TMALL_SCHEME = "tmall://";
    //    web 空白页面
    public static final String WEB_BLACK_PAGE = "about:blank";

    //    下载Map
    public static Map<String, String> downFileMap = new HashMap<>();

    /**
     * oss 获取变量
     */
    public static final String OSS_BUCKET_NAME = "O_B_N";
    public static final String OSS_URL = "O_S_U";
    public static final String OSS_OBJECT = "O_S_Obj";

    /**
     * SharedPre键值常量
     */
    public static final String IS_NEW_USER = "isNewUser"; //是否是新安装的用户
    public static final String GET_FIRST_INSTALL_INFO = "getFirstInstallInfo";//是否成功调用 （统计首次安装设备信息）接口
    public static final String TOKEN = "token";//token
    public static final String TOKEN_EXPIRE_TIME = "tokenExpireTime";//token过期时间
    public static final String TOKEN_REFRESH_TIME = "tokenRefreshTime";//token刷新时间


    public static final String DEMO_LIFE_FILE = "duomolife";//用户信息本地shared_prefs文件名


    /**
     * Eventbus类型常量
     */
    public static final String UPDATE_CAR_NUM = "updateCarNum";//用户信息本地shared_prefs文件名


    /**
     * 商品item类型
     */
    public static final int PRODUCT = 0;//普通商品
    public static final int AD_COVER = 1;//好物封面
    public static final int TITLE = 2;//标题
    public static final int PIC_GOODS = 3;//图片商品


     /**
     * 购物车修改商品的类型
     */
    public static final int ADD_NUM = 0;//增加数量
    public static final int REDUCE_NUM = 1;//减少数量
    public static final int CHANGE_SKU = 2;//修改sku

}
