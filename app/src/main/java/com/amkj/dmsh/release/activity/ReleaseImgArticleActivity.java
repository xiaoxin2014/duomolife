package com.amkj.dmsh.release.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.ReplaceData;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.find.bean.InvitationImgDetailEntity;
import com.amkj.dmsh.release.adapter.AddRelevanceProAdapter;
import com.amkj.dmsh.release.adapter.ImgGArticleRecyclerAdapter;
import com.amkj.dmsh.release.bean.ImagePathBean;
import com.amkj.dmsh.release.bean.IndentOrderProBean;
import com.amkj.dmsh.release.bean.IndentOrderProBean.FindTopicBean;
import com.amkj.dmsh.release.bean.IndentOrderProBean.ProductsBean;
import com.amkj.dmsh.release.bean.RelevanceProEntity;
import com.amkj.dmsh.release.bean.RelevanceProEntity.RelevanceProBean;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.FileStreamUtils;
import com.amkj.dmsh.utils.ImgUrlHelp;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.utils.pictureselector.PictureSelectorUtils;
import com.amkj.dmsh.utils.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.amkj.dmsh.views.ReleaseEditView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfigC;
import com.luck.picture.lib.entity.LocalMediaC;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;
import org.lasque.tusdk.TuSdkGeeV1;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.TuEditMultipleComponent;
import org.lasque.tusdk.impl.components.edit.TuEditMultipleOption;
import org.lasque.tusdk.modules.components.TuSdkComponent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_ADD_IMG;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.RELEVANCE_PRO_REQ;
import static com.amkj.dmsh.release.tutu.CameraComponentSample.DEFAULT_PATH;
import static com.amkj.dmsh.utils.ImageFormatUtils.getImageFormatInstance;

;

public class ReleaseImgArticleActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    @BindView(R.id.release_et_input)
    ReleaseEditView release_et_input;
    //    商品图片
    @BindView(R.id.rv_img_article)
    RecyclerView rv_img_article;
    //    关联商品
    @BindView(R.id.communal_recycler_wrap)
    RecyclerView communal_recycler_wrap;
    @BindView(R.id.rel_relevance_product_header)
    RelativeLayout rel_relevance_product_header;
    //    发送
    @BindView(R.id.tv_find_release_topic)
    TextView tv_find_release_topic;
    // 获得存储图片的路径
    private String Img_PATH = null;
    private ImgGArticleRecyclerAdapter imgGArticleRecyclerAdapter;
    private List<ImagePathBean> imagePathBeans = new ArrayList<>();
    //图片路径
    private ArrayList<String> mSelectPath = new ArrayList();

    //    已上传图片保存
    private List<String> updatedImages = new ArrayList<>();
    private int adapterPosition;
    private final int REQUEST_IMG = 80;
    private AddRelevanceProAdapter addRelevanceProAdapter;
    //    关联商品列表
    private List<RelevanceProBean> relevanceProList = new ArrayList<>();
    //    已选择关联商品数据
    private List<RelevanceProBean> selectRelevanceProList = new ArrayList<>();
    private String topicId;
    private String topicTitle = "";
    private String orderNo;
    private int maxSelImg = 9;

    @Override
    protected int getContentView() {
        return R.layout.activity_release_img_article;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tl_normal_bar.setSelected(true);
        try {
            Intent intent = getIntent();
            topicId = intent.getStringExtra("topicId");
            String topicTitle = intent.getStringExtra("topicTitle");
            String topicHint = intent.getStringExtra("topicHint");
            if (!TextUtils.isEmpty(topicId) && !TextUtils.isEmpty(topicTitle)) {
                initTopicTitle(topicTitle, topicHint);
            }
            orderNo = intent.getStringExtra("orderNo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Img_PATH = getFilesDir().getAbsolutePath() + "/ImgArticle";
        createSaveImgFile();
        if (imagePathBeans.size() < 1) {
            imagePathBeans.add(getImageFormatInstance().getDefaultAddImage());
        }
        if (!TextUtils.isEmpty(topicId)) {
            tv_header_titleAll.setText("参与话题");
        } else {
            tv_header_titleAll.setText("发布帖子");
        }
        tv_find_release_topic.setText("发送");
        header_shared.setVisibility(View.GONE);
        imgGArticleRecyclerAdapter = new ImgGArticleRecyclerAdapter(this, imagePathBeans);
        rv_img_article.setLayoutManager(new GridLayoutManager(ReleaseImgArticleActivity.this, 3));
        rv_img_article.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_img_white)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        rv_img_article.setAdapter(imgGArticleRecyclerAdapter);
        imgGArticleRecyclerAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.delete) {
                    adapterPosition = (int) view.getTag();
                    getImageFormatInstance().delImageBean(imagePathBeans, adapterPosition);
                    setSelectImageData();
                    imgGArticleRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });
        imgGArticleRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                隐藏键盘
                if (((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).isActive()) {
                    CommonUtils.hideSoftInput(ReleaseImgArticleActivity.this, release_et_input);
                }
                pickImage(position);
            }
        });
//        关联商品
        communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(ReleaseImgArticleActivity.this, LinearLayoutManager.HORIZONTAL, false));
        addRelevanceProAdapter = new AddRelevanceProAdapter(ReleaseImgArticleActivity.this, relevanceProList);
        communal_recycler_wrap.setAdapter(addRelevanceProAdapter);
        communal_recycler_wrap.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_img_white)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        addRelevanceProAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                RelevanceProBean relevanceProBean = (RelevanceProBean) view.getTag(R.id.iv_tag);
                if (relevanceProBean == null) {
                    relevanceProBean = (RelevanceProBean) view.getTag();
                }
                if (relevanceProBean != null) {
                    switch (view.getId()) {
                        case R.id.iv_release_product_img:
                        case R.id.iv_release_product_img_sel:
                            relevanceProBean.setSelPro(!relevanceProBean.isSelPro());
                            if (relevanceProBean.isSelPro()) {
                                if (selectRelevanceProList.size() < 5) {
                                    selectRelevanceProList.add(relevanceProBean);
                                } else {
                                    showToast(ReleaseImgArticleActivity.this, R.string.relevance_pro_more);
                                    return;
                                }
                            } else {
                                for (RelevanceProBean relevancePro : selectRelevanceProList) {
                                    if (relevancePro.getId() == relevanceProBean.getId()) {
                                        selectRelevanceProList.remove(relevancePro);
                                        break;
                                    }
                                }
                            }
                            setRelevanceProduct(relevanceProBean);
                            break;
                    }
                }
            }
        });
        tv_find_release_topic.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tv_find_release_topic.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                float cornerRadii = tv_find_release_topic.getMeasuredHeight() / 2f;
                GradientDrawable drawable = new GradientDrawable();
                drawable.setCornerRadius(cornerRadii);
                drawable.setColor(0xffdaeeff);
                tv_find_release_topic.setBackground(drawable);
            }
        });
    }

    private void setRelevanceProduct(RelevanceProBean relevanceProBean) {
        int itemPosition = 0;
        for (int i = 0; i < relevanceProList.size(); i++) {
            RelevanceProBean relevancePro = relevanceProList.get(i);
            if (relevancePro.getId() == relevanceProBean.getId()) {
                itemPosition = i;
                break;
            }
        }
        relevanceProList.set(itemPosition, relevanceProBean);
        addRelevanceProAdapter.notifyItemChanged(itemPosition);
    }

    /**
     * 初始化主题
     *
     * @param topicName
     * @param topicHint
     */
    private void initTopicTitle(String topicName, String topicHint) {
        topicTitle = String.format(getResources().getString(R.string.topic_format), getStrings(topicName));
//        CharSequence stringText = setLinkString(topicTitle, topicTitle);
        release_et_input.setHint(getStrings(topicHint));
    }
//
//    private CharSequence setLinkString(String topicTitle, String content) {
//        CharSequence stringText = constantMethod.topicFormat(ReleaseImgArticleActivity.this, topicTitle, content);
//        release_et_input.setText(stringText);
//        return stringText;
//    }

    private void createSaveImgFile() {
        File destDir = new File(Img_PATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    private void pickImage(final int position) {
        ConstantMethod constantMethod = new ConstantMethod();
        constantMethod.setOnGetPermissionsSuccess(new ConstantMethod.OnGetPermissionsSuccessListener() {
            @Override
            public void getPermissionsSuccess() {
                int imgLength = imagePathBeans.size() - 1;
                if (position == imgLength && imagePathBeans.get(imgLength).getPath()
                        .contains(DEFAULT_ADD_IMG)) {
                    PictureSelectorUtils.getInstance()
                            .resetVariable()
                            .isCrop(false)
                            .selImageList(mSelectPath)
                            .imageMode(PictureConfigC.MULTIPLE)
                            .isShowGif(true)
                            .openGallery(ReleaseImgArticleActivity.this);
                } else {
                    TuSdkComponent.TuSdkComponentDelegate delegate = new TuSdkComponent.TuSdkComponentDelegate() {
                        @Override
                        public void onComponentFinished(TuSdkResult result, Error error, TuFragment lastFragment) {
                            ReplaceData path = new ReplaceData();
                            try {
                                path.setPosition(position);
                                path.setPath(result.imageSqlInfo.path);
                                EventBus.getDefault().post(new EventMessage("replace", path));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    // 组件选项配置
                    TuEditMultipleComponent component = TuSdkGeeV1.editMultipleCommponent(ReleaseImgArticleActivity.this, delegate);
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
            CommonUtils.hideSoftInput(ReleaseImgArticleActivity.this, release_et_input);
        }
    }

    private void setSelectImageData() {
        mSelectPath.clear();
        mSelectPath.addAll(getImageFormatInstance().formatStringPathRemoveDefault(imagePathBeans));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            } else {
                return;
            }
        }
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
                        if (imagePathBeans.size() < maxSelImg) {
                            imagePathBeans.add(getImageFormatInstance().getDefaultAddImage());
                        }
                        setSelectImageData();
                        imgGArticleRecyclerAdapter.notifyDataSetChanged();
                    }
                    break;
                case RELEVANCE_PRO_REQ:
                    List<RelevanceProBean> relevanceList = data.getExtras().getParcelableArrayList("relevanceList");
                    if (relevanceList != null) {
                        selectRelevanceProList.clear();
                        selectRelevanceProList.addAll(relevanceList);
                        insertSelectSortProduct();
                        addRelevanceProAdapter.notifyItemRangeChanged(0, relevanceProList.size());
                    }
                    break;
            }
        }
    }

    /**
     * 插入已选择的商品
     */
    private void insertSelectSortProduct() {
        List<RelevanceProBean> transfersList = new ArrayList<>();
        transfersList.addAll(selectRelevanceProList);
        for (RelevanceProBean relevanceProBean : relevanceProList) {
            boolean isAdd = true;
            for (RelevanceProBean relevancePro : selectRelevanceProList) {
                if (relevanceProBean.getId() == relevancePro.getId()) {
                    isAdd = false;
                    break;
                } else {
                    isAdd = true;
                }
            }
            if (isAdd) {
                relevanceProBean.setSelPro(false);
                transfersList.add(relevanceProBean);
            }
        }
        relevanceProList.removeAll(transfersList);
        relevanceProList.clear();
        relevanceProList.addAll(transfersList);
    }

    @OnClick(R.id.tv_find_release_topic)
    void sendMessage(View view) {
        Editable text = release_et_input.getText();
        if (text == null) {
            showToast(this, "请输入发送内容");
            return;
        }
        if (text.toString().trim().length() < 1
                && (imagePathBeans.size() < 1 || (imagePathBeans.size() < 2 && imagePathBeans.get(0).equals(DEFAULT_ADD_IMG)))) {
            showToast(this, "请输入发送内容");
        } else {
            if (loadHud != null) {
                loadHud.show();
            }
            final String content = release_et_input.getText().toString();
            tv_find_release_topic.setText("发送中...");
            tv_find_release_topic.setEnabled(false);
            if (updatedImages.size() > 0) {
                sendData(updatedImages, content);
            } else {
                ImgUrlHelp imgUrlHelp = new ImgUrlHelp();
                imgUrlHelp.setUrl(ReleaseImgArticleActivity.this, mSelectPath);
                imgUrlHelp.setOnFinishListener(new ImgUrlHelp.OnFinishDataListener() {
                    @Override
                    public void finishData(@NonNull List<String> data, Handler handler) {
                        updatedImages.clear();
                        updatedImages.addAll(data);
                        //                            已上传不可删除 不可更换图片
                        getImageFormatInstance().submitChangeIconStatus(imagePathBeans, false);
                        imgGArticleRecyclerAdapter.notifyDataSetChanged();
                        sendData(data, content);
                        handler.removeCallbacksAndMessages(null);
                    }

                    @Override
                    public void finishError(String error) {
                        if (loadHud != null) {
                            loadHud.dismiss();
                        }
                        header_shared.setText("发送");
                        header_shared.setEnabled(true);
                        showToast(ReleaseImgArticleActivity.this, "网络异常");
                    }

                    @Override
                    public void finishSingleImg(String singleImg, Handler handler) {
                    }
                });
            }
        }
    }

    private void sendData(List<String> callBackPath, String content) {
        //图片地址
        StringBuffer imgPath = new StringBuffer();
        String url = Url.BASE_URL + Url.F_SEND_INVITATION;
        Map<String, Object> params = new HashMap<>();
        //用户Id
        params.put("fuid", userId);
        //图片地址
        if (callBackPath != null) {
            for (int i = 0; i < callBackPath.size(); i++) {
                if (i == 0) {
                    imgPath.append(callBackPath.get(i));
                } else {
                    imgPath.append("," + callBackPath.get(i));
                }
            }
            params.put("photographurl", imgPath);
        }
        if (selectRelevanceProList.size() > 0) {
            StringBuffer proString = new StringBuffer();
            for (int i = 0; i < (selectRelevanceProList.size() > 5 ? 5 : selectRelevanceProList.size()); i++) {
                int skuId = selectRelevanceProList.get(i).getId();
                if (skuId > 0) {
                    if (i == 0) {
                        proString.append(String.valueOf(skuId));
                    } else {
                        proString.append("," + String.valueOf(skuId));
                    }
                }
            }
            params.put("objectList", proString.toString());
        }

        if (!TextUtils.isEmpty(topicTitle) && !TextUtils.isEmpty(topicId)/* && content.contains(topicTitle)
                && topicTitle.equals(content.substring(0, topicTitle.length()))*/) {
            params.put("topic_id", topicId);
            content = topicTitle + content;
        }
        params.put("description", content);
        if (!TextUtils.isEmpty(orderNo)) {
            params.put("productOrderNo", orderNo);
        }
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                InvitationImgDetailEntity invitationDetailEntity = gson.fromJson(result, InvitationImgDetailEntity.class);
                if (invitationDetailEntity != null) {
                    if (invitationDetailEntity.getCode().equals("01")) {
                        showToast(ReleaseImgArticleActivity.this, "发布完成");
                        tv_find_release_topic.setText("已发送");
                        tv_find_release_topic.setEnabled(true);
                        finish();
                    } else {
                        showToast(ReleaseImgArticleActivity.this, invitationDetailEntity.getMsg());
                        tv_find_release_topic.setEnabled(true);
                        header_shared.setText("发送");
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                tv_find_release_topic.setEnabled(true);
                header_shared.setText("发送");
                super.onError(ex, isOnCallback);
            }
        });
    }

    @Override
    protected void loadData() {
        if (!TextUtils.isEmpty(orderNo)) {
            getIndentProData();
        } else {
            getRelevanceProduct();
        }
    }

    private void getRelevanceProduct() {
        if (userId < 1) {
            return;
        }
        String url = Url.BASE_URL + Url.RELEASE_RELEVANCE_PRODUCT;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(@NonNull String result) {
                relevanceProList.clear();
                RelevanceProEntity relevanceProEntity = RelevanceProEntity.objectFromData(result);
                if (relevanceProEntity != null) {
                    String backCode = relevanceProEntity.getCode();
                    if (backCode.equals("01")) {
                        if (relevanceProEntity.getRelevanceProList() != null
                                && relevanceProEntity.getRelevanceProList().size() > 0) {
                            relevanceProList.addAll(relevanceProEntity.getRelevanceProList());
                            addRelevanceProAdapter.notifyItemRangeChanged(0, relevanceProList.size());
                            rel_relevance_product_header.setVisibility(View.VISIBLE);
                            communal_recycler_wrap.setVisibility(View.VISIBLE);
                        }
                    } else {
                        relevanceDataException();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                relevanceDataException();
            }
        });
    }

    private void relevanceDataException() {
        communal_recycler_wrap.setVisibility(View.GONE);
        rel_relevance_product_header.setVisibility(View.GONE);
        showToast(ReleaseImgArticleActivity.this, "商品数据异常！");
    }

    /**
     * 获取订单数据
     */
    private void getIndentProData() {
        if (userId < 1) {
            return;
        }
        String url = Url.BASE_URL + Url.F_REL_INDENT_PRO_LIST;
        Map<String, Object> params = new HashMap<>();
        params.put("productOrderNo", orderNo);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                IndentOrderProBean indentOrderProBean = IndentOrderProBean.objectFromData(result);
                if (indentOrderProBean != null) {
                    String backCode = indentOrderProBean.getCode();
                    if (backCode.equals("01")) {
                        if (indentOrderProBean.getProductList() != null && indentOrderProBean.getProductList().size() > 0) {
                            RelevanceProBean relevanceProBean;
                            for (ProductsBean productsBean : indentOrderProBean.getProductList()) {
                                relevanceProBean = new RelevanceProBean();
                                relevanceProBean.setId(productsBean.getId());
                                relevanceProBean.setProductId(productsBean.getProductId());
                                relevanceProBean.setTitle(productsBean.getTitle());
                                relevanceProBean.setPrice(productsBean.getPrice());
                                relevanceProBean.setPictureUrl(productsBean.getPictureUrl());
                                relevanceProBean.setSelPro(true);
                                relevanceProList.add(relevanceProBean);
                            }
                            addRelevanceProAdapter.notifyDataSetChanged();
                        }
                        setReleaseData(indentOrderProBean);
                    } else {
                        relevanceDataException();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                relevanceDataException();
            }
        });
    }

    /**
     * 设置发布内容
     *
     * @param indentOrderProBean
     */
    private void setReleaseData(IndentOrderProBean indentOrderProBean) {
//        主题发布
        if (indentOrderProBean.getFindTopicBean() != null) {
            FindTopicBean findTopicBean = indentOrderProBean.getFindTopicBean();
            topicId = String.valueOf(findTopicBean.getId());
            String topicTitle = getStrings(findTopicBean.getTitle());
            if (!TextUtils.isEmpty(topicId) && !TextUtils.isEmpty(topicTitle)) {
                initTopicTitle(topicTitle, findTopicBean.getReminder());
            }
        }
        if (!TextUtils.isEmpty(indentOrderProBean.getImgs())) {
            String[] images = indentOrderProBean.getImgs().split(",");
            for (int i = 0; i < images.length; i++) {
                downSaveImage(images[i]);
            }
        }
    }

    private void downSaveImage(final String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            String topicSavePath = Environment.getExternalStorageDirectory().getPath() + "/camera";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                createFilePath(topicSavePath);
                topicSavePath = topicSavePath + "/" + imageUrl.substring(imageUrl.lastIndexOf("/"));
                if (!fileIsExist(topicSavePath)) {
                    final String finalTopicSavePath = topicSavePath;
                    GlideImageLoaderUtil.downOriginalImg(ReleaseImgArticleActivity.this, imageUrl, new GlideImageLoaderUtil.OriginalLoaderFinishListener() {
                        @Override
                        public void onSuccess(File file) {
                            try {
                                if (FileStreamUtils.forChannel(file, new File(finalTopicSavePath))) {
                                    if (imagePathBeans.size() > 0) {
                                        String pathString = imagePathBeans.get(imagePathBeans.size() - 1).getPath();
                                        if (pathString.equals(DEFAULT_ADD_IMG)) {
                                            imagePathBeans.remove(imagePathBeans.size() - 1);
                                        }
                                    }
                                    imagePathBeans.add(new ImagePathBean(finalTopicSavePath, true));
                                    if (imagePathBeans.size() < maxSelImg) {
                                        imagePathBeans.add(getImageFormatInstance().getDefaultAddImage());
                                    }
                                    setSelectImageData();
                                    imgGArticleRecyclerAdapter.notifyDataSetChanged();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError() {
                        }
                    });
                } else {
                    if (imagePathBeans.size() > 0) {
                        String pathString = imagePathBeans.get(imagePathBeans.size() - 1).getPath();
                        if (pathString.equals(DEFAULT_ADD_IMG)) {
                            imagePathBeans.remove(imagePathBeans.size() - 1);
                        }
                    }
                    imagePathBeans.add(new ImagePathBean(topicSavePath, true));
                    if (imagePathBeans.size() < 9) {
                        imagePathBeans.add(getImageFormatInstance().getDefaultAddImage());
                    }
                    setSelectImageData();
                    imgGArticleRecyclerAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    //判断文件是否存在
    private boolean fileIsExist(String invoiceSavePath) {
        File file = new File(invoiceSavePath);
        return file.exists();
    }

    //创建文件夹
    private void createFilePath(String savePath) {
        File destDir = new File(savePath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    //        关联商品
    @OnClick(R.id.tv_release_add_good)
    void relevancePro(View view) {
        Intent intent = new Intent(ReleaseImgArticleActivity.this, RelevanceProListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("relevanceList", (ArrayList<? extends Parcelable>) selectRelevanceProList);
        intent.putExtras(bundle);
        startActivityForResult(intent, RELEVANCE_PRO_REQ);
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }
}
