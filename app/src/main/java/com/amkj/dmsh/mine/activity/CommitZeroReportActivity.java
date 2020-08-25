package com.amkj.dmsh.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.bean.ZeroDetailEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.release.adapter.ImgGArticleRecyclerAdapter;
import com.amkj.dmsh.release.bean.ImagePathBean;
import com.amkj.dmsh.release.tutu.CustomMultipleFragment;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.ImageFormatUtils;
import com.amkj.dmsh.utils.ImgUrlHelp;
import com.amkj.dmsh.utils.TextWatchListener;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.pictureselector.PictureSelectorUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfigC;
import com.luck.picture.lib.entity.LocalMediaC;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.yanzhenjie.permission.Permission;

import org.lasque.tusdk.TuSdkGeeV1;
import org.lasque.tusdk.impl.components.TuEditMultipleComponent;
import org.lasque.tusdk.impl.components.edit.TuEditMultipleOption;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.emoji.widget.EmojiEditText;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_ADD_IMG;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.release.tutu.CameraComponentSample.DEFAULT_PATH;
import static com.amkj.dmsh.utils.ImageFormatUtils.getImageFormatInstance;

/**
 * Created by xiaoxin on 2020/8/25
 * Version:v4.7.0
 * ClassDescription :提交0元试用报告
 */
public class CommitZeroReportActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.tv_goods_name)
    TextView mTvGoodsName;
    @BindView(R.id.tv_max_reward)
    TextView mTvMaxReward;
    @BindView(R.id.tv_apply_num)
    TextView mTvApplyNum;
    @BindView(R.id.ll_score_goods)
    LinearLayout mLlScoreGoods;
    @BindView(R.id.rating_bar)
    MaterialRatingBar mRatingBar;
    @BindView(R.id.ll_ratingbar)
    LinearLayout mLlRatingbar;
    @BindView(R.id.tv_product_tips)
    TextView mTvProductTips;
    @BindView(R.id.et_input)
    EmojiEditText mEtInput;
    @BindView(R.id.rv_img_article)
    RecyclerView mRvImgArticle;
    @BindView(R.id.ll_editor)
    LinearLayout mLlEditor;
    @BindView(R.id.tv_commit_report)
    TextView mTvCommitReport;
    @BindView(R.id.nested_scrollview)
    NestedScrollView mNestedScrollview;

    // 获得存储图片的路径
    private String Img_PATH = null;
    private ImgGArticleRecyclerAdapter imgGArticleRecyclerAdapter;
    private List<ImagePathBean> imagePathBeans = new ArrayList<>();

    //已上传图片保存
    private List<String> updatedImages = new ArrayList<>();
    private int adapterPosition;
    private String topicId;
    private String activityId;
    private String orderId;
    private int maxSelImg = 9;
    private String content = "";
    private ZeroDetailEntity.ZeroDetailBean mZeroDetailBean;
    private ZeroDetailEntity mZeroDetailEntity;
    private String mStatus;

    @Override
    protected int getContentView() {
        return R.layout.activity_commit_zero_report;
    }

    @Override
    protected void initViews() {

        Intent intent = getIntent();
        activityId = intent.getStringExtra("activityId");
        orderId = intent.getStringExtra("orderId");
        mStatus = intent.getStringExtra("status");

        if (!TextUtils.isEmpty(activityId)) {//发布0元试用报告
            mTvHeaderTitle.setText("试用报告");
            mTvHeaderShared.setVisibility(View.GONE);
        } else {
            showToast("数据有误，请重试");
            finish();
            return;
        }

        File destDir = new File(getFilesDir().getAbsolutePath() + "/ImgArticle");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        if (imagePathBeans.size() < 1) {
            imagePathBeans.add(getImageFormatInstance().getDefaultAddImage());
        }

        //初始化图片列表
        imgGArticleRecyclerAdapter = new ImgGArticleRecyclerAdapter(this, imagePathBeans);
        mRvImgArticle.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mRvImgArticle.addItemDecoration(new ItemDecoration.Builder()
                .setDividerId(R.drawable.item_divider_img_white)
                .create());
        mRvImgArticle.setAdapter(imgGArticleRecyclerAdapter);
        imgGArticleRecyclerAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            //点击删除图片
            if (view.getId() == R.id.delete) {
                adapterPosition = (int) view.getTag();
                getImageFormatInstance().delImageBean(imagePathBeans, adapterPosition);
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

    private void pickImage(final int position) {
        ConstantMethod constantMethod = new ConstantMethod();
        constantMethod.setOnGetPermissionsSuccess(() -> {
            int imgLength = imagePathBeans.size() - 1;
            //点击+添加图片
            if (position == imgLength && imagePathBeans.get(imgLength).getPath().contains(DEFAULT_ADD_IMG)) {
                PictureSelectorUtils.getInstance()
                        .resetVariable()
                        .isCrop(false)
                        .selImageList(ImageFormatUtils.getImageFormatInstance().formatStringPathRemoveDefault(imagePathBeans))
                        .imageMode(PictureConfigC.MULTIPLE)
                        .isShowGif(true)
                        .openGallery(getActivity());
            } else {
                //点击图片编辑
                TinkerBaseApplicationLike tinkerApplicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
                tinkerApplicationLike.initTuSdk();
                // 组件委托
                TuEditMultipleComponent component = TuSdkGeeV1.editMultipleCommponent(this, (result, error, lastFragment) -> {
                    imagePathBeans.set(position, new ImagePathBean(result.imageSqlInfo.path, true));

                    imgGArticleRecyclerAdapter.notifyDataSetChanged();
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

    @Override
    protected void loadData() {
        getProductInfo();
        //获取原报告信息并修改
        if ("3".equals(mStatus)) {
            getReportContent();
        }
    }

    private void getReportContent() {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("activityId", activityId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_MY_REPORT, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);

                if (requestStatus != null) {
                    if (SUCCESS_CODE.equals(requestStatus.getCode())) {
                        RequestStatus.Result resultBean = requestStatus.getResult();
                        if (!TextUtils.isEmpty(resultBean.getContent())) {
                            mEtInput.setText(resultBean.getContent());
                            mEtInput.setSelection(resultBean.getContent().length());
                        }

                        if (!TextUtils.isEmpty(resultBean.getImgs())) {

                        }
                    }
                }

            }

            @Override
            public void onNotNetOrException() {
            }
        });
    }

    //获取商品信息
    private void getProductInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("activityId", activityId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_ZERO_PRODUCT_DETAIL, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mZeroDetailEntity = GsonUtils.fromJson(result, ZeroDetailEntity.class);
                if (mZeroDetailEntity != null) {
                    if (SUCCESS_CODE.equals(mZeroDetailEntity.getCode())) {
                        mZeroDetailBean = mZeroDetailEntity.getResult();
                        GlideImageLoaderUtil.loadCenterCrop(getActivity(), mIvCover, mZeroDetailBean.getProductImg());
                        mTvGoodsName.setText(mZeroDetailBean.getProductName());
                        mTvApplyNum.setText(getStringsFormat(getActivity(), R.string.apply_num, mZeroDetailBean.getPartakeCount()));
                    } else {
                        showToast(mZeroDetailEntity.getMsg());
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mZeroDetailBean, mZeroDetailEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mZeroDetailBean, mZeroDetailEntity);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IS_LOGIN_CODE:
                    loadData();
                    break;
                case PictureConfigC.CHOOSE_REQUEST:
                    List<LocalMediaC> localMediaList = PictureSelector.obtainMultipleResult(data);
                    if (localMediaList != null && localMediaList.size() > 0) {
                        imagePathBeans.clear();
                        for (LocalMediaC localMedia : localMediaList) {
                            if (!TextUtils.isEmpty(localMedia.getPath())) {
                                imagePathBeans.add(new ImagePathBean(localMedia.getPath(), true));
                            }
                        }
                        if (imagePathBeans.size() < maxSelImg) {
                            imagePathBeans.add(getImageFormatInstance().getDefaultAddImage());
                        }
                        imgGArticleRecyclerAdapter.notifyDataSetChanged();
//                        pickImage(size - 1);
                    }
                    break;
            }
        }
    }


    //提交内容
    private void publishPost(List<String> callBackPath) {
        Map<String, String> params = new HashMap<>();
        //图片地址
        params.put("imgs", getImgPath(callBackPath));

        //帖子内容
        if (!TextUtils.isEmpty(content)) {
            params.put("content", content);
        }

        params.put("star", String.valueOf(mRatingBar.getNumStars()));
        params.put("activityId", activityId);
        params.put("orderId", orderId);

        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.SUBMIT_ZERO_REPORT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast("提交成功");
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

    @OnClick({R.id.tv_life_back, R.id.tv_commit_report})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            case R.id.tv_commit_report:
                boolean noSelectPic = ImageFormatUtils.getImageFormatInstance().formatStringPathRemoveDefault(imagePathBeans).size() == 0;
                if (content.length() < 1 && noSelectPic) {
                    showToast("请输入发送内容");
                } else if (noSelectPic) {//发布报告图片必传
                    showToast("请添加图片");
                } else {//带图片
                    if (loadHud != null) {
                        loadHud.show();
                    }
                    if (updatedImages.size() > 0) {
                        publishPost(updatedImages);
                    } else {
                        ImgUrlHelp imgUrlHelp = new ImgUrlHelp();
                        imgUrlHelp.setUrl(getActivity(), ImageFormatUtils.getImageFormatInstance().formatStringPathRemoveDefault(imagePathBeans));
                        imgUrlHelp.setOnFinishListener(new ImgUrlHelp.OnFinishDataListener() {
                            @Override
                            public void finishData(@NonNull List<String> data, Handler handler) {
                                showToast(data.size() + "张图片上传成功");
                                updatedImages.clear();
                                updatedImages.addAll(data);
                                publishPost(data);
                                handler.removeCallbacksAndMessages(null);
                            }

                            @Override
                            public void finishError(String error) {
                                updatedImages.clear();
                                if (loadHud != null) {
                                    loadHud.dismiss();
                                }
                                showToast("网络异常");
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
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mNestedScrollview;
    }
}
