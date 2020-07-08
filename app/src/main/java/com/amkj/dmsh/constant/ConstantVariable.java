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

    public static final String TAOBAO_PID = "mm_113346569_43964046_400008826";
    //    adzoneid
    public static final String TAOBAO_ADZONEID = "400008826";
    //    淘宝联盟appKey
    public static final String TAOBAO_APPKEY = "27852329";
    //    默认图片名称
    public static final String DEFAULT_ADD_IMG = "plus_icon_nor.png";
    //    收到消息广播，发送刷新，更新消息
    public static final String REFRESH_MESSAGE_TOTAL = "RefreshMessageTotal";
    //    token过期，强制登出
    public static final String TOKEN_EXPIRE_LOG_OUT = "tokenExpireLogOut";
    //    首页
    public static final String MAIN_HOME = "home";
    //    发现
    public static final String MAIN_FIND = "find";
    //    分类
    public static final String MAIN_QUALITY = "quality";
    //    我
    public static final String MAIN_MINE = "mine";
    //    淘好货
    public static final String MAIN_TIME = "time";

    //    关注回调请求码 请求登录
    public static final int IS_LOGIN_CODE = 10;
    //    子评论默认加载条数
    public static final int DEFAULT_COMMENT_TOTAL_COUNT = 20;
    //    默认加载条目
    public static final int TOTAL_COUNT_TEN = 10;
    //    加载条目20
    public static final int TOTAL_COUNT_TWENTY = 20;
    //    加载条目30
    public static final int TOTAL_COUNT_THIRTY = 30;
    //    加载条目40
    public static final int TOTAL_COUNT_FORTY = 40;
    //    加载条目80
    public static final int TOTAL_COUNT_EIGHTY = 80;
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
    //    取消订单 待付款
    public static final String CANCEL_ORDER = "cancelOrder";
    //    取消订单 待发货
    public static final String CANCEL_PAY_ORDER = "cancelPayOrder";
    //    部分发货
    public static final String LITTER_CONSIGN = "litterConsignment";
    //    付款
    public static final String PAY = "pay";
    //    查看物流
    public static final String CHECK_LOG = "checkLogistics";
    //    确认订单
    public static final String CONFIRM_ORDER = "confirmOrder";
    //    商品评价
    public static final String PRO_APPRAISE = "productAppraise";
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
     * 验证码类型
     */
    public static final String BIND_PHONE = "remobile";//绑定手机
    public static final String RESET_PASS = "repass";//重置密码
    public static final String LOGIN = "login";//登录

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
    public static final String BROADCAST_NOTIFY = "cn.broadcast.notify.click";

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
     * 统计 map参数
     */
    //    统计Id
    public static final String TOTAL_ID = "totalId";
    //    统计名称
    public static final String TOTAL_NAME = "totalName";

    /**
     * 商品详情 推荐分类
     */
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
    public static final String UPDATE_CAR_NUM = "updateCarNum";
    public static final String UPDATE_POST_CONTENT = "updatePostContent";
    public static final String UPDATE_FOLLOW_STATUS = "updateFollowStatus";//刷新关注状态
    public static final String UPDATE_POST_COMMENT = "updatePostComment";
    public static final String UPDATE_SCORE_LIST = "updateScoreList";
    public static final String UPDATE_WAITAPPRAISE_ICON = "updateWaitAppraiseIcon";//刷新全部订单/待评价角标
    public static final String DELETE_POST = "deletePost";//删除帖子刷新帖子列表
    public static final String UPDATE_SEARCH_NUM = "updateSearchNum";//刷新搜索结果数量
    public static final String UPDATE_USER_PAGER = "updateUserPager";//刷新用户主页信息
    public static final String SEARCH_DATA = "searchData";    //    搜索 获取传递信息参数
    public static final String UPDATE_CUSTOM_NAME = "updateCustomName";    //    更新自定义专区tab名称
    public static final String INVOICE_APPLY_SUCCESS = "invoiceApplySuccess";    //发票开具成功，刷新发票详情
    public static final String RECEIVED_NEW_QY_MESSAGE = "receivedNewQyMessage";    //收到新的七鱼客服消息
    public static final String UPDATE_EXPRESS_DATA = "updateExpressData";    //刷新物流数据
    public static final String UPDATE_INDENT_LIST = "updateIndentList";    //刷新订单列表
    public static final String UPDATE_WAIT_EVALUATE_INDENT_LIST = "updateWaitEvaluateIndentList";    //刷新订单列表
    public static final String UNION_PAY_CALLBACK = "unionPayCallback";    //h5银联支付回调
    public static final String UPDATE_INDENT_DISCOUNTS = "updateIndentDiscounts";    //更新结算信息


    /**
     * 商品item类型
     */
    public static final int PRODUCT = 0;//普通商品
    public static final int AD_COVER = 1;//好物封面
    public static final int TITLE = 2;//标题


    /**
     * 购物车修改商品的类型
     */
    public static final int ADD_NUM = 0;//增加数量
    public static final int REDUCE_NUM = 1;//减少数量
    public static final int CHANGE_SKU = 2;//修改sku


    public static final int NEW_FANS = 1;//新增粉丝
    public static final int RECOMMEND_FOLLOW = 2;//推荐关注
    public static final int RECOMMEND_FOLLOW_TITLE = 3;//推荐关注头部

    public static final String TOPIC_TYPE = "topic";


    /**
     * 搜索类型key
     */
    public static final String ALL_SEARCH_KEY = "allSearch";
    public static final String ARTICLE_SEARCH_KEY = "articleSearch";
    public static final String TOPIC_SEARCH_KEY = "topicSearch";
    public static final String USER_SEARCH_KEY = "userSearch";


    /**
     * 口令红包类型
     */
    public static final String COUPON = "1";//优惠券
    public static final String COUPON_PACKAGE = "2";//优惠前礼包
    public static final String SKIP_LINK = "3";//跳转链接

    /**
     * 优惠券专区样式
     */
    public static final int COUPON_COVER = 0;//封面
    public static final int COUPON_ONE_COLUMN = 1;//一列
    public static final int COUPON_THREE_COLUMN = 3;//三列


    /**
     * 拼团订单类型
     */
    public static final int OPEN_GROUP = 1;//开团
    public static final int JOIN_GROUP = 2;//拼团


    /**
     * 拼团类型
     */
    public static final String GROUP_PRODUCT = "0";//普通团
    public static final String GROUP_LOTTERY = "1";//抽奖团

    /**
     * 弹窗编号
     */
    public static final String FORCE_UPDATE = "1";//强制更新
    public static final String COUPON_POPUP = "10";//优惠券弹窗
    public static final String GP_REMIND = "21";//拼团未完成
    public static final String NOT_FORCE_UPDATE = "22";//非强制更新
    public static final String PUSH_OPEN_REMIND = "23";//推送通知打开提醒
    public static final String MARKING_POPUP = "31";//营销弹窗


    /**
     * 商品数据埋点来源类型
     */
    public static final int ARTICLE = 1;//文章
    public static final int MUST_BUY_TOPIC = 2;//必买清单专题
    public static final int WELFARE_TOPIC = 3;//福利社专题
    public static final int SUPER_GOOD = 4;//定制专题
    public static final int REDACTOR_PICKED = 5;//小编精选
    public static final int WEEKLY_ZONE = 6;//每周优选
    public static final int POST = 7;//帖子详情
    public static final int AD = 8;//广告（轮播，启动广告，浮动广告，好物广告）
    public static final int POPUP = 9;//营销弹窗
    public static final int DYNAMIC_AREA = 10;//首页动态专区

    /**
     * 订单列表按钮类型
     */
    public static final int CONFIRM_TAKE = 1;//确认收货
    public static final int CHECK_LOGISTICS = 2;//查看物流
    public static final int DELAY_TAKE = 3;//延迟收货
    public static final int APPLY_REFUND = 4;//申请退款
    public static final int REFUND_ASPECT = 5;//退款去向
    public static final int CUSTOMER_SERVICE_DETAIL = 6;//售后详情
    public static final int WAIT_COMMENT = 7;//待点评
    public static final int WAITDELIVERY = 8;//催发货
    public static final int ADD_CART = 9;//加入购物车
    public static final int DELETE_ORDER = 10;//删除订单
    public static final int CANCEL_ORDER_NEW = 11;//取消订单
    public static final int GO_PAY = 12;//去支付
    public static final int TO_BUY = 13;//再次购买
    public static final int INVITE_JOIN_GROUP = 14;//邀请参团
    public static final int VIEW_INVOICE = 15;//查看发票
    public static final int BATCH_REFUND = 16;//批量退款
    public static final int URGE_REFUND = 17;//催促退款
    public static final int EDIT_ADDRESS = 18;//修改地址
    public static final int CHECK_REFUND_LOGISTICS = 19;//查看退货物流


    //Activity跳转请求码
    public static final int NEW_CRE_ADDRESS_REQ = 101;
    public static final int SEL_ADDRESS_REQ = 102;
    public static final int CREATE_ADDRESS_REQ = 103;
    public static final int EDIT_ADDRESS_REQ = 104;
    public static final int DIRECT_COUPON_REQ = 105;
    public static final int CHANGE_ORDER_ADDRESS = 106;


    /**
     * 退款类型
     */
    public static final String NOGOODS_REFUND = "1";//未收到货仅退款
    public static final String GOODS_REFUND = "4";//已收到货仅退款
    public static final String RETURN_GOODS = "2";//退货退款


    /**
     * 订单列表类型
     */
    public static final String INDENT_TYPE = "1";//订单列表
    public static final String INDENT_DETAILS_TYPE = "2";//订单详情
    public static final String SELECT_REFUND_GOODS = "3";//选择退款商品
    public static final String SELECT_REFUND_TYPE = "4";//选择退款类型
    public static final String APPLY_REFUND_TYPE = "5";//申请退款
    public static final String REFUND_DETAIL_TYPE = "6";//退款详情
    public static final String INDENT_W_TYPE = "7";//普通订单填写
    public static final String INDENT_GROUP_SHOP = "8";//拼团订单填写
}
