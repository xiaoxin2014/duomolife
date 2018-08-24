package com.amkj.dmsh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.homepage.activity.DoMoLifeCommunalActivity;
import com.amkj.dmsh.homepage.activity.DoMoLifeLotteryActivity;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.microquation.linkedme.android.LinkedME;
import com.microquation.linkedme.android.util.LinkProperties;

import java.util.HashMap;

import static com.amkj.dmsh.homepage.activity.DoMoLifeLotteryActivity.LOTTERY_URL;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/2/10
 * class description:请输入类描述
 */

public class MiddleActivity extends AppCompatActivity {
    private int uid;
    private int REQLOGINCODE = 0;
    private final int LOTTERY_PAGER = 101;
    private boolean isOnPause;
    // ...

    /**
     * 解析深度链获取跳转参数，开发者自己实现参数相对应的页面内容
     * <p>
     * <p>
     * 通过LinkProperties对象调用getControlParams方法获取自定义参数的HashMap对象,
     * 通过创建的自定义key获取相应的值,用于数据处理。
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            //获取与深度链接相关的值
            LinkProperties linkProperties = getIntent().getParcelableExtra(LinkedME.LM_LINKPROPERTIES);
            if (linkProperties != null) {
                //获取自定义参数封装成的hashmap对象,参数键值对由集成方定义
                HashMap<String, String> hashMap = linkProperties.getControlParams();
                //根据key获取传入的参数的值,该key关键字View可为任意值,由集成方规定,请与web端商议,一致即可
                String isSkipLogin = hashMap.get("isSkipLogin");
                String skipPage = hashMap.get("skipPage");
                if (isSkipLogin != null && !TextUtils.isEmpty(skipPage)) {
                    //根据不同的参数进行页面跳转,detail代表具体跳转到哪个页面,此处语义指详情页
                    if (!TextUtils.isEmpty(skipPage)) {
                        if (isSkipLogin.equals("true")) {
                            getLoginStatus(skipPage);
                        } else {
                            ConstantMethod.setSkipPath(MiddleActivity.this, skipPage, true);
                        }
                    } else {
                        skipMainActivity();
                    }
                } else {
                    skipMainActivity();
                }
            }
        }
    }

    private void skipMainActivity() {
        Intent intent = new Intent(MiddleActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    private void getLoginStatus(String skipPage) {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
            if (skipPage.equals("lottery")) {
                setSkipPager();
            } else {
                skipMainActivity();
            }
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(MiddleActivity.this, MineLoginActivity.class);
            if (skipPage.equals("lottery")) {
                REQLOGINCODE = LOTTERY_PAGER;
            }
            startActivityForResult(intent, REQLOGINCODE);
        }
    }

    private void setSkipPager() {
        Intent intent = new Intent(MiddleActivity.this, DoMoLifeLotteryActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnPause = true;
    }

    @Override
    protected void onResume() {
        if (isOnPause) {
            skipMainActivity();
        }
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOTTERY_PAGER:
                CommunalUserInfoEntity accountInf = (CommunalUserInfoEntity) data.getExtras().get("AccountInf");
                uid = accountInf.getCommunalUserInfoBean().getUid();
                Intent intent = new Intent(MiddleActivity.this, DoMoLifeCommunalActivity.class);
                intent.putExtra("loadUrl", LOTTERY_URL + "?uid=" + uid);
                startActivity(intent);
                finish();
                break;
            default:
                skipMainActivity();
                break;
        }
    }
}
