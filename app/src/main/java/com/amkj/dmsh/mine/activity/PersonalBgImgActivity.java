package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantVariable.REQ_MINE_BG;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/9/26
 * class descript背景图片更换
 */
public class PersonalBgImgActivity extends BaseActivity {
    @BindView(R.id.iv_mine_bg_img)
    ImageView iv_mine_bg_img;
    private String imgBgUrl;
    @Override
    protected int getContentView() {
        return R.layout.activity_mine_bg_img;
    }
    @Override
    protected void initViews() {
        Intent intent = getIntent();
        imgBgUrl = intent.getStringExtra("imgUrl");
        if (!TextUtils.isEmpty(imgBgUrl)) {
            GlideImageLoaderUtil.loadCenterCrop(PersonalBgImgActivity.this, iv_mine_bg_img, imgBgUrl);
        } else {
            GlideImageLoaderUtil.loadCenterCrop(PersonalBgImgActivity.this, iv_mine_bg_img, "android.resource://com.amkj.dmsh/drawable/" + R.drawable.mine_no_login_bg);
        }
    }

    @Override
    protected void loadData() {

    }

    //    返回键监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_MINE_BG) {
            String imgUrl = data.getStringExtra("imgUrl");
            if (!TextUtils.isEmpty(imgUrl)) {
                GlideImageLoaderUtil.loadCenterCrop(PersonalBgImgActivity.this, iv_mine_bg_img, imgUrl);
            }
        }
    }

    //  更换背景
    @OnClick(R.id.tv_mine_bg_cover)
    void changeCoverBg(View view) {
        Intent intent = new Intent(PersonalBgImgActivity.this, PersonalBgImgSelActivity.class);
        startActivityForResult(intent, REQ_MINE_BG);
    }

    //  更换背景
    @OnClick(R.id.tv_mine_bg_cancel)
    void cancelSetBg(View view) {
        finish();
    }


}
