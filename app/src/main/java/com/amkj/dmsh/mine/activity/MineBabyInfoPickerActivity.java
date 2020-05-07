package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.mine.adapter.MineBabyInfoAdapter;
import com.amkj.dmsh.mine.bean.MineBabyEntity.BabyBean;
import com.amkj.dmsh.mine.bean.TimeSexOptionsBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.MINE_BABY_INFO;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/9/27
 * class description:宝宝信息 picker
 * version 3.6
 */
public class MineBabyInfoPickerActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    private List<BabyBean> babyBeanList = new ArrayList<>();
    private final String[] mineStatusName = {"备孕中", "怀孕中", "有宝宝"};
    private final String[] mineStatusPic = {"sel_baby_one_icon", "sel_baby_two_icon", "sel_baby_thr_icon"};
    //    性别
    private final List<TimeSexOptionsBean> mineBabySexList = new ArrayList<>();
    private BabyBean babyBean;
    private MineBabyInfoAdapter mineBabyInfoAdapter;
    private MineBabyFootView mineBabyFootView;
    private int babyStatus;
    //  怀孕日期选择
    private TimePickerView pregnancyTime;
    //    现有宝宝
    private TimePickerView nowBabyTime;
    //    性别选择
    private OptionsPickerView sexOptions;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_baby_info_new;
    }

    @Override
    protected void initViews() {
        getLoginStatus(MineBabyInfoPickerActivity.this);
        Intent intent = getIntent();
        babyBean = (BabyBean) intent.getExtras().get("babyBean");
        tv_header_titleAll.setText("宝宝信息");
        tv_header_shared.setCompoundDrawables(null, null, null, null);
        tv_header_shared.setText("确定");
        babyBeanList.clear();
        /**
         * 添加性别数据
         */
        mineBabySexList.add(new TimeSexOptionsBean(0, "小王子"));
        mineBabySexList.add(new TimeSexOptionsBean(1, "小公主"));

        communal_recycler.setLayoutManager(new GridLayoutManager(MineBabyInfoPickerActivity.this, 3));
        mineBabyInfoAdapter = new MineBabyInfoAdapter(MineBabyInfoPickerActivity.this, babyBeanList);
        View headerView = LayoutInflater.from(MineBabyInfoPickerActivity.this).inflate(R.layout.layout_mine_baby_header, null,false);
        View footView = LayoutInflater.from(MineBabyInfoPickerActivity.this).inflate(R.layout.layout_mine_baby_foot, null,false);
        mineBabyFootView = new MineBabyFootView();
        ButterKnife.bind(mineBabyFootView, footView);
        mineBabyInfoAdapter.addHeaderView(headerView);
        mineBabyInfoAdapter.setFooterView(footView);
        communal_recycler.setAdapter(mineBabyInfoAdapter);
        for (int i = 0; i < mineStatusName.length; i++) {
            BabyBean babyBean = new BabyBean();
            babyBean.setStatusName(mineStatusName[i]);
            babyBean.setPicStatus(mineStatusPic[i]);
            babyBeanList.add(babyBean);
        }
        if (babyBean != null && babyBean.getBaby_status() > 0 && babyBean.getBaby_status() < 4) {
            BabyBean babyNewBean = babyBeanList.get(babyBean.getBaby_status() - 1);
            babyNewBean.setBaby_status(babyBean.getBaby_status());
            babyBeanList.set(babyBean.getBaby_status() - 1, babyNewBean);
            babyStatus = babyBean.getBaby_status();
            setFootViewShow();
            mineBabyFootView.tv_mine_baby_foot_sex.setText(mineBabySexList.get(babyBean.getSex() > 0 ? 1 : 0).getPickerViewText());
            mineBabyFootView.tv_mine_baby_time.setText(getStrings(babyBean.getBirthday()));
        } else {
            BabyBean babyBean = babyBeanList.get(0);
            babyBean.setBaby_status(1);
            babyBeanList.set(0, babyBean);
            babyStatus = 1;
            setFootViewShow();
        }
        mineBabyInfoAdapter.setNewData(babyBeanList);
        mineBabyInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BabyBean babyBean = (BabyBean) view.getTag();
                if (babyBean != null) {
                    for (int i = 0; i < babyBeanList.size(); i++) {
                        BabyBean babyBeanInfo = babyBeanList.get(i);
                        if (position == i) {
                            babyBeanInfo.setBaby_status(i + 1);
                        } else {
                            babyBeanInfo.setBaby_status(0);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    babyStatus = babyBean.getBaby_status();
                    setFootViewShow();
                }
            }
        });
    }

    //   展示控件
    private void setFootViewShow() {
        switch (babyStatus) {
            case 1:
                mineBabyFootView.ll_baby_status.setVisibility(View.GONE);
                break;
            case 2:
                mineBabyFootView.ll_baby_status.setVisibility(View.VISIBLE);
                mineBabyFootView.tv_mine_baby_foot_sex.setVisibility(View.GONE);
                mineBabyFootView.tv_mine_baby_time.setVisibility(View.VISIBLE);
                mineBabyFootView.tv_mine_baby_time.setHint("请选择宝宝预产期");
                if (isTimeLess(mineBabyFootView.tv_mine_baby_time.getText().toString().trim())) {
                    mineBabyFootView.tv_mine_baby_time.setText(formatTime(Calendar.getInstance().getTime()));
                }
                break;
            case 3:
                mineBabyFootView.ll_baby_status.setVisibility(View.VISIBLE);
                mineBabyFootView.tv_mine_baby_foot_sex.setVisibility(View.VISIBLE);
                mineBabyFootView.tv_mine_baby_time.setVisibility(View.VISIBLE);
                mineBabyFootView.tv_mine_baby_time.setHint("请选择宝宝生日");
                if (isTimeGreater(mineBabyFootView.tv_mine_baby_time.getText().toString().trim())) {
                    mineBabyFootView.tv_mine_baby_time.setText(formatTime(Calendar.getInstance().getTime()));
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void loadData() {
    }

    //    配置宝宝信息
    private void setBabyInfo(String babyDate, String babySex) {
        if(userId<1){
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("baby_status", babyStatus);
        if (babyBean != null) {
            params.put("id", babyBean.getId());
        }
        if (!TextUtils.isEmpty(babySex)) {
            params.put("sex", babySex.equals(mineBabySexList.get(0).getPickerViewText()) ? 0 : 1);
        }
        if (!TextUtils.isEmpty(babyDate)) {
            params.put("birthday", babyDate);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this,MINE_BABY_INFO,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast( String.format(getResources().getString(R.string.doSuccess), "修改"));
                        finish();
                    } else {
                        showToast( R.string.do_failed);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast( R.string.do_failed);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (babyStatus) {
            case 2:
                pregnancyTime.dismiss();
                if (v.getId() == R.id.tv_time_confirm) {
                    pregnancyTime.returnData();
                }
                break;
            case 3:
                nowBabyTime.dismiss();
                if (v.getId() == R.id.tv_time_confirm) {
                    nowBabyTime.returnData();
                }
                break;
        }
    }

    class MineBabyFootView {
        @BindView(R.id.ll_baby_status)
        LinearLayout ll_baby_status;
        @BindView(R.id.tv_mine_baby_foot_sex)
        TextView tv_mine_baby_foot_sex;
        @BindView(R.id.tv_mine_baby_time)
        TextView tv_mine_baby_time;

        //        配置宝宝信息
        @OnClick(R.id.tv_mine_baby_foot_sex)
        void setBabySex() {
            if (sexOptions != null) {
                sexOptions.show(tv_mine_baby_foot_sex);
            } else {
                //返回的分别是三个级别的选中位置
                sexOptions = new OptionsPickerBuilder(MineBabyInfoPickerActivity.this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        TimeSexOptionsBean timeSexOptionsBean = mineBabySexList.get(options1);
                        tv_mine_baby_foot_sex.setText(timeSexOptionsBean.getPickerViewText());
                    }
                })
                        .setLayoutRes(R.layout.mine_baby_sex_custom, new CustomListener() {
                            @Override
                            public void customLayout(View view) {
                                TextView tv_sex_cancel = (TextView) view.findViewById(R.id.tv_sex_cancel);
                                TextView tv_sex_confirm = (TextView) view.findViewById(R.id.tv_sex_confirm);
                                tv_sex_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        sexOptions.dismiss();
                                    }
                                });
                                tv_sex_confirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        sexOptions.returnData();
                                        sexOptions.dismiss();
                                    }
                                });
                            }
                        })
                        .setContentTextSize(28)
                        .setBackgroundId(0x00ffffff)
                        .setOutSideCancelable(false)
                        .setDividerColor(Color.parseColor("#666666"))
                        .build();
                sexOptions.setPicker(mineBabySexList);//添加数据
                sexOptions.show(tv_mine_baby_foot_sex);
            }
        }

        //        配置宝宝日期
        @OnClick(R.id.tv_mine_baby_time)
        void setBabyTime() {
            if (babyStatus == 2) {
                if (pregnancyTime != null) {
                    if (isTimeLess(tv_mine_baby_time.getText().toString().trim())) {
                        tv_mine_baby_time.setText(formatTime(Calendar.getInstance().getTime()));
                    }
                    pregnancyTime.show(tv_mine_baby_time);
                } else {
                    //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
                    //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
                    Calendar selectedDate = Calendar.getInstance();
                    Calendar startDate = Calendar.getInstance();
                    Calendar endDate = Calendar.getInstance();
                    endDate.set(endDate.get(Calendar.YEAR) + 1, 11, 31);
                    //时间选择器
                    //选中事件回调
                    // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                    //年月日时分秒 的显示与否，不设置则默认全部显示
                    //                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                    pregnancyTime = new TimePickerBuilder(MineBabyInfoPickerActivity.this, new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {//选中事件回调
                            // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                            TextView tv = (TextView) v;
                            tv.setText(formatTime(date));
                        }
                    })
                            //年月日时分秒 的显示与否，不设置则默认全部显示
                            .setType(new boolean[]{true, true, true, false, false, false})
                            .setLabel("年", "月", "日", "", "", "")
                            .isCenterLabel(false)
                            .setDividerColor(Color.parseColor("#666666"))
                            .setContentTextSize(24)
                            .setBackgroundId(0x00FFFFFF)
                            .setDate(selectedDate)
                            .setRangDate(startDate, endDate)
                            .setOutSideCancelable(false)
                            .setLayoutRes(R.layout.mine_baby_date_custom, new CustomListener() {
                                @Override
                                public void customLayout(View view) {
                                    TextView tv_time_cancel = view.findViewById(R.id.tv_time_cancel);
                                    TextView tv_time_confirm = view.findViewById(R.id.tv_time_confirm);
                                    tv_time_cancel.setOnClickListener(MineBabyInfoPickerActivity.this);
                                    tv_time_confirm.setOnClickListener(MineBabyInfoPickerActivity.this);
                                }
                            })
                            .build();
                    pregnancyTime.show(tv_mine_baby_time);
                    if (isTimeLess(tv_mine_baby_time.getText().toString().trim())) {
                        tv_mine_baby_time.setText(formatTime(Calendar.getInstance().getTime()));
                    }
                }
            } else {
                if (nowBabyTime != null) {
                    if (isTimeGreater(tv_mine_baby_time.getText().toString().trim())) {
                        tv_mine_baby_time.setText(formatTime(Calendar.getInstance().getTime()));
                    }
                    nowBabyTime.show(tv_mine_baby_time);
                } else {
                    //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
                    //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
                    Calendar selectedDate = Calendar.getInstance();
                    Calendar startDate = Calendar.getInstance();
                    startDate.set(startDate.get(Calendar.YEAR) - 30, 0, 1);
                    Calendar endDate = Calendar.getInstance();
                    //时间选择器
                    //选中事件回调
                    // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                    //年月日时分秒 的显示与否，不设置则默认全部显示
                    //                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                    nowBabyTime = new TimePickerBuilder(MineBabyInfoPickerActivity.this, new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {//选中事件回调
                            // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                            TextView tv = (TextView) v;
                            tv.setText(formatTime(date));
                        }
                    })
                            //年月日时分秒 的显示与否，不设置则默认全部显示
                            .setType(new boolean[]{true, true, true, false, false, false})
                            .setLabel("年", "月", "日", "", "", "")
                            .isCenterLabel(false)
                            .setDividerColor(Color.parseColor("#666666"))
                            .setContentTextSize(24)
                            .setBackgroundId(0x00FFFFFF)
                            .setDate(selectedDate)
                            .setRangDate(startDate, endDate)
                            .setOutSideCancelable(false)
                            .setLayoutRes(R.layout.mine_baby_date_custom, new CustomListener() {
                                @Override
                                public void customLayout(View view) {
                                    TextView tv_time_cancel = view.findViewById(R.id.tv_time_cancel);
                                    TextView tv_time_confirm = view.findViewById(R.id.tv_time_confirm);
                                    tv_time_cancel.setOnClickListener(MineBabyInfoPickerActivity.this);
                                    tv_time_confirm.setOnClickListener(MineBabyInfoPickerActivity.this);
                                }
                            })
                            .build();
                    nowBabyTime.show(tv_mine_baby_time);
                    if (isTimeGreater(tv_mine_baby_time.getText().toString().trim())) {
                        tv_mine_baby_time.setText(formatTime(Calendar.getInstance().getTime()));
                    }
                }
            }
        }
    }

    /**
     * 比对是否大于当前日期
     *
     * @param dateString
     * @return
     */
    private boolean isTimeGreater(String dateString) {
        try {
            if (!TextUtils.isEmpty(dateString)) {
                SimpleDateFormat timeReceiveFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date stringText = timeReceiveFormat.parse(dateString);
                Date currentDate = Calendar.getInstance().getTime();
                return stringText.getTime() > currentDate.getTime();
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * 比对是否小于当前日期
     *
     * @param dateString
     * @return
     */
    private boolean isTimeLess(String dateString) {
        try {
            if (!TextUtils.isEmpty(dateString)) {
                SimpleDateFormat timeReceiveFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date stringText = timeReceiveFormat.parse(dateString);
                Date currentDate = Calendar.getInstance().getTime();
                return currentDate.getTime() > stringText.getTime();
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    private String formatTime(Date date) {
        try {
            SimpleDateFormat timeReceiveFormat = new SimpleDateFormat("yyyy-MM-dd");
            return timeReceiveFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @OnClick(R.id.tv_header_shared)
    void confirmedSel(View view) {
        String babySex = mineBabyFootView.tv_mine_baby_foot_sex.getText().toString().trim();
        String babyDate = mineBabyFootView.tv_mine_baby_time.getText().toString().trim();
        if (babyStatus > 2) {
            if (!TextUtils.isEmpty(babySex) && !TextUtils.isEmpty(babyDate)) {
                setBabyInfo(babyDate, babySex);
            } else if (TextUtils.isEmpty(babySex)) {
                showToast( String.format(getResources().getString(R.string.sel_no_not), "宝宝性别"));
            } else if (TextUtils.isEmpty(babyDate)) {
                showToast(String.format(getResources().getString(R.string.sel_no_not), "宝宝生日"));
            }
        } else if (babyStatus > 1) {
            if (!TextUtils.isEmpty(babyDate)) {
                setBabyInfo(babyDate, "");
            } else if (TextUtils.isEmpty(babyDate)) {
                showToast(String.format(getResources().getString(R.string.sel_no_not), "宝宝预产期"));
            }
        } else {
            setBabyInfo("", "");
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }


}
