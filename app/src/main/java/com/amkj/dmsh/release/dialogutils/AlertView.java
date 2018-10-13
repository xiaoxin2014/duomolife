package com.amkj.dmsh.release.dialogutils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amkj.dmsh.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;

/**
 * Created by Sai on 15/8/9.
 * 精仿iOSAlertViewController控件
 * 点击取消按钮返回 －1，其他按钮从0开始算
 */
public class AlertView {

    private TextView tvAlertTitle;
    private TextView tvAlertMsg;

    public enum Style {
//        列表
        ActionSheet,
//        横向选择
        Alert
    }

    private final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
    );

    public static final int HORIZONTAL_BUTTONS_MAXCOUNT = 2;
    public static final int CANCELPOSITION = -1;//点击取消按钮返回 －1，其他按钮从0开始算
    private String title;
    private String determine;
    private String msg;
    private String cancelStr;
    //    确定 取消
    private ArrayList<String> mAlertData = new ArrayList<>();
    //    正常
    private ArrayList<String> mAlertNormalData = new ArrayList<>();
    //    其它
    private ArrayList<String> mAlertOtherData = new ArrayList<>();

    private WeakReference<Context> contextWeak;
    private ViewGroup contentContainer;
    private ViewGroup decorView;//activity的根View
    private ViewGroup rootView;//AlertView 的 根View
    private ViewGroup loAlertHeader;//窗口headerView

    private Style style = Style.Alert;

    private OnDismissListener onDismissListener;
    private OnAlertItemClickListener onAlertItemClickListener;
    private boolean isShowing;

    private Animation outAnim;
    private Animation inAnim;
    private int gravity = Gravity.CENTER;

    public AlertView(Builder builder) {
        this.contextWeak = new WeakReference<>(builder.context);
        this.onAlertItemClickListener = builder.onAlertItemClickListener;
        initData(builder.alertSettingBean.getAlertData());
        initViews(builder.alertSettingBean.getAlertInitView() == null ? new AlertSettingBean.AlertInitView() : builder.alertSettingBean.getAlertInitView());
        init();
    }

    /**
     * @param alertSettingBean
     * @param context
     * @param onAlertItemClickListener
     */
    public AlertView(AlertSettingBean alertSettingBean, Context context, OnAlertItemClickListener onAlertItemClickListener) {
        this.contextWeak = new WeakReference<>(context);
        if (style != null) this.style = alertSettingBean.style;
        this.onAlertItemClickListener = onAlertItemClickListener;
        initData(alertSettingBean.getAlertData());
        initViews(alertSettingBean.getAlertInitView() == null ? new AlertSettingBean.AlertInitView() : alertSettingBean.getAlertInitView());
        init();
    }

    /**
     * 设置Alert数据
     * @param alertData
     */
    public void setAlertData(@NonNull AlertSettingBean.AlertData alertData){
        initData(alertData);
    }

    /**
     * 获取数据
     *
     * @param alertData
     */
    protected void initData(AlertSettingBean.AlertData alertData) {
        this.title = alertData.getTitle();
        this.msg = alertData.getMsg();
        if (alertData.getNormalData() != null && alertData.getOtherData() != null) {
            mAlertData.clear();
            mAlertNormalData.clear();
            mAlertOtherData.clear();
            mAlertNormalData.addAll(Arrays.asList(alertData.getNormalData()));
            mAlertOtherData.addAll(Arrays.asList(alertData.getOtherData()));
            if (alertData.isFirstNormal()) {
                mAlertData.addAll(Arrays.asList(alertData.getNormalData()));
                mAlertData.addAll(Arrays.asList(alertData.getOtherData()));
            } else {
                mAlertData.addAll(Arrays.asList(alertData.getOtherData()));
                mAlertData.addAll(Arrays.asList(alertData.getNormalData()));
            }
        } else if (alertData.getNormalData() != null) {
            mAlertData.clear();
            mAlertNormalData.clear();
            mAlertNormalData.addAll(Arrays.asList(alertData.getNormalData()));
            mAlertData.addAll(Arrays.asList(alertData.getNormalData()));

        } else if (alertData.getOtherData() != null) {
            mAlertData.clear();
            mAlertOtherData.clear();
            mAlertOtherData.addAll(Arrays.asList(alertData.getOtherData()));
            mAlertData.addAll(Arrays.asList(alertData.getOtherData()));
        }
        if (!TextUtils.isEmpty(alertData.getCancelStr())) {
            this.cancelStr = alertData.getCancelStr();
            if (style == Style.Alert && mAlertData.size() < HORIZONTAL_BUTTONS_MAXCOUNT) {
                this.mAlertData.add(0, cancelStr);
            }
        }
        if (!TextUtils.isEmpty(alertData.getDetermineStr())) {
            this.determine = alertData.getDetermineStr();
        }
    }

    protected void initViews(AlertSettingBean.AlertInitView alertInitView) {
        Context context = contextWeak.get();
        if (context == null) return;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        decorView = (ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        rootView = (ViewGroup) layoutInflater.inflate(R.layout.layout_alertview, decorView, false);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
        contentContainer = (ViewGroup) rootView.findViewById(R.id.content_container);
        int margin_alert_left_right = 0;
        switch (style) {
            case ActionSheet:
                params.gravity = Gravity.BOTTOM;
                margin_alert_left_right = context.getResources().getDimensionPixelSize(R.dimen.margin_actionsheet_left_right);
                params.setMargins(margin_alert_left_right, 0, margin_alert_left_right, margin_alert_left_right);
                contentContainer.setLayoutParams(params);
                gravity = Gravity.BOTTOM;
                initActionSheetViews(layoutInflater, alertInitView);
                break;
            case Alert:
                params.gravity = Gravity.CENTER;
                margin_alert_left_right = context.getResources().getDimensionPixelSize(R.dimen.margin_alert_left_right);
                params.setMargins(margin_alert_left_right, 0, margin_alert_left_right, 0);
                contentContainer.setLayoutParams(params);
                gravity = Gravity.CENTER;
                initAlertViews(layoutInflater, alertInitView);
                break;
        }
    }

    protected void initHeaderView(ViewGroup viewGroup, AlertSettingBean.AlertInitView alertInitView) {
        loAlertHeader = (ViewGroup) viewGroup.findViewById(R.id.loAlertHeader);
        //标题和消息
        tvAlertTitle = (TextView) viewGroup.findViewById(R.id.tvAlertTitle);
        tvAlertMsg = (TextView) viewGroup.findViewById(R.id.tvAlertMsg);
        if (title != null) {
            tvAlertTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoSizeUtils.mm2px(mAppContext,alertInitView.getTitleSize() > 0 ? (int) alertInitView.getTitleSize() : 30));
            tvAlertTitle.setTextColor(Color.parseColor(!TextUtils.isEmpty(alertInitView.getTitleColorValue()) ? alertInitView.getTitleColorValue() : "#333333"));
            tvAlertTitle.setText(title);
        } else {
            tvAlertTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(msg)) {
            tvAlertMsg.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoSizeUtils.mm2px(mAppContext,alertInitView.getMsgSize() > 0 ? (int) alertInitView.getMsgSize() : 28));
            tvAlertMsg.setTextColor(Color.parseColor(!TextUtils.isEmpty(alertInitView.getMsgColorValue()) ? alertInitView.getMsgColorValue() : "#666666"));
            tvAlertMsg.setText(msg);
        } else {
            tvAlertMsg.setVisibility(View.GONE);
        }
    }

    protected void initListView(AlertSettingBean.AlertInitView alertInitView) {
        Context context = contextWeak.get();
        if (context == null) return;

        ListView alertButtonListView = (ListView) contentContainer.findViewById(R.id.alertButtonListView);
        //把cancel作为footerView
        if (cancelStr != null && style == Style.Alert) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_alertbutton, null);
            TextView tvAlert = (TextView) itemView.findViewById(R.id.tvAlert);
            tvAlert.setText(cancelStr);
            tvAlert.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoSizeUtils.mm2px(mAppContext,(alertInitView.getCancelSize() > 0
                    ? alertInitView.getCancelSize() : 26)));
            tvAlert.setTextColor(Color.parseColor(!TextUtils.isEmpty(alertInitView.getCancelColorValue()) ? alertInitView.getCancelColorValue() : "#666666"));
            tvAlert.setClickable(true);
            tvAlert.setTypeface(Typeface.DEFAULT_BOLD);
            tvAlert.setBackgroundResource(R.drawable.bg_alertbutton_bottom);
            tvAlert.setOnClickListener(new OnTextClickListener(CANCELPOSITION));
            alertButtonListView.addFooterView(itemView);
        }
        AlertViewAdapter adapter = new AlertViewAdapter(mAlertData, mAlertOtherData, alertInitView);
        alertButtonListView.setAdapter(adapter);
        alertButtonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (onAlertItemClickListener != null){
                    onAlertItemClickListener.onAlertItemClick(AlertView.this, position);}
                dismiss();
            }
        });
    }

    /**
     * 列表选择
     *
     * @param layoutInflater
     * @param alertInitView
     */

    protected void initActionSheetViews(LayoutInflater layoutInflater, AlertSettingBean.AlertInitView alertInitView) {
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.layout_alertview_actionsheet, contentContainer);
        initHeaderView(viewGroup, alertInitView);
        initListView(alertInitView);
        TextView tvAlertCancel = (TextView) contentContainer.findViewById(R.id.tvAlertCancel);
        if (cancelStr != null) {
            tvAlertCancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoSizeUtils.mm2px(mAppContext,(alertInitView.getCancelSize() > 0
                    ? alertInitView.getCancelSize() : 26)));
            tvAlertCancel.setTextColor(Color.parseColor(!TextUtils.isEmpty(alertInitView.getCancelColorValue()) ? alertInitView.getCancelColorValue() : "#666666"));
            tvAlertCancel.setVisibility(View.VISIBLE);
            tvAlertCancel.setText(cancelStr);
        }
        tvAlertCancel.setOnClickListener(new OnTextClickListener(CANCELPOSITION));
    }

    /**
     * 横向选择按钮
     * @param layoutInflater
     * @param alertInitView
     */
    protected void initAlertViews(LayoutInflater layoutInflater, AlertSettingBean.AlertInitView alertInitView) {
        Context context = contextWeak.get();
        if (context == null) return;

        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.layout_alertview_alert, contentContainer);
        initHeaderView(viewGroup, alertInitView);

        int position = 0;
        //如果总数据小于等于HORIZONTAL_BUTTONS_MAXCOUNT，则是横向button
        if (mAlertData.size() <= HORIZONTAL_BUTTONS_MAXCOUNT) {
            ViewStub viewStub = (ViewStub) contentContainer.findViewById(R.id.viewStubHorizontal);
            viewStub.inflate();
            if (!TextUtils.isEmpty(determine)||TextUtils.isEmpty(cancelStr)) {
                mAlertData.add(determine);
            }
            LinearLayout loAlertButtons = (LinearLayout) contentContainer.findViewById(R.id.loAlertButtons);
            for (int i = 0; i < mAlertData.size(); i++) {
                //如果不是第一个按钮
                if (i != 0) {
                    //添加上按钮之间的分割线
                    View divier = new View(context);
                    divier.setBackgroundColor(context.getResources().getColor(R.color.bgColor_divier));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) context.getResources().getDimension(R.dimen.size_divier), LinearLayout.LayoutParams.MATCH_PARENT);
                    loAlertButtons.addView(divier, params);
                }
                View itemView = LayoutInflater.from(context).inflate(R.layout.item_alertbutton, null);
                TextView tvAlert = (TextView) itemView.findViewById(R.id.tvAlert);
                tvAlert.setClickable(true);

                //设置点击效果
                if (mAlertData.size() == 1) {
                    tvAlert.setBackgroundResource(R.drawable.bg_alertbutton_bottom);
                } else if (i == 0) {//设置最左边的按钮效果
                    tvAlert.setBackgroundResource(R.drawable.bg_alertbutton_left);
                } else if (i == mAlertData.size() - 1) {//设置最右边的按钮效果
                    tvAlert.setBackgroundResource(R.drawable.bg_alertbutton_right);
                }
                String data = mAlertData.get(i);
                tvAlert.setText(data);

                //取消按钮的样式
                if (data.equals(cancelStr)) {
                    tvAlert.setTypeface(Typeface.DEFAULT_BOLD);
                    tvAlert.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoSizeUtils.mm2px(mAppContext,(alertInitView.getCancelSize() > 0
                            ? alertInitView.getCancelSize() : 26)));
                    tvAlert.setTextColor(Color.parseColor(!TextUtils.isEmpty(alertInitView.getCancelColorValue()) ? alertInitView.getCancelColorValue() : "#666666"));
                    tvAlert.setOnClickListener(new OnTextClickListener(CANCELPOSITION));
                    position = position - 1;
                } else if (data.equals(determine)) {
                    tvAlert.setTypeface(Typeface.DEFAULT_BOLD);
                    tvAlert.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoSizeUtils.mm2px(mAppContext,(alertInitView.getCancelSize() > 0
                            ? alertInitView.getCancelSize() : 26)));
                    tvAlert.setTextColor(Color.parseColor(!TextUtils.isEmpty(alertInitView.getDetColorValue()) ? alertInitView.getDetColorValue() : "#0a88fa"));
                } else if (mAlertOtherData != null && mAlertOtherData.contains(data)) { //正常item
                    tvAlert.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoSizeUtils.mm2px(mAppContext,(alertInitView.getOthSize() > 0
                            ? alertInitView.getOthSize() : 26)));
                    tvAlert.setTextColor(Color.parseColor(!TextUtils.isEmpty(alertInitView.getOthColorValue()) ? alertInitView.getOthColorValue() : "#ff5e6b"));
                } else {
                    tvAlert.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoSizeUtils.mm2px(mAppContext,(alertInitView.getNorSize() > 0
                            ? alertInitView.getNorSize() : 26)));
                    tvAlert.setTextColor(Color.parseColor(!TextUtils.isEmpty(alertInitView.getNorColorValue()) ? alertInitView.getNorColorValue() : "#0a88fa"));
                }
                tvAlert.setOnClickListener(new OnTextClickListener(position));
                position++;
                loAlertButtons.addView(itemView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            }
        } else {
            ViewStub viewStub = (ViewStub) contentContainer.findViewById(R.id.viewStubVertical);
            viewStub.inflate();
            initListView(alertInitView);
        }
    }

    protected void init() {
        inAnim = getInAnimation();
        outAnim = getOutAnimation();
    }

    public AlertView addExtView(View extView) {
        loAlertHeader.addView(extView);
        return this;
    }

    /**
     * show的时候调用
     *
     * @param view 这个View
     */
    private void onAttached(View view) {
        isShowing = true;
        decorView.addView(view);
        contentContainer.startAnimation(inAnim);
    }

    /**
     * 添加这个View到Activity的根视图
     */
    public void show() {
        if (isShowing()) {
            return;
        }
        onAttached(rootView);
    }

    /**
     * 检测该View是不是已经添加到根视图
     *
     * @return 如果视图已经存在该View返回true
     */
    public boolean isShowing() {
        return rootView.getParent() != null && isShowing;
    }

    public void dismiss() {
        //消失动画
        outAnim.setAnimationListener(outAnimListener);
        contentContainer.startAnimation(outAnim);
    }

    public void dismissImmediately() {
        decorView.removeView(rootView);
        isShowing = false;
        if (onDismissListener != null) {
            onDismissListener.onDismiss(this);
        }

    }

    public Animation getInAnimation() {
        Context context = contextWeak.get();
        if (context == null) return null;

        int res = AlertAnimateUtil.getAnimationResource(this.gravity, true);
        return AnimationUtils.loadAnimation(context, res);
    }

    public Animation getOutAnimation() {
        Context context = contextWeak.get();
        if (context == null) return null;

        int res = AlertAnimateUtil.getAnimationResource(this.gravity, false);
        return AnimationUtils.loadAnimation(context, res);
    }

    public AlertView setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    class OnTextClickListener implements View.OnClickListener {

        private int position;

        public OnTextClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            if (onAlertItemClickListener != null) {
                onAlertItemClickListener.onAlertItemClick(AlertView.this, position);
            }
            dismiss();
        }
    }

    private Animation.AnimationListener outAnimListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            dismissImmediately();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    /**
     * 主要用于拓展View的时候有输入框，键盘弹出则设置MarginBottom往上顶，避免输入法挡住界面
     */
    public void setMarginBottom(int marginBottom) {
        Context context = contextWeak.get();
        if (context == null) return;

        int margin_alert_left_right = context.getResources().getDimensionPixelSize(R.dimen.margin_alert_left_right);
        params.setMargins(margin_alert_left_right, 0, margin_alert_left_right, marginBottom);
        contentContainer.setLayoutParams(params);
    }

    public AlertView setCancelable(boolean isCancelable) {
        View view = rootView.findViewById(R.id.outmost_container);

        if (isCancelable) {
            view.setOnTouchListener(onCancelableTouchListener);
        } else {
            view.setOnTouchListener(null);
        }
        return this;
    }

    /**
     * Called when the user touch on black overlay in order to dismiss the dialog
     */
    private final View.OnTouchListener onCancelableTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dismiss();
            }
            return false;
        }
    };

    public TextView getTvAlertTitle() {
        return tvAlertTitle;
    }

    public TextView getTvAlertMsg() {
        return tvAlertMsg;
    }

    /**
     * Builder for arguments
     */
    public static class Builder {
        private Context context;
        private AlertSettingBean alertSettingBean;
        private OnAlertItemClickListener onAlertItemClickListener;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setAlertSetting(AlertSettingBean alertSettingBean) {
            this.alertSettingBean = alertSettingBean;
            return this;
        }

        public Builder setOnAlertItemClickListener(OnAlertItemClickListener onAlertItemClickListener) {
            this.onAlertItemClickListener = onAlertItemClickListener;
            return this;
        }

        public AlertView build() {
            return new AlertView(this);
        }
    }
}
