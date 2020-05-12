package com.amkj.dmsh.message.adapter;

import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dao.SoftApiDao;
import com.amkj.dmsh.find.bean.FansEntity;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.NEW_FANS;
import static com.amkj.dmsh.constant.ConstantVariable.RECOMMEND_FOLLOW;
import static com.amkj.dmsh.constant.ConstantVariable.RECOMMEND_FOLLOW_TITLE;

/**
 * Created by xiaoxin on 2019/7/20
 * Version:v4.1.0
 * ClassDescription :消息中心-新增粉丝列表适配器
 */
public class FansAdapter extends BaseMultiItemQuickAdapter<FansEntity.FansBean, BaseViewHolder> {
    private BaseActivity activity;

    public FansAdapter(BaseActivity activity, @Nullable List<FansEntity.FansBean> data) {
        super(data);
        addItemType(NEW_FANS, R.layout.item_fans);
        addItemType(RECOMMEND_FOLLOW, R.layout.item_fans);
        addItemType(RECOMMEND_FOLLOW_TITLE, R.layout.layout_recommend_follow);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, FansEntity.FansBean item) {
        if (item == null) return;
        switch (item.getItemType()) {
            case NEW_FANS:
            case RECOMMEND_FOLLOW:
                ImageView ivCover = helper.getView(R.id.iv_cover);
                GlideImageLoaderUtil.loadRoundImg(activity, ivCover, item.getAvatar(), AutoSizeUtils.mm2px(mAppContext, 80));
                helper.setText(R.id.tv_user_name, getStrings(item.getNickname()))
                        .setText(R.id.tv_time, getStrings(item.getCreateTime()))
                        .setGone(R.id.tv_time, !TextUtils.isEmpty(item.getCreateTime()))
                        .setText(R.id.tv_user_name, getStrings(item.getNickname()));
                TextView tvFollow = helper.getView(R.id.tv_follow);
                tvFollow.setSelected(item.getIsFocus());
                tvFollow.setText(item.getIsFocus() ? "已关注" : (item.getItemType() == NEW_FANS ? "回粉" : "关注"));
                //点击头像
                ivCover.setOnClickListener(v -> ConstantMethod.skipUserCenter(activity, getStringChangeIntegers(item.getUid())));
                //关注或取消
                tvFollow.setOnClickListener(v -> SoftApiDao.followUser(activity, getStringChangeIntegers(item.getUid()), tvFollow, item, new SoftApiDao.FollowCompleteListener() {
                    @Override
                    public void followComplete(boolean isFollow) {
                        item.setIsFocus(isFollow);
                    }
                }));
                break;
        }

    }
}
