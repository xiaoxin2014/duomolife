package com.amkj.dmsh.find.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.text.emoji.widget.EmojiEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.ReplaceData;
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
import com.amkj.dmsh.utils.TextWatchListener;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.pictureselector.PictureSelectorUtils;
import com.google.gson.Gson;
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

import butterknife.BindView;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_ADD_IMG;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.GET_TOPIC_DETAIL;
import static com.amkj.dmsh.constant.Url.PUBLISH_POST_ANDE_VALUATE;
import static com.amkj.dmsh.release.tutu.CameraComponentSample.DEFAULT_PATH;
import static com.amkj.dmsh.utils.ImageFormatUtils.getImageFormatInstance;

/**
 * Created by xiaoxin on 2019/7/17
 * Version:v4.1.0
 * ClassDescription :话题-话题详情-参与话题
 */
public class JoinTopicActivity extends BaseActivity {
    @BindView(R.id.et_input)
    EmojiEditText mEtInput;
    @BindView(R.id.rv_img_article)
    RecyclerView rv_img_article;    //    商品图片
    @BindView(R.id.tv_find_release_topic)
    TextView mTvRelease;
    @BindView(R.id.tv_topic_name)
    TextView mTvTopicName;
    @BindView(R.id.tv_score_tips)
    TextView mTvScoreTips;     //获得积分提示
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.tv_goods_name)
    TextView mTvGoodsName;  //商品名称
    @BindView(R.id.tv_max_reward)
    TextView mTvMaxReward;  //最高奖励
    @BindView(R.id.ll_score_goods)
    LinearLayout mRlScoreGoods;
    @BindView(R.id.rating_bar)
    MaterialRatingBar mRatingBar;
    @BindView(R.id.ll_ratingbar)
    LinearLayout mLlRatingbar;
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    // 获得存储图片的路径
    private String Img_PATH = null;
    private ImgGArticleRecyclerAdapter imgGArticleRecyclerAdapter;
    private List<ImagePathBean> imagePathBeans = new ArrayList<>();
    //图片路径
    private ArrayList<String> mSelectPath = new ArrayList<>();

    //已上传图片保存
    private List<String> updatedImages = new ArrayList<>();
    private int adapterPosition;
    private final int REQUEST_IMG = 80;
    private String topicId;
    private ScoreGoodsBean scoreGoodsBean;
    private int maxSelImg = 9;
    private AlertDialogHelper alertDialogHelper;
    private String content = "";

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
            String rewardTip = intent.getStringExtra("rewardtip");
            //编辑框提示
            if (!TextUtils.isEmpty(reminder)) {
                mEtInput.setHint(reminder);
            }

            //奖励提示
            mTvScoreTips.setVisibility(!TextUtils.isEmpty(rewardTip) ? View.VISIBLE : View.GONE);
            mTvScoreTips.setText(getStrings(rewardTip));

            //参与话题
            if (!TextUtils.isEmpty(topicId)) {
                String topicTitle = intent.getStringExtra("topicTitle");
                if (!TextUtils.isEmpty(topicTitle)) {
                    mTvTopicName.setText(topicTitle);
                    mTvTopicName.setVisibility(View.VISIBLE);
                }
            } else if (scoreGoodsBean != null) {//商品晒单
                String maxRewardTip = intent.getStringExtra("maxRewardTip");
                //商品晒单（发布帖子并评价）
                GlideImageLoaderUtil.loadImage(this, mIvCover, scoreGoodsBean.getCover());
                mTvGoodsName.setText(getStrings(scoreGoodsBean.getProductName()));
                //最高奖励
                mTvMaxReward.setVisibility(!TextUtils.isEmpty(maxRewardTip) ? View.VISIBLE : View.GONE);
                mTvMaxReward.setText(getStrings(maxRewardTip));
                mRlScoreGoods.setVisibility(View.VISIBLE);
                mLlRatingbar.setVisibility(View.VISIBLE);
            } else {
                showToast("数据有误，请重试");
                finish();
                return;
            }
        } else {
            showToast("数据有误，请重试");
            finish();
        }

        Img_PATH = getFilesDir().getAbsolutePath() + "/ImgArticle";
        createSaveImgFile();
        if (imagePathBeans.size() < 1) {
            imagePathBeans.add(getImageFormatInstance().getDefaultAddImage());
        }

        //初始化图片列表
        imgGArticleRecyclerAdapter = new ImgGArticleRecyclerAdapter(this, imagePathBeans);
        rv_img_article.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        rv_img_article.addItemDecoration(new ItemDecoration.Builder()
                .setDividerId(R.drawable.item_divider_img_white)
                .create());
        rv_img_article.setAdapter(imgGArticleRecyclerAdapter);
        imgGArticleRecyclerAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            //点击删除图片
            if (view.getId() == R.id.delete) {
                adapterPosition = (int) view.getTag();
                getImageFormatInstance().delImageBean(imagePathBeans, adapterPosition);
                setSelectImageData();
                imgGArticleRecyclerAdapter.notifyDataSetChanged();
            }
        });
        imgGArticleRecyclerAdapter.setOnItemClickListener((adapter, view, position) -> {
            //隐藏键盘
            if (((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).isActive()) {
                CommonUtils.hideSoftInput(getActivity(), mEtInput);
            }
            pickImage(position);
        });

        mEtInput.addTextChangedListener(new TextWatchListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                content = s.toString();
            }
        });
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
            int imgLength = imagePathBeans.size() - 1;
            if (position == imgLength && imagePathBeans.get(imgLength).getPath()
                    .contains(DEFAULT_ADD_IMG)) {
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
                // 组件委托
                TuEditMultipleComponent component = TuSdkGeeV1.editMultipleCommponent(this, (result, error, lastFragment) -> {
                    ReplaceData path = new ReplaceData();
                    try {
                        path.setPosition(position);
                        path.setPath(result.imageSqlInfo.path);
                        EventBus.getDefault().post(new EventMessage("replace", path));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                component.componentOption().editMultipleOption().setComponentClazz(CustomMultipleFragment.class);// 指定控制器类型
                component.componentOption().editMultipleOption().setRootViewLayoutId(R.layout.tusdk_impl_component_edit_multiple_fragment); // 指定根视图资源ID
                // 设置图片
                TuEditMultipleOption option = component.componentOption().editMultipleOption();
                option.setSaveToAlbumName(DEFAULT_PATH);
                option.setSaveToAlbum(true);
                option.setAutoRemoveTemp(true);
                component.setImage(BitmapFactory.decodeFile(imagePathBeans.get(position).getPath()))
                        // 在组件执行完成后自动关闭组件
                        .setAutoDismissWhenCompleted(true)
                        // 开启组件
                        .showComponent();
            }
        });
        constantMethod.getPermissions(this, Permission.Group.STORAGE);
    }

    //接收图片
    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        switch (message.type) {
            case "resultImg":
                ReplaceData result = (ReplaceData) message.result;
                imagePathBeans.clear();
                imagePathBeans.add(new ImagePathBean(result.getPath(), true));
                imagePathBeans.add(getImageFormatInstance().getDefaultAddImage());
                setSelectImageData();
                imgGArticleRecyclerAdapter.notifyDataSetChanged();
                break;
            case "addImg":
                result = (ReplaceData) message.result;
                imagePathBeans.remove(imagePathBeans.size() - 1);
                imagePathBeans.add(new ImagePathBean(result.getPath(), true));
                if (imagePathBeans.size() < maxSelImg) {
                    imagePathBeans.add(getImageFormatInstance().getDefaultAddImage());
                }
                mSelectPath.clear();
                mSelectPath.addAll(getImageFormatInstance().formatStringPathRemoveDefault(imagePathBeans));
                imgGArticleRecyclerAdapter.notifyDataSetChanged();
                break;
            case "replace":
                ReplaceData path = (ReplaceData) message.result;
                imagePathBeans.set(path.getPosition(), new ImagePathBean(path.getPath(), true));
                setSelectImageData();
                imgGArticleRecyclerAdapter.notifyDataSetChanged();
                break;
        }
        if ("closeKeyBroad".equals(message.type) && "close".equals(message.result)) {
            CommonUtils.hideSoftInput(getActivity(), mEtInput);
        }
    }

    private void setSelectImageData() {
        mSelectPath.clear();
        mSelectPath.addAll(getImageFormatInstance().formatStringPathRemoveDefault(imagePathBeans));
        mTvScoreTips.setVisibility((imagePathBeans.size() < 1) || (imagePathBeans.size() < 2 && DEFAULT_ADD_IMG.equals(imagePathBeans.get(0).getPath())) ? View.VISIBLE : View.GONE);
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
                        int size = imagePathBeans.size();
                        imagePathBeans.clear();
                        for (LocalMediaC localMedia : localMediaList) {
                            if (!TextUtils.isEmpty(localMedia.getPath())) {
                                imagePathBeans.add(new ImagePathBean(localMedia.getPath(), true));
                            }
                        }
                        if (imagePathBeans.size() < maxSelImg) {
                            imagePathBeans.add(getImageFormatInstance().getDefaultAddImage());
                        }
                        setSelectImageData();
                        imgGArticleRecyclerAdapter.notifyDataSetChanged();

                        pickImage(size - 1);
                    }
                    break;
            }
        }
    }


    @Override
    protected void loadData() {
        //订单列表直接跳转到点评界面时，调用此接口获取编辑框提示和奖励提示
        if (TextUtils.isEmpty(topicId)) {
            Map<String, Object> params = new HashMap<>();
            NetLoadUtils.getNetInstance().loadNetDataPost(this, GET_TOPIC_DETAIL, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    TopicDetailEntity topicDetailEntity = new Gson().fromJson(result, TopicDetailEntity.class);
                    if (topicDetailEntity != null) {
                        if (topicDetailEntity.getCode().equals(SUCCESS_CODE)) {
                            //编辑框提示
                            mEtInput.setHint(getStrings(topicDetailEntity.getReminder()));
                            //奖励提示
                            mTvScoreTips.setVisibility(!TextUtils.isEmpty(topicDetailEntity.getRewardTip()) ? View.VISIBLE : View.GONE);
                            mTvScoreTips.setText(getStrings(topicDetailEntity.getRewardTip()));
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
            showToast("数据有误，请重试");
            finish();
            if (loadHud != null) {
                loadHud.dismiss();
            }
        }
    }

    private void publishPost(List<String> callBackPath) {
        Map<String, String> params = new HashMap<>();
        //图片地址
        if (!TextUtils.isEmpty(getImgPath(callBackPath))) {
            params.put("images", getImgPath(callBackPath));
        }

        //参与话题指定参数
        if (!TextUtils.isEmpty(topicId)) {
            params.put("topicId", topicId);
        }

        //帖子内容
        if (!TextUtils.isEmpty(content))
            params.put("content", content);

        //商品晒单指定参数
        if (scoreGoodsBean != null) {
            params.put("star", String.valueOf(mRatingBar.getNumStars()));
            params.put("orderProductId", scoreGoodsBean.getOrderProductId());
            params.put("productId", scoreGoodsBean.getProductId());
            params.put("orderNo", scoreGoodsBean.getOrderNo());
        }

        NetLoadUtils.getNetInstance().loadNetDataPost(this, !TextUtils.isEmpty(topicId) ? Url.JOIN_TOPIC : PUBLISH_POST_ANDE_VALUATE, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        if (!TextUtils.isEmpty(topicId)) {
                            //通知话题详情帖子列表刷新
                            String type = getIntent().getStringExtra("type");
                            EventBus.getDefault().post(new EventMessage(ConstantVariable.UPDATE_POST_CONTENT, new EventMessageBean(TopicDetailActivity.class.getSimpleName(), type)));
                            showToast(getActivity(), "发布成功!" + (requestStatus.getScore() > 0 ? getIntegralFormat(getActivity(), R.string.post_pass_get_score, requestStatus.getScore()) : ""));
                            finish();
                        } else {
                            //通知积分列表刷新
                            EventBus.getDefault().post(new EventMessage(ConstantVariable.UPDATE_SCORE_LIST, 0));
                            //跳转晒单成功页面
                            Intent intent = new Intent(getActivity(), JoinSuccessActivity.class);
                            intent.putExtra("score", requestStatus.getScore());
                            intent.putExtra("reminder", requestStatus.getReminder());
                            intent.putExtra("postId", requestStatus.getPostId());
                            intent.putExtra("digest", requestStatus.getDigest());
                            intent.putExtra("coverPath", requestStatus.getCoverPath());
                            intent.putExtra("topicTitle", requestStatus.getTopicTitle());
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        showToastRequestMsg(getActivity(), requestStatus);
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
                    showToast(this, "请输入发送内容");
                } else if (noSelectPic) {//纯文字
                    if (loadHud != null) {
                        loadHud.show();
                    }
                    sendData(null);
                } else {//带图片
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
                                //                            已上传不可删除 不可更换图片
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
                                showToast(getActivity(), "网络异常");
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
            //奖励规则
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
     * 确定退出弹窗
     */
    private void confirmExitAppraise() {
        if (scoreGoodsBean != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && imm.isActive()) {
                imm.hideSoftInputFromWindow(mTvLifeBack.getWindowToken(), 0); //强制隐藏键盘)
            }
            if (alertDialogHelper == null) {
                alertDialogHelper = new AlertDialogHelper(this);
                alertDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                        .setMsg("评价未完成，确定要离开吗？").setCancelText("取消").setConfirmText("确定")
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialogHelper != null && alertDialogHelper.getAlertDialog().isShowing()) {
            alertDialogHelper.dismiss();
        }
    }
}
