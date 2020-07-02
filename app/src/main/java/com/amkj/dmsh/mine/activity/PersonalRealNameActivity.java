package com.amkj.dmsh.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity.CommunalUserInfoBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.homepage.bean.CommunalRuleEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.utils.ImgUrlHelp;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.pictureselector.PictureSelectorUtils;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfigC;
import com.luck.picture.lib.entity.LocalMediaC;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStringFilter;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.setEtFilter;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.GET_REMIN_TEXT;
import static com.amkj.dmsh.constant.Url.MINE_PAGE;
import static com.amkj.dmsh.constant.Url.MINE_RESET_REAL_NAME;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/1/11
 * version 3.7
 * class description:设置实名信息
 */

public class PersonalRealNameActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    //    真实名字
    @BindView(R.id.et_per_real_name)
    EditText et_per_real_name;
    //    身份证号码
    @BindView(R.id.et_per_real_id_card)
    EditText et_per_real_id_card;
    @BindView(R.id.iv_front)
    ImageView mIvFront;
    @BindView(R.id.iv_background)
    ImageView mIvBackground;
    @BindView(R.id.delete_front)
    ImageView mDeleteFront;
    @BindView(R.id.delete_background)
    ImageView mDeleteBackground;
    @BindView(R.id.rv_rule)
    RecyclerView mRvRule;
    private int position;
    private String mIdcardImg1;
    private String mIdcardImg2;
    private CommunalDetailAdapter ruleAdapter;
    private List<CommunalDetailObjectBean> ruleList = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_persional_set_real_name;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_titleAll.setText("实名信息");
        tv_header_shared.setCompoundDrawables(null, null, null, null);
        tv_header_shared.setText("完成");
        setEtFilter(et_per_real_name);
        setEtFilter(et_per_real_id_card);
        mRvRule.setLayoutManager(new LinearLayoutManager(this));
        ruleAdapter = new CommunalDetailAdapter(this, ruleList);
        mRvRule.setAdapter(ruleAdapter);
        KeyboardUtils.registerSoftInputChangedListener(this, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if (height == 0) {
                    ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).requestFocus();
                }
            }
        });
    }

    @Override
    protected void loadData() {
        if (userId < 1) {
            NetLoadUtils.getNetInstance().showLoadSirLoadFailed(loadService);
            return;
        }
        getRealNameInfo();
        getReminText();
    }

    //获取实名信息
    private void getRealNameInfo() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_PAGE, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                CommunalUserInfoEntity communalUserInfoEntity = GsonUtils.fromJson(result, CommunalUserInfoEntity.class);
                if (communalUserInfoEntity != null) {
                    if (communalUserInfoEntity.getCode().equals(SUCCESS_CODE)) {
                        CommunalUserInfoBean communalUserInfoBean = communalUserInfoEntity.getCommunalUserInfoBean();
                        setPersonalData(communalUserInfoBean);
                    } else if (!communalUserInfoEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(communalUserInfoEntity.getMsg());
                    }
                }
            }
        });
    }


    //显示实名信息
    private void setPersonalData(CommunalUserInfoBean communalUserInfoBean) {
        String idcard = getStrings(communalUserInfoBean.getIdcard());
        String realName = getStrings(communalUserInfoBean.getReal_name());
        String showIdcard = getStrings(communalUserInfoBean.getShowIdcard());
        if (!TextUtils.isEmpty(idcard)) {
            et_per_real_id_card.setText(getStringFilter(getStrings(showIdcard)));
            et_per_real_id_card.setSelection(showIdcard.length());
            et_per_real_id_card.setTag(R.id.id_tag, idcard);
            et_per_real_id_card.setTag(showIdcard);
            et_per_real_name.setText(getStringFilter(realName));
            et_per_real_name.setSelection(realName.length());
        }

        mIdcardImg1 = communalUserInfoBean.getIdcardImg1();
        if (!TextUtils.isEmpty(mIdcardImg1)) {
            GlideImageLoaderUtil.loadCenterCrop(this, mIvFront, mIdcardImg1, R.drawable.idcard_front);
            mDeleteFront.setVisibility(View.VISIBLE);
        }

        mIdcardImg2 = communalUserInfoBean.getIdcardImg2();
        if (!TextUtils.isEmpty(mIdcardImg2)) {
            GlideImageLoaderUtil.loadCenterCrop(this, mIvBackground, mIdcardImg2, R.drawable.idcard_background);
            mDeleteBackground.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 上传身份证
     */
    private void changeRealName() {
        if (userId < 1) {
            return;
        }
        String name = et_per_real_name.getText().toString().trim();
        String idcard = et_per_real_id_card.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            showToast(R.string.input_name);
        } else if (TextUtils.isEmpty(idcard)) {
            showToast(R.string.input_card);
        } else if (TextUtils.isEmpty(mIdcardImg1)) {
            showToast("请上传身份证人像面");
        } else if (TextUtils.isEmpty(mIdcardImg2)) {
            showToast("请上传身份证国徽面");
        } else {
            showLoadhud(this);
            List<String> selectPath = new ArrayList<>();
            if (!mIdcardImg1.contains("http")) {
                selectPath.add(mIdcardImg1);
            }
            if (!mIdcardImg2.contains("http")) {
                selectPath.add(mIdcardImg2);
            }
            if (selectPath.size() > 0) {
                ImgUrlHelp imgUrlHelp = new ImgUrlHelp();
                imgUrlHelp.setUrl(this, selectPath);
                imgUrlHelp.setOnFinishListener(new ImgUrlHelp.OnFinishDataListener() {
                    @Override
                    public void finishData(@NonNull List<String> data, Handler handler) {
                        for (int i = 0; i < data.size(); i++) {
                            if (selectPath.get(i).equals(mIdcardImg1)) {
                                mIdcardImg1 = data.get(i);
                            } else {
                                mIdcardImg2 = data.get(i);
                            }
                        }
                        updateRealNameInfo(name, idcard);
                        handler.removeCallbacksAndMessages(null);
                    }

                    @Override
                    public void finishError(String error) {
                        dismissLoadhud(getActivity());
                        showToast("网络异常");
                    }

                    @Override
                    public void finishSingleImg(String singleImg, Handler handler) {
                    }
                });
            } else {
                updateRealNameInfo(name, idcard);
            }
        }
    }

    //更新实名信息
    private void updateRealNameInfo(String realName, String idCard) {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("realName", realName);
        String showIdCard = (String) et_per_real_id_card.getTag();
        String reallyIdCard = (String) et_per_real_id_card.getTag(R.id.id_tag);
        //判断是否修改了默认的idcard
        if (idCard.equals(getStrings(showIdCard))) {
            params.put("idcard", reallyIdCard);
        } else {
            params.put("idcard", idCard);
        }
        params.put("idcardImg1", mIdcardImg1);
        params.put("idcardImg2", mIdcardImg2);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_RESET_REAL_NAME, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast(String.format(getResources().getString(R.string.doSuccess), "修改"));
                        finish();
                    } else {
                        showToastRequestMsg(requestStatus);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        } else if (requestCode == PictureConfigC.CHOOSE_REQUEST) {
            List<LocalMediaC> localMediaList = PictureSelector.obtainMultipleResult(data);
            if (localMediaList != null && localMediaList.size() > 0) {
                String path = localMediaList.get(0).getPath();
                if (position == 0) {
                    mIdcardImg1 = path;
                    mDeleteFront.setVisibility(View.VISIBLE);
                    GlideImageLoaderUtil.loadCenterCrop(this, mIvFront, "file://" + path, R.drawable.idcard_front);
                } else {
                    mIdcardImg2 = path;
                    mDeleteBackground.setVisibility(View.VISIBLE);
                    GlideImageLoaderUtil.loadCenterCrop(this, mIvBackground, "file://" + path, R.drawable.idcard_background);
                }
            }
        }
    }

    @OnClick(R.id.tv_life_back)
    public void goBack(View view) {
        finish();
    }

    @OnClick({R.id.tv_header_shared, R.id.iv_front, R.id.iv_background, R.id.delete_front, R.id.delete_background})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_header_shared:
                changeRealName();
                break;
            //上传正面
            case R.id.iv_front:
                selectPic(0);
                break;
            case R.id.delete_front:
                mIdcardImg1 = "";
                mDeleteFront.setVisibility(View.GONE);
                mIvFront.setImageResource(R.drawable.idcard_front);
                break;
            //上传反面
            case R.id.iv_background:
                selectPic(1);
                break;
            case R.id.delete_background:
                mIdcardImg2 = "";
                mDeleteBackground.setVisibility(View.GONE);
                mIvBackground.setImageResource(R.drawable.idcard_background);
                break;
        }
    }

    private void selectPic(int mposition) {
        ConstantMethod constantMethod = new ConstantMethod();
        constantMethod.setOnGetPermissionsSuccess(() -> {
            position = mposition;
            List<String> mSelectPath = new ArrayList<>();
            if (position == 0 && !TextUtils.isEmpty(mIdcardImg1) && !mIdcardImg1.contains("http")) {
                mSelectPath.add(mIdcardImg1);
            }

            if (position == 1 && !TextUtils.isEmpty(mIdcardImg2) && !mIdcardImg2.contains("http")) {
                mSelectPath.add(mIdcardImg2);
            }
            PictureSelectorUtils.getInstance()
                    .resetVariable()
                    .isCrop(false)
                    .selImageList(mSelectPath)
                    .imageMode(PictureConfigC.MULTIPLE)
                    .isShowGif(true)
                    .selMaxNum(1)
                    .openGallery(this);
        });
        constantMethod.getPermissions(this, Permission.Group.STORAGE);
    }


    //获取提示语
    private void getReminText() {
        Map<Object, Object> params = new HashMap<>();
        params.put("reminderType", 29);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, GET_REMIN_TEXT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                CommunalRuleEntity communalRuleEntity = GsonUtils.fromJson(result, CommunalRuleEntity.class);
                if (communalRuleEntity != null) {
                    if (communalRuleEntity.getCode().equals(SUCCESS_CODE)) {
                        if (communalRuleEntity.getCommunalRuleList() != null && communalRuleEntity.getCommunalRuleList().size() > 0) {
                            ruleList.clear();
                            ruleList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(communalRuleEntity.getCommunalRuleList()));
                            ruleAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v instanceof EditText) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.unregisterSoftInputChangedListener(this);
    }
}
