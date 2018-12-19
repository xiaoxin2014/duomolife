package com.amkj.dmsh.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.netloadpage.NetEmptyCallback;
import com.amkj.dmsh.netloadpage.NetErrorCallback;
import com.amkj.dmsh.netloadpage.NetLoadCallback;
import com.amkj.dmsh.netloadpage.NetLoadTranslucenceCallback;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.release.util.LogUtils;
import com.amkj.dmsh.utils.FileCacheUtils;
import com.amkj.dmsh.utils.FileStreamUtils;
import com.kingja.loadsir.core.LoadSir;
import com.leon.channel.helper.ChannelReaderUtil;
import com.microquation.linkedme.android.LinkedME;
import com.mob.MobSDK;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatService;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.entry.DefaultApplicationLike;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.lasque.tusdk.core.TuSdk;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;

import static android.content.Context.MODE_PRIVATE;
import static com.amkj.dmsh.constant.ConstantMethod.createExecutor;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.OSS_BUCKET_NAME;
import static com.amkj.dmsh.constant.ConstantVariable.OSS_OBJECT;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_ID;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_NAME;
import static com.amkj.dmsh.constant.ConstantVariable.isDebugTag;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/9/25
 * version 3.1.7
 * class description:Tinker集成
 */
@SuppressWarnings("unused")
@DefaultLifeCycle(application = "com.amkj.dmsh.base.TinkerBaseApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class TinkerBaseApplicationLike extends DefaultApplicationLike {
    private OSS oss;
    private final String MobAPPKEY = "1693fa0f7b0a0";
    private final String MobAPPSECRET = "435ac0137e0179dafee0139a85f6ed92";
    private final String BUGLY_APP_ID = "385d38aeeb";
    private String BUCKET_NAME = "domolifes";
    private String OSS_URL;
    public IWXAPI api;
    private int screenWidth;
    private int screenHeight;
    private String analyzeKey = "A1M12V8YKBTI";
    private final String INIT_TAG = "初始化";            //安全加密和安全签名使用的秘钥在jpg中对应的key
    private float density;
    //    web链接替换
    private Map<String, String> webUrlTransform;
    //    web链接参数转换
    private Map<String, Map<String, String>> webUrlParameterTransform;
    //
    private Map<String, Map<String, String>> totalActionMap;
    // 此处采用 LinkedList作为容器，增删速度快
    private LinkedList<Activity> activityLinkedList;
    //    全局上下文
    public static Context mAppContext;
//    是否已初始化TuSdk
    private boolean isInitTuSdk;
    private Map<String, Object> ossMap;

    public TinkerBaseApplicationLike(Application application, int tinkerFlags,
                                     boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                                     long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        Beta.installTinker(this);
        //should set before tinker is installed
        TinkerManager.setUpgradeRetryEnable(true);

        //optional set logIml, or you can use default debug log

        //installTinker after load multiDex
        //or you can put com.tencent.tinker.** to main dex
        TinkerManager.installTinker(this);
        Tinker tinker = Tinker.with(getApplication());
        fixTimeOut();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    @Override
    public void onCreate() {
        // 调试时，将第三个参数改为true
//        腾讯hotfix
//        已包含bugly 初始化
        mAppContext = getApplication().getApplicationContext();
        setTotalChanel();
        activityLinkedList = new LinkedList<>();
        getApplication().registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activityLinkedList.add(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                activityLinkedList.remove(activity);
            }
        });

        if (isDebugTag) {
            // 补丁回调接口
            Beta.betaPatchListener = new BetaPatchListener() {
                @Override
                public void onPatchReceived(String patchFile) {
                    showToast(mAppContext, "补丁下载地址" + patchFile);
                }

                @Override
                public void onDownloadReceived(long savedLength, long totalLength) {
                    showToast(mAppContext,
                            String.format(Locale.getDefault(), "%s %d%%",
                                    Beta.strNotificationDownloading,
                                    (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)));
                }

                @Override
                public void onDownloadSuccess(String msg) {
                    showToast(mAppContext, "补丁下载成功");
                }

                @Override
                public void onDownloadFailure(String msg) {
                    showToast(mAppContext, "补丁下载失败");

                }

                @Override
                public void onApplySuccess(String msg) {
                    showToast(mAppContext, "补丁应用成功");
                }

                @Override
                public void onApplyFailure(String msg) {
                    showToast(mAppContext, "补丁应用失败");
                }

                @Override
                public void onPatchRollback() {

                }
            };
            // 设置开发设备，默认为false，上传补丁如果下发范围指定为“开发设备”，需要调用此接口来标识开发设备
            Bugly.setIsDevelopmentDevice(mAppContext, true);
        }
        super.onCreate();
//        initBaiCHotFix();
        if (LeakCanary.isInAnalyzerProcess(getApplication())) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(getApplication());
        initAutoSizeScreen();
        //        LinkedMe 深度链接
        initLinkMe();
        // 初始化xUtils
        x.Ext.init(getApplication());
        if (isAppMainProcess()) {
            if (isDebugTag) {
                SharedPreferences sharedPreferences = mAppContext.getSharedPreferences("selectedServer", MODE_PRIVATE);
                int selectServer = sharedPreferences.getInt("selectServer", 0);
                new Url(mAppContext, selectServer);
            }
            initLoadSir();
            //        阿里百川 在异步初始化
            initNewAliBaiC();
            //      jPush 初始化
            JPushInterface.setDebugMode(isDebugTag);    // 设置开启日志,发布时请关闭日志
            JPushInterface.init(mAppContext);
//            必须在主线程调用
            QyServiceUtils.getQyInstance().initQyService(getApplication().getApplicationContext());
            createExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences sp = mAppContext.getSharedPreferences("delOldVersion", MODE_PRIVATE);
                    boolean isDelOldVersionCache = sp.getBoolean("delOldVersionCache", true);
                    if (isDelOldVersionCache) {
                        FileCacheUtils.cleanApplicationData(mAppContext);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putBoolean("delOldVersionCache", false);
                        edit.apply();
                    }
                    //shareSDK
                    MobSDK.init(mAppContext, MobAPPKEY, MobAPPSECRET);

                    //      友盟初始化
                    youMengInit();
                    //        oss初始化
                    initOSS();
                    try {
                        StatService.startStatService(mAppContext, analyzeKey, "com.tencent.stat.common.StatConstants.VERSION");
                    } catch (MtaSDkException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 初始化屏幕适配
     */
    private void initAutoSizeScreen() {
        //AndroidAutoSize 默认开启对 dp 的支持, 调用 UnitsManager.setSupportDP(false); 可以关闭对 dp 的支持
        //主单位 dp 和 副单位可以同时开启的原因是, 对于旧项目中已经使用了 dp 进行布局的页面的兼容
        //让开发者的旧项目可以渐进式的从 dp 切换到副单位, 即新页面用副单位进行布局, 然后抽时间逐渐的将旧页面的布局单位从 dp 改为副单位
        //最后将 dp 全部改为副单位后, 再使用 UnitsManager.setSupportDP(false); 将 dp 的支持关闭, 彻底隔离修改 density 所造成的不良影响
        //如果项目完全使用副单位, 则可以直接以像素为单位填写 AndroidManifest 中需要填写的设计图尺寸, 不需再把像素转化为 dp
        AutoSizeConfig.getInstance().getUnitsManager()
                .setSupportDP(false)
                //AndroidAutoSize 默认开启对 sp 的支持, 调用 UnitsManager.setSupportSP(false); 可以关闭对 sp 的支持
                //如果关闭对 sp 的支持, 在布局时就应该使用副单位填写字体的尺寸
                //如果开启 sp, 对其他三方库控件影响不大, 也可以不关闭对 sp 的支持, 这里我就继续开启 sp, 请自行斟酌自己的项目是否需要关闭对 sp 的支持
                .setSupportSP(false)

                //AndroidAutoSize 默认不支持副单位, 调用 UnitsManager#setSupportSubunits() 可选择一个自己心仪的副单位, 并开启对副单位的支持
                //只能在 pt、in、mm 这三个冷门单位中选择一个作为副单位, 三个单位的适配效果其实都是一样的, 您觉的哪个单位看起顺眼就用哪个
                //您选择什么单位就在 layout 文件中用什么单位进行布局, 我选择用 mm 为单位进行布局, 因为 mm 翻译为中文是妹妹的意思
                //如果大家生活中没有妹妹, 那我们就让项目中最不缺的就是妹妹!
                .setSupportSubunits(Subunits.MM);
    }

    /**
     * 初始化 加载页面
     */
    private void initLoadSir() {
        LoadSir.beginBuilder()
                .addCallback(new NetErrorCallback())//网络错误
                .addCallback(new NetLoadCallback())//加载中
                .addCallback(new NetLoadTranslucenceCallback())//加载中 半透明
                .addCallback(new NetEmptyCallback())//空值
                .setDefaultCallback(NetLoadCallback.class)//设置默认状态页
                .commit();
    }

    private void initLinkMe() {
        if (isDebugTag) {
            //设置debug模式下打印LinkedME日志
            LinkedME.getInstance(mAppContext).setDebug();
        } else {
            LinkedME.getInstance(mAppContext);
        }
        // 设置是否开启自动跳转指定页面，默认为true
        // 若在此处设置为false，请务必在配置Uri scheme的Activity页面的onCreate()方法中，
        // 重新设置为true，否则将禁止开启自动跳转指定页面功能
        // 示例：
        // @Override
        // public class MainActivity extends AppCompatActivity {
        // ...
        // @Override
        // protected void onCreate(Bundle savedInstanceState) {
        //    super.onCreate(savedInstanceState);
        //    setContentView(R.layout.main);
        //    LinkedME.getInstance().setImmediate(true);
        //   }
        // ...
        //  }
        LinkedME.getInstance().setImmediate(true);
    }

    /**
     * 配置统计渠道
     */
    private void setTotalChanel() {
        String channel = ChannelReaderUtil.getChannel(mAppContext);
        if (!TextUtils.isEmpty(channel)) {
            CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(mAppContext);
            strategy.setAppChannel(channel);  //设置渠道
            strategy.setAppVersion(getVersionName());      //App的版本
            strategy.setAppPackageName(getStrings(mAppContext.getPackageName()));  //App的包名
            Bugly.init(mAppContext, BUGLY_APP_ID, isDebugTag, strategy);
        } else {
            Bugly.init(mAppContext, BUGLY_APP_ID, isDebugTag);
        }
    }

    //    获取版本号
    private String getVersionName() {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = mAppContext.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(mAppContext.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void initNewAliBaiC() {
        AlibcTradeSDK.asyncInit(getApplication(), new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
                //初始化成功，设置相关的全局配置参数
            }

            @Override
            public void onFailure(int code, String msg) {
                //初始化失败，可以根据code和msg判断失败原因，详情参见错误说明
            }
        });
    }

    private void getScreenInfo() {
//        获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        WindowManager mWindowManager = (WindowManager) mAppContext.getSystemService(Context.WINDOW_SERVICE);
        if (mWindowManager == null) throw new AssertionError();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        density = dm.density;
    }

    public int getScreenWidth() {
        if (screenWidth == 0) {
            getScreenInfo();
        }
        return screenWidth;
    }

    public int getScreenHeight() {
        if (screenHeight == 0) {
            getScreenInfo();
        }
        return screenHeight;
    }

    public float getDensity() {
        if (density == 0) {
            getScreenInfo();
        }
        return density;
    }


    private void youMengInit() {
        //SDK 初始化
        UMConfigure.init(mAppContext, "57db8f1fe0f55a7ac0004684"
                , getStrings(ChannelReaderUtil.getChannel(mAppContext)), UMConfigure.DEVICE_TYPE_PHONE, null);
        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(isDebugTag);

        UMShareAPI umShareAPI = null;
        try {
            umShareAPI = UMShareAPI.get(mAppContext);
            if (umShareAPI != null) {
                //微信 appid appsecret
                PlatformConfig.setWeixin(WeChatPayConstants.APP_ID, "cf7907b157cae36cfa0310fad61db22b");
                //新浪微博 appkey appsecret
                PlatformConfig.setSinaWeibo("2214087291", "b7474eb86cb90eabcd838849d5e8f167", "http://sns.whalecloud.com");
                // QQ和Qzone appid appkey
                PlatformConfig.setQQZone("1105138467", "I2DGw1xXPl4HCUFf");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Map<String, String>> getTotalActionMap() {
        if (totalActionMap == null) {
            initTotalAction();
        }
        return totalActionMap;
    }

    private void initOSS() {
        ossMap = new HashMap<>();
        // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的访问控制章节
        SharedPreferences preferences = mAppContext.getSharedPreferences("ossConfig", MODE_PRIVATE);
        String endpoint = new String(Base64.decode(preferences.getString("endpoint", "b3NzLWNuLWJlaWppbmcuYWxpeXVuY3MuY29t"), Base64.DEFAULT));
        String accessKeyId = new String(Base64.decode(preferences.getString("accessKeyId", "TFRBSVd3d2FkcjdidGJpMg=="), Base64.DEFAULT));
        String accessKeySecret = new String(Base64.decode(preferences.getString("accessKeySecret", "UnVVTWh4ZHEwejJucnNLZkRvemY3MmU0R1ZnNWFE"), Base64.DEFAULT));
        BUCKET_NAME = new String(Base64.decode(preferences.getString("bucketName", "ZG9tb2xpZmVz"), Base64.DEFAULT));
        OSS_URL = new String(Base64.decode(preferences.getString("url", "aHR0cDovL2ltYWdlLmRvbW9saWZlLmNu"), Base64.DEFAULT)) + "/";
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        oss = new OSSClient(mAppContext, endpoint, credentialProvider, conf);
        ossMap.put(OSS_BUCKET_NAME,BUCKET_NAME);
        ossMap.put(OSS_URL,OSS_URL);
        ossMap.put(OSS_OBJECT,oss);
    }

    /**
     * 判断是不是UI主进程，因为有些东西只能在UI主进程初始化
     */
    public boolean isAppMainProcess() {
        try {
            int pid = android.os.Process.myPid();
            String process = getAppNameByPID(mAppContext, pid);
            if (TextUtils.isEmpty(process)) {
                return true;
            } else if (mAppContext.getPackageName().equalsIgnoreCase(process)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * 根据Pid得到进程名
     */
    public static String getAppNameByPID(Context context, int pid) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return null;
        }
        for (android.app.ActivityManager.RunningAppProcessInfo processInfo : runningAppProcesses) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return "";
    }

    /**
     * @throws IOException
     */
    private void initTotalAction() {
        try {
            AssetManager asset = getApplication().getApplicationContext().getAssets();
            InputStream totalStream = asset.open("totalAction.txt");
            totalActionMap = new HashMap<>();
            String fileTotal = FileStreamUtils.InputStreamTOString(totalStream);
            for (String totalString : fileTotal.split("\n")) {
                String[] split = totalString.split(",");
                if (split.length == 3) {
                    Map<String, String> totalIdName = new HashMap<>();
                    totalIdName.put(TOTAL_NAME, split[1]);
                    totalIdName.put(TOTAL_ID, split[2]);
                    totalActionMap.put(split[0], totalIdName);
                }
            }
        } catch (Exception e) {
            android.util.Log.d(getClass().getSimpleName(), "initTotalAction: ");
            e.printStackTrace();
        }
    }


    public IWXAPI getApi() {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(mAppContext, WeChatPayConstants.APP_ID, false);
            // 将该app注册到微信
            api.registerApp(WeChatPayConstants.APP_ID);
        }
        return api;
    }

    private String getFilePath() {
        String Img_PATH = mAppContext.getFilesDir() + "/ImgArticle";
        File ImgFile = new File(Img_PATH);
        if (!ImgFile.exists()) {
            LogUtils.d("创建文件夹");
            ImgFile.mkdirs();
        }
        return ImgFile.getAbsolutePath();
    }

    /**
     * 初始化web链接转换本地链接
     */
    private void initWebUrlTransformLocation() {
        webUrlTransform = new HashMap<>();
        webUrlParameterTransform = new HashMap<>();
        //        自营商品商品详情
        webUrlTransform.put("proprietary.html", "app://ShopScrollDetailsActivity");
        webUrlParameterTransform.put("proprietary.html", getWebUrlParameter("id", "productId", null));
        //        pc自营商品商品详情
        webUrlTransform.put("ProductDetails.html", "app://ShopScrollDetailsActivity");
        webUrlParameterTransform.put("ProductDetails.html", getWebUrlParameter("id", "productId", null));
        //        淘宝商品详情
        webUrlTransform.put("taoBaoGoods.html", "app://ShopTimeScrollDetailsActivity");
        webUrlParameterTransform.put("taoBaoGoods.html", getWebUrlParameter("id", "productId", null));
        //        pc淘宝商品详情
        webUrlTransform.put("LimitedBlurb.html", "app://ShopTimeScrollDetailsActivity");
        webUrlParameterTransform.put("LimitedBlurb.html", getWebUrlParameter("id", "productId", null));
        //        pc积分商品详情
        webUrlTransform.put("IntegralGoods.html", "app://IntegralScrollDetailsActivity");
        webUrlParameterTransform.put("IntegralGoods.html", getWebUrlParameter("id", "productId", null));
        //        文章详情
        webUrlTransform.put("study_detail.html", "app://ArticleOfficialActivity");
        webUrlParameterTransform.put("study_detail.html", getWebUrlParameter("id", "ArtId", null));
        //        帖子详情
        webUrlTransform.put("find_detail.html", "app://ArticleDetailsImgActivity");
        webUrlParameterTransform.put("find_detail.html", getWebUrlParameter("id", "ArtId", null));
        //        品牌团详情
        webUrlTransform.put("brand.html", "app://TimeBrandDetailsActivity");
        webUrlParameterTransform.put("brand.html", getWebUrlParameter("id", "brandId", null));
        //        必买清单
        webUrlTransform.put("must_buy.html", "app://QualityShopBuyListActivity");
        //        福利社列表
        webUrlTransform.put("welfare.html", "app://DuomoLifeActivity");
        //        福利社详情
        webUrlTransform.put("topic.html", "app://DoMoLifeWelfareDetailsActivity");
        webUrlParameterTransform.put("topic.html", getWebUrlParameter("id", "welfareId", null));
        //        每周优选
        webUrlTransform.put("weekly_optimization.html", "app://QualityWeekOptimizedActivity");
        //        必买清单
        webUrlTransform.put("must_buy.html", "app://QualityShopBuyListActivity");
        //        拼团详情页
        webUrlTransform.put("groupDetail.html", "app://QualityGroupShopDetailActivity");
        webUrlParameterTransform.put("groupDetail.html", getWebUrlParameter("id", "gpInfoId", null));
        //        分享拼团详情页
        webUrlTransform.put("groupShare.html", "app://QualityGroupShopDetailActivity?gpInfoId=&gpRecordId=");
        webUrlParameterTransform.put("groupShare.html", getWebUrlParameter("id", "gpInfoId",
                getWebUrlParameter("record", "gpRecordId", null)));
        //        拼团列表
        webUrlTransform.put("group.html", "app://QualityGroupShopActivity");
        //        抽奖
        webUrlTransform.put("lottery.html", "app://DoMoLifeLotteryActivity");
        //        热销单品
        webUrlTransform.put("hot_products.html", "app://QualityTypeHotSaleProActivity");
        //        新品发布
        webUrlTransform.put("new_arrival.html", "app://QualityNewProActivity");
        //        种草营列表
        webUrlTransform.put("study.html", "app://QualityDMLLifeSearchActivity");
        //        多么定制
        webUrlTransform.put("optimize.html", "app://DmlOptimizedSelActivity");
        //        多么定制详情页
        webUrlTransform.put("optimize_detail.html", "app://DmlOptimizedSelDetailActivity");
        webUrlParameterTransform.put("optimize_detail.html", getWebUrlParameter("id", "optimizedId", null));
        //        新人专区
        webUrlTransform.put("new_exclusive.html", "app://QualityNewUserActivity");
        //        自定义专区
        webUrlTransform.put("CustomZone.html", "app://QualityCustomTopicActivity");
        webUrlParameterTransform.put("CustomZone.html", getWebUrlParameter("id", "productType", null));
        //        订单详情
        webUrlTransform.put("order.html", "app://DirectExchangeDetailsActivity");
        webUrlParameterTransform.put("order.html", getWebUrlParameter("noid", "orderNo", null));
        //        活动专区
        webUrlTransform.put("activitySpecial.html", "app://QualityTypeProductActivity");
        webUrlParameterTransform.put("activitySpecial.html", getWebUrlParameter("id", "activityCode", null));
    }

    /**
     * @param webParameter      web参数
     * @param locationParameter 本地参数
     * @return
     */
    private static Map<String, String> getWebUrlParameter(String webParameter, String locationParameter, Map<String, String> parameterMap) {
        if (parameterMap == null) {
            parameterMap = new HashMap<>();
        }
        parameterMap.put(webParameter, locationParameter);
        return parameterMap;
    }

    public Map<String, Object> getOSSDataMap() {
        if(ossMap==null){
            initOSS();
        }
        return ossMap;
    }

    public String getOSSDataUrl() {
        if(ossMap==null||ossMap.get(OSS_URL)==null){
            initOSS();
        }
        return (String) ossMap.get(OSS_URL);
    }

    public Context getContext() {
        return mAppContext;
    }

    /**
     * 修复AssetManager.finalize() timed out
     */
    private void fixTimeOut() {
        try {
            Class clazz = Class.forName("java.lang.Daemons$FinalizerWatchdogDaemon");
            Method method = clazz.getSuperclass().getDeclaredMethod("stop");
            method.setAccessible(true);

            Field field = clazz.getDeclaredField("INSTANCE");
            field.setAccessible(true);

            method.invoke(field.get(null));

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void exitApp() {
        // 逐个退出Activity
        for (Activity activity : activityLinkedList) {
            activity.finish();
        }
    }

    /**
     * 栈内是否存在当前activity
     * 确保推送进来，finish后返回主页
     *
     * @param currentActivityName 当前类名
     * @return
     */
    public boolean existActivity(String currentActivityName) {
        if (TextUtils.isEmpty(currentActivityName)) {
            return false;
        }
        for (Activity activity : activityLinkedList) {
            if (activity.getClass().getName().equals(currentActivityName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取 webUrlTransform
     *
     * @return
     */
    public Map<String, String> getWebUrlTransform() {
        if (webUrlTransform == null) {
            //          web自动识别
            initWebUrlTransformLocation();
        }
        return webUrlTransform;
    }

    /**
     * webUrlParameterTransform
     *
     * @return
     */
    public Map<String, Map<String, String>> getWebUrlParameterTransform() {
        if (webUrlParameterTransform == null) {
            //          web自动识别
            initWebUrlTransformLocation();
        }
        return webUrlParameterTransform;
    }

    public void initTuSdk(){
        if(!isInitTuSdk){
            try {
//                不能放到子线程初始化
                TuSdk.enableDebugLog(isDebugTag);
                TuSdk.init(getApplication().getApplicationContext(), "08b501fdf166d42d-02-5dvwp1");
                isInitTuSdk = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
