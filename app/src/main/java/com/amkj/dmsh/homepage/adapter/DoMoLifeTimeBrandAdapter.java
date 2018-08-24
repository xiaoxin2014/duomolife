package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.dominant.bean.DMLTimeDetailEntity.DMLTimeDetailBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * Created by atd48 on 2016/6/27.
 */
public class DoMoLifeTimeBrandAdapter extends BaseQuickAdapter<DMLTimeDetailBean, BaseViewHolderHelper> {

    private final Context context;

    public DoMoLifeTimeBrandAdapter(Context context, List<DMLTimeDetailBean> brandProductList) {
        super(R.layout.adapter_promotion_pro_item, brandProductList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, DMLTimeDetailBean dmlTimeDetailBean) {
        if (dmlTimeDetailBean.getQuantity() < 1) {
            helper.setGone(R.id.img_spring_sale_tag_out, true);
        } else {
            helper.setGone(R.id.img_spring_sale_tag_out, false);
        }
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.img_springSale_product), dmlTimeDetailBean.getPicUrl());
        helper.setText(R.id.tv_product_duomolife_price, "￥" + dmlTimeDetailBean.getPrice());
        helper.setText(R.id.tv_springSale_introduce, getStrings(dmlTimeDetailBean.getName()));
        TextView tv_product_original_price = helper.getView(R.id.tv_product_original_price);
        tv_product_original_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_product_original_price.setText("￥" + dmlTimeDetailBean.getMarketPrice());
        if (isTimeStart(dmlTimeDetailBean)) {
            helper.setGone(R.id.iv_pro_time_warm, false);
        } else {
            helper.setGone(R.id.iv_pro_time_warm, true);
        }
        helper.addOnClickListener(R.id.iv_pro_time_warm).setTag(R.id.iv_pro_time_warm, dmlTimeDetailBean);
        helper.itemView.setTag(dmlTimeDetailBean);
    }

    private boolean isTimeStart(DMLTimeDetailBean dmlTimeDetailBean) {
        try {
            //格式化开始时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date dateStart = formatter.parse(dmlTimeDetailBean.getStartTime());
            Date dateCurrent;
            if (!TextUtils.isEmpty(dmlTimeDetailBean.getCurrentTime())) {
                dateCurrent = formatter.parse(dmlTimeDetailBean.getCurrentTime());
            } else {
                dateCurrent = new Date();
            }
            if (dateCurrent.getTime() >= dateStart.getTime()) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
