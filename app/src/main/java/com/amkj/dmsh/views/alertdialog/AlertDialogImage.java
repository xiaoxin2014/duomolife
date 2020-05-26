package com.amkj.dmsh.views.alertdialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseAlertDialog;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/16
 * version 3.1.5
 * class description:弹窗广告
 */
public class AlertDialogImage extends BaseAlertDialog {

    @BindView(R.id.iv_dialog_image_close)
    ImageView iv_dialog_image_close;
    @BindView(R.id.iv_dialog_image_show)
    ImageView iv_ad_image;
    @BindView(R.id.tv_dialog_text)
    TextView tv_text;
    private AlertImageClickListener alertImageClickListener;

    public AlertDialogImage(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_alert_dialog_image;
    }

    @Override
    protected int getLayoutWith() {
        return AutoSizeUtils.mm2px(mAppContext, 500);
    }

    public void setImage(@NonNull Bitmap bitmap) {
        try {
            if (isContextExisted(context)) {
                iv_ad_image.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImageResource(@NonNull int resouce) {
        if (isContextExisted(context)) {
            iv_ad_image.setImageResource(resouce);
        }
    }

    public void setDialogText(String text) {
        tv_text.setVisibility(View.VISIBLE);
        tv_text.setText(text);
    }

    //隐藏关闭按钮
    public void hideCloseBtn() {
        iv_dialog_image_close.setVisibility(View.GONE);
    }

    public void setAlertClickListener(AlertImageClickListener alertImageClickListener) {
        this.alertImageClickListener = alertImageClickListener;
    }

    @OnClick({R.id.iv_dialog_image_close, R.id.iv_dialog_image_show})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_dialog_image_close:
                dismiss();
                break;
            case R.id.iv_dialog_image_show:
                dismiss();
                if (alertImageClickListener != null) {
                    alertImageClickListener.imageClick();
                }
                break;
        }
    }

    public interface AlertImageClickListener {
        void imageClick();
    }
}
