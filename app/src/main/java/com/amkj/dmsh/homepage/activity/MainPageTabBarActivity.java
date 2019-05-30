package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.catergory.fragment.QualityFragment;
import com.amkj.dmsh.find.fragment.FindFragment;
import com.amkj.dmsh.homepage.fragment.AliBCFragment;
import com.amkj.dmsh.homepage.fragment.HomePageFragment;
import com.amkj.dmsh.homepage.fragment.TimeShowNewFragment;
import com.amkj.dmsh.mine.fragment.MineDataFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_FIND;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_HOME;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_MINE;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_QUALITY;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_TIME;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_TEXT;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/11/2
 * version 3.1.8 原则上必须五个版本后才能直接调用跳转
 * class description:为了兼容 跳转底部导航栏菜单， 找不到类型
 */
public class MainPageTabBarActivity extends BaseActivity {
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private BaseFragment fragment;

    @Override
    protected int getContentView() {
        return R.layout.activity_main_page_tab_bar;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        String tabType = intent.getStringExtra("tabType");
        if(TextUtils.isEmpty(tabType)){
            return;
        }
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        if (isWebUrl(tabType)) {
            Map<String, String> params = new HashMap<>();
            params.put("paddingStatus", "true");
            params.put("loadUrl", tabType);
            fragment = BaseFragment.newInstance(AliBCFragment.class, params, null);
        }else{
            switch (getStrings(tabType)) {
                case MAIN_HOME:
                    fragment = BaseFragment.newInstance(HomePageFragment.class, null, null);
                    break;
                case MAIN_QUALITY:
                    fragment = BaseFragment.newInstance(QualityFragment.class, null, null);
                    break;
                case MAIN_TIME:
                    fragment = BaseFragment.newInstance(TimeShowNewFragment.class, null, null);
                    break;
                case MAIN_FIND:
                    fragment = BaseFragment.newInstance(FindFragment.class, null, null);
                    break;
                case MAIN_MINE:
                    fragment = BaseFragment.newInstance(MineDataFragment.class, null, null);
                    break;
                default:
                    fragment = BaseFragment.newInstance(HomePageFragment.class, null, null);
                    break;
            }
        }
        transaction.add(R.id.fl_main_page_container, fragment, null).commitAllowingStateLoss();
    }

    @Override
    protected void loadData() {}

    /**
     * 是否网页地址
     *
     * @param androidLink
     * @return
     */
    private boolean isWebUrl(String androidLink) {
        if (TextUtils.isEmpty(androidLink)) {
            return false;
        }
        Matcher matcher = Pattern.compile(REGEX_TEXT).matcher(androidLink);
        while (matcher.find()) {
            return true;
        }
        return false;
    }
}
