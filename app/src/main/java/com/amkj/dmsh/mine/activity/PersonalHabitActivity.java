package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.adapter.MineHabitTypeAdapter;
import com.amkj.dmsh.mine.bean.HabitTypeEntity;
import com.amkj.dmsh.mine.bean.HabitTypeEntity.HabitTypeBean;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/9
 * class description:兴趣爱好类别选择
 */
public class PersonalHabitActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    //    兴趣爱好
    @BindView(R.id.rel_habit_type)
    RelativeLayout rel_habit_type;
    private MineHabitTypeAdapter mineHabitTypeAdapter;
    private List<HabitTypeBean> habitTypeBeanList = new ArrayList<>();
    private int uid;
    @Override
    protected int getContentView() {
        return R.layout.activity_mine_habit_type;
    }
    @Override
    protected void initViews() {
        getLoginStatus();
        tv_header_shared.setVisibility(GONE);
        tl_normal_bar.setSelected(true);
        tv_header_titleAll.setText("选择兴趣类别");
        communal_recycler.setLayoutManager(new GridLayoutManager(PersonalHabitActivity.this, 3));
        View headerView = LayoutInflater.from(PersonalHabitActivity.this)
                .inflate(R.layout.layout_habit_type_header, (ViewGroup) communal_recycler.getParent(), false);
        mineHabitTypeAdapter = new MineHabitTypeAdapter(PersonalHabitActivity.this, habitTypeBeanList);
        mineHabitTypeAdapter.addHeaderView(headerView);
        communal_recycler.setAdapter(mineHabitTypeAdapter);

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
                loadData();
        });
        mineHabitTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HabitTypeBean habitTypeBean = (HabitTypeBean) view.getTag();
                if (habitTypeBean != null) {
                    ImageView iv_img = (ImageView) view.getTag(R.id.iv_two_tag);
                    if (iv_img.getVisibility() == VISIBLE) {
                        habitTypeBean.setIsOpen(0);
                        iv_img.setVisibility(GONE);
                    } else {
                        habitTypeBean.setIsOpen(1);
                        iv_img.setVisibility(VISIBLE);
                    }
                    habitTypeBeanList.set(position, habitTypeBean);
                }
            }
        });
    }

    @Override
    protected void loadData() {
        String url = Url.BASE_URL + Url.MINE_HABIT_TYPE;
        if (NetWorkUtils.checkNet(PersonalHabitActivity.this)) {
            Map<String, Object> params = new HashMap<>();
            params.put("uid", uid);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    habitTypeBeanList.clear();
                    Gson gson = new Gson();
                    HabitTypeEntity habitTypeEntity = gson.fromJson(result, HabitTypeEntity.class);
                    if (habitTypeEntity != null) {
                        if (habitTypeEntity.getCode().equals("01")) {
                            rel_habit_type.setVisibility(VISIBLE);
                            habitTypeBeanList.addAll(habitTypeEntity.getHabitTypeBeanList());
                            mineHabitTypeAdapter.setNewData(habitTypeBeanList);
                        } else if (!habitTypeEntity.getCode().equals("02")) {
                            rel_habit_type.setVisibility(GONE);
                            showToast(PersonalHabitActivity.this, habitTypeEntity.getMsg());
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    rel_habit_type.setVisibility(GONE);
                    showToast(PersonalHabitActivity.this, R.string.unConnectedNetwork);
                }
            });
        } else {
            smart_communal_refresh.finishRefresh();
            communal_load.setVisibility(GONE);
            mineHabitTypeAdapter.loadMoreComplete();
            showToast(PersonalHabitActivity.this, R.string.unConnectedNetwork);
        }
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(this, MineLoginActivity.class);
            startActivityForResult(intent, ConstantVariable.IS_LOGIN_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            } else {
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                getLoginStatus();
            }
        }
    }

    //    确定
    @OnClick(R.id.tv_habit_type_confirm)
    void selHabitType(View view) {
        if (habitTypeBeanList.size() > 0) {
            loadHud.show();
            StringBuffer habitString = new StringBuffer();
            for (int i = 0; i < habitTypeBeanList.size(); i++) {
                HabitTypeBean habitTypeBean = habitTypeBeanList.get(i);
                if (habitTypeBean.getIsOpen() == 1) {
                    if (habitString.length() > 0) {
                        habitString.append(",");
                    }
                    habitString.append(habitTypeBean.getId());
                }
            }
            String habitTag = habitString.toString().trim();
            if (habitTag.length() > 0) {
                setUserHabitTag(habitTag);
            } else {
                loadHud.dismiss();
                showToast(this, R.string.sel_habit_tag);
            }
        }
    }

    private void setUserHabitTag(String habitTag) {
        String url = Url.BASE_URL + Url.CHANGE_USER_HABIT;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("uid", uid);
        //文章id
        params.put("interest_ids", habitTag);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        loadData();
                        showToast(PersonalHabitActivity.this, String.format(getResources().getString(R.string.doSuccess), "修改"));
                        finish();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadHud.dismiss();
                showToast(PersonalHabitActivity.this, R.string.do_failed);
                super.onError(ex, isOnCallback);
            }
        });
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

}
