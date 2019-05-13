package com.amkj.dmsh.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import com.amkj.dmsh.R;

import java.util.ArrayList;
import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;



/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/9/9
 * class description:应用市场
 */

public class MarketUtils {
    private static List<String> MarketPackages = new ArrayList<>();

    static {
//        华为应用商店
        MarketPackages.add("com.huawei.appmarket");
//        小米应用商店
        MarketPackages.add("com.xiaomi.market");
//        魅族应用商店
        MarketPackages.add("com.meizu.mstore");
//        联想应用商店
        MarketPackages.add("com.lenovo.leos.appstore");
//        OPPO应用商店
        MarketPackages.add("com.oppo.market");
//        VIVO应用商店
        MarketPackages.add("com.bbk.appstore");
//        应用宝
        MarketPackages.add("com.tencent.android.qqdownloader");
//        百度手机助手
        MarketPackages.add("com.baidu.appsearch");
//        360手机助手
        MarketPackages.add("com.qihoo.appstore");
//        豌豆荚
        MarketPackages.add("com.wandoujia.phoenix2");
//        谷歌市场
        MarketPackages.add("com.android.vending");
    }

    public static List<String> getMarketPackages() {
        return MarketPackages;
    }

    public static void setMarketPackages(List<String> marketPackages) {
        MarketPackages = marketPackages;
    }

    public static List<ActivityInfo> queryInstalledMarketInfos(Context context) {
        List<ActivityInfo> infos = new ArrayList<>();
        if (context == null) return infos;

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_MARKET);

        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        if (resolveInfos == null || infos.size() == 0) {
            return infos;
        }

        for (int i = 0; i < resolveInfos.size(); i++) {
            try {
                infos.add(resolveInfos.get(i).activityInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return infos;
    }

    /**
     * @param context
     * @return 过滤掉手机上没有安装的应用商店
     */
    public static List<ApplicationInfo> filterInstalledPkgs(Context context) {
        List<ApplicationInfo> infos = new ArrayList<>();
        if (context == null || MarketPackages == null || MarketPackages.size() == 0)
            return infos;
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPkgs = pm.getInstalledPackages(0);
        int li = installedPkgs.size();
        int lj = MarketPackages.size();
        for (int j = 0; j < lj; j++) {
            for (int i = 0; i < li; i++) {
                String installPkg = "";
                String checkPkg = MarketPackages.get(j);
                try {
                    installPkg = installedPkgs.get(i).applicationInfo.packageName;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(installPkg))
                    continue;
                if (installPkg.equals(checkPkg)) {
                    infos.add(installedPkgs.get(i).applicationInfo);
                    break;
                }

            }
        }
        return infos;
    }

    /**
     * 获取已安装应用商店的包名列表
     *
     * @param context
     * @return
     */
    public static ArrayList<String> queryInstalledMarketPkgs(Context context) {
        ArrayList<String> pkgs = new ArrayList<String>();
        if (context == null)
            return pkgs;
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.APP_MARKET");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        if (infos == null || infos.size() == 0)
            return pkgs;
        int size = infos.size();
        for (int i = 0; i < size; i++) {
            String pkgName = "";
            try {
                ActivityInfo activityInfo = infos.get(i).activityInfo;
                pkgName = activityInfo.packageName;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(pkgName))
                pkgs.add(pkgName);
        }
        return pkgs;
    }

    /**
     * 过滤出已经安装的包名集合
     *
     * @param context
     * @param pkgs    待过滤包名集合
     * @return 已安装的包名集合
     */
    public static ArrayList<String> filterInstalledPkgs(Context context, ArrayList<String> pkgs) {
        ArrayList<String> empty = new ArrayList<String>();
        if (context == null || pkgs == null || pkgs.size() == 0)
            return empty;
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPkgs = pm.getInstalledPackages(0);
        int li = installedPkgs.size();
        int lj = pkgs.size();
        for (int j = 0; j < lj; j++) {
            for (int i = 0; i < li; i++) {
                String installPkg = "";
                String checkPkg = pkgs.get(j);
                try {
                    installPkg = installedPkgs.get(i).applicationInfo.packageName;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(installPkg))
                    continue;
                if (installPkg.equals(checkPkg)) {
                    empty.add(installPkg);
                    break;
                }

            }
        }
        return empty;
    }

    /**
     * 启动到app详情界面
     *
     * @param appPkg    App的包名
     * @param marketPkg 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择
     *                  ,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public static void launchAppDetail(Context context, String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg))
                return;
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg))
                intent.setPackage(marketPkg);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            showToast(context, R.string.no_install_app_store);
            e.printStackTrace();
        }
    }
}
