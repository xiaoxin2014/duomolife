package com.amkj.dmsh.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.release.bean.ImagePathBean;

import java.util.ArrayList;
import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_ADD_IMG;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/10/29
 * version 3.1.8
 * class description:上传图片转换工具
 */
public class ImageFormatUtils {
    private static ImageFormatUtils imageFormatUtils;

    private ImageFormatUtils() {
    }

    public static ImageFormatUtils getImageFormatInstance() {
        if (imageFormatUtils == null) {
            synchronized (QyServiceUtils.class) {
                if (imageFormatUtils == null) {
                    imageFormatUtils = new ImageFormatUtils();
                }
            }
        }
        return imageFormatUtils;
    }

    /**
     * 添加默认图片
     *
     * @return
     */
    public ImagePathBean getDefaultAddImage() {
        return new ImagePathBean(DEFAULT_ADD_IMG, false);
    }

    /**
     * bean切换为String
     *
     * @param imagePathBeans
     * @return
     */
    public List<String> formatStringPath(List<ImagePathBean> imagePathBeans) {
        List<String> formatPaths = new ArrayList<>();
        if (imagePathBeans == null) {
            return formatPaths;
        }
        for (int i = 0; i < imagePathBeans.size(); i++) {
            ImagePathBean imagePathBean = imagePathBeans.get(i);
            if (!TextUtils.isEmpty(imagePathBean.getPath())) {
                formatPaths.add(imagePathBean.getPath());
            }
        }
        return formatPaths;
    }

    /**
     * 转换为String 移除默认占位图片
     *
     * @param imagePathBeans
     * @return
     */
    public List<String> formatStringPathRemoveDefault(List<ImagePathBean> imagePathBeans) {
        List<String> formatPaths = new ArrayList<>();
        if (imagePathBeans == null) {
            return formatPaths;
        }
        for (int i = 0; i < imagePathBeans.size(); i++) {
            ImagePathBean imagePathBean = imagePathBeans.get(i);
            if (!TextUtils.isEmpty(imagePathBean.getPath())
                    && !imagePathBean.getPath().contains(DEFAULT_ADD_IMG)) {
                formatPaths.add(imagePathBean.getPath());
            }
        }
        return formatPaths;
    }

    /**
     *
     * @param imagePathList imageBean || String
     * @param isShowDel     是否展示删除按钮
     * @return
     */
    public List<ImagePathBean> submitChangeIconStatus(@NonNull List<ImagePathBean> imagePathList, boolean isShowDel) {
        for (int i = 0; i < imagePathList.size(); i++) {
            ImagePathBean imagePathBean = imagePathList.get(i);
            if(imagePathBean!=null&&
                    !DEFAULT_ADD_IMG.equals(imagePathBean.getPath())){
                imagePathBean.setShowDelIcon(isShowDel);
            }

        }
        return imagePathList;
    }

    /**
     * 删除某一项
     *
     * @param imagePathBeans
     * @param delPosition
     * @return
     */
    public List<ImagePathBean> delImageBean(List<ImagePathBean> imagePathBeans, int delPosition) {
        if (imagePathBeans == null) {
            return new ArrayList<>();
        }
        if (imagePathBeans.size() <= delPosition) {
            return imagePathBeans;
        }
        imagePathBeans.remove(delPosition);
        if (imagePathBeans.size() > 0) {
            ImagePathBean imagePathBean = imagePathBeans.get(imagePathBeans.size() - 1);
            if (!getStrings(imagePathBean.getPath()).contains(DEFAULT_ADD_IMG)) {
                imagePathBeans.add(getDefaultAddImage());
            }
        } else {
            imagePathBeans.add(getDefaultAddImage());
        }
        return imagePathBeans;
    }
}
