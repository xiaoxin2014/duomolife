package com.amkj.dmsh.constant;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.utils.FileStreamUtils;
import com.amkj.dmsh.utils.alertdialog.AlertDialogShareHelper;
import com.amkj.dmsh.utils.alertdialog.ShareIconTitleBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMWeb;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.shareRewardSuccess;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.BASE_URL;
import static com.amkj.dmsh.constant.Url.SHARE_SAVE_IMAGE_URL;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.createFilePath;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.fileIsExist;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.getThumbImgUrl;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/2/17
 * class description:友盟分享
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
    private ConstantMethod constantMethod;

    /**
     * UMImage image = new UMImage(ShareActivity.this, "imageurl");//网络图片
     * UMImage image = new UMImage(ShareActivity.this, file);//本地文件
     * UMImage image = new UMImage(ShareActivity.this, R.drawable.xxx);//资源文件
     * UMImage image = new UMImage(ShareActivity.this, bitmap);//bitmap文件
     * UMImage image = new UMImage(ShareActivity.this, byte[]);//字节流
     * 分享内容
     *
     * @param context
     * @param imgUrl
     * @param title
     * @param description
     * @param urlLink
     */
    public UMShareAction(final Activity context, String imgUrl,
                         final String title, final String description, final String urlLink, int id) {
        this(context, imgUrl, title, description, urlLink, null, id);
    }
//    public UMShareAction(final Activity context, String imgUrl,
//                         final String title, final String description, final String urlLink) {
//        this(context, imgUrl, title, description, urlLink, null);
//    }

    /**
     * 分享内容 加载图片
     *
     * @param context
     * @param imgUrl
     * @param title
     * @param description
     * @param urlLink     正常地址
     * @param routineUrl  小程序地址
     */
    public UMShareAction(final Activity context, String imgUrl,
                         final String title, final String description, final String urlLink, String routineUrl, int id) {
        this(context, imgUrl, title, description, urlLink, routineUrl, id, false);
    }
//    public UMShareAction(final Activity context, String imgUrl,
//                         final String title, final String description, final String urlLink, String routineUrl) {
//        this(context, imgUrl, title, description, urlLink, routineUrl, 0, false);
//    }

    /**
     * @param context
     * @param imgUrl      图片地址
     * @param title       标题
     * @param description 分享副标题
     * @param urlLink     正常跳转地址
     * @param routineUrl  小程序地址
     * @param isSaveImg   是否展示保存图片
     */
    public UMShareAction(final Activity context, String imgUrl,
                         final String title, final String description, final String urlLink, String routineUrl
            , int productId, boolean isSaveImg) {
        this.context = context;
        this.title = title;
        this.id = productId;
        if (!TextUtils.isEmpty(routineUrl)) {
            this.routineUrl = routineUrl;
        }

        //如果是自营商品详情页面，则分享弹窗显示保存图片按钮
        if (alertDialogShareHelper == null) {
            alertDialogShareHelper = new AlertDialogShareHelper(context, !TextUtils.isEmpty(routineUrl)
                    && isSaveImg && productId > 0 && context.getClass().getSimpleName().equals(ShopScrollDetailsActivity.class.getSimpleName()));
        }
        alertDialogShareHelper.show();
        alertDialogShareHelper.setAlertSelectShareListener(new AlertDialogShareHelper.AlertSelectShareListener() {
            @Override
            public void selectShare(ShareIconTitleBean shareIconTitleBean) {
                alertDialogShareHelper.setLoading(0);
                if (!isSharing) {
                    isSharing = true;
                    switch (shareIconTitleBean.getSharePlatformType()) {
                        case POCKET:
                            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData mClipData = ClipData.newPlainText("Label", !TextUtils.isEmpty(urlLink) ? urlLink : "多么生活");
                            cmb.setPrimaryClip(mClipData);
                            showToast(context, R.string.copy_url_success);
                            if (alertDialogShareHelper != null) {
                                alertDialogShareHelper.dismiss();
                            }
                            alertDialogShareHelper.setLoading(1);
                            isSharing = false;
                            break;
                        //                            保存图片
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
                        default:
                            SHARE_MEDIA sharePlatformType = shareIconTitleBean.getSharePlatformType();
                            if (!TextUtils.isEmpty(imgUrl)) {
                                //        加载图片
                                GlideImageLoaderUtil.loadFinishImgDrawable(context, getThumbImgUrl(imgUrl, 300), new GlideImageLoaderUtil.ImageLoaderFinishListener() {
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
                                setLoadImageShare(sharePlatformType, new UMImage(context, R.drawable.domolife_logo), context, urlLink, title, description);
                            }
                            break;
                    }
                }
            }
        });
    }

    /**
     * 加载图片分享
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
        //        链接地址
        final UMWeb web = new UMWeb(!TextUtils.isEmpty(urlLink) ? urlLink : "");
        web.setTitle(!TextUtils.isEmpty(title) ? title : "多么生活");//标题
        web.setThumb(umImage);  //缩略图
        web.setDescription(!TextUtils.isEmpty(description) ? description : "有你更精彩");//描述
        switch (platformType) {
            case SINA:
                new ShareAction(context).setPlatform(platformType)
                        .withText((!TextUtils.isEmpty(title) ? title : "")
                                + (!TextUtils.isEmpty(description) ? description : "")
                                + (!TextUtils.isEmpty(urlLink) ? urlLink : "多么生活"))
                        .withMedia(umImage)
                        .setCallback(umShareListener)
                        .share();
                break;
            case WEIXIN:
                if (!TextUtils.isEmpty(routineUrl)) {
                    UMMin umMin = new UMMin(urlLink);
                    //兼容低版本的网页链接
                    umMin.setThumb(umImage);
                    // 小程序消息封面图片
                    umMin.setTitle(!TextUtils.isEmpty(title) ? title : "多么生活");
                    // 小程序消息title
                    umMin.setDescription(!TextUtils.isEmpty(description) ? description : "有你更精彩");
                    // 小程序消息描述
                    umMin.setPath(routineUrl);
                    //小程序页面路径
                    umMin.setUserName(routineId);
                    // 小程序原始id,在微信平台查询
                    new ShareAction(context)
                            .withMedia(umMin)
                            .setPlatform(platformType)
                            .setCallback(umShareListener).share();
                } else {
                    //                        分享链接
                    new ShareAction(context).setPlatform(platformType)
                            .withMedia(web)
                            .setCallback(umShareListener)
                            .share();
                }
                break;
            case QQ:
//                qq需要获取读写文件权限
                if (constantMethod == null) {
                    constantMethod = new ConstantMethod();
                }
                constantMethod.setOnGetPermissionsSuccess(new ConstantMethod.OnGetPermissionsSuccessListener() {
                    @Override
                    public void getPermissionsSuccess() {
                        new ShareAction(context).setPlatform(platformType)
                                .withMedia(web)
                                .setCallback(umShareListener)
                                .share();
                    }
                });
                alertDialogShareHelper.setLoading(1);
                constantMethod.getPermissions(context, com.yanzhenjie.permission.Permission.WRITE_EXTERNAL_STORAGE);
                isSharing = false;
                break;
            default:
                //                        分享链接
                new ShareAction(context).setPlatform(platformType)
                        .withMedia(web)
                        .setCallback(umShareListener)
                        .share();
                break;
        }
    }

    /**
     * 获取图片地址
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
        NetLoadUtils.getNetInstance().loadNetDataPost(context, BASE_URL + SHARE_SAVE_IMAGE_URL, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = new Gson().fromJson(result, RequestStatus.class);
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
                                                showToast(context, R.string.saveSuccess);
                                            }
                                            // 其次把文件插入到系统图库
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
                                showToast(context, "相片已保存在相册，赶紧去分享吧~");
                            }
                            if (alertDialogShareHelper != null) {
                                alertDialogShareHelper.dismiss();
                            }
                        }
                    } else {
                        showToast(context, getStrings(requestStatus.getMsg()));
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
                showToast(context, getPlatFormText(platform) + " 分享成功啦");
            }
            if (onShareSuccessListener != null) {
                onShareSuccessListener.onShareSuccess();
            }
            if (alertDialogShareHelper != null) {
                alertDialogShareHelper.dismiss();
            }
            isSharing = false;
            //分享成功调用后台接口获取奖励
            if (userId > 0) {
                shareRewardSuccess(userId, context);
            }

            //统计用户分享行为
            statisticsShare(context, id, title, 1, platform);

            //文章分享统计
            if (needStatistics(context)) {
                ConstantMethod.addArticleShareCount(context, id);
            }

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            isSharing = false;
            if (alertDialogShareHelper != null) {
                alertDialogShareHelper.setLoading(1);
            }
            showToast(context, getPlatFormText(platform) + " 分享失败,请检查是否安装该应用");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            isSharing = false;
            showToast(context, getPlatFormText(platform) + " 分享取消了");
            if (alertDialogShareHelper != null) {
                alertDialogShareHelper.dismiss();
            }
            //统计用户分享行为
            statisticsShare(context, id, title, 0, platform);
        }
    };

    /**
     * 统计用户分享行为
     *
     * @param activity
     * @param objId    对应的内容ID，如-分享文章类型，则传文章ID，对于一些固定的专区，如每周优选，传0即可
     * @param ObjName  分享对象名称， 如分享文章类型，则传文章标题
     * @param status   要分享的平台
     * @param platform 分享状态 （0-取消，1-成功） 如果不能获取到分享是否被取消，则固定传1
     */
    private void statisticsShare(Activity activity, int objId, String ObjName, int status, SHARE_MEDIA platform) {
        int shareType = getShareType(activity.getClass().getSimpleName());
        if (shareType == -1) return;
        Map<String, Object> map = new HashMap<>();
        map.put("shareType", getShareType(activity.getClass().getSimpleName()));
        map.put("objId", objId);
        map.put("road", getShareRoad(platform));
        map.put("objName", ObjName);
        map.put("status", status);
        NetLoadUtils.getNetInstance().loadNetDataPost(context, Url.STATISTICS_SHARE, map, null);
    }

    /**
     * 返回分享平台名字
     *
     * @param platform 平台
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
                platformText = "微信";
                break;
            case WEIXIN_CIRCLE:
                platformText = "微信朋友圈";
                break;
            case SINA:
                platformText = "新浪微博";
                break;
        }
        return platformText;
    }


    /**
     * 返回分享途径字符串
     *
     * @param platform 平台
     * @return
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
     *获取分享类型
     * @param platform 平台
     */
    private int getShareType(String simpleName) {
        switch (simpleName) {
            case "ShopScrollDetailsActivity"://商品
                return 1;
            case "ArticleOfficialActivity"://文章
                return 2;
            case "ArticleDetailsImgActivity"://帖子
                return 3;
            case "EditorSelectActivity"://小编精选
                return 4;
            case "WholePointSpikeProductActivity"://整点秒杀
                return 5;
            case "QualityNewUserActivity"://新人专享
                return 6;
            case "QualityGroupShopDetailActivity"://品牌团
                return 7;
            case "DoMoLifeWelfareDetailsActivity"://福利社
                return 8;
            case "QualityCustomTopicActivity"://自定义专区
                return 9;
            case "QualityTypeProductActivity"://活动专区
                return 10;
            case "DmlOptimizedSelDetailActivity"://多么定制
                return 11;
            case "QualityShopBuyListActivity"://必买清单
            case "QualityShopHistoryListActivity"://历史清单
                return 12;
            case "QualityWeekOptimizedActivity"://每周优选
                return 13;
            default:
                return -1;
        }
    }

    //判断该页面分享成功之后是否需要 --统计文章分享数量
    private boolean needStatistics(Activity activity) {
        switch (activity.getClass().getSimpleName()) {
            case "ArticleDetailsImgActivity":
            case "ArticleInvitationDetailsActivity":
            case "DmlLifeSearchDetailActivity":
            case "DmlOptimizedSelDetailActivity":
            case "DoMoLifeWelfareDetailsActivity":
            case "ArticleOfficialActivity":
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
}
