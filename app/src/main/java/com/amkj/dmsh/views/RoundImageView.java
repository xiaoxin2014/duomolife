package com.amkj.dmsh.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.amkj.dmsh.R;

import java.lang.ref.WeakReference;

public class RoundImageView extends AppCompatImageView {
    // 画笔
    private Paint mPaint;
    // 取上层交集
    private Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    // 虚引用来保存Bitmap，内存不够，则虚拟机立即回收存放在虚引用中的内存
    private WeakReference<Bitmap> weakBitmap;
    // 用它来保存图片
    private Bitmap mBitmap;

    // 图片显示类型,用常量声明
    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;

    // type用于来设置从自定义控件中获取的类型
    private int type = TYPE_CIRCLE;

    // 圆角大小或者圆的半径
    private static final int RADIUS_DEFAULT = 30; // 10dp

    // 保存从自定义控件中获取的圆角大小或者圆的半径
    private int mRadius;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initViews(context, attrs);
    }

    /**
     * 初始化视图
     *
     * @param context
     * @param attrs
     */
    private void initViews(Context context, AttributeSet attrs) {
        // 初始化画笔
        //mPaint = new Paint();
        //mPaint.setAntiAlias(true);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // 获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView1);
        // 获取类型,设置默认为圆形图片e
        type = array.getInt(R.styleable.RoundImageView1_type, TYPE_CIRCLE);

        // 获取圆角大小或者圆形半径
        // private static final int RADIUS_DEFAULT = 10; //10dp
        int pxv = dp2px(30);

        // 第二个参数是一个像素值
        mRadius = array.getDimensionPixelSize(R.styleable.RoundImageView1_radius, pxv);

        array.recycle();
    }

    /**
     * 把dp装换为px
     *
     * @param dpv
     * @return
     */
    private int dp2px(int dpv) {
        // 在自定义View中获取屏幕测量对象
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        // 获取以dip单位
        int dipUnit = TypedValue.COMPLEX_UNIT_DIP;

        // 转换为px值
        float pxv = TypedValue.applyDimension(dipUnit, dpv, metrics);

        return (int) pxv;
    }

    /**
     * 测量控件
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 类型如果是圆形图片，则设置高度和宽度一致
        if (type == TYPE_CIRCLE) {
            int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
            setMeasuredDimension(size, size);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        // 获取ImageView设置的资源
        // android:src="@null"  drawable = null
        Drawable drawable = getDrawable();

        // 判断虚引用中是否保存着Bitmap
        // 虚引用的get方法weakBitmap.get()，返回存放在虚引用中对象
        Bitmap bitmap = weakBitmap == null ? null : weakBitmap.get();

        if (bitmap == null) {
            if (drawable != null) {
                // 获取资源本身固有的宽度
                int dWidth = drawable.getIntrinsicWidth();

                // 获取资源本身固有的高度
                int dHeight = drawable.getIntrinsicHeight();

                // =====================缩放处理=====================
                // Bitmap.Config.ARGB_8888 - 一个像素点包含8个字节
                // Bitmap.Config.RGB_565 - 一个像素点包含4个字节
                bitmap = Bitmap.createBitmap(
                        getWidth(), getHeight(), Bitmap.Config.ARGB_8888);

                // 创建一个画布，在这个画布上绘制图片
                Canvas drawableCanvas = new Canvas(bitmap);

                // 定义缩放大小
                float scale = 0f;

                //getWidth() - 130dp
                //dWidth - 438px
                if (type == TYPE_ROUND) {
                    scale = Math.max(getWidth() * 1.0f / dWidth,
                            getHeight() * 1.0f / dHeight);
                } else {
                    // getWidth() == getHeight()
                    scale = getWidth() * 1.0f / Math.min(dWidth, dHeight);
                }

                // 根据此比例来缩放图片
                drawable.setBounds(0, 0,
                        (int) (scale * dWidth), (int) (scale * dHeight));

                // 调用此方法后，画布上的图片就会是缩小的,缩放的图片已经画到画布上了
                drawable.draw(drawableCanvas);

                // =====================进行xforeMode图层处理=====================
                // 在绘制一个圆角的矩形或者一个圆来画到画布上
                // 使用PorterDuff.Mode.DST_IN图层模式
                Bitmap roundBitmap = getRoundBitmap();

                // 清理绘制的内容
                mPaint.reset();
                // 把滤镜效果去掉
                mPaint.setFilterBitmap(false);
                // 设置图层模式
                mPaint.setXfermode(xfermode);
                drawableCanvas.drawBitmap(roundBitmap, 0, 0, mPaint);

                // 把我们通过ImageView资源创建的画布上对应的Bitmap，再通过onDraw方法回调的canvas绘制出来
                mPaint.setXfermode(null);
                canvas.drawBitmap(bitmap, 0, 0, null);

                // 把已经绘制出来的Bitmap存放到weakBitmap虚引用中
                weakBitmap = new WeakReference<Bitmap>(bitmap);
            }
        }
        // 如果虚引用中有对应绘制的Bitmap，则直接显示绘制
        else {
            mPaint.setXfermode(null);
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }

    // 重写重绘的方法
    @Override
    public void invalidate() {
        if (weakBitmap != null) {
            Bitmap bitmap = weakBitmap.get();
            if (bitmap != null) {
                // 回收Bitmap的内存
                bitmap.recycle();
                bitmap = null;
            }
            // 回收虚引用中的内容
            weakBitmap.clear();
            weakBitmap = null;
        }

        // 再次调用onDraw方法
        super.invalidate();
    }

    public void setRadius(int radius) {
        this.mRadius = radius;
        invalidate();
    }

    /**
     * 获取绘制的圆角的矩形或者一个圆
     *
     * @return
     */
    public Bitmap getRoundBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(
                getWidth(), getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);

        if (type == TYPE_ROUND) {
            // 用画布画的内容，其实就是画到bitmap上
            canvas.drawRoundRect(
                    new RectF(0, 0, getWidth(), getHeight()),
                    mRadius,
                    mRadius,
                    paint);
        } else {
            int radius = mRadius > (getWidth() / 2) ? (getWidth() / 2) : mRadius;
            canvas.drawCircle(
                    (getWidth() / 2),
                    (getWidth() / 2),
                    radius,
                    paint);
        }
        return bitmap;
    }
}
