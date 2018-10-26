package com.amkj.dmsh.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.ImageBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.find.activity.ImagePagerActivity;
import com.amkj.dmsh.mine.adapter.SuggestionFeedBackTypeAdapter;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.mine.bean.SuggestionTypeEntity;
import com.amkj.dmsh.mine.bean.SuggestionTypeEntity.FeedBackTypeBean;
import com.amkj.dmsh.release.adapter.ImgGridRecyclerAdapter;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.ImgUrlHelp;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.utils.pictureselector.PictureSelectorUtils;
import com.amkj.dmsh.utils.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfigC;
import com.luck.picture.lib.entity.LocalMediaC;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.BASE_URL;
import static com.amkj.dmsh.constant.Url.MINE_FEEDBACK_TYPE;
import static com.amkj.dmsh.release.adapter.ImgGridRecyclerAdapter.DEFAULT_ADD_IMG;

;

/**
 * Created by atd48 on 2016/10/19.
 */
public class SuggestionFeedBackActivity extends BaseActivity {
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
    private int uid;
    private ImgGridRecyclerAdapter imgGridRecyclerAdapter;
    private ArrayList<String> mSelectPath = new ArrayList<>();
    private ArrayList<String> dataPath = new ArrayList<>();
    private final int REQUEST_PERMISSIONS = 60;
    private final int SUBMIT_FEEDBACK_CODE = 120;
    private List<FeedBackTypeBean> feedBackTypeBeans = new ArrayList<>();
    private int maxSelImg = 5;
    private int adapterPosition;
    private AlertDialog selectAlertView;
    private SuggestionFeedBackTypeAdapter suggestionFeedBackAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_suggestion_feed_back;
    }

    @Override
    protected void initViews() {
        tv_header_titleAll.setText("意见反馈");
        header_shared.setCompoundDrawables(null, null, null, null);
        header_shared.setText("提交");
        getLoginStatus();
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        if (app.getScreenWidth() >= AutoSizeUtils.mm2px(mAppContext,600)) {
            rv_sug_img_show.setLayoutManager(new GridLayoutManager(SuggestionFeedBackActivity.this, 5));
        } else {
            rv_sug_img_show.setLayoutManager(new GridLayoutManager(SuggestionFeedBackActivity.this, 3));
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
                    mSelectPath.clear();
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
                    CommonUtils.hideSoftInput(SuggestionFeedBackActivity.this, emoji_mine_suggestion_feed_back);
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
                if (position == imgLength
                        && dataPath.get(imgLength).equals(DEFAULT_ADD_IMG)) {
                    PictureSelectorUtils.getInstance()
                            .resetVariable()
                            .isCrop(false)
                            .selImageList(mSelectPath)
                            .selMaxNum(maxSelImg)
                            .imageMode(PictureConfigC.MULTIPLE)
                            .isShowGif(true)
                            .openGallery(SuggestionFeedBackActivity.this);
                } else {
                    Intent intent = new Intent(SuggestionFeedBackActivity.this, ImagePagerActivity.class);
                    intent.putExtra(ImagePagerActivity.INTENT_POSITION, position);
                    List<ImageBean> imageBeanList = new ArrayList<>();
                    ImageBean imageBean;
                    for (String imgUrl : mSelectPath) {
                        imageBean = new ImageBean();
                        imageBean.setPicUrl(imgUrl);
                        imageBeanList.add(imageBean);
                    }
                    intent.putParcelableArrayListExtra(ImagePagerActivity.INTENT_IMGURLS, (ArrayList<? extends Parcelable>) imageBeanList);
                    startActivity(intent);
                }
            }
        });
        constantMethod.getPermissions(SuggestionFeedBackActivity.this, Permission.Group.STORAGE);
    }


    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(rv_sug_img_show.getWindowToken(), 0); //强制隐藏键盘)
        }
        finish();
    }

    /**
     * 提交反馈
     * @param view
     */
    @OnClick(R.id.tv_suggestion_type)
    void commitSuggestion(View view) {
        if(feedBackTypeBeans.size()>0){
            if(selectAlertView == null){
                AlertDialog.Builder builder = new AlertDialog.Builder(SuggestionFeedBackActivity.this,R.style.Theme_AppCompat_Dialog);
                View dialogView = LayoutInflater.from(SuggestionFeedBackActivity.this).inflate(R.layout.alert_suggestion_feedback_type, null, false);
                RecyclerView communal_recycler_wrap = dialogView.findViewById(R.id.communal_recycler_wrap);
                communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(SuggestionFeedBackActivity.this));
                suggestionFeedBackAdapter = new SuggestionFeedBackTypeAdapter(feedBackTypeBeans);
                communal_recycler_wrap.setAdapter(suggestionFeedBackAdapter);
                communal_recycler_wrap.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                        // 设置分隔线资源ID
                        .setDividerId(R.drawable.item_divider_gray_f_two_px)
                        // 开启绘制分隔线，默认关闭
                        .enableDivider(true)
                        // 是否关闭标签点击事件，默认开启
                        .disableHeaderClick(false)
                        // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                        .setHeaderClickListener(null)
                        .create());
                selectAlertView = builder.create();
                Window window = selectAlertView.getWindow();
                if (window != null) {
                    window.setBackgroundDrawableResource(R.color.translucence);
                    WindowManager.LayoutParams attributes = window.getAttributes();
                    attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
                    attributes.height = 750;
                    window.setAttributes(attributes);
                    window.setGravity(Gravity.BOTTOM);
                    window.setContentView(view);
                }
            }
            if(!selectAlertView.isShowing()){
                selectAlertView.show();
            }
        }
    }

    @OnClick(R.id.tv_header_shared)
    void submitSuggestion(View view) {
        if (!TextUtils.isEmpty(emoji_mine_suggestion_feed_back.getText().toString().trim())) {
            if (loadHud != null) {
                loadHud.show();
            }
            header_shared.setText("提交中...");
            header_shared.setEnabled(false);
            if (dataPath.size() > 0 && dataPath.size() < maxSelImg) {
                dataPath.remove(dataPath.size() - 1);
            } else if (dataPath.size() == maxSelImg && dataPath.get(maxSelImg - 1).equals(ConstantVariable.DEFAULT_ADD_IMG)) {
                dataPath.remove(dataPath.size() - 1);
            }
            if (dataPath.size() < 1) {
                sendSuggestionData(null);
            } else {
                ImgUrlHelp imgUrlHelp = new ImgUrlHelp();
                imgUrlHelp.setUrl(SuggestionFeedBackActivity.this, dataPath);
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
                        showToast(SuggestionFeedBackActivity.this, "网络异常");
                    }

                    @Override
                    public void finishSingleImg(String singleImg, Handler handler) {
                    }
                });
            }
        } else {
            showToast(this, "请输入反馈内容");
        }
    }

    //  提交反馈
    private void sendSuggestionData(List<String> data) {
        //图片地址
        StringBuffer imgPath = new StringBuffer();
        String url = BASE_URL + Url.MINE_FEEDBACK;
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
                        showToast(SuggestionFeedBackActivity.this, "提交完成");
                        finish();
                    } else {
                        showToast(SuggestionFeedBackActivity.this, requestInfo.getResult() != null ?
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
                showToast(SuggestionFeedBackActivity.this, ex.getMessage() + "");
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
            startActivityForResult(intent, IS_LOGIN_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            if (requestCode == SUBMIT_FEEDBACK_CODE || requestCode == IS_LOGIN_CODE) {
                finish();
                return;
            } else {
                return;
            }
        }
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
        } else if (requestCode == IS_LOGIN_CODE) {
            getLoginStatus();
        }
    }


    @Override
    protected void loadData() {
        if(userId>0){
            String url = BASE_URL+MINE_FEEDBACK_TYPE;
            Map<String,Object> params = new HashMap<>();
            params.put("uid",userId);
            NetLoadUtils.getQyInstance().loadNetDataPost(this, url, params, new NetLoadUtils.NetLoadListener() {
                @Override
                public void onSuccess(String result) {
                    SuggestionTypeEntity suggestionTypeEntity = new Gson().fromJson(result, SuggestionTypeEntity.class);
                    if(suggestionTypeEntity!=null){
                        if(SUCCESS_CODE.equals(suggestionTypeEntity.getCode())){
                            feedBackTypeBeans.addAll(suggestionTypeEntity.getFeedBackTypeList());
                        }else{
                            showToast(SuggestionFeedBackActivity.this,getStrings(suggestionTypeEntity.getMsg()));
                        }
                    }
                }

                @Override
                public void netClose() {
                    showToast(SuggestionFeedBackActivity.this,R.string.unConnectedNetwork);
                }

                @Override
                public void onError(Throwable throwable) {
                    showToast(SuggestionFeedBackActivity.this,R.string.invalidData);
                }
            });
        }
    }
}
