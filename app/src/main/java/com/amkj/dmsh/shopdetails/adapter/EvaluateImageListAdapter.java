package com.amkj.dmsh.shopdetails.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean.PictureBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;

/**
 * Created by xiaoxin on 2020/1/6
 * Version:v4.4.1
 * ClassDescription :商品评论图片适配器
 */

public class EvaluateImageListAdapter extends BaseMultiItemQuickAdapter<PictureBean, BaseViewHolder> {
    private final Context context;

    public EvaluateImageListAdapter(Context context, List<PictureBean> pictureBeanList) {
        super(pictureBeanList);
        addItemType(TYPE_1, R.layout.item_eva_img_single);
        addItemType(TYPE_2, R.layout.item_eva_img_not_single);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PictureBean pictureBean) {
        if (!TextUtils.isEmpty(pictureBean.getPath())) {
            GlideImageLoaderUtil.loadThumbCenterCrop(context, helper.getView(R.id.iv_eva_image), pictureBean.getPath(), "");
        }
        helper.itemView.setTag(R.id.iv_tag, pictureBean);
    }
}
