package com.amkj.dmsh.homepage.adapter;

import android.app.Activity;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dao.SoftApiDao;
import com.amkj.dmsh.homepage.bean.VideoProductEntity.VideoProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.userId;

/**
 * Created by xiaoxin on 2021/2/23
 * Version:v5.0.0
 * ClassDescription :视频商品列表适配器
 */
public class VideoProductAdapter extends BaseQuickAdapter<VideoProductBean, BaseViewHolder> {
    private Activity context;

    public VideoProductAdapter(Activity context, @Nullable List<VideoProductBean> data) {
        super(R.layout.item_video_product, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoProductBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_cover), item.getCoverPath());
        helper.setText(R.id.tv_title, getStrings(item.getTitle()))
                .setText(R.id.tv_price, ConstantMethod.getRmbFormat(context, item.getPrice() + "起"));
        helper.getView(R.id.tv_collect).setSelected("1".equals(item.getIsCollect()));
        TextView tvCollect = helper.getView(R.id.tv_collect);
        helper.getView(R.id.tv_collect).setOnClickListener(v -> {
            if (userId > 0) {
                SoftApiDao.collectVideo(context, item, tvCollect);
            } else {
                getLoginStatus(context);
            }
        });

        helper.itemView.setTag(item);
    }
}
