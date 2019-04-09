package com.amkj.dmsh.dominant.activity;

import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.Url.BASE_SHARE_PAGE_TWO;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/3/1
 * version 3.3.0
 * class description:整点秒杀
 */
public class WholePointSpikeProductActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    CommunalADActivityBean communalADActivityBean = null;

    @Override
    protected int getContentView() {
        return R.layout.activity_point_spike_product;
    }

    @Override
    protected void initViews() {
        tv_header_shared.setEnabled(false);
        tvHeaderTitle.setText("整点秒");
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.tv_life_back, R.id.tv_header_shared})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            case R.id.tv_header_shared:
                if (communalADActivityBean != null) {
                    String title = "限时限量秒杀";
                    String description = "每天都有惊喜价，买到就是省钱，手慢无!";
                    new UMShareAction(getActivity()
                            , communalADActivityBean.getPicUrl()
                            , title
                            , description
                            , BASE_SHARE_PAGE_TWO + "m/template/common/hours_activity.html?nav=1", 1);
                }
                break;
        }
    }

    public void setUrl(CommunalADActivityBean communalADActivityBean) {
        this.communalADActivityBean = communalADActivityBean;
    }
}
