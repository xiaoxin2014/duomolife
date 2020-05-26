package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.ImageBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.views.alertdialog.AlertDialogEdit;
import com.amkj.dmsh.find.activity.ImagePagerActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.adapter.InvoiceListAdapter;
import com.amkj.dmsh.shopdetails.bean.IndentInvoiceEntity;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.INVOICE_APPLY_SUCCESS;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.INVOICE_DETAIL;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.IMAGE_DEF;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.INTENT_IMAGE_TYPE;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.INTENT_POSITION;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.base64ToBitmap;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.createFilePath;

/**
 * Created by xiaoxin on 2020/3/9
 * Version:v4.4.2
 * ClassDescription :新版发票详情
 */
public class InvoiceDetailActivity extends BaseActivity {

    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.iv_img_share)
    ImageView mIvImgShare;
    @BindView(R.id.tv_invoice_status)
    TextView mTvInvoiceStatus;
    @BindView(R.id.rv_invoice)
    RecyclerView mRvInvoice;
    @BindView(R.id.tv_apply_for)
    TextView mTvApplyFor;
    @BindView(R.id.tv_save)
    TextView mTvSave;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.tl_quality_bar)
    Toolbar mTlQualityBar;
    @BindView(R.id.tv_send_email)
    TextView mTvSendEmail;
    @BindView(R.id.tv_complete_time)
    TextView mTvCompleteTime;

    private String mOrderNo;
    private IndentInvoiceEntity mIndentInvoiceEntity;
    private InvoiceListAdapter mInvoiceListAdapter;
    private List<ImageBean> mImageBeanList;
    private List<Bitmap> mBitmapList = new ArrayList<>();
    private AlertDialogEdit mAlertDialogSendInvoice;

    @Override
    protected int getContentView() {
        return R.layout.activity_invoice_detail;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("发票详情");
        mIvImgShare.setVisibility(View.GONE);
        mTlQualityBar.setSelected(true);
        String str = getString(R.string.invoice_send_email);
        mTvSendEmail.setText(ConstantMethod.getSpannableString(str, 8, str.length(), -1, "#0a88fa"));
        if (getIntent() != null) {
            mOrderNo = getIntent().getStringExtra("orderNo");
        }

        smart_communal_refresh.setOnRefreshListener(refreshLayout -> refreshData());

        //初始化发票列表
        mRvInvoice.setLayoutManager(new LinearLayoutManager(this));
        mInvoiceListAdapter = new InvoiceListAdapter(mBitmapList);
        mInvoiceListAdapter.bindToRecyclerView(mRvInvoice);
        mInvoiceListAdapter.setEmptyView(R.layout.empty_invoice_bg);
        mRvInvoice.setAdapter(mInvoiceListAdapter);
        mRvInvoice.setNestedScrollingEnabled(false);
        mInvoiceListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
                intent.putExtra(INTENT_POSITION, position);
                intent.putExtra(INTENT_IMAGE_TYPE, IMAGE_DEF);
                startActivity(intent);
            }
        });
        mImageBeanList = ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getImageBeanList();
    }

    private void refreshData() {
        mAlertDialogSendInvoice = null;
        loadData();
    }

    @Override
    protected void loadData() {
        Map<String, Object> params = new HashMap<>();
        params.put("no", mOrderNo);
        params.put("version", 1);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, INVOICE_DETAIL, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                mIndentInvoiceEntity = GsonUtils.fromJson(result, IndentInvoiceEntity.class);

                if (mIndentInvoiceEntity != null) {
                    IndentInvoiceEntity.IndentInvoiceBean indentInvoiceBean = mIndentInvoiceEntity.getIndentInvoiceBean();
                    if (SUCCESS_CODE.equals(mIndentInvoiceEntity.getCode())) {
                        if (indentInvoiceBean != null) {
                            int statusNum = indentInvoiceBean.getInvoice().getStatus();
                            Map<String, String> statusMap = indentInvoiceBean.getStatus();
                            String status = statusMap.get(indentInvoiceBean.getInvoice().getStatus() + "");
                            String imgBase64Arr = indentInvoiceBean.getInvoice().getImgBase64Arr();
                            mTvInvoiceStatus.setText(status);
                            //"0": "未开","1": "待出","2": "已开 ","3": "作废","4": "重开"
                            mTvApplyFor.setVisibility(statusNum == 0 ? VISIBLE : View.GONE);
                            mTvSave.setVisibility(statusNum != 0 ? VISIBLE : View.GONE);
                            mTvSave.setEnabled(statusNum == 2);
                            mRvInvoice.setVisibility(statusNum == 1 || statusNum == 2 ? VISIBLE : View.GONE);
                            mTvSendEmail.setVisibility(statusNum == 2 ? VISIBLE : View.GONE);
                            mTvCompleteTime.setVisibility(statusNum == 1 ? VISIBLE : View.GONE);
                            if (!TextUtils.isEmpty(imgBase64Arr)) {
                                String[] invoiceList = imgBase64Arr.split("@@");
                                List<Bitmap> bitmaps = new ArrayList<>();
                                mImageBeanList.clear();
                                for (String picUrl : invoiceList) {
                                    ImageBean imageBean = new ImageBean();
                                    imageBean.setPicUrl("data:image/png;base64," + picUrl);
                                    mImageBeanList.add(imageBean);
                                    bitmaps.add(base64ToBitmap(picUrl));
                                }
                                recycleInvoice();
                                mBitmapList = bitmaps;
                            }
                        }
                    } else {
                        showToast(mIndentInvoiceEntity.getMsg());
                    }
                }

                mInvoiceListAdapter.setNewData(mBitmapList);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mIndentInvoiceEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mIndentInvoiceEntity);
            }
        });
    }

    private void recycleInvoice() {
        for (Bitmap bitmap : mBitmapList) {
            ConstantMethod.recycleBitmap(bitmap);
        }
        mBitmapList.clear();
        System.gc();
    }

    @OnClick({R.id.tv_life_back, R.id.iv_img_service, R.id.tv_apply_for, R.id.tv_save, R.id.tv_send_email})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            case R.id.iv_img_service:
                QyServiceUtils.getQyInstance().openQyServiceChat(getActivity(), "发票详情");
                break;
            //申请发票
            case R.id.tv_apply_for:
                Intent intent = new Intent(this, ApplyInvoiceActivity.class);
                intent.putExtra("orderNo", mOrderNo);
                startActivity(intent);
                break;
            //保存发票
            case R.id.tv_save:
                ConstantMethod constantMethod = new ConstantMethod();
                constantMethod.setOnGetPermissionsSuccess(() -> {
                    showLoadhud(this);
                    for (Bitmap bitmap : mBitmapList) {
                        compressPic(bitmap);
                    }
                    dismissLoadhud(this);
                });

                constantMethod.getPermissions(this, Permission.WRITE_EXTERNAL_STORAGE);
                break;
            //发送到邮箱
            case R.id.tv_send_email:
                try {
                    if (mAlertDialogSendInvoice == null) {
                        mAlertDialogSendInvoice = new AlertDialogEdit(this);
                        mAlertDialogSendInvoice.setCoverVisible(VISIBLE)
                                .setTitleVisible(VISIBLE)
                                .setOnAlertListener(this::sendEmail);
                    }

                    ImageView ivCover = mAlertDialogSendInvoice.getDialogView().findViewById(R.id.iv_invoice_cover);
                    if (mBitmapList != null && mBitmapList.size() > 0) {
                        Bitmap bitmap = mBitmapList.get(0);
                        ivCover.setImageBitmap(bitmap.isRecycled() ? null : bitmap);
                    }

                    mAlertDialogSendInvoice.show();
                } catch (Exception e) {
                    showToast("请刷新页面后重试");
                }
                break;
        }
    }


    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        recycleInvoice();
        ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getImageBeanList().clear();
    }

    //把图片缓存至本地，并通知图库更新
    public void compressPic(Bitmap bitmap) {
        String dirPath = Environment.getExternalStorageDirectory().getPath() + "/camera";
        String filePath = dirPath + "/" + System.currentTimeMillis() + ".jpg";
        File file = new File(filePath);
        try {
            createFilePath(dirPath);
            FileOutputStream fos = new FileOutputStream(file);
            //把图片缓存至本地
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            //把图片插入系统图库
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            showToast("保存失败");
            return;
        }

        // 最后通知图库更新
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
        showToast("已保存至" + dirPath);
    }

    private void sendEmail(String mEmail) {
        if (TextUtils.isEmpty(mEmail)) {
            showToast("邮箱地址不能为空");
            return;
        } else {
            String rule = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
            Pattern pattern = Pattern.compile(rule);
            Matcher matcher = pattern.matcher(mEmail);
            if (!matcher.matches()) {
                showToast("请输入正确的邮箱地址");
                return;
            }
        }
        mAlertDialogSendInvoice.dismiss();
        showLoadhud(getActivity());
        Map<String, String> map = new HashMap<>();
        map.put("orderNo", mOrderNo);
        map.put("mailAddress", mEmail);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.SEND_INVOICE, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(getActivity());
                showToast("发送成功");
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(getActivity());
                showToast("发送失败");
            }
        });
    }


    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (INVOICE_APPLY_SUCCESS.equals(message.type)) {
            mAlertDialogSendInvoice = null;
            loadData();
        }
    }
}
