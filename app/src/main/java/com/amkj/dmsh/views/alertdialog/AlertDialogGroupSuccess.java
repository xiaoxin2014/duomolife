package com.amkj.dmsh.views.alertdialog;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseAlertDialog;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.utils.FileStreamUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.yanzhenjie.permission.Permission;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.UMShareAction.getImageContentValues;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.createFilePath;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.fileIsExist;

/**
 * Created by xiaoxin on 2021/3/22
 * Version:v5.1.0
 * ClassDescription :拼团完成弹窗
 */
public class AlertDialogGroupSuccess extends BaseAlertDialog {


    @BindView(R.id.iv_close)
    ImageView mIvClose;
    @BindView(R.id.iv_code)
    ImageView mIvCode;
    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    @BindView(R.id.rel_share_loading)
    RelativeLayout loadView;
    private ConstantMethod mConstantMethod;

    public AlertDialogGroupSuccess(Activity context, String url) {
        super(context);
        GlideImageLoaderUtil.loadCenterCrop(context, mIvCode, url);
        mLlContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mConstantMethod == null) {
                    mConstantMethod = new ConstantMethod();
                }
                mConstantMethod.setOnGetPermissionsSuccess(new ConstantMethod.OnGetPermissionsSuccessListener() {
                    @Override
                    public void getPermissionsSuccess() {
                        getSaveImageToCamera(url);
                    }
                });
                mConstantMethod.getPermissions(context, Permission.WRITE_EXTERNAL_STORAGE);
                return true;
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_alert_dialog_group;
    }


    public void setLoading(int code) {
        if (loadView != null) {
            if (code == 1) {
                loadView.setVisibility(View.GONE);
            } else {
                loadView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getSaveImageToCamera(String imageUrl) {
        String topicSavePath = Environment.getExternalStorageDirectory().getPath() + "/camera";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            setLoading(0);
            createFilePath(topicSavePath);
            String imageName = imageUrl.substring(imageUrl.lastIndexOf("/"));
            topicSavePath = topicSavePath + "/" + imageName;
            if (!fileIsExist(topicSavePath)) {
                final String finalTopicSavePath = topicSavePath;
                GlideImageLoaderUtil.downOriginalImg(context, imageUrl, new GlideImageLoaderUtil.OriginalLoaderFinishListener() {
                    @Override
                    public void onSuccess(File file) {
                        setLoading(1);
                        File pathFile = new File(finalTopicSavePath);
                        try {
                            if (FileStreamUtils.forChannel(file, pathFile)) {
                                showToast(R.string.saveSuccess);
                            }
                            // 其次把文件插入到系统图库
                            insertImageToSyatemImage(context, pathFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError() {
                        setLoading(1);
                    }
                });
            } else {
                setLoading(1);
                showToast("相片已保存在相册，赶紧去分享吧~");
            }
        }
    }

    private void insertImageToSyatemImage(Context context, File file) {
        ContentResolver localContentResolver = context.getContentResolver();
        ContentValues localContentValues = getImageContentValues(file, System.currentTimeMillis());
        localContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, localContentValues);
        Intent localIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        final Uri localUri = Uri.fromFile(file);
        localIntent.setData(localUri);
        context.sendBroadcast(localIntent);
    }

    @Override
    protected int getLayoutWith() {
        return AutoSizeUtils.mm2px(mAppContext, 560);
    }


    @OnClick(R.id.iv_close)
    public void onViewClicked() {
        dismiss();
    }
}
