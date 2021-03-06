package com.amkj.dmsh.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.ImageBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.find.activity.ImagePagerActivity;
import com.amkj.dmsh.mine.adapter.SuggestionFeedBackTypeAdapter;
import com.amkj.dmsh.mine.bean.SuggestionTypeEntity;
import com.amkj.dmsh.mine.bean.SuggestionTypeEntity.FeedBackTypeBean;
import com.amkj.dmsh.netloadpage.NetErrorCallback;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.release.adapter.ImgGridRecyclerAdapter;
import com.amkj.dmsh.release.bean.ImagePathBean;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.ImgUrlHelp;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.pictureselector.PictureSelectorUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kingja.loadsir.core.Transport;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfigC;
import com.luck.picture.lib.entity.LocalMediaC;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_ADD_IMG;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.MINE_FEEDBACK;
import static com.amkj.dmsh.constant.Url.MINE_FEEDBACK_TYPE;
import static com.amkj.dmsh.utils.ImageFormatUtils.getImageFormatInstance;

;

/**
 * Created by atd48 on 2016/10/19.
 */
public class SuggestionFeedBackActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    //    ????????????
    @BindView(R.id.emoji_mine_suggestion_feed_back)
    EditText emoji_mine_suggestion_feed_back;
    @BindView(R.id.tv_suggestion_type)
    TextView tv_suggestion_type;
    //    ????????????
    @BindView(R.id.rv_sug_img_show)
    RecyclerView rv_sug_img_show;
    //    ??????
    @BindView(R.id.tv_suggestion_commit)
    TextView tv_suggestion_commit;
    private ImgGridRecyclerAdapter imgGridRecyclerAdapter;
    private ArrayList<String> mSelectPath = new ArrayList<>();
    private List<ImagePathBean> imagePathBeans = new ArrayList<>();
    //    ?????????????????????
    private List<String> updatedImages = new ArrayList<>();
    private final int REQUEST_PERMISSIONS = 60;
    private final int SUBMIT_FEEDBACK_CODE = 120;
    private List<FeedBackTypeBean> feedBackTypeBeans = new ArrayList<>();
    private int maxSelImg = 5;
    private int adapterPosition;
    private AlertDialog selectAlertView;
    private SuggestionFeedBackTypeAdapter suggestionFeedBackAdapter;
    private FeedBackTypeBean feedBackTypeBean;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_suggestion_feed_back;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_titleAll.setText("????????????");
        header_shared.setVisibility(View.GONE);
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        if (app.getScreenWidth() >= AutoSizeUtils.mm2px(mAppContext, 600)) {
            rv_sug_img_show.setLayoutManager(new GridLayoutManager(SuggestionFeedBackActivity.this, 5));
        } else {
            rv_sug_img_show.setLayoutManager(new GridLayoutManager(SuggestionFeedBackActivity.this, 3));
        }
        if (imagePathBeans.size() < 1) {
            imagePathBeans.add(getImageFormatInstance().getDefaultAddImage());
        }
        rv_sug_img_show.addItemDecoration(new ItemDecoration.Builder()
                // ?????????????????????ID
                .setDividerId(R.drawable.item_divider_img_white).create());
        imgGridRecyclerAdapter = new ImgGridRecyclerAdapter(this, imagePathBeans);
        rv_sug_img_show.setAdapter(imgGridRecyclerAdapter);
        imgGridRecyclerAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.delete) {
                    adapterPosition = (int) view.getTag();
                    getImageFormatInstance().delImageBean(imagePathBeans, adapterPosition);
                    mSelectPath.clear();
                    mSelectPath.addAll(getImageFormatInstance().formatStringPathRemoveDefault(imagePathBeans));
                    imgGridRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });
        imgGridRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                ????????????
                if (((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).isActive()) {
                    CommonUtils.hideSoftInput(SuggestionFeedBackActivity.this, emoji_mine_suggestion_feed_back);
                }
                pickImage(position);
            }
        });
        KeyboardUtils.registerSoftInputChangedListener(this, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if (height == 0) {
                    ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).requestFocus();
                }
            }
        });
        if (loadService != null) {
            loadService.setCallBack(NetErrorCallback.class, new Transport() {
                @Override
                public void order(Context context, View view) {
                    TextView backView = view.findViewById(R.id.tv_net_load_error_back);
                    backView.setVisibility(View.VISIBLE);
                    backView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }
            });
        }
    }

    private void pickImage(final int position) {
        ConstantMethod constantMethod = new ConstantMethod();
        constantMethod.setOnGetPermissionsSuccess(new ConstantMethod.OnGetPermissionsSuccessListener() {
            @Override
            public void getPermissionsSuccess() {
                int imgLength = imagePathBeans.size() - 1;
                if (position == imgLength
                        && DEFAULT_ADD_IMG.equals(imagePathBeans.get(imgLength).getPath())) {
                    PictureSelectorUtils.getInstance()
                            .resetVariable()
                            .isCrop(false)
                            .selImageList(mSelectPath)
                            .selMaxNum(maxSelImg)
                            .imageMode(PictureConfigC.MULTIPLE)
                            .isShowGif(true)
                            .openGallery(SuggestionFeedBackActivity.this);
                } else {
                    Intent intent = new Intent(SuggestionFeedBackActivity.this, ImagePagerActivity.class);
                    intent.putExtra(ImagePagerActivity.INTENT_POSITION, position);
                    List<ImageBean> imageBeanList = new ArrayList<>();
                    ImageBean imageBean;
                    for (String imgUrl : mSelectPath) {
                        imageBean = new ImageBean();
                        imageBean.setPicUrl(imgUrl);
                        imageBeanList.add(imageBean);
                    }
                    intent.putParcelableArrayListExtra(ImagePagerActivity.INTENT_IMGURLS, (ArrayList<? extends Parcelable>) imageBeanList);
                    startActivity(intent);
                }
            }
        });
        constantMethod.getPermissions(SuggestionFeedBackActivity.this, Permission.Group.STORAGE);
    }


    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(rv_sug_img_show.getWindowToken(), 0); //??????????????????)
        }
        finish();
    }

    /**
     * ????????????
     *
     * @param view
     */
    @OnClick(R.id.tv_suggestion_type)
    void commitSuggestion(View view) {
        if (KeyboardUtils.isSoftInputVisible(this)) {
            KeyboardUtils.hideSoftInput(this);
        }
        if (feedBackTypeBeans.size() > 0) {
            if (selectAlertView == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SuggestionFeedBackActivity.this, R.style.service_dialog_theme);
                View dialogView = LayoutInflater.from(SuggestionFeedBackActivity.this).inflate(R.layout.alert_suggestion_feedback_type, null, false);
                RecyclerView communal_recycler_wrap = dialogView.findViewById(R.id.communal_recycler_wrap);
                communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(SuggestionFeedBackActivity.this));
                suggestionFeedBackAdapter = new SuggestionFeedBackTypeAdapter(feedBackTypeBeans);
                communal_recycler_wrap.setAdapter(suggestionFeedBackAdapter);
                communal_recycler_wrap.addItemDecoration(new ItemDecoration.Builder()
                        // ?????????????????????ID
                        .setDividerId(R.drawable.item_divider_gray_f_one_px).create());
                suggestionFeedBackAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        FeedBackTypeBean feedBackTypeBean = (FeedBackTypeBean) view.getTag();
                        if (feedBackTypeBean != null) {
                            setTypeData(feedBackTypeBean);
                            if (selectAlertView != null) {
                                selectAlertView.dismiss();
                            }
                        }
                    }
                });
                selectAlertView = builder.create();
                selectAlertView.show();
                TinkerBaseApplicationLike tinkerBaseApplicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
                int screenHeight = tinkerBaseApplicationLike.getScreenHeight() / 3 * 2;
                Window window = selectAlertView.getWindow();
                if (window != null) {
                    window.setBackgroundDrawableResource(R.color.translucence);
                    WindowManager.LayoutParams attributes = window.getAttributes();
                    attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    dialogView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            if (v.getMeasuredHeight() > screenHeight) {
                                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                                layoutParams.height = screenHeight;
                                v.setLayoutParams(layoutParams);
                            }
                        }
                    });
                    attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
                    window.setAttributes(attributes);
                    window.setGravity(Gravity.BOTTOM);
                    window.setContentView(dialogView);
                }
            } else if (!selectAlertView.isShowing()) {
                selectAlertView.show();
            }
        }
    }

    private void setTypeData(FeedBackTypeBean feedBackTypeBean) {
        this.feedBackTypeBean = feedBackTypeBean;
        tv_suggestion_type.setText(getStrings(feedBackTypeBean.getTitle()));
    }

    @OnClick(R.id.tv_suggestion_commit)
    void submitSuggestion(View view) {
        if (feedBackTypeBean == null) {
            showToast(getStrings(tv_suggestion_type.getHint().toString().trim()));
            return;
        }
        if (!TextUtils.isEmpty(emoji_mine_suggestion_feed_back.getText().toString().trim())) {
            if (loadHud != null) {
                loadHud.show();
            }
            tv_suggestion_commit.setText("?????????...");
            tv_suggestion_commit.setEnabled(false);
            if (mSelectPath.size() < 1) {
                sendSuggestionData(null);
            } else {
                if (updatedImages.size() > 0) {
                    sendSuggestionData(updatedImages);
                } else {
                    ImgUrlHelp imgUrlHelp = new ImgUrlHelp();
                    imgUrlHelp.setUrl(SuggestionFeedBackActivity.this, mSelectPath);
                    imgUrlHelp.setOnFinishListener(new ImgUrlHelp.OnFinishDataListener() {
                        @Override
                        public void finishData(@NonNull List<String> data, Handler handler) {
                            updatedImages.clear();
                            updatedImages.addAll(data);
                            //                            ????????????????????? ??????????????????
                            getImageFormatInstance().submitChangeIconStatus(imagePathBeans, false);
                            imgGridRecyclerAdapter.notifyDataSetChanged();
                            sendSuggestionData(data);
                            handler.removeCallbacksAndMessages(null);
                        }

                        @Override
                        public void finishError(String error) {
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            tv_suggestion_commit.setText("??????");
                            tv_suggestion_commit.setEnabled(true);
                            showToast("????????????");
                        }

                        @Override
                        public void finishSingleImg(String singleImg, Handler handler) {
                        }
                    });
                }
            }
        } else {
            showToast("?????????????????????");
        }
    }

    //  ????????????
    private void sendSuggestionData(List<String> data) {
        //????????????
        String imgPath = "";
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("remark", emoji_mine_suggestion_feed_back.getText().toString().trim());
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                if (i == 0) {
                    imgPath += getStrings(data.get(i));
                } else {
                    imgPath += ("," + getStrings(data.get(i)));
                }
            }
            params.put("feed_images", imgPath);
        }
        params.put("type", feedBackTypeBean.getId());
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_FEEDBACK, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }

                RequestStatus requestInfo = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestInfo != null) {
                    if (requestInfo.getCode().equals(SUCCESS_CODE)) {
                        showToast("????????????");
                        finish();
                    } else {
                        showToastRequestMsg( requestInfo);
                    }
                }
                tv_suggestion_commit.setText("??????");
                tv_suggestion_commit.setEnabled(true);
            }

            @Override
            public void onNotNetOrException() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                tv_suggestion_commit.setText("??????");
                tv_suggestion_commit.setEnabled(true);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.do_failed);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            if (requestCode == SUBMIT_FEEDBACK_CODE
                    || requestCode == IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        if (requestCode == PictureConfigC.CHOOSE_REQUEST) {
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
                mSelectPath.clear();
                mSelectPath.addAll(getImageFormatInstance().formatStringPathRemoveDefault(imagePathBeans));
                imgGridRecyclerAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == REQUEST_PERMISSIONS) {
            showToast("??????????????????????????????");
        } else if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
    }

    @Override
    protected void loadData() {
        if (userId < 1) {
            NetLoadUtils.getNetInstance().showLoadSirEmpty(loadService);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_FEEDBACK_TYPE, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                SuggestionTypeEntity suggestionTypeEntity = GsonUtils.fromJson(result, SuggestionTypeEntity.class);
                if (suggestionTypeEntity != null) {
                    if (SUCCESS_CODE.equals(suggestionTypeEntity.getCode())) {
                        feedBackTypeBeans.addAll(suggestionTypeEntity.getFeedBackTypeList());
                        if (suggestionFeedBackAdapter != null) {
                            suggestionFeedBackAdapter.notifyDataSetChanged();
                        }
                    } else {
                        showToast(getStrings(suggestionTypeEntity.getMsg()));
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService,suggestionTypeEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSirLoadFailed(loadService);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.invalidData);
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
        if (selectAlertView != null && selectAlertView.isShowing()) {
            selectAlertView.dismiss();
        }
        KeyboardUtils.unregisterSoftInputChangedListener(this);
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }
}
