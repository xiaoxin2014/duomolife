package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity.CommunalUserInfoBean;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.bean.MineBabyEntity;
import com.amkj.dmsh.release.dialogutils.AlertSettingBean;
import com.amkj.dmsh.release.dialogutils.AlertView;
import com.amkj.dmsh.release.dialogutils.OnAlertItemClickListener;
import com.amkj.dmsh.utils.ImageConverterUtils;
import com.amkj.dmsh.utils.ImgUrlHelp;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;

import org.lasque.tusdk.TuSdkGeeV1;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.TuAvatarComponent;
import org.lasque.tusdk.modules.components.TuSdkComponent;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/8
 * class description:个人信息
 */
public class PersonalDataActivity extends BaseActivity implements OnAlertItemClickListener, View.OnClickListener {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.rImg_personal_header)
    ImageView rImg_personal_header;
    @BindView(R.id.tv_per_data_name)
    TextView tv_per_data_name;
    @BindView(R.id.tv_per_data_sex)
    TextView tv_per_data_sex;
    @BindView(R.id.tv_per_data_birth)
    TextView tv_per_data_birth;
    @BindView(R.id.tv_per_data_baby_info)
    TextView tv_per_data_baby_info;
    @BindView(R.id.tv_per_data_habit)
    TextView tv_per_data_habit;
    @BindView(R.id.fl_personal_info)
    FrameLayout fl_personal_info;
    private final String[] SEX = new String[]{"女神", "男神"};
    private final String[] BABY_SEX = new String[]{"小公主", "小王子"};
    private final String NAME = "name";
    private final int NAME_RES_CODE = 20;
    private String avatarPath;
    private int sexSelector;
    private String nowName;
    private AlertView sexDialog;
    private CommunalUserInfoBean communalUserInfoBean;
    private boolean isOnPause;
    private TimePickerView nowBabyTime;
    private AlertView birthAlert;
    private CommunalUserInfoEntity communalUserInfoEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_per_data;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_titleAll.setText("个人信息");
        header_shared.setVisibility(View.GONE);
    }

    @Override
    protected void loadData() {
        getDataInfo();
    }

    @Override
    protected View getLoadView() {
        return fl_personal_info;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getDataInfo() {
        if (userId > 0) {
            String url = Url.BASE_URL + Url.MINE_PAGE;
            Map<String,Object> params = new HashMap<>();
            params.put("uid",userId);
            NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url
                    , params, new NetLoadUtils.NetLoadListener() {
                        @Override
                        public void onSuccess(String result) {
                            Gson gson = new Gson();
                            communalUserInfoEntity = gson.fromJson(result, CommunalUserInfoEntity.class);
                            if (communalUserInfoEntity != null) {
                                if (communalUserInfoEntity.getCode().equals("01")) {
                                    communalUserInfoBean = communalUserInfoEntity.getCommunalUserInfoBean();
                                    setPersonalData(communalUserInfoBean);
                                } else if (!communalUserInfoEntity.getCode().equals("02")) {
                                    showToast(PersonalDataActivity.this, communalUserInfoEntity.getMsg());
                                }
                            }
                            NetLoadUtils.getQyInstance().showLoadSir(loadService, communalUserInfoEntity);
                        }

                        @Override
                        public void netClose() {
                            NetLoadUtils.getQyInstance().showLoadSir(loadService, communalUserInfoEntity);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            NetLoadUtils.getQyInstance().showLoadSir(loadService, communalUserInfoEntity);
                        }
                    });
        }else{
            NetLoadUtils.getQyInstance().showLoadSirLoadFailed(loadService);
        }
    }

    private void changePersonalData(final String type, final String date) {
        String url = Url.BASE_URL + Url.MINE_CHANGE_DATA;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        switch (type) {
            case "SexSelector":
                params.put("sex", sexSelector);
                break;
            case "headerImg":
                params.put("avatar", avatarPath);
                break;
            case "birthday":
                params.put("birthday", date);
                break;
        }
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                CommunalUserInfoEntity communalUserInfoEntity = gson.fromJson(result, CommunalUserInfoEntity.class);
                if (communalUserInfoEntity != null) {
                    if (communalUserInfoEntity.getCode().equals("01")) {
                        CommunalUserInfoBean communalUserInfoBean = communalUserInfoEntity.getCommunalUserInfoBean();
                        switch (type) {
                            case "SexSelector":
                                tv_per_data_sex.setText(SEX[communalUserInfoEntity.getCommunalUserInfoBean().getSex() == (0) ? 1 : 0]);
                                showToast(PersonalDataActivity.this, "修改完成");
                                break;
                            case "headerImg":
                                GlideImageLoaderUtil.loadHeaderImg(PersonalDataActivity.this, rImg_personal_header, communalUserInfoBean.getAvatar());
                                showToast(PersonalDataActivity.this, "修改完成");
                                break;
                            case "birthday":
                                tv_per_data_birth.setText(getStrings(TextUtils.isEmpty(communalUserInfoBean.getBirthday()) ? communalUserInfoBean.getBirthday() : date));
                                showToast(PersonalDataActivity.this, "修改完成");
                                break;
                        }
                    } else {
                        showToast(PersonalDataActivity.this, communalUserInfoEntity.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void setPersonalData(CommunalUserInfoBean communalUserInfoBean) {
        GlideImageLoaderUtil.loadHeaderImg(PersonalDataActivity.this, rImg_personal_header, ImageConverterUtils.getFormatImg(communalUserInfoBean.getAvatar()));
        tv_per_data_name.setText(getStrings(communalUserInfoBean.getNickname()));
        if (0 <= communalUserInfoBean.getSex() && communalUserInfoBean.getSex() < 2) {
            tv_per_data_sex.setText(communalUserInfoBean.getSex() == 0 ? SEX[1] : SEX[0]);
        } else {
            tv_per_data_sex.setText(R.string.set_sex);
        }
        if (!TextUtils.isEmpty(communalUserInfoBean.getBirthday())) {
            tv_per_data_birth.setText(communalUserInfoBean.getBirthday());
        }
        if (!TextUtils.isEmpty(communalUserInfoBean.getInterests())) {
            String habitName = communalUserInfoBean.getInterests();
            tv_per_data_habit.setText(habitName.length() > 6 ? habitName.substring(0, 6) + "…" : habitName);
        }
        if (communalUserInfoBean.getBabys() != null && communalUserInfoBean.getBabys().size() > 0) {
            MineBabyEntity.BabyBean babyBean = communalUserInfoBean.getBabys().get(0);
            switch (babyBean.getBaby_status()) {
                case 1:
                    tv_per_data_baby_info.setText("备孕中");
                    break;
                case 2:
                    tv_per_data_baby_info.setText("怀孕中");
                    break;
                case 3:
                    tv_per_data_baby_info.setText(((babyBean.getSex() == 0 ? BABY_SEX[1] : BABY_SEX[0])
                            + " " + getStrings(babyBean.getBirthday())));
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        } else if (requestCode == NAME_RES_CODE) {
            String newName = data.getStringExtra(NAME);
            if (nowName.equals(newName)) {
                return;
            } else {
                tv_per_data_name.setText(newName);
                showToast(this, "修改完成");
            }
        }
    }

    //    头像设置
    @OnClick(R.id.ll_per_avatar)
    public void changAvatar(View view) {
        TuAvatarComponent component = TuSdkGeeV1.avatarCommponent(PersonalDataActivity.this, new TuSdkComponent.TuSdkComponentDelegate() {
            @Override
            public void onComponentFinished(TuSdkResult result, Error error, TuFragment lastFragment) {
                final ImgUrlHelp imgUrlHelp = new ImgUrlHelp();
                imgUrlHelp.setSingleImg(PersonalDataActivity.this, result.image);
                if (loadHud != null) {
                    loadHud.show();
                }
                imgUrlHelp.setOnFinishListener(new ImgUrlHelp.OnFinishDataListener() {
                    @Override
                    public void finishData(List<String> data, Handler handler) {
                    }

                    @Override
                    public void finishError(String error) {
                        showToast(PersonalDataActivity.this, "网络异常");
                        if (loadHud != null) {
                            loadHud.dismiss();
                        }
                    }

                    @Override
                    public void finishSingleImg(String singleImg, Handler handler) {
                        if (singleImg != null) {
                            avatarPath = singleImg;
                            changePersonalData("headerImg", null);
                            handler.removeCallbacksAndMessages(null);
                        }
                    }
                });
            }
        });
        String[] filters = {"SkinNature", "SkinPink", "SkinJelly", "SkinNoir", "SkinRuddy", "SkinPowder", "SkinSugar"};
        component.componentOption().cameraOption().setFilterGroup(Arrays.asList(filters));
        // 是否保存到相册
        component.componentOption().editTurnAndCutOption().setSaveToAlbum(false);
        // 是否保存到临时文件
        component.componentOption().editTurnAndCutOption().setSaveToTemp(false);
        component
                // 在组件执行完成后自动关闭组件
                .setAutoDismissWhenCompleted(true)
                // 显示组件
                .showComponent();
    }

    //    设置昵称
    @OnClick(R.id.rel_per_nickname)
    public void skipNameSetting(View view) {
        nowName = tv_per_data_name.getText().toString().trim();
        //获取昵称
        Intent intent = new Intent(PersonalDataActivity.this, SettingPersonalNameActivity.class);
        intent.putExtra(NAME, nowName);
        startActivityForResult(intent, NAME_RES_CODE);
    }

    //    性别选择
    @OnClick(R.id.rel_per_sex)
    public void selSex() {
        AlertSettingBean alertSettingBean = new AlertSettingBean();
        AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
        alertData.setNormalData(SEX);
        alertData.setCancelStr("取消");
        alertData.setFirstDet(true);
        alertData.setTitle("性别");
        alertSettingBean.setStyle(AlertView.Style.ActionSheet);
        alertSettingBean.setAlertData(alertData);
        sexDialog = new AlertView(alertSettingBean, this, this);
        sexDialog.show();
        sexDialog.setCancelable(true);
    }

    //    生日配置
    @OnClick(R.id.rel_per_birthday)
    void selBirth(View view) {
        String birthday = tv_per_data_birth.getText().toString().trim();
        if (communalUserInfoBean != null && TextUtils.isEmpty(birthday)) {
            if (nowBabyTime != null) {
                nowBabyTime.show(tv_per_data_birth);
            } else {
                //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
                //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
                Calendar selectedDate = Calendar.getInstance();
                Calendar startDate = Calendar.getInstance();
                startDate.set(startDate.get(Calendar.YEAR) - 48, 0, 1);
                Calendar endDate = Calendar.getInstance();
                //时间选择器
                //选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                //年月日时分秒 的显示与否，不设置则默认全部显示
                //                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                //选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                //年月日时分秒 的显示与否，不设置则默认全部显示
                nowBabyTime = new TimePickerBuilder(PersonalDataActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(final Date date, View v) {//选中事件回调
                        // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                        if (birthAlert == null) {
                            AlertSettingBean alertSettingBean = new AlertSettingBean();
                            AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
                            alertData.setMsg("生日一经提交不可更改");
                            alertData.setCancelStr("取消");
                            alertData.setDetermineStr("提交");
                            alertSettingBean.setStyle(AlertView.Style.Alert);
                            alertSettingBean.setAlertData(alertData);
                            //                        提交
                            birthAlert = new AlertView(alertSettingBean, PersonalDataActivity.this, new OnAlertItemClickListener() {
                                @Override
                                public void onAlertItemClick(Object o, int position) {
                                    if (position != AlertView.CANCELPOSITION) {
                                        //                        提交
                                        changePersonalData("birthday", formatTime(date));
                                    }
                                }
                            });
                            birthAlert.setCancelable(false);
                        }
                        birthAlert.show();
                    }
                })
                        //年月日时分秒 的显示与否，不设置则默认全部显示
                        .setType(new boolean[]{true, true, true, false, false, false})
                        .setLabel("年", "月", "日", "", "", "")
                        .isCenterLabel(false)
                        .setDividerColor(Color.parseColor("#666666"))
                        .setContentTextSize(24)
                        .setBackgroundId(0x00FFFFFF)
                        .setDate(selectedDate)
                        .setRangDate(startDate, endDate)
                        .setOutSideCancelable(false)
                        .setLayoutRes(R.layout.mine_baby_date_custom, new CustomListener() {
                            @Override
                            public void customLayout(View view) {
                                TextView tv_time_cancel = view.findViewById(R.id.tv_time_cancel);
                                TextView tv_time_confirm = view.findViewById(R.id.tv_time_confirm);
                                tv_time_cancel.setOnClickListener(PersonalDataActivity.this);
                                tv_time_confirm.setOnClickListener(PersonalDataActivity.this);
                            }
                        })
                        .build();
                nowBabyTime.show(tv_per_data_birth);
            }
        } else {
            showToast(this, R.string.birthday_set);
        }
    }

    private String formatTime(Date date) {
        try {
            SimpleDateFormat timeReceiveFormat = new SimpleDateFormat("yyyy-MM-dd");
            return timeReceiveFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //  设置爱好
    @OnClick(R.id.rel_per_habit)
    void setHabit(View view) {
        Intent intent = new Intent(PersonalDataActivity.this, PersonalHabitActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rel_per_bg_cover)
    public void setBgCover(View view) {
        Intent intent = new Intent(PersonalDataActivity.this, PersonalBgImgActivity.class);
        startActivity(intent);
    }

    //    宝宝信息
    @OnClick(R.id.rel_per_baby_info)
    void setBabyInfo(View view) {
        if (communalUserInfoBean != null) {
            Intent intent = new Intent(PersonalDataActivity.this, MineBabyInfoPickerActivity.class);
            Bundle bundle = new Bundle();
            List<MineBabyEntity.BabyBean> babys = communalUserInfoBean.getBabys();
            bundle.putParcelable("babyBean", babys.size() > 0 ? babys.get(0) : null);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onAlertItemClick(Object o, int position) {
        if (o == sexDialog && position != AlertView.CANCELPOSITION) {
            sexSelector = (position == 0 ? 1 : 0);
            changePersonalData("SexSelector", null);
        }
    }

    //    返回
    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOnPause) {
            getDataInfo();
        }
        isOnPause = false;
    }

    @Override
    public void onClick(View v) {
        nowBabyTime.dismiss();
        if (v.getId() == R.id.tv_time_confirm) {
            nowBabyTime.returnData();
        }
    }
}
