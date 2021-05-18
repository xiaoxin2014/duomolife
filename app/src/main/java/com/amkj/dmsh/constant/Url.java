package com.amkj.dmsh.constant;

/**
 * Created by atd48 on 2016/8/27.
 */
public class Url {

    //    public static String BASE_URL = "http://dev.domolife.cn/";
    public static String BASE_URL = "https://app.domolife.cn/";

    public static String getUrl(int position) {
        if (position == 0) {//正式库
            return BASE_URL = "https://app.domolife.cn/";
        } else if (position == 1) {//测试库
            return BASE_URL = "http://ts.domolife.cn/";
        } else if (position == 2) {//钊立
            return BASE_URL = "http://192.168.2.111:8080/";
        } else if (position == 3) {//泽鑫
            return BASE_URL = "http://192.168.1.51:8080/";
        } else if (position == 4) {//泽鑫2
            return BASE_URL = "http://192.168.1.178:9090/";
        } else if (position == 5) {//预发布
            return BASE_URL = "http://dev.domolife.cn/";
        } else if (position == 6) {//王凯2
            return BASE_URL = "http://192.168.3.6:8080/";
        } else if (position == 7) {//王凯1
            return BASE_URL = "http://192.168.2.212:80/";
        } else if (position == 8) {//鸿星
            return BASE_URL = "http://192.168.1.9:8080/";
        }
        return BASE_URL = "https://app.domolife.cn/";
    }

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    /**
     * 首页
     */
    //    首次启动获取push信息
    public static final String FIRST_PUSH_INFO = "api/my/sendAppPushRule";
    //    统计首次安装设备信息
    public static final String FIRST_INSTALL_DEVICE_INFO = "/api/home/firstInstallDeviceInfo";
    //    统计用户分享行为
    public static final String STATISTICS_SHARE = "/api/shareRecord/myShare";
    //    push信息获取
    public static final String FIRST_PUSH_INFO_RECEIVE = "api/my/pushAppPush";
    //    设置设备信息
    public static final String DEVICE_INFO = "api/my/updateUserDeviceInfo";
    //    版本信息
    public static final String APP_VERSION_INFO = "api/my/getVersions";
    //    开启通知接口
    public static final String APP_SYS_NOTIFICATION = "api/my/remind/getSysNoticeInfo";
    //    用户关注
    public static final String UPDATE_ATTENTION = "api/find/followsaveOrupdate";
    //   手机号密码登录
    public static final String LOGIN_ACCOUNT = "api/my/mobileAndPasswordToLogin";
    //    手机号验证码登录
    public static final String LOGIN_CHECK_SMS_CODE = "api/my/mobileToLogin";
    //    新版第三方登录
    public static final String MINE_OTHER_NEW_ACCOUNT = "api/third/thirdLogin";
    //   账号安全，微信账号绑定手机
    public static final String MINE_BIND_WX_MOBILE = "api/third/thirdBindingMobile";
    //  绑定未注册的手机时设置密码
    public static final String SETTING_NEW_PWD = "api/third/setThirdToAppPassword";


    //    首页文章分类列表
    public static final String H_CATEGORY_LIST = "api/home/getNewCategoryList";

    //    首页 跑马灯效果
    public static final String H_Q_MARQUEE_AD = "api/search/getRollingNotice";
    //    详情分类文章列表
    public static final String CATE_DOC_LIST = "api/home/getDocumentHomeList";
    //    OSS配置
    public static final String H_OSS_CONFIG = "api/oss/getDetail";
    //    地址配置版本
    public static final String H_ADDRESS_VERSION = "api/area/getVersion";
    //    底栏导航
    public static final String H_BOTTOM_ICON = "api/appindex/getAppIndexConfig";
    //    地址配置数据
    public static final String H_ADDRESS_DATA = "api/area/getArea";
    //    新版搜索热门标签
    public static final String H_HOT_NEW_SEARCH_LIST = "api/search/getSearchHotWord";
    //    全局搜索
    public static final String H_HOT_SEARCH_ALL = "api/search/searchContent";
    //    搜索产品类目推荐
    public static final String H_SEARCH_PRODUCT_RECOMMEND = "api/search/searchRecommendCategory";
    //    搜索产品无商品推荐
    public static final String H_SEARCH_PRODUCT_GOOD = "api/search/searchRecommendGoodThings";
    //  搜索留言
    public static final String SEARCH_LEAVE_MES = "api/search/saveAdvise";
    //  新版分享
    public static final String BASE_SHARE_PAGE_TWO = "https://www.domolife.cn/m/template/";
    //    public static final String BASE_SHARE_PAGE_TWO = "https://test.domolife.cn/test/template/";
    //    文章详情公共分享
    public static final String SHARE_COMMUNAL_ARTICLE = "api/reminder/getSharePrompt";
    //签到提醒
    public static final String H_ATTENDANCE_WARM = "api/activity/setUpSign";
    //新版签到
    public static final String H_ATTENDANCE = "api/activity/newSign";
    //新版签到 领取签到奖励
    public static final String H_ATTENDANCE_AWARD = "api/activity/getSignReward";
    //积分明细
    public static final String H_ATTENDANCE_INTEGRAl_DETAIL = "api/activity/integralMsgList";
    //    签到规则
    public static final String H_ATTENDANCE_RULE = "api/activity/getSignExplain";
    //    二版签到详情
    public static final String H_ATTENDANCE_DETAIL = "api/activity/getSignDetail";
    //    积分获取途径
    public static final String H_ATTENDANCE_GET = "api/activity/getEarnScoreMenu";
    //    积分夺宝
    public static final String H_ATTENDANCE_INTEGRAL_LOTTERY = "api/activity/getLotteryInfo";
    //    积分夺宝奖励
    public static final String H_ATTENDANCE_INTEGRAL_LOTTERY_AWARD = "api/activity/getMyLotteryCode";
    //    参与积分夺宝
    public static final String H_ATTENDANCE_JOIN_IN_INTEGRAL_LOTTERY = "api/activity/joinLotteryActivity";
    //    积分夺宝 领取信息
    public static final String H_ATTENDANCE_INTEGRAL_LOTTERY_AWARD_INFO = "api/activity/goTakePrize";
    //    积分夺宝 领取奖品
    public static final String H_ATTENDANCE_INTEGRAL_LOTTERY_AWARD_GET = "api/activity/getTakePrize";
    //    签到更多专区
    public static final String H_ATTENDANCE_MORE_ACTIVITY = "api/home/getSignInTopic";
    //    往期夺宝
    public static final String H_ATTENDANCE_INTEGRAL_LOTTERY_AWARD_HISTORY = "api/activity/getOverLotteryActivity";
    //    夺宝规则
    public static final String H_ATTENDANCE_INTEGRAL_LOTTERY_RULE = "api/activity/getLotteryExplain";
    //    领券中心
    public static final String H_COUPON_CENTER_DATA = "api/reminder/getCouponCore";
    //    新版消息统计
    public static final String H_MES_STATISTICS_NEW = "api/message/getMessageInfo";
    //     app最后登录时间
    public static final String H_LOGIN_LAST_TIME = "api/my/updateLastLogin";
    //    新人优惠弹窗
    public static final String H_NEW_USER_COUPON = "api/usertype/judgeUserType";
    //    统计点击商品数
    public static final String TOTAL_PRO_NUM = "api/goods/addDocumentProductNum";
    //    统计福利社点击商品数
    public static final String TOTAL_WELFARE_PRO_NUM = "api/goods/addTopicProductNum";
    //    统计官方通知点击商品数
    public static final String TOTAL_OFFICIAL_PRO_NUM = "api/goods/addCommProductNum";
    //    统计广告点击数
    public static final String TOTAL_AD_COUNT = "api/home/addAdClickCount";
    //    统计通知消息点击数
    public static final String TOTAL_NOTIFY_MSG_COUNT = "api/message/addClickNumByPlatform";
    //    统计活动消息点击数
    public static final String TOTAL_ACTIVITY_MSG_COUNT = "api/message/addClickNumByActivityInfo";
    //    统计动态专区点击数
    public static final String TOTAL_DYNAMIC_COUNT = "api/home/addDynamicAreaCount";
    //    统计十二宫格
    public static final String TOTAL_MYDEFINEDICON_COUNT = "api/home/addMyDefinedClickCount";
    //    统计极光消息打开数
    public static final String TOTAL_JPUSH_COUNT = "api/home/addPushClickNum";
    //    官方详情
    public static final String H_MES_OFFICIAL = "api/message/newOfficialCommentList";
    //    订单物流消息列表
    public static final String H_MES_INDENT = "api/message/getApiNewOrderMsgListPage";
    //    二期通知消息
    public static final String H_MES_NOTIFY = "api/message/newMsgMessageList";
    //      官方通知详情
    public static final String H_MES_OFFICIAL_DETAILS = "api/message/getOfficialCommentDetails";
    //    平台通知
    public static final String H_MES_PLATFORM_DETAILS = "api/message/getMsgDetail";
    //    小编精选列表
    public static final String EDITOR_SELECT_LIST = "api/redactorpicked/redactorpickedList";
    //    小编精选点赞
    public static final String EDITOR_SELECT_FAVOR = "api/redactorpicked/favor";
    //    订阅小编精选推送
    public static final String SUBSCRIBER_EDITOR = "api/redactorpicked/subscribe";
    //    小编精选添加留言
    public static final String EDITOR_SELECT_COMMENT = "api/redactorpickedcomment/addComment";
    //    小编精选留言点赞
    public static final String EDITOR_COMMENT_FAVOR = "api/redactorpickedcomment/favor";
    //    小编精选留言列表
    public static final String EDITOR_COMMENT_LIST = "api/redactorpickedcomment/getCommentList";
    //    获取自定义专区（逛一逛得积分）奖励
    public static final String GET_SHOPPING_REWARD = "api/goods/greate/grantViewReward";
    //    统一弹窗规则接口
    public static final String GET_UNIFIED_POPUP = "api/my/remind/getUnifiedPopup";
    //   获取未读的客服通知
    public static final String GET_UNREAD_CUSTOMER_MSG = "api/message/getUnreadCustomerServiceMsg";


    /**
     * 大改版首页
     */
    //    横排Tab
    public static final String GTE_HOME_NAVBAR = "api/appindex/getAppIndexNavBarList";
    //    Top栏
    public static final String GTE_HOME_TOP = "api/appindex/getAppIndexActivityZoneList";
    //    动态专区（新人专享）
    public static final String GTE_HOME_DYNAMIC_AREA = "api/appindex/getAppIndexDynamicArea";
    //    获取专区（多个）
    public static final String GTE_HOME_SPECIAL_ZONE = "api/appindex/getAppIndexSpecialZoneList";

    /**
     * 我的
     */
    //    查询个人信息
    public static final String MINE_PAGE = "api/my/getPersonalData";
    //    我-底部宫格数据
    public static final String MINE_BOTTOM_DATA = "api/my/getMyDefinedIcon";
    //我的粉丝
    public static final String MINE_FANS = "api/my/getMyFans";
    //我的关注
    public static final String MINE_ATTENTION = "api/my/getMyFllow";
    //我的提醒列表
    public static final String MINE_WARM = "api/my/remind/myRemindList";
    //取消提醒
    public static final String CANCEL_MINE_WARM = "api/my/remind/deleteMyRemind";
    //设置提醒
    public static final String ADD_MINE_WARM = "api/my/remind/addMyRemind";
    //    收藏商品列表
    public static final String COLLECT_PRO = "api/my/getMyCollectGoodList";
    //    取消收藏 多个
    public static final String CANCEL_MULTI_COLLECT_PRO = "api/home/cancelGoodsCollect";
    //    获取足迹时间轴
    public static final String MINE_BROWSING_HISTORY_TIME_SHAFT = "api/my/getFootmarkTime";
    //    我的浏览记录
    public static final String MINE_BROWSING_HISTORY = "api/my/getMyFootmark";
    //    删除浏览记录
    public static final String DEL_MINE_BROWSING_HISTORY = "api/my/deleteMyFootmark";
    //修改信息
    public static final String MINE_CHANGE_DATA = "api/my/updatePersonalData";
    //    二期新增 修改会员宝宝
    public static final String MINE_BABY_INFO = "api/my/updateMemberBabyTwo";
    //    选择兴趣类别
    public static final String MINE_HABIT_TYPE = "api/my/getMyInterest";
    //    背景图片列表
    public static final String MINE_BG_IMG_LIST = "api/my/getMybgimg";
    //    消息推送列表
    public static final String MINE_MES_PUSH_LIST = "api/my/getMyMessagepushtype";
    //    修改消息推送提醒
    public static final String MINE_MES_PUSH_SWITCH = "api/my/deitMyMessagepushtype";
    //    用户注册
    public static final String USER_REGISTER_ACCOUNT = "api/my/appRegisterV2";
    //    注册协议
    public static final String USER_REGISTER_AGREEMENT = "api/reminder/getUserRegisterAgreement";
    //    用户隐私政策
    public static final String USER_PRIVACY_POLICY = "api/reminder/getUserPolicySecret";
    //    隐私政策
    //账号安全，获取第三方账号列表
    public static final String MINE_SYNC_LOGIN = "api/my/getSyncLoginList";
    //账号安全，修改密码
    public static final String MINE_CHANGE_PASSWORD = "api/my/updatePassword";
    //账号安全，重置密码
    public static final String MINE_RESET_PASSWORD = "api/my/resetPassword";
    //    注销文案
    public static final String ACCOUNT_LOGOUT_TIP = "api/destroy/getDestroyTip";
    //    注销原因
    public static final String ACCOUNT_LOGOUT_REASON = "api/destroy/getDestroyCause";
    //    注销请求
    public static final String ACCOUNT_LOGOUT_REQUEST = "api/destroy/sureDestroyAccount";
    //    修改实名信息
    public static final String MINE_RESET_REAL_NAME = "api/my/updateIdcardAndRealName";
    //账号安全，更换手机
    public static final String MINE_CHANGE_MOBILE = "api/my/updateMobile";
    //账号安全，绑定第三方账号
    public static final String MINE_BIND_ACCOUNT = "api/my/bindSyncLogin";
    //意见反馈
    public static final String MINE_FEEDBACK = "api/my/addFeedBackNew";
    //    意见反馈类型
    public static final String MINE_FEEDBACK_TYPE = "api/my/getFeedBackType";
    //    检查该号码是否已注册
    public static final String CHECK_PHONE_IS_REG = "api/my/checkRegMobile";
    //    更新清除数据
    public static final String CHECK_CLEAR_USER_DATA = "api/my/checkUserCleanLogin";
    //    请求验证码
    public static final String REQ_SEND_SMS_CODE = "api/SMS/appSendSMSV2";
    //    修改兴趣爱好
    public static final String CHANGE_USER_HABIT = "api/my/deitMemberInterest";
    //    微信解除绑定
    public static final String ACCOUNT_UNBIND_WECHAT = "api/my/wxUnbind";
    //    微博QQ解除绑定
    public static final String ACCOUNT_UNBIND_SINA_QQ = "api/my/sinaAndqqUnbind";
    //    确认用户token是否真的过期
    public static final String CONFIRM_LOGIN_TOKEN_EXPIRE = "api/my/confirmLoginTokenExpire";
    //    刷新用户登录token,每天第一次打开app的时候调用,刷新token时间
    public static final String FLUSH_LOGIN_TOKEN = "api/my/flushLoginToken";
    //    用户登出(清除记录的token信息)
    public static final String LOG_OUT = "/api/my/logOut";


    /**
     * 我的赞列表
     */
    //     文章删除
    public static final String MINE_INVITATION_DEL = "api/my/deleteDocument";
    //    收藏帖子
    public static final String COLLECT_INVITATION = "api/my/getMyCollectPostListV2";
    //  收藏专题(文章)
    public static final String COLLECT_SPECIAL = "api/my/getMyCollectDocumentList";
    //    收藏话题
    public static final String COLLECT_TOPIC = "api/my/getMyCollectFindTopiclist";
    //    我的帖子列表
    public static final String MINE_INVITATION_LIST = "api/my/getMyPostListV2";
    /**
     * 限时特惠
     */
    //    限时特惠 商品列表
    public static final String TIME_SHOW_PRODUCT_TOPIC_SHAFT = "api/goods/flashSale/getNewFlashSaleGoodsV2";
    //    限时特惠top推荐
    public static final String TIME_SHOW_PRO_TOP_PRODUCT = "api/goods/flashSale/getGoodsRecommend";
    //    淘宝长期商品推荐
    public static final String TIME_SHOW_TAOBAO_PRODUCT = "api/goods/flashSale/getLongTimeTaobaoProducts";
    //    是否设置过提醒
    public static final String TIME_SHOW_PRO_WARM = "api/my/remind/getHadRemind";
    //    限时特惠商品购买点击统计
    public static final String TIME_PRODUCT_CLICK_TOTAL = "api/userbehavioursts/collectFlashSaleProductBuyClick";
    /**
     * 积分商城
     */
    //   积分筛选
    public static final String H_INTEGRAL_PRODUCT_FILTRATE = "api/hot/intergral/getGoodsListV2";
    //    积分详情
    public static final String H_INTEGRAL_DETAILS_GOODS = "api/hot/intergral/getGoodsV2";
    //    积分列表
    public static final String INTEGRAL_INDENT_LIST = "api/integralorder/getIntegralOrders";
    //    积分订单结算信息
    public static final String INTEGRAL_DIRECT_SETTLEMENT = "api/integralorder/getIntegralOrderSettleInfo";
    //    积分订单详情
    public static final String INDENT_INTEGRAL_DETAILS = "api/integralorder/getIntegralOrderDetail";
    //    服务承诺
    public static final String INTEGRAL_PRODUCT_SERVICE = "api/activity/getIntegralProductServicePromise";
    //    获取提示语
    public static final String GET_REMIN_TEXT = "api/my/remind/getReminText";

    /**
     * 良品
     */
    //     获取第三方支付结果
    public static final String Q_UPDATE_PAY_RESULT = "api/goods/order/updatePayResult";
    //    大营销中心订单创建
    public static final String Q_CREATE_INDENT = "api/goods/order/addOrderPayInfoNewV2";
    //    积分订单创建
    public static final String INTEGRAL_CREATE_INDENT = "api/integralorder/addIntegralOrder";
    //    新版再次购买
    public static final String Q_NEW_RE_BUY_INDENT = "api/goods/order/againBuyNewV2";
    //    订单付款
    public static final String Q_PAYMENT_INDENT = "api/goods/order/buy";
    //    银联支付手动回调
    public static final String Q_UNIONPAY_PAYMENT_INDENT = "api/goods/order/abcOrderQuery";
    //    订单支付取消提示
    public static final String PAY_CANCEL = "api/reminder/getLeaveConfirm";
    //    新版购物车结算金额
    public static final String NEW_PRO_SETTLE_PRICE = "api/goods/cart/changeCartCheckStatusV2";
    //    新版订单详情
    public static final String Q_INDENT_NEW_DETAILS = "api/goods/order/getOrderDetail";
    //    订单删除
    public static final String Q_INDENT_DEL = "api/goods/order/del";
    //    订单取消 3.1.5 积分 商品 订单通用
    public static final String Q_INDENT_CANCEL = "api/goods/order/cancel";
    //    订单确定
    public static final String Q_INDENT_CONFIRM = "api/goods/order/takeDelivery";
    //    发布评价
    public static final String Q_SEND_APPRAISE = "api/goods/order/evaluate";
    //    新版订单列表
    public static final String Q_GET_ORDER_LIST = "api/goods/order/getOrderList";
    //    查询良品全部订单
    public static final String Q_INQUIRY_ALL_ORDER = "api/goods/order/getOrders";
    //    获取七鱼客服快捷入口
    public static final String GET_CUSTOMER_SERVICE_BAR = "api/home/getCustomerServiceBar";
    //    待发货 催单
    public static final String Q_INQUIRY_WAIT_SEND_EXPEDITING = "api/goods/order/urgeOrderV2";
    //    获取退款去向
    public static final String Q_GET_REFUND_GO_INFO = "api/goods/order/getRefundGoInfo";
    //    延长收货
    public static final String Q_INQUIRY_DELAY_TAKE_TIME = "api/goods/order/delayTakeTime";
    //    申请取消订单 待发货
    public static final String Q_CANCEL_INDENT_REFUND = "api/goods/order/cancelRefund";
    //    新版物流详情
    public static final String Q_LOGISTICS_DETAIL = "api/goods/order/getOrderLogistics";
    //    售后物流
    public static final String Q_REFUND_LOGISTICS_DETAIL = "api/goods/order/getOrderRefundLogistics";
    //    查询订单包裹信息
    public static final String Q_ORDER_LOGISTICS_PACKAGE = "api/goods/order/getOrderLogisticsPackage";
    //    新版售后列表
    public static final String Q_APPLY_AFTER_SALE_REPLY_RECORD = "api/goods/order/getAfterSaleV2";
    //    退款详情
    public static final String Q_INDENT_REFUND_DETAIL = "api/goods/order/getRefundDetailInfo";
    //    新版退款详情
    public static final String Q_INDENT_REFUND_NEW_DETAIL = "api/goods/order/getRefundDetailInfoV2";
    //    维修详情
    public static final String Q_INDENT_REPAIR_DETAIL = "api/goods/order/getRepairDetailInfo";
    //    售后维修
    public static final String Q_INDENT_REFUND_REPAIR_SUB = "api/goods/order/repairOrderProduct";
    //    维修撤销
    public static final String Q_INDENT_REFUND_REPAIR_CANCEL = "api/goods/order/undoApplyRepair";
    //    维修邮寄
    public static final String Q_INDENT_REPAIR_LOGISTIC_SUB = "api/goods/order/sendRepaiOrderProduct";
    //    确认维修商品收货
    public static final String Q_INDENT_REPAIR_RECEIVE = "api/goods/order/takeRepairDelivery";
    //    撤销申请
    public static final String Q_CANCEL_APPLY = "api/goods/order/undoApplyRefund";
    //    新版撤销申请
    public static final String Q_CANCEL_APPLY_NEW = "api/goods/order/undoApplyRefundV2";
    //    获取物流公司
    public static final String Q_INDENT_LOGISTIC_COM = "api/goods/order/getExpressCompanys";
    //    提交物流信息
    public static final String Q_INDENT_LOGISTIC_SUB = "api/goods/order/refundExpressNew";
    //    申请退款
    public static final String Q_INDENT_APPLY_REFUND = "api/goods/order/getApplyRefundInfo";
    //    新版申请退款
    public static final String Q_INDENT_APPLY_REFUND_NEW = "api/goods/order/getApplyRefundInfoV2";
    //    提交退款申请
    public static final String Q_INDENT_APPLY_REFUND_SUB = "api/goods/order/refundOrderProduct";
    //    新版提交退款申请
    public static final String Q_INDENT_APPLY_REFUND_SUB_NEW = "api/goods/order/refundOrderProductV2";
    //    修改退款申请
    public static final String Q_INDENT_CHANGE_REFUND_SUB = "api/goods/order/modifyApplyRefund";
    //    新版修改退款申请
    public static final String Q_INDENT_CHANGE_REFUND_SUB_NEW = "api/goods/order/modifyApplyRefundV2";
    //    申诉查询
    public static final String Q_INDENT_APPLY_REFUND_CHECK = "api/goods/order/getVerifyRefundResultNew";
    //    催促退款
    public static final String Q_INDENT_URGE_REFUND_PRICE = "api/goods/order/urgeRefundPrice";
    //    获取主订单支持退款的商品
    public static final String Q_GET_REFUND_REFUND_PRODUCTS = "api/goods/order/getBatchRefundProductList";
    //    获取点评弹窗信息
    public static final String Q_GET_TAKE_DELIVERY_POPUP = "api/goods/order/getTakeDeliveryPopUp";


    //    商品详情 新版
    public static final String Q_NEW_SHOP_DETAILS = "api/goods/getSelfProduct";
    //    商品评价
    public static final String Q_SHOP_DETAILS_COMMENT = "api/goods/getEvaluate";
    //    加入购物车 新版
    public static final String Q_SHOP_DETAILS_ADD_CAR = "api/goods/cart/addNew";
    //    组合商品加入购物车
    public static final String Q_COMBINE_PRODUCT_ADD_CAR = "api/goods/cart/addCombineProducts";
    //    订单商品加入购物车
    public static final String Q_ADD_ORDER_PRODUCT_TOCART = "api/goods/order/addOrderProductToCart";
    //    新版购物车列表
    public static final String NEW_MINE_SHOP_CAR_GOODS = "api/goods/cart/getCartNewV2";
    //    购物车推荐商品
    public static final String MINE_SHOP_CAR_RECOMMEND_GOODS = "api/goods/cart/shopFullCartRecommend";
    //    查询购物车数量
    public static final String Q_QUERY_CAR_COUNT = "api/goods/cart/getCartTip";
    //    添加取消到货通知
    public static final String Q_REPLENISHMENT_NOTICE = "api/goods/addUserSkuNotice";
    //    查询订单数量
    public static final String Q_QUERY_INDENT_COUNT = "api/goods/order/getOrderNum";
    //    购物车修改 新版
    public static final String Q_SHOP_DETAILS_CHANGE_CAR = "api/goods/cart/changeNew";
    //    购物车删除
    public static final String Q_SHOP_DETAILS_DEL_CAR = "api/goods/cart/del";
    //    获取商品属性
    public static final String Q_SHOP_DETAILS_GET_SKU_CAR = "api/goods/cart/getSaleSku";
    //    优惠券展示 新版
    public static final String Q_SHOP_DETAILS_COUPON = "api/my/coupon/getNewMyCouponList";
    //    自选优惠券
    public static final String Q_SELF_SHOP_DETAILS_COUPON = "api/my/coupon/choiceSelfCouponV2";
    //    优惠券专区
    public static final String Q_COUPON_ZONE = "api/activity/zone/getApiCouponZoneInfo";
    //    订单结算信息 大营销中心新版
    public static final String INDENT_DISCOUNTS_NEW_INFO = "api/goods/order/getOrderSettleInfoNewV2";
    //    跳转订单结算前校验
    public static final String CHECK_ORDER_SETTLE_INFOV2 = "api/goods/order/checkSelfProductOrderSettleInfoV2";
    //    再次购买跳转订单结算前校验
    public static final String CHECK_BUY_AGAIN_NEWV2 = "api/goods/order/checkAgainBuyNewV2";
    //    特殊地区提示信息
    public static final String GET_AREA_TIP = "api/goods/order/getAreaTip";
    //    更新订单优惠信息 大营销中心新版
    public static final String INDENT_DISCOUNTS_UPDATE_INFO = "api/goods/order/getOrderSettlePriceInfo";
    //    获取可支付方式
    public static final String GET_PAYTYPE_LIST = "api/goods/order/getPayTypeList";
    //    发票详情
    public static final String INVOICE_DETAIL = "api/goods/order/getInvoice";
    //    邮件发送发票
    public static final String SEND_INVOICE = "api/home/sendInvoiceMail";
    //    开发票
    public static final String INVOICE_DRAW_UP = "api/goods/order/addInvoice";
    //    新版拼团首页
    public static final String GROUP_SHOP_JOIN_NEW_INDEX = "api/gp/getGpListV2";
    //    拼团首页轮播图
    public static final String GROUP_SHOP_LOOP_INDEX = "api/gp/getGpIndexAd";
    //    新人团判断
    public static final String GROUP_SHOP_JOIN_NRE_USER = "api/gp/getQualifications";
    //    拼团通用信息改版
    public static final String GROUP_SHOP_NEW_DETAILS = "api/gp/getGpInfoDetailInfoV2";
    //    新版创建拼团订单
    public static final String Q_CREATE_GROUP_NEW_INDENT = "api/goods/order/addGpOrderV2";
    //    新版我的拼团订单
    public static final String GROUP_MINE_NEW_INDENT = "api/gp/getMyGpInfoV2";
    //    获取拼团弹窗
    public static final String GROUP_GET_GP_POPUP = "api/gp/getGpPopup";
    //    商品评论点赞
    public static final String SHOP_EVA_LIKE = "api/goods/addEvaluateFavor";
    //    必买清单
    public static final String QUALITY_SHOP_BUY_DETAIL = "api/goods/mustbuy/getZone";
    //    历史清单
    public static final String QUALITY_SHOP_HISTORY_LIST = "api/goods/mustbuy/getoverdueZone";
    //    历史清单详情
    public static final String QUALITY_SHOP_HISTORY_LIST_DETAIL = "api/goods/mustbuy/getZonebyId";
    //    历史清单商品列表
    public static final String QUALITY_SHOP_HISTORY_LIST_PRO = "api/goods/mustbuy/newGetGoods";

    //    每周精选详情
    public static final String QUALITY_WEEK_OPTIMIZED_DETAIL = "api/goods/weeklypreferred/getZone";
    //    每周精选列表
    public static final String QUALITY_WEEK_OPTIMIZED_PRO = "api/goods/weeklypreferred/getGoods";
    //    良品侧栏分类
    public static final String QUALITY_SHOP_TYPE = "api/goods/category/getCategoryListV2";
    //    新版首页分类
    public static final String HOME_CATERGORY_ONE_LIST = "api/goods/category/getCategoryLevelOneList";
    //    良品好物
    public static final String QUALITY_SHOP_GOODS_PRO = "api/goods/greate/getGreateGoodList";
    //    良品根据父类获取分类栏目
    public static final String Q_PRODUCT_TYPE = "api/goods/category/getCategoryByParent";
    //    良品分类 一级分类
    public static final String QUALITY_CATEGORY_TYPE = "api/goods/category/getCategoryLevelOneList";
    //    良品分类排序
    public static final String Q_SORT_TYPE = "api/goods/category/getCategoryOrderType";
    //    良品分类商品列表
    public static final String Q_PRODUCT_TYPE_LIST = "api/goods/getCategoryGoods/getCategoryProduct";
    //    优惠券对应的商品列表
    public static final String Q_COUPON_PRODUCT_TYPE_LIST = "api/goods/getCategoryGoods/getCategoryProductByCoupon";
    //    优惠券类别商品
    public static final String Q_COUPON_PRODUCT_LIST = "api/my/coupon/getCouponAvailableProductList";
    //    domo推荐
    public static final String Q_SP_DETAIL_DOMO_RECOM = "api/my/coupon/getCouponRecommendList";
    //    商品详情推荐
    public static final String Q_SP_DETAIL_RECOMMEND = "api/goods/getProductDetailRecommend";
    //    商品详情文章主题推荐
    public static final String Q_SP_DETAIL_TOPIC_RECOMMEND = "api/goods/getProductDetailDocumentListV2";
    //    商品收藏
    public static final String Q_SP_DETAIL_PRO_COLLECT = "api/home/addGoodsCollect";
    //    服务承诺
    public static final String Q_SP_DETAIL_SERVICE_COMMITMENT = "api/goods/order/getProductPromise";
    //    组合商品基本信息
    public static final String Q_GROUP_GOODS_BASIC = "api/goods/getCombineBasicInfo";
    //    组合商品详细信息
    public static final String Q_GROUP_GOODS_DETAIL = "api/goods/getCombineDetailInfo";

    //    海外直邮主题列表
    public static final String QUALITY_OVERSEAS_THEME = "api/goods/overseas/getTopics";
    //    海外直邮商品列表
    public static final String QUALITY_OVERSEAS_LIST = "api/goods/overseas/getGoods";
    //    海外直邮主题详情
    public static final String QUALITY_OVERSEAS_THEME_DETAIL = "api/goods/overseas/getTopic";
    //    海外直邮详情详情商品列表
    public static final String QUALITY_OVERSEAS_DETAIL_LIST = "api/goods/overseas/getTopicGoods";
    //    新人专区商品列表
    public static final String QUALITY_NEW_USER_LIST = "api/goods/newuser/getGoods";
    //    新人领取优惠券礼包
    public static final String QUALITY_NEW_USER_GET_COUPON = "api/goods/newuser/getCouponByNewUser";
    //    新人首单0元购商品
    public static final String QUALITY_NEW_USER_FIRST = "api/goods/newuser/getNewUserFirstActivityGoods";
    //    热销单品时间轴
    public static final String QUALITY_HOT_SALE_SHAFT = "api/goods/getHotGoodsDay";
    //    标签详情商品
    public static final String QUALITY_PRODUCT_LABEL = "api/goods/getProductListByLabelId";
    //    新版热销单品 时间轴
    public static final String QUALITY_HOT_SALE_LIST_NEW = "api/goods/getHotGoodsNew";
    //    多么研究所
    public static final String Q_DML_SEARCH_LIST = "api/goods/lifesearch/getDocumentList";
    //    多么优选评论
    public static final String Q_DML_SEARCH_COMMENT = "api/find/getNewFindCommentList";
    //    评论列表详情
    public static final String Q_COMMENT_DETAILS = "api/find/getAllCommentByCommentId";
    //    多么优选
    public static final String Q_DML_OPTIMIZED_LIST = "api/goods/super/getSuperGoodList";
    //    多么优选详情
    public static final String Q_DML_OPTIMIZED_DETAIL = "api/goods/super/getSuperGoodDetails";
    //    新品发布时间轴
    public static final String Q_NEW_PRO_TIME_SHAFT = "api/goods/new/release/getNewReleaseTimes";
    //    新品发布
    public static final String Q_NEW_PRO_LIST = "api/goods/new/release/getNewReleaseGoods";
    //    营销活动专场
    public static final String Q_ACT_PRO_LIST = "api/goods/activity/getDiscountActivityGoods";
    //    良品自定义专区
    public static final String Q_CUSTOM_PRO_LIST = "api/goods/greate/getZoneProductList";
    //    良品自定义专区封面
    public static final String Q_CUSTOM_PRO_COVER = "api/goods/greate/getNewGreateTypeAd";
    //    支付完成商品推荐
    public static final String Q_PAY_SUCCESS_PRODUCT = "api/goods/paySuccessProductRecomment";
    //    整点秒时间轴
    public static final String Q_POINT_SPIKE_TIME_SHAFT = "api/goods/hoursActivity/getActivityTimeAxis";
    //    整点秒杀轮播位
    public static final String Q_POINT_SPIKE_AD = "api/goods/getBannerAd";
    //    整点秒商品
    public static final String Q_POINT_SPIKE_PRODUCT = "api/goods/activity/getActivityTimeAxisPorduct";
    //    整点秒杀商品点击统计
    public static final String Q_POINT_SPIKE_PRODUCT_CLICK_TOTAL = "api/goods/activity/addPorductClick";
    /**
     * 多么福利社
     */
    //主题列表
    public static final String H_DML_THEME = "api/hot/welfare/getTopics";
    //福利社推荐列表
    public static final String H_DML_RECOMMEND = "api/hot/welfare/getTopicGoodsPush";
    //    福利社主题详情
    public static final String H_DML_THEME_DETAIL = "api/hot/welfare/getNewTopic";
    //    往期福利社
    public static final String H_DML_PREVIOUS_THEME = "api/hot/welfare/getOverdueTopics";
    /**
     * 收货地址
     */
    //    读取默认地址
    public static final String DELIVERY_ADDRESS = "api/user/address/getDefaultAddress";
    //    地址详情
    public static final String ADDRESS_DETAILS = "api/user/address/getUserAddressDetail";
    //    添加地址
    public static final String ADD_ADDRESS = "api/user/address/addUserAddress";
    //    修改地址
    public static final String EDIT_ADDRESS = "api/user/address/updateUserAddress";
    //    地址列表
    public static final String ADDRESS_LIST = "api/user/address/getDocumentRemendList";
    //    修改默认地址
    public static final String UPDATE_DEFAULT_ADDRESS = "api/user/address/updateDefaultAddress";
    //    删除地址
    public static final String DEL_ADDRESS = "api/user/address/delUserAddress";
    //    修改订单收货地址
    public static final String CHANGE_ORDER_ADDRESS = "api/goods/order/editAddress";
    //    收货地址智能识别
    public static final String DISCERN_ADDRESS_INFO = "api/area/discernAddressInfo";


    //  文章 产品详情点赞取消
    public static final String F_ARTICLE_DETAILS_FAVOR = "api/find/addDocumentFavor";
    //    文章收藏
    public static final String F_ARTICLE_COLLECT = "api/home/addDoucmentCollect";
    //  文章详情
    public static final String F_INVITATION_DETAIL = "api/find/getFindDocumentDetails";
    //  自营商品优惠券领取
    public static final String FIND_ARTICLE_COUPON = "api/my/coupon/receiveCoupon";
    //    优惠券礼包领取
    public static final String FIND_COUPON_PACKAGE = "api/my/coupon/sendCouponPackage";
    //  点赞
    public static final String FIND_AND_COMMENT_FAV = "api/find/addCommentFavor";
    //     帖子标签关联列表
    public static final String FIND_RELEVANCE_TAG = "api/find/getPostByTag";
    //      帖子标签关联信息
    public static final String FIND_RELEVANCE_TAG_INFO = "api/find/getDocumentOtherInfoByTag";
    //    话题收藏
    public static final String F_TOPIC_COLLECT = "api/home/addFindTopic";
    //   添加文章评论帖子
    public static final String FIND_COMMENT = "api/find/newAddComment";
    //    添加限时特惠产品评论
    public static final String GOODS_COMMENT = "api/find/addComment";
    //    文章分享统计
    public static final String ARTICLE_SHARE_COUNT = "api/my/addDocumentShareCount";
    //   每日分享
    public static final String SHARE_SUCCESS = "api/activity/shareReward";

    /**
     * 新版发现相关接口
     */
    //   获取帖子列表
    public static final String GET_POST_LIST = "api/find/post/getPostList";
    //  个人主页帖子列表
    public static final String GET_USER_POST_LIST = "api/find/post/getHomePagePostList";
    //   获取热门话题
    public static final String GET_HOT_TOPIC = "api/find/post/getRecommendTopicList";
    //   获取话题分类
    public static final String GET_TOPIC_CATERGORY = "api/find/post/getAllTopicCategory";
    //   获取分类话题
    public static final String GET_CATERGORY_TOPIC = "api/find/post/getAllTopic";
    //   获取话题详情
    public static final String GET_TOPIC_DETAIL = "api/find/post/getTopicInfo";
    //   获取评价相关字段
    public static final String GET_EVALUATE_TIP = "api/find/post/getEvaluateTip";
    //   获取可评价商品列表
    public static final String GET_SCORE_PRODUCT = "api/find/post/getNeedCommentProductList";
    //   待评价商品订单列表
    public static final String GET_WAIT_EVALUATE_PRODUCTS = "api/goods/order/getWaitEvaluateOrderProductList";
    //   商品晒单（发布帖子并评价）
    public static final String PUBLISH_POST_ANDE_VALUATE = "api/find/post/publishPostAndEvaluate";
    //   参与话题
    public static final String JOIN_TOPIC = "api/find/post/savePost";
    //   举报非法内容
    public static final String REPORT_ILLEGAL = "api/find/post/reportIllegalContent";
    //   新增粉丝
    public static final String NEW_FANS = "api/find/post/getNewFans";
    //    奖励规则H5地址
    public static final String REWARD_RULE = Url.BASE_SHARE_PAGE_TWO + "find_template/rewardRule.html?hideNav=1";
    //    获取商品详情更多评论
    public static final String GET_PRODUCT_POST = "api/find/post/getProductPost";
    //    抽奖页面
    public static final String LOTTERY_URL = "https://www.domolife.cn/m/template/home/lottery.html";
    //    分享保存图片
    public static final String SHARE_SAVE_IMAGE_URL = "api/miniprogram/createMiniProductSharePic";


    /*
      vip相关
     */
    // 获取权益接口
    public static final String GET_VIP_POWER = "api/vip/vipuser/getVipInviolableRightsList";
    // 获取会员专享优惠券列表
    public static final String GET_VIP_COUPON_LIST = "api/vip/vipuser/getVipCouponList";
    // 会员专享优惠券专区
    public static final String GET_VIP_COUPON_ZONE = "api/vip/vipuser/getVipCouponZone";
    // 获取会员日封面图
    public static final String GET_VIPDAYS_INFO = "api/vip/vipuser/getVipDaysInfo";
    // 获取月销满多少赠送会员
    public static final String GET_CONSUME_LARGESS_INFO = "api/vip/vipuser/getConsumeLargessInfo";
    // 获取会员信息接口
    public static final String GET_VIP_USER_INFO = "api/vip/vipuser/getVipUserInfo";
    // 获取会员购买结算信息
    public static final String GET_VIP_SETTLEINFO = "api/vip/vipuser/getVipCardSettleInfo";
    // 创建订单
    public static final String SUBMIT_VIP_USER = "api/vip/vipuser/submitVipUser";
    // 省钱计算器
    public static final String GET_BEECONOMICAL = "api/vip/vipuser/getBeEconomical";
    // 获取每周会员特价商品
    public static final String GET_WEEK_GOODS = "api/vip/vipuser/getWeekVipGoodsList";
    // 获取0元试用活动信息
    public static final String GET_ZERO_ACTIVITY_INFO = "api/vip/vipuser/getZeroActivityCoverInfo";
    // 0元试用活动详情
    public static final String GET_ZERO_PRODUCT_DETAIL = "api/vip/zeroActivity/getZeroProductDetail";
    // 获取0元试用活动列表
    public static final String GET_ZERO_LIST = "api/vip/zeroActivity/getZeroActivityList";
    // 获取0元试用活动列表
    public static final String GET_APPLY_ZERO = "api/vip/zeroActivity/applyZeroActivity";
    // 获取往期0元试用中奖名单
    public static final String GET_WINNERS_INFO_LIST = "api/vip/zeroActivity/getWinnersInfoList";
    // 获取开卡礼封面
    public static final String GET_CARD_GIFT_INFO = "api/vip/vipuser/getBuyCardGiftInfo";
    // 获取0元试用申请列表
    public static final String GET_MY_APPLY_LIST = "api/vip/zeroActivity/getMyApplyList";
    // 每周会员/会员日专区详情
    public static final String GET_VIP_ZONE_DETAIL = "api/vip/vipuser/getVipZoneDetail";
    // 0元试用订单结算信息
    public static final String GET_ZERO_SETTLE_INFO = "api/vip/zeroActivity/getZeroOrderSettleInfo";
    // 提交0元试用订单
    public static final String CREATE_ZERO_ORDER = "api/vip/zeroActivity/createZeroOrder";
    // 提交0元试用报告
    public static final String SUBMIT_ZERO_REPORT = "api/vip/zeroActivity/submitReport";
    // 获取0元试用心得列表
    public static final String GET_REPORT_LIST = "api/vip/zeroActivity/getActivityReportList";
    // 获取0元试用报告详情
    public static final String GET_REPORT_DETAIL = "api/vip/zeroActivity/getActivityReportDetail";
    // 报告点赞
    public static final String REPORT_FAVOR = "api/vip/zeroActivity/reportFavor";
    // 报告收藏
    public static final String REPORT_COLLECT = "api/vip/zeroActivity/reportCollect";
    // 获取我的报告
    public static final String GET_MY_REPORT = "api/vip/zeroActivity/getMyReport";
    // 0元试用订单详情
    public static final String GET_ZERO_INDENT_DETAIL = "api/vip/zeroActivity/getZeroOrderDetail";
    // 0元试用订单详情-去支付
    public static final String GET_ZERO_GO_PAY = "api/vip/zeroActivity/goPay";
    // 会员购买记录
    public static final String GET_VIP_RECORD = "api/vip/vipuser/getVipRecord";
    // 0元订单退款去向
    public static final String GET_ZERO_REFUND_INFO = "api/vip/zeroActivity/getRefundGoInfo";
    // 分享有礼
    public static final String GET_VIP_SHARE_INFO = "api/vip/vipuser/getVipShareInfo";
    // 获取邀请列表（分享有礼）
    public static final String GET_VIP_INVITE_LIST = "api/vip/vipuser/getMyVipInviteList";
    // 心得收藏列表
    public static final String GET_COLLECT_REPORT_LIST = "api/vip/zeroActivity/getCollectReportList";
    // 省钱规则/分享有礼规则
    public static final String GET_VIP_REALTED_RULE = "api/my/remind/getReminText";
    // 获取多么会员价商品
    public static final String GET_VIP_PRICE_GOODS = "api/vip/vipuser/getVipPriceGoodsList";
    // 获取会员最爱买
    public static final String GET_VIP_LIKE_GOODS = "api/vip/vipuser/getVipLikeGoodsList";
    // 获取会员专享价专区id和标题
    public static final String GET_VIP_EXCLUSIVE_TITLE = "api/vip/vipuser/getVipPriceExclusiveGoodsTitle";
    // 获取会员专享价专区商品列表
    public static final String GET_VIP_EXCLUSIVE_GOODS_LIST = "api/vip/vipuser/getVipPriceExclusiveGoodsList";
    // 0元试用订单详情-查看物流
    public static final String GET_ZERO_ORDER_LOGISTICS = "api/vip/zeroActivity/getZeroOrderLogistics";


    /*
    淘好货相关
    */
    //获取所有团购商品以及品牌团
    public static final String GET_ALL_GROUP_INFO = "api/groupbuy/getAllGroupBuyInfo";
    //获取团购首页帖子
    public static final String GET_TIME_DOCUMENT_LIST = "api/groupbuy/getGrupBuyHomePageDocumentList";
    //获取品牌团详情
    public static final String GET_BRAND_DETAILS = "api/groupbuy/getGorupBuyTopic";
    //获取团购商品详情
    public static final String GET_GROUP_PEODUCT_DETAILS = "api/groupbuy/getGorupBuyProductInfo";
    //获取团购种草帖子分类
    public static final String GET_GROUP_CATEGORY_LIST = "api/groupbuy/getGorupBuyCategoryList";
    //获取团购种草帖子列表
    public static final String GET_CATEGORY_DOCLIST_PAGE = "api/groupbuy/getCategoryDocListPage";
    //获取团购种草帖子详情
    public static final String GET_DOCUMENT_DETAILLIST_PAGE = "api/groupbuy/getDocumentDetailListPage";
    //统计淘好货帖子/帖子内商品点击量
    public static final String SAVE_DOCUMENT_DATA = "api/groupbuy/saveDocumentData";
    //获取我的评论消息列表
    public static final String GET_MY_COMMENT_MESSAGE_LIST = "api/message/getMyCommentMessageList";
    //获取我的赞消息列表
    public static final String GET_MY_FAVOR_MESSAGE_LIST = "api/message/getMyFavorMessageList";
    //获取拼团规则
    public static final String GET_GROUP_RULE = "api/groupbuy/getGroupBuyExplain";
    //获取定金结算信息
    public static final String GET_DEPOSIT_SETTLE_INFO = "api/goods/order/getDepositOrderSettleInfo";

    /*
    视频列表相关
    */
    //获取视频列表
    public static final String GET_VIDEO_LIST = "api/find/video/getApilistPage";
    //获取视频详情
    public static final String GET_VIDEO_DETAIl = "api/find/video/getApiVideoInfo";
    //视频统计
    public static final String ADD_CLICK_VIDEO = "api/find/video/addClickVideo";
    //视频点赞
    public static final String ADD_VIDEO_COLLECT = "api/find/video/addVideoCollect";
    //视频收藏列表
    public static final String GTE_VIDEO_COLLECT = "api/my/getMyCollectVideolistPage";


    /*
    广告相关
     */
    //我 广告
    public static final String MINE_PAGE_AD = "api/home/getMyBottomAdTopic";
    //发现广告图
    public static final String FIND_AD = "api/website/getAdByFind";
    //良品分类广告
    public static final String Q_QUALITY_TYPE_AD = "api/goods/getAdByGoodsCategory";
    //新品轮播广告
    public static final String Q_NEW_PRO_AD = "api/goods/new/release/getNewReleaseGoodAds";
    //热销单品广告位
    public static final String QUALITY_HOT_SALE_AD = "api/home/getHotProductAd";
    //支付完成 弹窗广告
    public static final String Q_PAY_SUCCESS_AD_DIALOG = "api/goods/order/getAdByOrderPayOver";
    //已购买商品列表界面 广告位
    public static final String FIND_AD2 = "api/website/getAdByFindTopic";
    //获取首页广告
    public static final String GET_TIME_HOME_AD = "api/groupbuy/getHomeTopBannerAd";
    //获取推荐商品广告
    public static final String GET_RECOMMNED_AD = "api/groupbuy/getHomeDomoBanner";
    //首页,良品浮窗广告
    public static final String H_Q_FLOAT_AD = "api/home/getAdByHomeFloat";
    //首页广告轮播图
    public static final String H_AD_LIST = "api/home/getHomeAdList";
    //弹窗广告
    public static final String H_AD_DIALOG = "api/home/getAdByPopup";
    //启动广告
    public static final String H_LAUNCH_AD_DIALOG = "api/home/getAdByStart";
    //启动广告
    public static final String GET_GROUP_AD = "api/goods/order/getAdByGpSharePopup";

    /*
    问答相关
    */
    //获取商品的问题列表
    public static final String GET_QUESTION_LIST = "api/question/getProductQuestionList";
    //获取问题详情
    public static final String GET_QUESTION_DETAIL = "api/question/getProductQuestionDetail";
    //关注问题
    public static final String FOLLOW_QUESTION = "api/question/replyFollow";
    //删除回答
    public static final String DELETE_ANSWER = "api/question/delReply";
    //删除问题
    public static final String DELETE_QUESTION = "api/question/delQuestion";
    //点赞回答
    public static final String FAVOR_ANSWER = "api/question/replyFavor";
    //提问问题
    public static final String ASK_QUESTION = "api/question/submitQuestion";
    //回答问题
    public static final String ANSWER_QUESTION = "api/question/submitReply";
    //获取与我相关的问题列表
    public static final String GET_MY_QUESTION_LIST = "api/question/getMyRelatedQuestionList";
    //评论完成抽奖
    public static final String DRAW_EVALUATE_PRIZE = "api/find/post/drawEvaluatePrize";
    //评论详情
    public static final String GET_EVALUATE_DETAIL = "api/find/post/getEvaluateDetail";
    //记录选中的sku
    public static final String SELECT_PRODUCT_SKU = "api/goods/userSelectProductSku";
    //奖励提现
    public static final String SUB_CASH_APPLY = "api/vip/vipuser/subCashApply";
}
