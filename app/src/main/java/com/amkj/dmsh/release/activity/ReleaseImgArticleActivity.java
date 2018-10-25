package com.amkj.dmsh.release.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.find.bean.InvitationImgDetailEntity;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.release.adapter.AddRelevanceProAdapter;
import com.amkj.dmsh.release.adapter.ImgGArticleRecyclerAdapter;
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
import com.amkj.dmsh.views.ReleaseEditView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfigC;
import com.luck.picture.lib.entity.LocalMediaC;
import com.amkj.dmsh.utils.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
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

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.RELEVANCE_PRO_REQ;
import static com.amkj.dmsh.release.adapter.ImgGridRecyclerAdapter.DEFAULT_ADD_IMG;
import static com.amkj.dmsh.release.tutu.CameraComponentSample.DEFAULT_PATH;

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
    private ArrayList<String> dataPath = new ArrayList<>();
    //图片路径
    private ArrayList<String> mSelectPath = new ArrayList();
    private int uid;
    private int adapterPosition;
    private final int REQUEST_IMG = 80;
    private AddRelevanceProAdapter addRelevanceProAdapter;
    //    关联商品列表
    private List<RelevanceProBean> relevanceProList = new ArrayList<>();
    //    已选择关联商品数据
    private List<RelevanceProBean> selectRelevanceProList = new ArrayList<>();
    private String topicId;
    private String topicTitle = "";
    private ConstantMethod constantMethod;
    private String orderNo;

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
        getLoginStatus();
        constantMethod = new ConstantMethod();
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
        this.dataPath.add(DEFAULT_ADD_IMG);
        if (!TextUtils.isEmpty(topicId)) {
            tv_header_titleAll.setText("参与话题");
        } else {
            tv_header_titleAll.setText("发布帖子");
        }
        tv_find_release_topic.setText("发送");
        header_shared.setVisibility(View.GONE);
        imgGArticleRecyclerAdapter = new ImgGArticleRecyclerAdapter(this, dataPath);
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
                    if (dataPath.size() > dataPath.size() - 1 && !dataPath.get(dataPath.size() - 1).equals(DEFAULT_ADD_IMG)) {
                        dataPath.set(dataPath.size() - 1, DEFAULT_ADD_IMG);
                    } else {
                        dataPath.remove(adapterPosition);
                        if (!dataPath.get(dataPath.size() - 1).equals(DEFAULT_ADD_IMG)) {
                            dataPath.add(DEFAULT_ADD_IMG);
                        }
                    }
                    setSelectImageData();
                    imgGArticleRecyclerAdapter.setNewData(dataPath);
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
//        rv_rel_topic.setLayoutManager(new GridLayoutManager(this, 3));
//        rv_rel_topic.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
//                // 设置分隔线资源ID
//                .setDividerId(R.drawable.item_divider_nine_dp_white)
//                // 开启绘制分隔线，默认关闭
//                .enableDivider(true)
//                // 是否关闭标签点击事件，默认开启
//                .disableHeaderClick(false)
//                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
//                .setHeaderClickListener(null)
//                .create());
//        findRelTopicAdapter = new FindRelTopicAdapter(relevanceTopicList);
//        rv_rel_topic.setAdapter(findRelTopicAdapter);
//        findRelTopicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                TextView tv_title = (TextView) view.getTag();
//                if (tv_title != null) {
//                    FindRelTopicBean topicBean = (FindRelTopicBean) tv_title.getTag();
//                    if (topicBean != null) {
//                        String relText = release_et_input.getText().toString();
//                        if (!TextUtils.isEmpty(relText) && !TextUtils.isEmpty(topicTitle)
//                                && relText.contains(topicTitle)) {
//                            if (!topicBean.isSelect()) {
//                                String topicTitle = getStrings(topicBean.getTitle());
//                                relText = relText.replace(ReleaseImgArticleActivity.this.topicTitle, topicTitle);
//                                setLinkData(topicTitle, relText);
//                            } else {
//                                relText = relText.replace(ReleaseImgArticleActivity.this.topicTitle, "");
//                                release_et_input.setText(relText);
//                            }
//                        } else {
//                            if (!topicBean.isSelect()) {
//                                String topicTitle = getStrings(topicBean.getTitle());
//                                String subText = insertText(topicTitle, relText);
//                                setLinkData(topicTitle, subText);
//                            }
//                        }
//                        setEtTextSelection();
//                        if (!topicTitle.equals(getStrings(topicBean.getTitle()))
//                                && !topicBean.isSelect()) {
//                            topicTitle = topicBean.getTitle();
//                            topicId = String.valueOf(topicBean.getId());
//                        }
//                        for (int i = 0; i < relevanceTopicList.size(); i++) {
//                            FindRelTopicBean findRelTopicBean = relevanceTopicList.get(i);
//                            if (i == topicBean.getPosition()) {
//                                findRelTopicBean.setSelect(!topicBean.isSelect());
//                            } else {
//                                findRelTopicBean.setSelect(false);
//                            }
//                        }
//                        findRelTopicAdapter.notifyDataSetChanged();
//                    }
//                }
//            }
//        });

//        release_et_input.addTextChangedListener(new TextWatchListener() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String stringText = s.toString();
//                if (!TextUtils.isEmpty(topicTitle)
//                        && !stringText.contains(topicTitle)
//                        && stringText.length() >= topicTitle.length() - 1
//                        && before == 1 && start < topicTitle.length()) {
//                    if ((start == 0 &&
//                            stringText.substring(0, topicTitle.length() - 1).equals(topicTitle.substring(1, topicTitle.length() - 1))) ||
//                            ((stringText.substring(0, start).equals(topicTitle.substring(0, start))
//                                    && stringText.substring(start, topicTitle.length() - 1).equals(topicTitle.substring(start + before, topicTitle.length())))) ||
//                            (start == topicTitle.length() - 1 &&
//                                    stringText.substring(0, topicTitle.length() - 1).equals(topicTitle.substring(0, topicTitle.length() - 1)))) {
//                        release_et_input.setText(getStrings(s.subSequence(topicTitle.length() - 1, s.length()).toString()));
//                    }
//                }
//                super.onTextChanged(s, start, before, count);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                super.afterTextChanged(s);
//            }
//        });
//
//        release_et_input.setOnSelectionChangedListener(new ReleaseEditView.OnSelectionChangedListener() {
//            @Override
//            public void onSelectionChanged(int selStart, int selEnd) {
//                String topicString = release_et_input.getText().toString();
//                if (!TextUtils.isEmpty(topicString) && !TextUtils.isEmpty(topicTitle)
//                        && topicString.contains(topicString)
//                        && topicString.length() >= topicTitle.length()
//                        && (selStart >= 0 && selEnd < topicTitle.length()
//                        || (selStart >= 0 && selStart < topicTitle.length()))) {
//                    release_et_input.setSelection(topicTitle.length());
//                }
//            }
//        });
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
                int imgLength = dataPath.size() - 1;
                if (position == imgLength && dataPath.get(imgLength).
                        equals(DEFAULT_ADD_IMG)) {
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
                    component.setImage(BitmapFactory.decodeFile(dataPath.get(position)))
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
                dataPath.clear();
                dataPath.add(result.getPath());
                dataPath.add(DEFAULT_ADD_IMG);
                setSelectImageData();
                imgGArticleRecyclerAdapter.setNewData(dataPath);
                break;
            case "addImg":
                result = (ReplaceData) message.result;
                dataPath.remove(dataPath.size() - 1);
                dataPath.add(result.getPath());
                if (dataPath.size() < 9) {
                    dataPath.add(DEFAULT_ADD_IMG);
                }
                setSelectImageData();
                imgGArticleRecyclerAdapter.setNewData(dataPath);
                break;
            case "replace":
                ReplaceData path = (ReplaceData) message.result;
                dataPath.set(path.getPosition(), path.getPath());
                setSelectImageData();
                imgGArticleRecyclerAdapter.setNewData(dataPath);
                break;
        }
        if ("closeKeyBroad".equals(message.type) && "close".equals(message.result)) {
            CommonUtils.hideSoftInput(ReleaseImgArticleActivity.this, release_et_input);
        }
    }

    private void setSelectImageData() {
        mSelectPath.clear();
        mSelectPath.addAll(dataPath);
        if (mSelectPath.size() > 0 && mSelectPath.get(mSelectPath.size() - 1).equals(DEFAULT_ADD_IMG)) {
            mSelectPath.remove(mSelectPath.size() - 1);
        }
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(this);
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
                    getLoginStatus();
                    loadData();
                    break;
                case 100:
                case REQUEST_IMG:
                case PictureConfigC.CHOOSE_REQUEST:
                    List<LocalMediaC> localMediaList = PictureSelector.obtainMultipleResult(data);
                    if (localMediaList != null && localMediaList.size() > 0) {
                        this.dataPath.clear();
                        for (LocalMediaC localMedia : localMediaList) {
                            if (!TextUtils.isEmpty(localMedia.getPath())) {
                                this.dataPath.add(localMedia.getPath());
                            }
                        }
                        this.dataPath.remove(DEFAULT_ADD_IMG);
                        if (this.dataPath.size() < 9) {
                            this.dataPath.add(DEFAULT_ADD_IMG);
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
        if (release_et_input.getText().toString().trim().length() < 1
                && (dataPath.size() < 1 || (dataPath.size() < 2 && dataPath.get(0).equals(DEFAULT_ADD_IMG)))) {
            showToast(this, "请输入发送内容");
            return;
        } else {
            if (loadHud != null) {
                loadHud.show();
            }
            final String content = release_et_input.getText().toString();
            tv_find_release_topic.setText("发送中...");
            tv_find_release_topic.setEnabled(false);
            if (dataPath.size() < 9) {
                dataPath.remove(dataPath.size() - 1);
            } else if (dataPath.size() == 9 && dataPath.get(8).equals(DEFAULT_ADD_IMG)) {
                dataPath.remove(dataPath.size() - 1);
            }
            if (dataPath.size() < 1) {
                sendData(null, content);
            } else {
                ImgUrlHelp imgUrlHelp = new ImgUrlHelp();
                imgUrlHelp.setUrl(ReleaseImgArticleActivity.this, dataPath);
                imgUrlHelp.setOnFinishListener(new ImgUrlHelp.OnFinishDataListener() {
                    @Override
                    public void finishData(List<String> data, Handler handler) {
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
        params.put("fuid", uid);
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
        if (uid > 0) {
            String url = Url.BASE_URL + Url.RELEASE_RELEVANCE_PRODUCT;
            Map<String, Object> params = new HashMap<>();
            params.put("uid", uid);
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
                                    if (dataPath.size() > 0) {
                                        String pathString = dataPath.get(dataPath.size() - 1);
                                        if (pathString.equals(DEFAULT_ADD_IMG)) {
                                            dataPath.remove(pathString);
                                        }
                                    }
                                    dataPath.add(finalTopicSavePath);
                                    if (dataPath.size() < 9) {
                                        dataPath.add(DEFAULT_ADD_IMG);
                                    }
                                    setSelectImageData();
                                    imgGArticleRecyclerAdapter.notifyDataSetChanged();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onError(Drawable errorDrawable) {
                        }
                    });
                } else {
                    if (dataPath.size() > 0) {
                        String pathString = dataPath.get(dataPath.size() - 1);
                        if (pathString.equals(DEFAULT_ADD_IMG)) {
                            dataPath.remove(pathString);
                        }
                    }
                    dataPath.add(topicSavePath);
                    if (dataPath.size() < 9) {
                        dataPath.add(DEFAULT_ADD_IMG);
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

    @Override
    protected void onPause() {
        super.onPause();
    }
}
