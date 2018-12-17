package com.amkj.dmsh.constant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.FileCacheUtils;
import com.amkj.dmsh.utils.FileSizeUtil;
import com.amkj.dmsh.utils.FileStreamUtils;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.bugly.beta.tinker.TinkerManager;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.createExecutor;
import static com.amkj.dmsh.constant.ConstantMethod.getVersionName;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_ID;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_NAME;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_NAME_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.UP_TOTAL_SIZE;
import static com.amkj.dmsh.constant.ConstantVariable.isDebugTag;
import static com.amkj.dmsh.constant.ConstantVariable.isUpTotalFile;
import static com.amkj.dmsh.constant.Url.BASE_URL;
import static com.amkj.dmsh.constant.Url.TOTAL_DATA_UP;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/4/19
 * version 3.1.2
 * class description:用户轨迹统计
 */
public class TotalPersonalTrajectory {

    private ScheduledExecutorService schedule;
    private final Context context;
    private long timeMilliseconds;
    private String mFragmentName;
    private String orderNo;
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private String relateId;
    private String pushId;

    public TotalPersonalTrajectory(Context mContext) {
        schedule = createSchedule();
        context = mContext;
    }

    public TotalPersonalTrajectory(Context mContext, String fragmentName) {
        schedule = createSchedule();
        context = mContext;
        mFragmentName = fragmentName;
    }

    public TotalPersonalTrajectory(Context mContext, String fragmentName, Map<String, String> totalMap) {
        context = mContext;
        mFragmentName = fragmentName;
        if (totalMap != null) {
            saveTotalDataToFile();
        }
    }


    public void stopTotal() {
        stopTotal(null);
    }

    public void stopTotal(Map<String, String> totalMap) {
        if (schedule != null && !schedule.isShutdown() && !isUpTotalFile) {
            schedule.shutdown();
            if (timeMilliseconds > 100) {
                saveTotalDataToFile(totalMap);
            }
        }
    }

    /**
     * 保存轨迹
     *
     * @param totalMap 参数map
     */
    public void saveTotalDataToFile(Map<String, String> totalMap) {
        String typeName = "";
        if (totalMap != null) {
            if (!TextUtils.isEmpty(totalMap.get("order_no"))) {
                orderNo = totalMap.get("order_no");
            }
            if (!TextUtils.isEmpty(totalMap.get("relate_id"))) {
                relateId = totalMap.get("relate_id");
            }
            if (!TextUtils.isEmpty(totalMap.get("productId"))) {
                relateId = totalMap.get("productId");
            }
            if (!TextUtils.isEmpty(totalMap.get("pushId"))) {
                relateId = totalMap.get("pushId");
            }
            if (!TextUtils.isEmpty(totalMap.get("categoryId"))) {
                relateId = totalMap.get("categoryId");
            }
            if (totalMap.get(TOTAL_NAME_TYPE) != null) {
                typeName = totalMap.get(TOTAL_NAME_TYPE);
            }
        }
        if (!TextUtils.isEmpty(typeName)) {
            saveTotalDataToFile(typeName);
        } else {
            saveTotalDataToFile();
        }
    }

    /**
     * 记录轨迹路径 写入文件
     */
    private void saveTotalDataToFile() {
        if (context == null) {
            return;
        }
        saveTotalDataToFile(TextUtils.isEmpty(mFragmentName) ? context.getClass().getSimpleName() : mFragmentName);
    }

    private void saveTotalDataToFile(String name) {
        if (context == null) {
            return;
        }
        TinkerBaseApplicationLike application = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        Map<String, Map<String, String>> totalActionMap = application.getTotalActionMap();
        if (totalActionMap == null) {
            return;
        }
        Map<String, String> stringStringMap = totalActionMap.get(name);
        if (stringStringMap == null) {
            return;
        }
        if (!isUpTotalFile && !TextUtils.isEmpty(stringStringMap.get(TOTAL_NAME)) && !TextUtils.isEmpty(stringStringMap.get(TOTAL_ID))) {
            createExecutor().execute(() -> {
                File trajectoryFile = createFiles(context.getDir("trajectory", Context.MODE_APPEND));
                File file = new File(trajectoryFile.getAbsolutePath() + "/" + "trajectoryData" + ".txt");
                Map<String, Object> totalDataMap = new HashMap<>();
                totalDataMap.put("act", stringStringMap.get(TOTAL_NAME));
                totalDataMap.put("act_id", stringStringMap.get(TOTAL_ID));
                if (!TextUtils.isEmpty(orderNo)) {
                    totalDataMap.put("order_no", orderNo);
                }
                if (!TextUtils.isEmpty(relateId)) {
                    totalDataMap.put("relate_id", relateId);
                }
                totalDataMap.put("stay_time", timeMilliseconds);
                /**
                 * 3.1.5加入
                 */
                totalDataMap.put("version", getVersionName(context));
                totalDataMap.put("act_time", simpleDateFormat.format(System.currentTimeMillis()));
                totalDataMap.put("uid", userId > 0 ? userId : null);
                JSONObject jsonObject = new JSONObject(totalDataMap);
                FileStreamUtils.writeFileFromString(file.getAbsolutePath(), jsonObject.toString() + ",", true);
                if (isDebugTag) {
                    String s = FileStreamUtils.readFile2String(file.getAbsolutePath());
                    com.amkj.dmsh.utils.Log.longLogW("fileTotal:————》", s);
                }
                isUpTotalFile(trajectoryFile.getAbsolutePath());
            });
        }
    }

    /**
     * 判断是否上传数据
     *
     * @param upFilePath
     */
    private void isUpTotalFile(String upFilePath) {
        if (FileSizeUtil.getFileOrFilesSize(upFilePath, FileSizeUtil.SIZE_TYPE_KB) >= UP_TOTAL_SIZE) {
            isUpTotalFile = true;
            getFileTotalTrajectory();
        }
    }

    /**
     * 读取文件 获取统计数据上传
     */
    public void getFileTotalTrajectory() {
        File trajectoryFile = context.getDir("trajectory", Context.MODE_APPEND);
        if (trajectoryFile != null && trajectoryFile.isDirectory()) {
            for (File file : trajectoryFile.listFiles()) {
                if (file.getName().contains("trajectoryData")) {
                    String trajectoryData = FileStreamUtils.readFile2String(file.getAbsolutePath());
                    if (!TextUtils.isEmpty(trajectoryData)) {
                        upTotalDataAndDel(trajectoryData, file.getAbsolutePath());
                    } else {
                        FileCacheUtils.deleteFolderFile(file.getPath(), true);
                    }
                } else {
                    FileCacheUtils.deleteFolderFile(file.getPath(), true);
                }
            }
            isUpTotalFile = false;
        } else {
            isUpTotalFile = false;
        }
    }

    private void upTotalDataAndDel(@NonNull String trajectoryData, String filePath) {
        if (NetWorkUtils.checkNet(context) && trajectoryData.length() > 1) {
            String jsonStr = "[" + trajectoryData.substring(0, trajectoryData.length() - 1) + "]";
            Gson gson = new Gson();
//            原数据
            List<Object> objectList = gson.fromJson(jsonStr, new TypeToken<List<Object>>() {
            }.getType());
//            存储数据
            List<Object> saveObjects = new ArrayList<>(objectList);
            List<Object> reserveList = new ArrayList<>();
            try {
                /**
                 * 数据量大 分步上传 延迟上传
                 */
                for (int i = 0; i < objectList.size(); i++) {
                    reserveList.add(objectList.get(i));
                    if ((i + 1) % 20 == 0 || i == objectList.size() - 1) {
                        //                        上传数据
                        boolean isUpSuccess = uploadingTotalData(reserveList);
//                        避免用户中断网络或者数据异常导致上传失败
                        if(!isUpSuccess){
                            break;
                        }
                        //                        清除上传数据
                        /**
                         * 避免上传中断，需要将未上传的数据保存
                         */
                        deleteUploadingTotalData(filePath, saveObjects, reserveList);
                        reserveList.clear();
                        Thread.sleep(500);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isUpTotalFile = false;
        }
    }

    /**
     * 清除上传数据
     *
     * @param filePath    源文件
     * @param saveObjects 保存数据
     * @param reserveList 已上传数据
     */
    private void deleteUploadingTotalData(String filePath, List<Object> saveObjects, List<Object> reserveList) {
        removeList(saveObjects, reserveList.size());
        if (saveObjects.size() > 0) {
            String saveStr = JSON.toJSONString(saveObjects);
            saveStr = saveStr.substring(1, saveStr.length() - 1) + ",";
            FileStreamUtils.writeFileFromString(filePath, saveStr, false);
        }
    }

    // 顺序删除，但是对下标和索引进行了处理
    public static void removeList(List<Object> list, int delCount) {
        for (int i = 0; i < delCount; i++) {
            list.remove(i);
            delCount--;
            i--;
        }
    }

    private boolean uploadingTotalData(List<Object> jsonStr) {
        String url = BASE_URL + TOTAL_DATA_UP;
        Map<String, Object> params = new HashMap<>();
        params.put("deviceType", "Android");
        params.put("jsonData", JSON.toJSONString(jsonStr));
        try {
            Response<String> response = NetLoadUtils.getNetInstance().loadNetDataPostSync(mAppContext, url, params);
            if (response != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 创建文件夹
     *
     * @param file
     * @return
     */
    private File createFiles(File file) {
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    /**
     * 创建线程定时器
     */
    public ScheduledExecutorService createSchedule() {
        timeMilliseconds = 0;
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                timeMilliseconds += 100;
            }
        }, 100, 100, TimeUnit.MILLISECONDS);
        return scheduler;
    }

}
