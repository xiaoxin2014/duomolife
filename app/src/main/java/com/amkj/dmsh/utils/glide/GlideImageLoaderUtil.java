package com.amkj.dmsh.utils.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.opengl.GLES10;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.utils.AsyncUtils;
import com.amkj.dmsh.utils.FileStreamUtils;
import com.amkj.dmsh.utils.glide.RoundedCornersTransformation.CornerType;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;
import static com.amkj.dmsh.constant.CommunalSavePutValueVariable.FILE_IMAGE;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;
import static com.amkj.dmsh.utils.AsyncUtils.createExecutor;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * 图片加载工具类
 *
 * @author liangzx
 */
public class GlideImageLoaderUtil {

    /**
     * 加载图片根据控件设置来展示
     */
    public static void loadFitCenter(Context context, ImageView iv, String imgUrl) {
        if (isContextExisted(context) && iv != null) {
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions().fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .error(R.drawable.load_loading_image))
                    .transition(withCrossFade())
                    .into(iv);
        }
    }

    public static void loadImage(Context context, final ImageView iv, String imgUrl) {
        if (isContextExisted(context) && iv != null) {
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.DATA))
                    .error(R.drawable.load_loading_image)
                    .into(iv);
        }
    }

    public static void loadCenterCrop(Context context, final ImageView iv, String imgUrl) {
        if (isContextExisted(context) && iv != null) {
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions().centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .error(R.drawable.load_loading_image))
                    .transition(withCrossFade())
                    .into(iv);
        }
    }

    public static void loadCenterCrop(Context context, ImageView iv, String imgUrl, int defaultImgResource) {
        loadCenterCrop(context, iv, imgUrl, defaultImgResource, R.drawable.load_loading_image);
    }

    public static void loadCenterCrop(Context context, ImageView iv, String imgUrl, int defaultImgResource, int errorImgResource) {
        if (isContextExisted(context) && iv != null) {
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions().dontAnimate()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .placeholder(defaultImgResource)
                            .error(errorImgResource))
                    .transition(withCrossFade())
                    .into(iv);
        }
    }

    //base64转bitmap
    public static Bitmap base64ToBitmap(String base64String) {
        try {
            //不占用内存的情况下获取bitmap宽高
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            byte[] decode1 = Base64.decode(base64String.replaceAll("data:image/png;base64,", ""), Base64.DEFAULT);
            BitmapFactory.decodeByteArray(decode1, 0, decode1.length, options);
            int imgWidth = options.outWidth;

            //采样率压缩
            int screenWidth = ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getScreenWidth();
            if (imgWidth > screenWidth) {
                double inSampleSize = imgWidth * 1.0f / screenWidth;
                options.inSampleSize = (int) Math.round(inSampleSize);//采样率只支持整数，所以这里采取4舍5入
            }

            //质量压缩
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            //如果有前缀解码时会抛异常java.lang.IllegalArgumentException: bad base-64
            byte[] decode = Base64.decode(base64String.replaceAll("data:image/png;base64,", ""), Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decode, 0, decode.length, options);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl
     */
    public static void loadHeaderImg(final Context context, final ImageView iv, String imgUrl) {
        if (isContextExisted(context) && iv != null) {
            Glide.with(context).load(getThumbImgUrl(imgUrl, 200))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)
                            .centerCrop()
                            .placeholder(R.drawable.default_ava_img)
                            .error(R.drawable.default_ava_img))
                    .into(iv);
        }
    }

    /**
     * 加载正方形图片
     */
    public static void loadSquareImg(Context context, final ImageView iv, String imgUrl, String waterRemark, int sizeValue) {
        if (isContextExisted(context) && iv != null) {
            Glide.with(context).load(getSquareImgUrl(imgUrl, sizeValue, waterRemark))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .centerCrop().error(R.drawable.load_loading_image))
                    .transition(withCrossFade())
                    .into(iv);
        }
    }


    /**
     * 获取缩略图
     */
    public static String getThumbImgUrl(String imgUrl, int sizeValue) {
        if (!TextUtils.isEmpty(imgUrl) && imgUrl.contains(getOSSDataUrl())) {
            String ossPrefix = "?x-oss-process=image";
            String ossImgThumbSize = "/auto-orient,1/resize,w_" + sizeValue;
            if (imgUrl.contains(ossPrefix)) {
                return imgUrl + ossImgThumbSize;
            } else {
                return imgUrl + ossPrefix + ossImgThumbSize;
            }
        }
        return imgUrl;
    }

    //裁剪指定尺寸的正方形图片(先缩放成指定尺寸，然后居中裁剪成正方形)
    public static String getSquareImgUrl(String imgUrl, int sizeValue, String waterRemark) {
        if (!TextUtils.isEmpty(imgUrl) && imgUrl.contains(getOSSDataUrl())) {
            String ossPrefix = "?x-oss-process=image";
            String ossImg = "/resize,m_fill,w_" + sizeValue + ",limit_0/auto-orient,1" + (!imgUrl.contains(".gif") ? "/format,jpg" : "");//转成jpg格式，修复透明底色封面图水印显示不全的问题
            if (imgUrl.contains(ossPrefix)) {
                return imgUrl + ossImg + (!TextUtils.isEmpty(waterRemark) ? ("/" + getStrings(waterRemark)) : "");
            } else {
                return imgUrl + ossPrefix + ossImg + (!TextUtils.isEmpty(waterRemark) ? ("/" + getStrings(waterRemark)) : "");
            }
        }
        return imgUrl;
    }

    /**
     * 获取水印图片
     */
    public static String getWaterMarkImgUrl(String imgUrl, String waterRemark) {
        if (!TextUtils.isEmpty(imgUrl) && imgUrl.contains(getOSSDataUrl())) {
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

    //获取视频第一帧
    public static String getVideoSnapShot(String videoUrl) {
        if (!TextUtils.isEmpty(videoUrl) && videoUrl.contains(getOSSDataUrl())) {
            String ossPrefix = "?x-oss-process=video";
            String ossImgResizeOri = "/snapshot,t_100,f_jpg";
            return videoUrl + (videoUrl.contains(ossPrefix) ? "" : ossPrefix) + ossImgResizeOri;
        }
        return "";
    }

    /**
     * 获取矩形圆角
     */
    public static String getCornerImg(String imgUrl, int radius) {
        if (!TextUtils.isEmpty(imgUrl) && imgUrl.contains(getOSSDataUrl())) {
            String ossPrefix = "?x-oss-process=image";
            String ossImgRadius = "/rounded-corners,r_" + radius + "/format,png";
            if (imgUrl.contains(ossPrefix)) {
                return imgUrl + ossImgRadius;
            } else {
                return imgUrl + ossPrefix + ossImgRadius;
            }
        }
        return imgUrl;
    }

    /**
     * 加载矩形圆角
     */
    public static void loadRoundImg(final Context context, final ImageView iv, String imgUrl, int radius) {
        loadRoundImg(context, iv, imgUrl, radius, R.drawable.load_loading_image);
    }

    /**
     * 加载矩形圆角
     *
     * @param defaultImgResource 占位图
     */
    public static void loadRoundImg(final Context context, final ImageView iv, String imgUrl, int radius, int defaultImgResource) {
        loadRoundImg(context, iv, imgUrl, radius, defaultImgResource, CornerType.ALL);
    }


    /**
     * 加载矩形圆角
     *
     * @param cornerType 指定圆角类型
     */
    public static void loadRoundImg(final Context context, final ImageView iv, String imgUrl, int radius, int defaultImgResource, CornerType cornerType) {
        if (isContextExisted(context) && iv != null) {
            if (TextUtils.isEmpty(imgUrl)) {
                imgUrl = "android.resource://com.amkj.dmsh/" + defaultImgResource;
            }
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions().dontAnimate()
                            .placeholder(defaultImgResource)
                            .error(defaultImgResource)
                            .transform(new CenterCrop(), new RoundedCornersTransformation(radius, 0, cornerType)))
                    .into(iv);
        }
    }

    /**
     * 图片加载监听
     *
     * @param loaderFinishListener 加载回调
     */
    public static void setLoadImgFinishListener(final Context context, String imgUrl, final ImageLoaderFinishListener loaderFinishListener) {
        if (isContextExisted(context)) {
            Glide.with(context).asBitmap().load(imgUrl)
                    .apply(new RequestOptions().centerCrop()
                            .placeholder(R.drawable.load_loading_image))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition transition) {
                            loaderFinishListener.onSuccess(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            loaderFinishListener.onError();
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            loaderFinishListener.onError();
                        }
                    });
        }
    }

    /**
     * gif加载监听
     *
     * @param loaderFinishListener 加载回调
     */
    public static void setLoadGifFinishListener(final Context context, String imgUrl, final GifLoaderFinishListener loaderFinishListener) {
        if (isContextExisted(context)) {
            Glide.with(context).asGif().load(imgUrl)
                    .apply(new RequestOptions().centerCrop()
                            .placeholder(R.drawable.load_loading_image)
                            .diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(new CustomTarget<GifDrawable>() {
                        @Override
                        public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition transition) {
                            loaderFinishListener.onSuccess(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            loaderFinishListener.onError();
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            loaderFinishListener.onError();
                        }
                    });
        }
    }

    /**
     * 自定义尺寸加载图片(避免大图无法加载)
     * 支持图片和gif
     *
     * @param with 图片指定尺寸  -1表示屏幕宽度
     */
    public static void loadImgDynamicDrawable(final Context context, final ImageView imageView, String imgUrl, int with) {
        setLoadDynamicFinishListener(context, imageView, imgUrl, with, null);
    }


    /**
     * 图片加载监听，只支持图片
     */
    public static void setLoadDynamicFinishListener(final Context context, final ImageView imageView, String imgUrl, int with, ImageLoaderFinishListener loaderFinishListener) {
        if (isContextExisted(context) && !TextUtils.isEmpty(imgUrl)) {
            new AsyncUtils<int[]>(context) {
                @Override
                public int[] runOnIO() {
                    return getImageUrlWidthHeight(imgUrl);
                }

                @Override
                public void runOnUI(int[] imageSize) {
                    int imgWidth = 0;
                    int imgHeight = 0;
                    try {
                        if (imageSize != null && imageSize[0] > 0 && imageSize[1] > 0) {
                            int screenWidth = (with == -1 ? ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getScreenWidth() : with);
                            //判断图片宽度是否大于屏幕宽度
//                            if (imgWidth > screenWidth) {
                            imgWidth = screenWidth;
                            imgHeight = screenWidth * imageSize[1] / imageSize[0];
//                            }

                            //判断缩放后的高度是否大于限制高度
                            int limitHeight = getOpenglRenderLimitValue();
                            if (imgHeight > limitHeight) {
                                imgWidth = (int) (limitHeight * 1.0f / (imgHeight * 1.0f) * imgWidth);
                                imgHeight = limitHeight;
                            }
                        }
                    } catch (Exception e) {
                        //原始尺寸
                        imgWidth = Target.SIZE_ORIGINAL;
                        imgHeight = Target.SIZE_ORIGINAL;
                    }
                    //针对RecyclerView使用Glide加载图片导致错位问题,优先获取tag
                    String tag = TextUtils.isEmpty((String) imageView.getTag(R.id.iv_tag)) ? imgUrl : (String) imageView.getTag(R.id.iv_tag);
                    if (isContextExisted(context)) {
                        if (loaderFinishListener == null) {
                            Glide.with(context)
                                    .load(tag).apply(new RequestOptions()
                                    .placeholder(R.drawable.load_loading_image)
                                    .error(R.drawable.load_loading_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .override(imgWidth, imgHeight))
                                    .into(imageView);
                        } else {
                            Glide.with(context).asBitmap()
                                    .load(tag).apply(new RequestOptions()
                                    .placeholder(R.drawable.load_loading_image)
                                    .error(R.drawable.load_loading_image)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .override(imgWidth, imgHeight))
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            imageView.setImageBitmap(resource);
                                            loaderFinishListener.onSuccess(resource);
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                            loaderFinishListener.onError();
                                        }

                                        @Override
                                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                            super.onLoadFailed(errorDrawable);
                                            loaderFinishListener.onError();
                                        }
                                    });
                        }
                    }
                }
            }.excueTask();
        }
    }


    /**
     * 图片下载监听回调
     */
    public static void downOriginalImg(Context context, String originalImgUrl, final OriginalLoaderFinishListener originalLoaderFinishListener) {
        if (isContextExisted(context)) {
            Observable.create(new ObservableOnSubscribe<File>() {
                @Override
                public void subscribe(ObservableEmitter<File> emitter) {
                    Glide.with(context).download(originalImgUrl).apply(new RequestOptions()
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
     * 清除Glide图片缓存
     */
    public static void clearCache(Context context) {
        if (null != context) {
            Glide.get(context).clearMemory();//清除内存缓存
            Glide.get(context).clearDiskCache();//清除磁盘缓存
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

    public interface GifLoaderFinishListener {

        void onSuccess(GifDrawable drawable);

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
                        //必须为子线程调用，否则阻塞线程
                        File file = Glide.with(context).downloadOnly().load(picUrl)
                                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)).submit().get();
                        if (file != null) {
                            FileStreamUtils.forChannel(file, new File(imageFilePath));
                        }
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
    public static void saveImageToFile(Context context, String picUrl, String
            saveFilePath, OriginalLoaderFinishListener imageLoaderListener) {
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
                                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)).submit().get();
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (imageLoaderListener != null) {
                        imageLoaderListener.onSuccess(new File(imageFilePath));
                    }
                }
            }
        });
    }

    /**
     * 获取当前文件路径 仅限
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
        //options.outHeight为原始图片的高
        return new int[]{options.outWidth, options.outHeight};
    }


    //获取分享封面图（整个页面控件减去状态栏和标题栏的高度）
    public static Bitmap getBitmapFromView(View v) {
        Bitmap b = null;
        try {
            b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredWidth(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            int left = v.getLeft();
            int top = v.getTop();
            int right = v.getRight();
            int bottom = v.getBottom();
            v.layout(left, top, right, bottom);
            // Draw background
            Drawable bgDrawable = v.getBackground();
            //绘制图片本身的背景
//            if (bgDrawable != null)
//                bgDrawable.draw(c);
//            else
            //手动设置背景
            c.drawColor(Color.WHITE);
            // Draw view to canvas
            v.draw(c);
        } catch (Exception e) {
            return null;
        }

        return b;
    }

    public static int getOpenglRenderLimitValue() {
        int maxsize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            maxsize = getOpenglRenderLimitEqualAboveLollipop();
        } else {
            maxsize = getOpenglRenderLimitBelowLollipop();
        }
        return maxsize == 0 ? 3574 : maxsize;
    }

    private static int getOpenglRenderLimitBelowLollipop() {
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        return maxSize[0];
    }

    private static int getOpenglRenderLimitEqualAboveLollipop() {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay dpy = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        int[] vers = new int[2];
        egl.eglInitialize(dpy, vers);
        int[] configAttr = {
                EGL10.EGL_COLOR_BUFFER_TYPE, EGL10.EGL_RGB_BUFFER,
                EGL10.EGL_LEVEL, 0,
                EGL10.EGL_SURFACE_TYPE, EGL10.EGL_PBUFFER_BIT,
                EGL10.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfig = new int[1];
        egl.eglChooseConfig(dpy, configAttr, configs, 1, numConfig);
        if (numConfig[0] == 0) {// TROUBLE! No config found.
        }
        EGLConfig config = configs[0];
        int[] surfAttr = {
                EGL10.EGL_WIDTH, 64,
                EGL10.EGL_HEIGHT, 64,
                EGL10.EGL_NONE
        };
        EGLSurface surf = egl.eglCreatePbufferSurface(dpy, config, surfAttr);
        final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;// missing in EGL10
        int[] ctxAttrib = {
                EGL_CONTEXT_CLIENT_VERSION, 1,
                EGL10.EGL_NONE
        };
        EGLContext ctx = egl.eglCreateContext(dpy, config, EGL10.EGL_NO_CONTEXT, ctxAttrib);
        egl.eglMakeCurrent(dpy, surf, surf, ctx);
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        egl.eglMakeCurrent(dpy, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE,
                EGL10.EGL_NO_CONTEXT);
        egl.eglDestroySurface(dpy, surf);
        egl.eglDestroyContext(dpy, ctx);
        egl.eglTerminate(dpy);
        return maxSize[0];
    }

    //获取Oss图片地址前缀
    private static String getOSSDataUrl() {
        return ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getOSSDataUrl().replace("https://", "").replace("http://", "");
    }
}
