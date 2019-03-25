package com.amkj.dmsh.utils.alertdialog;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/16
 * version 3.1.5
 * class description:规则dialog
 */
public class AlertRuleDialogHelper implements View.OnClickListener {

    private final Context context;
    private AlertDialog ruleAlertView;
    private View dialogView;
    private CommunalDetailAdapter integralRuleAdapter;
    private RecyclerView rv_communal_rule;
    private TextView tv_integral_rule_title;
    private List<CommunalDetailObjectBean> ruleList = new ArrayList<>();
    private boolean isFirstSet = true;

    public AlertRuleDialogHelper (Activity context) {
        this.context = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialogView = LayoutInflater.from(context).inflate(R.layout.attendance_integral_rule, null, false);
        tv_integral_rule_title = dialogView.findViewById(R.id.tv_integral_rule_title);
        TextView tv_integral_rule_dismiss = dialogView.findViewById(R.id.tv_integral_rule_dismiss);
        rv_communal_rule = dialogView.findViewById(R.id.rv_communal_rule);
        tv_integral_rule_dismiss.setOnClickListener(this);
        rv_communal_rule.setLayoutManager(new LinearLayoutManager(context));
        integralRuleAdapter = new CommunalDetailAdapter(context, ruleList);
        rv_communal_rule.setAdapter(integralRuleAdapter);
        builder.setCancelable(false);
        ruleAlertView = builder.create();
    }

    public void setRuleData(String title, List<CommunalDetailObjectBean> ruleDataList){
        tv_integral_rule_title.setText(getStrings(title));
        if(ruleDataList!=null&&ruleDataList.size()>0){
            ruleList.clear();
            ruleList.addAll(ruleDataList);
            integralRuleAdapter.notifyDataSetChanged();
        }
    }

    public void show(){
        if (!ruleAlertView.isShowing()
                && isContextExisted(context)) {
            AutoSize.autoConvertDensityOfGlobal((Activity) context);
            ruleAlertView.show();
        }
        if(isFirstSet){
            Window window = ruleAlertView.getWindow();
            if(window!=null){
                window.setBackgroundDrawableResource(android.R.color.transparent);
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = AutoSizeUtils.mm2px(mAppContext,500);
                window.setAttributes(params);
                window.setContentView(dialogView);
            }
        }
        isFirstSet = false;
    }

    @Override
    public void onClick(View v) {
        if(ruleAlertView!=null&& isContextExisted(context)){
            ruleAlertView.dismiss();
        }
    }
}
