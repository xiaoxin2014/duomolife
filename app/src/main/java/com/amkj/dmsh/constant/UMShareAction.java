package com.amkj.dmsh.constant;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.dominant.activity.CouponZoneActivity;
import com.amkj.dmsh.dominant.activity.DoMoLifeWelfareDetailsActivity;
import com.amkj.dmsh.dominant.activity.QualityCustomTopicActivity;
import com.amkj.dmsh.dominant.activity.QualityNewUserActivity;
import com.amkj.dmsh.find.activity.JoinSuccessActivity;
import com.amkj.dmsh.find.activity.PostDetailActivity;
import com.amkj.dmsh.homepage.activity.ArticleOfficialActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.AsyncUtils;
import com.amkj.dmsh.utils.FileStreamUtils;
import com.amkj.dmsh.utils.LifecycleHandler;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.HtmlWebView;
import com.amkj.dmsh.views.alertdialog.AlertDialogShareHelper;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.shareRewardSuccess;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.isDebugTag;
import static com.amkj.dmsh.constant.Url.SHARE_SAVE_IMAGE_URL;
import static com.amkj.dmsh.dao.AddClickDao.addArticleShareCount;
import static com.amkj.dmsh.dao.SoftApiDao.reportIllegal;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.createFilePath;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.fileIsExist;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.getThumbImgUrl;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/2/17
 * class description:????????????
 */

public class UMShareAction {
    private int id;
    private String title;
    private Activity context;
    private OnShareSuccessListener onShareSuccessListener;
    private AlertDialogShareHelper alertDialogShareHelper;
    private boolean isSharing = false;
    private String routineUrl;
    public static String routineId = "gh_cdbcf7765273";
    private String logoUrl = "http://domolifes.oss-cn-beijing.aliyuncs.com/wechatIcon/domolife_logo.png";
    private ConstantMethod constantMethod;


    /**
     * ????????????
     */
    public UMShareAction(final BaseActivity context, String imgUrl,
                         final String title, final String description, final String urlLink, int id) {
        this(context, imgUrl, title, description, urlLink, null, id);
    }


    /**
     * ????????????,????????????
     */
    public UMShareAction(final BaseActivity context, String imgUrl,
                         final String title, final String description, final String urlLink, String routineUrl, int id) {
        this(context, imgUrl, title, description, urlLink, routineUrl, id, false, -1, "");
    }

    /**
     * ????????????,???????????????
     */
    public UMShareAction(final BaseActivity context, String imgUrl,
                         final String title, final String description, final String urlLink, String routineUrl, int id, boolean isSaveImg) {
        this(context, imgUrl, title, description, urlLink, routineUrl, id, isSaveImg, -1, "");
    }

    /**
     * ???H5????????????
     */
    public UMShareAction(final BaseActivity context, String imgUrl,
                         final String title, final String description, final String urlLink, String routineUrl, int id, int shareType, String h5Platform) {
        this(context, imgUrl, title, description, urlLink, routineUrl, id, false, shareType, h5Platform);
    }


    /**
     * UMImage image = new UMImage(ShareActivity.this, "imageurl");//????????????
     * UMImage image = new UMImage(ShareActivity.this, file);//????????????
     * UMImage image = new UMImage(ShareActivity.this, R.drawable.xxx);//????????????
     * UMImage image = new UMImage(ShareActivity.this, bitmap);//bitmap??????
     * UMImage image = new UMImage(ShareActivity.this, byte[]);//?????????
     *
     * @param imgUrl      ????????????
     * @param title       ??????
     * @param description ???????????????
     * @param urlLink     ??????????????????
     * @param routineUrl  ???????????????
     * @param isSaveImg   ????????????????????????
     * @param h5Platform  H5????????????????????????????????????h5??????????????????
     * @param shareTpe    ??????????????????(????????????????????????????????????)
     */
    public UMShareAction(final BaseActivity context, String imgUrl,
                         final String title, final String description, final String urlLink, String routineUrl
            , int productId, boolean isSaveImg, int shareTpe, String h5Platform) {
        this.context = context;
        this.title = title;
        this.id = productId;
        if (!TextUtils.isEmpty(routineUrl)) {
            this.routineUrl = routineUrl;
        }

        //?????????????????????????????????????????????????????????
        if (!TextUtils.isEmpty(h5Platform) && "1".equals(h5Platform)) {
            if (!TextUtils.isEmpty(imgUrl)) {
                //        ????????????
                GlideImageLoaderUtil.setLoadImgFinishListener(context, getThumbImgUrl(imgUrl, 300), new GlideImageLoaderUtil.ImageLoaderFinishListener() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        setLoadImageShare(SHARE_MEDIA.WEIXIN, new UMImage(context, bitmap), context, urlLink, title, description);
                    }

                    @Override
                    public void onError() {
                        setLoadImageShare(SHARE_MEDIA.WEIXIN, new UMImage(context, getDefaultCover(context)), context, urlLink, title, description);
                    }
                });
            } else {
                setLoadImageShare(SHARE_MEDIA.WEIXIN, new UMImage(context, getDefaultCover(context)), context, urlLink, title, description);
            }
            return;
        }

        if (alertDialogShareHelper == null) {
            alertDialogShareHelper = new AlertDialogShareHelper(context, !TextUtils.isEmpty(routineUrl) && isSaveImg && productId > 0, h5Platform);
        }
        alertDialogShareHelper.show();
        alertDialogShareHelper.setAlertSelectShareListener(shareIconTitleBean -> {
            alertDialogShareHelper.setLoading(0);
            if (!isSharing) {
                isSharing = true;
                switch (shareIconTitleBean.getSharePlatformType()) {
                    //????????????
                    case POCKET:
                        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData mClipData = ClipData.newPlainText("Label", !TextUtils.isEmpty(urlLink) ? urlLink : "????????????");
                        cmb.setPrimaryClip(mClipData);
                        showToast(R.string.copy_url_success);
                        if (alertDialogShareHelper != null) {
                            alertDialogShareHelper.dismiss();
                        }
                        alertDialogShareHelper.setLoading(1);
                        isSharing = false;
                        break;
                    //????????????
                    case MORE:
                        if (constantMethod == null) {
                            constantMethod = new ConstantMethod();
                        }
                        constantMethod.setOnGetPermissionsSuccess(new ConstantMethod.OnGetPermissionsSuccessListener() {
                            @Override
                            public void getPermissionsSuccess() {
                                alertDialogShareHelper.setLoading(0);
                                getSaveImageToCamera(productId, routineUrl);
                            }
                        });
                        alertDialogShareHelper.setLoading(1);
                        constantMethod.getPermissions(context, com.yanzhenjie.permission.Permission.WRITE_EXTERNAL_STORAGE);
                        isSharing = false;
                        break;
                    //????????????
                    case TUMBLR:
                        reportIllegal(context, id, 1);
                        if (alertDialogShareHelper != null) {
                            alertDialogShareHelper.dismiss();
                        }
                        alertDialogShareHelper.setLoading(1);
                        isSharing = false;
                        break;
                    //????????????
                    case LINE:
                        if (context instanceof PostDetailActivity) {
                            ((PostDetailActivity) context).showDelDialog();
                        }
                        if (alertDialogShareHelper != null) {
                            alertDialogShareHelper.dismiss();
                        }
                        alertDialogShareHelper.setLoading(1);
                        isSharing = false;
                        break;
                    //????????????????????????
                    default:
                        SHARE_MEDIA sharePlatformType = shareIconTitleBean.getSharePlatformType();
                        //????????????????????????
                        statisticsShare(context, id, title, shareTpe, sharePlatformType);
                        View view = context.getTopView();
                        if (view != null && TextUtils.isEmpty(imgUrl)) {
                            if (context instanceof DoMoLifeWelfareDetailsActivity) {
                                ((ScrollView) view).scrollTo(0, 0);
                            } else if (context instanceof QualityCustomTopicActivity || context instanceof PostDetailActivity) {
                                ((RecyclerView) view).scrollToPosition(0);
                            } else if (context instanceof ArticleOfficialActivity) {
                                ((HtmlWebView) view).scrollTo(0, 0);
                            }
                            //????????????
                            new LifecycleHandler(context).postDelayed(() -> new AsyncUtils<Bitmap>(context) {
                                @Override
                                public Bitmap runOnIO() {
                                    return GlideImageLoaderUtil.getBitmapFromView(view);
                                }

                                @Override
                                public void runOnUI(Bitmap bitmap) {
                                    setLoadImageShare(sharePlatformType, new UMImage(context, bitmap), context, urlLink, title, description);
                                }
                            }.excueTask(), 200);
                        } else {
                            if (!TextUtils.isEmpty(imgUrl)) {
                                //        ????????????
                                GlideImageLoaderUtil.setLoadImgFinishListener(context, getThumbImgUrl(imgUrl, 300), new GlideImageLoaderUtil.ImageLoaderFinishListener() {
                                    @Override
                                    public void onSuccess(Bitmap bitmap) {
                                        setLoadImageShare(sharePlatformType, new UMImage(context, bitmap), context, urlLink, title, description);
                                    }

                                    @Override
                                    public void onError() {
                                        setLoadImageShare(sharePlatformType, new UMImage(context, R.drawable.domolife_logo), context, urlLink, title, description);
                                    }
                                });
                            } else {
                                setLoadImageShare(sharePlatformType, new UMImage(context, getDefaultCover(context)), context, urlLink, title, description);
                            }
                        }

                        break;
                }
            }
        });
    }


    /**
     * ??????????????????
     *
     * @param platformType
     * @param umImage
     * @param context
     * @param urlLink
     * @param title
     * @param description
     */
    private void setLoadImageShare(SHARE_MEDIA platformType, UMImage umImage, Activity context, final String urlLink,
                                   final String title, final String description) {
        umImage.compressFormat = Bitmap.CompressFormat.PNG;
        //        ????????????
        final UMWeb web = new UMWeb(!TextUtils.isEmpty(urlLink) ? urlLink : "");
        web.setTitle(!TextUtils.isEmpty(title) ? title : "????????????");//??????
        web.setThumb(umImage);  //?????????
        web.setDescription(!TextUtils.isEmpty(description) ? description : "???????????????");//?????????v4.1.0???????????? ?????????????????????????????????????????????????????????????????????

        switch (platformType) {
            case SINA:
                new ShareAction(context).setPlatform(platformType)
                        .withText((!TextUtils.isEmpty(title) ? title : "")
                                + (!TextUtils.isEmpty(description) ? description : "")
                                + (!TextUtils.isEmpty(urlLink) ? urlLink : "????????????"))
                        .withMedia(umImage)
                        .setCallback(umShareListener)
                        .share();
                break;
            case WEIXIN:
                //?????????????????????
                if (!TextUtils.isEmpty(routineUrl)) {
                    UMMin umMin = new UMMin(!TextUtils.isEmpty(umImage.toUrl()) ? umImage.toUrl() : logoUrl);//???????????????????????????????????????2000
                    //??????????????????????????????
                    umMin.setThumb(umImage);
                    // ???????????????????????????
                    umMin.setTitle(!TextUtils.isEmpty(title) ? title : "????????????");
                    // ???????????????title
                    umMin.setDescription(!TextUtils.isEmpty(description) ? description : "???????????????");
                    // ?????????????????????
                    umMin.setPath(routineUrl);
                    //?????????????????????
                    umMin.setUserName(routineId);
                    if (isDebugTag) {
                        SharedPreferences sharedPreferences = mAppContext.getSharedPreferences("selectedServer", MODE_PRIVATE);
                        String baseUrl = sharedPreferences.getString("selectServerUrl", Url.getUrl(0));
                        //????????????????????????????????????????????????????????????????????????????????????
                        if ("http://dev.domolife.cn/".equals(baseUrl)) {
                            com.umeng.socialize.Config.setMiniPreView();
                        } else if (!"https://app.domolife.cn/".equals(baseUrl)) {
                            // TODO: 2020/1/8
//                            com.umeng.socialize.Config.setMiniTest();
                            com.umeng.socialize.Config.setMiniPreView();
                        }
                    }

                    // ???????????????id,?????????????????????
                    new ShareAction(context)
                            .withMedia(umMin)
                            .setPlatform(platformType)
                            .setCallback(umShareListener).share();
                } else if (!TextUtils.isEmpty(urlLink)) {
                    boolean isVideo = urlLink.endsWith("mp4") || urlLink.endsWith("flv") || urlLink.endsWith("avi") || urlLink.endsWith("rmvb") || urlLink.endsWith("wmv");
                    if (isVideo) {//??????????????????
                        UMVideo umVideo = new UMVideo(urlLink);
                        umVideo.setTitle(title);//???????????????
                        umVideo.setThumb(umImage);//??????????????????
                        umVideo.setDescription(!TextUtils.isEmpty(description) ? description : "???????????????");//???????????????
                        new ShareAction(context).setPlatform(platformType)
                                .withMedia(umVideo)
                                .setCallback(umShareListener)
                                .share();
                    } else {
                        //????????????h5??????
                        new ShareAction(context).setPlatform(platformType)
                                .withMedia(web)
                                .setCallback(umShareListener)
                                .share();
                    }
                } else {
                    //???????????????
                    new ShareAction(context).setPlatform(platformType)
                            .withMedia(umImage)
                            .setCallback(umShareListener)
                            .share();
                }
                break;
            case QQ:
//                qq??????????????????????????????
                if (constantMethod == null) {
                    constantMethod = new ConstantMethod();
                }
                constantMethod.setOnGetPermissionsSuccess(new ConstantMethod.OnGetPermissionsSuccessListener() {
                    @Override
                    public void getPermissionsSuccess() {
                        //????????????
                        if (!TextUtils.isEmpty(urlLink)) {
                            new ShareAction(context).setPlatform(platformType)
                                    .withMedia(web)
                                    .setCallback(umShareListener)
                                    .share();
                        } else {//????????????
                            new ShareAction(context).setPlatform(platformType)
                                    .withMedia(umImage)
                                    .setCallback(umShareListener)
                                    .share();
                        }
                    }
                });
                alertDialogShareHelper.setLoading(1);
                constantMethod.getPermissions(context, com.yanzhenjie.permission.Permission.WRITE_EXTERNAL_STORAGE);
                isSharing = false;
                break;
            default:
                //                        ????????????
                new ShareAction(context).setPlatform(platformType)
                        .withMedia(web)
                        .setCallback(umShareListener)
                        .share();
                break;
        }
    }

    /**
     * ??????????????????
     *
     * @param productId
     * @param routineUrl
     */
    private void getSaveImageToCamera(int productId, String routineUrl) {
        int urlEndIndex = routineUrl.indexOf("?");
        if (urlEndIndex != -1) {
            routineUrl = routineUrl.substring(0, urlEndIndex);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("page", routineUrl);
        params.put("productId", productId);
        params.put("scene", "id=" + productId + "&pid=0");
        NetLoadUtils.getNetInstance().loadNetDataPost(context, SHARE_SAVE_IMAGE_URL, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (SUCCESS_CODE.equals(requestStatus.getCode()) &&
                            requestStatus.getResult() != null && !TextUtils.isEmpty(requestStatus.getResult().getImgUrl())) {
                        String imageUrl = requestStatus.getResult().getImgUrl();
                        String topicSavePath = Environment.getExternalStorageDirectory().getPath() + "/camera";
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            createFilePath(topicSavePath);
                            String imageName = imageUrl.substring(imageUrl.lastIndexOf("/"));
                            topicSavePath = topicSavePath + "/" + imageName;
                            if (!fileIsExist(topicSavePath)) {
                                final String finalTopicSavePath = topicSavePath;
                                GlideImageLoaderUtil.downOriginalImg(context, imageUrl, new GlideImageLoaderUtil.OriginalLoaderFinishListener() {
                                    @Override
                                    public void onSuccess(File file) {
                                        File pathFile = new File(finalTopicSavePath);
                                        try {
                                            if (FileStreamUtils.forChannel(file, pathFile)) {
                                                showToast(R.string.saveSuccess);
                                            }
                                            // ????????????????????????????????????
                                            insertImageToSyatemImage(context, pathFile);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError() {
                                    }
                                });
                            } else {
                                showToast("?????????????????????????????????????????????~");
                            }
                            if (alertDialogShareHelper != null) {
                                alertDialogShareHelper.dismiss();
                            }
                        }
                    } else {
                        showToast(getStrings(requestStatus.getMsg()));
                    }
                }
                if (alertDialogShareHelper != null) {
                    alertDialogShareHelper.setLoading(1);
                }
            }

            @Override
            public void onNotNetOrException() {
                if (alertDialogShareHelper != null) {
                    alertDialogShareHelper.setLoading(1);
                }
            }
        });
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            isSharing = false;
            if (alertDialogShareHelper != null) {
                alertDialogShareHelper.dismiss();
            }
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform != SHARE_MEDIA.WEIXIN && platform != SHARE_MEDIA.WEIXIN_CIRCLE) {
                showToast(getPlatFormText(platform) + " ???????????????");
            }
            if (onShareSuccessListener != null) {
                onShareSuccessListener.onShareSuccess();
            }
            if (alertDialogShareHelper != null) {
                alertDialogShareHelper.dismiss();
            }
            isSharing = false;
            //??????????????????????????????????????????
            if (userId > 0) {
                shareRewardSuccess(userId, context);
            }

            //??????????????????
            if (needStatistics(context) && id > 0) {
                addArticleShareCount(context, id);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            isSharing = false;
            if (alertDialogShareHelper != null) {
                alertDialogShareHelper.setLoading(1);
            }

            showToast(getPlatFormText(platform) + " ????????????,??????????????????????????????");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            isSharing = false;
            showToast(getPlatFormText(platform) + " ???????????????");
            if (alertDialogShareHelper != null) {
                alertDialogShareHelper.dismiss();
            }
        }
    };

    /**
     * ????????????????????????
     *
     * @param objId    ???????????????ID??????-?????????????????????????????????ID??????????????????????????????????????????????????????0??????
     * @param ObjName  ????????????????????? ??????????????????????????????????????????
     * @param platform ??????????????????
     */
    private void statisticsShare(Activity activity, int objId, String ObjName, int shareType, SHARE_MEDIA platform) {
        int type = (shareType == -1 ? getShareType(activity.getClass().getSimpleName()) : shareType);
        if (type == -1) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("shareType", type);
        map.put("objId", objId);
        map.put("road", getShareRoad(platform));
        map.put("objName", ObjName);
        map.put("status", 1);//???????????? ???0-?????????1-????????? ?????????????????????????????????????????????????????????1
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, Url.STATISTICS_SHARE, map, null);
    }

    /**
     * ????????????????????????
     *
     * @param platform ??????
     * @return
     */
    @NonNull
    private String getPlatFormText(SHARE_MEDIA platform) {
        String platformText = "";
        switch (platform) {
            case QQ:
                platformText = "QQ";
                break;
            case WEIXIN:
                platformText = "??????";
                break;
            case WEIXIN_CIRCLE:
                platformText = "???????????????";
                break;
            case SINA:
                platformText = "????????????";
                break;
        }
        return platformText;
    }


    /**
     * ???????????????????????????
     *
     * @param platform ??????
     */
    @NonNull
    private String getShareRoad(SHARE_MEDIA platform) {
        String roadText = "";
        switch (platform) {
            case QQ:
                roadText = "qq";
                break;
            case WEIXIN:
                roadText = "wechatFriend";
                break;
            case WEIXIN_CIRCLE:
                roadText = "wechatMoment";
                break;
            case SINA:
                roadText = "weibo";
                break;
        }
        return roadText;
    }

    /*
     *??????????????????
     * @param platform ??????
     */
    private int getShareType(String simpleName) {
        switch (simpleName) {
            case "ShopScrollDetailsActivity"://??????
                return 1;
            case "ArticleOfficialActivity"://??????
                return 2;
            case "PostDetailActivity"://??????
                return 3;
            case "EditorSelectActivity"://????????????
                return 4;
            case "WholePointSpikeProductActivity"://????????????
                return 5;
            case "QualityNewUserActivity"://????????????
                return 6;
            case "QualityGroupShopDetailActivity"://?????????
                return 7;
            case "DoMoLifeWelfareDetailsActivity"://?????????
                return 8;
            case "QualityCustomTopicActivity"://???????????????
                return 9;
            case "QualityTypeProductActivity"://????????????
                return 10;
            case "DmlOptimizedSelDetailActivity"://????????????
                return 11;
            case "QualityShopBuyListActivity"://????????????
            case "QualityShopHistoryListActivity"://????????????
                return 12;
            case "QualityWeekOptimizedActivity"://????????????
                return 13;
            case "VideoDetailActivity"://????????????
                return 16;
            default:
                return -1;
        }
    }

    //????????????????????????????????????????????? --????????????????????????
    private boolean needStatistics(Activity activity) {
        switch (activity.getClass().getSimpleName()) {
            case "PostDetailActivity":
            case "ArticleInvitationDetailsActivity":
            case "DmlLifeSearchDetailActivity":
            case "DmlOptimizedSelDetailActivity":
            case "DoMoLifeWelfareDetailsActivity":
            case "ArticleOfficialActivity":
            case "DoMoLifeCommunalActivity":
                return true;
            default:
                return false;
        }
    }


    public void setOnShareSuccessListener(OnShareSuccessListener onShareSuccessListener) {
        this.onShareSuccessListener = onShareSuccessListener;
    }

    public interface OnShareSuccessListener {
        void onShareSuccess();
    }

    private void insertImageToSyatemImage(Context context, File file) {
        ContentResolver localContentResolver = context.getContentResolver();
        ContentValues localContentValues = getImageContentValues(file, System.currentTimeMillis());
        localContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, localContentValues);
        Intent localIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        final Uri localUri = Uri.fromFile(file);
        localIntent.setData(localUri);
        context.sendBroadcast(localIntent);
    }

    public static ContentValues getImageContentValues(File paramFile, long paramLong) {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("title", paramFile.getName());
        localContentValues.put("_display_name", paramFile.getName());
        localContentValues.put("mime_type", "image/jpeg");
        localContentValues.put("datetaken", paramLong);
        localContentValues.put("date_modified", paramLong);
        localContentValues.put("date_added", paramLong);
        localContentValues.put("orientation", 0);
        localContentValues.put("_data", paramFile.getAbsolutePath());
        localContentValues.put("_size", paramFile.length());
        return localContentValues;
    }

    private int getDefaultCover(Activity context) {
        if (context instanceof QualityNewUserActivity) {
            return R.drawable.newuser_top_img;
        } else if (context instanceof JoinSuccessActivity) {
            return R.drawable.share_post_default;
        } else if (context instanceof CouponZoneActivity) {
            return R.drawable.coupon_default;
        } else {
            return R.drawable.domolife_logo;
        }
    }
}
