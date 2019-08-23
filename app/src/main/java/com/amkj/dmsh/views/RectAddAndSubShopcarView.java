package com.amkj.dmsh.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.EditText;

import com.amkj.dmsh.R;


public class RectAddAndSubShopcarView extends LinearLayout {
    private Context context = getContext();//上下文
    private OnNumChangeListener onNumChangeListener;

    private ImageView addButton;//加按钮

    private ImageView subButton;//减按钮

    private EditText textViewCount;//数量显示

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
    private int maxNum = 999;
    private int minNum = 1;

    /**
     * 构造方法
     */
    public RectAddAndSubShopcarView(Context context) {
        super(context);
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
    public RectAddAndSubShopcarView(Context context, int num) {
        super(context);
        this.context = context;
        this.num = num;
        control();
    }

    public RectAddAndSubShopcarView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        View view = LayoutInflater.from(context).inflate(R.layout.layout_add_sub_view_shop_car_edittext, RectAddAndSubShopcarView.this, false);
        addButton = (ImageView) view.findViewById(R.id.img_integration_details_credits_add);
        subButton = (ImageView) view.findViewById(R.id.img_integration_details_credits_minus);
        textViewCount = (EditText) view.findViewById(R.id.tv_integration_details_credits_count);
        setNum(1);
        addView(view);
    }

    /**
     * 设置中间的距离
     *
     * @param distance
     */
    public void setMiddleDistance(int distance) {
        textViewCount.setPadding(distance, 0, distance, 0);
    }

    /**
     * 设置默认数量
     *
     * @param num
     */
    public void setNum(int num) {
        this.num = num >= 0 ? num : 1;
        textViewCount.setText(String.valueOf(num));
    }

    /**
     * 获取值
     *
     * @return
     */
    public int getNum() {
        if (!TextUtils.isEmpty(textViewCount.getText().toString().trim())) {
            return Integer.parseInt(textViewCount.getText().toString());
        } else {
            return 1;
        }
    }

    /**
     * 设置最大值
     *
     * @param maxNum
     */
    public void setMaxNum(int maxNum) {
        if (maxNum > 1) {
            this.maxNum = maxNum;
        }
    }

    /**
     * 获取最大值
     */
    public int getMaxNum() {
        return maxNum;
    }

    /**
     * 设置最小值
     *
     * @param minNum
     */
    public void setMinNum(int minNum) {
        if (minNum > 0) {
            this.minNum = minNum;
        }
    }

    /**
     * 获取最小值
     */
    public int getMinNum() {
        return minNum;
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
     * 监听器监听事件
     */
    private class OnButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            String numString = textViewCount.getText().toString();
            if (TextUtils.isEmpty(numString)) {
                num = 1;
                textViewCount.setText(String.valueOf(num));
            } else {
                if (v.getId() == R.id.img_integration_details_credits_add) {
                    if (num + 1 > maxNum) {
                        setNum(num);
                        if (onNumChangeListener != null) {
                            onNumChangeListener.onNumChange(RectAddAndSubShopcarView.this, TYPE_ADD, num + 1, num);
                        }
                    } else {
                        ++num;
                        if (isAutoChangeNum) {
                            setNum(num);
                        }
                        if (onNumChangeListener != null) {
                            onNumChangeListener.onNumChange(RectAddAndSubShopcarView.this, TYPE_ADD, num, num - 1);
                        }
                    }
                } else if (v.getId() == R.id.img_integration_details_credits_minus) {
                    if (num - 1 < minNum) {
                        setNum(num);
                    } else {
                        --num;
                        if (isAutoChangeNum) {
                            setNum(num);
                        }
                        if (onNumChangeListener != null) {
                            onNumChangeListener.onNumChange(RectAddAndSubShopcarView.this, TYPE_SUBTRACT, num, num + 1);
                        }
                    }
                }
            }
        }
    }


    public interface OnNumChangeListener {
        /**
         * @param view
         * @param type   点击类型
         * @param newNum 点击后的数量
         * @param oldNum 原来的数量
         */
        void onNumChange(View view, int type, int newNum, int oldNum);
    }

}
