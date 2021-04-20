package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.BaseAddCarProInfoBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dao.SoftApiDao;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.QuestionsEntity.ResultBean;
import com.amkj.dmsh.shopdetails.activity.QuestionsEntity.ResultBean.ReplyBean;
import com.amkj.dmsh.shopdetails.adapter.AnswerAdapter;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.decoration.RecycleViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.dao.OrderDao.addShopCarGetSku;
import static com.amkj.dmsh.dao.SoftApiDao.favorAnswer;

/**
 * Created by xiaoxin on 2021/4/8
 * Version:v5.1.0
 * ClassDescription :问题详情
 */
public class QuestionDetailActivity extends BaseActivity {

    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.view_divider)
    View mViewDivider;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_all_question)
    TextView mTvAllQuestion;
    @BindView(R.id.tv_follow)
    TextView mTvFollow;
    @BindView(R.id.rv_answer)
    RecyclerView mRvAnswer;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    @BindView(R.id.tv_question)
    TextView mTvQuestion;
    @BindView(R.id.et_answer)
    EditText mEtAnswer;
    @BindView(R.id.fl_content)
    FrameLayout mFlContent;
    private QuestionsEntity mQuestionsEntity;
    private String mQuestionId;
    private String mProductId;
    private List<ReplyBean> mReplyList = new ArrayList<>();
    private AnswerAdapter mAnswerAdapter;
    private ResultBean mResultBean;
    private AlertDialogHelper mAlertDialogDelete;

    @Override
    protected int getContentView() {
        return R.layout.activity_question_detail;
    }

    @Override
    protected void initViews() {
        mQuestionId = getIntent().getStringExtra("questionId");
        mProductId = getIntent().getStringExtra("productId");
        if (TextUtils.isEmpty(mQuestionId) || TextUtils.isEmpty(mProductId)) {
            showToast("数据有误，请重试");
            finish();
        }
        mTvHeaderShared.setCompoundDrawables(null, null, null, null);
        mTvHeaderShared.setText("我的回答");
        mTvHeaderShared.setTextColor(getResources().getColor(R.color.text_login_gray_s));
        mTvHeaderTitle.setText("问题详情");
        //初始化回答列表
        mRvAnswer.setLayoutManager(new LinearLayoutManager(this));
        mRvAnswer.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, R.drawable.item_divider_gray_f_one_px));
        mAnswerAdapter = new AnswerAdapter(this, mReplyList);
        mRvAnswer.setAdapter(mAnswerAdapter);
        mAnswerAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ReplyBean replyBean = (ReplyBean) view.getTag();
                if (replyBean != null) {
                    if (view.getId() == R.id.ll_delete) {
                        if (mAlertDialogDelete == null) {
                            mAlertDialogDelete = new AlertDialogHelper(getActivity());
                            mAlertDialogDelete.setMsg("确定删除回答吗？")
                                    .setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                        @Override
                                        public void confirm() {
                                            deleteAnswer(replyBean.getReplyId(), position);
                                        }

                                        @Override
                                        public void cancel() {

                                        }
                                    });
                        }
                        mAlertDialogDelete.show();
                    } else if (view.getId() == R.id.tv_favor) {
                        favorAnswer(getActivity(), replyBean, (TextView) view);
                    }
                }
            }
        });
        mSmartCommunalRefresh.setOnRefreshListener(refreshLayout -> loadData());
    }


    //删除回答
    private void deleteAnswer(String replyId, int position) {
        Map<String, Object> map = new HashMap<>();
        map.put("replyId", replyId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.DELETE_ANSWER, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (SUCCESS_CODE.equals(requestStatus.getCode())) {
                        showToast("删除成功");
                        mAnswerAdapter.remove(position);
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
        map.put("questionId", mQuestionId);
        map.put("productId", mProductId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_QUESTION_DETAIL, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartCommunalRefresh.finishRefresh();
                mReplyList.clear();
                mQuestionsEntity = GsonUtils.fromJson(result, QuestionsEntity.class);
                if (mQuestionsEntity != null) {
                    String code = mQuestionsEntity.getCode();
                    mResultBean = mQuestionsEntity.getResult();
                    if (SUCCESS_CODE.equals(code)) {
                        List<ReplyBean> replyList = mResultBean.getReplyList();
                        if (replyList != null && replyList.size() > 0) {
                            mReplyList.addAll(replyList);
                        }

                        GlideImageLoaderUtil.loadCenterCrop(getActivity(), mIvCover, mResultBean.getProductImg());
                        mTvName.setText(mResultBean.getProductName());
                        mTvQuestion.setText(mResultBean.getContent());
                        mTvFollow.setEnabled(mResultBean.isFollow());
                        mTvFollow.setText(mResultBean.isFollow() ? "已关注" : "关注问题");
                    }
                }
                mTvAllQuestion.setVisibility(mReplyList.size() > 0 ? View.VISIBLE : View.GONE);
                mTvAllQuestion.setText(getStringsFormat(getActivity(), R.string.all_answer, mResultBean.getCount()));
                mEtAnswer.setEnabled(mResultBean.isReply());//未购买用户不可唤起键盘
                mEtAnswer.setHint(mResultBean.isReply() ? "我来回答" : "只有购买过的用户才可以回答！");
                mAnswerAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mQuestionsEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                mTvAllQuestion.setVisibility(mReplyList.size() > 0 ? View.VISIBLE : View.GONE);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mQuestionsEntity);
            }
        });
    }


    @OnClick({R.id.tv_life_back, R.id.tv_header_shared, R.id.iv_shopcar, R.id.ll_follow, R.id.tv_all_question, R.id.tv_answer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            //我的回答
            case R.id.tv_header_shared:
                Intent intent = new Intent(this, MyQuestionListActivity.class);
                startActivity(intent);
                break;
            //关注问题
            case R.id.ll_follow:
                SoftApiDao.followQuestion(this, mQuestionId, mTvFollow);
                break;
            case R.id.iv_shopcar:
                BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                baseAddCarProInfoBean.setProductId(mResultBean.getProductId());
                baseAddCarProInfoBean.setProName(getStrings(mResultBean.getProductName()));
                baseAddCarProInfoBean.setProPic(getStrings(mResultBean.getProductImg()));
                addShopCarGetSku(this, baseAddCarProInfoBean);
                break;
            //全部回答
            case R.id.tv_all_question:
                Intent intent1 = new Intent(this, QuestionListActivity.class);
                intent1.putExtra("productId", mProductId);
                startActivity(intent1);
                break;
            //回答问题
            case R.id.tv_answer:
                String question = mEtAnswer.getText().toString().trim();
                if (userId == 0) {
                    getLoginStatus(this);
                } else if (mResultBean != null && !mResultBean.isReply()) {
                    showToast("只有购买过的用户才可以回答！");
                } else if (TextUtils.isEmpty(question)) {
                    showToast("请输入内容");
                } else {
                    answerQuestion(question);
                }
                break;
        }
    }

    private void answerQuestion(String answer) {
        showLoadhud(this);
        Map<String, String> map = new HashMap<>();
        map.put("questionId", mQuestionId);
        map.put("content", answer);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.ANSWER_QUESTION, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(getActivity());
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (SUCCESS_CODE.equals(requestStatus.getCode())) {
                        showToast("回答成功");
                        mEtAnswer.setText("");
                        CommonUtils.hideSoftInput(mEtAnswer.getContext(), mEtAnswer);
                        loadData();
                    } else {
                        showToast(requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(getActivity());
            }
        });
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mFlContent;
    }

}
