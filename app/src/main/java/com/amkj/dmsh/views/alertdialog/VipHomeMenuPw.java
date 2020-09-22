package com.amkj.dmsh.views.alertdialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.amkj.dmsh.MainActivity;
import com.amkj.dmsh.R;
import com.amkj.dmsh.homepage.activity.AllSearchDetailsNewActivity;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.qyservice.QyServiceUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by xiaoxin on 2019/7/15
 * Version:v4.7.0
 * ClassDescription :会员首页菜单弹窗
 */
public class VipHomeMenuPw extends BasePopupWindow {

    private final Context mContext;

    public VipHomeMenuPw(Context context) {
        super(context);
        ButterKnife.bind(this, getContentView());
        mContext = context;
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pw_vip_home_menu);
    }

    @OnClick({R.id.tv_home, R.id.tv_search, R.id.tv_shopcar, R.id.tv_qy})
    public void onViewClicked(View view) {
        Intent intent;
        dismiss();
        switch (view.getId()) {
            case R.id.tv_home:
                intent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.tv_search:
                intent = new Intent(mContext, AllSearchDetailsNewActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.tv_shopcar:
                intent = new Intent(mContext, ShopCarActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.tv_qy:
                QyServiceUtils.getQyInstance().openQyServiceChat(mContext, "会员首页");
                break;
        }
    }
}
