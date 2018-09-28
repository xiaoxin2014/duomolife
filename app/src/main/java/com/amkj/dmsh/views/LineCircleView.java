package com.amkj.dmsh.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.amkj.dmsh.R;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/2
 * class description:请输入类描述
 */

public class LineCircleView extends View {

    private float borderWidth;
    private int lineSelColor;
    private int lineUnColor;
    private boolean drawLineAll;
    private boolean drawLineUn;
    private Paint paint;

    public LineCircleView(Context context) {
        super(context, null);
    }

    public LineCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LineCircleView);
        borderWidth = typedArray.getDimension(R.styleable.LineCircleView_lt_border_width, 2);
        lineSelColor = typedArray.getColor(R.styleable.LineCircleView_lt_line_sel_color, Color.BLUE);
        lineUnColor = typedArray.getColor(R.styleable.LineCircleView_lt_line_un_color, Color.GRAY);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float width;
        int height = getHeight();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(lineSelColor);
        float centerPoint = height / 2f;
        if (borderWidth < 3) {
            borderWidth = AutoSizeUtils.mm2px(mAppContext,borderWidth);
        }
        paint.setStrokeWidth(borderWidth);
        if (drawLineAll) {
            width = getWidth();
//        线条
            canvas.drawLine(0, centerPoint, width, centerPoint, paint);
        } else if (drawLineUn) {
            width = getWidth();
            paint.setColor(lineUnColor);
//        线条
            canvas.drawLine(0, centerPoint, width, centerPoint, paint);
        } else {
            width = getWidth() / 2f;
            //        线条
//            前半段
            canvas.drawLine(0, centerPoint, width/* - height / 2f*/, centerPoint, paint);
//            后半段
            Paint unPaint = new Paint();
            unPaint.setAntiAlias(true);
            unPaint.setColor(lineUnColor);
            unPaint.setStrokeWidth(borderWidth);
            unPaint.setStyle(Paint.Style.FILL);
            canvas.drawLine(width/* + height / 2f*/, centerPoint, getWidth(), centerPoint, unPaint);
        }
//        实心圆
        canvas.drawCircle(getWidth() / 2f, centerPoint, centerPoint, paint);
        super.onDraw(canvas);
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }

    public int getLineSelColor() {
        return lineSelColor;
    }

    public void setLineSelColor(int lineSelColor) {
        this.lineSelColor = lineSelColor;
    }

    public int getLineUnColor() {
        return lineUnColor;
    }

    public void setLineUnColor(int lineUnColor) {
        this.lineUnColor = lineUnColor;
    }

    public boolean isDrawLineAll() {
        return drawLineAll;
    }

    public void setDrawLineAll(boolean drawLineAll) {
        this.drawLineAll = drawLineAll;
    }

    public boolean isDrawLineUn() {
        return drawLineUn;
    }

    public void setDrawLineUn(boolean drawLineUn) {
        this.drawLineUn = drawLineUn;
    }
}
