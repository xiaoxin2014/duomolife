package com.amkj.dmsh.shopdetails.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.adapter.DirectPublishAppraiseAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectAppraisePassBean;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.ImgUrlHelp;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_ADD_IMG;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.Q_SEND_APPRAISE;

;

/**
 * ????????????
 */
public class DirectPublishAppraiseActivity extends BaseActivity{
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    @BindView(R.id.recycler_direct_publish_appraise)
    RecyclerView recycler_direct_publish_appraise;
    private List<String> dataPath = new ArrayList<>();
    private List<String> mFilePathList = new ArrayList<>();
    private List<DirectAppraisePassBean> directAppraisePassList = new ArrayList<>();
    private DirectPublishAppraiseAdapter directPublishAppraiseAdapter;
    public static final int REQUEST_PERMISSIONS = 60;
    private String orderNo;
    private AlertDialogHelper alertDialogHelper;

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
        getLoginStatus(this);
        tv_header_titleAll.setText("????????????");
        tv_header_shared.setCompoundDrawables(null, null, null, null);
        tv_header_shared.setText("??????");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            directAppraisePassList = bundle.getParcelableArrayList("appraiseData");
        }
        orderNo = intent.getStringExtra("orderNo");
        if (directAppraisePassList != null && directAppraisePassList.size() > 0) {
            for (int i = 0; i < directAppraisePassList.size(); i++) {
                directAppraisePassList.get(i).setImagePaths(DEFAULT_ADD_IMG);
            }
        }
        directPublishAppraiseAdapter = new DirectPublishAppraiseAdapter(DirectPublishAppraiseActivity.this, directAppraisePassList);
        recycler_direct_publish_appraise.setLayoutManager(new LinearLayoutManager(DirectPublishAppraiseActivity.this, LinearLayoutManager.VERTICAL, false));
        recycler_direct_publish_appraise.addItemDecoration(new ItemDecoration.Builder()
                // ?????????????????????ID
                .setDividerId(R.drawable.item_divider_five_dp).create());
        recycler_direct_publish_appraise.setAdapter(directPublishAppraiseAdapter);
    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            }
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
        } else if (requestCode == REQUEST_PERMISSIONS) {
            showToast("??????????????????????????????");
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("appraiseDate")) {
            directAppraisePassList = (List<DirectAppraisePassBean>) message.result;
        } else if (message.type.equals("closeKeyBroad")) {
            if (message.result.equals("close")) {
                CommonUtils.hideSoftInput(DirectPublishAppraiseActivity.this, tv_header_shared);
            }
        }
    }

    @OnClick(R.id.tv_header_shared)
    void publishAppraise(View view) {
        // : 2016/8/24 ????????????
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
                showToast("?????????????????????");
            } else {
                if (loadHud != null) {
                    loadHud.show();
                }
                tv_header_shared.setText("????????????");
                tv_header_shared.setEnabled(false);
                mFilePathList.clear();
//                ????????????
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
                    //                ????????????
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
                            tv_header_shared.setText("????????????");
                            tv_header_shared.setEnabled(true);
                            showToast("????????????");
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
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", orderNo);
        params.put("userId", userId);
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
        NetLoadUtils.getNetInstance().loadNetDataPost(this,Q_SEND_APPRAISE,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast(R.string.appraise_public_success);
                        finish();
                    } else {
                        showToastRequestMsg( requestStatus);
                    }
                    tv_header_shared.setText("????????????");
                    tv_header_shared.setEnabled(true);
                }
            }

            @Override
            public void onNotNetOrException() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                tv_header_shared.setText("????????????");
                tv_header_shared.setEnabled(true);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast( R.string.commit_failed);
            }
        });
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        confirmExitAppraise();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            confirmExitAppraise();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * ??????????????????
     */
    private void confirmExitAppraise() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm!=null&&imm.isActive()) {
            imm.hideSoftInputFromWindow(tv_header_shared.getWindowToken(), 0); //??????????????????)
        }
        if (alertDialogHelper == null) {
            alertDialogHelper = new AlertDialogHelper(DirectPublishAppraiseActivity.this);
            alertDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                    .setMsg("???????????????????????????????????????").setCancelText("??????").setConfirmText("??????")
                    .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
            alertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                @Override
                public void confirm() {
                    finish();
                }

                @Override
                public void cancel() {
                }
            });
        }
        alertDialogHelper.show();
    }
}
