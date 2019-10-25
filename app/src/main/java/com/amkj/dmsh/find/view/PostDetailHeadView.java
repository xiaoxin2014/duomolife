package com.amkj.dmsh.find.view;

import android.content.Context;
import android.support.text.emoji.widget.EmojiTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dao.SoftApiDao;
import com.amkj.dmsh.find.bean.PostDetailEntity.PostDetailBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;
import static com.amkj.dmsh.constant.ConstantMethod.skipUserCenter;

/**
 * Created by xiaoxin on 2019/3/18 0018
 * Version：V4.1.0
 * ClassDescription :帖子插入单件商品
 */
public class PostDetailHeadView extends LinearLayout {
    @BindView(R.id.tv_life_back)
    ImageView mTvLifeBack;
    @BindView(R.id.iv_head2)
    ImageView mIvHead;
    @BindView(R.id.tv_user_name2)
    EmojiTextView mTvUserName;
    @BindView(R.id.tv_follow2)
    TextView mTvFollow;
    @BindView(R.id.iv_img_share)
    ImageView mIvImgShare;
    @BindView(R.id.rl_head)
    RelativeLayout mRlHead;
    private BaseActivity mActivity;
    private PostDetailBean postDetailBean;


    public PostDetailHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View headView = LayoutInflater.from(context).inflate(R.layout.layout_post_detail_head, this, true);
        ButterKnife.bind(this, headView);

    }

    public void updateData(BaseActivity activity, PostDetailBean postDetailBean) {
        //设置用户信息
        mActivity = activity;
        if (postDetailBean != null) {
            this.postDetailBean = postDetailBean;
            GlideImageLoaderUtil.loadRoundImg(mActivity, mIvHead, postDetailBean.getAvatar(), AutoSizeUtils.mm2px(mAppContext, 70), R.drawable.default_ava_img);
            mTvUserName.setText(getStrings(postDetailBean.getNickname()));
            mTvFollow.setText(postDetailBean.isFlag() ? "已关注" : "关注");
            mTvFollow.setSelected(postDetailBean.isFlag());
        }
    }

    @OnClick({R.id.tv_life_back, R.id.iv_head2, R.id.tv_follow2, R.id.iv_img_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                if (isContextExisted(mActivity)) {
                    mActivity.finish();
                }
                break;
            case R.id.iv_head2:
                if (postDetailBean != null) {
                    skipUserCenter(mActivity, postDetailBean.getUid());
                }
                break;
            case R.id.tv_follow2:
                if (postDetailBean != null) {
                    SoftApiDao.followUser(mActivity, postDetailBean.getUid(), (TextView) view, null);
                }
                break;
            case R.id.iv_img_share:
                if (postDetailBean != null) {
                    new UMShareAction(mActivity
                            , postDetailBean.getPath()
                            , "分享" + postDetailBean.getNickname() + "帖子"
                            , postDetailBean.getDigest()
                            , Url.BASE_SHARE_PAGE_TWO + "m/template/find_template/find_detail.html?id=" + postDetailBean.getId(), postDetailBean.getId());
                }
                break;
        }
    }
}



