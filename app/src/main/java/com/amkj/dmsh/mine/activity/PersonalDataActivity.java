package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity.CommunalUserInfoBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.mine.bean.MineBabyEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.ImageConverterUtils;
import com.amkj.dmsh.utils.ImgUrlHelp;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.pictureselector.PictureSelectorUtils;
import com.amkj.dmsh.views.alertdialog.AlertDialogBottomListHelper;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfigC;
import com.luck.picture.lib.entity.LocalMediaC;
import com.yanzhenjie.permission.Permission;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.emoji.widget.EmojiTextView;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.MINE_CHANGE_DATA;
import static com.amkj.dmsh.constant.Url.MINE_PAGE;



/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/8
 * class description:????????????
 */
public class PersonalDataActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.rImg_personal_header)
    ImageView rImg_personal_header;
    @BindView(R.id.tv_per_data_name)
    EmojiTextView tv_per_data_name;
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
    private final String[] SEX = new String[]{"??????", "??????"};
    private final String[] BABY_SEX = new String[]{"?????????", "?????????"};
    private final String NAME = "name";
    private final int NAME_RES_CODE = 20;
    private String avatarPath;
    private int sexSelector;
    private String nowName;
    private CommunalUserInfoBean communalUserInfoBean;
    private boolean isOnPause;
    private TimePickerView nowBabyTime;
    private CommunalUserInfoEntity communalUserInfoEntity;
    private AlertDialogHelper commitBirthdayDialogHelper;
    private AlertDialogBottomListHelper sexDialogBottomListHelper;
    private AlertDialogBottomListHelper alertDialogBottomListHelper;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_per_data;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_titleAll.setText("????????????");
        header_shared.setVisibility(View.GONE);
    }

    @Override
    protected void loadData() {
        getDataInfo();
    }

    @Override
    public View getLoadView() {
        return fl_personal_info;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getDataInfo() {
        if (userId < 1) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_PAGE
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {

                        communalUserInfoEntity = GsonUtils.fromJson(result, CommunalUserInfoEntity.class);
                        if (communalUserInfoEntity != null) {
                            if (communalUserInfoEntity.getCode().equals(SUCCESS_CODE)) {
                                communalUserInfoBean = communalUserInfoEntity.getCommunalUserInfoBean();
                                setPersonalData(communalUserInfoBean);
                            } else if (!communalUserInfoEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(communalUserInfoEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, communalUserInfoEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, communalUserInfoEntity);
                    }
                });

    }

    private void changePersonalData(final String type, final String date) {
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
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_CHANGE_DATA, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }

                CommunalUserInfoEntity communalUserInfoEntity = GsonUtils.fromJson(result, CommunalUserInfoEntity.class);
                if (communalUserInfoEntity != null) {
                    if (communalUserInfoEntity.getCode().equals(SUCCESS_CODE)) {
                        CommunalUserInfoBean communalUserInfoBean = communalUserInfoEntity.getCommunalUserInfoBean();
                        switch (type) {
                            case "SexSelector":
                                tv_per_data_sex.setText(SEX[communalUserInfoEntity.getCommunalUserInfoBean().getSex() == (0) ? 1 : 0]);
                                showToast("????????????");
                                break;
                            case "headerImg":
                                GlideImageLoaderUtil.loadHeaderImg(PersonalDataActivity.this, rImg_personal_header, communalUserInfoBean.getAvatar());
                                showToast("????????????");
                                break;
                            case "birthday":
                                tv_per_data_birth.setText(getStrings(TextUtils.isEmpty(communalUserInfoBean.getBirthday()) ? communalUserInfoBean.getBirthday() : date));
                                showToast("????????????");
                                break;
                        }
                    } else {
                        showToast(communalUserInfoEntity.getMsg());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
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
            tv_per_data_habit.setText(habitName.length() > 6 ? habitName.substring(0, 6) + "???" : habitName);
        }
        if (communalUserInfoBean.getBabys() != null && communalUserInfoBean.getBabys().size() > 0) {
            MineBabyEntity.BabyBean babyBean = communalUserInfoBean.getBabys().get(0);
            switch (babyBean.getBaby_status()) {
                case 1:
                    tv_per_data_baby_info.setText("?????????");
                    break;
                case 2:
                    tv_per_data_baby_info.setText("?????????");
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
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            }
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
                showToast("????????????");
            }
        } else if (requestCode == PictureConfigC.CHOOSE_REQUEST) {
            try {
                List<LocalMediaC> localMediaList = PictureSelector.obtainMultipleResult(data);
                if (localMediaList != null && localMediaList.size() > 0) {
                    LocalMediaC localMedia = localMediaList.get(0);
                    if (localMedia != null && !TextUtils.isEmpty(localMedia.getPath()) && localMedia.isCut()) {
                        String cutPath = localMedia.getCutPath();
                        if (!TextUtils.isEmpty(cutPath)) {
                            final ImgUrlHelp imgUrlHelp = new ImgUrlHelp();
                            imgUrlHelp.setSingleImg(PersonalDataActivity.this, BitmapFactory.decodeFile(cutPath));
                            if (loadHud != null) {
                                loadHud.show();
                            }
                            imgUrlHelp.setOnFinishListener(new ImgUrlHelp.OnFinishDataListener() {
                                @Override
                                public void finishData(List<String> data, Handler handler) {
                                }

                                @Override
                                public void finishError(String error) {
                                    showToast("????????????");
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
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //    ????????????
    @OnClick(R.id.ll_per_avatar)
    public void changAvatar(View view) {
        if (alertDialogBottomListHelper == null) {
            alertDialogBottomListHelper = new AlertDialogBottomListHelper(this);
            alertDialogBottomListHelper.setTitleVisibility(GONE).setMsg("????????????")
                    .setItemData(new String[]{"???????????????", "????????????"}).itemNotifyDataChange().setAlertListener(new AlertDialogBottomListHelper.AlertItemClickListener() {
                @Override
                public void itemClick(@Nullable String text, int itemPosition) {
                    openImageGallery(itemPosition);
                }
            });
        }
        alertDialogBottomListHelper.show();
    }

    /**
     * ??????????????????
     */
    private void openImageGallery(int itemPosition) {
        ConstantMethod constantMethod = new ConstantMethod();
        constantMethod.setOnGetPermissionsSuccess(new ConstantMethod.OnGetPermissionsSuccessListener() {
            @Override
            public void getPermissionsSuccess() {
                if (itemPosition == 0) {
//                ??????????????? ????????????
                    PictureSelectorUtils.getInstance()
                            .resetVariable()
                            .isCrop(true)
                            .imageMode(PictureConfigC.SINGLE)
                            .withAspectRatio(AutoSizeUtils.mm2px(mAppContext, 750), AutoSizeUtils.mm2px(mAppContext, 750))
                            .openGallery(getActivity());
                } else if (itemPosition == 1) {
                    PictureSelectorUtils.getInstance()
                            .resetVariable()
                            .isCrop(true)
                            .withAspectRatio(AutoSizeUtils.mm2px(mAppContext, 750), AutoSizeUtils.mm2px(mAppContext, 750))
                            .openCamera(getActivity());
                }
            }
        });
        constantMethod.getPermissions(getActivity(), Permission.Group.STORAGE);
    }


    //    ????????????
    @OnClick(R.id.rel_per_nickname)
    public void skipNameSetting(View view) {
        nowName = tv_per_data_name.getText().toString().trim();
        //????????????
        Intent intent = new Intent(PersonalDataActivity.this, SettingPersonalNameActivity.class);
        intent.putExtra(NAME, nowName);
        startActivityForResult(intent, NAME_RES_CODE);
    }

    //    ????????????
    @OnClick(R.id.rel_per_sex)
    public void selSex() {
        if (sexDialogBottomListHelper == null) {
            sexDialogBottomListHelper = new AlertDialogBottomListHelper(this);
            sexDialogBottomListHelper.setTitleVisibility(GONE).setMsg("??????")
                    .setItemData(SEX).itemNotifyDataChange().setAlertListener(new AlertDialogBottomListHelper.AlertItemClickListener() {
                @Override
                public void itemClick(@Nullable String text, int itemPosition) {
                    sexSelector = (itemPosition == 0 ? 1 : 0);
                    changePersonalData("SexSelector", null);
                }
            });
        }
        sexDialogBottomListHelper.show();
    }

    //    ????????????
    @OnClick(R.id.rel_per_birthday)
    void selBirth(View view) {
        String birthday = tv_per_data_birth.getText().toString().trim();
        if (communalUserInfoBean != null && TextUtils.isEmpty(birthday)) {
            if (nowBabyTime != null) {
                nowBabyTime.show(tv_per_data_birth);
            } else {
                //??????????????????(?????????????????????????????????????????????1900-2100???????????????????????????)
                //????????????Calendar???????????????0-11???,?????????????????????Calendar???set?????????????????????,???????????????????????????0-11
                Calendar currentTime = Calendar.getInstance();
                Calendar startDate = Calendar.getInstance();
                startDate.set(1900, 0, 1);
                //???????????????
                //??????????????????
                // ?????????????????????v,??????show()???????????????????????? View ???????????????show??????????????????????????????v??????null
                //?????????????????? ????????????????????????????????????????????????
                //                .setBackgroundId(0x00FFFFFF) //????????????????????????
                //??????????????????
                // ?????????????????????v,??????show()???????????????????????? View ???????????????show??????????????????????????????v??????null
                //?????????????????? ????????????????????????????????????????????????
                nowBabyTime = new TimePickerBuilder(PersonalDataActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(final Date date, View v) {//??????????????????
                        // ?????????????????????v,??????show()???????????????????????? View ???????????????show??????????????????????????????v??????null
                        if (commitBirthdayDialogHelper == null) {
                            commitBirthdayDialogHelper = new AlertDialogHelper(PersonalDataActivity.this);
                            commitBirthdayDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                                    .setMsg("??????????????????????????????").setCancelText("??????").setConfirmText("??????").setCancelable(false)
                                    .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
                            commitBirthdayDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                @Override
                                public void confirm() {
                                    //                        ??????
                                    changePersonalData("birthday", formatTime(date));
                                }

                                @Override
                                public void cancel() {
                                }
                            });
                        }
                        commitBirthdayDialogHelper.show();
                    }
                })
                        //?????????????????? ????????????????????????????????????????????????
                        .setType(new boolean[]{true, true, true, false, false, false})
                        .setLabel("???", "???", "???", "", "", "")
                        .isCenterLabel(false)
                        .setDividerColor(Color.parseColor("#666666"))
                        .setContentTextSize(24)
                        .setBackgroundId(0x00FFFFFF)
                        .setDate(currentTime)
                        .setRangDate(startDate, currentTime)
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
            showToast(R.string.birthday_set);
        }
    }

    private String formatTime(Date date) {
        try {
            SimpleDateFormat timeReceiveFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            return timeReceiveFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //  ????????????
    @OnClick(R.id.rel_per_habit)
    void setHabit(View view) {
        Intent intent = new Intent(PersonalDataActivity.this, PersonalHabitActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rel_per_bg_cover)
    public void setBgCover(View view) {
        if (userId > 0 && communalUserInfoBean != null) {
            Intent intent = new Intent(PersonalDataActivity.this, PersonalBgImgActivity.class);
            intent.putExtra("imgUrl", getStrings(communalUserInfoBean.getBgimg_url()));
            startActivity(intent);
        }
    }

    //    ????????????
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

    //    ??????
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
