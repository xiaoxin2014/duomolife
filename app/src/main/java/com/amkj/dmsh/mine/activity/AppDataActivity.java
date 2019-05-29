package com.amkj.dmsh.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.amkj.dmsh.R;
import com.amkj.dmsh.address.activity.SelectedAddressActivity;
import com.amkj.dmsh.address.bean.AddressInfoEntity;
import com.amkj.dmsh.address.bean.AddressInfoEntity.AddressInfoBean;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.rxeasyhttp.EasyHttp;
import com.amkj.dmsh.utils.FileCacheUtils;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.google.gson.Gson;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.NEW_USER_DIALOG;
import static com.amkj.dmsh.constant.ConstantMethod.createExecutor;
import static com.amkj.dmsh.constant.ConstantMethod.getMarketApp;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.DELIVERY_ADDRESS;
import static com.amkj.dmsh.utils.FileCacheUtils.getFolderSize;


/**
 * Created by atd48 on 2016/8/18.
 * app资料配置
 */
public class AppDataActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    //    展示默认地址
    @BindView(R.id.tv_mine_setting_address)
    TextView tv_mine_setting_address;
    //    账号退出
    @BindView(R.id.tv_set_account_exit)
    TextView tv_set_account_exit;
    //    缓存统计
    @BindView(R.id.tv_set_clear_cache)
    TextView tv_set_clear_cache;
    private AddressInfoBean addressInfoBean;
    private String Img_PATH;
    private List<File> files = new ArrayList<>();
    private boolean isPause;
    private AlertDialogHelper alertDialogHelper;

    @Override
    protected int getContentView() {
        return R.layout.activity_persional_data_setting;
    }

    @Override
    protected void initViews() {
        tl_normal_bar.setSelected(true);
        tv_header_titleAll.setText("设置");
        header_shared.setVisibility(View.INVISIBLE);
        setLayoutUI();
        getCacheStatic();
    }

    /**
     * 根据是否登录展示UI
     */
    private void setLayoutUI() {
        if (userId < 1) {
            tv_set_account_exit.setVisibility(View.GONE);
            tv_mine_setting_address.setText("");
        } else {
            tv_set_account_exit.setVisibility(View.VISIBLE);
            loadData();
        }
    }

    @Override
    protected void loadData() {
//        获取默认地址
        getDefaultAddress();
    }

    private void getDefaultAddress() {
        if (userId < 1) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, DELIVERY_ADDRESS, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                AddressInfoEntity addressInfoEntity = gson.fromJson(result, AddressInfoEntity.class);
                if (addressInfoEntity != null) {
                    if (addressInfoEntity.getCode().equals(SUCCESS_CODE)) {
                        addressInfoBean = addressInfoEntity.getAddressInfoBean();
                        setAddressData(addressInfoBean);
                    } else if (addressInfoEntity.getCode().equals(EMPTY_CODE)) {
                        tv_mine_setting_address.setText("");
                    } else {
                        tv_mine_setting_address.setText("");
                        showToast(AppDataActivity.this, addressInfoEntity.getMsg());
                    }
                }
            }
        });
    }

    private void setAddressData(AddressInfoBean addressInfoBean) {
        tv_mine_setting_address.setText((getStrings(addressInfoBean.getConsignee()) + " "
                + getStrings(addressInfoBean.getAddress_com())
                + getStrings(addressInfoBean.getAddress())));
    }

    //    统计缓存
    private void getCacheStatic() {
        Context applicationContext = this.getApplicationContext();
        Img_PATH = getFilesDir().getAbsolutePath() + "/ImgArticle";
        files.add(new File(Img_PATH));
        files.add(applicationContext.getCacheDir());
        if (getGlideCacheFile(applicationContext) != null) {
            files.add(getGlideCacheFile(applicationContext));
        }
//        查看文件夹的缓存大小
        try {
            tv_set_clear_cache.setText(FileCacheUtils.getCacheListSize(files));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public File getGlideCacheFile(Context context) {
        try {
            return new File(context.getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //退出登录
    @OnClick(R.id.tv_set_account_exit)
    void offLine() {
        if (alertDialogHelper == null) {
            alertDialogHelper = new AlertDialogHelper(AppDataActivity.this);
            alertDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                    .setMsg("确定要退出当前账号？").setCancelText("取消").setConfirmText("确定")
                    .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
            alertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                @Override
                public void confirm() {
                    NEW_USER_DIALOG = true;
                    //调用登出接口
                    ConstantMethod.logout(getActivity());
                    showToast(AppDataActivity.this, "注销成功");
                    exitNewTaoBaoAccount();
                    //        清除账号 修改我的页面
                    finish();
                }

                @Override
                public void cancel() {

                }
            });
        }
        alertDialogHelper.show();
    }

    private void exitNewTaoBaoAccount() {
        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.logout(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i) {
                showToast(AppDataActivity.this, "退出登录成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                showToast(AppDataActivity.this, "退出登录失败 " + code + msg);
            }
        });
    }

    @Override
    protected void onPause() {
        isPause = true;
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (isPause) {
            setLayoutUI();
        }
        super.onResume();
    }

    //  个人信息
    @OnClick(R.id.tv_mine_setting_per)
    void setPersonalData(View view) {
        Intent intent = new Intent(AppDataActivity.this, PersonalDataActivity.class);
        startActivity(intent);
    }

    //  账户安全
    @OnClick(R.id.tv_mine_setting_account_safe)
    void setAccountSafe(View view) {
        Intent intent = new Intent(AppDataActivity.this, AccountSafeActivity.class);
        startActivity(intent);
    }

    //    消息推送
    @OnClick(R.id.tv_mine_setting_mes)
    void setMineMes(View view) {
        Intent intent = new Intent(AppDataActivity.this, MessagePushTypeActivity.class);
        startActivity(intent);
    }

    //    地址管理
    @OnClick(R.id.ll_personal_address)
    void setAddress(View view) {
        Intent intent = new Intent(AppDataActivity.this, SelectedAddressActivity.class);
        intent.putExtra("isMineSkip", "1");
        startActivity(intent);
    }

    //    关于多么生活
    @OnClick(R.id.tv_setting_about)
    void aboutApp(View view) {
        Intent intent = new Intent(AppDataActivity.this, AboutUsDoMoLifeActivity.class);
        startActivity(intent);
    }

    //    给个好评  跳转app商店
    @OnClick(R.id.tv_setting_good)
    void skipShop(View view) {
        getMarketApp(AppDataActivity.this, "本机应用商店暂未上线哦，小主会努力呢~");
    }

    //    意见反馈
    @OnClick(R.id.tv_setting_sug)
    void sugFeedBack(View view) {
        Intent intent = new Intent();
        intent.setClass(AppDataActivity.this, SuggestionFeedBackActivity.class);
        startActivity(intent);
    }

    //    清理缓存
    @OnClick({R.id.rel_mine_cache})
    void clearAppCache(View view) {
        Context applicationContext = this.getApplicationContext();
        try {
            if (getFolderSize(new File(Img_PATH)) > 0
                    || getFolderSize(AppDataActivity.this.getCacheDir()) > 0) {
//                点击清除-》子线程清除数据 禁用点击 手动改为已清除
                tv_set_clear_cache.setText("0.0kb");
                view.setEnabled(false);
                createExecutor().execute(() -> {
                    try {
                        if (getFolderSize(new File(Img_PATH)) > 0) {
                            FileCacheUtils.deleteFolderFile(Img_PATH, false);
                        }
                        if (getFolderSize(AppDataActivity.this.getCacheDir()) > 0) {
                            FileCacheUtils.cleanInternalCache(applicationContext);
                        }
//                       七鱼客服清除缓存
                        QyServiceUtils.getQyInstance().clearQyCache();
//                        网络缓存
                        EasyHttp.clearCache();
                        PictureFileUtils.deleteCacheDirFile(applicationContext);
                        GlideImageLoaderUtil.clearMemoryByContext(applicationContext);
                        SharedPreferences sharedPreferences = getSharedPreferences("launchAD", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.clear().apply();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    返回
    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialogHelper != null && alertDialogHelper.getAlertDialog().isShowing()) {
            alertDialogHelper.dismiss();
        }
    }
}
