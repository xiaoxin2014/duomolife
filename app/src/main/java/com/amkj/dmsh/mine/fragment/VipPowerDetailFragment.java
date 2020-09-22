package com.amkj.dmsh.mine.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;

import butterknife.BindView;

/**
 * Created by xiaoxin on 2020/7/23
 * Version:v4.7.0
 * ClassDescription :会员权益详情
 */
public class VipPowerDetailFragment extends BaseFragment {

    @BindView(R.id.tv_power_name)
    TextView mTvPowerName;
    @BindView(R.id.iv_pic)
    ImageView mIvPic;
    @BindView(R.id.tv_detail)
    TextView mTvDetail;
    @BindView(R.id.tv_skip)
    TextView mTvSkip;
    private String mName;
    private String mPicUrl;
    private String mDetail;
    private String mAndroidLink;
    private String mBtnText;

    @Override
    protected int getContentView() {
        return R.layout.fragment_vip_power_detail;
    }

    @Override
    protected void initViews() {
        mTvPowerName.setText(mName);
        GlideImageLoaderUtil.loadImage(getActivity(), mIvPic, mPicUrl);
        mTvDetail.setText(mDetail);
        mTvSkip.setText(mBtnText);
        mTvSkip.setVisibility(!TextUtils.isEmpty(mAndroidLink) ? View.VISIBLE : View.GONE);
        mTvSkip.setOnClickListener(v -> {
            ConstantMethod.setSkipPath(getActivity(), mAndroidLink, false);
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected boolean isLazy() {
        return true;
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        super.getReqParams(bundle);
        if (bundle != null) {
            mName = (String) bundle.get("name");
            mPicUrl = (String) bundle.get("picUrl");
            mDetail = (String) bundle.get("detail");
            mAndroidLink = (String) bundle.get("androidLink");
            mBtnText = (String) bundle.get("btnText");
        }
    }
}
