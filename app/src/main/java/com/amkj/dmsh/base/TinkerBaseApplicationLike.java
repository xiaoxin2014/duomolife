package com.amkj.dmsh.base;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
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
import com.amkj.dmsh.address.bean.AddressInfo;
import com.amkj.dmsh.address.bean.AddressInfo.AddressBean;
import com.amkj.dmsh.address.bean.CityModel;
import com.amkj.dmsh.address.bean.DistrictModel;
import com.amkj.dmsh.address.bean.ProvinceModel;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.netloadpage.NetEmptyCallback;
import com.amkj.dmsh.netloadpage.NetErrorCallback;
import com.amkj.dmsh.netloadpage.NetLoadCallback;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.release.util.LogUtils;
import com.amkj.dmsh.utils.FileCacheUtils;
import com.amkj.dmsh.utils.FileStreamUtils;
import com.amkj.dmsh.utils.Log;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.kingja.loadsir.core.LoadSir;
import com.leon.channel.helper.ChannelReaderUtil;
import com.microquation.linkedme.android.LinkedME;
import com.mob.MobSDK;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatService;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.lasque.tusdk.core.TuSdk;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
public class TinkerBaseApplicationLike extends DefaultApplicationLike {
    public OSS oss;
    private final String MobAPPKEY = "1693fa0f7b0a0";
    private final String MobAPPSECRET = "435ac0137e0179dafee0139a85f6ed92";
    private String BUGLY_APP_ID = "385d38aeeb";
    public OSSCredentialProvider credentialProvider;
    public static String BUCKET_NAME = "domolifes";
    public static String OSS_URL;
    public ClientConfiguration conf;
    /**
     * 所有省
     */
    public ProvinceModel[] mProvinceData;
    /**
     * key - 省 value - 市
     */
    public Map<Integer, CityModel[]> mCitiesDataMap = new HashMap<>();

    /**
     * key - 市 values - 区
     */
    public Map<Integer, DistrictModel[]> mDistrictDataMap = new HashMap<>();

    /**
     * key - 区 values - 邮编
     */
    public Map<Integer, String> mZipCodeDataMap = new HashMap<>();
    /**
     * 当前省的名称
     */
    public int mCurrentProvinceId;
    /**
     * 当前市的名称
     */
    public int mCurrentCityId;

    /**
     * 当前区的名称
     */
    public int mCurrentDistrictId;
    /**
     * 当前区的邮政编码
     */
    public String mCurrentZipCode = "";
    public IWXAPI api;
    public static int screenWidth;
    private int screenHeight;
    private Thread thread;
    private String analyzeKey = "A1M12V8YKBTI";
    public static final String INIT_TAG = "初始化";            //安全加密和安全签名使用的秘钥在jpg中对应的key
    private float density;
    //    web链接替换
    public static Map<String, String> webUrlTransform;
    //    web链接参数转换
    public static Map<String, Map<String, String>> webUrlParameterTransform;
    //
    public static Map<String, Map<String, String>> totalActionMap;
    //    全局上下文
    public static Context mAppContext;

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
        SharedPreferences sp = mAppContext.getSharedPreferences("delOldVersion", MODE_PRIVATE);
        boolean isDelOldVersionCache = sp.getBoolean("delOldVersionCache", true);
        if (isDelOldVersionCache) {
            FileCacheUtils.cleanApplicationData(mAppContext);
            SharedPreferences.Editor edit = sp.edit();
            edit.putBoolean("delOldVersionCache", false);
            edit.apply();
        }
        super.onCreate();
//        initBaiCHotFix();
        //        阿里百川初始化
        if (isDebugTag) {
            SharedPreferences sharedPreferences = mAppContext.getSharedPreferences("selectedServer", MODE_PRIVATE);
            int selectServer = sharedPreferences.getInt("selectServer", 0);
            new Url(mAppContext, selectServer);
        }
        initNewAliBaiC();
        if (isAppMainProcess()) {
            //        七鱼客服初始化
            initQYService();
            //      友盟初始化
            youMengInit();
            try {
                TuSdk.enableDebugLog(isDebugTag);
                TuSdk.init(mAppContext, "08b501fdf166d42d-02-5dvwp1");
            } catch (Exception e) {
                e.printStackTrace();
            }
            createExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    initProvinceData();
                }
            });
            createExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    initTotalAction();
                }
            });
        }
//        oss初始化
        initOSS();
        // 初始化xUtils
        x.Ext.init(getApplication());
        //shareSDK
        MobSDK.init(mAppContext, MobAPPKEY, MobAPPSECRET);

//        腾讯移动分析初始化
        // 第三个参数必须为：com.tencent.stat.common.StatConstants.VERSION
        try {
            StatService.startStatService(mAppContext, analyzeKey, "com.tencent.stat.common.StatConstants.VERSION");
        } catch (MtaSDkException e) {
            e.printStackTrace();
        }
//      jPush 初始化
        JPushInterface.setDebugMode(isDebugTag);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(mAppContext);
//      腾讯升级策略
// 如果通过“AndroidManifest.xml”来配置APP信息，初始化方法如下
// CrashReport.initCrashReport(context, strategy);
        getScreenInfo();
//        LinkedMe 深度链接
        initLinkMe();
        initWebUrlTransformLocation();
        initLoadSir();
        initAutoSizeScreen();
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
                .addCallback(new NetEmptyCallback())//空值
                .setDefaultCallback(NetLoadCallback.class)//设置默认状态页
                .commit();
    }

    private void initQYService() {
        QyServiceUtils qyInstance = QyServiceUtils.getQyInstance();
        qyInstance.initQyService(getContext());
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
//        MemberSDK.turnOnDebug();
        AlibcTradeSDK.asyncInit(getApplication(), new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
                //初始化成功，设置相关的全局配置参数
                Log.d(INIT_TAG, "alibc_onSuccess: ");
                // ...
            }

            @Override
            public void onFailure(int code, String msg) {
                //初始化失败，可以根据code和msg判断失败原因，详情参见错误说明
                Log.d(INIT_TAG, "alibc_onFailure: " + "errorCode:" + code + msg);
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
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public float getDensity() {
        return density;
    }

    private void youMengInit() {
        //SDK 初始化
        UMConfigure.init(mAppContext, "57db8f1fe0f55a7ac0004684", getStrings(ChannelReaderUtil.getChannel(mAppContext)), UMConfigure.DEVICE_TYPE_PHONE, null);
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
        return totalActionMap;
    }

    private void initOSS() {
        // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的访问控制章节
        SharedPreferences preferences = mAppContext.getSharedPreferences("ossConfig", MODE_PRIVATE);
        String endpoint = new String(Base64.decode(preferences.getString("endpoint", "b3NzLWNuLWJlaWppbmcuYWxpeXVuY3MuY29t"), Base64.DEFAULT));
        BUCKET_NAME = new String(Base64.decode(preferences.getString("bucketName", "ZG9tb2xpZmVz"), Base64.DEFAULT));
        String accessKeyId = new String(Base64.decode(preferences.getString("accessKeyId", "TFRBSVd3d2FkcjdidGJpMg=="), Base64.DEFAULT));
        String accessKeySecret = new String(Base64.decode(preferences.getString("accessKeySecret", "UnVVTWh4ZHEwejJucnNLZkRvemY3MmU0R1ZnNWFE"), Base64.DEFAULT));
        OSS_URL = new String(Base64.decode(preferences.getString("url", "aHR0cDovL2ltYWdlLmRvbW9saWZlLmNu"), Base64.DEFAULT)) + "/";
        credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
        conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        oss = new OSSClient(mAppContext, endpoint, credentialProvider, conf);
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


    //    地址初始化
    protected void initProvinceData() {
        String adsPath = mAppContext.getFilesDir().getAbsolutePath() + "/adr_s/asr_s.txt";
        if (new File(adsPath).exists()) {
            try {
                Gson gson = new Gson();
                AddressInfo addressInfo = gson.fromJson(FileStreamUtils.readFile2String(adsPath), AddressInfo.class);
                setAddressData(addressInfo);
            } catch (JsonSyntaxException e) {
                try {
                    getAssetAdsData();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        } else {
            try {
                getAssetAdsData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @throws IOException
     */
    private void initTotalAction() {
        try {
            AssetManager asset = mAppContext.getAssets();
            InputStream totalStream = asset.open("totalAction.txt");
            totalActionMap = new HashMap<>();
            String fileTotal = FileStreamUtils.InputStreamTOString(totalStream);
            for (String totalString : fileTotal.split("\r\n")) {
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

    /**
     * @throws IOException
     */
    private void getAssetAdsData() throws IOException {
        AssetManager asset = mAppContext.getAssets();
        InputStream input = asset.open("area.json");
        Gson gson = new Gson();
        AddressInfo addressInfo = gson.fromJson(new InputStreamReader(input), AddressInfo.class);
        input.close();
        setAddressData(addressInfo);
    }

    private void setAddressData(AddressInfo addressInfo) {
        try {
            Comparator<AddressBean> comparator = (lhs, rhs) -> {
                if (lhs.getId() < rhs.getId()) {
                    return -1;
                }
                return 1;
            };
            List<AddressBean> addressList = addressInfo.getArea();
            Collections.sort(addressList, comparator);
//            全部省市县的集合
            List<AddressBean> provinceAddressList = new ArrayList<>();
            List<AddressBean> cityAddressList = new ArrayList<>();
            List<AddressBean> districtAddressList = new ArrayList<>();
            for (AddressBean address : addressList) {
                if (address.getType() == 2 && address.getParent_id() == 1) {
//                获取省
                    provinceAddressList.add(address);
//                    省级排序
//                    Collections.sort(provinceTestList, comparator);
                } else if (address.getType() == 3) {
//                    获取市
                    cityAddressList.add(address);
//                    市级排序
//                    Collections.sort(cityTestList, comparator);
                } else if (address.getType() == 4) {
//                    获取县
                    districtAddressList.add(address);
//                    县级排序
//                    Collections.sort(districtTestList, comparator);
                }
            }
            mProvinceData = new ProvinceModel[provinceAddressList.size()];
            ProvinceModel provinceModel;
            CityModel cityModel;
            DistrictModel districtModel;
//            省集合 包含每个市集合
            List<ProvinceModel> provinceModelList = new ArrayList<>();
//            市集合 包含每个县
            List<CityModel> cityModelList;
//          县
            List<DistrictModel> districtModelList;
            for (int i = 0; i < provinceAddressList.size(); i++) {
                cityModelList = new ArrayList<>();
                provinceModel = new ProvinceModel();
//                获取当前省的市
                for (int j = 0; j < cityAddressList.size(); j++) {
                    cityModel = new CityModel();
                    // 遍历省下面的所有市的数据
                    if (cityAddressList.get(j).getParent_id() == provinceAddressList.get(i).getId()) {
//                        设置cityModel数据
                        cityModel.setId(cityAddressList.get(j).getId());
                        cityModel.setName(cityAddressList.get(j).getName());
                        cityModel.setZip(cityAddressList.get(j).getZip());
                        cityModelList.add(cityModel);
                    }
                }
                provinceModel.setName(provinceAddressList.get(i).getName());
                provinceModel.setId(provinceAddressList.get(i).getId());
                provinceModel.setCityList(cityModelList);
                provinceModelList.add(provinceModel);
// 遍历所有省的数据
                mProvinceData[i] = provinceModelList.get(i);
                CityModel[] cities = new CityModel[cityModelList.size()];
                for (int j = 0; j < cityModelList.size(); j++) {
                    districtModelList = new ArrayList<>();
                    cities[j] = cityModelList.get(j);
//                    获取市下面的县集合
                    for (int k = 0; k < districtAddressList.size(); k++) {
                        districtModel = new DistrictModel();
                        if (districtAddressList.get(k).getParent_id() == cityModelList.get(j).getId()) {
//                        设置districtModel数据
                            districtModel.setName(districtAddressList.get(k).getName());
                            districtModel.setId(districtAddressList.get(k).getId());
                            districtModel.setZip(districtAddressList.get(k).getZip());
                            districtModelList.add(districtModel);
                        }
                    }
                    cityModelList.get(j).setDistrictList(districtModelList);
                    DistrictModel[] districtArray = new DistrictModel[districtModelList.size()];
                    for (int k = 0; k < districtModelList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        districtModel = districtModelList.get(k);
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipCodeDataMap.put(districtModelList.get(k).getId(), districtModelList.get(k).getZip());
                        districtArray[k] = districtModel;
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDataMap.put(cities[j].getId(), districtArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitiesDataMap.put(provinceModelList.get(i).getId(), cities);
            }
            // 获取解析出来的数据
            //*/ 初始化默认选中的省、市、区
            if (!provinceModelList.isEmpty()) {
                mCurrentProvinceId = provinceModelList.get(0).getId();
                List<CityModel> cityList = provinceModelList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityId = cityList.get(0).getId();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictId = districtList.get(0).getId();
                    mCurrentZipCode = districtList.get(0).getZip();
                }
            }
        } catch (Throwable e) {
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

    //      所有省
    public ProvinceModel[] getAllProvince() {
        return mProvinceData;
    }

    //    当前市的名称
    public int getCurrentCity() {
        return mCurrentCityId;
    }

    //    当前县的名称
    public int getCurrentDistrict() {
        return mCurrentDistrictId;
    }

    //    当前邮政编码
    public String getCurrentZipCode() {
        return mCurrentZipCode;
    }

    //    邮政Map
    public Map<Integer, String> getZipCodeDataMap() {
        return mZipCodeDataMap;
    }

    //    市-县
    public Map<Integer, DistrictModel[]> getCityDistrict() {
        return mDistrictDataMap;
    }

    //   省 -市
    public Map<Integer, CityModel[]> getCitiesDataMap() {
        return mCitiesDataMap;
    }

    //   当前省名字
    public int getCurrentProvince() {
        return mCurrentProvinceId;
    }

    public OSS getOSS() {
        return oss;
    }

    public Context getContext() {
        return mAppContext;
    }
}
