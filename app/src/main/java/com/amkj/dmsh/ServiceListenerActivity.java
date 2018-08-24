package com.amkj.dmsh;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;

import org.greenrobot.eventbus.EventBus;

import cn.xiaoneng.activity.ChatActivity;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.uiapi.OnCustomMsgListener;
import cn.xiaoneng.uiapi.XNSendGoodsBtnListener;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/10/11
 * class description:小能客服事件监听
 */

public class ServiceListenerActivity extends ChatActivity implements OnCustomMsgListener, View.OnClickListener, XNSendGoodsBtnListener {
    private static final int SHOP_MSG_TYPE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setExtraFunc();
    }

    private void setExtraFunc() {
        Ntalker.getExtendInstance().chatHeadBar().setOnSendGoodsListener(this);
        Ntalker.getExtendInstance().message().setOnCustomMsgListener(SHOP_MSG_TYPE, R.layout.activity_service_order, this);
    }

    @Override
    public void setCustomViewFromDB(View view, int msgType, String[] strings) {
        if (msgType == SHOP_MSG_TYPE) {
            ImageView iv_service_order_pic = (ImageView) view.findViewById(R.id.iv_service_order_pic);
            LinearLayout ll_service_order_click = (LinearLayout) view.findViewById(R.id.ll_service_order_click);
            TextView tv_service_pro_name = (TextView) view.findViewById(R.id.tv_service_order_create_time);
            TextView tv_service_order_number = (TextView) view.findViewById(R.id.tv_service_order_number);
            TextView tv_service_order_pro_count = (TextView) view.findViewById(R.id.tv_service_order_pro_count);
            TextView tv_service_pro_price = (TextView) view.findViewById(R.id.tv_service_order_price);
            ll_service_order_click.setOnClickListener(this);
            iv_service_order_pic.setOnClickListener(this);
            if (strings.length > 4) {
                tv_service_pro_name.setText(getStrings(strings[0]));
                tv_service_order_number.setText(getStrings(strings[1]));
                tv_service_order_pro_count.setText(getStrings(strings[2]));
                tv_service_pro_price.setText(getStrings(strings[3]));
                GlideImageLoaderUtil.loadCenterCrop(this, iv_service_order_pic, getStrings(strings[4]));
            } else if (strings.length > 2) {
                tv_service_pro_name.setText(getStrings(strings[0]));
                tv_service_pro_price.setText(getStrings(strings[1]));
                GlideImageLoaderUtil.loadCenterCrop(this, iv_service_order_pic, getStrings(strings[2]));
            } else if (strings.length > 1) {
                tv_service_pro_price.setText(String.format(getResources().getString(R.string.money_price_chn), getStrings(strings[1])));
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void setSendGoodsBtnListener(View view, final String title, final String price, final String pic) {
        view.findViewById(R.id.tv_sendgoods).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventMessage("serviceSendInfo", "Info"));
            }
        });
    }
}
