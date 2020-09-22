package com.amkj.dmsh.mine.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.find.bean.PostEntity.PostBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.itemdecoration.NewGridItemDecoration;
import com.amkj.dmsh.views.TextViewSuffixWrapper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.dao.SoftApiDao.collectReport;
import static com.amkj.dmsh.dao.SoftApiDao.favorReport;

/**
 * Created by xiaoxin on 2020/8/22
 * Version:v4.7.0
 */
public class ReportContentAdapter extends BaseQuickAdapter<PostBean, BaseViewHolder> {
    private final Activity context;

    public ReportContentAdapter(Activity context, @Nullable List<PostBean> data) {
        super(R.layout.item_reprot_content, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PostBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadRoundImg(context, helper.getView(R.id.iv_head), item.getAvatar(), AutoSizeUtils.mm2px(mAppContext, 35));
        helper.setText(R.id.tv_user_name, getStrings(item.getNickName()))
                .setText(R.id.tv_publish_time, item.getCreateTime())
                .setGone(R.id.tv_follow, false)
                .setGone(R.id.tv_content, !TextUtils.isEmpty(item.getContent()));
        TextView mTvContent = helper.getView(R.id.tv_content);
        //点赞
        TextView tvLike = helper.getView(R.id.tv_like);
        tvLike.setSelected(item.isFavor());
        tvLike.setText(item.getFavorNum() > 0 ? String.valueOf(item.getFavorNum()) : "赞");
        tvLike.setOnClickListener(v -> favorReport(context, item, tvLike));
        //收藏
        TextView tvCollect = helper.getView(R.id.tv_collect);
        tvCollect.setSelected(item.isCollect());
        tvCollect.setOnClickListener(v -> collectReport(context, item, tvCollect));

        //显示图片
        if (!TextUtils.isEmpty(item.getImgs())) {
            RecyclerView rvImgs = helper.getView(R.id.rv_imgs);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            rvImgs.setLayoutManager(gridLayoutManager);
            if (rvImgs.getTag() == null) {
                rvImgs.addItemDecoration(new NewGridItemDecoration.Builder()
                        // 设置分隔线资源ID
                        .setDividerId(R.drawable.item_divider_five_white)
                        .create());
                rvImgs.setTag(item);
            }
            ImgsListAdapter reportImgsAdapter = new ImgsListAdapter(context, Arrays.asList(item.getImgs().split(",")), AutoSizeUtils.mm2px(mAppContext, 224));
            rvImgs.setAdapter(reportImgsAdapter);
        }

        //显示内容
        if (!TextUtils.isEmpty(item.getContent())) {
            mTvContent.setText(getStrings(item.getContent()));
            TextViewSuffixWrapper wrapper = new TextViewSuffixWrapper(mTvContent);
            wrapper.setMainContent(item.getContent());
            wrapper.setTargetLineCount(4);
            String suffix = "...查看全文";
            wrapper.setSuffix(suffix);
            wrapper.suffixColor("...".length(), suffix.length(), R.color.text_login_blue_z, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wrapper.toggle();

                }
            });
            wrapper.collapse(false);
        }
    }
}
