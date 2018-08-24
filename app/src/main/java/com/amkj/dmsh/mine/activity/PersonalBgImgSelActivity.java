package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.adapter.PersonalBgImgSelAdapter;
import com.amkj.dmsh.mine.bean.MineBgImgEntity;
import com.amkj.dmsh.mine.bean.MineBgImgEntity.MineBgImgBean;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.release.dialogutils.AlertSettingBean;
import com.amkj.dmsh.release.dialogutils.AlertView;
import com.amkj.dmsh.release.dialogutils.OnAlertItemClickListener;
import com.amkj.dmsh.utils.ImgUrlHelp;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.utils.pictureselector.PictureSelectorUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfigC;
import com.luck.picture.lib.entity.LocalMediaC;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.yanzhenjie.permission.Permission;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/9/26
 * class description:背景图片选择
 */
public class PersonalBgImgSelActivity extends BaseActivity implements OnAlertItemClickListener {
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    private int uid;
    private PersonalBgImgSelAdapter personalBgImgSelAdapter;
    private List<MineBgImgBean> mineBgImgBeanList = new ArrayList<>();
    private AlertView openImg;
    private int coverImgWidth;
    private int coverImgHeight;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_bg_sel;
    }

    @Override
    protected void initViews() {
        getLoginStatus();
        tl_normal_bar.setSelected(true);
        tv_header_shared.setVisibility(GONE);
        communal_recycler.setLayoutManager(new GridLayoutManager(PersonalBgImgSelActivity.this, 3));
        personalBgImgSelAdapter = new PersonalBgImgSelAdapter(PersonalBgImgSelActivity.this, mineBgImgBeanList);
        communal_recycler.setAdapter(personalBgImgSelAdapter);

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {

                loadData();

        });
        personalBgImgSelAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MineBgImgBean mineBgImgBean = (MineBgImgBean) view.getTag();
                if (mineBgImgBean != null) {
                    if (mineBgImgBean.getId() > 0) {
//                        网页图片
                        setBgImgUrl(mineBgImgBean.getBgimg_url());
                    } else {
//                        存储图片地址
                        AlertSettingBean alertSettingBean = new AlertSettingBean();
                        AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
                        alertData.setCancelStr("取消");
                        alertData.setNormalData(new String[]{"从相册上传", "拍照上传"});
                        alertData.setFirstDet(true);
                        alertData.setTitle("选择图片");
                        alertSettingBean.setStyle(AlertView.Style.ActionSheet);
                        alertSettingBean.setAlertData(alertData);
                        openImg = new AlertView(alertSettingBean, PersonalBgImgSelActivity.this, PersonalBgImgSelActivity.this);
                        openImg.setCancelable(true);
                        openImg.show();
                    }
                }
            }
        });
        coverImgWidth = AutoUtils.getPercentWidthSize(750);
        coverImgHeight = AutoUtils.getPercentWidthSize(480);
    }

    private void setBgImgUrl(final String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl)) {
            String url = Url.BASE_URL + Url.MINE_CHANGE_DATA;
            Map<String, Object> params = new HashMap<>();
            params.put("uid", uid);
            params.put("bgimg_url", imgUrl);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    Gson gson = new Gson();
                    CommunalUserInfoEntity communalUserInfoEntity = gson.fromJson(result, CommunalUserInfoEntity.class);
                    if (communalUserInfoEntity != null) {
                        if (communalUserInfoEntity.getCode().equals("01")) {
                            PictureFileUtils.deleteCacheDirFile(PersonalBgImgSelActivity.this);
                            showToast(PersonalBgImgSelActivity.this, "修改完成");
                            Intent intent = new Intent();
                            intent.putExtra("imgUrl", imgUrl);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            showToast(PersonalBgImgSelActivity.this, communalUserInfoEntity.getMsg());
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            showToast(this, R.string.img_url_error);
        }
    }

    @Override
    protected void loadData() {
        String url = Url.BASE_URL + Url.MINE_BG_IMG_LIST;
        if (NetWorkUtils.checkNet(PersonalBgImgSelActivity.this)) {
            XUtil.Post(url, null, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    mineBgImgBeanList.clear();
                    Gson gson = new Gson();
                    MineBgImgEntity mineBgImgEntity = gson.fromJson(result, MineBgImgEntity.class);
                    if (mineBgImgEntity != null) {
                        if (mineBgImgEntity.getCode().equals("01")) {
                            mineBgImgBeanList.addAll(mineBgImgEntity.getMineBgImgList());
                            MineBgImgBean mineBgImgBean = new MineBgImgBean();
                            mineBgImgBean.setBgimg_url(ConstantVariable.BASE_RESOURCE_DRAW + R.drawable.plus_icon_nor);
                            mineBgImgBeanList.add(mineBgImgBean);
                        } else if (!mineBgImgEntity.getCode().equals("02")) {
                            showToast(PersonalBgImgSelActivity.this, mineBgImgEntity.getMsg());
                        }
                        setBgImg();
                        personalBgImgSelAdapter.setNewData(mineBgImgBeanList);
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    setBgImg();
                    smart_communal_refresh.finishRefresh();
                    communal_error.setVisibility(View.VISIBLE);
                    showToast(PersonalBgImgSelActivity.this, R.string.unConnectedNetwork);
                }
            });
        } else {
            setBgImg();
            smart_communal_refresh.finishRefresh();
            communal_load.setVisibility(GONE);
            showToast(PersonalBgImgSelActivity.this, R.string.unConnectedNetwork);
        }
    }

    public void setBgImg() {
        if (mineBgImgBeanList.size() < 1) {
            MineBgImgBean mineBgImgBean = new MineBgImgBean();
            mineBgImgBean.setBgimg_url(ConstantVariable.BASE_RESOURCE_DRAW + R.drawable.plus_icon_nor);
            mineBgImgBeanList.add(mineBgImgBean);
        }
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(this, MineLoginActivity.class);
            startActivityForResult(intent, ConstantVariable.IS_LOGIN_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == ConstantVariable.IS_LOGIN_CODE) {
                finish();
            } else {
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                getLoginStatus();
            } else if (requestCode == PictureConfigC.CHOOSE_REQUEST) {
                try {
                    List<LocalMediaC> localMediaList = PictureSelector.obtainMultipleResult(data);
                    if (localMediaList != null && localMediaList.size() > 0) {
                        LocalMediaC localMedia = localMediaList.get(0);
                        if (localMedia != null && !TextUtils.isEmpty(localMedia.getPath()) && localMedia.isCut()) {
                            String cutPath = localMedia.getCutPath();
                            if (!TextUtils.isEmpty(cutPath)) {
                                setCoverImg(cutPath);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData(View view) {
        communal_load.setVisibility(View.VISIBLE);
        communal_empty.setVisibility(View.GONE);
        communal_error.setVisibility(View.GONE);
        loadData();
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @Override
    public void onAlertItemClick(Object o, final int position) {
        if (o == openImg && position != AlertView.CANCELPOSITION) {
            ConstantMethod constantMethod = new ConstantMethod();
            constantMethod.setOnGetPermissionsSuccess(new ConstantMethod.OnGetPermissionsSuccessListener() {
                @Override
                public void getPermissionsSuccess() {
                    if (position == 0) {
//                从相册选择 裁剪图片
                        PictureSelectorUtils.getInstance()
                                .resetVariable()
                                .isCrop(true)
                                .imageMode(PictureConfigC.SINGLE)
                                .withAspectRatio(coverImgWidth, coverImgHeight)
                                .openGallery(PersonalBgImgSelActivity.this);
                    } else if (position == 1) {
                        PictureSelectorUtils.getInstance()
                                .resetVariable()
                                .isCrop(true)
                                .withAspectRatio(coverImgWidth, coverImgHeight)
                                .openCamera(PersonalBgImgSelActivity.this);
                    }
                }
            });
            constantMethod.getPermissions(this, Permission.Group.STORAGE);
        }
    }

    private void setCoverImg(String coverImgPath) {
        //        加载本地图片
        if (!TextUtils.isEmpty(coverImgPath)) {
            final ImgUrlHelp imgUrlHelp = new ImgUrlHelp();
            imgUrlHelp.setSingleImg(PersonalBgImgSelActivity.this, BitmapFactory.decodeFile(coverImgPath));
            if (loadHud != null) {
                loadHud.show();
            }
            imgUrlHelp.setOnFinishListener(new ImgUrlHelp.OnFinishDataListener() {
                @Override
                public void finishData(List<String> data, Handler handler) {
                }

                @Override
                public void finishError(String error) {
                    showToast(PersonalBgImgSelActivity.this, "网络异常");
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                }

                @Override
                public void finishSingleImg(String singleImg, Handler handler) {
                    if (singleImg != null) {
                        setBgImgUrl(singleImg);
                        handler.removeCallbacksAndMessages(null);
                    }
                }
            });
        }
    }
}
