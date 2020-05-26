package com.amkj.dmsh.views.alertdialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseAlertDialog;
import com.amkj.dmsh.bean.ShareIconTitleBean;
import com.amkj.dmsh.find.activity.JoinSuccessActivity;
import com.amkj.dmsh.find.activity.PostDetailActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/6/6
 * version 3.1.3
 * class description:分享统一弹窗
 */
public class AlertDialogShareHelper extends BaseAlertDialog {
    @BindView(R.id.communal_recycler_wrap)
    RecyclerView communal_recycler_wrap;
    @BindView(R.id.rel_share_loading)
    RelativeLayout loadView;
    private AlertSelectShareListener alertSelectShareListener;
    private List<ShareIconTitleBean> iconTitleList = new ArrayList<>();

    public AlertDialogShareHelper(Activity context) {
        this(context, false, "");
    }

    public AlertDialogShareHelper(Activity context, boolean showDownImg, String h5Platform) {
        super(context);
        if (TextUtils.isEmpty(h5Platform)) {
            //默认显示微信和朋友圈
            iconTitleList.add(new ShareIconTitleBean(R.drawable.share_wechat_icon, "微信", SHARE_MEDIA.WEIXIN));
            iconTitleList.add(new ShareIconTitleBean(R.drawable.share_circle_icon, "微信朋友圈", SHARE_MEDIA.WEIXIN_CIRCLE));

            //晒单成功界面只分享到微信渠道
            if (!JoinSuccessActivity.class.getSimpleName().equals(context.getClass().getSimpleName())) {
                iconTitleList.add(new ShareIconTitleBean(R.drawable.share_qq_icon, "QQ", SHARE_MEDIA.QQ));
                iconTitleList.add(new ShareIconTitleBean(R.drawable.share_sina_icon, "微博", SHARE_MEDIA.SINA));
            }

            //默认显示复制链接
            iconTitleList.add(new ShareIconTitleBean(R.drawable.share_copy_icon, "复制链接", SHARE_MEDIA.POCKET));

            //是否保存商品图片
            if (showDownImg) {
                iconTitleList.add(new ShareIconTitleBean(R.drawable.share_save_icon, "保存图片", SHARE_MEDIA.MORE));
            }

            //自己的帖子显示删除按钮
            if (PostDetailActivity.class.getSimpleName().equals(context.getClass().getSimpleName()) && ((PostDetailActivity) context).isMyPost()) {
                iconTitleList.add(new ShareIconTitleBean(R.drawable.delete_post, "删除帖子", SHARE_MEDIA.LINE));
            }

            //不是自己的帖子显示举报按钮
            if (PostDetailActivity.class.getSimpleName().equals(context.getClass().getSimpleName()) && !((PostDetailActivity) context).isMyPost()) {
                iconTitleList.add(new ShareIconTitleBean(R.drawable.report_post, "举报帖子", SHARE_MEDIA.TUMBLR));
            }
        } else {
            List<String> platforms = Arrays.asList(h5Platform.split(","));
            for (String platform : platforms) {
                switch (platform) {
                    case "1":
                        iconTitleList.add(new ShareIconTitleBean(R.drawable.share_wechat_icon, "微信", SHARE_MEDIA.WEIXIN));
                        break;
                    case "2":
                        iconTitleList.add(new ShareIconTitleBean(R.drawable.share_circle_icon, "微信朋友圈", SHARE_MEDIA.WEIXIN_CIRCLE));
                        break;
                    case "3":
                        iconTitleList.add(new ShareIconTitleBean(R.drawable.share_qq_icon, "QQ", SHARE_MEDIA.QQ));
                        break;
                    case "4":
                        iconTitleList.add(new ShareIconTitleBean(R.drawable.share_sina_icon, "微博", SHARE_MEDIA.SINA));
                        break;
                    case "5":
                        iconTitleList.add(new ShareIconTitleBean(R.drawable.share_copy_icon, "复制链接", SHARE_MEDIA.POCKET));
                        break;
                }
            }
        }

        if (iconTitleList.size() == 2) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) communal_recycler_wrap.getLayoutParams();
            layoutParams.setMargins(AutoSizeUtils.mm2px(mAppContext, 125), 0, AutoSizeUtils.mm2px(mAppContext, 125), 0);
        }
        communal_recycler_wrap.setLayoutManager(new GridLayoutManager(context, iconTitleList.size() <= 4 ? iconTitleList.size() : 3));//4个及4个以内都是一行，超过4个才换行
        ShareIconTitleAdapter shareIconTitleAdapter = new ShareIconTitleAdapter(iconTitleList);
        communal_recycler_wrap.setAdapter(shareIconTitleAdapter);
        shareIconTitleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShareIconTitleBean shareIconTitleBean = (ShareIconTitleBean) view.getTag();
                if (shareIconTitleBean != null && alertSelectShareListener != null) {
                    alertSelectShareListener.selectShare(shareIconTitleBean);
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_alert_dialog_share;
    }

    @Override
    public void show(int gravity) {
        super.show(gravity);
        if (loadView != null) {
            loadView.setVisibility(View.GONE);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (loadView != null) {
            loadView.setVisibility(View.GONE);
        }
    }

    public void setLoading(int code) {
        if (loadView != null) {
            if (code == 1) {
                loadView.setVisibility(View.GONE);
            } else {
                loadView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setAlertSelectShareListener(AlertSelectShareListener alertSelectShareListener) {
        this.alertSelectShareListener = alertSelectShareListener;
    }

    public interface AlertSelectShareListener {
        void selectShare(ShareIconTitleBean shareIconTitleBean);
    }

    private class ShareIconTitleAdapter extends BaseQuickAdapter<ShareIconTitleBean, BaseViewHolder> {

        public ShareIconTitleAdapter(List<ShareIconTitleBean> iconTitleList) {
            super(R.layout.adapter_share_icon_title, iconTitleList);
        }

        @Override
        protected void convert(BaseViewHolder helper, ShareIconTitleBean shareIconTitleBean) {
            ImageView iv_share_icon = helper.getView(R.id.iv_share_icon);
            try {
                iv_share_icon.setImageResource(shareIconTitleBean.getShareIconResId());
            } catch (Exception e) {
                e.printStackTrace();
                iv_share_icon.setImageResource(R.drawable.load_loading_image);
            }
            helper.setText(R.id.tv_share_title, getStrings(shareIconTitleBean.getShareTitle()))
                    .itemView.setTag(shareIconTitleBean);
        }
    }
}
