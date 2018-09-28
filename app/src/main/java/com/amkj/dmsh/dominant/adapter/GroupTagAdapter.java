package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.dominant.bean.QualityGroupShareEntity.QualityGroupShareBean.MemberListBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/6/23
 * version 3.1.4
 * class description:组团邀请参团
 */
public class GroupTagAdapter extends BaseQuickAdapter<MemberListBean,BaseViewHolder>{
    private final Context context;

    public GroupTagAdapter(Context context, List<MemberListBean> memberListBeans) {
        super(R.layout.layout_gp_join_avator,memberListBeans);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberListBean memberListBean) {
        ImageView imageView = (ImageView) helper.getView(R.id.iv_dm_gp_open_ava);
        TextView tv_dm_gp_name = (TextView) helper.getView(R.id.tv_dm_gp_name);
        GlideImageLoaderUtil.loadRoundImg(context, imageView, memberListBean.getAvatar(), (int) (AutoSizeUtils.mm2px(mAppContext,100)));
        String name = getStrings(memberListBean.getNickname());
        if (name.length() > 7) {
            name = name.substring(0, 7) + "...";
        }
        tv_dm_gp_name.setText(name);
    }
}
