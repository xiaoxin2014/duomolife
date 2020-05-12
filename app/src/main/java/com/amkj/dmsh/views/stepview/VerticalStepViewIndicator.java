package com.amkj.dmsh.views.stepview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.amkj.dmsh.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 日期：16/6/24 11:48
 * <p/>
 * 描述：
 */
public class VerticalStepViewIndicator extends View {
    private final String TAG_NAME = this.getClass().getSimpleName();

    //定义默认的高度   definition default height
    private int defaultStepIndicatorNum = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());

    private float mCompletedLineHeight;//完成线的高度     definition completed line height
    private float mCircleRadius;//圆的半径  definition circle radius

    private Drawable mCompleteIcon;//完成的默认图片    definition default completed icon
    private Drawable mAttentionIcon;//正在进行的默认图片     definition default underway icon
    private Drawable mDefaultIcon;//默认的背景图  definition default unCompleted icon
    private float mCenterX;//该View的X轴的中间位置
    private float mLeftY;
    private float mRightY;

    private int mStepNum = 0;//当前有几部流程    there are currently few step
    private float mLinePadding;//两条连线之间的间距  definition the spacing between the two circles

    private List<Float> mCircleCenterPointPositionList;//定义所有圆的圆心点位置的集合 definition all of circles center point list
    private Paint mUnCompletedPaint;//未完成Paint  definition mUnCompletedPaint
    private Paint mCompletedPaint;//完成paint      definition mCompletedPaint
    private int mUnCompletedLineColor = ContextCompat.getColor(getContext(), R.color.text_gray_c);//定义默认未完成线的颜色  definition mUnCompletedLineColor
    private int mCompletedLineColor = R.color.text_gray_c;//定义默认完成线的颜色      definition mCompletedLineColor
    private PathEffect mEffects;

    private int mComplectingPosition;//正在进行position   underway position
    private Path mPath;

    private OnDrawIndicatorListener mOnDrawListener;
    private Rect mRect;
    private int mHeight;//这个控件的动态高度    this view dynamic height
    private boolean mIsReverseDraw;//is reverse draw this view;


    /**
     * 设置监听
     *
     * @param onDrawListener
     */
    public void setOnDrawListener(OnDrawIndicatorListener onDrawListener) {
        mOnDrawListener = onDrawListener;
    }

    /**
     * get圆的半径  get circle radius
     *
     * @return
     */
    public float getCircleRadius() {
        return mCircleRadius;
    }


    public VerticalStepViewIndicator(Context context) {
        this(context, null);
    }

    public VerticalStepViewIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalStepViewIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * init
     */
    private void init() {
        mPath = new Path();
        mEffects = new DashPathEffect(new float[]{8, 8, 8, 8}, 1);

        mCircleCenterPointPositionList = new ArrayList<>();//初始化

        mUnCompletedPaint = new Paint();
        mCompletedPaint = new Paint();
        mUnCompletedPaint.setAntiAlias(true);
        mUnCompletedPaint.setColor(mUnCompletedLineColor);
        mUnCompletedPaint.setStyle(Paint.Style.STROKE);
        mUnCompletedPaint.setStrokeWidth(2);

        mCompletedPaint.setAntiAlias(true);
        mCompletedPaint.setColor(mCompletedLineColor);
        mCompletedPaint.setStyle(Paint.Style.STROKE);
        mCompletedPaint.setStrokeWidth(2);

        mUnCompletedPaint.setPathEffect(mEffects);
        mCompletedPaint.setStyle(Paint.Style.FILL);

        //已经完成线的宽高 set mCompletedLineHeight
        mCompletedLineHeight = 0.05f * defaultStepIndicatorNum;
        //圆的半径  set mCircleRadius
        mCircleRadius = 0.28f * defaultStepIndicatorNum;
        //线与线之间的间距    set mLinePadding
        mLinePadding = 0.85f * defaultStepIndicatorNum;

        mCompleteIcon = ContextCompat.getDrawable(getContext(), R.drawable.complted);//已经完成的icon
        mAttentionIcon = ContextCompat.getDrawable(getContext(), R.drawable.attention);//正在进行的icon
        mDefaultIcon = ContextCompat.getDrawable(getContext(), R.drawable.default_icon);//未完成的icon

        mIsReverseDraw = true;//default draw
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG_NAME, "onMeasure");
        int width = defaultStepIndicatorNum;
        mHeight = 0;
        if (mStepNum > 0) {
            int height = 0;
            if (null != mTextViewList) {
                for (int i = 0; i < mTextViewList.size(); i++) {
                    height += mTextViewList.get(i);
                }
            }

            //dynamic measure VerticalStepViewIndicator height  (mStepNum - 1) * mLinePadding+mCircleRadius*2
            //mCircleRadius * 2 * mStepNum + height+(mStepNum - 1) * mLinePadding)
            mHeight = height + 10;//此处和右边布局高度吻合，不写死
        }
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
            width = Math.min(width, MeasureSpec.getSize(widthMeasureSpec));
        }
        setMeasuredDimension(width, mHeight);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i("xxx", "onSizeChanged");
        mCenterX = getWidth() / 2;
        mLeftY = mCenterX - (mCompletedLineHeight / 2);
        mRightY = mCenterX + (mCompletedLineHeight / 2);
        mHeight = h;
        mCircleCenterPointPositionList.clear();
        for (int i = 0; i < mStepNum; i++) {
            //reverse draw VerticalStepViewIndicator
            if (mIsReverseDraw) {
                if (mTextViewList != null && mTextViewList.size() > 0) {

                    mCircleCenterPointPositionList.add(mHeight - (mCircleRadius + i * mCircleRadius * 2 + i * mLinePadding + mTextViewList.get(i)));
                } else {
                    mCircleCenterPointPositionList.add(mHeight - (mCircleRadius + i * mCircleRadius * 2 + i * mLinePadding));
                }
            } else {
                if (mTextViewList != null && mTextViewList.size() > 0) {
                    if (i == 0) {
                        // mCircleCenterPointPositionList.add(mCircleRadius + i * mCircleRadius * 2 + i *mLinePadding+dpToPx(10,getResources()));  如果需要设置Margin
                        mCircleCenterPointPositionList.add(mCircleRadius + i * mCircleRadius * 2 + i * mLinePadding);
                    } else {
                        mCircleCenterPointPositionList.add(mCircleCenterPointPositionList.get(i - 1) + Float.valueOf(mTextViewList.get(i - 1)));
                    }
                } else {
                    mCircleCenterPointPositionList.add(mCircleRadius + i * mCircleRadius * 2 + i * mLinePadding);
                }

            }
        }
        /**
         * set listener
         */
        if (mOnDrawListener != null) {
            mOnDrawListener.ondrawIndicator();
        }
    }

    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("xxx", "onDraw");
        if (mOnDrawListener != null) {
            mOnDrawListener.ondrawIndicator();
        }
        mUnCompletedPaint.setColor(mUnCompletedLineColor);
        mCompletedPaint.setColor(mCompletedLineColor);

        //-----------------------画线-------draw line-----------------------------------------------
        for (int i = 0; i < mCircleCenterPointPositionList.size() - 1; i++) {
            //前一个ComplectedXPosition
            final float preComplectedXPosition = mCircleCenterPointPositionList.get(i);
            //后一个ComplectedXPosition
            final float afterComplectedXPosition = mCircleCenterPointPositionList.get(i + 1);

            if (i < mComplectingPosition)//判断在完成之前的所有点
            {
                //判断在完成之前的所有点，画完成的线，这里是矩形,很细的矩形，类似线，为了做区分，好看些
                if (mIsReverseDraw) {
                    canvas.drawRect(mLeftY, afterComplectedXPosition + mCircleRadius - 10, mRightY, preComplectedXPosition - mCircleRadius + 10, mCompletedPaint);
                } else {
                    canvas.drawRect(mLeftY, preComplectedXPosition + mCircleRadius - 10, mRightY, afterComplectedXPosition - mCircleRadius + 10, mCompletedPaint);
                }
            } else {
                if (mIsReverseDraw) {
                    mPath.moveTo(mCenterX, afterComplectedXPosition + mCircleRadius);
                    mPath.lineTo(mCenterX, preComplectedXPosition - mCircleRadius);
                    canvas.drawPath(mPath, mUnCompletedPaint);
                } else {
                    mPath.moveTo(mCenterX, preComplectedXPosition + mCircleRadius);
                    mPath.lineTo(mCenterX, afterComplectedXPosition - mCircleRadius);
                    canvas.drawPath(mPath, mUnCompletedPaint);
                }

            }
        }
        //-----------------------画线-------draw line-----------------------------------------------

        //-----------------------画图标-----draw icon-----------------------------------------------
        for (int i = 0; i < mCircleCenterPointPositionList.size(); i++) {
            final float currentComplectedXPosition = mCircleCenterPointPositionList.get(i);
            mRect = new Rect((int) (mCenterX - mCircleRadius), (int) (currentComplectedXPosition - mCircleRadius), (int) (mCenterX + mCircleRadius), (int) (currentComplectedXPosition + mCircleRadius));
            if (i < mComplectingPosition) {
                mCompleteIcon.setBounds(mRect);
                mCompleteIcon.draw(canvas);
            } else if (i == mComplectingPosition && mCircleCenterPointPositionList.size() != 1) {
                mCompletedPaint.setColor(Color.WHITE);
                canvas.drawCircle(mCenterX, currentComplectedXPosition, mCircleRadius * 1.1f, mCompletedPaint);
                mAttentionIcon.setBounds(mRect);
                mAttentionIcon.draw(canvas);
            } else {
                mDefaultIcon.setBounds(mRect);
                mDefaultIcon.draw(canvas);
            }
        }
        //-----------------------画图标-----draw icon-----------------------------------------------
    }


    /**
     * 得到所有圆点所在的位置
     *
     * @return
     */
    public List<Float> getCircleCenterPointPositionList() {
        return mCircleCenterPointPositionList;
    }

    private List<Integer> mTextViewList;


    /**
     * 设置流程步数
     *
     * @param stepNum 流程步数
     */
    public void setStepNum(int stepNum) {
        this.mStepNum = stepNum;
    }

    public void setTextViewList(List<Integer> mList) {
        this.mTextViewList = mList;
        requestLayout();
    }

    /**
     * 设置线间距的比例系数 set linePadding proportion
     *
     * @param linePaddingProportion
     */
    public void setIndicatorLinePaddingProportion(float linePaddingProportion) {
        this.mLinePadding = linePaddingProportion * defaultStepIndicatorNum;
    }

    /**
     * 设置正在进行position
     *
     * @param complectingPosition
     */
    public void setComplectingPosition(int complectingPosition) {
        this.mComplectingPosition = complectingPosition;
        requestLayout();
    }

    /**
     * 设置未完成线的颜色
     *
     * @param unCompletedLineColor
     */
    public void setUnCompletedLineColor(int unCompletedLineColor) {
        this.mUnCompletedLineColor = unCompletedLineColor;
    }

    /**
     * 设置已完成线的颜色
     *
     * @param completedLineColor
     */
    public void setCompletedLineColor(int completedLineColor) {
        this.mCompletedLineColor = completedLineColor;
    }

    /**
     * is reverse draw 是否倒序画
     */
    public void reverseDraw(boolean isReverseDraw) {
        this.mIsReverseDraw = isReverseDraw;
        invalidate();
    }

    /**
     * 设置默认图片
     *
     * @param defaultIcon
     */
    public void setDefaultIcon(Drawable defaultIcon) {
        this.mDefaultIcon = defaultIcon;
    }

    /**
     * 设置已完成图片
     *
     * @param completeIcon
     */
    public void setCompleteIcon(Drawable completeIcon) {
        this.mCompleteIcon = completeIcon;
    }

    /**
     * 设置正在进行中的图片
     *
     * @param attentionIcon
     */
    public void setAttentionIcon(Drawable attentionIcon) {
        this.mAttentionIcon = attentionIcon;
    }

    /**
     * 设置对view监听
     */
    public interface OnDrawIndicatorListener {
        void ondrawIndicator();
    }
}