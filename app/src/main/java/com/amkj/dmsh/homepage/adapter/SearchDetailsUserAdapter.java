package com.amkj.dmsh.homepage.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.mine.bean.UserAttentionFansEntity.UserAttentionFansBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.dao.UserDao.getPersonalInfo;


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
        if (userAttentionFansBean.isFlag()) {
            tv_search_details_attention.setSelected(true);
            tv_search_details_attention.setText("已关注");
        } else {
            tv_search_details_attention.setSelected(false);
            tv_search_details_attention.setText("关注");
        }
        tv_search_details_attention.setTag(userAttentionFansBean);
        tv_search_details_attention.setOnClickListener(v -> isLoginStatus(v));

        helper.itemView.setOnClickListener(v -> {
            Intent intent = new Intent();
            if (type.equals("attention")) {
                if (userId != userAttentionFansBean.getBuid()) {
                    intent.setClass(context, UserPagerActivity.class);
                    intent.putExtra("userId", String.valueOf(userAttentionFansBean.getBuid()));
                    context.startActivity(intent);
                } else {
                    showToast(context, R.string.not_attention_self);
                }
            } else {
                if (userId != userAttentionFansBean.getFuid()) {
                    intent.setClass(context, UserPagerActivity.class);
                    intent.putExtra("userId", String.valueOf(userAttentionFansBean.getFuid()));
                    context.startActivity(intent);
                } else {
                    showToast(context, R.string.not_attention_self);
                }
            }
        });
        helper.itemView.setTag(userAttentionFansBean);
    }

    private void isLoginStatus(View v) {
        SavePersonalInfoBean personalInfo = getPersonalInfo(context);
        if (personalInfo.isLogin()) {
            //登陆成功，加载信息
            int uid = personalInfo.getUid();
            //登陆成功处理
            setAttentionFlag(uid, v);
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(context, MineLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            (context).startActivityForResult(intent, IS_LOGIN_CODE);
        }
    }

    public void setAttentionFlag(int uid, final View v) {
        final TextView textView = (TextView) v;
        UserAttentionFansBean userAttentionFansBean = (UserAttentionFansBean) v.getTag();
        String url = Url.UPDATE_ATTENTION;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("fuid", uid);
        //关注id
        if (type.equals("attention")) {
            params.put("buid", userAttentionFansBean.getBuid());
        } else {
            params.put("buid", userAttentionFansBean.getFuid());
        }
        String flag;
        if (textView.isSelected()) {
            flag = "cancel";
        } else {
            flag = "add";
        }
        params.put("ftype", flag);
        NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        if (!textView.isSelected()) {
                            textView.setSelected(true);
                            textView.setText("已关注");
                            showToast(context, "已关注");
                        } else {
                            textView.setSelected(false);
                            textView.setText("关注");
                            showToast(context, "已取消关注");
                        }
                    } else {
                        showToastRequestMsg(context, requestStatus);
                    }
                }

            }
        });
    }
}


