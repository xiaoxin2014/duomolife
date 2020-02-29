package com.amkj.dmsh.homepage.adapter;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.dao.SoftApiDao;
import com.amkj.dmsh.mine.bean.UserAttentionFansEntity.UserAttentionFansBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.skipUserCenter;


/**
 * Created by atd48 on 2016/7/4.
 */
public class SearchDetailsUserAdapter extends BaseQuickAdapter<UserAttentionFansBean, BaseViewHolder> {

    private final Activity context;
    private String type;

    public SearchDetailsUserAdapter(Activity context, List<UserAttentionFansBean> userAttentionFansList, String type) {
        super(R.layout.adapter_search_details_user, userAttentionFansList);
        this.type = type;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserAttentionFansBean userAttentionFansBean) {
        if (userAttentionFansBean == null) return;
        ImageView search_details_user_header = helper.getView(R.id.search_details_user_header);
        TextView tv_search_details_user_name = helper.getView(R.id.tv_search_details_user_name);
        TextView tv_search_details_attention = helper.getView(R.id.tv_search_details_attention);
//        关注
        if (type.equals("attention")) {
            GlideImageLoaderUtil.loadHeaderImg(context, search_details_user_header, userAttentionFansBean.getBavatar());
            tv_search_details_user_name.setText(userAttentionFansBean.getBnickname());
        } else {
//            被关注
            GlideImageLoaderUtil.loadHeaderImg(context, search_details_user_header, userAttentionFansBean.getFavatar());
            tv_search_details_user_name.setText(userAttentionFansBean.getFnickname());
        }
        tv_search_details_attention.setSelected(userAttentionFansBean.isFlag());
        tv_search_details_attention.setText(userAttentionFansBean.isFlag() ? "已关注" : "关注");
        tv_search_details_attention.setTag(userAttentionFansBean);
        //关注或取消
        tv_search_details_attention.setOnClickListener(v -> {
            SoftApiDao.followUser(((BaseActivity) context), "attention".equals(type) ? userAttentionFansBean.getBuid() : userAttentionFansBean.getFuid(),
                    ((TextView) v), userAttentionFansBean, new SoftApiDao.FollowCompleteListener() {
                        @Override
                        public void followComplete(boolean isFollow) {
                            userAttentionFansBean.setFlag(isFollow);
                        }
                    });
        });

        helper.itemView.setOnClickListener(v -> skipUserCenter(context, "attention".equals(type) ? userAttentionFansBean.getBuid() : userAttentionFansBean.getFuid()));
        helper.itemView.setTag(userAttentionFansBean);
    }
}


