package com.amkj.dmsh.network.downfile;

import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.downFileMap;
import static com.amkj.dmsh.constant.ConstantVariable.isDebugTag;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/11/30
 * version 3.1.9
 * class description:下载的工具
 */
public class DownloadHelper {

    // 超时15s
    private static final int DEFAULT_TIMEOUT = 15;
    // 网络工具retrofit
    private Retrofit retrofit;
    // 下载进度、完成、失败等的回调事件
    private DownloadListener mDownloadListener;
    // 清除线程需要用到的
    private Disposable disposable;

    /**
     * 构造函数初始化
     *
     * @param listener 回调函数
     */
    public DownloadHelper(DownloadListener listener, String url) {
        this.mDownloadListener = listener;
        DownloadInterceptor mInterceptor = new DownloadInterceptor(listener);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (isDebugTag) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(logging);
        }
        OkHttpClient httpClient = builder
                .addInterceptor(mInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 进行文件下载
     *
     * @param url     网址
     * @param destDir 文件目录
     */
    public void downloadFile(String url, final String destDir) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
//        避免多次下载
        if (downFileMap == null) {
            downFileMap = new HashMap<>();
        }
        if (downFileMap.get(url) != null) {
            showToast(mAppContext, "正在下载……");
            return;
        } else {
            downFileMap.put(url, url);
        }
        dispose();
        retrofit.create(NetDownLoadApiService.class)
                .downLoadFile(url)
                .subscribeOn(Schedulers.io())//请求网络 在调度者的io线程
                .observeOn(Schedulers.io()) //指定线程保存文件
                .observeOn(Schedulers.computation())
                .map(new Function<Response<ResponseBody>, File>() {
                    @Override
                    public File apply(Response<ResponseBody> responseBodyResponse) throws Exception {
                        ResponseBody responseBody = responseBodyResponse.body();
                        if (responseBody == null) {
                            return null;
                        }
                        mDownloadListener.onStartDownload();
                        okhttp3.Response response = responseBodyResponse.raw();
                        String downUrl = response.request().url().toString();
                        String fileName = null;
                        int lastCode = downUrl.lastIndexOf("/");
                        if (lastCode != -1 && lastCode + 1 < downUrl.length()) {
                            fileName = downUrl.substring(lastCode + 1);
                            int parameterCode = fileName.indexOf("?");
                            if (parameterCode != -1) {
                                fileName = fileName.substring(0, parameterCode);
                            }
                        }
                        Headers headers = responseBodyResponse.headers();
                        Map<String, List<String>> stringListMap = headers.toMultimap();
                        List<String> fileNameList = stringListMap.get("content-disposition");
                        if (fileNameList != null && fileNameList.size() > 0
                                && fileNameList.toString().contains("filename=")) {
                            for (String typeParameter : fileNameList) {
                                String[] split = typeParameter.split(";");
                                for (String name : split) {
                                    if (name.contains("filename=")) {
                                        String[] names = name.split("filename=");
                                        if (names.length > 1) {
                                            fileName = names[1];
                                        }
                                    }
                                }
                            }
                        }
                        return saveFile(responseBody.byteStream(), destDir, fileName);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseDownloadObserver<File>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    protected void onDownloadSuccess(File file) {
                        mDownloadListener.onFinishDownload(file);
                        if (downFileMap.get(url) != null) {
                            downFileMap.remove(url);
                        }
                    }

                    @Override
                    protected void onDownloadError(Throwable e) {
                        mDownloadListener.onFail(e);
                        if (downFileMap.get(url) != null) {
                            downFileMap.remove(url);
                        }
                    }
                });
    }

    /**
     * 销毁
     */
    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /**
     * 将文件写入本地
     *
     * @param destFileDir  目标文件夹
     * @param destFileName 目标文件名
     * @return 写入完成的文件
     * @throws IOException IO异常
     */
    private File saveFile(InputStream is, String destFileDir, String destFileName) throws IOException {
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            if (file.exists()) {
                file.delete();
            }
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            return file;

        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
