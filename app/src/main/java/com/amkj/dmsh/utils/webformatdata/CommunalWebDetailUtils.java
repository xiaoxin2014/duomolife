package com.amkj.dmsh.utils.webformatdata;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.BaseAddCarProInfoBean;
import com.amkj.dmsh.bean.CommunalDetailBean;
import com.amkj.dmsh.bean.CouponEntity;
import com.amkj.dmsh.bean.CouponEntity.CouponListEntity;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.shopdetails.bean.PicTagBean;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.fragment.app.Fragment;
import cn.jzvd.Jzvd;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IMG_REGEX_TAG;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_TEXT;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.regexATextUrl;
import static com.amkj.dmsh.constant.Url.FIND_ARTICLE_COUPON;
import static com.amkj.dmsh.constant.Url.FIND_COUPON_PACKAGE;
import static com.amkj.dmsh.dao.BaiChuanDao.skipAliBC;
import static com.amkj.dmsh.dao.OrderDao.addShopCarGetSku;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON_PACKAGE;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_GOODS_2X;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_GOODS_3X;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_GOODS_IMG;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_GOODS_WEL;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/12/20
 * version 3.2.0
 * class description:web???????????? ????????????
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
     * web????????????
     *
     * @param descriptionList
     * @return
     */
    public List<CommunalDetailObjectBean> getWebDetailsFormatDataList(List<CommunalDetailBean> descriptionList) {
        List<CommunalDetailObjectBean> descriptionDetailList = new ArrayList<>();
        if (descriptionList != null && descriptionList.size() > 0) {
            for (int i = 0; i < descriptionList.size(); i++) {
                try {
                    CommunalDetailObjectBean detailObjectBean = new CommunalDetailObjectBean();
                    CommunalDetailBean descriptionBean = descriptionList.get(i);
                    if (descriptionBean == null) {
                        continue;
                    }
                    String type = descriptionBean.getType();
                    if ("goods".equals(type)) {//????????????
                        detailObjectBean.setItemType(TYPE_GOODS_WEL);
                        detailObjectBean.setSelfGoods(true);
                        Map<String, Object> hashMap = (Map<String, Object>) descriptionBean.getContent();
                        setGoodsData(detailObjectBean, hashMap);
                    } else if ("pictureGoods".equals(type)) { //????????????
                        detailObjectBean.setItemType(TYPE_GOODS_IMG);
                        detailObjectBean.setSelfGoods(true);
                        detailObjectBean.setPicUrl(getStrings(descriptionBean.getPicUrl()));
                        detailObjectBean.setId(descriptionBean.getId());
                    } else if ("picLinkx1".equals(type)) {
                        List<Map<String, Object>> goodList = (List<Map<String, Object>>) descriptionBean.getContent();
                        Map<String, Object> hashMap = goodList.get(0);
                        String styleType = (String) hashMap.get("styleType");
                        detailObjectBean.setItemType("picType".equals(styleType) ? TYPE_GOODS_IMG : CommunalDetailObjectBean.TYPE_GOODS_WEL);
                        detailObjectBean.setSelfGoods("selfGoods".equals(hashMap.get("type")));
                        setGoodsData(detailObjectBean, hashMap);
                    } else if ("picLinkx2".equals(descriptionBean.getType()) || "goodsX2".equals(descriptionBean.getType()) || "pictureGoodsX2".equals(descriptionBean.getType())) {
                        detailObjectBean.setItemType(TYPE_GOODS_2X);

                        String strContent = GsonUtils.toJson(descriptionBean.getContent());
                        List<LikedProductBean> goodList = GsonUtils.fromJson(strContent, new TypeToken<List<LikedProductBean>>() {
                        }.getType());
                        goodList = goodList.size() > 2 ? goodList.subList(0, 2) : goodList;
                        for (LikedProductBean likedProductBean : goodList) {
                            //??????:????????????
                            boolean isPicture = type.contains("picture") || "picType".equals(likedProductBean.getStyleType());
                            likedProductBean.setItemType(isPicture ? ConstantVariable.PICTURE : ConstantVariable.PRODUCT);
                        }
                        detailObjectBean.setGoodsList(goodList);
                    } else if ("picLinkx3".equals(descriptionBean.getType()) || ("goodsX3".equals(descriptionBean.getType()) || "pictureGoodsX3".equals(descriptionBean.getType()))) {
                        detailObjectBean.setItemType(TYPE_GOODS_3X);

                        String strContent = GsonUtils.toJson(descriptionBean.getContent());
                        List<LikedProductBean> goodList = GsonUtils.fromJson(strContent, new TypeToken<List<LikedProductBean>>() {
                        }.getType());
                        goodList = goodList.size() > 3 ? goodList.subList(0, 3) : goodList;
                        for (LikedProductBean likedProductBean : goodList) {
                            //??????:????????????
                            likedProductBean.setItemType(type.contains("picture") || "picType".equals(likedProductBean.getStyleType()) ? ConstantVariable.PICTURE : ConstantVariable.PRODUCT);
                        }
                        detailObjectBean.setGoodsList(goodList);
                    } else if (descriptionBean.getType().equals("coupon")) {
                        detailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_COUPON);
                        Map<String, Object> hashMap = (Map<String, Object>) descriptionBean.getContent();
                        detailObjectBean.setPicUrl((String) hashMap.get("picUrl"));
                        detailObjectBean.setNewPirUrl((String) hashMap.get("newPirUrl"));
                        detailObjectBean.setId(((Double) hashMap.get("id")).intValue());
                    } else if (descriptionBean.getType().equals("couponPackage")) {
                        Map<String, Object> hashMap = (Map<String, Object>) descriptionBean.getContent();
                        detailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_COUPON_PACKAGE);
                        detailObjectBean.setPicUrl((String) hashMap.get("imgUrl"));
                        detailObjectBean.setCpName((String) hashMap.get("cpName"));
                        detailObjectBean.setId(((Double) hashMap.get("cpId")).intValue());
                    } else if (descriptionBean.getType().equals("taobaoLink")) {
                        detailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_LINK_TAOBAO);
                        detailObjectBean.setContent(getStrings(descriptionBean.getText()));
                        detailObjectBean.setPicUrl(getStrings(descriptionBean.getImage()));
                        detailObjectBean.setUrl(getStrings(descriptionBean.getAndroidLink()));
                    } else if (descriptionBean.getType().equals("text")) {
                        String content = (String) descriptionBean.getContent();
                        if (!TextUtils.isEmpty(content)) {
                            //                    ?????????????????????
                            Matcher imgIsFind = Pattern.compile(IMG_REGEX_TAG).matcher(content);
                            boolean isImageTag = imgIsFind.find();
                            if (isImageTag) {
                                detailObjectBean = null;
                                String stringContent = imgIsFind.group();
                                //                    ????????????
                                Matcher aMatcher = Pattern.compile(regexATextUrl).matcher(content);
                                CommunalDetailObjectBean communalDetailObjectBean;
                                if (aMatcher.find()) {
                                    communalDetailObjectBean = new CommunalDetailObjectBean();
                                    communalDetailObjectBean.setContent(content);
                                    communalDetailObjectBean.setItemType(CommunalDetailObjectBean.NORTEXT);
                                    communalDetailObjectBean.setTagList(descriptionBean.getTagList());
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
                                            communalDetailObjectBean.setTagList(descriptionBean.getTagList());
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
                                                        addImagePath(descriptionDetailList, imageUrl, descriptionBean.getTagList());
                                                    }
                                                } else {
                                                    addImagePath(descriptionDetailList, imgUrl, descriptionBean.getTagList());
                                                }
                                            } else {
                                                addImagePath(descriptionDetailList, imgUrl, descriptionBean.getTagList());
                                            }
                                        }
                                        hasImgUrl = matcher.find();
                                    }
                                }
                            } else {
                                //                        ??????
                                if (i == 0) {
                                    detailObjectBean.setFirstLinePadding(true);
                                }
                                detailObjectBean.setContent(content);
                                detailObjectBean.setTagList(descriptionBean.getTagList());
                                detailObjectBean.setItemType(CommunalDetailObjectBean.NORTEXT);
                            }
                        }
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
                    } else {
                        detailObjectBean = null;
                    }
                    if (detailObjectBean != null) {
                        descriptionDetailList.add(detailObjectBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
//      ????????????????????????????????????????????????????????????????????????????????????onSurfaceTextureDestroyed -> ??????
        Jzvd.releaseAllVideos();
        return descriptionDetailList;
    }


    private void setGoodsData(CommunalDetailObjectBean detailObjectBean, Map<String, Object> hashMap) {
        String name = (String) hashMap.get("name");
        detailObjectBean.setName(!TextUtils.isEmpty(name) ? name : (String) hashMap.get("title"));
        detailObjectBean.setId(getStringChangeIntegers(hashMap.get("id") + ""));
        if (hashMap.get("itemTypeId") != null) {
            detailObjectBean.setItemTypeId(getStringChangeIntegers(hashMap.get("itemTypeId") + ""));
        }
        if (hashMap.get("quantity") != null) {
            detailObjectBean.setQuantity(getStringChangeIntegers(hashMap.get("quantity") + ""));
        }
        detailObjectBean.setPicUrl((String) hashMap.get("picUrl"));
        detailObjectBean.setPrice(hashMap.get("price") + "");
        String androidLink = (String) hashMap.get("androidLink");
        if (!TextUtils.isEmpty(androidLink)) {
            detailObjectBean.setAndroidLink(hashMap.get("androidLink") + "");
        }
    }

    /**
     * ????????? json???????????????
     *
     * @param descriptionDetailList ??????????????????
     * @param imgUrl                ????????????
     */
    private void addImagePath(List<CommunalDetailObjectBean> descriptionDetailList, String imgUrl, List<PicTagBean> tagList) {
        CommunalDetailObjectBean communalDetailObjectBean = new CommunalDetailObjectBean();
        String imgUrlContent = ("<span><img src=\"" + imgUrl + "\" /></span>");
        communalDetailObjectBean.setContent(imgUrlContent);
        communalDetailObjectBean.setItemType(CommunalDetailObjectBean.NORTEXT);
        communalDetailObjectBean.setTagList(tagList);
        descriptionDetailList.add(communalDetailObjectBean);
    }

    /**
     * ????????????????????????????????????4096*4096
     * ??????????????????
     */
    private List<String> getImageCrop(String imageUrl, int sizeHeight) {
        int maxSize = 4096;
        List<String> imageCropList = new ArrayList<>();
//        oss????????????
//        ?????????????????? ????????????????????????????????????
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
     * ???????????????
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
     * ???????????????
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
     * ??????web????????????
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
        switch (view.getId()) {
            case R.id.img_product_coupon_pic:
            case R.id.ll_layout_tb_coupon:
            case R.id.iv_ql_bl_add_car:
                if (userId < 1) {
                    if (activity != null) {
                        getLoginStatus(activity);
                    } else {
                        getLoginStatus(fragment);
                    }
                    return;
                } else {
                    switch (view.getId()) {
                        //???????????????
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
                        //???????????????
                        case R.id.ll_layout_tb_coupon:
                            CommunalDetailObjectBean couponBean = (CommunalDetailObjectBean) view.getTag();
                            if (couponBean != null) {
                                skipAliBC(mActivity, couponBean.getCouponUrl(), "");
                            } else {
                                showToast("??????????????????????????????~");
                            }
                            break;
                        //???????????????
                        case R.id.iv_ql_bl_add_car:
                            CommunalDetailObjectBean qualityWelPro = (CommunalDetailObjectBean) view.getTag();
                            if (qualityWelPro != null) {
                                BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                                baseAddCarProInfoBean.setProductId(qualityWelPro.getId());
                                baseAddCarProInfoBean.setProName(getStrings(qualityWelPro.getName()));
                                baseAddCarProInfoBean.setProPic(getStrings(qualityWelPro.getPicUrl()));
                                addShopCarGetSku(activity, baseAddCarProInfoBean);
                            }
                            break;
                    }
                }
                break;
            //????????????
            case R.id.tv_communal_share:
                if (shareDataBean != null) {
                    setShareData(mActivity, shareDataBean);
                }
                break;
            //????????????
            case R.id.tv_communal_tb_link:
            case R.id.iv_communal_tb_cover:
                CommunalDetailObjectBean tbLink = (CommunalDetailObjectBean) view.getTag(R.id.iv_tag);
                if (tbLink == null) {
                    tbLink = (CommunalDetailObjectBean) view.getTag();
                }
                if (tbLink != null) {
                    skipAliBC(mActivity, tbLink.getUrl(), "");
                }
                break;
        }
    }

    /**
     * ??????????????? ??????
     */
    public void getDirectCoupon(Context mContext, int id, KProgressHUD loadHud) {
        getDirectCoupon(mContext, id, loadHud, null);
    }

    public void getDirectCoupon(Context mContext, int id, KProgressHUD loadHud, GetCouponListener getCouponListener) {
        if (loadHud != null) {
            loadHud.show();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("couponId", id);
        NetLoadUtils.getNetInstance().loadNetDataPost(mContext, FIND_ARTICLE_COUPON, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null && loadHud.isShowing()) {
                    loadHud.dismiss();
                }
                CouponEntity couponEntity = GsonUtils.fromJson(result, CouponEntity.class);
                showToast(couponEntity == null ? "???????????????" :
                        couponEntity.getResult() != null ? getStrings(couponEntity.getResult().getMsg()) : getStrings(couponEntity.getMsg()));
                if (getCouponListener != null && couponEntity != null && couponEntity.getResult() != null) {
                    if (SUCCESS_CODE.equals(couponEntity.getCode()) && SUCCESS_CODE.equals(couponEntity.getResult().getCode())) {
                        getCouponListener.onSuccess(couponEntity.getResult());
                    } else {
                        getCouponListener.onFailure(couponEntity.getResult());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                if (loadHud != null && loadHud.isShowing()) {
                    loadHud.dismiss();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.Get_Coupon_Fail);
            }
        });
    }

    /**
     * ??????????????? ??????
     */
    public void getDirectCouponPackage(Context mContext, int couponId, KProgressHUD loadHud) {
        getDirectCouponPackage(mContext, couponId, loadHud, null);
    }

    public void getDirectCouponPackage(Context mContext, int couponId, KProgressHUD loadHud, GetCouponListener getCouponListener) {
        if (loadHud != null) {
            loadHud.show();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uId", userId);
        params.put("cpId", couponId);
        NetLoadUtils.getNetInstance().loadNetDataPost(mContext, FIND_COUPON_PACKAGE, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null && loadHud.isShowing()) {
                    loadHud.dismiss();
                }

                CouponListEntity couponListEntity = GsonUtils.fromJson(result, CouponListEntity.class);
                showToast(couponListEntity == null ? "????????????" : couponListEntity.getMsg());
                if (getCouponListener != null && couponListEntity != null) {
                    String code = couponListEntity.getCode();
                    if (SUCCESS_CODE.equals(code)) {
                        getCouponListener.onSuccess(couponListEntity);
                    } else {
                        getCouponListener.onFailure(couponListEntity);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                if (loadHud != null && loadHud.isShowing()) {
                    loadHud.dismiss();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.Get_Coupon_Fail);
            }
        });
    }

    /**
     * ????????????
     *
     * @param activity
     * @param shareDataBean
     */
    public void setShareData(Activity activity, ShareDataBean shareDataBean) {
        if (shareDataBean != null) {
            UMShareAction umShareAction = new UMShareAction((BaseActivity) activity
                    , shareDataBean.getImgUrl()
                    , getStrings(shareDataBean.getTitle())
                    , getStrings(shareDataBean.getDescription())
                    , getStrings(shareDataBean.getUrlLink()), shareDataBean.getRoutineUrl(), shareDataBean.getBackId(), -1, shareDataBean.getPlatform());
//            if (shareDataBean.getBackId() > 0) {
//                umShareAction.setOnShareSuccessListener(() ->
//                        addArticleShareCount(activity, shareDataBean.getBackId())
//                );
//            }
        }
    }

    public interface GetCouponListener {
        //????????????
        void onSuccess(CouponListEntity couponListEntity);

        //????????????
        void onFailure(CouponListEntity couponListEntity);
    }
}
