package com.amkj.dmsh.constant;

import android.content.Context;

/**
 * Created by atd48 on 2016/8/27.
 */
public class Url {

    public static String BASE_URL = "https://app.domolife.cn/";

    public Url(Context context, int position) {
        if (context != null) {
            if (position == 0) {
                BASE_URL = "https://app.domolife.cn/";
            } else if (position == 1) {
                BASE_URL = "http://ts.domolife.cn/";
            } else if (position == 2) {
                BASE_URL = "http://192.168.1.98:8080/";
            } else if (position == 3) {
                BASE_URL = "http://192.168.1.180:8080/";
            } else if (position == 4) {
                BASE_URL = "http://192.168.1.212/";
            } else if (position == 6) {
                BASE_URL = "http://dev.domolife.cn/";
            } else if (position == 7) {
                BASE_URL = "http://192.168.1.87:8080/";
            } else if (position == 8) {
                BASE_URL = "http://192.168.1.128:8080/";
            }
        }
    }


    /**
     * 首页
     */
    //    首次启动获取push信息
    public static final String FIRST_PUSH_INFO = "api/my/sendAppPushRule";
    //    统计首次安装设备信息
    public static final String FIRST_INSTALL_DEVICE_INFO = "/api/home/firstInstallDeviceInfo";
    //    统计推送
    public static final String TOTAL_PUSH_INFO = "api/userbehavioursts/collectMessagePushInfo";
    //    上传数据容量
    public static final String TOTAL_UP_SIZE = "api/userbehavioursts/getCollectSize";
    //    统计数据上传
    public static final String TOTAL_DATA_UP = "api/userbehavioursts/collectBehaviourData";
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
    //    第三方登录
    public static final String MINE_OTHER_ACCOUNT = "api/my/syncLogin";
    //账号安全，第三方账号绑定手机
    public static final String MINE_BIND_ACCOUNT_MOBILE = "api/my/syncLoginBindMobile";
    //    首页文章分类列表
    public static final String H_CATEGORY_LIST = "api/home/getNewCategoryList";
    //    首页热门活动列表
    public static final String H_HOT_ACTIVITY_LIST = "api/home/getAdByActivity?version=3";
    //    首页专区广告
    public static final String H_REGION_ACTIVITY = "api/home/getHomeAdTopic";
    //    首页,良品浮窗广告
    public static final String H_Q_FLOAT_AD = "api/home/getAdByHomeFloat";
    //    首页 跑马灯效果
    public static final String H_Q_MARQUEE_AD = "api/search/getRollingNotice";
    //    详情分类文章列表
    public static final String CATE_DOC_LIST = "api/home/getDocumentHomeList";
    //    首页广告轮播图
    public static final String H_AD_LIST = "api/home/getHomeAdList";
    //是否有新消息提醒
    public static final String H_MESSAGE_WARM = "api/message/remind";
    //    弹窗广告
    public static final String H_AD_DIALOG = "api/home/getAdByPopup";
    //    统计字段
    public static final String APP_TOTAL_ACTION = "api/userbehavioursts/getActList";
    //    启动广告
    public static final String H_LAUNCH_AD_DIALOG = "api/home/getAdByStart";
    //    OSS配置
    public static final String H_OSS_CONFIG = "api/oss/getDetail";
    //    地址配置版本
    public static final String H_ADDRESS_VERSION = "api/area/getVersion";
    //    七鱼客服获取
    public static final String XN_SERVICE = "api/xiaoneng/getReceptionGroups";
    //    底栏导航
    public static final String H_BOTTOM_ICON = "api/appindex/getAppIndexConfig";
    //    地址配置数据
    public static final String H_ADDRESS_DATA = "api/area/getArea";
    //    添加抽奖次数
    public static final String H_HOT_ACTIVITY_ADD_LOTTERY = "api/turn/addTurnCount";
    //    获取搜索热门标签
    public static final String H_HOT_SEARCH_LIST = "api/search/getHotSearchLinkList";
    //    搜索产品
    public static final String H_HOT_SEARCH_PRODUCT = "api/search/searchGoodslist";
    //    搜索产品类目推荐
    public static final String H_SEARCH_PRODUCT_RECOMMEND = "api/search/searchRecommendCategory";
    //    搜索产品无商品推荐
    public static final String H_SEARCH_PRODUCT_GOOD = "api/search/searchRecommendGoodThings";
    //    搜索专题
    public static final String H_HOT_SEARCH_SPECIAL = "api/search/searchDucumentAndTopiclist";
    //    搜索帖子
    public static final String H_HOT_SEARCH_INVITATION = "api/search/searchPostlist";
    //    搜索用户
    public static final String H_HOT_SEARCH_USER = "api/search/searchMemberlist";
    //    搜索话题
    public static final String H_HOT_SEARCH_TOPIC = "api/search/searchTopiclist";
    //  搜索留言
    public static final String SEARCH_LEAVE_MES = "api/search/saveAdvise";
    //  新版分享
    public static final String BASE_SHARE_PAGE_TWO = "https://www.domolife.cn/";
    //    文章详情公共分享
    public static final String SHARE_COMMUNAL_ARTICLE = "api/reminder/getSharePrompt";
    //签到详情
    public static final String H_ATTENDANCE_D = "api/activity/selectSignPageDetail";
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
    //限时特惠详情
    public static final String H_TIME_GOODS_DETAILS = "api/goods/flashSale/getGoods";
    //消息统计
    public static final String H_MES_STATISTICS = "api/message/messageTotal";
    //app最后登录时间
    public static final String H_LOGIN_LAST_TIME = "api/my/updateLastLogin";
    //    新人优惠弹窗
    public static final String H_NEW_USER_COUPON = "api/usertype/judgeUserType";
    //    统计点击商品数
    public static final String TOTAL_PRO_NUM = "api/goods/addDocumentProductNum";
    //    统计福利社点击商品数
    public static final String TOTAL_WELFARE_PRO_NUM = "api/goods/addTopicProductNum";
    //    统计官方通知点击商品数
    public static final String TOTAL_OFFICIAL_PRO_NUM = "api/goods/addCommProductNum";
    //    统计广告点击
    public static final String TOTAL_AD_COUNT = "api/home/addAdClickCount";
    //    弹窗广告
    public static final String TOTAL_AD_DIALOG_COUNT = "api/home/addMarketingAdClickCount";
    //    统计极光消息打开数目
    public static final String TOTAL_JPUSH_COUNT = "api/home/addPushClickNum";
    //    官方详情
    public static final String H_MES_OFFICIAL = "api/message/newOfficialCommentList";
    //    订单物流信息
    public static final String H_MES_INDENT = "api/message/orderMessageList";
    //    二期通知消息
    public static final String H_MES_NOTIFY = "api/message/newMsgMessageList";
    //      官方通知详情
    public static final String H_MES_OFFICIAL_DETAILS = "api/message/getOfficialCommentDetails";
    //    平台通知
    public static final String H_MES_PLATFORM_DETAILS = "api/message/getMsgDetail";
    //     评论消息
    public static final String H_MES_COMMENT = "api/message/commentMessageList";
    //     消息赞
    public static final String H_MES_LIKED = "api/message/favorMessageList";
    //    积分规则
    public static final String H_ATT_INTEG = "api/reminder/getSnigInfo";
    //    发现广告图
    public static final String FIND_AD = "api/website/getAdByFind";
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

    /**
     * 我的
     */
    //我的首页
    public static final String MINE_PAGE = "api/my/getPersonalData";
    //    我-底部宫格数据
    public static final String MINE_BOTTOM_DATA = "api/my/getMyDefinedIcon";
    //    查询信息
    public static final String MINE_PAGE_POST = "api/my/getPersonalData";
    //    我 广告
    public static final String MINE_PAGE_AD = "api/home/getMyBottomAdTopic";
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
    //会员宝宝
    public static final String MINE_GET_BABY_DATA = "api/my/getMemberBabyList";
    //修改会员宝宝
    public static final String MINE_UPDATE_BABY_DATA = "api/my/updateMemberBaby";
    //修改会员宝宝
    public static final String MINE_ADD_BABY_DATA = "api/my/addMemberBaby";
    //删除宝宝信息
    public static final String MINE_DEL_BABY_DATA = "api/my/deleteMemberBaby";
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
    //    是否是第一次登录App
    public static final String FIRST_LOGIN_APP = "api/my/coupon/firstAppLoginCouponConfig";
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
    /**
     * 我的赞列表
     */
    //     文章删除
    public static final String MINE_INVITATION_DEL = "api/my/deleteDocument";
    //     用户首页信息
    public static final String USER_PAGE_INFO = "api/my/getPersonalData";
    //    收藏帖子
    public static final String COLLECT_INVITATION = "api/my/getMyCollectPostListV2";
    //  收藏专题(文章)
    public static final String COLLECT_SPECIAL = "api/my/getMyCollectDocumentList";
    //    收藏话题
    public static final String COLLECT_TOPIC = "api/my/getMyCollectFindTopiclist";
    //    我的帖子列表
    public static final String MINE_INVITATION_LIST = "api/my/getMyPostListV2";
    //    用户帖子列表
    public static final String USER_INVITATION_LIST = "api/my/getUserPostListV2";
    /**
     * 限时特惠
     */
    //限时特惠 品牌团专题
    public static final String H_TIME_BRAND_DETAILS = "api/goods/flashSale/topic/get";
    //限时特惠 主题详情推荐商品列表
    public static final String H_TIME_BRAND_DETAILS_REC = "api/goods/flashSale/topic/getGoods";
    //    限时特惠 新
//    限时特惠时间轴
    public static final String TIME_SHOW_SHAFT = "api/goods/flashSale/getTimeAxisV2";
    //    限时特惠 商品列表
    public static final String TIME_SHOW_PRODUCT_TOPIC_SHAFT = "api/goods/flashSale/getNewFlashSaleGoodsV2";
    //    限时特惠top推荐
    public static final String TIME_SHOW_PRO_TOP_PRODUCT = "api/goods/flashSale/getGoodsRecommend";
    //    是否设置过提醒
    public static final String TIME_SHOW_PRO_WARM = "api/my/remind/getHadRemind";
    //    设置提醒时间
    public static final String TIME_WARM_PRO = "api/my/remind/updateMyRemind";
    //    限时特惠商品购买点击统计
    public static final String TIME_PRODUCT_CLICK_TOTAL = "api/userbehavioursts/collectFlashSaleProductBuyClick";
    /**
     * 积分商城
     */
    //积分筛选
    public static final String H_INTEGRAL_PRODUCT_FILTRATE = "api/hot/intergral/getGoodsListV2";
    //积分详情
    public static final String H_INTEGRAL_DETAILS_GOODS = "api/hot/intergral/getGoodsV2";
    //积分列表
    public static final String INTEGRAL_INDENT_LIST = "api/integralorder/getIntegralOrders";
    //    积分订单结算信息
    public static final String INTEGRAL_DIRECT_SETTLEMENT = "api/integralorder/getIntegralOrderSettleInfo";
    //    积分订单详情
    public static final String INDENT_INTEGRAL_DETAILS = "api/integralorder/getIntegralOrderDetail";
    //    服务承诺
    public static final String INTEGRAL_PRODUCT_SERVICE = "api/activity/getIntegralProductServicePromise";

    /**
     * 良品
     */
    //首页轮播图
    public static final String Q_HOME_AD_LOOP = "api/goods/getAd";
    //    二期订单创建
    public static final String Q_CREATE_INDENT = "api/goods/order/addOrderNew";
    //    积分订单创建
    public static final String INTEGRAL_CREATE_INDENT = "api/integralorder/addIntegralOrder";
    //    再次购买
    public static final String Q_RE_BUY_INDENT = "api/goods/order/againBuy";
    //订单付款
    public static final String Q_PAYMENT_INDENT = "api/goods/order/buy";
    //    银联支付手动回调
    public static final String Q_UNIONPAY_PAYMENT_INDENT = "api/goods/order/abcOrderQuery";
    //订单支付失败提示
    public static final String PAY_ERROR = "api/reminder/getPayErorr";
    //    订单支付取消提示
    public static final String PAY_CANCEL = "api/reminder/getLeaveConfirm";
    //    购物车结算金额
    public static final String PRO_SETTLE_PRICE = "api/goods/cart/changeCartCheckStatus";
    //订单详情
    public static final String Q_INDENT_DETAILS = "api/goods/order/getOrderNew";
    //订单删除
    public static final String Q_INDENT_DEL = "api/goods/order/del";
    //订单取消 3.1.5 积分 商品 订单通用
    public static final String Q_INDENT_CANCEL = "api/goods/order/cancel";
    //订单确定
    public static final String Q_INDENT_CONFIRM = "api/goods/order/takeDelivery";
    //发布评价
    public static final String Q_SEND_APPRAISE = "api/goods/order/evaluate";
    //查询良品全部订单
    public static final String Q_INQUIRY_ALL_ORDER = "api/goods/order/getOrders";
    //查询良品已完成订单
    public static final String Q_INQUIRY_FINISH = "api/goods/order/getWaitEvaluate";
    //查询良品待发货订单
    public static final String Q_INQUIRY_WAIT_SEND = "api/goods/order/getWaitDelivery";
    //    待发货 催单
    public static final String Q_INQUIRY_WAIT_SEND_EXPEDITING = "api/goods/order/urgeOrder";
    //查询良品待付款订单
    public static final String Q_INQUIRY_WAIT_PAY = "api/goods/order/getWaitPay";
    //查询良品已发货订单
    public static final String Q_INQUIRY_DEL_IVERED = "api/goods/order/getWaitTakeDelivery";
    //申请退款
    public static final String Q_INDENT_REFUND = "api/goods/order/getApplyRefundInfo";
    //申请取消订单 待发货
    public static final String Q_CANCEL_INDENT_REFUND = "api/goods/order/cancelRefund";
    //查询物流
    public static final String Q_CONFIRM_LOGISTICS = "api/goods/order/viewLogistics";
    //查询包裹
    public static final String Q_CONFIRM_PACKET = "api/goods/order/viewPackage";
    //    退款售后 二期
    public static final String Q_APPLY_AFTER_SALE_REPLY_RECORD = "api/goods/order/getAfterSale";
    //    退款详情
    public static final String Q_INDENT_REFUND_DETAIL = "api/goods/order/getRefundDetailInfo";
    //    整单退，退款去向
    public static final String Q_INDENT_DETAIL_REFUND = "api/goods/order/getOrderRefundDetailInfo";
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
    //    维修物流查询
    public static final String Q_INDENT_REPAIR_LOGISTIC = "api/goods/order/viewRepairLogistics";
    //    撤销申请
    public static final String Q_CANCEL_APPLY = "api/goods/order/undoApplyRefund";
    //    获取物流公司
    public static final String Q_INDENT_LOGISTIC_COM = "api/goods/order/getExpressCompanys";
    //    提交物流信息
    public static final String Q_INDENT_LOGISTIC_SUB = "api/goods/order/refundExpressNew";
    //    申请退款
    public static final String Q_INDENT_APPLY_REFUND = "api/goods/order/getApplyRefundInfo";
    //    申诉信息提交
    public static final String Q_INDENT_APPLY_REFUND_SUB = "api/goods/order/refundOrderProduct";
    //   修改申请
    public static final String Q_INDENT_CHANGE_REFUND_SUB = "api/goods/order/modifyApplyRefund";
    //  申诉查询
    public static final String Q_INDENT_APPLY_REFUND_CHECK = "api/goods/order/getVerifyRefundResultNew";

    //商品详情
    public static final String Q_SHOP_DETAILS = "api/goods/getGoods";
    //商品评价
    public static final String Q_SHOP_DETAILS_COMMENT = "api/goods/getEvaluate";
    //加入购物车 新版
    public static final String Q_SHOP_DETAILS_ADD_CAR = "api/goods/cart/addNew";
    //    新版购物车
    public static final String MINE_SHOP_CAR_GOODS = "api/goods/cart/getCartNew";
    //    购物车推荐商品
    public static final String MINE_SHOP_CAR_RECOMMEND_GOODS = "api/goods/cart/shopFullCartRecommend";
    //    购物车无商品推荐
    public static final String SHOP_CART_RECOMMEND_EMPTY_GOODS = "api/goods/cart/shopEmptyCartRecommend";
    //查询购物车数量
    public static final String Q_QUERY_CAR_COUNT = "api/goods/cart/getNumber";
    //    添加取消到货通知
    public static final String Q_REPLENISHMENT_NOTICE = "api/goods/addUserSkuNotice";
    //    查询订单数量
    public static final String Q_QUERY_INDENT_COUNT = "api/goods/order/getOrderNum";
    //购物车修改 新版
    public static final String Q_SHOP_DETAILS_CHANGE_CAR = "api/goods/cart/changeNew";
    //购物车删除
    public static final String Q_SHOP_DETAILS_DEL_CAR = "api/goods/cart/del";
    //获取商品属性
    public static final String Q_SHOP_DETAILS_GET_SKU_CAR = "api/goods/cart/getSaleSku";
    //优惠券展示 新版
    public static final String Q_SHOP_DETAILS_COUPON = "api/my/coupon/getNewMyCouponList";
    //自选优惠券
    public static final String Q_SELF_SHOP_DETAILS_COUPON = "api/my/coupon/choiceSelfCouponV2";
    //    订单优惠信息 新版
    public static final String INDENT_DISCOUNTS_INFO = "api/goods/order/getOrderSettleInfoNew";
    //    订单结算价格 新
    public static final String INDENT_SETTLE_PRICE = "api/goods/order/getTotalPriceByCouponNew";
    //    订单搜索
    public static final String INDENT_SEARCH = "api/goods/order/searchOrder";
    //    发票提示
    public static final String INDENT_DRAW_UP_INVOICE = "api/reminder/getReminderAsInvoice";
    //    发票详情
    public static final String INVOICE_DETAIL = "api/goods/order/getInvoice";
    //    开发票
    public static final String INVOICE_DRAW_UP = "api/goods/order/addInvoice";
    //    全部团购
    public static final String GROUP_SHOP_JOIN_ALL = "api/gp/getAllDomoGpInfo";
    //    拼团首页
    public static final String GROUP_SHOP_JOIN_INDEX = "api/gp/getDomoGpIndexInfo";
    //    拼团首页轮播图
    public static final String GROUP_SHOP_LOOP_INDEX = "api/gp/getGpIndexAd";
    //    拼团商品详细信息
    public static final String GROUP_SHOP_COMMUNAL = "api/gp/getDomoGpExtraInfo";
    //    新人团判断
    public static final String GROUP_SHOP_JOIN_NRE_USER = "api/gp/getQualifications";
    //    拼团通用信息
    public static final String GROUP_SHOP_DETAILS = "api/gp/getGpInfoDetailInfo";
    //    拼团列表
    public static final String GROUP_SHOP_OPEN_PERSON = "api/gp/getDomoCreateGpInfp";
    //创建拼团订单
    public static final String Q_CREATE_GROUP_INDENT = "api/goods/order/addGpOrder";
    //我的拼团订单
    public static final String GROUP_MINE_INDENT = "api/gp/getMyGpInfo";
    //  分享参团信息
    public static final String GROUP_MINE_SHARE = "api/gp/inviteToGp";
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

    //    良品横向
    public static final String QUALITY_SHOP_HOR_TYPE = "api/goods/navbarconfg/getNavbarConfig";
    //    良品侧栏分类
    public static final String QUALITY_SHOP_TYPE = "api/goods/category/getCategoryListV2";
    //    良品好物
    public static final String QUALITY_SHOP_GOODS_PRO = "api/goods/greate/getGreateGoodList";
    //    良品首栏分类入口
    public static final String Q_HOME_CLASS_TYPE = "api/goods/index/getProductUpIndex";
    //    良品中间栏分类入口
    public static final String Q_HOME_CENTER_TYPE = "api/goods/index/getProductMiddleIndex";
    //    良品根据父类获取分类栏目
    public static final String Q_PRODUCT_TYPE = "api/goods/category/getCategoryByParent";
    //    良品分类 一级分类
    public static final String QUALITY_CATEGORY_TYPE = "api/goods/category/getCategoryLevelOneList";
    //    良品分类排序
    public static final String Q_SORT_TYPE = "api/goods/category/getCategoryOrderType";
    //    良品分类商品列表
    public static final String Q_PRODUCT_TYPE_LIST = "api/goods/getCategoryGoods/getCategoryProduct";
    //    优惠券类别商品
    public static final String Q_COUPON_PRODUCT_LIST = "api/my/coupon/getCouponAvailableProductList";
    //    良品分类广告
    public static final String Q_QUALITY_TYPE_AD = "api/goods/getAdByGoodsCategory";
    //    新品轮播广告
    public static final String Q_NEW_PRO_AD = "api/goods/new/release/getNewReleaseGoodAds";
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

    //海外直邮主题列表
    public static final String QUALITY_OVERSEAS_THEME = "api/goods/overseas/getTopics";
    //海外直邮商品列表
    public static final String QUALITY_OVERSEAS_LIST = "api/goods/overseas/getGoods";
    //海外直邮主题详情
    public static final String QUALITY_OVERSEAS_THEME_DETAIL = "api/goods/overseas/getTopic";
    //海外直邮详情详情商品列表
    public static final String QUALITY_OVERSEAS_DETAIL_LIST = "api/goods/overseas/getTopicGoods";
    //新人专区商品列表
    public static final String QUALITY_NEW_USER_LIST = "api/goods/newuser/getGoods";
    //    新人用券专区
    public static final String QUALITY_NEW_USER_COUPON_LIST = "api/goods/coupon/getCouponGoodsList";
    //    新人专区封面图
    public static final String QUALITY_NEW_USER_COVER = "api/goods/newuser/getNewUserImg";
    //    新人领取优惠券礼包
    public static final String QUALITY_NEW_USER_GET_COUPON = "api/goods/newuser/getCouponByNewUser";
    //    热销单品广告位
    public static final String QUALITY_HOT_SALE_AD = "api/home/getHotProductAd";
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
    //    支付完成 弹窗广告
    public static final String Q_PAY_SUCCESS_AD_DIALOG = "api/goods/order/getAdByOrderPayOver";
    //    支付完成商品推荐
    public static final String Q_PAY_SUCCESS_PRODUCT = "api/goods/paySuccessProductRecomment";
    //    整点秒时间轴
    public static final String Q_POINT_SPIKE_TIME_SHAFT = "api/goods/hoursActivity/getActivityTimeAxis";
    //    整点秒杀轮播位
    public static final String Q_POINT_SPIKE_AD = "api/goods/getBannerAd";
    //    整点秒商品
    public static final String Q_POINT_SPIKE_PRODUCT = "api/goods/activity/getActivityTimeAxisPorduct";
    //    设置整点秒商品状态
    public static final String Q_POINT_SPIKE_PRODUCT_STATUS = "api/goods/addHoursActivityProductNotice";
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
    /**
     * 发现
     */
//  获取标签
    public static final String F_TAGS_LIST = "api/find/recommendTagList";
    //    获取话题
    public static final String F_REL_TOPIC_LIST = "api/find/getTopicListAsDoc";
    //    订单 晒单赢积分
    public static final String F_REL_INDENT_PRO_LIST = "api/goods/order/getBaskOrderInfo";
    //    发布 关联商品
    public static final String RELEASE_RELEVANCE_PRODUCT = "api/find/getGoodsList";
    //  发布图文
//  相册文章
    public static final String F_SEND_INVITATION = "api/find/savePost";
    //  发布文章
    public static final String F_SEND_ARTICLE = "api/find/saveDocumentDoc";
    //  文章 产品详情点赞取消
    public static final String F_ARTICLE_DETAILS_FAVOR = "api/find/addDocumentFavor";
    //    文章收藏
    public static final String F_ARTICLE_COLLECT = "api/home/addDoucmentCollect";
    //  文章详情
    public static final String F_INVITATION_DETAIL = "api/find/getFindDocumentDetails";
    //  自营商品优惠券领取
    public static final String FIND_ARTICLE_COUPON = "api/my/coupon/receiveCoupon";
    //    优惠券礼包领取
    public static final String COUPON_PACKAGE = "api/my/coupon/sendCouponPackage";
    //  文章评论列表
    public static final String FIND_INVI_COMMENT_LIST = "api/find/getFindCommentList";
    //  发现-推荐
    public static final String FIND_RECOMMEND = "api/find/getPostRemendList";
    //  点赞
    public static final String FIND_AND_COMMENT_FAV = "api/find/addCommentFavor";
    //      帖子标签关联列表
    public static final String FIND_RELEVANCE_TAG = "api/find/getPostByTag";
    //      帖子标签关联信息
    public static final String FIND_RELEVANCE_TAG_INFO = "api/find/getDocumentOtherInfoByTag";
    //    发现-活动
    public static final String F_ACTIVITY_AD = "api/home/getAdByFindActivity";
    //    发现-热门话题
    public static final String F_HOT_TOPIC_LIST = "api/find/getHotTopic";
    //    发现-热门话题
    public static final String F_TOPIC_LIST = "api/find/getTopicList";
    //    话题详情
    public static final String F_TOPIC_DES = "api/find/getTopicById";
    //    话题收藏
    public static final String F_TOPIC_COLLECT = "api/home/addFindTopic";
    //    话题推荐
    public static final String F_TOPIC_RECOMMEND = "api/find/getDocumentTopicListV3";
    //添加文章评论帖子
    public static final String FIND_COMMENT = "api/find/newAddComment";
    //    添加限时特惠产品评论
    public static final String GOODS_COMMENT = "api/find/addComment";
    //    文章分享统计
    public static final String ARTICLE_SHARE_COUNT = "api/my/addDocumentShareCount";
    //   每日分享
    public static final String SHARE_SUCCESS = "api/activity/shareReward";
    /**
     * 关联商品
     */
    //    多么订单商品
    public static final String REL_INDENT_PRO = "api/find/getOrderGoodsList";
    //    分享保存图片
    public static final String SHARE_SAVE_IMAGE_URL = "api/miniprogram/createMiniProductSharePic";
    //    购物车商品
    public static final String REL_SHOP_CAR_PRO = "api/find/getCartGoodsList";
    //    收藏商品
    public static final String REL_COLLECT_PRO = "api/find/getCollectGoodsList";
    //  OSS 缩略图300
    public static final String IMAGE_RESIZE = "?x-oss-process=image";
    //  OSS 缩略图方向
    public static final String IMAGE_RESIZE_ORI = "/auto-orient,1";
    //    oss图片格式转换
    public static final String OSS_IMG_FORMAT = "?x-oss-process=image/format,jpg";
}
