package com.amkj.dmsh.find.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.bean.EventMessageBean;
import com.amkj.dmsh.find.bean.TopicDetailEntity;
import com.amkj.dmsh.homepage.bean.ScoreGoodsEntity.ScoreGoodsBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.release.adapter.ImgGArticleRecyclerAdapter;
import com.amkj.dmsh.release.bean.ImagePathBean;
import com.amkj.dmsh.release.tutu.CustomMultipleFragment;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.ImgUrlHelp;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.TextWatchListener;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.pictureselector.PictureSelectorUtils;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfigC;
import com.luck.picture.lib.entity.LocalMediaC;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;
import org.lasque.tusdk.TuSdkGeeV1;
import org.lasque.tusdk.impl.components.TuEditMultipleComponent;
import org.lasque.tusdk.impl.components.edit.TuEditMultipleOption;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.emoji.widget.EmojiEditText;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_ADD_IMG;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.GET_EVALUATE_TIP;
import static com.amkj.dmsh.constant.Url.PUBLISH_POST_ANDE_VALUATE;
import static com.amkj.dmsh.release.tutu.CameraComponentSample.DEFAULT_PATH;
import static com.amkj.dmsh.utils.ImageFormatUtils.getImageFormatInstance;

/**
 * Created by xiaoxin on 2019/7/17
 * Version:v4.1.0
 * ClassDescription :??????-????????????-????????????
 */
public class JoinTopicActivity extends BaseActivity {
    @BindView(R.id.et_input)
    EmojiEditText mEtInput;
    @BindView(R.id.rv_img_article)
    RecyclerView rv_img_article;    //    ????????????
    @BindView(R.id.tv_find_release_topic)
    TextView mTvRelease;
    @BindView(R.id.tv_topic_name)
    TextView mTvTopicName;
    @BindView(R.id.tv_score_tips)
    TextView mTvScoreTips;     //??????????????????
    @BindView(R.id.tv_score_tips_bottom)
    TextView mTvScoreTipsBottom;     //??????????????????
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.tv_goods_name)
    TextView mTvGoodsName;  //????????????
    @BindView(R.id.tv_max_reward)
    TextView mTvMaxReward;  //????????????
    @BindView(R.id.ll_score_goods)
    LinearLayout mRlScoreGoods;
    @BindView(R.id.rating_bar)
    MaterialRatingBar mRatingBar;
    @BindView(R.id.ll_ratingbar)
    LinearLayout mLlRatingbar;
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.cb_cryptonym)
    CheckBox mCbCryptonym;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.ll_editor)
    LinearLayout mLlEditor;
    @BindView(R.id.tv_award_rules)
    TextView mTvAwardRules;
    // ???????????????????????????
    private String Img_PATH = null;
    private ImgGArticleRecyclerAdapter imgGArticleRecyclerAdapter;
    private List<ImagePathBean> imagePathBeans = new ArrayList<>();
    //????????????
    private ArrayList<String> mSelectPath = new ArrayList<>();

    //?????????????????????
    private List<String> updatedImages = new ArrayList<>();
    private int adapterPosition;
    private final int REQUEST_IMG = 80;
    private String topicId;
    private ScoreGoodsBean scoreGoodsBean;
    private int maxSelImg = 9;
    private AlertDialogHelper alertDialogHelper;
    private String content = "";
    private TopicDetailEntity mTopicDetailEntity;
    private List<Map<Integer, String>> mImgTipList = new ArrayList<>();
    private List<Map<Integer, String>> mWordTipList = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_join_topic;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        if (intent != null) {
            topicId = intent.getStringExtra("topicId");
            scoreGoodsBean = intent.getParcelableExtra("scoreGoods");
            String reminder = intent.getStringExtra("reminder");
            //???????????????
            if (!TextUtils.isEmpty(reminder)) {
                mEtInput.setHint(reminder);
            }

            //????????????
            if (!TextUtils.isEmpty(topicId)) {
                String topicTitle = intent.getStringExtra("topicTitle");
                if (!TextUtils.isEmpty(topicTitle)) {
                    mTvTopicName.setText(topicTitle);
                    mTvTopicName.setVisibility(View.VISIBLE);
                }
            } else if (scoreGoodsBean != null) {//????????????
                //???????????????????????????????????????
                mTvTitle.setText("??????");
                mEtInput.setHint("???????????????????????????????????????????????????");
                GlideImageLoaderUtil.loadImage(this, mIvCover, scoreGoodsBean.getCover());
                mTvGoodsName.setText(getStrings(scoreGoodsBean.getProductName()));
                mRlScoreGoods.setVisibility(View.VISIBLE);
                mLlRatingbar.setVisibility(View.VISIBLE);
                mCbCryptonym.setVisibility(View.VISIBLE);
            } else {
                showToast("????????????????????????");
                finish();
                return;
            }
        } else {
            showToast("????????????????????????");
            finish();
        }

        Img_PATH = getFilesDir().getAbsolutePath() + "/ImgArticle";
        createSaveImgFile();
        if (imagePathBeans.size() < 1) {
            imagePathBeans.add(getImageFormatInstance().getDefaultAddImage());
        }

        //?????????????????????
        imgGArticleRecyclerAdapter = new ImgGArticleRecyclerAdapter(this, imagePathBeans);
        rv_img_article.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        rv_img_article.addItemDecoration(new ItemDecoration.Builder()
                .setDividerId(R.drawable.item_divider_img_white)
                .create());
        rv_img_article.setAdapter(imgGArticleRecyclerAdapter);
        imgGArticleRecyclerAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            //??????????????????
            if (view.getId() == R.id.delete) {
                adapterPosition = (int) view.getTag();
                getImageFormatInstance().delImageBean(imagePathBeans, adapterPosition);
                setSelectImageData();
                imgGArticleRecyclerAdapter.notifyDataSetChanged();
            }
        });
        imgGArticleRecyclerAdapter.setOnItemClickListener((adapter, view, position) -> {
            //????????????
            if (((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).isActive()) {
                CommonUtils.hideSoftInput(getActivity(), mEtInput);
            }
            pickImage(position);
        });

        mEtInput.addTextChangedListener(new TextWatchListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                content = s.toString();
                if (mWordTipList != null && mWordTipList.size() > 0) {
                    checkTextReward();
                }
            }
        });

        //???????????????
        KeyboardUtils.registerSoftInputChangedListener(this, height -> {
            if (height == 0) {
                mTvScoreTips.setVisibility(View.VISIBLE);
                mTvScoreTipsBottom.setVisibility(View.GONE);
            } else {
                //???????????????
                mTvScoreTips.setVisibility(View.GONE);
                mTvScoreTipsBottom.setVisibility(View.VISIBLE);
            }
        });
    }

    //???????????????????????????
    private void checkTextReward() {
        for (int i = 0; i < mWordTipList.size(); i++) {
            boolean flag = false;
            Map<Integer, String> map = mWordTipList.get(i);
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                Integer key = entry.getKey();
                String value = entry.getValue();
                if (key > content.length()) {
                    int length = key - content.length();
                    mTvScoreTips.setText(getLinkText(length, value, R.string.reward_tips));
                    mTvScoreTipsBottom.setText(getLinkText(length, value, R.string.reward_tips));
                    flag = true;
                    break;
                } else if (i == mWordTipList.size() - 1) {
                    chekImgReward();
                }
            }
            if (flag) break;
        }
    }

    //???????????????????????????
    private void chekImgReward() {
        for (int j = 0; j < mImgTipList.size(); j++) {
            boolean imgFlag = false;
            Map<Integer, String> map = mImgTipList.get(j);
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                Integer key = entry.getKey();
                String value = entry.getValue();
                if (key > getImageSize()) {
                    int length = key - getImageSize();
                    mTvScoreTips.setText(getLinkText(length, value, R.string.reward_tips_img));
                    mTvScoreTipsBottom.setText(getLinkText(length, value, R.string.reward_tips_img));
                    imgFlag = true;
                    break;
                }
            }
            if (imgFlag) break;
        }
    }

    private CharSequence getLinkText(int num, String reward, int resId) {
        String tips = getStringsFormat(getActivity(), resId, String.valueOf(num), reward);
        Link link = new Link(String.valueOf(num));
        link.setTextColor(getResources().getColor(R.color.text_normal_red))
                .setUnderlined(false);
        Link link1 = new Link(reward);
        link1.setTextColor(getResources().getColor(R.color.text_normal_red))
                .setUnderlined(false);
        CharSequence charSequence = LinkBuilder.from(this, tips)
                .addLink(link)
                .addLink(link1)
                .build();
        return charSequence;
    }

    private void createSaveImgFile() {
        File destDir = new File(Img_PATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    private void pickImage(final int position) {
        ConstantMethod constantMethod = new ConstantMethod();
        constantMethod.setOnGetPermissionsSuccess(() -> {
            if (imagePathBeans.get(position).getPath().contains(DEFAULT_ADD_IMG)) {
                PictureSelectorUtils.getInstance()
                        .resetVariable()
                        .isCrop(false)
                        .selImageList(mSelectPath)
                        .imageMode(PictureConfigC.MULTIPLE)
                        .isShowGif(true)
                        .openGallery(getActivity());
            } else {
                TinkerBaseApplicationLike tinkerApplicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
                tinkerApplicationLike.initTuSdk();
                // ????????????
                TuEditMultipleComponent component = TuSdkGeeV1.editMultipleCommponent(this, (result, error, lastFragment) -> {
                    try {
                        imagePathBeans.set(position, new ImagePathBean(result.imageSqlInfo.path, true));
                        setSelectImageData();
                        imgGArticleRecyclerAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                component.componentOption().editMultipleOption().setComponentClazz(CustomMultipleFragment.class);// ?????????????????????
                component.componentOption().editMultipleOption().setRootViewLayoutId(R.layout.tusdk_impl_component_edit_multiple_fragment); // ?????????????????????ID
                // ????????????
                TuEditMultipleOption option = component.componentOption().editMultipleOption();
                option.setSaveToAlbumName(DEFAULT_PATH);
                option.setSaveToAlbum(true);
                option.setAutoRemoveTemp(true);
                component.setImage(BitmapFactory.decodeFile(imagePathBeans.get(position).getPath()))
                        // ??????????????????????????????????????????
                        .setAutoDismissWhenCompleted(true)
                        // ????????????
                        .showComponent();
            }
        });
        constantMethod.getPermissions(this, Permission.Group.STORAGE);
    }


    private void setSelectImageData() {
        mSelectPath.clear();
        mSelectPath.addAll(getImageFormatInstance().formatStringPathRemoveDefault(imagePathBeans));
        chekImgReward();
//        mTvScoreTips.setVisibility((imagePathBeans.size() < 1) || (imagePathBeans.size() < 2 && DEFAULT_ADD_IMG.equals(imagePathBeans.get(0).getPath())) ? View.VISIBLE : View.GONE);
    }

    //???????????????????????????
    private int getImageSize() {
        boolean flag = false;
        for (int i = 0; i < imagePathBeans.size(); i++) {
            ImagePathBean imagePathBean = imagePathBeans.get(i);
            String path = imagePathBean.getPath();
            if (DEFAULT_ADD_IMG.equals(path)) {
                flag = true;
            }
        }
        return flag ? imagePathBeans.size() - 1 : imagePathBeans.size();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IS_LOGIN_CODE:
                    loadData();
                    break;
                case 100:
                case REQUEST_IMG:
                case PictureConfigC.CHOOSE_REQUEST:
                    List<LocalMediaC> localMediaList = PictureSelector.obtainMultipleResult(data);
                    if (localMediaList != null && localMediaList.size() > 0) {
                        imagePathBeans.clear();
                        for (LocalMediaC localMedia : localMediaList) {
                            if (!TextUtils.isEmpty(localMedia.getPath())) {
                                imagePathBeans.add(new ImagePathBean(localMedia.getPath(), true));
                            }
                        }
                        pickImage(imagePathBeans.size() - 1);

                        if (imagePathBeans.size() < maxSelImg) {
                            imagePathBeans.add(getImageFormatInstance().getDefaultAddImage());
                        }
                        setSelectImageData();
                        imgGArticleRecyclerAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }


    @Override
    protected void loadData() {
        //????????????????????????????????????????????????????????????????????????????????????????????????
        if (TextUtils.isEmpty(topicId)) {
            Map<String, Object> params = new HashMap<>();
            params.put("productId", scoreGoodsBean.getProductId());
            NetLoadUtils.getNetInstance().loadNetDataPost(this, GET_EVALUATE_TIP, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    mTopicDetailEntity = GsonUtils.fromJson(result, TopicDetailEntity.class);
                    if (mTopicDetailEntity != null) {
                        if (mTopicDetailEntity.getCode().equals(SUCCESS_CODE)) {
                            mImgTipList = mTopicDetailEntity.getImgTipList();
                            mWordTipList = mTopicDetailEntity.getWordTipList();
                            checkTextReward();
                            //???????????????
                            mEtInput.setHint(getStrings(mTopicDetailEntity.getReminder()));
                            //????????????
                            mTvMaxReward.setText(getStrings(mTopicDetailEntity.getMaxRewardTip()));
                            mTvMaxReward.setVisibility(!TextUtils.isEmpty(mTopicDetailEntity.getMaxRewardTip()) ? View.VISIBLE : View.GONE);
                        }
                    }
                }
            });
        }
    }


    private void sendData(List<String> callBackPath) {
        if (!TextUtils.isEmpty(topicId) || scoreGoodsBean != null) {
            publishPost(callBackPath);
        } else {
            showToast("????????????????????????");
            finish();
            if (loadHud != null) {
                loadHud.dismiss();
            }
        }
    }

    private void publishPost(List<String> callBackPath) {
        Map<String, String> params = new HashMap<>();
        //????????????
        if (!TextUtils.isEmpty(getImgPath(callBackPath))) {
            params.put("images", getImgPath(callBackPath));
        }

        //????????????????????????
        if (!TextUtils.isEmpty(topicId)) {
            params.put("topicId", topicId);
        }

        //????????????
        if (!TextUtils.isEmpty(content))
            params.put("content", content);

        //????????????????????????
        if (scoreGoodsBean != null) {
            params.put("star", String.valueOf(mRatingBar.getNumStars()));
            params.put("orderProductId", scoreGoodsBean.getOrderProductId());
            params.put("productId", scoreGoodsBean.getProductId());
            params.put("orderNo", scoreGoodsBean.getOrderNo());
        }

        //????????????
        params.put("isCryptonym", mCbCryptonym.isChecked() ? "1" : "0");

        NetLoadUtils.getNetInstance().loadNetDataPost(this, !TextUtils.isEmpty(topicId) ? Url.JOIN_TOPIC : PUBLISH_POST_ANDE_VALUATE, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        if (!TextUtils.isEmpty(topicId)) {
                            //????????????????????????????????????
                            String type = getIntent().getStringExtra("type");
                            EventBus.getDefault().post(new EventMessage(ConstantVariable.UPDATE_POST_CONTENT, new EventMessageBean(TopicDetailActivity.class.getSimpleName(), type)));
                            showToast("????????????!" + (requestStatus.getScore() > 0 ? getIntegralFormat(getActivity(), R.string.post_pass_get_score, requestStatus.getScore()) : ""));
                            finish();
                        } else {
                            //????????????????????????
                            EventBus.getDefault().post(new EventMessage(ConstantVariable.UPDATE_SCORE_LIST, 0));
                            //?????????????????????????????????
                            if (scoreGoodsBean.getPosition() != -2) {
                                EventBus.getDefault().post(new EventMessage(ConstantVariable.UPDATE_WAIT_EVALUATE_INDENT_LIST, scoreGoodsBean.getPosition()));
                            }
                            //????????????????????????
                            Intent intent = new Intent(getActivity(), JoinSuccessActivity.class);
                            intent.putExtra("score", requestStatus.getScore());
                            intent.putExtra("reminder", requestStatus.getReminder());
                            intent.putExtra("postId", requestStatus.getPostId());
                            intent.putExtra("digest", requestStatus.getDigest());
                            intent.putExtra("coverPath", requestStatus.getCoverPath());
                            intent.putExtra("topicTitle", requestStatus.getTopicTitle());
                            intent.putExtra("drawRuleId", requestStatus.getDrawRuleId());
                            intent.putExtra("evaluateId", requestStatus.getEvaluateId());
                            startActivity(intent);
                            finish();
                        }
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

    private String getImgPath(List<String> callBackPath) {
        StringBuilder imgPath = new StringBuilder();
        if (callBackPath != null) {
            for (int i = 0; i < callBackPath.size(); i++) {
                if (i == 0) {
                    imgPath.append(callBackPath.get(i));
                } else {
                    imgPath.append(",").append(callBackPath.get(i));
                }
            }
        }

        return imgPath.toString();
    }

    @OnClick({R.id.tv_life_back, R.id.tv_find_release_topic, R.id.tv_award_rules, R.id.ll_score_goods})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                confirmExitAppraise();
                break;
            case R.id.tv_find_release_topic:
                boolean noSelectPic = (imagePathBeans.size() < 1) || (imagePathBeans.size() < 2 && DEFAULT_ADD_IMG.equals(imagePathBeans.get(0).getPath()));
                if (content.length() < 1 && noSelectPic) {
                    showToast("?????????????????????");
                } else if (noSelectPic) {//?????????
                    if (loadHud != null) {
                        loadHud.show();
                    }
                    sendData(null);
                } else {//?????????
                    if (loadHud != null) {
                        loadHud.show();
                    }
                    if (updatedImages.size() > 0) {
                        sendData(updatedImages);
                    } else {
                        ImgUrlHelp imgUrlHelp = new ImgUrlHelp();
                        imgUrlHelp.setUrl(getActivity(), mSelectPath);
                        imgUrlHelp.setOnFinishListener(new ImgUrlHelp.OnFinishDataListener() {
                            @Override
                            public void finishData(@NonNull List<String> data, Handler handler) {
                                updatedImages.clear();
                                updatedImages.addAll(data);
                                //????????????????????? ??????????????????
                                getImageFormatInstance().submitChangeIconStatus(imagePathBeans, false);
                                imgGArticleRecyclerAdapter.notifyDataSetChanged();
                                sendData(data);
                                handler.removeCallbacksAndMessages(null);
                            }

                            @Override
                            public void finishError(String error) {
                                if (loadHud != null) {
                                    loadHud.dismiss();
                                }
                                showToast("????????????");
                            }

                            @Override
                            public void finishSingleImg(String singleImg, Handler handler) {
                                if (loadHud != null) {
                                    loadHud.dismiss();
                                }
                            }
                        });
                    }
                }
                break;
            //????????????
            case R.id.tv_award_rules:
                ConstantMethod.setSkipPath(this, Url.REWARD_RULE, false);
                break;
            case R.id.ll_score_goods:
                break;

        }
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
        if (scoreGoodsBean != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && imm.isActive()) {
                imm.hideSoftInputFromWindow(mTvLifeBack.getWindowToken(), 0); //??????????????????)
            }
            if (alertDialogHelper == null) {
                alertDialogHelper = new AlertDialogHelper(this);
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
        } else {
            finish();
        }
    }
}
