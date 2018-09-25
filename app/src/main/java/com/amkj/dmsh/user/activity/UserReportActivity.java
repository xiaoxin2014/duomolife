package com.amkj.dmsh.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.ImageBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.find.activity.ImagePagerActivity;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.release.adapter.ImgGridRecyclerAdapter;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.ImgUrlHelp;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.utils.pictureselector.PictureSelectorUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfigC;
import com.luck.picture.lib.entity.LocalMediaC;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.yanzhenjie.permission.Permission;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.release.adapter.ImgGridRecyclerAdapter.DEFAULT_ADD_IMG;
import static com.amkj.dmsh.shopdetails.activity.DirectPublishAppraiseActivity.REQUEST_PERMISSIONS;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/10
 * class description:用户举报
 */
public class UserReportActivity extends BaseActivity{
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    //    反馈内容
    @BindView(R.id.emoji_mine_suggestion_feed_back)
    EditText emoji_mine_suggestion_feed_back;
    //    添加图片
    @BindView(R.id.rv_sug_img_show)
    RecyclerView rv_sug_img_show;
    private String TYPE = "publishAppraise";
    private int uid;
    private ImgGridRecyclerAdapter imgGridRecyclerAdapter;
    private ArrayList<String> mSelectPath;
    private ArrayList<String> dataPath = new ArrayList<>();
    private final int SUBMIT_FEEDBACK_CODE = 120;
    private int maxSelImg = 5;
    private int adapterPosition;
    @Override
    protected int getContentView() {
        return R.layout.activity_mine_suggestion_feed_back;
    }
    @Override
    protected void initViews() {
        tv_header_titleAll.setText("投诉");
        header_shared.setCompoundDrawables(null, null, null, null);
        header_shared.setText("提交");
        getLoginStatus();
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        if (app.getScreenWidth() >= AutoUtils.getPercentWidthSizeBigger(600)) {
            rv_sug_img_show.setLayoutManager(new GridLayoutManager(UserReportActivity.this, 5));
        } else {
            rv_sug_img_show.setLayoutManager(new GridLayoutManager(UserReportActivity.this, 3));
        }
        if (dataPath.size() < 1) {
            dataPath.add(DEFAULT_ADD_IMG);
        }
        rv_sug_img_show.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_img_white)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        imgGridRecyclerAdapter = new ImgGridRecyclerAdapter(this, dataPath);
        rv_sug_img_show.setAdapter(imgGridRecyclerAdapter);
        imgGridRecyclerAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.delete) {
                    adapterPosition = (int) view.getTag();
                    if (dataPath.size() > dataPath.size() - 1 && !dataPath.get(dataPath.size() - 1).equals(DEFAULT_ADD_IMG)) {
                        dataPath.set(dataPath.size() - 1, DEFAULT_ADD_IMG);
                    } else {
                        dataPath.remove(adapterPosition);
                        if (!dataPath.get(dataPath.size() - 1).equals(DEFAULT_ADD_IMG)) {
                            dataPath.add(DEFAULT_ADD_IMG);
                        }
                    }
                    mSelectPath = new ArrayList();
                    mSelectPath.addAll(dataPath);
                    if (mSelectPath.size() > 0 && mSelectPath.get(mSelectPath.size() - 1).equals(DEFAULT_ADD_IMG))
                        mSelectPath.remove(mSelectPath.size() - 1);
                    imgGridRecyclerAdapter.setNewData(dataPath);
                }
            }
        });
        imgGridRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                隐藏键盘
                if (((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).isActive()) {
                    CommonUtils.hideSoftInput(UserReportActivity.this, emoji_mine_suggestion_feed_back);
                }
                pickImage(position);
            }
        });
    }

    private void pickImage(final int position) {
        ConstantMethod constantMethod = new ConstantMethod();
        constantMethod.setOnGetPermissionsSuccess(new ConstantMethod.OnGetPermissionsSuccessListener() {
            @Override
            public void getPermissionsSuccess() {
                int imgLength = dataPath.size() - 1;
                if (position == imgLength && dataPath.get(imgLength).equals(DEFAULT_ADD_IMG)) {
                    PictureSelectorUtils.getInstance()
                            .resetVariable()
                            .isCrop(false)
                            .selImageList(mSelectPath)
                            .selMaxNum(maxSelImg)
                            .imageMode(PictureConfigC.MULTIPLE)
                            .isShowGif(true)
                            .openGallery(UserReportActivity.this);
                } else {
                    Intent intent = new Intent(UserReportActivity.this, ImagePagerActivity.class);
                    intent.putExtra(ImagePagerActivity.INTENT_POSITION, position);
                    List<ImageBean> imageBeanList = new ArrayList<>();
                    ImageBean imageBean;
                    for (String imgUrl:mSelectPath) {
                        imageBean = new ImageBean();
                        imageBean.setPicUrl(imgUrl);
                        imageBeanList.add(imageBean);
                    }
                    intent.putParcelableArrayListExtra(ImagePagerActivity.INTENT_IMGURLS, (ArrayList<? extends Parcelable>) imageBeanList);
                    startActivity(intent);
                }
            }
        });
        constantMethod.getPermissions(this, Permission.Group.STORAGE);
    }


    @OnClick(R.id.tv_life_back)
   void goBack(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(rv_sug_img_show.getWindowToken(), 0); //强制隐藏键盘)
        }
        finish();
    }


    @OnClick(R.id.tv_header_shared)
    void submitSuggestion(View view) {
        if (!TextUtils.isEmpty(emoji_mine_suggestion_feed_back.getText().toString().trim())) {
            if (loadHud != null) {
                loadHud.show();
            }
            header_shared.setText("提交中...");
            header_shared.setEnabled(false);
            if (dataPath.size() > 0 && dataPath.size() < 5) {
                dataPath.remove(dataPath.size() - 1);
            } else if (dataPath.size() == 5 && dataPath.get(4).equals("plus_icon_nor.png")) {
                dataPath.remove(dataPath.size() - 1);
            }
            if (dataPath.size() < 1) {
                sendSuggestionData(null);
            } else {
                ImgUrlHelp imgUrlHelp = new ImgUrlHelp();
                imgUrlHelp.setUrl(UserReportActivity.this, dataPath);
                imgUrlHelp.setOnFinishListener(new ImgUrlHelp.OnFinishDataListener() {
                    @Override
                    public void finishData(List<String> data, Handler handler) {
                        sendSuggestionData(data);
                        handler.removeCallbacksAndMessages(null);
                    }

                    @Override
                    public void finishError(String error) {
                        if (loadHud != null) {
                            loadHud.dismiss();
                        }
                        header_shared.setText("提交");
                        header_shared.setEnabled(true);
                        showToast(UserReportActivity.this, "网络异常");
                    }

                    @Override
                    public void finishSingleImg(String singleImg, Handler handler) {
                    }
                });
            }
        } else {
            showToast(this, "请输入反馈内容");
            return;
        }
    }

    //  提交反馈

    private void sendSuggestionData(List<String> data) {
        //图片地址
        StringBuffer imgPath = new StringBuffer();
        String url = Url.BASE_URL + Url.MINE_FEEDBACK;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", uid);
        params.put("remark", emoji_mine_suggestion_feed_back.getText().toString().trim());
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                if (i == 0) {
                    imgPath.append(data.get(i));
                } else {
                    imgPath.append("," + data.get(i));
                }
            }
            params.put("feed_images", imgPath);
        }
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                RequestStatus requestInfo = gson.fromJson(result, RequestStatus.class);
                if (requestInfo != null) {
                    if (requestInfo.getCode().equals("01")) {
                        showToast(UserReportActivity.this, "提交完成");
                        finish();
                    } else {
                        showToast(UserReportActivity.this, requestInfo.getResult() != null ?
                                requestInfo.getResult().getMsg() : requestInfo.getMsg());
                    }
                    header_shared.setText("提交");
                    header_shared.setEnabled(true);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                header_shared.setText("提交");
                header_shared.setEnabled(true);
                showToast(UserReportActivity.this, ex.getMessage() + "");
            }
        });
    }
    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(this, MineLoginActivity.class);
            startActivityForResult(intent, SUBMIT_FEEDBACK_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            if (requestCode == SUBMIT_FEEDBACK_CODE) {
                finish();
            } else {
                return;
            }
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfigC.CHOOSE_REQUEST) {
                List<LocalMediaC> localMediaList = PictureSelector.obtainMultipleResult(data);
                if (localMediaList != null && localMediaList.size() > 0) {
                    dataPath.clear();
                    for (LocalMediaC localMedia : localMediaList) {
                        if (!TextUtils.isEmpty(localMedia.getPath())) {
                            dataPath.add(localMedia.getPath());
                        }
                    }
                    dataPath.remove(ConstantVariable.DEFAULT_ADD_IMG);
                    if (dataPath.size() < maxSelImg) {
                        dataPath.add(DEFAULT_ADD_IMG);
                    }
                    mSelectPath.clear();
                    mSelectPath.addAll(dataPath);
                    if (mSelectPath.size() > 0 && mSelectPath.get(mSelectPath.size() - 1).equals(DEFAULT_ADD_IMG)) {
                        mSelectPath.remove(mSelectPath.size() - 1);
                    }
                    imgGridRecyclerAdapter.notifyDataSetChanged();
                }
            } else if (requestCode == REQUEST_PERMISSIONS) {
                showToast(this, "请到应用管理授予权限");
                return;
            } else if (requestCode == SUBMIT_FEEDBACK_CODE) {
                //我的
                //登陆成功，加载信息
                getLoginStatus();
            }
        }
    }

    @Override
    protected void loadData() {
    }


}
