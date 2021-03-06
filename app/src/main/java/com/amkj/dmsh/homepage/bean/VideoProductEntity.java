package com.amkj.dmsh.homepage.bean;

import com.amkj.dmsh.base.BaseEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2021/2/23
 * Version:v5.0.0
 * ClassDescription :视频列表实体类
 */
public class VideoProductEntity extends BaseEntity {

    /**
     * currentPage : 0
     * result : [{"coverPath":"string","id":0,"isCollect":"string","price":"string","title":"string","videoPath":"string"}]
     * showCount : 0
     * sysTime : string
     * totalPage : 0
     * totalResult : 0
     */

    private int currentPage;
    private int showCount;
    private String sysTime;
    private int totalPage;
    private int totalResult;
    private List<VideoDetailEntity.VideoDetailBean> result;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getShowCount() {
        return showCount;
    }

    public void setShowCount(int showCount) {
        this.showCount = showCount;
    }

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public List<VideoDetailEntity.VideoDetailBean> getResult() {
        return result;
    }

    public void setResult(List<VideoDetailEntity.VideoDetailBean> result) {
        this.result = result;
    }
}
