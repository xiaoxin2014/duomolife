package com.amkj.dmsh.mine.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.mine.fragment.CollectInvitationFragment;
import com.amkj.dmsh.mine.fragment.CollectTopicFragment;
import com.amkj.dmsh.mine.fragment.CollectSpecialFragment;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/10
 * class description:请输入类描述
 */

public class MineContentPageAdapter extends FragmentPagerAdapter {
    String[] titlePage = {"帖子", "专题", "话题"};

    public MineContentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (titlePage[position]) {
            case "帖子":
                return BaseFragment.newInstance(CollectInvitationFragment.class, null, null);
            case "专题":
                return BaseFragment.newInstance(CollectSpecialFragment.class, null, null);
            case "话题":
                return BaseFragment.newInstance(CollectTopicFragment.class, null, null);
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
