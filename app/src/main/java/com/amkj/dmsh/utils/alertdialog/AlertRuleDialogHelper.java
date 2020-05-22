package com.amkj.dmsh.utils.alertdialog;

import android.app.Activity;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/16
 * version 3.1.5
 * class description:规则dialog
 */
public class AlertRuleDialogHelper extends BaseAlertDialogHelper {

    @BindView(R.id.tv_integral_rule_title)
    TextView tv_integral_rule_title;
    @BindView(R.id.rv_communal_rule)
    RecyclerView rv_communal_rule;
    private CommunalDetailAdapter integralRuleAdapter;
    private List<CommunalDetailObjectBean> ruleList = new ArrayList<>();

    public AlertRuleDialogHelper(Activity context) {
        super(context);
        getAlertDialog().setCancelable(false);
        rv_communal_rule.setLayoutManager(new LinearLayoutManager(context));
        integralRuleAdapter = new CommunalDetailAdapter(context, ruleList);
        rv_communal_rule.setAdapter(integralRuleAdapter);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.attendance_integral_rule;
    }

    @Override
    protected int getLayoutWith() {
        return AutoSizeUtils.mm2px(mAppContext, 540);
    }

    public void setRuleData(String title, List<CommunalDetailObjectBean> ruleDataList) {
        tv_integral_rule_title.setText(getStrings(title));
        if (ruleDataList != null && ruleDataList.size() > 0) {
            ruleList.clear();
            ruleList.addAll(ruleDataList);
            integralRuleAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.tv_integral_rule_dismiss)
    public void onViewClicked() {
        dismiss();
    }
}
