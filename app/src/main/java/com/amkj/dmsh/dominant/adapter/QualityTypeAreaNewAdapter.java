package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.dominant.bean.QualityHomeTypeEntity.QualityHomeTypeBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhy.autolayout.AutoLayoutInfo;
import com.zhy.autolayout.attr.HeightAttr;
import com.zhy.autolayout.attr.WidthAttr;
import com.zhy.autolayout.utils.AutoLayoutHelper;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/9
 * class description:请输入类描述
 */

public class QualityTypeAreaNewAdapter extends BaseMultiItemQuickAdapter<QualityHomeTypeBean, BaseViewHolder> {
    private final Context context;
    private final List<QualityHomeTypeBean> qualityHomeTypeList;
    private int widthPx;

    public QualityTypeAreaNewAdapter(Context context, List<QualityHomeTypeBean> qualityHomeTypeList) {
        super(qualityHomeTypeList);
        addItemType(ConstantVariable.TYPE_0, R.layout.adapter_ql_area_type);
        addItemType(ConstantVariable.TYPE_1, R.layout.adapter_ql_center_type);
        addItemType(ConstantVariable.TYPE_2, R.layout.adapter_ql_center_hori_type);
        this.context = context;
        this.qualityHomeTypeList = qualityHomeTypeList;
    }

    @Override
    protected void convert(final BaseViewHolder helper, QualityHomeTypeBean qualityHomeTypeBean) {
        switch (helper.getItemViewType()) {
            case ConstantVariable.TYPE_1:
            case ConstantVariable.TYPE_2:
                final ImageView iv_type_center = helper.getView(R.id.iv_type_center);
                final FrameLayout fl_type_center = helper.getView(R.id.fl_type_center);
                final int layoutPosition = helper.getLayoutPosition();
                iv_type_center.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        iv_type_center.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        AutoLayoutHelper.AutoLayoutParams layoutParams = (AutoLayoutHelper.AutoLayoutParams) iv_type_center.getLayoutParams();
                        AutoLayoutInfo autoLayoutInfo = layoutParams.getAutoLayoutInfo();
                        if (qualityHomeTypeList.size() == 5) {
                            if (layoutPosition == 0) {
                                widthPx = 148;
                            } else {
                                widthPx = 110;
                            }
                        } else if (qualityHomeTypeList.size() == 4) {
                            widthPx = 142;
                        } else if (qualityHomeTypeList.size() == 3) {
                            if (layoutPosition == 0) {
                                widthPx = 240;
                            } else {
                                widthPx = 142;
                            }
                        }
                        autoLayoutInfo.addAttr(new WidthAttr(widthPx, 0, 0));
                        autoLayoutInfo.addAttr(new HeightAttr(widthPx, 1, 0));
                        FrameLayout.LayoutParams layoutParamsV = (FrameLayout.LayoutParams) iv_type_center.getLayoutParams();
                        if (qualityHomeTypeList.size() == 5 && layoutPosition == 0) {
                            layoutParamsV.setMargins(0, 0, AutoSizeUtils.mm2px(mAppContext,47), 0);
                        } else {
                            layoutParamsV.setMargins(0, 0, 0, 0);
                        }
                        iv_type_center.setLayoutParams(layoutParamsV);
                    }
                });
//                if (qualityHomeTypeList.size() == 3 && layoutPosition == 0) {
//                    fl_type_center.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                        @Override
//                        public void onGlobalLayout() {
//                            fl_type_center.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                            AutoLayoutHelper.AutoLayoutParams layoutParams = (AutoLayoutHelper.AutoLayoutParams) fl_type_center.getLayoutParams();
//                            AutoLayoutInfo autoLayoutInfo = layoutParams.getAutoLayoutInfo();
//                            autoLayoutInfo.addAttr(new WidthAttr(375, 0, 0));
//                            ViewGroup.LayoutParams layoutParamsF = fl_type_center.getLayoutParams();
//                            fl_type_center.setLayoutParams(layoutParamsF);
//                        }
//                    });
//                } else {
//                    fl_type_center.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                        @Override
//                        public void onGlobalLayout() {
//                            fl_type_center.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                            AutoLayoutHelper.AutoLayoutParams layoutParams = (AutoLayoutHelper.AutoLayoutParams) fl_type_center.getLayoutParams();
//                            AutoLayoutInfo autoLayoutInfo = layoutParams.getAutoLayoutInfo();
//                            autoLayoutInfo.addAttr(new HeightAttr(192, 1, 0));
//                            ViewGroup.LayoutParams layoutParamsF = fl_type_center.getLayoutParams();
//                            fl_type_center.setLayoutParams(layoutParamsF);
//                        }
//                    });
//                }
                GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_type_center), qualityHomeTypeBean.getPicUrl());
                TextView tv_type_center_name = helper.getView(R.id.tv_type_center_name);
                try {
                    tv_type_center_name.setTextColor(Color.parseColor("#" + (!TextUtils.isEmpty(qualityHomeTypeBean.getColor()) ? qualityHomeTypeBean.getColor() : "f7586a")));
                } catch (Exception e) {
                    tv_type_center_name.setTextColor(Color.parseColor("#f7586a"));
                    e.printStackTrace();
                }
                tv_type_center_name.setText(getStrings(qualityHomeTypeBean.getTitle()));
                helper.setText(R.id.tv_type_center_description, getStrings(qualityHomeTypeBean.getSubtitle()));
                break;
            default:
                GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.cir_img_type_area), qualityHomeTypeBean.getPicUrl());
                helper.setText(R.id.tv_type_area_name, (getStrings(qualityHomeTypeBean.getTitle())))
                        .setText(R.id.tv_type_area_description, getStrings(qualityHomeTypeBean.getSubtitle()));
                break;
        }
        helper.itemView.setTag(qualityHomeTypeBean);
    }
}
