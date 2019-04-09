package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.EditorEntity;
import com.amkj.dmsh.bean.EditorEntity.EditorBean;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.umeng.socialize.UMShareAPI;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2019/3/14 0014
 * Version：V3.3.0
 * class description:小编精选
 */
public class EditorSelectActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.iv_img_service)
    ImageView mIvImgService;
    EditorEntity mEditorEntity = null;

    @Override
    protected int getContentView() {
        return R.layout.activity_editor_select;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("小编精选");
        mIvImgService.setVisibility(View.GONE);
        initRv();
    }

    private void initRv() {
    }

    @Override
    protected void loadData() {
    }

    @OnClick({R.id.tv_life_back, R.id.iv_img_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            //分享
            case R.id.iv_img_share:
                if (mEditorEntity != null) {
                    List<EditorBean> EditorList = mEditorEntity.getResult();
                    if (EditorList != null && EditorList.size() > 0) {
                        EditorBean editorBean = EditorList.get(0);
                        if (editorBean != null) {
                            new UMShareAction(this
                                    , editorBean.getCoverImg()
                                    , getStrings(mEditorEntity.getTitle())
                                    , getStrings(mEditorEntity.getDescription())
                                    , Url.BASE_SHARE_PAGE_TWO + ("m/template/find_template/handpick-article.html"), 1);
                        }
                    }
                }
                break;
        }
    }

    //友盟分享注册回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    public void setUrl(EditorEntity editorEntity) {
        this.mEditorEntity = editorEntity;
    }
}
