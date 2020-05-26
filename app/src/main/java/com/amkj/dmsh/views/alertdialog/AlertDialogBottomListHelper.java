package com.amkj.dmsh.views.alertdialog;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseAlertDialog;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.utils.WindowUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStringFilter;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/6/6
 * version 3.1.3
 * class description:默认样式dialog
 */
public class AlertDialogBottomListHelper extends BaseAlertDialog {
    @BindView(R.id.tv_alert_bottom_list_title)
    TextView tv_alert_bottom_list_title;
    @BindView(R.id.tv_alert_bottom_list_message)
    TextView tv_alert_bottom_list_message;
    @BindView(R.id.communal_recycler_wrap)
    RecyclerView communal_recycler_wrap;
    @BindView(R.id.tv_alert_bottom_list_cancel)
    TextView tv_alert_bottom_list_cancel;
    private AlertItemClickListener alertItemClickListener;
    private List<String> bottomItemStringList = new ArrayList<>();
    private final BottomListTextAdapter bottomListTextAdapter;
    private int itemTextColor;
    private int itemTextSize;
    private final int screenHeight;

    public AlertDialogBottomListHelper(Context context) {
        super(context);
        communal_recycler_wrap.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(context));
        communal_recycler_wrap.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_one_px)
                .setLastDraw(false).create());
        bottomListTextAdapter = new BottomListTextAdapter(bottomItemStringList);
        communal_recycler_wrap.setAdapter(bottomListTextAdapter);
        bottomListTextAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (alertItemClickListener != null) {
                    String text = (String) view.getTag();
                    if (!TextUtils.isEmpty(text)) {
                        alertItemClickListener.itemClick(text, position);
                    }
                }
                dismiss();
            }
        });
        TinkerBaseApplicationLike tinkerBaseApplicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenHeight = tinkerBaseApplicationLike.getScreenHeight() / 3 * 2;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_alert_dialog_botom_list;
    }

    @Override
    protected int getLayoutWith() {
        return AutoSizeUtils.mm2px(mAppContext, 630);
    }

    @OnClick(R.id.tv_alert_bottom_list_cancel)
    public void onViewClicked() {
        dismiss();
    }

    @Override
    public void show() {
        if (getAlertDialog().getWindow() != null) {
            getAlertDialog().getWindow().setWindowAnimations(WindowUtils.getAnimaitionStyle(Gravity.BOTTOM));
        }
        getDialogView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (v.getMeasuredHeight() > screenHeight) {
                    ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                    layoutParams.height = screenHeight;
                    v.setLayoutParams(layoutParams);
                }
            }
        });
        super.show(Gravity.BOTTOM);
    }


    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public AlertDialogBottomListHelper setTitle(String title) {
        tv_alert_bottom_list_title.setText(getStrings(title));
        return this;
    }

    /**
     * 设置标题是否展示
     *
     * @param visibility
     * @return
     */
    public AlertDialogBottomListHelper setTitleVisibility(int visibility) {
        tv_alert_bottom_list_title.setVisibility(View.GONE);
        return this;
    }

    /**
     * 设置标题位置显示
     *
     * @param gravity
     * @return
     */
    public AlertDialogBottomListHelper setTitleGravity(int gravity) {
        tv_alert_bottom_list_title.setGravity(gravity);
        return this;
    }

    /**
     * 设置描述信息
     *
     * @param msg
     * @return
     */
    public AlertDialogBottomListHelper setMsg(String msg) {
        tv_alert_bottom_list_message.setText(getStrings(msg));
        return this;
    }

    /**
     * 设置描述信息
     *
     * @param msgResId
     * @return
     */
    public AlertDialogBottomListHelper setMsg(int msgResId) {
        tv_alert_bottom_list_message.setText(getStrings(context.getResources().getString(msgResId)));
        return this;
    }

    /**
     * 控制描述信息是否显示
     */
    public AlertDialogBottomListHelper setMsgVisible(int visible) {
        tv_alert_bottom_list_message.setVisibility(visible);
        return this;
    }

    /**
     * 设置meg文本位置显示
     *
     * @param msgTextGravity
     * @return
     */
    public AlertDialogBottomListHelper setMsgTextGravity(int msgTextGravity) {
        tv_alert_bottom_list_message.setGravity(msgTextGravity);
        return this;
    }

    /**
     * 获取信息文本
     *
     * @return
     */
    public TextView getMsgTextView() {
        return tv_alert_bottom_list_message;
    }

    /**
     * 设置取消字样
     *
     * @param cancelText
     * @return
     */
    public AlertDialogBottomListHelper setCancelText(String cancelText) {
        tv_alert_bottom_list_cancel.setText(getStringFilter(cancelText));
        return this;
    }

    /**
     * 设置取消字样颜色
     *
     * @param cancelTextColor
     * @return
     */
    public AlertDialogBottomListHelper setCancelTextColor(int cancelTextColor) {
        tv_alert_bottom_list_cancel.setTextColor(cancelTextColor);
        return this;
    }

    /**
     * 设置item 字体颜色
     *
     * @param itemTextColor 必须调用notify…… 生效
     * @return
     */
    public AlertDialogBottomListHelper setItemTextColor(@IdRes int itemTextColor) {
        this.itemTextColor = itemTextColor;
        return this;
    }

    /**
     * 设置item 字体大小
     *
     * @param itemTextSize 必须调用notify…… 生效
     * @return
     */
    public AlertDialogBottomListHelper setItemTextSize(int itemTextSize) {
        this.itemTextSize = itemTextSize;
        tv_alert_bottom_list_cancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoSizeUtils.mm2px(context, itemTextSize));
        return this;
    }

    /**
     * 设置item数据
     *
     * @param itemData
     * @return
     */
    public AlertDialogBottomListHelper setItemData(List<String> itemData) {
        if (itemData != null) {
            bottomItemStringList.clear();
            bottomItemStringList.addAll(itemData);
        }
        return this;
    }

    /**
     * @param itemData 数组
     * @return
     */
    public AlertDialogBottomListHelper setItemData(String[] itemData) {
        if (itemData != null) {
            setItemData(Arrays.asList(itemData));
        }
        return this;
    }

    /**
     * item notify
     *
     * @return
     */
    public AlertDialogBottomListHelper itemNotifyDataChange() {
        if (bottomListTextAdapter != null) {
            bottomListTextAdapter.notifyDataSetChanged();
        }
        return this;
    }

    public void setAlertListener(AlertItemClickListener alertConfirmCancelListener) {
        this.alertItemClickListener = alertConfirmCancelListener;
    }


    public interface AlertItemClickListener {
        void itemClick(@Nullable String text, int itemPosition);
    }

    private class BottomListTextAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public BottomListTextAdapter(List<String> data) {
            super(R.layout.adapter_layout_communal_text_wrap, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String text) {
            TextView tv_communal_text_wrap = helper.getView(R.id.tv_communal_text_wrap);
            if (itemTextColor > 0) {
                tv_communal_text_wrap.setTextColor(context.getResources().getColor(itemTextColor));
            }
            if (itemTextSize > 0) {
                tv_communal_text_wrap.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoSizeUtils.mm2px(context, itemTextSize));
            }
            helper.setText(R.id.tv_communal_text_wrap, getStrings(text)).itemView.setTag(getStrings(text));
        }
    }
}
