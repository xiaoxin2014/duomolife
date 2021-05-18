package com.amkj.dmsh.dominant.adapter;

import android.app.Activity;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;

/**
 * Created by xiaoxin on 2021/5/13
 * Version:v5.0.0
 * ClassDescription :多么会员价
 */
public class VipPriceAdapter extends BaseQuickAdapter<LikedProductBean, BaseViewHolder> {
    private Activity context;

    public VipPriceAdapter(Activity activity, @Nullable List<LikedProductBean> data) {
        super(R.layout.item_vip_price, data);
        this.context = activity;
    }


    @Override
    protected void convert(BaseViewHolder helper, LikedProductBean likedProductBean) {
        if (likedProductBean == null) return;
        GlideImageLoaderUtil.loadSquareImg(context, helper.getView(R.id.iv_qt_pro_img)
                , likedProductBean.getPicUrl(), likedProductBean.getWaterRemark(), AutoSizeUtils.mm2px(mAppContext, 350));
        helper.setGone(R.id.iv_com_pro_tag_out, likedProductBean.getQuantity() < 1)
                .setText(R.id.tv_qt_pro_descrip, getStrings(likedProductBean.getSubtitle()))
                .setText(R.id.tv_qt_pro_name, !TextUtils.isEmpty(likedProductBean.getName()) ?
                        getStrings(likedProductBean.getName()) : getStrings(likedProductBean.getTitle()))
                .setText(R.id.tv_qt_pro_price, ConstantMethod.getRmbFormat(context, likedProductBean.getPrice()))
                .setText(R.id.tv_save, getStringsFormat(context, R.string.vip_save_money, likedProductBean.getVipReduce()))
                .setGone(R.id.ll_save, !TextUtils.isEmpty(likedProductBean.getVipReduce()));
        //点击商品进入详情
        helper.itemView.setOnClickListener(v -> skipProductUrl(context, likedProductBean.getType_id(), likedProductBean.getId(), likedProductBean.getAndroidLink()));
        helper.itemView.setTag(likedProductBean);
    }
}
