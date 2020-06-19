package com.amkj.dmsh.views.bottomdialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import androidx.core.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.ImageBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.activity.ImagePagerActivity;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.EditGoodsSkuEntity.EditGoodsSkuBean;
import com.amkj.dmsh.shopdetails.bean.PropsBean;
import com.amkj.dmsh.shopdetails.bean.PropvaluesBean;
import com.amkj.dmsh.shopdetails.bean.ShopCarGoodsSku;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean.CombineProductInfoBean;
import com.amkj.dmsh.shopdetails.bean.SkuSaleBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.RectAddAndSubViewDirect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.inflate;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.formatFloatNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getFloatNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;


/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/5/9
 * class description:sku
 */
public class SkuDialog implements KeywordContainer.OnClickKeywordListener {
    private EditGoodsSkuBean editGoodsSkuBean;
    private Activity baseAct;
    private Dialog dialog;
    private DataListener listener;
    private DismissListener dismissListener;
    //  存储Sku属性Id组合值
    private final Map<String, List<String>> skuIdGroup = new HashMap<>();

    private final Map<String, List<String>> proValuesQueryList = new HashMap<>();
    //  相同父类Sku属性
    private final Map<String, List<String>> propIdList = new HashMap<>();
    //  全部属性
    private List<String> allSkuValuesList = new ArrayList<>();
    //    有库存的属性
    private List<String> allQualitySkuValuesList = new ArrayList<>();
    //    无库存sku属性
    private List<ProductParameterValueBean> nonNullAllList = new ArrayList<>();

    List<String> skuString = new ArrayList<>();
//

    private View skuItem;
    private TextView tv_title_is;
    private KeywordContainer kc_params_as;
    private ArrayList<ProductParameterTypeBean> productParameterTypeBeanList;
    private List<SkuSaleBean> skuSaleList;
    private List<PropvaluesBean> propValuesList;
    private List<PropsBean> propsList;
    private RectAddAndSubViewDirect rectAddAndSubViewDirect;
    private int numCount;
    private SkuDialogView skuDialogView;
    private boolean isShowSingle;
    //    属性图片
    private List<ImageBean> picValueList = new ArrayList<>();
    private Map<Integer, Float> discountMap;
    private boolean isSelectNotice;
    private int noticeType;
    private RelativeLayout rel_rect_count;
    private NestedScrollView scrollView;

    /**
     * skuValues ID对应的ProValues值;
     */

    public SkuDialog(Activity baseAct) {
        this.baseAct = baseAct;
    }

    /**
     * 单个按钮 默认 确定字段
     */
    public void show() {
        if (dialog == null) {
            init();
            getSingleDouble(true, false, "确定", 0);
        }
        if (!isShowSingle) {
            getSingleDouble(true, editGoodsSkuBean != null && editGoodsSkuBean.isSellStatus(), "确定", noticeType);
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * sku展示，单个按钮加入购物车
     *
     * @param isShowSingle
     * @param singleString
     */
    public void show(boolean isShowSingle, String singleString) {
        if (dialog == null) {
            init();
            getSingleDouble(isShowSingle, false, TextUtils.isEmpty(singleString) ? "确定" : singleString, 0);
        }
        if (isShowSingle != this.isShowSingle) {
            getSingleDouble(isShowSingle, false, TextUtils.isEmpty(singleString) ? "确定" : singleString, noticeType);
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * sku展示，两个按钮，判断是否待售
     *
     * @param isShowSingle
     * @param isSellStatus
     * @param singleString
     */
    public void show(boolean isShowSingle, boolean isSellStatus, String singleString) {
        if (dialog == null) {
            init();
            getSingleDouble(isShowSingle, isSellStatus, TextUtils.isEmpty(singleString) ? "确定" : singleString, 0);
        }
        if (isShowSingle != this.isShowSingle) {
            getSingleDouble(isShowSingle, isSellStatus, TextUtils.isEmpty(singleString) ? "确定" : singleString, noticeType);
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void init() {
        dialog = new Dialog(baseAct, R.style.custom_dialog);
        dialog.setCanceledOnTouchOutside(true);
        //获取对话框的窗口，并设置窗口参数
        Window win = dialog.getWindow();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // 不能写成这样,否则Dialog显示不出来
        // LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        //对话框窗口的宽和高
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //对话框窗口显示的位置
        params.x = 120;
        params.y = 100;
        win.setAttributes(params);
        //设置对话框布局
        View view = LayoutInflater.from(dialog.getContext()).inflate(R.layout.sku_layout_product, (ViewGroup) dialog.getWindow().getDecorView(), false);
        scrollView = view.findViewById(R.id.scroll_sku_values);
        dialog.setContentView(view);
        skuDialogView = new SkuDialogView();
        ButterKnife.bind(skuDialogView, view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getBackCloseKey();
            }
        });
    }

    //监听sku弹窗
    public void setDismissListener(DismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    //   刷新数据
    public void refreshView(EditGoodsSkuBean editGoodsSkuBean) {
        if (editGoodsSkuBean != null) {
            this.editGoodsSkuBean = editGoodsSkuBean;
        }
        if (dialog == null) {
            init();
        }
        if (editGoodsSkuBean != null && editGoodsSkuBean.getSkuSale() != null && editGoodsSkuBean.getSkuSale().size() > 0) {
            SkuSaleBean skuSaleBean = editGoodsSkuBean.getSkuSale().get(0);
            String[] skuValues = skuSaleBean.getPropValues().split(",");
            if (skuValues.length > 1) {
                scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        ViewGroup.LayoutParams layoutParams = scrollView.getLayoutParams();
                        layoutParams.height = AutoSizeUtils.mm2px(mAppContext, 600);
                        scrollView.setLayoutParams(layoutParams);
                    }
                });
            }
        }
        List<SkuSaleBean> skuSaleList = this.editGoodsSkuBean.getSkuSale();
        final ProductSkuBean productSkuBean = getSkuShow(this.editGoodsSkuBean);
        //         默认显示图片
        GlideImageLoaderUtil.loadCenterCrop(baseAct.getApplicationContext(), skuDialogView.rImg_direct_attribute_product, this.editGoodsSkuBean.getPicUrl());
        if (picValueList != null) {
            picValueList.clear();
            ImageBean imageBean = new ImageBean();
            imageBean.setPicUrl(getStrings(this.editGoodsSkuBean.getPicUrl()));
            imageBean.setSkuValue(getStrings(this.editGoodsSkuBean.getProductName()));
            picValueList.add(imageBean);
        }
        if (!TextUtils.isEmpty(editGoodsSkuBean.getMaxDiscounts())) {
            skuDialogView.tv_pro_combine_discount.setVisibility(View.VISIBLE);
            skuDialogView.tv_pro_combine_discount.setText(
                    String.format(baseAct.getString(R.string.sku_max_discount_price)
                            , editGoodsSkuBean.getMaxDiscounts()));
            discountMap = initDiscountPrice(editGoodsSkuBean.getCombineProductInfoList());
        } else {
            skuDialogView.tv_pro_combine_discount.setVisibility(View.GONE);
        }
        for (int i = 0; i < this.editGoodsSkuBean.getPropvalues().size(); i++) {
            PropvaluesBean propValuesBean = this.editGoodsSkuBean.getPropvalues().get(i);
            if (!TextUtils.isEmpty(propValuesBean.getPropValueUrl())
                    && !TextUtils.isEmpty(propValuesBean.getPropValueName())) {
                ImageBean imageBean = new ImageBean();
                imageBean.setSkuValue(getStrings(propValuesBean.getPropValueName()));
                imageBean.setPicUrl(getStrings(propValuesBean.getPropValueUrl()));
                picValueList.add(imageBean);
            }
        }
        skuDialogView.rImg_direct_attribute_product.setTag(R.id.iv_tag, getStrings(this.editGoodsSkuBean.getPicUrl()));
        skuDialogView.tv_dir_indent_pro_name.setText(this.editGoodsSkuBean.getProductName());
        skuDialogView.tv_dir_indent_pro_quality.setSelected(true);
        skuDialogView.tv_dir_indent_pro_quality.setText(editGoodsSkuBean.getQuantity() > 0 ? ("库存:" + editGoodsSkuBean.getQuantity()) : "");
        //        获取价格排序范围
        Collections.sort(skuSaleList, new Comparator<SkuSaleBean>() {
            @Override
            public int compare(SkuSaleBean lhs, SkuSaleBean rhs) {
                if (!TextUtils.isEmpty(lhs.getPrice()) && !TextUtils.isEmpty(rhs.getPrice())) {
                    float p1 = getFloatNumber(lhs.getPrice());
                    float p2 = getFloatNumber(rhs.getPrice());
                    return Float.compare(p1, p2);
                } else {
                    return 0;
                }
            }
        });
        getSingleDouble(editGoodsSkuBean.isShowBottom(), editGoodsSkuBean.isSellStatus(), "确定", 0);
        SkuSaleBean firstSaleBean = skuSaleList.get(0);
        String minPrice = skuSaleList.get(0).getPrice();
        String maxPrice = skuSaleList.get(skuSaleList.size() - 1).getPrice();
        String newUserTag = getStrings(firstSaleBean.getNewUserTag());
        skuDialogView.tv_dir_indent_pro_price.setTextColor(Color.parseColor(!TextUtils.isEmpty(firstSaleBean.getNewUserTag()) ? "#ff5e6b" : "#333000"));
        if (skuSaleList.size() > 1) {
            if (minPrice.equals(maxPrice)) {
                skuDialogView.tv_dir_indent_pro_price.setText((newUserTag + "¥" + minPrice));
            } else {
                skuDialogView.tv_dir_indent_pro_price.setText((newUserTag + "¥" + minPrice + " - " + maxPrice));
            }
        } else {
            skuDialogView.tv_dir_indent_pro_price.setText((newUserTag + "¥" + minPrice));
        }
        if (skuDialogView.layout_parameter_slp.getChildCount() > 0) {
            skuDialogView.layout_parameter_slp.removeAllViews();
        }
        productParameterTypeBeanList = productSkuBean.getParameters();
        if (editGoodsSkuBean.isShopCarEdit()) {
            selectedSkuValueId.clear();
            String[] splitValues = new String[0];
            int skuId = editGoodsSkuBean.getSkuId();
            for (int i = 0; i < editGoodsSkuBean.getSkuSale().size(); i++) {
                if (skuId == editGoodsSkuBean.getSkuSale().get(i).getId()) {
                    splitValues = editGoodsSkuBean.getSkuSale().get(i).getPropValues().split(",");
                }
            }
            for (int i = 0; i < splitValues.length; i++) {
                for (int j = 0; j < productParameterTypeBeanList.size(); j++) {
                    ProductParameterTypeBean productParameterTypeBean = productParameterTypeBeanList.get(j);
                    for (int k = 0; k < productParameterTypeBean.getValues().size(); k++) {
                        ProductParameterValueBean productParameterValueBean = productParameterTypeBean.getValues().get(k);
                        if (splitValues[i].equals(String.valueOf(productParameterValueBean.getPropId()))) {
                            productParameterValueBean.setSelected(true);
                            selectedSkuValueId.add(productParameterValueBean);
                            break;
                        }
                    }
                }
            }
            getShopCarSkuValueId();
        }
        setSkuStatusViews(productParameterTypeBeanList);
        for (int i = 0; i < productParameterTypeBeanList.size(); i++) {
            ProductParameterTypeBean productParameterTypeBean = productParameterTypeBeanList.get(i);
            for (int j = 0; j < productParameterTypeBean.getValues().size(); j++) {
                ProductParameterValueBean productParameterValueBean = productParameterTypeBean.getValues().get(j);
                if (productParameterValueBean.isSelected()
                        && productParameterValueBean.getPropId() == selectedSkuValueId.get(selectedSkuValueId.size() - 1).getPropId()) {
                    getSelectedSku(productParameterValueBean);
                }
            }
        }
    }

    private void setSkuQuality(int quantity, String skuProp) {
        if (noticeType == 1 || noticeType == 2) {
            skuDialogView.tv_dir_indent_pro_quality.setSelected(false);
            skuDialogView.tv_dir_indent_pro_quality.setText("缺货");
            if (selectedSkuValueId.size() > 0) {
                for (ProductParameterTypeBean productParameterTypeBean : productParameterTypeBeanList) {
                    for (ProductParameterValueBean productParameterValueBean : productParameterTypeBean.getValues()) {
                        if (skuProp.contains(String.valueOf(productParameterValueBean.getPropId())) && productParameterValueBean.getNotice() == 0) {
                            productParameterValueBean.setNotice(1);
                        }
                    }
                }
            }
        } else {
            skuDialogView.tv_dir_indent_pro_quality.setSelected(true);
            skuDialogView.tv_dir_indent_pro_quality.setText(quantity > 0 ? ("库存：" + quantity) : "");
            for (ProductParameterTypeBean productParameterTypeBean : productParameterTypeBeanList) {
                for (ProductParameterValueBean productParameterValueBean : productParameterTypeBean.getValues()) {
                    if (skuProp.contains(String.valueOf(productParameterValueBean.getPropId())) && productParameterValueBean.getNotice() != 0) {
                        productParameterValueBean.setNotice(0);
                    }
                }
            }
        }
        getSingleDouble(isShowSingle, editGoodsSkuBean != null && editGoodsSkuBean.isSellStatus(), "", noticeType);
    }

    /**
     * 优惠金额
     *
     * @param combineProductInfoList
     * @return
     */
    private Map<Integer, Float> initDiscountPrice(List<CombineProductInfoBean> combineProductInfoList) {
        Map<Integer, Float> discountMap = new HashMap<>();
        if (combineProductInfoList != null && combineProductInfoList.size() > 0) {
            for (int i = 0; i < combineProductInfoList.size(); i++) {
                CombineProductInfoBean combineProductInfoBean = combineProductInfoList.get(i);
                try {
                    discountMap.put(combineProductInfoBean.getSkuId(), getFloatNumber(combineProductInfoBean.getDiscounts()));
                } catch (NumberFormatException e) {
                    discountMap.put(combineProductInfoBean.getSkuId(), (float) 0);
                    e.printStackTrace();
                }
            }
        }
        return discountMap;
    }

    /**
     * 底栏是否显示单个按钮
     *
     * @param showSingle
     * @param isSellStatus
     * @param singleString
     */
    private void getSingleDouble(boolean showSingle, boolean isSellStatus, String singleString, int notice) {
        isShowSingle = showSingle;
        skuDialogView.bt_direct_attribute_buy.setSelected(false);
        skuDialogView.bt_direct_attribute_car.setSelected(false);
        if (showSingle) {
            if (notice == 1 || notice == 2) {
                skuDialogView.bt_direct_attribute_buy.setSelected(true);
                skuDialogView.bt_direct_attribute_buy.setEnabled(true);
                skuDialogView.bt_direct_attribute_buy.setText(notice == 1 ? baseAct.getText(R.string.sku_notice) : baseAct.getText(R.string.sku_cancel_notice));
                skuDialogView.bt_direct_attribute_car.setVisibility(View.GONE);
            } else {
                skuDialogView.bt_direct_attribute_buy.setSelected(false);
                skuDialogView.bt_direct_attribute_buy.setEnabled(true);
                skuDialogView.bt_direct_attribute_buy.setText(TextUtils.isEmpty(singleString) ? "确定" : singleString);
                skuDialogView.bt_direct_attribute_car.setVisibility(View.GONE);
            }
        } else {
            if (notice == 1 || notice == 2) {
                skuDialogView.bt_direct_attribute_car.setVisibility(View.VISIBLE);
                skuDialogView.bt_direct_attribute_car.setSelected(true);
                skuDialogView.bt_direct_attribute_car.setEnabled(true);
                skuDialogView.bt_direct_attribute_car.setText(notice == 1 ? baseAct.getText(R.string.sku_notice) : baseAct.getText(R.string.sku_cancel_notice));

                skuDialogView.bt_direct_attribute_buy.setEnabled(false);
                skuDialogView.bt_direct_attribute_buy.setText("已抢光");
            } else {
                skuDialogView.bt_direct_attribute_car.setVisibility(View.VISIBLE);
                skuDialogView.bt_direct_attribute_car.setText("加入购物车");
                if (isSellStatus) {
                    skuDialogView.bt_direct_attribute_buy.setEnabled(false);
                    skuDialogView.bt_direct_attribute_buy.setText("待售");
                } else {
                    skuDialogView.bt_direct_attribute_buy.setEnabled(true);
                    skuDialogView.bt_direct_attribute_buy.setText("立即购买");
                }
            }
        }
    }

    private void getShopCarSkuValueId() {
        ArrayList<ProductParameterValueBean> newValues;
        if (selectedSkuValueId.size() > 1) {
            canSelectedGroup = skuIdGroup.get(String.valueOf(selectedSkuValueId.get(selectedSkuValueId.size() - 1).getPropId()));
            for (int i = selectedSkuValueId.size() - 2; i >= 0; i--) {
                canSelectedGroup = getSameValue(canSelectedGroup, skuIdGroup.get(String.valueOf(selectedSkuValueId.get(i).getPropId())));
            }
        } else if (selectedSkuValueId.size() > 0) {
            canSelectedGroup = skuIdGroup.get(String.valueOf(selectedSkuValueId.get(0).getPropId()));
        }
        if (canSelectedGroup != null) {
            canSelected = getSplitGroup(canSelectedGroup);
            for (int j = 0; j < productParameterTypeBeanList.size(); j++) {
                newValues = productParameterTypeBeanList.get(j).getValues();
                if (selectedSkuValueId.size() < 1) {
                    for (int i = 0; i < newValues.size(); i++) {
                        newValues.get(i).setNull(true);
                    }
                } else {
                    for (int i = 0; i < newValues.size(); i++) {
                        if (productParameterTypeBeanList.get(j).getTypeId() != selectedSkuValueId.get(selectedSkuValueId.size() - 1).getParentId()) {
                            for (int k = 0; k < canSelected.size(); k++) {
                                if (newValues.get(i).getPropId() == Integer.parseInt(canSelected.get(k))) {
                                    newValues.get(i).setNull(true);
                                    break;
                                } else if (k == canSelected.size() - 1) {
                                    newValues.get(i).setNull(false);
                                }
                            }
                        }
                    }
                }
                productParameterTypeBeanList.get(j).setValues(newValues);
            }
        }
    }

    /**
     * 设置商品属性信息
     *
     * @param productParameterTypeBeanList
     */
    private void setSkuStatusViews(final ArrayList<ProductParameterTypeBean> productParameterTypeBeanList) {
        if (skuDialogView.layout_parameter_slp.getChildCount() > 0)
            skuDialogView.layout_parameter_slp.removeAllViews();
        for (int i = 0; i < productParameterTypeBeanList.size(); i++) {
            final ProductParameterTypeBean productParameterTypeBean = productParameterTypeBeanList.get(i);
            skuItem = inflate(baseAct, R.layout.sku_item, null);
            tv_title_is = (TextView) skuItem.findViewById(R.id.tv_title_is);
            kc_params_as = (KeywordContainer) skuItem.findViewById(R.id.kc_params_as);
            //            商品属性类型
            tv_title_is.setText((productParameterTypeBean.getTypeName() + ":"));
            kc_params_as.setKeywordViewFactory(new KeywordContainer.KeywordViewFactory() {
                @Override
                public CheckedTextView makeKeywordView(int proId, String proValue) {
                    CheckedTextView textView = (CheckedTextView) View.inflate(baseAct, R.layout.sku_checkbox, null);
                    LinearLineWrapLayout.LayoutParams params = new LinearLineWrapLayout.LayoutParams(LinearLineWrapLayout.LayoutParams.WRAP_CONTENT, LinearLineWrapLayout.LayoutParams.WRAP_CONTENT);
                    params.rightMargin = 21;
                    params.bottomMargin = 23;
                    textView.setLayoutParams(params);
                    return textView;
                }
            });
            kc_params_as.setKeywords(productParameterTypeBean.getValues());
            kc_params_as.setOnClickKeywordListener(this);
            skuDialogView.layout_parameter_slp.addView(skuItem);
        }
        if (rectAddAndSubViewDirect != null) {
            numCount = rectAddAndSubViewDirect.getNum();
        }
        View numCountView = LayoutInflater.from(baseAct).inflate(R.layout.layout_direct_count_num, (ViewGroup) skuDialogView.layout_parameter_slp.getParent(), false);
        rectAddAndSubViewDirect = numCountView.findViewById(R.id.rect_number);
        rel_rect_count = numCountView.findViewById(R.id.rel_rect_count);
        if (numCount == 0) {
            numCount = editGoodsSkuBean.getOldCount();
        }
        numCountView.setVisibility(numCount < 1 || editGoodsSkuBean.isCombine() ? View.GONE : View.VISIBLE);
        rectAddAndSubViewDirect.tv_direct_number_layout.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoSizeUtils.mm2px(mAppContext, 28));
        rectAddAndSubViewDirect.tv_direct_number_layout.setTextColor(baseAct.getResources().getColor(R.color.text_black_t));
        rectAddAndSubViewDirect.setNum(numCount == 0 ? 1 : numCount);
        rectAddAndSubViewDirect.setOnNumChangeListener(new RectAddAndSubViewDirect.OnNumChangeListener() {
            @Override
            public void onNumChange(View view, int stype, int num) {
                int qualityNumber = getNumber(getStrings(skuDialogView.tv_dir_indent_pro_quality.getText().toString().trim()));
                if (num > qualityNumber) {
                    rectAddAndSubViewDirect.setNum(qualityNumber);
                }
            }

            @Override
            public void onMaxQuantity(View view, int num) {
                showToast(R.string.product_sell_out);
            }
        });

        if (!editGoodsSkuBean.isCombine()) {
            skuDialogView.layout_parameter_slp.addView(numCountView);
        }
    }

    //    转换成数字
    private int getNumber(String str) {
        String regex = "\\d*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            if (!"".equals(m.group()))
                return Integer.parseInt(m.group());
        }
        return 0;
    }

    /**
     * 处理商品属性信息
     *
     * @param editGoodsSkuBean
     * @return
     */
    private ProductSkuBean getSkuShow(EditGoodsSkuBean editGoodsSkuBean) {
        //        销售组合
        skuSaleList = editGoodsSkuBean.getSkuSale();
        //        商品属性
        propValuesList = editGoodsSkuBean.getPropvalues();
        //        商品类型
        propsList = editGoodsSkuBean.getProps();
        ProductSkuBean productSkuBean = new ProductSkuBean();
        ProductParameterTypeBean productParameterTypeBean;
        //        类型集合
        ArrayList<ProductParameterTypeBean> productParameterTypes = new ArrayList<>();
        //        每种类型 的属性值
        ArrayList<ProductParameterValueBean> productParameterValues;
        for (int j = 0; j < propsList.size(); j++) {
            productParameterTypeBean = new ProductParameterTypeBean();
            //            商品类型名称
            productParameterTypeBean.setTypeName(propsList.get(j).getPropName());
            //            商品类型ID
            productParameterTypeBean.setTypeId(propsList.get(j).getPropId());
            productParameterValues = new ArrayList<>();
            ProductParameterValueBean productParameterValueBean;
            for (int i = 0; i < propValuesList.size(); i++) {
                productParameterValueBean = new ProductParameterValueBean();
                if (propsList.get(j).getPropId() == propValuesList.get(i).getPropId()) {
                    productParameterValueBean.setPropId(propValuesList.get(i).getPropValueId());
                    productParameterValueBean.setValueName(propValuesList.get(i).getPropValueName());
                    productParameterValueBean.setPropValueUrl(propValuesList.get(i).getPropValueUrl());
                    productParameterValueBean.setParentId(propValuesList.get(i).getPropId());
                    productParameterValueBean.setParentTypeName(propsList.get(j).getPropName());
                    productParameterValues.add(productParameterValueBean);
                }
                //                    判断key是否已创建
                PropvaluesBean propValuesBean = propValuesList.get(i);
                if (!propIdList.containsKey(String.valueOf(propValuesBean.getPropId()))) {
                    skuString = new ArrayList<>();
                    skuString.add(String.valueOf(propValuesBean.getPropValueId()));
//                            添加相同类型的Values值
                    propIdList.put(String.valueOf(propValuesBean.getPropId()), skuString);
                } else {
                    //                            已创建 比对里面的值是否跟现在的值是否相等
                    List<String> strings = propIdList.get(String.valueOf(propValuesBean.getPropId()));
                    for (int l = 0; l < strings.size(); l++) {
                        if (strings.get(l).equals(String.valueOf(propValuesBean.getPropValueId()))) {
                            break;
                        } else if (l == strings.size() - 1) {
                            propIdList.get(String.valueOf(propValuesBean.getPropId())).add(String.valueOf(propValuesBean.getPropValueId()));
                            break;
                        }
                    }
                }
            }
            productParameterTypeBean.setValues(productParameterValues);
            productParameterTypes.add(productParameterTypeBean);
        }
        for (int k = 0; k < productParameterTypes.size(); k++) {
            ArrayList<ProductParameterValueBean> values = productParameterTypes.get(k).getValues();
            for (int i = 0; i < values.size(); i++) {
                List<String> strings = new ArrayList<>();
                for (int j = 0; j < values.size(); j++) {
                    strings.add(String.valueOf(values.get(j).getPropId()));
                }
                proValuesQueryList.put(String.valueOf(values.get(i).getPropId()), strings);
            }
        }
        allSkuValuesList = new ArrayList<>();
        allQualitySkuValuesList = new ArrayList<>();
        for (int i = 0; i < skuSaleList.size(); i++) {
//            全部Sku属性
            SkuSaleBean skuSaleBean = skuSaleList.get(i);
            String[] splitProValues = skuSaleBean.getPropValues().split(",");
            if (allSkuValuesList.size() < 1) {
                allSkuValuesList.addAll(Arrays.asList(splitProValues));
            } else {
                allSkuValuesList = getDifferentValue(allSkuValuesList, Arrays.asList(splitProValues));
            }
//            有库存的Sku
            if (skuSaleBean.getQuantity() > 0 || skuSaleBean.getIsNotice() == 1 || skuSaleBean.getIsNotice() == 2) {
                if (allQualitySkuValuesList.size() < 1) {
                    allQualitySkuValuesList.addAll(Arrays.asList(splitProValues));
                } else {
                    allQualitySkuValuesList = getDifferentValue(allQualitySkuValuesList, Arrays.asList(splitProValues));
                }
                String[] splitSkuProValues = skuSaleBean.getPropValues().split(",");
                List<String> skuValuesContrast = Arrays.asList(splitSkuProValues);
                Collections.sort(skuValuesContrast, new Comparator<String>() {
                    @Override
                    public int compare(String lhs, String rhs) {
                        if (TextUtils.isEmpty(lhs) || TextUtils.isEmpty(rhs)) {
                            return 0;
                        } else {
                            return Integer.compare(Integer.parseInt(lhs), Integer.parseInt(rhs));
                        }
                    }
                });

                for (int j = 0; j < skuValuesContrast.size(); j++) {
                    StringBuffer stringGroup = new StringBuffer();
                    for (int k = 0; k < skuValuesContrast.size(); k++) {
                        if (k == 0) {
                            stringGroup.append(skuValuesContrast.get(k));
                        } else {
                            stringGroup.append("," + skuValuesContrast.get(k));
                        }
                    }
//                    判断key是否已创建
                    if (!skuIdGroup.containsKey(skuValuesContrast.get(j))) {
                        skuString = new ArrayList<>();
                        skuString.add(String.valueOf(stringGroup));
//                            添加相同类型的Values值
                        skuIdGroup.put(skuValuesContrast.get(j), skuString);
                    } else {
                        //                            已创建 比对里面的值是否跟现在的值是否相等
                        List<String> strings = skuIdGroup.get(skuValuesContrast.get(j));
                        for (int l = 0; l < strings.size(); l++) {
                            if (strings.get(l).equals(String.valueOf(stringGroup))) {
                                break;
                            } else if (l == strings.size() - 1) {
//                                skuIdGroup.get(splitProValues[j]).add(String.valueOf(stringGroup));
                                skuIdGroup.get(skuValuesContrast.get(j)).add(String.valueOf(stringGroup));
                                break;
                            }
                        }
                    }
                }

                StringBuffer splitValues = new StringBuffer();
                for (int j = 0; j < skuValuesContrast.size(); j++) {
                    if (j == 0) {
                        splitValues.append(skuValuesContrast.get(j));
                    } else {
                        splitValues.append("," + skuValuesContrast.get(j));
                    }
                }
//                设置排过序的SkuSale;
                skuSaleList.get(i).setPropValues(String.valueOf(splitValues));
            }
        }
//        相同ProValues()
        /**
         * 格式化无库存sku属性集合
         */
        nonNullAllList.clear();
        for (int i = 0; i < productParameterTypes.size(); i++) {
            ArrayList<ProductParameterValueBean> values = productParameterTypes.get(i).getValues();
            for (int j = 0; j < values.size(); j++) {
                ProductParameterValueBean productParameterValueBean = values.get(j);
                if (allQualitySkuValuesList.size() > 0) {
                    for (int k = 0; k < allQualitySkuValuesList.size(); k++) {
                        if (productParameterValueBean.getPropId() == Integer.parseInt(allQualitySkuValuesList.get(k))) {
                            productParameterValueBean.setNull(true);
                            break;
                        } else if (k == allQualitySkuValuesList.size() - 1) {
                            productParameterValueBean.setNull(false);
                            nonNullAllList.add(productParameterValueBean);
                        }
                    }
                } else {
                    productParameterValueBean.setNull(false);
                    nonNullAllList.add(productParameterValueBean);
                }
            }
            productParameterTypes.get(i).setValues(values);
        }
        productSkuBean.setParameters(productParameterTypes);
        return productSkuBean;
    }

    private void getBackCloseKey() {
        if (selectedSkuValueId.size() != editGoodsSkuBean.getProps().size() || isSelectNotice) {
            if (listener != null) {
                listener.getSkuProperty(null);
            }
        } else if (selectedSkuValueId.size() == editGoodsSkuBean.getProps().size() && dismissListener != null) {
            //sku弹窗消失时，如果选择了sku，刷新skuValue
            getCheckSelectedValue("");
            dismissListener.updateSkuValue(shopCarGoodsSku);
        }
        dialog.cancel();
    }

    private ShopCarGoodsSku shopCarGoodsSku = null;

    /**
     * 检查已选择属性
     *
     * @param type
     */
    private void getCheckSelectedValue(String type) {
        int numQuantity = rectAddAndSubViewDirect.getNum();
        String[] splitSize = editGoodsSkuBean.getSkuSale().get(0).getPropValues().split(",");
        Collections.sort(selectedSkuValueId, new Comparator<ProductParameterValueBean>() {
            @Override
            public int compare(ProductParameterValueBean lhs, ProductParameterValueBean rhs) {
                int propId1 = lhs.getPropId();
                int propId2 = rhs.getPropId();
                return Integer.compare(propId1, propId2);
            }
        });
        if (selectedSkuValueId.size() == splitSize.length) {
            StringBuffer valuesAppend = new StringBuffer();
            StringBuffer valuesNameAppend = new StringBuffer();
            String picUrl = null;
            for (int i = 0; i < selectedSkuValueId.size(); i++) {
                if (i != 0) {
                    valuesAppend.append(",");
                    valuesNameAppend.append(",");
                }
                valuesAppend.append(selectedSkuValueId.get(i).getPropId());
                valuesNameAppend.append((selectedSkuValueId.get(i).getParentTypeName() + ":" + selectedSkuValueId.get(i).getValueName()));
                picUrl = selectedSkuValueId.get(i).getPropValueUrl();
                if (i == selectedSkuValueId.size() - 1) {
                    picUrl = TextUtils.isEmpty(selectedSkuValueId.get(i).getPropValueUrl()) ? editGoodsSkuBean.getPicUrl() : selectedSkuValueId.get(i).getPropValueUrl();
                }
            }
            for (int i = 0; i < skuSaleList.size(); i++) {
                SkuSaleBean skuSaleBean = skuSaleList.get(i);
                if (valuesAppend.toString().equals(skuSaleBean.getPropValues())) {
                    if (skuSaleBean.getIsNotice() == 1 || skuSaleBean.getIsNotice() == 2) {
                        if (userId > 0) {
                            addCancelNotice(skuSaleBean);
                        } else {
                            Intent intent = new Intent(baseAct, MineLoginActivity.class);
                            baseAct.startActivityForResult(intent, IS_LOGIN_CODE);
                        }
                    } else {
                        shopCarGoodsSku = new ShopCarGoodsSku();
                        shopCarGoodsSku.setSaleSkuId(skuSaleBean.getId());
                        shopCarGoodsSku.setPrice(Double.parseDouble(skuSaleBean.getPrice()));
                        shopCarGoodsSku.setPresentIds(getStrings(skuSaleBean.getPresentSkuIds()));
                        shopCarGoodsSku.setValuesName(String.valueOf(valuesNameAppend));
                        shopCarGoodsSku.setPicUrl(getStrings(picUrl));
                        shopCarGoodsSku.setActivityCode(getStrings(editGoodsSkuBean.getActivityCode()));
                        shopCarGoodsSku.setQuantity(skuSaleBean.getQuantity());
                        //返回组合商品原始价格（方便计算出省了多少钱）
                        if (editGoodsSkuBean != null && editGoodsSkuBean.isCombine()) {
                            shopCarGoodsSku.setOriginalPrice(skuSaleBean.getPrePrice());
                        }
                        shopCarGoodsSku.setCount(numQuantity);

                        //已经获取到选中的sku,直接终止函数体
                        if (TextUtils.isEmpty(type)) {
                            return;
                        }
                        if (numQuantity > skuSaleBean.getQuantity()) {
                            showToast("库存不足，请更改数量后再购买");
                            return;
                        }
                        if (listener != null && shopCarGoodsSku != null) {
                            shopCarGoodsSku.setProType(getStrings(type));
                            listener.getSkuProperty(shopCarGoodsSku);
                        }
                        dialog.cancel();
                    }
                    break;
                } else if (i == skuSaleList.size() - 1) {
                    showToast("没有该商品属性");
                }
            }
        } else if (selectedSkuValueId.size() > 0) {
            for (int j = 0; j < propsList.size(); j++) {
                for (int i = 0; i < selectedSkuValueId.size(); i++) {
                    if (propsList.get(j).getPropId() == selectedSkuValueId.get(i).getParentId()) {
                        break;
                    } else if (i == selectedSkuValueId.size() - 1) {
                        showToast("请选择" + propsList.get(j).getPropName());
                        return;
                    }
                }
            }
        } else {
            showToast( "请选择商品属性");
        }
    }

    public void getGoodsSKu(DataListener listener) {
        this.listener = listener;
    }

    //    已选Sku值
    List<ProductParameterValueBean> selectedSkuValueId = new ArrayList<>();
    //    可选Sku值
    List<String> canSelected = new ArrayList<>();
    //    可选sku组合
    List<String> canSelectedGroup = new ArrayList<>();

    /**
     * 属性点击选择事件
     *
     * @param v
     * @param productParameterValueBean
     */
    @Override
    public void onClickKeyword(View v, ProductParameterValueBean productParameterValueBean) {
        rel_rect_count.setVisibility(View.VISIBLE);
        //        点击选择
        if (!productParameterValueBean.isSelected()) {
            getSelectedSku(productParameterValueBean);
        } else {
            getDeselectSkuValue(productParameterValueBean);
        }
        if (selectedSkuValueId.size() != editGoodsSkuBean.getProps().size()) {
            setNormalAndReplenishment();
        }
        setSkuStatusViews(productParameterTypeBeanList);
    }

    /**
     * 属性取消选择
     *
     * @param productParameterValueBean
     */
    private void getDeselectSkuValue(ProductParameterValueBean productParameterValueBean) {
        ArrayList<ProductParameterValueBean> newValues;
        for (int j = 0; j < productParameterTypeBeanList.size(); j++) {
            newValues = productParameterTypeBeanList.get(j).getValues();
            //                    同类型
            if (productParameterValueBean.getParentId() == productParameterTypeBeanList.get(j).getTypeId()) {
                for (int i = 0; i < newValues.size(); i++) {
                    //                        是否是当前点击item
                    if (newValues.get(i).getPropId() == productParameterValueBean.getPropId()) {
                        for (int k = 0; k < selectedSkuValueId.size(); k++) {
                            if (selectedSkuValueId.get(k).getPropId() == newValues.get(i).getPropId()) {
                                selectedSkuValueId.remove(k);
                                break;
                            }
                        }
                        newValues.get(i).setSelected(false);
                    }
                }
            }
            for (int i = 0; i < newValues.size(); i++) {
                ProductParameterValueBean proParameterValueBean = newValues.get(i);
                boolean isNull = false;
                for (ProductParameterValueBean pro : nonNullAllList) {
                    if (proParameterValueBean.getPropId() == pro.getPropId()) {
                        proParameterValueBean.setNull(false);
                        isNull = true;
                        break;
                    }
                }
                if (!isNull) {
                    proParameterValueBean.setNull(true);
                }
            }
        }

        if (selectedSkuValueId.size() > 1) {
            for (int i = 0; i < productParameterTypeBeanList.size(); i++) {
                newValues = productParameterTypeBeanList.get(i).getValues();
                boolean isSelectPropValue = false;
                for (int j = 0; j < selectedSkuValueId.size(); j++) {
                    ProductParameterValueBean currentProductParameterValueBean = selectedSkuValueId.get(j);
                    if (currentProductParameterValueBean.getParentId() == productParameterTypeBeanList.get(i).getTypeId()) {
                        isSelectPropValue = true;
                    }
                }
//            选择类型属性控制
                if (isSelectPropValue) {
                    for (int j = 0; j < selectedSkuValueId.size(); j++) {
//                相同属性集合
                        List<String> sameTypeList = propIdList.get(String.valueOf(selectedSkuValueId.get(j).getParentId()));
//                比对选择属性集合
                        for (int j1 = 0; j1 < sameTypeList.size(); j1++) {
                            boolean isSkuPropClick = false;
                            String currentProp = sameTypeList.get(j1);
                            List<String> propValues = skuIdGroup.get(sameTypeList.get(j1));
                            if (propValues != null) {
                                for (int k = 0; k < propValues.size(); k++) {
                                    if (isSkuPropClick) {
                                        break;
                                    }
                                    String propValueSku = propValues.get(k);
                                    int index = 0;
                                    String[] arrayList = propValueSku.split(",");
                                    for (int k1 = 0; k1 < selectedSkuValueId.size(); k1++) {
                                        for (int k2 = 0; k2 < arrayList.length; k2++) {
                                            if (arrayList[k2].equals(String.valueOf(selectedSkuValueId.get(k1).getPropId()))) {
                                                index++;
                                                break;
                                            }
                                        }
                                    }
                                    if (selectedSkuValueId.size() == arrayList.length) {
                                        index++;
                                    }
                                    if (index >= selectedSkuValueId.size()) {
                                        isSkuPropClick = true;
                                    } else {
                                        isSkuPropClick = false;
                                    }
                                }
                            }
                            for (int k = 0; k < newValues.size(); k++) {
                                if (String.valueOf(newValues.get(k).getPropId()).equals(currentProp)) {
                                    newValues.get(k).setNull(isSkuPropClick);
                                    break;
                                }
                            }
                            productParameterTypeBeanList.get(i).setValues(newValues);
                        }
                    }
                } else {
//            未选择类型属性控制
                    canSelectedGroup = skuIdGroup.get(String.valueOf(selectedSkuValueId.get(selectedSkuValueId.size() - 1).getPropId()));
                    for (int k = selectedSkuValueId.size() - 2; k >= 0; k--) {
                        canSelectedGroup = getSameValue(canSelectedGroup, skuIdGroup.get(String.valueOf(selectedSkuValueId.get(k).getPropId())));
                    }
                    for (int k = 0; k < newValues.size(); k++) {
                        boolean isSelectSkuValue = false;
                        for (int j = 0; j < canSelectedGroup.size(); j++) {
                            if (isSelectSkuValue) {
                                break;
                            }
                            String[] arrayList = canSelectedGroup.get(j).split(",");
                            for (int j1 = 0; j1 < arrayList.length; j1++) {
                                if (arrayList[j1].equals(String.valueOf(newValues.get(k).getPropId()))) {
                                    newValues.get(k).setNull(true);
                                    isSelectSkuValue = true;
                                    break;
                                } else {
                                    newValues.get(k).setNull(false);
                                    isSelectSkuValue = false;
                                }
                            }
                        }
                    }
                    productParameterTypeBeanList.get(i).setValues(newValues);
                }
            }
        } else {
            for (int j = 0; j < productParameterTypeBeanList.size(); j++) {
                newValues = productParameterTypeBeanList.get(j).getValues();
                if (selectedSkuValueId.size() < 1) {
//                    恢复有库存的属性Sku
                    for (int i = 0; i < newValues.size(); i++) {
                        for (int k = 0; k < allQualitySkuValuesList.size(); k++) {
                            if (newValues.get(i).getPropId() == Integer.parseInt(allQualitySkuValuesList.get(k))) {
                                newValues.get(i).setNull(true);
                                break;
                            } else if (k == allQualitySkuValuesList.size() - 1) {
                                newValues.get(i).setNull(false);
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < newValues.size(); i++) {
                        if (productParameterTypeBeanList.get(j).getTypeId()
                                != selectedSkuValueId.get(selectedSkuValueId.size() - 1).getParentId()) {
                            for (int k = 0; k < canSelected.size(); k++) {
                                if (newValues.get(i).getPropId() == Integer.parseInt(canSelected.get(k))) {
                                    newValues.get(i).setNull(true);
                                    break;
                                } else if (k == canSelected.size() - 1) {
                                    newValues.get(i).setNull(false);
                                }
                            }
                        }
                    }
                }
                productParameterTypeBeanList.get(j).setValues(newValues);
            }
        }
        if (skuDialogView.tv_pro_combine_discount.getVisibility() == View.VISIBLE
                && !TextUtils.isEmpty(editGoodsSkuBean.getMaxDiscounts())) {
            getDefaultDiscount();
        } else {
            skuDialogView.tv_pro_combine_discount.setVisibility(View.GONE);
        }
    }

    private void setNormalAndReplenishment() {
        noticeType = 0;
        if (selectedSkuValueId.size() < 1) {
            if (editGoodsSkuBean.getQuantity() == 0) {
                for (SkuSaleBean saleSkuBean : skuSaleList) {
                    if (saleSkuBean.getIsNotice() == 1 || saleSkuBean.getIsNotice() == 2) {
                        noticeType = 1;
                        break;
                    }
                }
            }
            setSkuQuality(editGoodsSkuBean.getQuantity(), "");
        } else {
            int skuQuantity = 0;
            noticeType = 1;
            StringBuilder stringBuilder = new StringBuilder();
            Collections.sort(selectedSkuValueId, new Comparator<ProductParameterValueBean>() {
                @Override
                public int compare(ProductParameterValueBean lhs, ProductParameterValueBean rhs) {
                    int propId1 = lhs.getPropId();
                    int propId2 = rhs.getPropId();
                    return Integer.compare(propId1, propId2);
                }
            });
            for (int i = 0; i < selectedSkuValueId.size(); i++) {
                stringBuilder.append(selectedSkuValueId.get(i).getPropId());
                stringBuilder.append(",");
            }
            if (stringBuilder.length() > 0) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            for (SkuSaleBean saleSkuBean : skuSaleList) {
                if (saleSkuBean.getPropValues().contains(stringBuilder) && saleSkuBean.getQuantity() > 0) {
                    skuQuantity = editGoodsSkuBean.getQuantity();
                    noticeType = 0;
                    break;
                }
            }
            setSkuQuality(skuQuantity, stringBuilder.toString());
        }
    }

    /**
     * 当前属性选择
     *
     * @param productParameterValueBean
     */
    private void getSelectedSku(ProductParameterValueBean productParameterValueBean) {
        ArrayList<ProductParameterValueBean> newValues;//            设置selectedId canSelected
        for (int j = 0; j < productParameterTypeBeanList.size(); j++) {
            newValues = productParameterTypeBeanList.get(j).getValues();
            //                    同类型
            if (productParameterValueBean.getParentId() == productParameterTypeBeanList.get(j).getTypeId()) {
                for (int i = 0; i < newValues.size(); i++) {
//                        是否是当前点击item
                    if (newValues.get(i).getPropId() == productParameterValueBean.getPropId()) {
                        if (selectedSkuValueId.size() < 1 || !selectedSkuValueId.get(selectedSkuValueId.size() - 1).equals(productParameterValueBean)) {
                            selectedSkuValueId.add(newValues.get(i));
                            newValues.get(i).setSelected(true);
                            newValues.get(i).setNotice(0);
                        }
                        if (!TextUtils.isEmpty(productParameterValueBean.getPropValueUrl())) {
                            GlideImageLoaderUtil.loadCenterCrop(baseAct.getApplicationContext(), skuDialogView.rImg_direct_attribute_product, productParameterValueBean.getPropValueUrl());
                            skuDialogView.rImg_direct_attribute_product.setTag(R.id.iv_tag, getStrings(productParameterValueBean.getPropValueUrl()));
                        }
                    } else {
                        //                        是否已选择过该类型
                        if (newValues.get(i).isSelected()) {
                            newValues.get(i).setSelected(false);
                            for (int k = 0; k < selectedSkuValueId.size(); k++) {
                                if (selectedSkuValueId.get(k).getPropId() == newValues.get(i).getPropId()) {
                                    selectedSkuValueId.remove(k);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (selectedSkuValueId.size() > 1) {
            for (int i = 0; i < productParameterTypeBeanList.size(); i++) {
                newValues = productParameterTypeBeanList.get(i).getValues();
                boolean isSelectPropValue = false;
                for (int j = 0; j < selectedSkuValueId.size(); j++) {
                    ProductParameterValueBean currentProductParameterValueBean = selectedSkuValueId.get(j);
                    if (currentProductParameterValueBean.getParentId() == productParameterTypeBeanList.get(i).getTypeId()) {
                        isSelectPropValue = true;
                    }
                }
//            选择类型属性控制
                if (isSelectPropValue) {
                    for (int j = 0; j < selectedSkuValueId.size(); j++) {
//                相同属性集合
                        List<String> sameTypeList = propIdList.get(String.valueOf(selectedSkuValueId.get(j).getParentId()));
//                比对选择属性集合
                        for (int j1 = 0; j1 < sameTypeList.size(); j1++) {
                            boolean isSkuPropClick = false;
                            String currentProp = sameTypeList.get(j1);
                            List<String> propValues = skuIdGroup.get(sameTypeList.get(j1));
                            if (propValues != null) {
                                for (int k = 0; k < propValues.size(); k++) {
                                    if (isSkuPropClick) {
                                        break;
                                    }
                                    String propValueSku = propValues.get(k);
                                    int index = 0;
                                    String[] arrayList = propValueSku.split(",");
                                    for (int k1 = 0; k1 < selectedSkuValueId.size(); k1++) {
                                        for (int k2 = 0; k2 < arrayList.length; k2++) {
                                            if (arrayList[k2].equals(String.valueOf(selectedSkuValueId.get(k1).getPropId()))) {
                                                index++;
                                                break;
                                            }
                                        }
                                    }
                                    if (selectedSkuValueId.size() == arrayList.length) {
                                        index++;
                                    }
                                    if (index >= selectedSkuValueId.size()) {
                                        isSkuPropClick = true;
                                    } else {
                                        isSkuPropClick = false;
                                    }
                                }
                            }
                            for (int k = 0; k < newValues.size(); k++) {
                                if (String.valueOf(newValues.get(k).getPropId()).equals(currentProp)) {
                                    newValues.get(k).setNull(isSkuPropClick);
                                    break;
                                }
                            }
                            productParameterTypeBeanList.get(i).setValues(newValues);
                        }
                    }
                } else {
//            未选择类型属性控制
                    canSelectedGroup = skuIdGroup.get(String.valueOf(selectedSkuValueId.get(selectedSkuValueId.size() - 1).getPropId()));
                    for (int k = selectedSkuValueId.size() - 2; k >= 0; k--) {
                        canSelectedGroup = getSameValue(canSelectedGroup, skuIdGroup.get(String.valueOf(selectedSkuValueId.get(k).getPropId())));
                    }
                    for (int k = 0; k < newValues.size(); k++) {
                        boolean isSelectSkuValue = false;
                        for (int j = 0; j < canSelectedGroup.size(); j++) {
                            if (isSelectSkuValue) {
                                break;
                            }
                            String[] arrayList = canSelectedGroup.get(j).split(",");
                            for (int j1 = 0; j1 < arrayList.length; j1++) {
                                if (arrayList[j1].equals(String.valueOf(newValues.get(k).getPropId()))) {
                                    newValues.get(k).setNull(true);
                                    isSelectSkuValue = true;
                                    break;
                                } else {
                                    newValues.get(k).setNull(false);
                                    isSelectSkuValue = false;
                                }
                            }
                        }
                    }
                    productParameterTypeBeanList.get(i).setValues(newValues);
                }
            }
        } else {
            canSelectedGroup = skuIdGroup.get(String.valueOf(selectedSkuValueId.get(0).getPropId()));
            if (canSelectedGroup != null) {
                canSelected = getSplitGroup(canSelectedGroup);
                for (int j = 0; j < productParameterTypeBeanList.size(); j++) {
                    newValues = productParameterTypeBeanList.get(j).getValues();
                    for (int i = 0; i < newValues.size(); i++) {
                        if (productParameterTypeBeanList.get(j).getTypeId() != productParameterValueBean.getParentId()) {
                            for (int k = 0; k < canSelected.size(); k++) {
                                if (newValues.get(i).getPropId() == Integer.parseInt(canSelected.get(k))) {
                                    newValues.get(i).setNull(true);
                                    break;
                                } else if (k == canSelected.size() - 1) {
                                    newValues.get(i).setNull(false);
                                }
                            }
                        }
                    }
                    productParameterTypeBeanList.get(j).setValues(newValues);
                }
            }
        }
//        选择属性完成
        if (selectedSkuValueId.size() == productParameterTypeBeanList.size()) {
            StringBuilder valuesAppend = new StringBuilder();
            int numQuantity = rectAddAndSubViewDirect.getNum();
            Collections.sort(selectedSkuValueId, new Comparator<ProductParameterValueBean>() {
                @Override
                public int compare(ProductParameterValueBean lhs, ProductParameterValueBean rhs) {
                    int propId1 = lhs.getPropId();
                    int propId2 = rhs.getPropId();
                    return Integer.compare(propId1, propId2);
                }
            });
            for (int i = 0; i < selectedSkuValueId.size(); i++) {
                valuesAppend.append(selectedSkuValueId.get(i).getPropId());
                valuesAppend.append(",");
            }
            if (valuesAppend.length() > 0) {
                valuesAppend.deleteCharAt(valuesAppend.length() - 1);
            }
            for (int i = 0; i < skuSaleList.size(); i++) {
                SkuSaleBean skuSaleBean = skuSaleList.get(i);
                if (valuesAppend.toString().equals(skuSaleBean.getPropValues())) {
                    if (numQuantity > skuSaleBean.getQuantity()) {
                        rectAddAndSubViewDirect.setNum(skuSaleBean.getQuantity());
                    }
                    getSingleDouble(isShowSingle, editGoodsSkuBean != null && editGoodsSkuBean.isSellStatus(), "确定", skuSaleBean.getIsNotice());
                    for (ProductParameterTypeBean pro : productParameterTypeBeanList) {
                        for (ProductParameterValueBean productValue : pro.getValues()) {
                            if (valuesAppend.toString().contains(String.valueOf(productValue.getPropId()))) {
                                productValue.setNotice(skuSaleBean.getIsNotice());
                            }
                        }
                    }
                    if (skuSaleBean.getIsNotice() == 1 || skuSaleBean.getIsNotice() == 2) {
                        skuDialogView.tv_dir_indent_pro_quality.setSelected(false);
                        skuDialogView.tv_dir_indent_pro_quality.setText("缺货");
                        isSelectNotice = true;
                        noticeType = skuSaleBean.getIsNotice();
                        rel_rect_count.setVisibility(View.GONE);
                    } else {
                        skuDialogView.tv_dir_indent_pro_quality.setSelected(true);
                        skuDialogView.tv_dir_indent_pro_quality.setText(skuSaleBean.getQuantity() > 0 ? ("库存：" + skuSaleBean.getQuantity()) : "");
                        isSelectNotice = false;
                        noticeType = 0;
                    }
                    setDiscountPrice(skuSaleBean.getId());
                    skuDialogView.tv_dir_indent_pro_price.setText((getStrings(skuSaleBean.getNewUserTag()) + "¥" + skuSaleBean.getPrice()));
                    break;
                }
            }

        }
    }

    /**
     * 设置优惠金额
     *
     * @param skuId
     */
    private void setDiscountPrice(int skuId) {
        if (discountMap != null) {
            if (discountMap.get(skuId) != null) {
                Float discountPrice = discountMap.get(skuId);
                if (discountPrice != 0) {
                    skuDialogView.tv_pro_combine_discount.setVisibility(View.VISIBLE);
                    skuDialogView.tv_pro_combine_discount.setText(
                            String.format(baseAct.getString(R.string.sku_discount_price)
                                    , formatFloatNumber(discountPrice)));
                } else {
                    getDefaultDiscount();
                }
            } else {
                getDefaultDiscount();
            }
        } else {
            if (skuDialogView.tv_pro_combine_discount.getVisibility() == View.VISIBLE
                    && !TextUtils.isEmpty(editGoodsSkuBean.getMaxDiscounts())) {
                getDefaultDiscount();
            } else {
                skuDialogView.tv_pro_combine_discount.setVisibility(View.GONE);
            }
        }
    }

    private void getDefaultDiscount() {
        skuDialogView.tv_pro_combine_discount.setText(
                String.format(baseAct.getString(R.string.sku_max_discount_price)
                        , editGoodsSkuBean.getMaxDiscounts()));
    }

    private List<String> getSplitGroup(List<String> canSelectedGroup) {
        List<String> splitValues1 = new ArrayList<>();
        for (int i = 0; i < canSelectedGroup.size(); i++) {
            String[] splitArray = canSelectedGroup.get(i).split(",");
            splitValues1.addAll(Arrays.asList(splitArray));
        }
        Set<String> set = new HashSet<>(splitValues1);
        return new ArrayList<>(set);
    }

    //  交集
    private List<String> getSameValue(List<String> list1, List<String> list2) {
        List<String> canSelected = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {
                if (list1.get(i).equals(list2.get(j))) {
                    canSelected.add(list1.get(i));
                }
            }
        }
        return canSelected;
    }

    //  并集 去重复
    private List<String> getDifferentValue(List<String> list1, List<String> list2) {
        List<String> canSelected = new ArrayList<>();
        canSelected.addAll(list1);
        canSelected.addAll(list2);
        Set<String> set = new HashSet<>();
        set.addAll(list1);
        set.addAll(list2);
        canSelected.addAll(set);
        return canSelected;
    }

    public interface DataListener {
        void getSkuProperty(ShopCarGoodsSku shopCarGoodsSku);
    }

    public interface DismissListener {
        void updateSkuValue(ShopCarGoodsSku shopCarGoodsSku);
    }

    class SkuDialogView {
        @BindView(R.id.iv_dir_indent_pro)
        ImageView rImg_direct_attribute_product;
        @BindView(R.id.tv_dir_indent_pro_name)
        TextView tv_dir_indent_pro_name;
        @BindView(R.id.tv_pro_combine_discount)
        TextView tv_pro_combine_discount;
        @BindView(R.id.tv_dir_indent_pro_price)
        TextView tv_dir_indent_pro_price;
        @BindView(R.id.tv_dir_indent_pro_quality)
        TextView tv_dir_indent_pro_quality;
        @BindView(R.id.layout_parameter_slp)
        LinearLayout layout_parameter_slp;
        @BindView(R.id.bt_direct_attribute_car)
        Button bt_direct_attribute_car;
        @BindView(R.id.bt_direct_attribute_buy)
        Button bt_direct_attribute_buy;

        //            确定
        @OnClick({R.id.bt_direct_attribute_buy, R.id.bt_direct_attribute_car})
        void atProCheck(View view) {
            String type = null;
            switch (view.getId()) {
//                加入购物车
                case R.id.bt_direct_attribute_car:
                    type = "addCar";
                    getCheckSelectedValue(type);
                    break;
                case R.id.bt_direct_attribute_buy:
                    if (isShowSingle) {
                        type = "addCar";
                    } else {
                        type = "buyGoIt";
                    }
                    getCheckSelectedValue(type);
                    break;
            }
        }

        @OnClick(R.id.dialog_layout_slp)
        void closeDialog(View view) {
            getBackCloseKey();
        }

        /**
         * 放大图片
         *
         * @param view
         */
        @OnClick(R.id.iv_dir_indent_pro)
        void enlargePic(View view) {
            String picUrl = (String) view.getTag(R.id.iv_tag);
            int picPosition = 0;
            if (!TextUtils.isEmpty(picUrl)) {
                if (picValueList.size() > 0) {
                    for (int i = 0; i < picValueList.size(); i++) {
                        ImageBean imageBean = picValueList.get(i);
                        if (picUrl.equals(imageBean.getPicUrl())) {
                            picPosition = i;
                        }
                    }
                    ImagePagerActivity.startImagePagerActivity(baseAct, ImagePagerActivity.IMAGE_PRO, picValueList, picPosition);
                }
            }
        }
    }

    /**
     * 取消添加通知
     *
     * @param skuSaleBean
     */
    private void addCancelNotice(SkuSaleBean skuSaleBean) {
        String url =  Url.Q_REPLENISHMENT_NOTICE;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("skuId", skuSaleBean.getId());
        NetLoadUtils.getNetInstance().loadNetDataPost(baseAct, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast(requestStatus.getIsNotice() == 1 ? "已取消通知" : "已设置通知");
                        if (isShowSingle) {
                            skuDialogView.bt_direct_attribute_buy.setText(requestStatus.getIsNotice() == 1 ? baseAct.getText(R.string.sku_notice) : baseAct.getText(R.string.sku_cancel_notice));
                        } else {
                            skuDialogView.bt_direct_attribute_car.setText(requestStatus.getIsNotice() == 1 ? baseAct.getText(R.string.sku_notice) : baseAct.getText(R.string.sku_cancel_notice));
                        }
                        skuSaleBean.setIsNotice(requestStatus.getIsNotice());
                    } else {
                        showToastRequestMsg( requestStatus);
                    }
                }
            }
        });
    }
}

