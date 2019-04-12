package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.EditorEntity;
import com.amkj.dmsh.bean.EditorEntity.EditorBean;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.homepage.adapter.EditorSelectAdapter;
import com.amkj.dmsh.homepage.view.EditorHeadView;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.google.gson.Gson;
import com.luck.picture.lib.decoration.RecycleViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.Url.EDITOR_SELECT_FAVOR;

/**
 * Created by xiaoxin on 2019/3/14 0014
 * Version：V3.3.0
 * class description:小编精选
 */
public class EditorSelectActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.iv_img_service)
    ImageView mIvImgService;
    @BindView(R.id.rv_editor)
    RecyclerView mRvEditor;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout mSmartLayout;
    private EditorSelectAdapter mEditorAdapter;
    List<EditorBean> EditorList = new ArrayList<>();
    private int page = 1;
    private EditorEntity mEditorEntity;
    private EditorHeadView mEditorHeadView;

    @Override
    protected int getContentView() {
        return R.layout.activity_editor_select;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("小编精选");
        mIvImgService.setVisibility(View.GONE);
        initRv();
    }

    private void initRv() {
        mEditorAdapter = new EditorSelectAdapter(this, R.layout.item_editor_introduce, EditorList);
        mEditorAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            EditorBean itemBean = (EditorBean) view.getTag();
            Intent intent;
            if (itemBean == null) return;
            switch (view.getId()) {
                //文章点赞
                case R.id.tv_com_art_like_count:
                    if (userId > 0) {
                        itemBean.setIsFavor(!itemBean.getIsFavor());
                        itemBean.setLikeNum(itemBean.getIsFavor() ? itemBean.getLikeNum() + 1 :
                                itemBean.getLikeNum() - 1);
                        TextView textView = (TextView) view;
                        textView.setSelected(!textView.isSelected());
                        textView.setText(itemBean.getLikeString());
                        setGoodsLiked(itemBean);
                    } else {
                        getLoginStatus(this);
                    }
                    break;
                //文章评论
                case R.id.tv_com_art_comment_count:
                    intent = new Intent(this, EditorCommentActivity.class);
                    intent.putExtra("redactorpickedId", String.valueOf(itemBean.getId()));
                    startActivity(intent);
                    break;
            }

        });
        mSmartLayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            loadData();
        });
        mEditorAdapter.setOnLoadMoreListener(() -> {
            page++;
            loadData();
        }, mRvEditor);
        mRvEditor.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvEditor.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, AutoSizeUtils.mm2px(this, 1), getResources().getColor(R.color.text_color_e_s)));
        mEditorHeadView = new EditorHeadView(EditorSelectActivity.this);
        mEditorAdapter.addHeaderView(mEditorHeadView);
        mRvEditor.setAdapter(mEditorAdapter);
    }

    @Override
    protected void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("currentPage", page);
        map.put("showCount", TOTAL_COUNT_TEN);
        if (userId > 0) {
            map.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.EDITOR_SELECT_LIST, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartLayout.finishRefresh();
                Gson gson = new Gson();
                mEditorEntity = gson.fromJson(result, EditorEntity.class);
                if (mEditorEntity != null) {
                    List<EditorBean> resultList = mEditorEntity.getResult();
                    String code = mEditorEntity.getCode();
                    if (resultList == null || resultList.size() < 1 || EMPTY_CODE.equals(code)) {
                        mEditorAdapter.loadMoreEnd();
                    } else if (SUCCESS_CODE.equals(code)) {
                        if (page == 1) {
                            EditorList.clear();
                        }
                        EditorList.addAll(mEditorEntity.getResult());
                        mEditorAdapter.notifyDataSetChanged();
                        mEditorHeadView.updateData(mEditorEntity);
                        mEditorAdapter.loadMoreComplete();
                    } else {
                        showToast(EditorSelectActivity.this, mEditorEntity.getMsg());
                        mEditorAdapter.loadMoreFail();
                    }
                }

                NetLoadUtils.getNetInstance().showLoadSir(loadService, EditorList, mEditorEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartLayout.finishRefresh();
                mEditorAdapter.loadMoreFail();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, EditorList, mEditorEntity);
            }
        });
    }

    //给文章点赞
    private void setGoodsLiked(EditorBean editorBean) {
        Map<String, Object> params = new HashMap<>();
        //文章id
        params.put("redactorpickedId", editorBean.getId());
        params.put("operation", editorBean.getFavorStatus());
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, EDITOR_SELECT_FAVOR, params, null);
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected View getLoadView() {
        return mSmartLayout;
    }

    @OnClick({R.id.tv_life_back, R.id.iv_img_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            //分享
            case R.id.iv_img_share:
                if (mEditorEntity != null && EditorList != null && EditorList.size() > 0) {
                    EditorBean editorBean = EditorList.get(0);
                    if (editorBean != null) {
                        new UMShareAction(this
                                , editorBean.getCoverImg()
                                , getStrings(mEditorEntity.getTitle())
                                , getStrings(mEditorEntity.getDescription())
                                , Url.BASE_SHARE_PAGE_TWO + ("m/template/find_template/handpick-article.html"), 1);
                    }
                }
                break;
        }
    }

    //友盟分享注册回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
