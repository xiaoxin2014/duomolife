package com.amkj.dmsh.shopdetails.fragment;

import android.os.Bundle;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.adapter.MyQuestionListAdapter;
import com.amkj.dmsh.shopdetails.bean.MyQuestionEntity;
import com.amkj.dmsh.shopdetails.bean.MyQuestionEntity.MyQuestionBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.decoration.RecycleViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipQuestionDetail;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

/**
 * Created by xiaoxin on 2021/4/9
 * Version:v5.1.0
 */
public class MyQuestionFragment extends BaseFragment {

    @BindView(R.id.communal_recycler)
    RecyclerView mCommunalRecycler;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    private String mType;
    private MyQuestionListAdapter mQuestionListAdapter;
    private List<MyQuestionBean> mQuestionList = new ArrayList<>();
    private MyQuestionEntity mMyQuestionEntity;
    private AlertDialogHelper mAlertDialogDelete;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_refresh_recycler_base;
    }

    @Override
    protected void initViews() {
        //初始化问题列表
        mCommunalRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCommunalRecycler.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.VERTICAL, AutoSizeUtils.mm2px(mAppContext, 1), getResources().getColor(R.color.text_color_e_s)));
        mQuestionListAdapter = new MyQuestionListAdapter(getActivity(), mQuestionList);
        mQuestionListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MyQuestionBean myQuestionBean = (MyQuestionBean) view.getTag();
                if (myQuestionBean != null) {
                    if (view.getId() == R.id.ll_delete) {
                        if (mAlertDialogDelete == null) {
                            mAlertDialogDelete = new AlertDialogHelper(getActivity());
                            mAlertDialogDelete.setMsg("确定删除问题吗？")
                                    .setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                        @Override
                                        public void confirm() {
                                            deleteQuestion(myQuestionBean.getQuestionId(), position);
                                        }

                                        @Override
                                        public void cancel() {

                                        }
                                    });
                        }
                        mAlertDialogDelete.show();
                    } else if (view.getId() == R.id.ll_question) {
                        skipQuestionDetail(getActivity(), myQuestionBean.getQuestionId(), myQuestionBean.getProductId());
                    }
                }
            }
        });
        mCommunalRecycler.setAdapter(mQuestionListAdapter);
        mSmartCommunalRefresh.setOnRefreshListener(refreshLayout -> loadData());
    }

    //删除问题
    private void deleteQuestion(String questionId, int position) {
        Map<String, Object> map = new HashMap<>();
        map.put("questionId", questionId);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.DELETE_QUESTION, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (SUCCESS_CODE.equals(requestStatus.getCode())) {
                        showToast("删除成功");
                        mQuestionListAdapter.remove(position);
                    } else {
                        showToast(requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {

            }
        });
    }

    @Override
    protected void loadData() {
        Map<String, String> map = new HashMap<>();
        map.put("type", mType);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_MY_QUESTION_LIST, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartCommunalRefresh.finishRefresh();
                mQuestionList.clear();
                mMyQuestionEntity = GsonUtils.fromJson(result, MyQuestionEntity.class);
                if (mMyQuestionEntity != null) {
                    List<MyQuestionBean> questionBeans = mMyQuestionEntity.getResult();
                    if (questionBeans != null && questionBeans.size() > 0) {
                        mQuestionList.addAll(questionBeans);
                    }
                }
                mQuestionListAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mQuestionList, mMyQuestionEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mQuestionList, mMyQuestionEntity);
            }
        });
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected boolean isDataInitiated() {
        return false;
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        super.getReqParams(bundle);
        mType = (String) bundle.get("type");
    }
}
