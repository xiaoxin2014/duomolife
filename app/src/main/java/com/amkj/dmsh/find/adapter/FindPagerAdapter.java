package com.amkj.dmsh.find.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.find.fragment.AttentionSuperFragment;
import com.amkj.dmsh.find.fragment.FindAllTopicFragment;
import com.amkj.dmsh.find.fragment.FindRecommendTopicFragment;
import com.amkj.dmsh.find.fragment.RecommendSuperFragment;

import java.util.HashMap;
import java.util.Map;

import static com.amkj.dmsh.find.activity.FindTopicDetailsActivity.TOPIC_TYPE;

/**
 * Created by atd48 on 2016/6/21.
 */
public class FindPagerAdapter extends FragmentPagerAdapter {
    private final String type;
    private String[] findTitle = {"人气", "关注"};
    private String[] topicTitle = {"推荐", "全部"};
    private Map<String,String> params = new HashMap<>();

    public FindPagerAdapter(FragmentManager fm, String type,String topicId) {
        super(fm);
        this.type = type;
        if(TOPIC_TYPE.equals(type)){
            params.put("topicId",topicId);
        }
    }

    @Override
    public int getCount() {
        return type.equals(TOPIC_TYPE) ? topicTitle.length : findTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return type.equals(TOPIC_TYPE) ? topicTitle[position] : findTitle[position];
    }

    @Override
    public Fragment getItem(int position) {
        if(type.equals(TOPIC_TYPE)){
            if (position == 0) {
                return BaseFragment.newInstance(FindRecommendTopicFragment.class, params, null);
            } else {
                return BaseFragment.newInstance(FindAllTopicFragment.class, params, null);
            }
        }else{
            if (position == 0) {
                params.put("cate", "recommend");
                return BaseFragment.newInstance(RecommendSuperFragment.class, params, null);
            } else {
                params.put("cate", "attention");
                return BaseFragment.newInstance(AttentionSuperFragment.class, params, null);
            }
        }
    }
}