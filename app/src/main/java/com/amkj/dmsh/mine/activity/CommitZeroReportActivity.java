package com.amkj.dmsh.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.release.adapter.ReportImgAdapter;
import com.amkj.dmsh.release.tutu.CustomMultipleFragment;
import com.amkj.dmsh.utils.CommonUtils;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.emoji.widget.EmojiEditText;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_ADD_IMG;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.release.tutu.CameraComponentSample.DEFAULT_PATH;

/**
 * Created by xiaoxin on 2020/8/25
 * Version:v4.7.0
 * ClassDescription :0元订单列表-提交/修改报告
 */
public class CommitZeroReportActivity extends BaseActivity {

    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.tv_goods_name)
    TextView mTvGoodsName;
    @BindView(R.id.ll_score_goods)
    RelativeLayout mLlScoreGoods;
    @BindView(R.id.rating_bar)
    MaterialRatingBar mRatingBar;
    @BindView(R.id.et_input)
    EmojiEditText mEtInput;
    @BindView(R.id.rv_img_article)
    RecyclerView mRvImgArticle;
    @BindView(R.id.ll_editor)
    LinearLayout mLlEditor;
    @BindView(R.id.nested_scrollview)
    NestedScrollView mNestedScrollview;
    private ReportImgAdapter mReportImgAdapter;
    private List<String> imagePathBeans = new ArrayList<>();
    private String activityId;
    private String orderId;
    private String mStatus;
    private int maxSelImg = 9;
    private String content = "";
    private RequestStatus mRequestStatus;
    private RequestStatus.Result mResultBean;

    @Override
    protected int getContentView() {
        return R.layout.activity_commit_zero_report;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mStatus = getIntent().getStringExtra("status");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        activityId = intent.getStringExtra("activityId");
        orderId = intent.getStringExtra("orderId");
        String productName = intent.getStringExtra("productName");
        String productImg = intent.getStringExtra("productImg");

        if (!TextUtils.isEmpty(activityId)) {//发布0元试用报告
            mTvHeaderTitle.setText("试用报告");
            mTvHeaderShared.setVisibility(View.GONE);
        } else {
            showToast("数据有误，请重试");
            finish();
        }

        GlideImageLoaderUtil.loadCenterCrop(getActivity(), mIvCover, productImg);
        mTvGoodsName.setText(getStrings(productName));
        //初始化图片列表
        File destDir = new File(getFilesDir().getAbsolutePath() + "/ImgArticle");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        if (imagePathBeans.size() < 1) {
            imagePathBeans.add(DEFAULT_ADD_IMG);
        }
        mReportImgAdapter = new ReportImgAdapter(this, imagePathBeans);
        mRvImgArticle.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mRvImgArticle.addItemDecoration(new ItemDecoration.Builder()
                .setDividerId(R.drawable.item_divider_img_white)
                .create());
        mRvImgArticle.setAdapter(mReportImgAdapter);
        mReportImgAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            //点击删除图片
            if (view.getId() == R.id.delete) {
                imagePathBeans.remove(position);
                if (!imagePathBeans.contains(DEFAULT_ADD_IMG)) {
                    imagePathBeans.add(DEFAULT_ADD_IMG);
                }
                mReportImgAdapter.notifyDataSetChanged();
            }
        });
        mReportImgAdapter.setOnItemClickListener((adapter, view, position) -> {
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
            //点击+添加图片
            if (DEFAULT_ADD_IMG.equals(imagePathBeans.get(position))) {
                PictureSelectorUtils.getInstance()
                        .resetVariable()
                        .isCrop(false)
                        .imageMode(PictureConfigC.MULTIPLE)
                        .isShowGif(true)
                        .setMaxNum(maxSelImg - imagePathBeans.size() + 1)
                        .openGallery(getActivity());
            } else {
                //点击图片编辑（可能是本地图片，也可能是网络url）
                GlideImageLoaderUtil.setLoadImgFinishListener(this, imagePathBeans.get(position), new GlideImageLoaderUtil.ImageLoaderFinishListener() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        TinkerBaseApplicationLike tinkerApplicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
                        tinkerApplicationLike.initTuSdk();
                        // 组件委托
                        TuEditMultipleComponent component = TuSdkGeeV1.editMultipleCommponent(getActivity(), (result, error, lastFragment) -> {
                            imagePathBeans.set(position, result.imageSqlInfo.path);
                            mReportImgAdapter.notifyDataSetChanged();
                        });
                        component.componentOption().editMultipleOption().setComponentClazz(CustomMultipleFragment.class);// 指定控制器类型
                        component.componentOption().editMultipleOption().setRootViewLayoutId(R.layout.tusdk_impl_component_edit_multiple_fragment); // 指定根视图资源ID
                        // 设置图片
                        TuEditMultipleOption option = component.componentOption().editMultipleOption();
                        option.setSaveToAlbumName(DEFAULT_PATH);
                        option.setSaveToAlbum(true);
                        option.setAutoRemoveTemp(true);
                        component.setImage(bitmap)
                                // 在组件执行完成后自动关闭组件
                                .setAutoDismissWhenCompleted(true)
                                // 开启组件
                                .showComponent();
                    }

                    @Override
                    public void onError() {
                    }
                });
            }
        });
        constantMethod.getPermissions(this, Permission.Group.STORAGE);
    }

    //获取需要上传的本地图片
    private List<String> getLocalPaths(List<String> imagePathBeans) {
        List<String> newDatas = new ArrayList<>();
        for (String path : imagePathBeans) {
            if (!DEFAULT_ADD_IMG.equals(path) && !path.contains("http")) {
                newDatas.add(path);
            }
        }

        return newDatas;
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
                        imagePathBeans.remove(DEFAULT_ADD_IMG);
                        for (LocalMediaC localMedia : localMediaList) {
                            if (!TextUtils.isEmpty(localMedia.getPath())) {
                                imagePathBeans.add(localMedia.getPath());
                            }
                        }
                        pickImage(imagePathBeans.size() - 1);//编辑最后一张图片
                        if (imagePathBeans.size() < maxSelImg) {
                            imagePathBeans.add(DEFAULT_ADD_IMG);
                        }
                        mReportImgAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }


    //提交内容
    private void publishPost() {
        Map<String, String> params = new HashMap<>();
        //图片地址
        params.put("imgs", getImgPath(getUploadPaths()));

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


    //集合拼接字符串
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
                boolean noSelectPic = (imagePathBeans.size() < 1) || (imagePathBeans.size() < 2 && DEFAULT_ADD_IMG.equals(imagePathBeans.get(0)));
                if (content.length() < 50) {
                    showToast("请至少输入50字");
                } else if (noSelectPic) {
                    showToast("请添加图片");
                } else {//带图片
                    if (loadHud != null) {
                        loadHud.show();
                    }

                    //所有图片已上传
                    if (getLocalPaths(imagePathBeans).size() == 0) {
                        publishPost();
                    } else {
                        ImgUrlHelp imgUrlHelp = new ImgUrlHelp();
                        imgUrlHelp.setUrl(getActivity(), getLocalPaths(imagePathBeans));
                        imgUrlHelp.setOnFinishListener(new ImgUrlHelp.OnFinishDataListener() {
                            @Override
                            public void finishData(@NonNull List<String> data, Handler handler) {
//                                showToast(data.size() + "张图片上传成功");
                                replaceLocalPaths(data);
                                publishPost();
                                handler.removeCallbacksAndMessages(null);
                            }

                            @Override
                            public void finishError(String error) {
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

    //使用图片url替换本地图片
    private void replaceLocalPaths(List<String> urls) {
        int j = 0;
        for (int i = 0; i < imagePathBeans.size(); i++) {
            String path = imagePathBeans.get(i);
            if (!path.contains("http")) {
                imagePathBeans.set(i, urls.get(j));
                j++;
                if (j >= urls.size()) break;
            }
        }
    }

    //获取已上传的图片
    private List<String> getUploadPaths() {
        List<String> newDatas = new ArrayList<>();
        for (String path : imagePathBeans) {
            if (!DEFAULT_ADD_IMG.equals(path) && path.contains("http")) {
                newDatas.add(path);
            }
        }

        return newDatas;
    }


    @Override
    protected boolean isAddLoad() {
        return "3".equals(mStatus);
    }

    @Override
    public View getLoadView() {
        return mNestedScrollview;
    }

    @Override
    protected void loadData() {
        //获取原报告信息并修改
        if ("3".equals(mStatus)) {
            getMyReport();
        }
    }

    private void getMyReport() {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("activityId", activityId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_MY_REPORT, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mRequestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (mRequestStatus != null) {
                    if (SUCCESS_CODE.equals(mRequestStatus.getCode())) {
                        mResultBean = mRequestStatus.getResult();
                        if (!TextUtils.isEmpty(mResultBean.getContent())) {
                            mEtInput.setText(mResultBean.getContent());
                            mEtInput.setSelection(mResultBean.getContent().length());
                        }

                        if (!TextUtils.isEmpty(mResultBean.getImgs())) {
                            imagePathBeans.clear();
                            imagePathBeans.addAll(Arrays.asList(mResultBean.getImgs().split(",")));
                            if (imagePathBeans.size() < maxSelImg) {
                                imagePathBeans.add(DEFAULT_ADD_IMG);
                            }
                        }
                    }
                }

                mReportImgAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mResultBean, mRequestStatus);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mResultBean, mRequestStatus);
            }
        });
    }
}
