package com.amkj.dmsh.find.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.ImageBean;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.adapter.FindImageListAdapter;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_3;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.IMAGE_DEF;

/**
 * Created by xiaoxin on 2021/4/16
 * Version:v5.1.0
 * ClassDescription :评论详情
 */
public class EvaluateDetailActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.view_divider)
    View mViewDivider;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_sku)
    TextView mTvSku;
    @BindView(R.id.rating_bar)
    MaterialRatingBar mRatingBar;
    @BindView(R.id.ll_ratingbar)
    LinearLayout mLlRatingbar;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    @BindView(R.id.rv_imgs)
    RecyclerView mRvImgs;
    private String mEvaluateId;
    private EvaluateDetailEntity mEvaluateDetailEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_evaluate_detail;
    }

    @Override
    protected void initViews() {
        mEvaluateId = getIntent().getStringExtra("evaluateId");
        if (TextUtils.isEmpty(mEvaluateId)) {
            showToast("数据有误，请重试");
            finish();
        }
        mTvHeaderShared.setVisibility(View.GONE);
        mTvHeaderTitle.setText("评价详情");
        mRvImgs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    protected void loadData() {
        Map<String, String> map = new HashMap<>();
        map.put("evaluateId", mEvaluateId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_EVALUATE_DETAIL, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mEvaluateDetailEntity = GsonUtils.fromJson(result, EvaluateDetailEntity.class);
                if (mEvaluateDetailEntity != null) {
                    String code = mEvaluateDetailEntity.getCode();
                    if (SUCCESS_CODE.equals(code)) {
                        EvaluateDetailEntity.ResultBean resultBean = mEvaluateDetailEntity.getResult();
                        GlideImageLoaderUtil.loadCenterCrop(getActivity(), mIvCover, resultBean.getProductImg());
                        mTvName.setText(resultBean.getProductName());
                        mTvSku.setText(resultBean.getSkuValue());
                        mRatingBar.setNumStars(resultBean.getStar());
                        mTvContent.setText(resultBean.getContent());
                        setEvaImages(mRvImgs, resultBean.getImages());
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mEvaluateDetailEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mEvaluateDetailEntity);
            }
        });
    }


    private void setEvaImages(RecyclerView rvImgs, String images) {
        if (!TextUtils.isEmpty(images)) {
            final List<String> originalPhotos = new ArrayList<>();
            final List<InvitationDetailEntity.InvitationDetailBean.PictureBean> pictureBeanList = new ArrayList<>();
            String[] evaImages = images.split(",");
            InvitationDetailEntity.InvitationDetailBean.PictureBean pictureBean;
            originalPhotos.addAll(Arrays.asList(evaImages));
            for (int i = 0; i < evaImages.length; i++) {
                pictureBean = new InvitationDetailEntity.InvitationDetailBean.PictureBean();
                pictureBean.setItemType(TYPE_3);
                pictureBean.setIndex(i);
                pictureBean.setPath(evaImages[i]);
                pictureBean.setOriginalList(originalPhotos);
                pictureBeanList.add(pictureBean);
            }
            FindImageListAdapter findImageListAdapter = new FindImageListAdapter(this, pictureBeanList);
            rvImgs.setAdapter(findImageListAdapter);
            findImageListAdapter.setNewData(pictureBeanList);
            findImageListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    InvitationDetailEntity.InvitationDetailBean.PictureBean pictureBean = (InvitationDetailEntity.InvitationDetailBean.PictureBean) view.getTag();
                    if (pictureBean != null) {
                        ImageBean imageBean = null;
                        List<ImageBean> imageBeanList = new ArrayList<>();
                        for (String picUrl : pictureBean.getOriginalList()) {
                            imageBean = new ImageBean();
                            imageBean.setPicUrl(picUrl);
                            imageBeanList.add(imageBean);
                        }
                        ImagePagerActivity.startImagePagerActivity(getActivity(), IMAGE_DEF, imageBeanList
                                , pictureBean.getIndex() < pictureBean.getOriginalList().size() ? pictureBean.getIndex() : 0);
                    }
                }
            });
        } else {
            rvImgs.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.tv_life_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mLlContent;
    }
}
