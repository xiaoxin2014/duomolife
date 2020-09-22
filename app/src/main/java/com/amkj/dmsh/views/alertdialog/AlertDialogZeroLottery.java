package com.amkj.dmsh.views.alertdialog;

import android.content.Context;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseAlertDialog;
import com.amkj.dmsh.mine.adapter.ZeroLotteryAdapter;
import com.amkj.dmsh.mine.bean.ZeroLotteryEntity.ZeroLotteryBean;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Created by xiaoxin on 2020/8/14
 * Version:v4.7.0
 * ClassDescription :0元中奖名单弹窗
 */
public class AlertDialogZeroLottery extends BaseAlertDialog {
    @BindView(R.id.rv_lottery)
    RecyclerView mRvLottery;
    private final ZeroLotteryAdapter mZeroLotteryAdapter;
    private List<ZeroLotteryBean> datas = new ArrayList<>();

    public AlertDialogZeroLottery(Context context) {
        super(context);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        mRvLottery.setLayoutManager(gridLayoutManager);
        mZeroLotteryAdapter = new ZeroLotteryAdapter(context, datas, true);
        mRvLottery.setAdapter(mZeroLotteryAdapter);
    }

    public void update(List<ZeroLotteryBean> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        mZeroLotteryAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.alert_zero_lottery;
    }
}
