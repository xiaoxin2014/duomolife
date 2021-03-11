package com.amkj.dmsh.mine.adapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.homepage.fragment.VideoListFragment;
import com.amkj.dmsh.mine.fragment.CollectInvitationFragment;
import com.amkj.dmsh.mine.fragment.CollectReportFragment;
import com.amkj.dmsh.mine.fragment.CollectSpecialFragment;
import com.amkj.dmsh.mine.fragment.CollectTopicFragment;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/10
 * class description:请输入类描述
 */

public class MineContentPageAdapter extends FragmentPagerAdapter {
    String[] titlePage = {"帖子", "心得", "专题", "话题", "视频"};

    public MineContentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (titlePage[position]) {
            case "帖子":
                return BaseFragment.newInstance(CollectInvitationFragment.class, null, null);
            case "心得":
                return BaseFragment.newInstance(CollectReportFragment.class, null, null);
            case "专题":
                return BaseFragment.newInstance(CollectSpecialFragment.class, null, null);
            case "话题":
                return BaseFragment.newInstance(CollectTopicFragment.class, null, null);
            case "视频":
                Map<String, Object> map = new HashMap<>();
                map.put("isCollect", true);
                return BaseFragment.newInstance(VideoListFragment.class, null, map);
        }
        return null;
    }

    @Override
    public int getCount() {
        return titlePage.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titlePage[position];
    }
}
