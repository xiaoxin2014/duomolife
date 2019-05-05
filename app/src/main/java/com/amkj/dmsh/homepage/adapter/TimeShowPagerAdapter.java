package com.amkj.dmsh.homepage.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.homepage.bean.TimeShowShaftEntity.TimeShowShaftBean;
import com.amkj.dmsh.homepage.fragment.SpringSaleFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.amkj.dmsh.constant.ConstantMethod.getNumber;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/4/21
 * class description:限时特惠
 */

public class TimeShowPagerAdapter extends FragmentPagerAdapter {
    private List<TimeShowShaftBean> timeShowBeanList;
    private final List<TimeShowShaftBean> timeShowBeanWaitList = new ArrayList<>();
    private final List<TimeShowShaftBean> timeShowBeanOpeningList = new ArrayList<>();
    private Map<String, Object> params = new HashMap<>();

    public TimeShowPagerAdapter(FragmentManager fm, List<TimeShowShaftBean> timeShowBeanList, String systemTime) {
        super(fm);
        for (TimeShowShaftBean timeShowBean : timeShowBeanList) {
            if (timeShowBean.getHourShaft() != null
                    && timeShowBean.getHourShaft().size() > 0) {
                if (timeShowBean.getType() > 1) {
                    timeShowBeanWaitList.add(timeShowBean);
                } else if (timeShowBean.getType() == 1) {
                    List<String> openString = new ArrayList<>();
                    List<String> waitString = new ArrayList<>();
                    for (int i = 0; i < timeShowBean.getHourShaft().size(); i++) {
                        String shaftTime = timeShowBean.getHourShaft().get(i);
                        if(Integer.parseInt(getNumber(shaftTime))>getTimeHour(systemTime)){
                            waitString.add(shaftTime);
                        }else{
                            openString.add(shaftTime);
                        }
                    }
                    if(openString.size()>0){
                        TimeShowShaftBean timeShowWaitBean = new TimeShowShaftBean();
                        timeShowWaitBean.setType(timeShowBean.getType());
                        timeShowWaitBean.setDate(timeShowBean.getDate());
                        timeShowWaitBean.setName(timeShowBean.getName());
                        timeShowWaitBean.setHourShaft(openString);
                        timeShowBeanOpeningList.add(timeShowWaitBean);
                    }
                    if(waitString.size()>0){
                        TimeShowShaftBean timeShowWaitBean = new TimeShowShaftBean();
                        timeShowWaitBean.setType(timeShowBean.getType());
                        timeShowWaitBean.setDate(timeShowBean.getDate());
                        timeShowWaitBean.setName(timeShowBean.getName());
                        timeShowWaitBean.setHourShaft(waitString);
                        timeShowBeanWaitList.add(timeShowWaitBean);
                    }
                } else {
                    timeShowBeanOpeningList.add(timeShowBean);
                }
            }
        }
    }

    private int getTimeHour(String systemTime) {
        Calendar calendar = Calendar.getInstance();
        if(!TextUtils.isEmpty(systemTime)){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            try {
                calendar.setTime(formatter.parse(systemTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    @Override
    public Fragment getItem(int position) {
        params.clear();
        timeShowBeanList = new ArrayList<>();
        if (position == 0) {
            timeShowBeanList.addAll(timeShowBeanOpeningList);
        } else {
            timeShowBeanList.addAll(timeShowBeanWaitList);
        }
        params.put("showTime", timeShowBeanList);
        params.put("position", String.valueOf(position));
        return BaseFragment.newInstance(SpringSaleFragment.class, null, params);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
