package com.amkj.dmsh.shopdetails.adapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.shopdetails.fragment.MyQuestionFragment;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by xiaoxin on 2021/4/9
 * Version:v5.1.0
 */
public class QuestionPagerAdapter extends FragmentPagerAdapter {
    String[] titles = {"我的提问", "我的回答", "我的关注", "我的点赞"};
    String[] types = {"question", "reply", "follow", "favor"};

    public QuestionPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        Map<String, String> map = new HashMap<>();
        map.put("type", types[position]);
        return BaseFragment.newInstance(MyQuestionFragment.class, map, null);
    }

    @Override
    public int getCount() {
        return types.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
