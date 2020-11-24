package com.amkj.dmsh.views.alertdialog;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseAlertDialog;
import com.amkj.dmsh.base.BaseRealNameEntity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.homepage.bean.CommunalRuleEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.utils.ImgUrlHelp;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.pictureselector.PictureSelectorUtils;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.luck.picture.lib.config.PictureConfigC;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.setEtFilter;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.GET_REMIN_TEXT;

/**
 * Created by xiaoxin on 2020/6/30
 * Version:v4.6.2
 * ClassDescription :购买海外商品实名认证弹窗
 */
public class AlertDialogRealName extends BaseAlertDialog {
    @BindView(R.id.iv_custom)
    ImageView mIvCustom;
    @BindView(R.id.et_oversea_name)
    EditText mEtOverseaName;
    @BindView(R.id.et_oversea_card)
    EditText mEtOverseaCard;
    @BindView(R.id.iv_front)
    ImageView mIvFront;
    @BindView(R.id.iv_background)
    ImageView mIvBackground;
    @BindView(R.id.delete_front)
    ImageView mDeleteFront;
    @BindView(R.id.delete_background)
    ImageView mDeleteBackground;
    @BindView(R.id.tv_commit)
    TextView mTvCommit;
    private Activity mContext;
    private int position;
    private String mIdcardImg1;
    private String mIdcardImg2;
    private AlertDialogRule alertLotteryRuleDialogHelper;
    private CommitListener commitListener;
    private BaseRealNameEntity mIndentWriteBean;

    public AlertDialogRealName(Activity context, BaseRealNameEntity indentWriteBean) {
        super(context);
        setEtFilter(mEtOverseaName);
        setEtFilter(mEtOverseaCard);
        mContext = context;
        mIndentWriteBean = indentWriteBean;
        mEtOverseaName.setText(getStrings(indentWriteBean.getRealName()));
        mEtOverseaCard.setText(getStrings(indentWriteBean.getShowIdCard()));
        if (!TextUtils.isEmpty(indentWriteBean.getIdcardImg1())) {
            mIdcardImg1 = indentWriteBean.getIdcardImg1();
            GlideImageLoaderUtil.loadCenterCrop(context, mIvFront, mIdcardImg1, R.drawable.idcard_front);
            mDeleteFront.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(indentWriteBean.getIdcardImg2())) {
            mIdcardImg2 = indentWriteBean.getIdcardImg2();
            GlideImageLoaderUtil.loadCenterCrop(context, mIvBackground, mIdcardImg2, R.drawable.idcard_background);
            mDeleteBackground.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_real_name;
    }

    @Override
    public void show(int gravity) {
        super.show(gravity);
        if (getAlertDialog().getWindow() != null) {
            getAlertDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
    }

    @OnClick({R.id.iv_custom, R.id.tv_commit, R.id.iv_front, R.id.iv_background, R.id.delete_front, R.id.delete_background})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
            //海关要求
            case R.id.iv_custom:
                getReminText();
                break;
            //提交实名信息
            case R.id.tv_commit:
                String name = mEtOverseaName.getText().toString().trim();
                String idcard = mEtOverseaCard.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    showToast(R.string.input_name);
                } else if (TextUtils.isEmpty(idcard)) {
                    showToast(R.string.input_card);
                } else if (TextUtils.isEmpty(mIdcardImg1)) {
                    showToast("请上传身份证人像面");
                } else if (TextUtils.isEmpty(mIdcardImg2)) {
                    showToast("请上传身份证国徽面");
                } else { //提交实名信息
                    showLoadhud(mContext);
                    List<String> selectPath = new ArrayList<>();
                    if (!mIdcardImg1.contains("http")) {
                        selectPath.add(mIdcardImg1);
                    }
                    if (!mIdcardImg2.contains("http")) {
                        selectPath.add(mIdcardImg2);
                    }
                    if (idcard.equals(mIndentWriteBean.getShowIdCard())) {
                        idcard = mIndentWriteBean.getIdCard();
                    }
                    String finalIdcard = idcard;
                    if (selectPath.size() > 0) {
                        ImgUrlHelp imgUrlHelp = new ImgUrlHelp();
                        imgUrlHelp.setUrl(mContext, selectPath);
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

                                if (commitListener != null) {
                                    commitListener.commit(name, finalIdcard, mIdcardImg1, mIdcardImg2);
                                }
                                handler.removeCallbacksAndMessages(null);
                            }

                            @Override
                            public void finishError(String error) {
                                dismissLoadhud(mContext);
                                showToast("网络异常");
                            }

                            @Override
                            public void finishSingleImg(String singleImg, Handler handler) {
                            }
                        });
                    } else {
                        if (commitListener != null) {
                            commitListener.commit(name, finalIdcard, mIdcardImg1, mIdcardImg2);
                        }
                    }
                }
                break;
        }
    }

    public interface CommitListener {
        void commit(String name, String idcard, String idcardImg1, String idcardImg2);
    }

    public void setOnCommitListener(CommitListener commitListener) {
        this.commitListener = commitListener;
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
                    .openGallery(mContext);
        });
        constantMethod.getPermissions(mContext, Permission.Group.STORAGE);
    }

    public void update(String path) {
        if (position == 0) {
            mIdcardImg1 = path;
            mDeleteFront.setVisibility(View.VISIBLE);
            GlideImageLoaderUtil.loadCenterCrop(context, mIvFront, "file://" + path, R.drawable.idcard_front);
        } else {
            mIdcardImg2 = path;
            mDeleteBackground.setVisibility(View.VISIBLE);
            GlideImageLoaderUtil.loadCenterCrop(context, mIvBackground, "file://" + path, R.drawable.idcard_background);
        }
    }

    private void getReminText() {
        if (alertLotteryRuleDialogHelper == null) {
            showLoadhud(mContext);
            Map<Object, Object> params = new HashMap<>();
            params.put("reminderType", 28);
            NetLoadUtils.getNetInstance().loadNetDataPost(mContext, GET_REMIN_TEXT, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    dismissLoadhud(mContext);
                    CommunalRuleEntity communalRuleEntity = GsonUtils.fromJson(result, CommunalRuleEntity.class);
                    if (communalRuleEntity != null) {
                        if (communalRuleEntity.getCode().equals(SUCCESS_CODE)) {
                            if (communalRuleEntity.getCommunalRuleList() != null
                                    && communalRuleEntity.getCommunalRuleList().size() > 0) {
                                List<CommunalDetailObjectBean> lotteryRuleList = new ArrayList<>(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(communalRuleEntity.getCommunalRuleList()));
                                alertLotteryRuleDialogHelper = new AlertDialogRule(mContext);
                                alertLotteryRuleDialogHelper.showTitle(false);
                                alertLotteryRuleDialogHelper.setRuleData("为什么要实名认证？", lotteryRuleList);
                                alertLotteryRuleDialogHelper.show();
                            }
                        }
                    }
                }

                @Override
                public void onNotNetOrException() {
                    dismissLoadhud(mContext);
                }
            });
        } else {
            alertLotteryRuleDialogHelper.show();
        }
    }
}
