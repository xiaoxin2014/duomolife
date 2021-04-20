package com.amkj.dmsh.views.alertdialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dao.SoftApiDao;
import com.amkj.dmsh.dominant.bean.PostCommentEntity;
import com.amkj.dmsh.dominant.bean.PostCommentEntity.PostCommentBean;
import com.amkj.dmsh.find.bean.PostEntity;
import com.amkj.dmsh.netloadpage.NetLoadCallback;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.adapter.PostCommentAdapter;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantVariable.COMMENT_VIDEO_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_COMMENT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_POST_COMMENT;

/**
 * Created by xiaoxin on 2020/10/9
 * Version:v4.8.0
 * ClassDescription :淘好货-种草帖子-全部评论
 */
public class TimePostCommentPw extends BottomPopupView {

    private BaseActivity mContext;
    private List<PostCommentBean> mDatas = new ArrayList<>();
    private int page = 1;
    private PostCommentAdapter mPostCommentAdapter;
    private TextView mTvNum;
    private LoadService mLoadService;
    private PostCommentEntity mPostCommentEntity;
    private TextView mTvLike;
    private PostEntity.PostBean mPostBean;
    private TimePostEditextPw mEditPopupView;
    private String mPostType;


    /**
     * @param postType 内容类型
     */
    public TimePostCommentPw(@NonNull BaseActivity context, PostEntity.PostBean postBean, String postType) {
        super(context);
        mContext = context;
        mPostBean = postBean;
        mPostType = postType;
    }

    public TimePostCommentPw(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout.pw_time_post_all_comment;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        mTvNum = findViewById(R.id.tv_all_num);
        findViewById(R.id.tv_article_bottom_collect).setVisibility(GONE);
        mTvLike = findViewById(R.id.tv_article_bottom_like);
        mTvLike.setVisibility(COMMENT_VIDEO_TYPE.equals(mPostType) ? GONE : VISIBLE);
        mTvLike.setText(mPostBean.getFavorNum() > 0 ? String.valueOf(mPostBean.getFavorNum()) : "赞");
        mTvLike.setSelected(mPostBean.isFavor());
        mTvLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftApiDao.favorPost(mContext, mPostBean, mTvLike, mPostType);
            }
        });
        findViewById(R.id.tv_publish_comment).setOnClickListener(v -> {
            //回复帖子
            showEditPop(null);
        });
        //初始化评论列表
        RecyclerView rvComment = findViewById(R.id.rv_comment);
        rvComment.setLayoutManager(new LinearLayoutManager(mContext));
        mPostCommentAdapter = new PostCommentAdapter(mContext, mDatas, mPostType);
        rvComment.setAdapter(mPostCommentAdapter);
        mPostCommentAdapter.setOnLoadMoreListener(() -> {
            page++;
            getComment();
        }, rvComment);
        mPostCommentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //回复评论
                PostCommentBean postCommentBean = (PostCommentBean) view.getTag();
                if (postCommentBean != null) {
                    showEditPop(postCommentBean);
                }
            }
        });
        mLoadService = LoadSir.getDefault().register(rvComment, (Callback.OnReloadListener) v -> {
            // 重新加载逻辑
            mLoadService.showCallback(NetLoadCallback.class);
            getComment();
        }, NetLoadUtils.getNetInstance().getLoadSirCover());
        getComment();
    }

    private void showEditPop(PostCommentBean postCommentBean) {
        mEditPopupView = (TimePostEditextPw) new XPopup.Builder(getContext())
                .autoOpenSoftInput(true)
                .autoDismiss(true)
                .asCustom(new TimePostEditextPw(mContext, mPostBean.getId(), mPostBean.getAuthorUid(), postCommentBean, mPostType));
        mEditPopupView.show();
    }


    //获取评论
    private void getComment() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", mPostBean.getId());
        map.put("currentPage", page);
        map.put("showCount", 20);
        map.put("replyCurrentPage", 1);
        map.put("replyShowCount", DEFAULT_COMMENT_TOTAL_COUNT);
        map.put("comtype", mPostType);
        NetLoadUtils.getNetInstance().loadNetDataPost(mContext, Url.Q_DML_SEARCH_COMMENT, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (page == 1) {
                    mDatas.clear();
                }
                mPostCommentEntity = GsonUtils.fromJson(result, PostCommentEntity.class);
                if (mPostCommentEntity != null) {
                    List<PostCommentBean> commentList = mPostCommentEntity.getCommentList();
                    int commentSize = mPostCommentEntity.getCommentSize();
                    if (commentList != null && commentList.size() > 0) {
                        mTvNum.setText(getIntegralFormat(mContext, R.string.all_comment_num, commentSize));
                        mDatas.addAll(commentList);
                        mPostCommentAdapter.loadMoreComplete();
                    } else if (ERROR_CODE.equals(mPostCommentEntity.getCode())) {
                        ConstantMethod.showToast(mPostCommentEntity.getMsg());
                        mPostCommentAdapter.loadMoreFail();
                    } else {
                        mPostCommentAdapter.loadMoreEnd();
                    }
                } else {
                    mPostCommentAdapter.loadMoreEnd();
                }

                mPostCommentAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(mLoadService, mDatas, mPostCommentEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(mLoadService, mDatas, mPostCommentEntity);
            }
        });
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext()) * .9f);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetResult(EventMessage message) {
        if (message == null) {
            return;
        }

        //子评论回复
        if ("replyChildComment".equals(message.type)) {
            PostCommentBean.ReplyCommListBean replyCommListBean = (PostCommentBean.ReplyCommListBean) message.result;
            PostCommentBean postCommentBean = new PostCommentBean();
            postCommentBean.setNickname(replyCommListBean.getNickname());
            postCommentBean.setUid(replyCommListBean.getUid());
            postCommentBean.setId(replyCommListBean.getId());
            postCommentBean.setMain_comment_id(replyCommListBean.getMain_comment_id());
            postCommentBean.setObj_id(replyCommListBean.getObj_id());
            showEditPop(postCommentBean);
        } else if (UPDATE_POST_COMMENT.equals(message.type) && mContext.getSimpleName().equals(message.result)) {
            page = 1;
            getComment();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
