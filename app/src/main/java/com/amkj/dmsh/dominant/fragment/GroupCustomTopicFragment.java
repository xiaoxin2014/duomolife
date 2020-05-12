package com.amkj.dmsh.dominant.fragment;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.TabNameBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dominant.adapter.CatergoryGoodsAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_CUSTOM_NAME;
import static com.amkj.dmsh.constant.Url.Q_CUSTOM_PRO_LIST;

/**
 * Created by xiaoxin on 2019/12/3
 * Version:v4.4.0
 * ClassDescription :拼团详情自定义专区
 */
public class GroupCustomTopicFragment extends BaseFragment {
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    private int page = 1;
    private BaseQuickAdapter qualityCustomTopicAdapter;
    private List<LikedProductBean> customProList = new ArrayList<>();
    private UserLikedProductEntity userLikedProductEntity;
    private String productType;
    private String position;
    private String simpleName;


    @Override
    protected int getContentView() {
        return R.layout.fragment_group_custom_topic;
    }

    @Override
    protected void initViews() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        qualityCustomTopicAdapter = new CatergoryGoodsAdapter(getActivity(), customProList);
        communal_recycler.setLayoutManager(gridLayoutManager);
        communal_recycler.setAdapter(qualityCustomTopicAdapter);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_gray_f)
                .create());
        qualityCustomTopicAdapter.setOnLoadMoreListener(() -> {
            page++;
            getQualityCustomPro();
        }, communal_recycler);
    }

    @Override
    protected void loadData() {
        page = 1;
        getQualityCustomPro();
    }


    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getQualityCustomPro() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("productType", productType);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Q_CUSTOM_PRO_LIST
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        qualityCustomTopicAdapter.loadMoreComplete();
                        if (page == 1) {
                            customProList.clear();
                        }

                        userLikedProductEntity = GsonUtils.fromJson(result, UserLikedProductEntity.class);
                        if (userLikedProductEntity != null) {
                            if (userLikedProductEntity.getCode().equals(SUCCESS_CODE)) {
                                customProList.addAll(userLikedProductEntity.getGoodsList());
                            } else if (userLikedProductEntity.getCode().equals(EMPTY_CODE)) {
                                qualityCustomTopicAdapter.loadMoreEnd();
                            } else {
                                showToast( userLikedProductEntity.getMsg());
                            }

                            if (!TextUtils.isEmpty(userLikedProductEntity.getZoneName())) {
                                EventBus.getDefault().post(new EventMessage(UPDATE_CUSTOM_NAME, new TabNameBean(userLikedProductEntity.getZoneName(), ConstantMethod.getStringChangeIntegers(position), simpleName)));
                            }
                        }
                        qualityCustomTopicAdapter.notifyDataSetChanged();

                        NetLoadUtils.getNetInstance().showLoadSir(loadService, customProList, userLikedProductEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        qualityCustomTopicAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, customProList, userLikedProductEntity);
                    }
                });
    }


    @Override
    protected void getReqParams(Bundle bundle) {
        productType = bundle.getString("productType");
        position = bundle.getString("position");
        simpleName = bundle.getString("simpleName");
    }
}
