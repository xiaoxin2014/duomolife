package com.amkj.dmsh.utils.webformatdata;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.AlibcConstants;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jzvd.Jzvd;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.addArticleShareCount;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IMG_REGEX_TAG;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_TEXT;
import static com.amkj.dmsh.constant.ConstantVariable.TAOBAO_APPKEY;
import static com.amkj.dmsh.constant.ConstantVariable.regexATextUrl;
import static com.amkj.dmsh.constant.Url.COUPON_PACKAGE;
import static com.amkj.dmsh.constant.Url.FIND_ARTICLE_COUPON;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON_PACKAGE;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_GOODS_2X;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_GOODS_3X;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/12/20
 * version 3.2.0
 * class description:web数据解析 点击处理
 */
public class CommunalWebDetailUtils {

    private static volatile CommunalWebDetailUtils communalWebDetailUtils;

    private CommunalWebDetailUtils() {
    }

    public static CommunalWebDetailUtils getCommunalWebInstance() {
        if (communalWebDetailUtils == null) {
            synchronized (QyServiceUtils.class) {
                if (communalWebDetailUtils == null) {
                    communalWebDetailUtils = new CommunalWebDetailUtils();
                }
            }
        }
        return communalWebDetailUtils;
    }

    /**
     * web数据解析
     *
     * @param descriptionList
     * @return
     */
    public List<CommunalDetailObjectBean> getWebDetailsFormatDataList(List<CommunalDetailBean> descriptionList) {
        CommunalDetailObjectBean detailObjectBean;
        List<CommunalDetailObjectBean> descriptionDetailList = new ArrayList<>();
        if (descriptionList != null && descriptionList.size() > 0) {
            for (int i = 0; i < descriptionList.size(); i++) {
                detailObjectBean = new CommunalDetailObjectBean();
                CommunalDetailBean descriptionBean = descriptionList.get(i);
                if (descriptionBean.getType().equals("goods")) {
                    try {
                        Map<String, Object> hashMap = (Map<String, Object>) descriptionBean.getContent();
                        detailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_GOODS_WEL);
                        detailObjectBean.setName(hashMap.get("name") + "");
                        detailObjectBean.setId(((Double) hashMap.get("id")).intValue());
                        detailObjectBean.setItemTypeId(((Double) hashMap.get("itemTypeId")).intValue());
                        detailObjectBean.setPicUrl((String) hashMap.get("picUrl"));
                        detailObjectBean.setPrice(hashMap.get("price") + "");
                    } catch (Exception e) {
                        detailObjectBean = null;
                    }
                } else if (descriptionBean.getContent() != null && ("goodsX2".equals(descriptionBean.getType()) || "pictureGoodsX2".equals(descriptionBean.getType()))) {
                    try {
                        Gson gson = new Gson();
                        String strContent = gson.toJson(descriptionBean.getContent());
                        List<LikedProductBean> goodList = gson.fromJson(strContent, new TypeToken<List<LikedProductBean>>() {
                        }.getType());
                        if (goodList == null || goodList.size() < 1) {
                            continue;
                        }
                        goodList = goodList.size() > 2 ? goodList.subList(0, 2) : goodList;
                        for (LikedProductBean likedProductBean : goodList) {
                            if (descriptionBean.getType().contains("goodsX")) {
                                //普通商品
                                likedProductBean.setItemType(ConstantVariable.PRODUCT);
                            } else {
                                //封面图片
                                likedProductBean.setItemType(ConstantVariable.AD_COVER);
                            }
                        }
                        detailObjectBean.setItemType(TYPE_GOODS_2X);
                        detailObjectBean.setGoodsList(goodList);
                    } catch (Exception e) {
                        detailObjectBean = null;
                        e.printStackTrace();
                    }
                } else if (descriptionBean.getContent() != null && ("goodsX3".equals(descriptionBean.getType()) || "pictureGoodsX3".equals(descriptionBean.getType()))) {
                    try {
                        Gson gson = new Gson();
                        String strContent = gson.toJson(descriptionBean.getContent());
                        List<LikedProductBean> goodList = gson.fromJson(strContent
                                , new TypeToken<List<LikedProductBean>>() {
                                }.getType());
                        if (goodList == null || goodList.size() < 1) {
                            continue;
                        }

                        goodList = goodList.size() > 3 ? goodList.subList(0, 3) : goodList;
                        for (LikedProductBean likedProductBean : goodList) {
                            if (descriptionBean.getType().contains("goodsX")) {
                                //普通商品(picture)
                                likedProductBean.setItemType(ConstantVariable.PRODUCT);
                            } else {
                                //封面图片
                                likedProductBean.setItemType(ConstantVariable.AD_COVER);
                            }
                        }
                        detailObjectBean.setItemType(TYPE_GOODS_3X);
                        detailObjectBean.setGoodsList(goodList);
                    } catch (Exception e) {
                        detailObjectBean = null;
                        e.printStackTrace();
                    }
                } else if (descriptionBean.getType().equals("coupon")) {
                    try {
                        detailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_COUPON);
                        Map<String, Object> hashMap = (Map<String, Object>) descriptionBean.getContent();
                        detailObjectBean.setPicUrl((String) hashMap.get("picUrl"));
                        detailObjectBean.setNewPirUrl((String) hashMap.get("newPirUrl"));
                        detailObjectBean.setId(((Double) hashMap.get("id")).intValue());
                    } catch (Exception e) {
                        detailObjectBean = null;
                    }
                } else if (descriptionBean.getType().equals("taobaoLink")) {
                    detailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_LINK_TAOBAO);
                    detailObjectBean.setContent(getStrings(descriptionBean.getText()));
                    detailObjectBean.setPicUrl(getStrings(descriptionBean.getImage()));
                    detailObjectBean.setUrl(getStrings(descriptionBean.getAndroidLink()));
                } else if (descriptionBean.getType().equals("text")) {
                    String content = (String) descriptionBean.getContent();
                    if (!TextUtils.isEmpty(content)) {
//                    判断是否有图片
                        Matcher imgIsFind = Pattern.compile(IMG_REGEX_TAG).matcher(content);
                        boolean isImageTag = imgIsFind.find();
                        if (isImageTag) {
                            detailObjectBean = null;
                            String stringContent = imgIsFind.group();
                            //                    匹配网址
                            Matcher aMatcher = Pattern.compile(regexATextUrl).matcher(content);
                            CommunalDetailObjectBean communalDetailObjectBean;
                            if (aMatcher.find()) {
                                communalDetailObjectBean = new CommunalDetailObjectBean();
                                communalDetailObjectBean.setContent(content);
                                communalDetailObjectBean.setItemType(CommunalDetailObjectBean.NORTEXT);
                                descriptionDetailList.add(communalDetailObjectBean);
                            } else {
                                Matcher matcher = Pattern.compile(REGEX_TEXT).matcher(stringContent);
                                boolean hasImgUrl = matcher.find();
                                while (hasImgUrl) {
                                    String imgUrl = matcher.group();
                                    if (imgUrl.contains(".gif")) {
                                        communalDetailObjectBean = new CommunalDetailObjectBean();
                                        communalDetailObjectBean.setPicUrl(imgUrl);
                                        communalDetailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_GIF_IMG);
                                        descriptionDetailList.add(communalDetailObjectBean);
                                    } else {
                                        String imgHeightSizeTag = "_height=";
                                        if (content.contains(imgHeightSizeTag)) {
                                            int heightStart = content.indexOf(imgHeightSizeTag) + imgHeightSizeTag.length() + 1;
                                            int heightEnd = content.indexOf("\"", heightStart);
                                            if (heightStart != -1 && heightEnd != -1) {
                                                String substring = content.substring(heightStart, heightEnd);
                                                List<String> imageCropList = getImageCrop(imgUrl, Integer.parseInt(getNumber(substring)));
                                                for (String imageUrl : imageCropList) {
                                                    addImagePath(descriptionDetailList, imageUrl);
                                                }
                                            } else {
                                                addImagePath(descriptionDetailList, imgUrl);
                                            }
                                        } else {
                                            addImagePath(descriptionDetailList, imgUrl);
                                        }
                                    }
                                    hasImgUrl = matcher.find();
                                }
                            }
                        } else {
//                        正文
                            if (i == 0) {
                                detailObjectBean.setFirstLinePadding(true);
                            }
                            detailObjectBean.setContent(content);
                            detailObjectBean.setItemType(CommunalDetailObjectBean.NORTEXT);
                        }
                    }
                } else if (descriptionBean.getType().equals("pictureGoods")) { //图片地址
                    detailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_GOODS_IMG);
                    detailObjectBean.setNewPirUrl(getStrings(descriptionBean.getPicUrl()));
                    detailObjectBean.setId(descriptionBean.getId());
                } else if (descriptionBean.getType().equals("video")) {
                    detailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_VIDEO);
                    detailObjectBean.setUrl(getStrings((String) descriptionBean.getContent()));
                    detailObjectBean.setPicUrl(getStrings(descriptionBean.getImage()));
                } else if (descriptionBean.getType().equals("audio")) {
                    detailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_AUDIO);
                    detailObjectBean.setUrl(getStrings((String) descriptionBean.getContent()));
                    detailObjectBean.setPicUrl(getStrings(descriptionBean.getImage()));
                    detailObjectBean.setName(getStrings(descriptionBean.getName()));
                    detailObjectBean.setFrom(getStrings(descriptionBean.getFrom()));
                } else if (descriptionBean.getType().equals("share")) {
                    detailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_SHARE);
                } else if (descriptionBean.getType().equals("couponPackage")) {
                    try {
                        Map<String, Object> hashMap = (Map<String, Object>) descriptionBean.getContent();
                        detailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_COUPON_PACKAGE);
                        detailObjectBean.setPicUrl((String) hashMap.get("imgUrl"));
                        detailObjectBean.setCpName((String) hashMap.get("cpName"));
                        detailObjectBean.setId(((Double) hashMap.get("cpId")).intValue());
                    } catch (Exception e) {
                        detailObjectBean = null;
                    }
                } else {
                    detailObjectBean = null;
                }
                if (detailObjectBean != null) {
                    descriptionDetailList.add(detailObjectBean);
                }
            }
        }
//      暂时解决当前框架问题，播放中刷新视图，导致视图被销毁回调onSurfaceTextureDestroyed -> 黑屏
        Jzvd.releaseAllVideos();
        return descriptionDetailList;
    }

    /**
     * 仅针对 json串图片地址
     *
     * @param descriptionDetailList 详情信息集合
     * @param imgUrl                图片地址
     */
    private void addImagePath(List<CommunalDetailObjectBean> descriptionDetailList, String imgUrl) {
        CommunalDetailObjectBean communalDetailObjectBean = new CommunalDetailObjectBean();
        String imgUrlContent = ("<span><img src=\"" + imgUrl + "\" /></span>");
        communalDetailObjectBean.setContent(imgUrlContent);
        communalDetailObjectBean.setItemType(CommunalDetailObjectBean.NORTEXT);
        descriptionDetailList.add(communalDetailObjectBean);
    }

    /**
     * 暂时限制每张图片不能超过4096*4096
     * 图片大图截取
     */
    private List<String> getImageCrop(String imageUrl, int sizeHeight) {
        int maxSize = 4096;
        List<String> imageCropList = new ArrayList<>();
//        oss图片样式
//        根据图片大小 获取展示在屏幕的真正大小
        if (sizeHeight > maxSize) {
            int imageNormalSize = 2000;
            int imageCount = sizeHeight / imageNormalSize;
            if (imageCount > 0) {
                imageCount += (sizeHeight % imageNormalSize != 0 ? 1 : 0);
                String ossPrefix = "?x-oss-process=image";
                String imageNewUrl;
                if (!imageUrl.contains(ossPrefix)) {
                    imageNewUrl = imageUrl + ossPrefix;
                } else {
                    imageNewUrl = imageUrl;
                }
                for (int i = 0; i < imageCount; i++) {
                    String cropNewUrl = imageNewUrl + String.format(mAppContext.getString(R.string.image_crop_style), imageNormalSize, i);
                    imageCropList.add(cropNewUrl);
                }
            } else {
                imageCropList.add(imageUrl);
            }
        } else {
            imageCropList.add(imageUrl);
        }
        return imageCropList;
    }

    /**
     * 带分享数据
     *
     * @param object
     * @param shareDataBean
     * @param view
     * @param loadHud
     */
    public void setWebDataClick(Object object, ShareDataBean shareDataBean, View view, KProgressHUD loadHud) {
        if (object instanceof Activity) {
            setWebDataClick((Activity) object, null, shareDataBean, view, loadHud);
        } else if (object instanceof Fragment) {
            setWebDataClick(null, (Fragment) object, shareDataBean, view, loadHud);
        }
    }

    /**
     * 无分享数据
     *
     * @param object
     * @param view
     * @param loadHud
     */
    public void setWebDataClick(Object object, View view, KProgressHUD loadHud) {
        if (object instanceof Activity) {
            setWebDataClick((Activity) object, null, null, view, loadHud);
        } else if (object instanceof Fragment) {
            setWebDataClick(null, (Fragment) object, null, view, loadHud);
        }
    }

    /**
     * 设置web数据点击
     *
     * @param activity
     * @param fragment
     * @param view
     * @param loadHud
     */
    private void setWebDataClick(Activity activity, Fragment fragment, ShareDataBean shareDataBean, View view, KProgressHUD loadHud) {
        if (activity == null && fragment == null) {
            return;
        }
        Context mContext = activity != null ? activity : fragment.getContext();
        Activity mActivity = activity != null ? activity : fragment.getActivity();
        if (mActivity == null || mContext == null) {
            return;
        }
        if (loadHud != null) {
            loadHud.show();
        }
        switch (view.getId()) {
            case R.id.img_product_coupon_pic:
            case R.id.ll_layout_tb_coupon:
            case R.id.iv_ql_bl_add_car:
                if (userId < 1) {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    if (activity != null) {
                        getLoginStatus(activity);
                    } else {
                        getLoginStatus(fragment);
                    }
                    return;
                } else {
                    switch (view.getId()) {
                        case R.id.img_product_coupon_pic:
                            int couponId = (int) view.getTag(R.id.iv_avatar_tag);
                            int type = (int) view.getTag(R.id.iv_type_tag);
                            if (couponId > 0) {
                                if (type == TYPE_COUPON) {
                                    getDirectCoupon(mContext, couponId, loadHud);
                                } else if (type == TYPE_COUPON_PACKAGE) {
                                    getDirectCouponPackage(mContext, couponId, loadHud);
                                }
                            }
                            break;
                        case R.id.ll_layout_tb_coupon:
                            CommunalDetailObjectBean couponBean = (CommunalDetailObjectBean) view.getTag();
                            if (couponBean != null && !TextUtils.isEmpty(couponBean.getCouponUrl())) {
                                if (loadHud != null) {
                                    loadHud.dismiss();
                                }
                                skipNewTaoBao(mActivity, couponBean.getCouponUrl(), loadHud);
                            } else {
                                showToast(mContext, "数据异常，地址缺失，请刷新重试~");
                            }
                            break;
                        case R.id.iv_ql_bl_add_car:
                            CommunalDetailObjectBean qualityWelPro = (CommunalDetailObjectBean) view.getTag();
                            if (qualityWelPro != null) {
                                BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                                baseAddCarProInfoBean.setProductId(qualityWelPro.getId());
                                baseAddCarProInfoBean.setProName(getStrings(qualityWelPro.getName()));
                                baseAddCarProInfoBean.setProPic(getStrings(qualityWelPro.getPicUrl()));
                                ConstantMethod constantMethod = new ConstantMethod();
                                constantMethod.addShopCarGetSku(activity, baseAddCarProInfoBean, loadHud);
                            }
                            break;
                        default:
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            break;
                    }
                }
                break;
            case R.id.iv_communal_cover_wrap:
                CommunalDetailObjectBean detailObjectBean = (CommunalDetailObjectBean) view.getTag(R.id.iv_tag);
                if (detailObjectBean != null) {
                    if (detailObjectBean.getItemType() == CommunalDetailObjectBean.TYPE_GOODS_IMG || detailObjectBean.getItemType() == CommunalDetailObjectBean.TYPE_GOODS_IMG_DIRECT_BUY) {
                        Intent newIntent = new Intent(mContext, ShopScrollDetailsActivity.class);
                        newIntent.putExtra("productId", String.valueOf(detailObjectBean.getId()));
                        mContext.startActivity(newIntent);
                    }
                }
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                break;
            case R.id.tv_communal_share:
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                if (shareDataBean != null) {
                    setShareData(mActivity, shareDataBean);
                }
                break;
            case R.id.tv_communal_tb_link:
            case R.id.iv_communal_tb_cover:
                if (loadHud != null) {
                    loadHud.show();
                }
                CommunalDetailObjectBean tbLink = (CommunalDetailObjectBean) view.getTag(R.id.iv_tag);
                if (tbLink == null) {
                    tbLink = (CommunalDetailObjectBean) view.getTag();
                }
                if (tbLink != null) {
                    skipNewTaoBao(mActivity, tbLink.getUrl(), loadHud);
                }
                break;
            default:
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                break;
        }

    }

    /**
     * 获取优惠券 单张
     */
    private void getDirectCoupon(Context mContext, int id, KProgressHUD loadHud) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("couponId", id);
        NetLoadUtils.getNetInstance().loadNetDataPost(mContext, FIND_ARTICLE_COUPON, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    showToastRequestMsg(mContext, requestStatus);
                }
            }

            @Override
            public void onNotNetOrException() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(mContext, R.string.Get_Coupon_Fail);
            }

            @Override
            public void netClose() {
                showToast(mContext, R.string.unConnectedNetwork);
            }
        });
    }

    /**
     * 获取优惠券礼包 多张
     *
     * @param mContext
     * @param couponId
     * @param loadHud
     */
    private void getDirectCouponPackage(Context mContext, int couponId, KProgressHUD loadHud) {
        Map<String, Object> params = new HashMap<>();
        params.put("uId", userId);
        params.put("cpId", couponId);
        NetLoadUtils.getNetInstance().loadNetDataPost(mContext, COUPON_PACKAGE, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    showToastRequestMsg(mContext, requestStatus);
                }
            }

            @Override
            public void onNotNetOrException() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(mContext, R.string.Get_Coupon_Fail);
            }

            @Override
            public void netClose() {
                showToast(mContext, R.string.unConnectedNetwork);
            }
        });
    }

    /**
     * 跳转淘宝登录
     *
     * @param context
     * @param url
     * @param loadHud
     */
    private void skipNewTaoBao(Activity context, String url, KProgressHUD loadHud) {
        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                skipNewShopDetails(context, url);
            }

            @Override
            public void onFailure(int code, String msg) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                showToast(context, "登录失败 ");
            }
        });
    }

    /**
     * 跳转阿里百川
     *
     * @param activity
     * @param url
     */
    private void skipNewShopDetails(Activity activity, String url) {
        //提供给三方传递配置参数
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
        //设置页面打开方式
        AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);
        //实例化商品详情 itemID打开page
        AlibcBasePage ordersPage = new AlibcPage(url);
        AlibcTaokeParams alibcTaokeParams = new AlibcTaokeParams();
//        alibcTaokeParams.setPid(TAOBAO_PID);
//        alibcTaokeParams.setAdzoneid(TAOBAO_ADZONEID);
        alibcTaokeParams.extraParams = new HashMap<>();
        alibcTaokeParams.extraParams.put("taokeAppkey", TAOBAO_APPKEY);
        AlibcTrade.show(activity, ordersPage, showParams, alibcTaokeParams, exParams, new AlibcTradeCallback() {
            @Override
            public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
                //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
//                showToast(context, "获取详情成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
//                showToast(ShopTimeScrollDetailsActivity.this, msg);
            }
        });
    }

    /**
     * 设置分享
     *
     * @param activity
     * @param shareDataBean
     */
    public void setShareData(Activity activity, ShareDataBean shareDataBean) {
        if (shareDataBean != null && !TextUtils.isEmpty(shareDataBean.getImgUrl())) {
            UMShareAction umShareAction = new UMShareAction((BaseActivity) activity
                    , shareDataBean.getImgUrl()
                    , getStrings(shareDataBean.getTitle())
                    , getStrings(shareDataBean.getDescription())
                    , getStrings(shareDataBean.getUrlLink()), shareDataBean.getRoutineUrl(), shareDataBean.getBackId());
            if (shareDataBean.getBackId() > 0) {
                umShareAction.setOnShareSuccessListener(() ->
                        addArticleShareCount(activity, shareDataBean.getBackId())
                );
            }
        }
    }
}
