package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.BaseAddCarProInfoBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.QuestionsEntity.ResultBean.QuestionInfoBean;
import com.amkj.dmsh.shopdetails.adapter.QuestionListAdapter;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
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
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.dao.OrderDao.addShopCarGetSku;

/**
 * Created by xiaoxin on 2021/4/9
 * Version:v5.1.0
 * ClassDescription :问题列表（问大家）
 */
public class QuestionListActivity extends BaseActivity {
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
    @BindView(R.id.iv_shopcar)
    ImageView mIvShopcar;
    @BindView(R.id.rv_question)
    RecyclerView mRvQuestion;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    @BindView(R.id.et_question)
    EditText mEtQuestion;
    @BindView(R.id.tv_ask)
    TextView mTvAsk;
    @BindView(R.id.ll_ask)
    LinearLayout mLlAsk;
    @BindView(R.id.fl_content)
    FrameLayout mFlContent;
    @BindView(R.id.ll_empty)
    LinearLayout mLlEmpty;
    private String mProductId;
    private QuestionListAdapter mQuestionListAdapter;
    private List<QuestionInfoBean> mQuestionList = new ArrayList<>();
    private QuestionsEntity mQuestionsEntity;
    private QuestionsEntity.ResultBean mResultBean;

    @Override
    protected int getContentView() {
        return R.layout.activity_question_list;
    }

    @Override
    protected void initViews() {
        mProductId = getIntent().getStringExtra("productId");
        if (TextUtils.isEmpty(mProductId)) {
            showToast("数据有误，请重试");
            finish();
        }
        mTvHeaderShared.setCompoundDrawables(null, null, null, null);
        mTvHeaderShared.setText("我的回答");
        mTvHeaderShared.setTextColor(getResources().getColor(R.color.text_login_gray_s));
        mTvHeaderTitle.setText("问大家");
        //初始化问题列表
        mRvQuestion.setLayoutManager(new LinearLayoutManager(this));
        mRvQuestion.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, AutoSizeUtils.mm2px(this, 1), getResources().getColor(R.color.text_color_e_s)));
        mQuestionListAdapter = new QuestionListAdapter(this, mQuestionList, mProductId);
        mRvQuestion.setAdapter(mQuestionListAdapter);
        mSmartCommunalRefresh.setOnRefreshListener(refreshLayout -> loadData());
    }

    @Override
    protected void loadData() {
        Map<String, String> map = new HashMap<>();
        map.put("productId", mProductId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_QUESTION_LIST, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartCommunalRefresh.finishRefresh();
                mQuestionList.clear();
                mQuestionsEntity = GsonUtils.fromJson(result, QuestionsEntity.class);
                if (mQuestionsEntity != null) {
                    String code = mQuestionsEntity.getCode();
                    mResultBean = mQuestionsEntity.getResult();
                    if (SUCCESS_CODE.equals(code)) {
                        List<QuestionInfoBean> questionInfoList = mResultBean.getQuestionInfoList();
                        if (questionInfoList != null && questionInfoList.size() > 0) {
                            mQuestionList.addAll(questionInfoList);
                        }

                        GlideImageLoaderUtil.loadCenterCrop(getActivity(), mIvCover, mResultBean.getProductImg());
                        mTvName.setText(mResultBean.getProductName());
                    }
                }
                mQuestionListAdapter.notifyDataSetChanged();
                mLlEmpty.setVisibility(mQuestionList.size() == 0 ? View.VISIBLE : View.GONE);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mQuestionsEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                mLlEmpty.setVisibility(mQuestionList.size() == 0 ? View.VISIBLE : View.GONE);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mQuestionsEntity);
            }
        });
    }


    @OnClick({R.id.tv_life_back, R.id.tv_header_shared, R.id.iv_shopcar, R.id.tv_ask})
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
            //加入购物车
            case R.id.iv_shopcar:
                BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                baseAddCarProInfoBean.setProductId(mResultBean.getProductId());
                baseAddCarProInfoBean.setProName(getStrings(mResultBean.getProductName()));
                baseAddCarProInfoBean.setProPic(getStrings(mResultBean.getProductImg()));
                addShopCarGetSku(this, baseAddCarProInfoBean);
                break;
            //提问
            case R.id.tv_ask:
                String question = mEtQuestion.getText().toString().trim();
                if (question.length() < 4) {
                    showToast("请输入至少4个字");
                    return;
                }

                if (userId > 0) {
                    askQuestion(question);
                } else {
                    getLoginStatus(this);
                }
                break;
        }
    }

    private void askQuestion(String question) {
        showLoadhud(this);
        Map<String, String> map = new HashMap<>();
        map.put("productId", mProductId);
        map.put("content", question);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.ASK_QUESTION, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(getActivity());
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (SUCCESS_CODE.equals(requestStatus.getCode())) {
                        showToast("提问成功，一大波回答正在赶来");
                        mEtQuestion.setText("");
                        CommonUtils.hideSoftInput(mEtQuestion.getContext(), mEtQuestion);
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
