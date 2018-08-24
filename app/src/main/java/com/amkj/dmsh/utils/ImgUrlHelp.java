package com.amkj.dmsh.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.amkj.dmsh.base.BaseApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

import static com.amkj.dmsh.base.BaseApplication.BUCKET_NAME;
import static com.amkj.dmsh.base.BaseApplication.OSS_URL;


/**
 * Created by atd48 on 2016/9/26.
 */
public class ImgUrlHelp {
    private List<String> data = new ArrayList<>();
    //传递回来地址集合
    private List<String> callBackPath;
    //压缩图片文件
    List<String> imgZip = new ArrayList<>();
    private Map<String, String> imgUrlMap;

    private String filePath;
    private OnFinishDataListener listener;
    private String imgName;
    private Context context;
    private OSS oss;

    public void setUrl(Context context, List<String> data) {
        //获取oss服务
        this.data.addAll(data);
        this.context = context;
        imgUrlMap = new HashMap<>();
        callBackPath = new ArrayList<>();
        compressWithRx(data);
    }

    /**
     * 鲁班压缩
     * @param photos
     */
    private void compressWithRx(final List<String> photos) {
        Flowable.just(photos)
                .observeOn(Schedulers.io())
                .map(list -> {
                    try {
                        List<File> files = Luban.with(context).load(list).get();
                        return files;
                    } catch (IOException e) {
                        e.printStackTrace();
                        List<File> errorList = new ArrayList<>();
                        for (String pathUrl:photos) {
                            errorList.add(new File(pathUrl));
                        }
                        return errorList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileList -> {
                    imgZip.clear();
                    for (File mFile : fileList) {
                        imgZip.add(mFile.getAbsolutePath());
                    }
                    sendOSSImg(context);
                });
    }

    private void sendOSSImg(Context context) {
        BaseApplication application = (BaseApplication) ((Activity) context).getApplication();
        oss = application.getOSS();
        //        上传网址
        // 构造上传请求
        //        objectkey 时间戳 + 十位随机数
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = format.format(new Date(System.currentTimeMillis()));
        StringBuffer buffer = new StringBuffer();
        PutObjectRequest put = null;
        if (data.size() == imgZip.size()) {
            for (int i = 0; i < imgZip.size(); i++) {
                for (int j = 0; j < 10; j++) {
                    if (j == 0) {
                        buffer.delete(0, buffer.length());
                    }
                    buffer.append((int) (Math.random() * 10));
                }
                String imgName = date + buffer + ".jpg";
                put = new PutObjectRequest(BUCKET_NAME, imgName, imgZip.get(i));
                UpLoadImage(oss, put);
            }
        }
    }

    private void UpLoadImage(OSS oss, final PutObjectRequest put) {
//        初始化上传图片失败最多次数
        oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(final PutObjectRequest request, PutObjectResult result) {
                Message message = handler.obtainMessage(2, request);
                handler.sendMessage(message);
            }
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                }
                Message message = handler.obtainMessage(0, request);
                handler.sendMessage(message);
            }
        });
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            PutObjectRequest request = (PutObjectRequest) msg.obj;
            if (msg.what == 2) {
                imgUrlMap.put(request.getUploadFilePath(), OSS_URL + request.getObjectKey());
                if (imgZip.size() == imgUrlMap.size()) {
                    for (int i = 0; i < imgZip.size(); i++) {
                        callBackPath.add(imgUrlMap.get(imgZip.get(i)));
                    }
                    if (listener != null && callBackPath.size() > 0) {
                        listener.finishData(callBackPath, handler);
                    }
                }
            } else if (msg.what == 1) {
                filePath = OSS_URL + request.getObjectKey();
                if (listener != null && filePath != null) {
                    listener.finishSingleImg(filePath, handler);
                }
            } else if (msg.what == 0) {
                if (listener != null) {
                    listener.finishError("网络异常");
                    handler.removeCallbacksAndMessages(null);
                }
            }
            return false;
        }
    });

    /**
     * 删除本次上传图片
     * @param bucketName
     * @param objectKey
     */
    private void deleteOssImage(@Nullable String bucketName, @Nullable String objectKey) {
        // 创建删除请求
        DeleteObjectRequest delete = new DeleteObjectRequest(bucketName, objectKey);
    // 异步删除
        oss.asyncDeleteObject(delete, new OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>() {
            @Override
            public void onSuccess(DeleteObjectRequest request, DeleteObjectResult result) {}
            @Override
            public void onFailure(DeleteObjectRequest request, ClientException clientException, ServiceException serviceException) {}

        });
    }

    public void setSingleImg(Context context, Bitmap bitmap) {
        BaseApplication application = (BaseApplication) ((Activity) context).getApplication();
        OSS oss = application.getOSS();
        //        上传网址
        // 构造上传请求
        //        objectkey 时间戳 + 十位随机数
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = format.format(new Date(System.currentTimeMillis()));
        StringBuffer buffer = new StringBuffer();
        PutObjectRequest put = null;
        for (int j = 0; j < 10; j++) {
            if (j == 0) {
                buffer.delete(0, buffer.length());
            }
            buffer.append((int) (Math.random() * 10));
        }
        imgName = date + buffer + ".png";
        byte[] uploadData = getBitmapByte(bitmap);
        put = new PutObjectRequest(BUCKET_NAME, imgName, uploadData);
        UpLoadImageSingle(oss, put);
    }

    private void UpLoadImageSingle(OSS oss, PutObjectRequest put) {
        oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Message message = handler.obtainMessage(1, request);
                handler.sendMessage(message);
                Log.d("PutObject", "UploadSuccess");
                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
                Message message = handler.obtainMessage();
                message.what = 0;
            }
        });
    }

    private byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public interface OnFinishDataListener {
        void finishData(List<String> data, Handler handler);

        void finishError(String error);

        void finishSingleImg(String singleImg, Handler handler);
    }

    public void setOnFinishListener(OnFinishDataListener listener) {
        this.listener = listener;
    }
}
