package com.klinker.android.link_builder;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/11/22
 * class description:背景颜色、区域绘制
 */

public class RadiusBackgroundSpan extends ReplacementSpan {

    private final Link link;
    private int mRadius;
    private int mSize;

    /**
     * @param link
     */
    public RadiusBackgroundSpan(Link link) {
        this.link = link;
        mRadius = link.getBgRadius();
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        paint.setTextSize(link.getTextSize());
        mSize = (int) (paint.measureText(text, start, end) + 2 * mRadius);
        //mSize就是span的宽度，span有多宽，开发者可以在这里随便定义规则
        //我的规则：这里text传入的是SpannableString，start，end对应setSpan方法相关参数
        //可以根据传入起始截至位置获得截取文字的宽度，最后加上左右两个圆角的半径得到span宽度
        return mSize;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        paint.setTextSize(link.getTextSize());
        int color = paint.getColor();//保存文字颜色
        if (link.getBgColor() != 0) {
            paint.setColor(link.getBgColor());
        } else {
            paint.setColor(Color.TRANSPARENT);
        }
        int mtbRadius = 0;
        if (mRadius > 2) {
            mtbRadius = mRadius - 2;
        }
        paint.setAntiAlias(true);// 设置画笔的锯齿效果
        RectF oval = new RectF(x, y + paint.ascent() - mtbRadius, x + mSize, y + paint.descent() + mtbRadius);
        //设置文字背景矩形，x为span其实左上角相对整个TextView的x值，y为span左上角相对整个View的y值。
        // paint.ascent()获得文字上边缘，paint.descent()获得文字下边缘
        canvas.drawRoundRect(oval, mRadius, mRadius, paint);//绘制圆角矩形，第二个参数是x半径，第三个参数是y半径
        if (link.getTextColor() != 0) {
            paint.setColor(link.getTextColor());
        } else {
            paint.setColor(color);//恢复画笔的文字颜色
        }
        canvas.drawText(text, start, end, x + mRadius, y, paint);//绘制文字
    }
}
