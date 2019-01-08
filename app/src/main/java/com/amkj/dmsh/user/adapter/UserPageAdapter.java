package com.amkj.dmsh.user.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.user.fragment.UserAttentionFragment;
import com.amkj.dmsh.user.fragment.UserFansFragment;
import com.amkj.dmsh.user.fragment.UserInvitationFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/10
 * class description:请输入类描述
 */

public class UserPageAdapter extends FragmentPagerAdapter {
    private String[] titles = {"帖子", "关注", "粉丝"};
    private final Map<String, String> params;

    public UserPageAdapter(FragmentManager fm, String userId) {
        super(fm);
        params = new HashMap<>();
        params.put("userId", userId);
    }

    @Override
    public Fragment getItem(int position) {
        switch (titles[position]) {
            case "帖子":
                return BaseFragment.newInstance(UserInvitationFragment.class, params, null);
            case "关注":
                return BaseFragment.newInstance(UserAttentionFragment.class, params, null);
            case "粉丝":
                return BaseFragment.newInstance(UserFansFragment.class, params, null);
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
