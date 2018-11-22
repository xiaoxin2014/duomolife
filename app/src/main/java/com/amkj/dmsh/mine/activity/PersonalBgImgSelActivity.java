package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.adapter.PersonalBgImgSelAdapter;
import com.amkj.dmsh.mine.bean.MineBgImgEntity;
import com.amkj.dmsh.mine.bean.MineBgImgEntity.MineBgImgBean;
import com.amkj.dmsh.utils.ImgUrlHelp;
import com.amkj.dmsh.utils.alertdialog.AlertDialogBottomListHelper;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.utils.pictureselector.PictureSelectorUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfigC;
import com.luck.picture.lib.entity.LocalMediaC;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;

;
;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/9/26
 * class description:背景图片选择
 */
public class PersonalBgImgSelActivity extends BaseActivity{
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    private PersonalBgImgSelAdapter personalBgImgSelAdapter;
    private List<MineBgImgBean> mineBgImgBeanList = new ArrayList<>();
    private int coverImgWidth;
    private int coverImgHeight;
    private MineBgImgEntity mineBgImgEntity;
    private AlertDialogBottomListHelper alertDialogBottomListHelper;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_bg_sel;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
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
                        if (alertDialogBottomListHelper == null) {
                            alertDialogBottomListHelper = new AlertDialogBottomListHelper(PersonalBgImgSelActivity.this);
                            alertDialogBottomListHelper.setTitleVisibility(GONE).setMsg("选择图片")
                                    .setItemData(new String[]{"从相册上传", "拍照上传"}).itemNotifyDataChange().setAlertListener(new AlertDialogBottomListHelper.AlertItemClickListener() {
                                @Override
                                public void itemClick(@Nullable String text) {

                                }

                                @Override
                                public void itemPosition(int itemPosition) {
                                    openImageGallery(itemPosition);
                                }
                            });
                        }
                        alertDialogBottomListHelper.show();
                    }
                }
            }
        });
        coverImgWidth = AutoSizeUtils.mm2px(mAppContext, 750);
        coverImgHeight = AutoSizeUtils.mm2px(mAppContext, 480);
    }

    /**
     * 弹窗选择图片
     *
     * @param itemPosition
     */
    private void openImageGallery(int itemPosition) {
        ConstantMethod constantMethod = new ConstantMethod();
        constantMethod.setOnGetPermissionsSuccess(new ConstantMethod.OnGetPermissionsSuccessListener() {
            @Override
            public void getPermissionsSuccess() {
                if (itemPosition == 0) {
//                从相册选择 裁剪图片
                    PictureSelectorUtils.getInstance()
                            .resetVariable()
                            .isCrop(true)
                            .imageMode(PictureConfigC.SINGLE)
                            .withAspectRatio(coverImgWidth, coverImgHeight)
                            .openGallery(PersonalBgImgSelActivity.this);
                } else if (itemPosition == 1) {
                    PictureSelectorUtils.getInstance()
                            .resetVariable()
                            .isCrop(true)
                            .withAspectRatio(coverImgWidth, coverImgHeight)
                            .openCamera(PersonalBgImgSelActivity.this);
                }
            }
        });
        constantMethod.getPermissions(PersonalBgImgSelActivity.this, Permission.Group.STORAGE);
    }

    private void setBgImgUrl(final String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl)) {
            String url = Url.BASE_URL + Url.MINE_CHANGE_DATA;
            Map<String, Object> params = new HashMap<>();
            params.put("uid", userId);
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
        NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url, null, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                mineBgImgBeanList.clear();
                Gson gson = new Gson();
                mineBgImgEntity = gson.fromJson(result, MineBgImgEntity.class);
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
                NetLoadUtils.getQyInstance().showLoadSirSuccess(loadService);
            }

            @Override
            public void netClose() {
                setBgImg();
                smart_communal_refresh.finishRefresh();
                showToast(PersonalBgImgSelActivity.this, R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSirSuccess(loadService);
            }

            @Override
            public void onError(Throwable throwable) {
                setBgImg();
                smart_communal_refresh.finishRefresh();
                showToast(PersonalBgImgSelActivity.this, R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSirSuccess(loadService);
            }
        });
    }

    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    public void setBgImg() {
        if (mineBgImgBeanList.size() < 1) {
            MineBgImgBean mineBgImgBean = new MineBgImgBean();
            mineBgImgBean.setBgimg_url(ConstantVariable.BASE_RESOURCE_DRAW + R.drawable.plus_icon_nor);
            mineBgImgBeanList.add(mineBgImgBean);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == ConstantVariable.IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfigC.CHOOSE_REQUEST) {
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

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(alertDialogBottomListHelper!=null&&alertDialogBottomListHelper.getAlertDialog()!=null
                &&alertDialogBottomListHelper.getAlertDialog().isShowing()){
            alertDialogBottomListHelper.dismiss();
        }
    }
}
