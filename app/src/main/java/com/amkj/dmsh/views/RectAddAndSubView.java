package com.amkj.dmsh.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.zhy.autolayout.AutoLinearLayout;


public class RectAddAndSubView extends AutoLinearLayout {
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

    /**
     * 构造方法
     */
    public RectAddAndSubView(Context context) {
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
    public RectAddAndSubView(Context context, int num) {
        super(context);
        this.context = context;
        this.num = num;
        control();
    }

    public RectAddAndSubView(Context context, AttributeSet attrs) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.layout_add_sub_view, RectAddAndSubView.this, false);
        addButton = (ImageView) view.findViewById(R.id.img_integration_details_credits_add);
        subButton = (ImageView) view.findViewById(R.id.img_integration_details_credits_minus);
        editText = (TextView) view.findViewById(R.id.tv_integration_details_credits_count);
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
            String numString = editText.getText().toString();
            if (TextUtils.isEmpty(numString)) {
                num = 1;
                editText.setText(String.valueOf(num));
            } else {
                if (v.getId() == R.id.img_integration_details_credits_add) {
                    if (++num > 999) {
                        --num;
                        editText.setText(String.valueOf(num));
                    } else {
                        if (isAutoChangeNum) {
                            setNum(num);
                        } else {
                            setNum(num - 1);
                        }
                        if (onNumChangeListener != null) {
                            onNumChangeListener.onNumChange(RectAddAndSubView.this, TYPE_ADD, getNum());
                        }
                    }
                } else if (v.getId() == R.id.img_integration_details_credits_minus) {
                    if (--num < 1) {
                        ++num;
                        editText.setText(String.valueOf(num));
                        if (onNumChangeListener != null) {
                            onNumChangeListener.onNumChange(RectAddAndSubView.this, TYPE_SUBTRACT, getNum());
                        }
                    } else {
                        if (isAutoChangeNum) {
                            editText.setText(String.valueOf(num));
                        } else {
                            num++;
                        }
                        if (onNumChangeListener != null) {
                            onNumChangeListener.onNumChange(RectAddAndSubView.this, TYPE_SUBTRACT, getNum());
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
    }

}
