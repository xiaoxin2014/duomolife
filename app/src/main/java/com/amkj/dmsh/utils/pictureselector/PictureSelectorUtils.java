package com.amkj.dmsh.utils.pictureselector;

import android.app.Activity;
import androidx.fragment.app.Fragment;

import com.amkj.dmsh.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfigC;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMediaC;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/1/17
 * version 3.7
 * class description:图片选择器工具Utils
 */

public class PictureSelectorUtils {
    public static PictureSelectorUtils pictureSelectorUtils = new PictureSelectorUtils();
    private int rowShowCount = 4;
    private int selMaxNum = 9;
    private int selectorMode = PictureConfigC.MULTIPLE;
    private boolean isCrop = false;
    private boolean isShowGif = false;
    private int aspectRatioX = 1;
    private int aspectRatioY = 1;
    private List<LocalMediaC> selImageList = new ArrayList<>();
    private int evaCurrentItem = 0;

    private PictureSelectorUtils() {
    }

    public static PictureSelectorUtils getInstance() {
        return pictureSelectorUtils;
    }

    /**
     * 重新设置参数
     */
    public PictureSelectorUtils resetVariable() {
        rowShowCount = 4;
        selectorMode = PictureConfigC.MULTIPLE;
        isCrop = false;
        isShowGif = false;
        aspectRatioX = 1;
        aspectRatioY = 1;
        selMaxNum = 9;
        selImageList.clear();
        evaCurrentItem = 0;
        return pictureSelectorUtils;
    }

    public void openGallery(Activity activity) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_sina_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .imageSpanCount(rowShowCount)// 每行显示个数
                .selectionMode(selectorMode)// 多选 or 单选
                .maxSelectNum(selMaxNum)
                .minSelectNum(1)
                .previewImage(true)// 是否可预览图片
                .enableCrop(isCrop)// 是否裁剪
                .compress(false)// 是否压缩
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(aspectRatioX, aspectRatioY)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .isGif(isShowGif)// 是否显示gif图片
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                .openClickSound(false)// 是否开启点击声音
                .selectionMedia(selImageList)// 传入已选图片
                .evaCurrentItem(evaCurrentItem)//商品评价
                .forResult(PictureConfigC.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    public void openGallery(Fragment fragment) {
        PictureSelector.create(fragment)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_sina_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .imageSpanCount(rowShowCount)// 每行显示个数
                .selectionMode(selectorMode)// 多选 or 单选
                .maxSelectNum(selMaxNum)
                .minSelectNum(1)
                .previewImage(true)// 是否可预览图片
                .enableCrop(isCrop)// 是否裁剪
                .compress(false)// 是否压缩
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(aspectRatioX, aspectRatioY)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .isGif(isShowGif)// 是否显示gif图片
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                .openClickSound(false)// 是否开启点击声音
                .selectionMedia(selImageList)// 传入已选图片
                .evaCurrentItem(evaCurrentItem)//商品评价
                .forResult(PictureConfigC.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    public void openCamera(Activity activity) {
        PictureSelector.create(activity)
                .openCamera(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_sina_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .enableCrop(isCrop)// 是否裁剪
                .compress(false)// 是否压缩
                .withAspectRatio(aspectRatioX, aspectRatioY)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                .openClickSound(false)// 是否开启点击声音
                .forResult(PictureConfigC.CHOOSE_REQUEST);
    }
    public void openCamera(Fragment fragment) {
        PictureSelector.create(fragment)
                .openCamera(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_sina_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .enableCrop(isCrop)// 是否裁剪
                .compress(false)// 是否压缩
                .withAspectRatio(aspectRatioX, aspectRatioY)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                .openClickSound(false)// 是否开启点击声音
                .forResult(PictureConfigC.CHOOSE_REQUEST);
    }
    /**
     * 压缩比例
     *
     * @param x
     * @param y
     * @return
     */
    public PictureSelectorUtils withAspectRatio(int x, int y) {
        aspectRatioX = x > 0 ? x : 1;
        aspectRatioY = y > 0 ? y : 1;
        return pictureSelectorUtils;
    }

    /**
     * 是否裁剪
     *
     * @param isCrop
     * @return
     */
    public PictureSelectorUtils isCrop(boolean isCrop) {
        this.isCrop = isCrop;
        return pictureSelectorUtils;
    }

    /**
     * 选择模式
     *
     * @param selectorMode
     * @return
     */
    public PictureSelectorUtils imageMode(int selectorMode) {
        this.selectorMode = selectorMode;
        if (selectorMode == PictureConfigC.SINGLE) {
            selMaxNum = 1;
        }
        return pictureSelectorUtils;
    }

    /**
     * 设置行数
     *
     * @param rowShowCount
     * @return
     */
    public PictureSelectorUtils rowShowCount(int rowShowCount) {
        this.rowShowCount = rowShowCount;
        return pictureSelectorUtils;
    }

    /**
     * 最大选择图片数量
     *
     * @param selMaxNum
     * @return
     */
    public PictureSelectorUtils selMaxNum(int selMaxNum) {
        this.selMaxNum = selMaxNum;
        return pictureSelectorUtils;
    }

    /**
     * 是否显示Gif
     *
     * @param isShowGif
     * @return
     */
    public PictureSelectorUtils isShowGif(boolean isShowGif) {
        this.isShowGif = isShowGif;
        return pictureSelectorUtils;
    }

    /**
     * 设置已选图片
     * @param selImageList
     * @return
     */
    public PictureSelectorUtils selImageList(List<String> selImageList) {
        this.selImageList.clear();
        if(selImageList!=null&&selImageList.size()>0){
            LocalMediaC localMedia;
            for (String imgPath : selImageList) {
                localMedia = new LocalMediaC();
                localMedia.setChecked(true);
                localMedia.setPath(imgPath);
                this.selImageList.add(localMedia);
            }
        }
        return pictureSelectorUtils;
    }

    /**
     * 评价自定义参数 -- 当前item
     * @param currentItem
     * @return
     */
    public PictureSelectorUtils appraiseCurrentEva(int currentItem) {
        this.evaCurrentItem = currentItem;
        return pictureSelectorUtils;
    }
}
