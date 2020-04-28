package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.bean.ButtonListBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.GO_PAY;

/**
 * Created by xiaoxin on 2020/3/19
 * Version:v4.5.0
 * ClassDescription :主订单状态按钮适配器
 */
public class MainOrderButtonAdapter extends BaseQuickAdapter<ButtonListBean, BaseViewHolder> {

    private final Activity context;
    private boolean isMore;

    public MainOrderButtonAdapter(Activity activity, List<ButtonListBean> orderList) {
        super(R.layout.item_order_button, orderList);
        context = activity;
    }

    public MainOrderButtonAdapter(Activity activity, List<ButtonListBean> orderList, boolean isMore) {
        super(R.layout.item_order_button, orderList);
        context = activity;
        this.isMore = isMore;
    }

    @Override
    protected void convert(BaseViewHolder helper, ButtonListBean item) {
        if (item == null) return;
        int id = item.getId();

        helper.setText(R.id.tv_button, getStrings(item.getBtnText()));
        if (!isMore) {
            if (id == GO_PAY) {
                helper.setTextColor(R.id.tv_button, context.getResources().getColor(R.color.white))
                        .setBackgroundRes(R.id.tv_button, R.drawable.border_circle_three_blue);
            } else {
                boolean blue = helper.getPosition() == getData().size() - 1;
                helper.setTextColor(R.id.tv_button, context.getResources().getColor(blue ? R.color.intent_foot_button : (item.isClickable() ? R.color.text_black_t : R.color.color_blue_reply_com)))
                        .setBackgroundRes(R.id.tv_button, blue ? R.drawable.border_circle_three_blue_white : R.drawable.border_circle_three_gray_white);
            }
        }
        helper.itemView.setTag(item);
    }
}
