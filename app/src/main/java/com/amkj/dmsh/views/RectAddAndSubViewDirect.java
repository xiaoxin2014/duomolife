package com.amkj.dmsh.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.zhy.autolayout.utils.AutoUtils;


public class RectAddAndSubViewDirect extends LinearLayout {
    private Context context = getContext();//上下文
    private OnNumChangeListener onNumChangeListener;

    private ImageView addButton;//加按钮

    private ImageView subButton;//减按钮

    private TextView editText;//数量显示

    int num;          //数量值
    /**
     * 减
     */
    public static final int TYPE_SUBTRACT = 0;
    /**
     * 加
     */
    public static final int TYPE_ADD = 1;

    public boolean isAutoChangeNum = true;//是否自动转变数量
    public TextView tv_direct_number_layout;
    private int maxNum = 999;
    private View view;

    /**
     * 构造方法
     */
    public RectAddAndSubViewDirect(Context context) {
        super(context);
        setOrientation(VERTICAL);
        this.context = context;
        num = 1;
        control();
    }

    /**
     * 构造方法
     *
     * @param context
     * @param
     */
    public RectAddAndSubViewDirect(Context context, int num) {
        super(context);
        setOrientation(VERTICAL);
        this.context = context;
        this.num = num;
        control();
    }
    public RectAddAndSubViewDirect(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        num = 1;
        control();
    }

    /**
     * 初始化
     */
    private void control() {
        setPadding(1, 1, 1, 1);
        initView();
        setViewListener();
    }


    /**
     * 初始化view
     */
    private void initView() {
        view = LayoutInflater.from(context).inflate(R.layout.layout_add_sub_view_direct, this, false);
        AutoUtils.autoSize(view);
        addButton = (ImageView) view.findViewById(R.id.img_integration_details_credits_add);
        subButton = (ImageView) view.findViewById(R.id.img_integration_details_credits_minus);
        editText = (TextView) view.findViewById(R.id.tv_integration_details_credits_count);
        tv_direct_number_layout = (TextView) view.findViewById(R.id.tv_direct_number_layout);
        setNum(1);
        addView(view);
    }

    /**
     * 设置中间的距离
     *
     * @param distance
     */
    public void setMiddleDistance(int distance) {
        editText.setPadding(distance, 0, distance, 0);
    }

    /**
     * 设置默认数量
     *
     * @param num
     */
    public void setNum(int num) {
        this.num = num;
        editText.setText(String.valueOf(num));
    }

    /**
     * 获取值
     *
     * @return
     */
    public int getNum() {
        if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
            return Integer.parseInt(editText.getText().toString());
        } else {
            return 1;
        }
    }

    public int getMaxNum() {
        return maxNum;
    }

    /**
     * 设置最大库存
     * @param maxNum
     */
    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public void setNumberTextColor(int textColor, int textSize) {
        if (tv_direct_number_layout != null) {
            tv_direct_number_layout.setTextColor(textColor);
            tv_direct_number_layout.setTextSize(textSize);
        }
    }


    /**
     * 设置加号图片
     *
     * @param addBtnDrawable
     */
    public void setAddBtnBackgroudResource(int addBtnDrawable) {
        addButton.setBackgroundResource(addBtnDrawable);
    }

    /**
     * 设置减法图片
     *
     * @param subBtnDrawable
     */
    public void setSubBtnBackgroudResource(int subBtnDrawable) {
        subButton.setBackgroundResource(subBtnDrawable);
    }

    /**
     * 设置是否自动改变数量玩
     *
     * @param isAutoChangeNum
     */
    public void setAutoChangeNumber(boolean isAutoChangeNum) {
        this.isAutoChangeNum = isAutoChangeNum;
    }

    /**
     * 设置加法减法的背景色
     *
     * @param addBtnColor
     * @param subBtnColor
     */
    public void setButtonBgColor(int addBtnColor, int subBtnColor) {
        addButton.setBackgroundColor(addBtnColor);
        subButton.setBackgroundColor(subBtnColor);
    }

    /**
     * 设置监听回调
     *
     * @param onNumChangeListener
     */
    public void setOnNumChangeListener(OnNumChangeListener onNumChangeListener) {
        this.onNumChangeListener = onNumChangeListener;
    }


    /**
     * 设置监听器
     */
    private void setViewListener() {
        addButton.setOnClickListener(new OnButtonClickListener());
        subButton.setOnClickListener(new OnButtonClickListener());
    }

    /**
     * 设置字体颜色
     * @param fontColor
     */
    public void setFontColor(int fontColor){
        tv_direct_number_layout.setTextColor(fontColor);
        editText.setTextColor(fontColor);
    }
    /**
     * 监听器监听事件
     */
    private class OnButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            String numString = editText.getText().toString();
            if (TextUtils.isEmpty(numString)) {
                num = 1;
                editText.setText(String.valueOf(num));
            } else {
                if (v.getId() == R.id.img_integration_details_credits_add) {
                    if (++num > maxNum) {
                        --num;
                        editText.setText(String.valueOf(num));
                        if (onNumChangeListener != null) {
                            onNumChangeListener.onMaxQuantity(RectAddAndSubViewDirect.this,num);
                        }
                    } else {
                        if (isAutoChangeNum) {
                            setNum(num);
                        } else {
                            setNum(num - 1);
                        }
                        if (onNumChangeListener != null) {
                            onNumChangeListener.onNumChange(RectAddAndSubViewDirect.this, TYPE_ADD, getNum());
                        }
                    }
                } else if (v.getId() == R.id.img_integration_details_credits_minus) {
                    if (--num < 1) {
                        ++num;
                        editText.setText(String.valueOf(num));
                        if (onNumChangeListener != null) {
                            onNumChangeListener.onNumChange(RectAddAndSubViewDirect.this, TYPE_SUBTRACT, getNum());
                        }
                    } else {
                        if (isAutoChangeNum) {
                            editText.setText(String.valueOf(num));
                        } else {
                            num++;
                        }
                        if (onNumChangeListener != null) {
                            onNumChangeListener.onNumChange(RectAddAndSubViewDirect.this, TYPE_SUBTRACT, getNum());
                        }
                    }
                }
            }
        }
    }


    public interface OnNumChangeListener {
        /**
         * 监听方法
         *
         * @param view
         * @param stype 点击按钮的类型
         * @param num   返回的数值
         */
        public void onNumChange(View view, int stype, int num);

        /**
         * 已是最大库存
         * @param view
         * @param num
         */
        void onMaxQuantity(View view, int num);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureSpecHandler(widthMeasureSpec,heightMeasureSpec);
    }

    private int measureSpecHandler(int measureSpec, int defaultSize){
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);      int specSize = MeasureSpec.getSize(measureSpec);

        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        } else if(specMode == MeasureSpec.AT_MOST){
            result = Math.min(result, specSize);
        } else{
            result = defaultSize;
        }
        return result;
    }
}