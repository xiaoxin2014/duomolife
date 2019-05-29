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
import com.amkj.dmsh.mine.adapter.MineHabitTypeAdapter;
import com.amkj.dmsh.mine.bean.HabitTypeEntity;
import com.amkj.dmsh.mine.bean.HabitTypeEntity.HabitTypeBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.CHANGE_USER_HABIT;
import static com.amkj.dmsh.constant.Url.MINE_HABIT_TYPE;

;
;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/9
 * class description:兴趣爱好类别选择
 */
public class PersonalHabitActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
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
    private HabitTypeEntity habitTypeEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_habit_type;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_shared.setVisibility(GONE);
        tl_normal_bar.setSelected(true);
        tv_header_titleAll.setText("选择兴趣类别");
        communal_recycler.setLayoutManager(new GridLayoutManager(PersonalHabitActivity.this, 3));
        View headerView = LayoutInflater.from(PersonalHabitActivity.this)
                .inflate(R.layout.layout_habit_type_header, (ViewGroup) communal_recycler.getParent(), false);
        mineHabitTypeAdapter = new MineHabitTypeAdapter(PersonalHabitActivity.this, habitTypeBeanList);
        mineHabitTypeAdapter.addHeaderView(headerView);
        communal_recycler.setAdapter(mineHabitTypeAdapter);

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
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
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_HABIT_TYPE
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        habitTypeBeanList.clear();
                        Gson gson = new Gson();
                        habitTypeEntity = gson.fromJson(result, HabitTypeEntity.class);
                        if (habitTypeEntity != null) {
                            if (habitTypeEntity.getCode().equals(SUCCESS_CODE)) {
                                rel_habit_type.setVisibility(VISIBLE);
                                habitTypeBeanList.addAll(habitTypeEntity.getHabitTypeBeanList());
                                mineHabitTypeAdapter.setNewData(habitTypeBeanList);
                            } else if (!habitTypeEntity.getCode().equals(EMPTY_CODE)) {
                                rel_habit_type.setVisibility(GONE);
                                showToast(PersonalHabitActivity.this, habitTypeEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, habitTypeEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        rel_habit_type.setVisibility(GONE);
                        mineHabitTypeAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, habitTypeEntity);
                    }

                    @Override
                    public void netClose() {
                        showToast(PersonalHabitActivity.this, R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(PersonalHabitActivity.this, R.string.invalidData);
                    }
                });
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("uid", userId);
        //文章id
        params.put("interest_ids", habitTag);
        NetLoadUtils.getNetInstance().loadNetDataPost(this,CHANGE_USER_HABIT,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        loadData();
                        showToast(PersonalHabitActivity.this, String.format(getResources().getString(R.string.doSuccess), "修改"));
                        finish();
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                loadHud.dismiss();
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(PersonalHabitActivity.this, R.string.do_failed);
            }

            @Override
            public void netClose() {
                showToast(PersonalHabitActivity.this, R.string.unConnectedNetwork);
            }
        });
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

}
