package com.amkj.dmsh.utils.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.utils.FileStreamUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;
import static com.amkj.dmsh.constant.ConstantMethod.createExecutor;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;
import static com.amkj.dmsh.constant.CommunalSavePutValueVariable.FILE_IMAGE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

;

/**
 * 图片加载工具类
 *
 * @author liangzx
 */
public class GlideImageLoaderUtil {

    /**
     * @param context
     * @param iv
     * @param imgUrl
     */
    public static void loadFitCenter(Context context, ImageView iv, String imgUrl) {
        if (null != context) {
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions().fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .error(R.drawable.load_loading_image))
                    .transition(withCrossFade())
                    .into(iv);
        }
    }

    /**
     * 加载图片根据控件设置来展示
     *
     * @param context
     * @param iv
     * @param imgUrl
     */
    public static void loadImage(Context context, final ImageView iv, String imgUrl) {
        if (isContextExisted(context)) {
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.load_loading_image)
                            .diskCacheStrategy(DiskCacheStrategy.DATA))
                    .into(iv);
        }
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl
     */
    public static void loadCenterCrop(Context context, final ImageView iv, String imgUrl) {
        if (null != context) {
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .centerCrop().error(R.drawable.load_loading_image))
                    .transition(withCrossFade())
                    .into(iv);
        }
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl
     */
    public static void loadCenterCropListener(Context context, final ImageView iv, String imgUrl
            , ImageLoaderListener imageLoaderListener) {
        if (null != context) {
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .skipMemoryCache(true)
                            .centerCrop().error(R.drawable.load_loading_image))
                    .transition(withCrossFade())
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            if (imageLoaderListener != null) {
                                imageLoaderListener.onError();
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (imageLoaderListener != null) {
                                imageLoaderListener.onSuccess();
                            }
                            return false;
                        }
                    })
                    .into(iv);
        }
    }

    /**
     * 加载双列商品图片 方形
     *
     * @param context
     * @param iv
     * @param imgUrl
     */
    public static void loadThumbCenterCrop(Context context, final ImageView iv, String imgUrl, String waterRemark, boolean isDouble) {
        if (null != context && iv != null) {
            Glide.with(context).load(getThumbImgUrl(imgUrl, waterRemark, isDouble))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)
                            .centerCrop().error(R.drawable.load_loading_image))
                    .transition(withCrossFade())
                    .into(iv);
        }
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl
     * @param defaultImgResource
     */
    public static void loadCenterCrop(Context context, ImageView iv, String imgUrl,
                                      int defaultImgResource) {
        if (null != context) {
            loadCenterCrop(context, iv, imgUrl, defaultImgResource, R.drawable.load_loading_image);
        }
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl
     * @param defaultImgResource
     */
    public static void loadCenterCrop(Context context, ImageView iv, String imgUrl,
                                      int defaultImgResource, int errorImgResource) {
        if (null != context) {
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions().dontAnimate()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .placeholder(defaultImgResource)
                            .error(errorImgResource)).transition(withCrossFade()).into(iv);
        }
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl
     */
    public static void loadHeaderImg(final Context context, final ImageView iv, String imgUrl) {
        if (null != context) {
            Glide.with(context).load(getHeaderThumbImgUrl(imgUrl))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)
                            .centerCrop()
                            .placeholder(R.drawable.default_ava_img)
                            .error(R.drawable.default_ava_img))
                    .into(iv);
        }
    }

    /**
     * 获取缩略图
     *
     * @param imgUrl
     * @return
     */
    public static String getThumbImgUrl(String imgUrl) {
        return getThumbImgUrl(imgUrl, "", true);
    }

    public static String getThumbImgUrl(String imgUrl, String waterRemark, boolean isDouble) {
        TinkerBaseApplicationLike applicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        String ossDataUrl = applicationLike.getOSSDataUrl();
        if (!TextUtils.isEmpty(imgUrl) && imgUrl.contains(ossDataUrl)) {
            String ossPrefix = "?x-oss-process=image";
            String ossImgResizeOri = "/auto-orient,1";
            String ossImgDoubleThumb = "/resize,w_400";
            String ossImgThreeThumb = "/resize,w_300";
            if (imgUrl.contains(ossPrefix)) {
                return imgUrl + (isDouble ? ossImgDoubleThumb : ossImgThreeThumb) + ossImgResizeOri + (!TextUtils.isEmpty(waterRemark) ? ("/" + getStrings(waterRemark)) : "");
            } else {
                return imgUrl + ossPrefix + (isDouble ? ossImgDoubleThumb : ossImgThreeThumb) + ossImgResizeOri + (!TextUtils.isEmpty(waterRemark) ? ("/" + getStrings(waterRemark)) : "");
            }
        }
        return imgUrl;
    }

    public static String getThumbImgUrl(String imgUrl, int sizeValue) {
        TinkerBaseApplicationLike applicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        String ossDataUrl = applicationLike.getOSSDataUrl();
        if (!TextUtils.isEmpty(imgUrl)
                && imgUrl.contains(ossDataUrl)) {
            String ossPrefix = "?x-oss-process=image";
            String ossImgResizeOri = "/auto-orient,1";
            if (sizeValue < 50) {
                sizeValue = 50;
            }
            String ossImgThumbSize = "/resize,w_" + sizeValue;
            if (imgUrl.contains(ossPrefix)) {
                return imgUrl + ossImgResizeOri + ossImgThumbSize;
            } else {
                return imgUrl + ossPrefix + ossImgResizeOri + ossImgThumbSize;
            }
        }
        return imgUrl;
    }


    /**
     * 获取水印图片
     *
     * @param imgUrl
     * @param waterRemark
     * @return
     */
    public static String getWaterMarkImgUrl(String imgUrl, String waterRemark) {
        TinkerBaseApplicationLike applicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        String ossDataUrl = applicationLike.getOSSDataUrl();
        if (!TextUtils.isEmpty(imgUrl) && imgUrl.contains(ossDataUrl)) {
            String ossPrefix = "?x-oss-process=image";
            String ossImgResizeOri = "/auto-orient,1";
            if (imgUrl.contains(ossPrefix)) {
                return imgUrl + ossImgResizeOri + (!TextUtils.isEmpty(waterRemark) ? ("/" + getStrings(waterRemark)) : "");
            } else {
                return imgUrl + ossPrefix + ossImgResizeOri + (!TextUtils.isEmpty(waterRemark) ? ("/" + getStrings(waterRemark)) : "");
            }
        }
        return imgUrl;
    }

    /**
     * 获取头像缩略图
     *
     * @param imgUrl
     * @return
     */
    private static String getHeaderThumbImgUrl(String imgUrl) {
        TinkerBaseApplicationLike applicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        String ossDataUrl = applicationLike.getOSSDataUrl();
        if (!TextUtils.isEmpty(imgUrl) && imgUrl.contains(ossDataUrl)) {
            String ossPrefix = "?x-oss-process=image";
            String ossImgResizeOri = "/auto-orient,1";
            String ossImgHeaderThumb = "/resize,w_200";
            if (imgUrl.contains(ossPrefix)) {
                return imgUrl + ossImgHeaderThumb + ossImgResizeOri;
            } else {
                return imgUrl + ossPrefix + ossImgHeaderThumb + ossImgResizeOri;
            }
        }
        return imgUrl;
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl
     * @param defaultImgResource
     */
    public static void loadWithFitCenter(Context context, ImageView iv, String imgUrl,
                                         int defaultImgResource) {
        if (null != context) {
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions()
                            .fitCenter().placeholder(defaultImgResource))
                    .transition(withCrossFade())
                    .into(iv);
        }
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl  加载矩形圆角
     */
    public static void loadRoundImg(final Context context, final ImageView iv,
                                    String imgUrl, int radius) {
        if (null != context) {
            loadRoundImg(context, iv, imgUrl, radius, R.drawable.load_loading_image);
        }
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl
     * @param defaultImgResource 加载矩形圆角
     */
    public static void loadRoundImg(final Context context, final ImageView iv,
                                    String imgUrl, int radius, int defaultImgResource) {
        if (null != context) {
            if (TextUtils.isEmpty(imgUrl)) {
                imgUrl = "android.resource://com.amkj.dmsh/" + defaultImgResource;
            }
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions().dontAnimate()
                            .placeholder(defaultImgResource)
                            .error(defaultImgResource)
                            .transforms(new GlideRoundTransform(radius)))
                    .into(iv);
        }
    }

    /**
     * @param context
     * @param imgUrl
     * @return
     */
    public static Bitmap get(Context context, String imgUrl) {
        Bitmap bitmap = null;
        try {
            if (null != context) {
                bitmap = Glide.with(context).asBitmap()
                        .load(imgUrl).submit(100, 100).get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 带回调的，例如启动页广告需要加载完成在展示
     * 启动一个子线程去加载图片
     *
     * @param context
     * @param imgUrl
     * @param loaderFinishListener
     * @see ImageLoaderFinishListener
     */
    public static void loadFinishImgDrawable(final Context context, String imgUrl, final ImageLoaderFinishListener loaderFinishListener) {
        if (null != context) {
            Observable.create(new ObservableOnSubscribe<Bitmap>() {
                @Override
                public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                    RequestOptions requestOptions = new RequestOptions().centerCrop()
                            .placeholder(R.drawable.load_loading_image)
                            .skipMemoryCache(true);
                    Glide.with(context.getApplicationContext()).asBitmap().load(imgUrl)
                            .apply(requestOptions)
                            .listener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                    emitter.onComplete();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(@NonNull Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                    emitter.onNext(resource);
                                    return true;
                                }
                            })
                            .submit();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Bitmap>() {
                Disposable disposable;

                @Override
                public void onSubscribe(Disposable d) {
                    this.disposable = d;
                }

                @Override
                public void onNext(Bitmap bitmap) {
                    if (loaderFinishListener != null) {
                        loaderFinishListener.onSuccess(bitmap);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if (loaderFinishListener != null) {
                        loaderFinishListener.onError();
                    }
                }

                @Override
                public void onComplete() {
                    if (loaderFinishListener != null) {
                        loaderFinishListener.onError();
                    }
                }
            });
        }
    }

    /**
     * 原图加载
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public static void loadImgDynamicDrawable(final Context context, final ImageView imageView, String imgUrl) {
        if (null != context) {
            // 原图加载避免大图无法加载 预判尺寸是否大于内存最大值
            Map<String,Object> params = new HashMap<>();
            params.put("imgUrl",imgUrl);
            params.put("imgView",imageView);
            Observable.create(new ObservableOnSubscribe<Map<String,Object>>() {
                @Override
                public void subscribe(ObservableEmitter<Map<String,Object>> emitter) throws Exception {
                    int[] imageUrlWidthHeight = getImageUrlWidthHeight(imgUrl);
                    if (imageUrlWidthHeight.length < 2) {
                        imageUrlWidthHeight = new int[]{0, 0};
                    }
                    params.put("imgSize",imageUrlWidthHeight);
                    emitter.onNext(params);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Map<String,Object>>() {
                Disposable disposable;

                @Override
                public void onSubscribe(Disposable d) {
                    this.disposable = d;
                }

                @Override
                public void onNext(Map<String,Object> params) {
                    int imgWidth = Target.SIZE_ORIGINAL;
                    int imgHeight = Target.SIZE_ORIGINAL;
                    String imgUrlX = null;
                    ImageView imageViewX = null;
                    int[] imageUrlWidthHeight = new int[]{imgWidth,imgHeight};
                    try {
                        imgUrlX = (String) params.get("imgUrl");
                        imageViewX = (ImageView) params.get("imgView");
                        imageUrlWidthHeight = (int[]) params.get("imgSize");
                        if (imageUrlWidthHeight.length > 1) {
                            imgWidth = imageUrlWidthHeight[0];
                            imgHeight = imageUrlWidthHeight[1];
                            if (imageUrlWidthHeight[1] > 3574) {
                                imgWidth = (int) (3574f / imgHeight * imgWidth);
                                imgHeight = 3574;
                            }
                        }
                        if (isContextExisted(context)&&imageViewX!=null) {
                            Glide.with(context).asDrawable().load(imgUrlX)
                                    .apply(new RequestOptions().dontAnimate()
                                            .error(R.drawable.load_loading_image)
                                            .override(imgWidth, imgHeight))
                                    .into(imageViewX);
                        }
                    } catch (Exception e) {
                        if (isContextExisted(context)&&imageViewX!=null) {
                            Glide.with(context).asDrawable().load(imgUrl)
                                    .apply(new RequestOptions().dontAnimate()
                                            .error(R.drawable.load_loading_image)
                                            .override(imgWidth, imgHeight))
                                    .into(imageViewX);
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if(disposable!=null){
                        disposable.dispose();
                    }
                }

                @Override
                public void onComplete() {
                }
            });
        }
    }

    /**
     * 动图加载
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public static void loadGif(final Context context, final ImageView imageView, String imgUrl) {
        if (null != context) {
            // 原图加载避免大图无法加载 预判尺寸是否大于内存最大值
            Map<String, Object> params = new HashMap<>();
            params.put("imgUrl", imgUrl);
            params.put("imgView", imageView);
            Observable.create(new ObservableOnSubscribe<Map<String, Object>>() {
                @Override
                public void subscribe(ObservableEmitter<Map<String, Object>> emitter) throws Exception {
                    int[] imageUrlWidthHeight = getImageUrlWidthHeight(imgUrl);
                    if (imageUrlWidthHeight.length < 2) {
                        imageUrlWidthHeight = new int[]{0, 0};
                    }
                    params.put("imgSize", imageUrlWidthHeight);
                    emitter.onNext(params);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Map<String, Object>>() {
                Disposable disposable;

                @Override
                public void onSubscribe(Disposable d) {
                    this.disposable = d;
                }

                @Override
                public void onNext(Map<String, Object> params) {
                    int imgWidth = Target.SIZE_ORIGINAL;
                    int imgHeight = Target.SIZE_ORIGINAL;
                    String imgUrlX = null;
                    ImageView imageViewX = null;
                    int[] imageUrlWidthHeight;
                    try {
                        imgUrlX = (String) params.get("imgUrl");
                        imageViewX = (ImageView) params.get("imgView");
                        imageUrlWidthHeight = (int[]) params.get("imgSize");
                        if (imageUrlWidthHeight.length > 1) {
                            imgWidth = imageUrlWidthHeight[0];
                            imgHeight = imageUrlWidthHeight[1];
                            if (imageUrlWidthHeight[1] > 3574) {
                                imgWidth = (int) (3574f / imgHeight * imgWidth);
                                imgHeight = 3574;
                            }
                        }
                        if (isContextExisted(context)&&imageViewX!=null) {
                            Glide.with(context).load(imgUrlX)
                                    .apply(new RequestOptions()
                                            .error(R.drawable.load_loading_image)
                                            .skipMemoryCache(true)
                                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                                            .override(imgWidth, imgHeight))
                                    .into(imageViewX);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (isContextExisted(context)&&imageViewX!=null) {
                            Glide.with(context).load(imgUrlX)
                                    .apply(new RequestOptions()
                                            .error(R.drawable.load_loading_image)
                                            .skipMemoryCache(true)
                                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                                            .override(imgWidth, imgHeight))
                                    .into(imageViewX);
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                }
            });
        }
    }

    /**
     * 动态图片带回调
     *
     * @param context
     * @param originalImgUrl
     * @param originalLoaderFinishListener
     */
    public static void downOriginalImg(Context context, String originalImgUrl, final OriginalLoaderFinishListener originalLoaderFinishListener) {
        if (null != context) {
            Observable.create(new ObservableOnSubscribe<File>() {
                @Override
                public void subscribe(ObservableEmitter<File> emitter) throws Exception {
                    Glide.with(context).download(originalImgUrl).apply(new RequestOptions().skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)).listener(new RequestListener<File>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                            emitter.onError(e);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                            emitter.onNext(resource);
                            return true;
                        }
                    }).submit();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<File>() {
                Disposable disposable;

                @Override
                public void onSubscribe(Disposable d) {
                    this.disposable = d;
                }

                @Override
                public void onNext(File resource) {
                    if (null != originalLoaderFinishListener) {
                        originalLoaderFinishListener.onSuccess(resource);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if (null != originalLoaderFinishListener) {
                        originalLoaderFinishListener.onError();
                    }
                }

                @Override
                public void onComplete() {
                    if (null != originalLoaderFinishListener) {
                        originalLoaderFinishListener.onError();
                    }
                }
            });
        }
    }

    //判断文件是否存在
    public static boolean fileIsExist(String invoiceSavePath) {
        File file = new File(invoiceSavePath);
        return file.exists();
    }

    //创建文件夹
    public static void createFilePath(String savePath) {
        File destDir = new File(savePath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    /**
     * 停止加载图片
     *
     * @param context
     */
    public static void stopLoadByContext(Context context) {
        if (null != context) {
            Glide.with(context).pauseRequests();
        }
    }

    /**
     * 停止加载图片
     *
     * @param activity
     */
    public static void stopLoadByActivity(Activity activity) {
        if (null != activity) {
            Glide.with(activity).pauseRequests();
        }
    }

    /**
     * 恢复加载图片
     *
     * @param activity
     */
    public static void resumeLoadByActivity(Activity activity) {
        if (null != activity) {
            Glide.with(activity).resumeRequests();
        }
    }

    /**
     * 恢复加载图片
     *
     * @param context
     */
    public static void resumeLoadByContext(Context context) {
        if (null != context) {
            Glide.with(context).resumeRequests();
        }
    }

    /**
     * 清除图片缓存
     *
     * @param context
     */
    public static void clearMemoryByContext(Context context) {
        if (null != context) {
            Glide.get(context).clearMemory();
        }
    }

    /**
     * 清除图片缓存
     *
     * @param activity
     */
    public static void clearMemoryByActivity(Activity activity) {
        if (null != activity) {
            Glide.get(activity.getApplicationContext()).clearMemory();
        }
    }

    public static Bitmap getBitmap(Context context, String imgUrl) {
        Bitmap bitmap = null;
        try {
            if (null != context) {
                bitmap = Glide.with(context).asBitmap()
                        .load(imgUrl).submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 获取图片大小
     *
     * @param path
     * @return
     */
    private static int[] getImageSizePath(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        return new int[]{options.outWidth, options.outHeight};
    }

    /**
     * 获取网络图片尺寸
     *
     * @param imageUrl
     * @return
     */
    private static int[] getImageSizeUrl(String imageUrl) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            InputStream inputStream = new URL(imageUrl).openStream();
            BitmapFactory.decodeStream(inputStream, null, options);
            return new int[]{options.outWidth, options.outHeight};
        } catch (IOException e) {
            return new int[]{300, 300};
        }
    }

    public interface ImageLoaderFinishListener {
        void onSuccess(Bitmap bitmap);

        void onError();
    }

    public interface OriginalLoaderFinishListener {

        void onSuccess(File file);

        void onError();
    }

    public interface gifLoaderFinishListener {

        void onSuccess(Drawable drawable);

        void onError(Drawable errorDrawable);
    }

    /**
     * 监听图片加载过程
     */
    public interface ImageLoaderListener {

        void onSuccess();

        void onError();

    }

    /**
     * 保存图片文件到app存储
     * 子线程调用
     *
     * @param picUrl
     */
    public static void saveImageToFile(Context context, String picUrl) {
        if (TextUtils.isEmpty(picUrl)) {
            return;
        }
        createExecutor().execute(new Runnable() {
            @Override
            public void run() {
                String filePath = context.getDir(FILE_IMAGE, MODE_PRIVATE).getAbsolutePath();
                createFilePath(filePath);
                String imageFilePath = filePath + "/" + picUrl.substring(picUrl.lastIndexOf("/"));
                if (!fileIsExist(imageFilePath)) {
                    try {
//                必须为子线程调用，否则阻塞线程
                        File file = Glide.with(context).downloadOnly().load(picUrl)
                                .apply(new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.DATA)).submit().get();
                        if (file != null) {
                            FileStreamUtils.forChannel(file, new File(imageFilePath));
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 保存图片文件到app存储
     * 子线程调用
     *
     * @param picUrl
     */
    public static void saveImageToFile(Context context, String picUrl, String saveFilePath, OriginalLoaderFinishListener imageLoaderListener) {
        if (TextUtils.isEmpty(picUrl)) {
            return;
        }
        createExecutor().execute(new Runnable() {
            @Override
            public void run() {
                String saveFileName = FILE_IMAGE;
                if (!TextUtils.isEmpty(saveFilePath)) {
                    saveFileName = saveFilePath;
                }
                String filePath = context.getDir(saveFileName, MODE_PRIVATE).getAbsolutePath();
                createFilePath(filePath);
                String imageFilePath = filePath + picUrl.substring(picUrl.lastIndexOf("/"));
                if (!fileIsExist(imageFilePath)) {
                    try {
//                必须为子线程调用，否则阻塞线程
                        File file = Glide.with(context).downloadOnly().load(picUrl)
                                .apply(new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.DATA)).submit().get();
                        if (file != null) {
                            try {
                                File saveFile = new File(imageFilePath);
                                FileStreamUtils.forChannel(file, saveFile);
                                if (imageLoaderListener != null) {
                                    imageLoaderListener.onSuccess(saveFile);
                                }
                            } catch (Exception e) {
                                if (imageLoaderListener != null) {
                                    imageLoaderListener.onError();
                                }
                                e.printStackTrace();
                            }
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    if (imageLoaderListener != null) {
                        imageLoaderListener.onSuccess(new File(imageFilePath));
                    }
                }
            }
        });
    }

    /**
     * 获取当前文件路径 仅限
     *
     * @param context
     * @param picUrl
     * @return
     */
    public static String getImageFilePath(Context context, String picUrl) {
        if (TextUtils.isEmpty(picUrl)) {
            return "";
        }
        String filePath = context.getDir(FILE_IMAGE, MODE_PRIVATE).getAbsolutePath();
        createFilePath(filePath);
        int indexCode = picUrl.lastIndexOf("/");
        if (indexCode > -1) {
            return filePath + "/" + picUrl.substring(indexCode);
        } else {
            return "";
        }
    }

    /**
     * 获取图片宽高
     *
     * @param path
     * @return
     */
    public static int[] getImageUrlWidthHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        InputStream inputStream = null;
        try {
            URL url = new URL(path);
            inputStream = url.openStream();
            BitmapFactory.decodeStream(inputStream, null, options); // 此时返回的bitmap为null
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        /**
         *options.outHeight为原始图片的高
         */
        return new int[]{options.outWidth, options.outHeight};
    }

    /**
     * 网络图片获取宽高
     *
     * @param imgUrl
     * @return
     */
    public static int[] getImageWidthHeight(String imgUrl) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgUrl, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        return new int[]{options.outWidth, options.outHeight};
    }

    /**
     * 获取当前应用最大能展示的图片尺寸
     */
//    public static int getMaxLoader() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            return getGLESTextureLimitEqualAboveLollipop();
//        } else {
//            return getGLESTextureLimitBelowLollipop();
//        }
//    }
//
//    private static int getGLESTextureLimitBelowLollipop() {
//        int[] maxSize = new int[1];
//        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
//        return maxSize[0];
//    }
//
//    private static int getGLESTextureLimitEqualAboveLollipop() {
//        EGL10 egl = (EGL10) EGLContext.getEGL();
//        EGLDisplay dpy = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
//        int[] vers = new int[2];
//        egl.eglInitialize(dpy, vers);
//        int[] configAttr = {
//                EGL10.EGL_COLOR_BUFFER_TYPE, EGL10.EGL_RGB_BUFFER,
//                EGL10.EGL_LEVEL, 0,
//                EGL10.EGL_SURFACE_TYPE, EGL10.EGL_PBUFFER_BIT,
//                EGL10.EGL_NONE
//        };
//        EGLConfig[] configs = new EGLConfig[1];
//        int[] numConfig = new int[1];
//        egl.eglChooseConfig(dpy, configAttr, configs, 1, numConfig);
//        if (numConfig[0] == 0) {// TROUBLE! No config found.
//        }
//        EGLConfig config = configs[0];
//        int[] surfAttr = {
//                EGL10.EGL_WIDTH, 64,
//                EGL10.EGL_HEIGHT, 64,
//                EGL10.EGL_NONE
//        };
//        EGLSurface surf = egl.eglCreatePbufferSurface(dpy, config, surfAttr);
//        final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;  // missing in EGL10
//        int[] ctxAttrib = {
//                EGL_CONTEXT_CLIENT_VERSION, 1,
//                EGL10.EGL_NONE
//        };
//        EGLContext ctx = egl.eglCreateContext(dpy, config, EGL10.EGL_NO_CONTEXT, ctxAttrib);
//        egl.eglMakeCurrent(dpy, surf, surf, ctx);
//        int[] maxSize = new int[1];
//        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
//        egl.eglMakeCurrent(dpy, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE,
//                EGL10.EGL_NO_CONTEXT);
//        egl.eglDestroySurface(dpy, surf);
//        egl.eglDestroyContext(dpy, ctx);
//        egl.eglTerminate(dpy);
//        return maxSize[0];
//    }
}
