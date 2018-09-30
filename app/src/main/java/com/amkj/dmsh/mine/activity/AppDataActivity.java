package com.amkj.dmsh.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.amkj.dmsh.R;
import com.amkj.dmsh.address.activity.SelectedAddressActivity;
import com.amkj.dmsh.address.bean.AddressInfoEntity;
import com.amkj.dmsh.address.bean.AddressInfoEntity.AddressInfoBean;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.release.dialogutils.AlertSettingBean;
import com.amkj.dmsh.release.dialogutils.AlertView;
import com.amkj.dmsh.release.dialogutils.OnAlertItemClickListener;
import com.amkj.dmsh.utils.FileCacheUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.google.gson.Gson;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.NEW_USER_DIALOG;
import static com.amkj.dmsh.constant.ConstantMethod.createExecutor;
import static com.amkj.dmsh.constant.ConstantMethod.getMarketApp;
import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.savePersonalInfoCache;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.utils.FileCacheUtils.getFolderSize;

;

/**
 * Created by atd48 on 2016/8/18.
 * app资料配置
 */
public class AppDataActivity extends BaseActivity implements OnAlertItemClickListener {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.ll_mine_personal)
    LinearLayout ll_mine_personal;
    //    展示默认地址
    @BindView(R.id.tv_mine_setting_address)
    TextView tv_mine_setting_address;
    //    账号退出
    @BindView(R.id.tv_set_account_exit)
    TextView tv_set_account_exit;
    //    缓存统计
    @BindView(R.id.tv_set_clear_cache)
    TextView tv_set_clear_cache;
    private int uid;
    private AlertView exitAccount;
    private AddressInfoBean addressInfoBean;
    private String Img_PATH;
    private List<File> files = new ArrayList<>();
    private boolean isPause;
    @Override
    protected int getContentView() {
        return R.layout.activity_persional_data_setting;
    }
    @Override
    protected void initViews() {
        isLoginStatus();
        tl_normal_bar.setSelected(true);
        tv_header_titleAll.setText("设置");
        header_shared.setVisibility(View.INVISIBLE);
        getCacheStatic();
    }

    @Override
    protected void loadData() {
//        获取默认地址
        getDefaultAddress();
    }

    private void getDefaultAddress() {
        isLoginStatus();
        if (uid > 0) {
            String url = Url.BASE_URL + Url.DELIVERY_ADDRESS + uid;
            XUtil.Get(url, null, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    AddressInfoEntity addressInfoEntity = gson.fromJson(result, AddressInfoEntity.class);
                    if (addressInfoEntity != null) {
                        if (addressInfoEntity.getCode().equals("01")) {
                            addressInfoBean = addressInfoEntity.getAddressInfoBean();
                            setAddressData(addressInfoBean);
                        } else if (addressInfoEntity.getCode().equals("02")) {
                            tv_mine_setting_address.setText("");
                        } else {
                            tv_mine_setting_address.setText("");
                            showToast(AppDataActivity.this, addressInfoEntity.getMsg());
                        }
                    }
                }
            });
        }
    }

    private void setAddressData(AddressInfoBean addressInfoBean) {
        tv_mine_setting_address.setText((getStrings(addressInfoBean.getConsignee()) + " "
                + getStrings(addressInfoBean.getAddress_com())
                + getStrings(addressInfoBean.getAddress())));
    }

    //    统计缓存
    private void getCacheStatic() {
        Img_PATH = getFilesDir().getAbsolutePath() + "/ImgArticle";
        files.add(new File(Img_PATH));
        files.add(AppDataActivity.this.getCacheDir());
        if (getGlideCacheFile(AppDataActivity.this) != null) {
            files.add(getGlideCacheFile(AppDataActivity.this));
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
    void offLine(View view) {
        if (exitAccount == null) {
            AlertSettingBean alertSettingBean = new AlertSettingBean();
            AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
            alertData.setCancelStr("取消");
            alertData.setDetermineStr("确定");
            alertData.setFirstDet(false);
            alertData.setMsg("确定要退出当前账号？");
            alertSettingBean.setStyle(AlertView.Style.Alert);
            alertSettingBean.setAlertData(alertData);
            exitAccount = new AlertView(alertSettingBean, this, this);
            exitAccount.setCancelable(true);
        }
        exitAccount.show();
    }

    private void isLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            uid = 0;
            ll_mine_personal.setVisibility(View.GONE);
            tv_set_account_exit.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAlertItemClick(Object o, int position) {
        if (o == exitAccount && position != AlertView.CANCELPOSITION) {
            NEW_USER_DIALOG = true;
            savePersonalInfoCache(AppDataActivity.this,new SavePersonalInfoBean(false));
            showToast(this, "注销成功");
            exitNewTaoBaoAccount();
//            exitOldTaoBaoAccount();
//        清除账号 修改我的页面
            finish();
        }
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

    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.arg1 == 66) {
                try {
                    GlideImageLoaderUtil.clearMemoryByActivity(AppDataActivity.this);
                    SharedPreferences sharedPreferences = getSharedPreferences("launchAD", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.clear().apply();
                    tv_set_clear_cache.setText(FileCacheUtils.getCacheListSize(files));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    });

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        isPause = true;
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (isPause) {
            loadData();
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
    @OnClick(R.id.tv_mine_setting_address)
    void setAddress(View view) {
        Intent intent = new Intent(AppDataActivity.this, SelectedAddressActivity.class);
        intent.putExtra("isMineSkip", true);
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
        getMarketApp(AppDataActivity.this,"本机应用商店暂未上线哦，小主会努力呢~");
    }

    //    意见反馈
    @OnClick(R.id.tv_setting_sug)
    void sugFeedBack(View view) {
        Intent intent = new Intent();
        intent.setClass(AppDataActivity.this, SuggestionFeedBackActivity.class);
        startActivity(intent);
    }

    //    清理缓存
    @OnClick({R.id.rel_mine_cache, R.id.tv_set_clear_cache})
    void clearAppCache(View view) {
        try {
            if (getFolderSize(new File(Img_PATH)) > 0 || getFolderSize(AppDataActivity.this.getCacheDir()) > 0) {
                createExecutor().execute(() -> {
                    try {
                        if (getFolderSize(new File(Img_PATH)) > 0) {
                            FileCacheUtils.deleteFolderFile(Img_PATH, false);
                        }
                        if (getFolderSize(AppDataActivity.this.getCacheDir()) > 0) {
                            FileCacheUtils.cleanInternalCache(AppDataActivity.this);
                        }
//                       七鱼客服清除缓存
                        QyServiceUtils.getQyInstance().clearQyCache();
                        PictureFileUtils.deleteCacheDirFile(AppDataActivity.this);
                        Message message = mHandler.obtainMessage();
                        message.arg1 = 66;
                        mHandler.sendMessage(message);
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

}
