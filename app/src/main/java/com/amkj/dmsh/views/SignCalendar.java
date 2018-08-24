package com.amkj.dmsh.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.amkj.dmsh.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lguipeng：
 */
@SuppressWarnings("deprecation")
public class SignCalendar extends ViewFlipper implements GestureDetector.OnGestureListener {
    public static final int COLOR_BG_WEEK_TITLE = Color.parseColor("#f2f9ff"); // 星期标题背景颜色
    public static final int COLOR_TX_WEEK_TITLE = Color.parseColor("#666666"); // 星期标题文字颜色
    public static final int COLOR_TX_THIS_MONTH_DAY = Color.parseColor("#666666"); // 当前月日历数字颜色
    public static final int COLOR_TX_OTHER_MONTH_DAY = Color.parseColor("#ff999999"); // 其他月日历数字颜色
    public static final int COLOR_TX_THIS_DAY = Color.parseColor("#333333"); // 当天日历数字颜色
    //    public static final int COLOR_BG_CALENDAR = Color.parseColor("#00ffffff"); // 日历背景色
    private GestureDetector gd; // 手势监听器
    private Animation push_left_in; // 动画-左进
    private Animation push_left_out; // 动画-左出
    private Animation push_right_in; // 动画-右进
    private Animation push_right_out; // 动画-右出

    private int ROWS_TOTAL = 6; // 日历的行数
    private int COLS_TOTAL = 7; // 日历的列数
    private String[][] dates = new String[6][7]; // 当前日历日期
    private float tb;
    private Paint mCirclePaint; // 绘制圆形的画笔
    private Paint mTextPaint; // 绘制文本的画笔
    private OnCalendarClickListener onCalendarClickListener; // 日历翻页回调
    private OnCalendarDateChangedListener onCalendarDateChangedListener; // 日历点击回调

    private String[] weekday = new String[]{"日", "一", "二", "三", "四", "五", "六"}; // 星期标题

    private int calendarYear; // 日历年份
    private int calendarMonth; // 日历月份
    private Date calendarDay; // 日历这个月第一天(1号)

    private LinearLayout firstCalendar; // 第一个日历
    private LinearLayout secondCalendar; // 第二个日历
    private LinearLayout currentCalendar; // 当前显示的日历

    private Map<String, Integer> marksMap = new HashMap<String, Integer>(); // 储存某个日子被标注(Integer
    // 为bitmap
    // res
    // id)
    private Map<String, Integer> dayBgColorMap = new HashMap<String, Integer>(); // 储存某个日子的背景色
    private Canvas canvas;
    private Date dateFormat;

    public SignCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SignCalendar(Context context) {
        super(context);
        init();
    }

    private void init() {
//        setBackgroundColor(R.color.transparent);
        // 实例化收拾监听器
        gd = new GestureDetector(this.getContext(), this);
        // 初始化日历翻动动画
        push_left_in = AnimationUtils.loadAnimation(getContext(), R.anim.push_left_in);
        push_left_out = AnimationUtils.loadAnimation(getContext(), R.anim.push_left_out);
        push_right_in = AnimationUtils.loadAnimation(getContext(), R.anim.push_right_in);
        push_right_out = AnimationUtils.loadAnimation(getContext(), R.anim.push_right_out);
        // 初始化第一个日历
        firstCalendar = new LinearLayout(getContext());
        firstCalendar.setOrientation(LinearLayout.VERTICAL);
        firstCalendar.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        // 初始化第二个日历
        secondCalendar = new LinearLayout(getContext());
        secondCalendar.setOrientation(LinearLayout.VERTICAL);
        secondCalendar.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        // 设置默认日历为第一个日历
        currentCalendar = firstCalendar;
        // 加入ViewFlipper
        addView(firstCalendar);
        addView(secondCalendar);
    }

    private void setCalendarColumn(Date calendarDay) {
        // 根据日历的日子获取第一天是星期几
        int weekday = calendarDay.getDay();
        // 每个月的最后一天
        int lastDay = getDateNum(calendarDay.getYear(), calendarDay.getMonth());
        ROWS_TOTAL = lastDay / COLS_TOTAL + (lastDay % COLS_TOTAL - 7 + weekday <= 0 ? 1 : 2);
    }

    private void drawFrame(LinearLayout oneCalendar) {
        // 添加周末线性布局
        LinearLayout title = new LinearLayout(getContext());
        title.setBackgroundColor(COLOR_BG_WEEK_TITLE);
        title.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layout =
                new LinearLayout.LayoutParams(MarginLayoutParams.MATCH_PARENT,
                        MarginLayoutParams.WRAP_CONTENT);
//        Resources res = getResources();
//        tb = res.getDimension(R.dimen.history_score_tb);
        // layout.setMargins(0, 0, 0, (int) (tb * 1.2));
        title.setLayoutParams(layout);
        oneCalendar.addView(title);

        // 添加周末TextView
        for (int i = 0; i < COLS_TOTAL; i++) {
            TextView view = new TextView(getContext());
            view.setGravity(Gravity.CENTER);
            view.setPadding(0, (int) (AutoUtils.getPercentHeight1px() * 24), 0, (int) (AutoUtils.getPercentHeight1px() * 24));
            view.setText(weekday[i]);
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidth1px() * 30);
//            view.setTextSize(DensityUtil.dip2px(getContext(),14));
            view.setTextColor(COLOR_TX_WEEK_TITLE);
            view.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1));
            title.addView(view);
        }

        // 添加日期布局
        LinearLayout content = new LinearLayout(getContext());
        content.setOrientation(LinearLayout.VERTICAL);
        content.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        oneCalendar.addView(content);

        // 添加日期TextView
        for (int i = 0; i < ROWS_TOTAL; i++) {
            LinearLayout row = new LinearLayout(getContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, (int) (AutoUtils.getPercentHeight1px() * 30), 0, 0);
            row.setLayoutParams(layoutParams);
            content.addView(row);
            // 绘制日历上的列
            for (int j = 0; j < COLS_TOTAL; j++) {
                LinearLayout col = new LinearLayout(getContext());
                col.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
                col.setClickable(false);
                row.addView(col);
                // 给每一个日子加上监听
                col.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewGroup parent = (ViewGroup) v.getParent();
                        int row = 0, col = 0;
                        // 获取列坐标
                        for (int i = 0; i < parent.getChildCount(); i++) {
                            if (v.equals(parent.getChildAt(i))) {
                                col = i;
                                break;
                            }
                        }
                        // 获取行坐标
                        ViewGroup pparent = (ViewGroup) parent.getParent();
                        for (int i = 0; i < pparent.getChildCount(); i++) {
                            if (parent.equals(pparent.getChildAt(i))) {
                                row = i;
                                break;
                            }
                        }
                        if (onCalendarClickListener != null) {
                            onCalendarClickListener.onCalendarClick(row, col, dates[row][col]);
                        }
                    }
                });
            }
        }
    }

    /**
     * 填充日历(包含日期、标记、背景等)
     */
    private void setCalendarDate() {
        // 根据日历的日子获取这一天是星期几
        int weekday = calendarDay.getDay();
        // 每个月第一天
        int firstDay = 1;
        // 每个月中间号,根据循环会自动++
        int day = firstDay;
        // 每个月的最后一天
        int lastDay = getDateNum(calendarDay.getYear(), calendarDay.getMonth());

        // 填充每一个空格
        for (int i = 0; i < ROWS_TOTAL; i++) {
            for (int j = 0; j < COLS_TOTAL; j++) {
                // 这个月第一天不是礼拜天,则需要绘制上个月的剩余几天
                if (i == 0 && j == 0 && weekday != 0) {
                    int year = 0;
                    int month = 0;
                    int lastMonthDays = 0;
                    // 如果这个月是1月，上一个月就是去年的12月
                    if (calendarDay.getMonth() == 0) {
                        year = calendarDay.getYear() - 1;
                        month = Calendar.DECEMBER;
                    } else {
                        year = calendarDay.getYear();
                        month = calendarDay.getMonth() - 1;
                    }
                    j = weekday - 1;
                    // 这个月第一天是礼拜天，不用绘制上个月的日期，直接绘制这个月的日期
                } else {
                    LinearLayout group = getDateView(i, j);
                    group.setGravity(Gravity.CENTER);
                    CircleTextView view = null;
                    if (group.getChildCount() > 0) {
                        view = (CircleTextView) group.getChildAt(0);
                    } else {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
                        view = new CircleTextView(getContext());
                        view.setLayoutParams(params);
                        view.setGravity(Gravity.CENTER);
                        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidth1px() * 30);
                        group.addView(view);
                    }
                    if (dateFormat == null) {
                        dateFormat = new Date();
                    }
                    // 本月
                    if (day <= lastDay) {
                        dates[i][j] = format(new Date(calendarDay.getYear(), calendarDay.getMonth(), day));
                        view.setText(Integer.toString(day));
                        // 当天
                        if (dateFormat.getDate() == day && dateFormat.getMonth() == dateFormat.getMonth()
                                && dateFormat.getYear() == calendarDay.getYear()) {
                            // view.setText("今天");
                            view.setTextColor(COLOR_TX_THIS_DAY);
                            // view.setBackgroundResource(R.drawable.bg_sign_today);
                        } else if (dateFormat.getMonth() == calendarDay.getMonth() && dateFormat.getYear() == calendarDay.getYear()) {
                            //绘制本月的颜色
                            view.setTextColor(COLOR_TX_THIS_MONTH_DAY);
                        } else {
                            //其他日期
                            view.setTextColor(COLOR_TX_OTHER_MONTH_DAY);
                        }
                        // 上面首先设置了一下默认的"当天"背景色，当有特殊需求时，才给当日填充背景色
                        // 设置日期背景色
                        if (dayBgColorMap.get(dates[i][j]) != null) {
                            // view.setTextColor(Color.WHITE);
                            //view.setBackgroundResource(dayBgColorMap.get(dates[i][j]));
                        }
                        // 设置标记
                        setMarker(group, i, j, view);
                        day++;
                        // 下个月
                    }
                }
            }
        }
    }

    /**
     * onClick接口回调
     */
    public interface OnCalendarClickListener {
        void onCalendarClick(int row, int col, String dateFormat);
    }

    /**
     * ondateChange接口回调
     */
    public interface OnCalendarDateChangedListener {
        void onCalendarDateChanged(int year, int month);
    }

    /**
     * 根据具体的某年某月，展示一个日历
     *
     * @param date
     */
    public void showCalendar(String date) {
        dateFormat = new Date();
        if (!TextUtils.isEmpty(date)) {
            dateFormat = getDateFormat(date);
        }
        // 设置日历上的日子(1号)
        calendarYear = dateFormat.getYear() + 1900;
        calendarMonth = dateFormat.getMonth();
        calendarDay = new Date(calendarYear - 1900, calendarMonth, 1);
        setCalendarColumn(calendarDay);
        // 绘制线条框架
        firstCalendar.removeAllViews();
        secondCalendar.removeAllViews();
        drawFrame(firstCalendar);
        drawFrame(secondCalendar);
        setCalendarDate();
    }

    private Date getDateFormat(String dateString) {
        SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd");
        Date tempDate;
        try {
            tempDate = s1.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            tempDate = new Date();
        }
        return tempDate;
    }

    /**
     * 根据当前月，展示一个日历
     *
     * @param
     * @param
     */
    public void showCalendar() {
        Date now = new Date();
        calendarYear = now.getYear() + 1900;
        calendarMonth = now.getMonth();
        calendarDay = new Date(calendarYear - 1900, calendarMonth, 1);
        setCalendarDate();
    }

    /**
     * 获取日历当前年份
     */
    public int getCalendarYear() {
        return calendarDay.getYear() + 1900;
    }

    /**
     * 获取日历当前月份
     */
    public int getCalendarMonth() {
        return calendarDay.getMonth() + 1;
    }

    /**
     * 在日历上做一个标记
     *
     * @param date 日期
     * @param id   bitmap res id
     */
    public void addMark(Date date, int id) {
        addMark(format(date), id);
    }

    /**
     * 在日历上做一个标记
     *
     * @param date 日期
     * @param id   bitmap res id
     */
    public void addMark(String date, int id) {
        marksMap.put(date, id);
        setCalendarDate();
    }

    /**
     * 在日历上做一组标记
     *
     * @param date 日期
     * @param id   bitmap res id
     */
    public void addMarks(Date[] date, int id) {
        for (int i = 0; i < date.length; i++) {
            marksMap.put(format(date[i]), id);
        }
        setCalendarDate();
    }

    /**
     * 在日历上做一组标记
     *
     * @param date 日期
     * @param id   bitmap res id
     */
    public void addMarks(List<String> date, int id) {
        for (int i = 0; i < date.size(); i++) {
            marksMap.put(date.get(i), id);
        }
        setCalendarDate();
    }

    /**
     * 移除日历上的标记
     */
    public void removeMark(Date date) {
        removeMark(format(date));
    }

    /**
     * 移除日历上的标记
     */
    public void removeMark(String date) {
        marksMap.remove(date);
        setCalendarDate();
    }

    /**
     * 移除日历上的所有标记
     */
    public void removeAllMarks() {
        marksMap.clear();
        setCalendarDate();
    }

    /**
     * 设置日历具体某个日期的背景色
     *
     * @param date
     * @param color
     */
    public void setCalendarDayBgColor(Date date, int color) {
        setCalendarDayBgColor(format(date), color);
    }

    /**
     * 设置日历具体某个日期的背景色
     *
     * @param date
     * @param color
     */
    public void setCalendarDayBgColor(String date, int color) {
        dayBgColorMap.put(date, color);
        setCalendarDate();
    }

    /**
     * 设置日历一组日期的背景色
     *
     * @param date
     * @param color
     */
    public void setCalendarDaysBgColor(List<String> date, int color) {
        for (int i = 0; i < date.size(); i++) {
            dayBgColorMap.put(date.get(i), color);
        }
        setCalendarDate();
    }

    /**
     * 设置日历一组日期的背景色
     *
     * @param date
     * @param color
     */
    public void setCalendarDayBgColor(String[] date, int color) {
        for (int i = 0; i < date.length; i++) {
            dayBgColorMap.put(date[i], color);
        }
        setCalendarDate();
    }

    /**
     * 移除日历具体某个日期的背景色
     *
     * @param date
     * @param
     */
    public void removeCalendarDayBgColor(Date date) {
        removeCalendarDayBgColor(format(date));
    }

    /**
     * 移除日历具体某个日期的背景色
     *
     * @param date
     * @param
     */
    public void removeCalendarDayBgColor(String date) {
        dayBgColorMap.remove(date);
        setCalendarDate();
    }

    /**
     * 移除日历具体某个日期的背景色
     *
     * @param
     * @param
     */
    public void removeAllBgColor() {
        dayBgColorMap.clear();
        setCalendarDate();
    }

    /**
     * 根据行列号获得包装每一个日子的LinearLayout
     *
     * @param row
     * @param col
     * @return
     */
    public String getDate(int row, int col) {
        return dates[row][col];
    }

    /**
     * 某天是否被标记了
     *
     * @param
     * @param
     * @return
     */
    public boolean hasMarked(String date) {
        return marksMap.get(date) == null ? false : true;
    }

    /**
     * 清除所有标记以及背景
     */
    public void clearAll() {
        marksMap.clear();
        dayBgColorMap.clear();
    }

    /***********************************************
     * private methods
     **********************************************/
    //设置标记
    private void setMarker(LinearLayout group, int i, int j, CircleTextView textView) {
        int childCount = group.getChildCount();
        //dates[i][j]=2015-12-20等为要对比的日期，marksMap中包括了dates[i][j]时就进入下面的if语句
        if (marksMap.get(dates[i][j]) != null) {
            if (childCount < 2) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidth1px() * 30);
                textView.setRadius(AutoUtils.getPercentWidth1px() * 24);
                textView.setPadding((int) (AutoUtils.getPercentWidth1px() * 15), (int) (AutoUtils.getPercentWidth1px() * 15)
                        , (int) (AutoUtils.getPercentWidth1px() * 15), (int) (AutoUtils.getPercentWidth1px() * 15));
                if (dates[i][j].equals(getDateDay(dateFormat))) {
                    textView.setCtType(2);
                    textView.setBorderAlpha(1);
                    textView.setBorderColor(Color.parseColor("#90caff"));
                    textView.setBorderWidth(AutoUtils.getPercentWidth1px() * 2);
                } else {
                    textView.setCtType(0);
                }
                textView.setBackgroundColor(Color.parseColor("#e7f3ff"));
            }
        } else {
            if (childCount > 1) {
                group.removeView(group.getChildAt(1));
            }
        }
    }

    private String getDateDay(Date dateFormat) {
        String timeDay = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            timeDay = simpleDateFormat.format(dateFormat);
            return timeDay;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * 计算某年某月有多少天
     *
     * @param year
     * @param month
     * @return
     */
    private int getDateNum(int year, int month) {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.set(Calendar.YEAR, year + 1900);
        time.set(Calendar.MONTH, month);
        return time.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据行列号获得包装每一个日子的LinearLayout
     *
     * @param row
     * @param col
     * @return
     */
    private LinearLayout getDateView(int row, int col) {
        return ((LinearLayout) ((LinearLayout) ((LinearLayout) currentCalendar.getChildAt(1))
                .getChildAt(row)).getChildAt(col));
    }

    /**
     * 将Date转化成字符串->2013-3-3
     */
    private String format(Date d) {
        return addZero(d.getYear() + 1900, 4) + "-" + addZero(d.getMonth() + 1, 2) + "-"
                + addZero(d.getDate(), 2);
    }

    // 2或4
    private static String addZero(int i, int count) {
        if (count == 2) {
            if (i < 10) {
                return "0" + i;
            }
        } else if (count == 4) {
            if (i < 10) {
                return "000" + i;
            } else if (i < 100 && i > 10) {
                return "00" + i;
            } else if (i < 1000 && i > 100) {
                return "0" + i;
            }
        }
        return "" + i;
    }

    /***********************************************
     * Override methods
     **********************************************/
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (gd != null) {
            if (gd.onTouchEvent(ev))
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return this.gd.onTouchEvent(event);
    }

    public boolean onDown(MotionEvent e) {
        return false;
    }

    public void onShowPress(MotionEvent e) {
    }

    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // 向左/上滑动
        if (e1.getX() - e2.getX() > 20) {
//            nextMonth();
        }
        // 向右/下滑动
        else if (e1.getX() - e2.getX() < -20) {
//            lastMonth();
        }
        return false;
    }

    /***********************************************
     * get/set methods
     **********************************************/

    public OnCalendarClickListener getOnCalendarClickListener() {
        return onCalendarClickListener;
    }

    public void setOnCalendarClickListener(OnCalendarClickListener onCalendarClickListener) {
        this.onCalendarClickListener = onCalendarClickListener;
    }

    public OnCalendarDateChangedListener getOnCalendarDateChangedListener() {
        return onCalendarDateChangedListener;
    }

    public void setOnCalendarDateChangedListener(
            OnCalendarDateChangedListener onCalendarDateChangedListener) {
        this.onCalendarDateChangedListener = onCalendarDateChangedListener;
    }

//    public Date getThisDay() {
//        return thisDay;
//    }
//
//    public void setThisDay(Date thisDay) {
//        this.thisDay = thisDay;
//    }

    public Map<String, Integer> getDayBgColorMap() {
        return dayBgColorMap;
    }

    public void setDayBgColorMap(Map<String, Integer> dayBgColorMap) {
        this.dayBgColorMap = dayBgColorMap;
    }
}
