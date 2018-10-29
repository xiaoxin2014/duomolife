package com.amkj.dmsh.shopdetails.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.release.dialogutils.AlertSettingBean;
import com.amkj.dmsh.release.dialogutils.AlertView;
import com.amkj.dmsh.release.dialogutils.OnAlertItemClickListener;
import com.amkj.dmsh.shopdetails.adapter.DirectPublishAppraiseAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectAppraisePassBean;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.ImgUrlHelp;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.utils.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfigC;
import com.luck.picture.lib.entity.LocalMediaC;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_ADD_IMG;

;

/**
 * 发表评论
 */
public class DirectPublishAppraiseActivity extends BaseActivity implements OnAlertItemClickListener {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    @BindView(R.id.recycler_direct_publish_appraise)
    RecyclerView recycler_direct_publish_appraise;
    private List<String> dataPath = new ArrayList<>();
    private List<String> mFilePathList = new ArrayList<>();
    private AlertView appraiseDialog;
    private List<DirectAppraisePassBean> directAppraisePassList = new ArrayList<>();
    private DirectPublishAppraiseAdapter directPublishAppraiseAdapter;
    public static final int REQUEST_PERMISSIONS = 60;
    private int uid;
    private String orderNo;
    @Override
    protected int getContentView() {
        return R.layout.activity_publish_appraise;
    }
    @Override
    protected void initViews() {
        loadHud.setCancellable(false);
        if (directAppraisePassList != null) {
            directAppraisePassList.clear();
        }
        getLoginStatus();
        tv_header_titleAll.setText("评价晒单");
        tv_header_shared.setCompoundDrawables(null, null, null, null);
        tv_header_shared.setText("提交");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            directAppraisePassList = bundle.getParcelableArrayList("appraiseData");
        }
        orderNo = intent.getStringExtra("orderNo");
        if(directAppraisePassList!=null&&directAppraisePassList.size()>0){
            for (int i = 0; i < directAppraisePassList.size(); i++) {
                directAppraisePassList.get(i).setImagePaths(DEFAULT_ADD_IMG);
            }
        }
        directPublishAppraiseAdapter = new DirectPublishAppraiseAdapter(DirectPublishAppraiseActivity.this, directAppraisePassList);
        recycler_direct_publish_appraise.setLayoutManager(new LinearLayoutManager(DirectPublishAppraiseActivity.this, LinearLayoutManager.VERTICAL, false));
        recycler_direct_publish_appraise.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        recycler_direct_publish_appraise.setAdapter(directPublishAppraiseAdapter);
    }

    @Override
    protected void loadData() {
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
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PictureConfigC.CHOOSE_REQUEST) {
                List<LocalMediaC> localMediaList = PictureSelector.obtainMultipleResult(data);
                if (localMediaList != null && localMediaList.size() > 0) {
                    StringBuffer spiltPath = new StringBuffer();
                    dataPath.clear();
                    LocalMediaC localMediaItem = localMediaList.get(0);
                    int productPosition = localMediaItem.getEvaCurrentItem();
                    for (LocalMediaC localMedia : localMediaList) {
                        if (!TextUtils.isEmpty(localMedia.getPath())) {
                            dataPath.add(localMedia.getPath());
                        }
                    }
                    dataPath.remove(ConstantVariable.DEFAULT_ADD_IMG);
                    if (dataPath.size() < 5) {
                        dataPath.add(DEFAULT_ADD_IMG);
                    }
                    for (int i = 0; i < dataPath.size(); i++) {
                        if (i == 0) {
                            spiltPath.append(dataPath.get(i));
                        } else {
                            spiltPath.append("," + dataPath.get(i));
                        }
                    }
                    DirectAppraisePassBean directAppraisePassBean = directAppraisePassList.get(productPosition);
                    directAppraisePassBean.setImagePaths(spiltPath.toString());
                    directAppraisePassList.set(productPosition, directAppraisePassBean);
                    directPublishAppraiseAdapter.notifyItemRangeChanged(productPosition, 1);
                }
            } else if (requestCode == ConstantVariable.IS_LOGIN_CODE) {
                getLoginStatus();
                loadData();
            } else if (requestCode == REQUEST_PERMISSIONS) {
                showToast(this, "请到应用管理授予权限");
                return;
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type .equals("appraiseDate")) {
            directAppraisePassList = (List<DirectAppraisePassBean>) message.result;
        } else if (message.type.equals("closeKeyBroad")) {
            if (message.result.equals("close")) {
                CommonUtils.hideSoftInput(DirectPublishAppraiseActivity.this, tv_header_shared);
            }
        }
    }

    @OnClick(R.id.tv_header_shared)
    void publishAppraise(View view) {
        // : 2016/8/24 发表评论
        if (directAppraisePassList.size() > 0) {
            int indexSuccess = 0;
            final StringBuffer imgCountAppend = new StringBuffer();
            for (int i = 0; i < directAppraisePassList.size(); i++) {
                DirectAppraisePassBean directAppraisePassBean = directAppraisePassList.get(i);
                if (!TextUtils.isEmpty(directAppraisePassBean.getContent())) {
                    indexSuccess++;
                }
            }
            if (indexSuccess < 1) {
                showToast(this, "请填写商品评论");
            } else {
                if (loadHud != null) {
                    loadHud.show();
                }
                tv_header_shared.setText("发表中…");
                tv_header_shared.setEnabled(false);
                mFilePathList.clear();
//                上传图片
                for (int i = 0; i < directAppraisePassList.size(); i++) {
                    String images = directAppraisePassList.get(i).getImagePaths();
                    int pageIndex = 0;
                    String[] splitFilePath = images.split(",");
                    if (splitFilePath.length > 0) {
                        for (int j = 0; j < splitFilePath.length; j++) {
                            if (j == splitFilePath.length - 1) {
                                if (!splitFilePath[j].equals(DEFAULT_ADD_IMG)) {
                                    mFilePathList.add(splitFilePath[j]);
                                    pageIndex++;
                                }
                                break;
                            } else {
                                mFilePathList.add(splitFilePath[j]);
                            }
                            pageIndex++;
                        }
                    }
                    if (i == 0) {
                        imgCountAppend.append(pageIndex + "");
                    } else {
                        imgCountAppend.append("," + pageIndex);
                    }
                }
                if (mFilePathList.size() == 0) {
                    for (int i = 0; i < directAppraisePassList.size(); i++) {
                        directAppraisePassList.get(i).setImagePaths("");
                    }
                    sendAppraiseData(directAppraisePassList);
                } else {
                    //                上传图片
                    ImgUrlHelp imgUrlHelp = new ImgUrlHelp();
                    imgUrlHelp.setUrl(DirectPublishAppraiseActivity.this, mFilePathList);
                    imgUrlHelp.setOnFinishListener(new ImgUrlHelp.OnFinishDataListener() {
                        @Override
                        public void finishData(List<String> data, Handler handler) {
                            String[] split = imgCountAppend.toString().split(",");
                            for (int i = 0; i < split.length; i++) {
                                imgCountAppend.delete(0, imgCountAppend.length());
                                if (Integer.parseInt(split[i]) != 0) {
                                    for (int k = 0; k < Integer.parseInt(split[i]); k++) {
                                        if (k == 0) {
                                            imgCountAppend.append(data.get(k));
                                        } else {
                                            imgCountAppend.append("," + data.get(k));
                                        }
                                    }
                                    if (Integer.parseInt(split[i]) != 0) {
                                        for (int j = Integer.parseInt(split[i]) - 1; j >= 0; j--) {
                                            data.remove(j);
                                        }
                                    }
                                }
                                directAppraisePassList.get(i).setImages(imgCountAppend.toString());
                            }
                            sendAppraiseData(directAppraisePassList);
                            handler.removeCallbacksAndMessages(null);
                        }

                        @Override
                        public void finishError(String error) {
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            tv_header_shared.setText("发表评价");
                            tv_header_shared.setEnabled(true);
                            showToast(DirectPublishAppraiseActivity.this, "网络异常");
                        }

                        @Override
                        public void finishSingleImg(String singleImg, Handler handler) {
                        }
                    });
                }
            }

        }
    }

    private void sendAppraiseData(List<DirectAppraisePassBean> directAppraisePassList) {
        String url = Url.BASE_URL + Url.Q_SEND_APPRAISE;
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", orderNo);
        params.put("userId", uid);
        JSONObject jsonObject;
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < directAppraisePassList.size(); i++) {
            DirectAppraisePassBean directAppraisePassBean = directAppraisePassList.get(i);
            if (!TextUtils.isEmpty(directAppraisePassBean.getContent())
                    || !TextUtils.isEmpty(directAppraisePassBean.getImages())) {
                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("productId", directAppraisePassBean.getProductId());
                    jsonObject.put("orderProductId", directAppraisePassBean.getOrderProductId());
                    jsonObject.put("star", directAppraisePassBean.getStar());
                    jsonObject.put("content", directAppraisePassBean.getContent());
                    if (!TextUtils.isEmpty(directAppraisePassBean.getImages())) {
                        jsonObject.put("images", directAppraisePassBean.getImages());
                    }
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        params.put("evaluates", jsonArray.toString());
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                RequestStatus indentInfo = gson.fromJson(result, RequestStatus.class);
                if (indentInfo != null) {
                    if (indentInfo.getCode().equals("01")) {
                        showToast(DirectPublishAppraiseActivity.this, R.string.appraise_public_success);
                        finish();
                    } else {
                        showToast(DirectPublishAppraiseActivity.this, indentInfo.getResult() != null
                                ? indentInfo.getResult().getMsg() : indentInfo.getMsg());
                    }
                    tv_header_shared.setText("发表评价");
                    tv_header_shared.setEnabled(true);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                tv_header_shared.setText("发表评价");
                tv_header_shared.setEnabled(true);
                showToast(DirectPublishAppraiseActivity.this, R.string.commit_failed);
                super.onError(ex, isOnCallback);
            }
        });
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(tv_header_shared.getWindowToken(), 0); //强制隐藏键盘)
        }
        if (appraiseDialog == null || !appraiseDialog.isShowing()) {
            //弹窗 打开微信
            AlertSettingBean alertSettingBean = new AlertSettingBean();
            AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
            alertData.setCancelStr("取消");
            alertData.setDetermineStr("确定");
            alertData.setFirstDet(true);
            alertData.setMsg("评价未完成，确定要离开吗？");
            alertSettingBean.setStyle(AlertView.Style.Alert);
            alertSettingBean.setAlertData(alertData);
            appraiseDialog = new AlertView(alertSettingBean, DirectPublishAppraiseActivity.this, DirectPublishAppraiseActivity.this);
            appraiseDialog.setCancelable(true);
            appraiseDialog.show();
        } else {
            appraiseDialog.dismiss();
        }
    }

    @Override
    public void onAlertItemClick(Object o, int position) {
        if (o == appraiseDialog && position != AlertView.CANCELPOSITION) {
            finish();
        }
    }
}
