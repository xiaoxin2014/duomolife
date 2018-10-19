package com.amkj.dmsh.utils;

import android.support.annotation.NonNull;

import com.amkj.dmsh.base.BaseRemoveExistProductBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 去重utils 不能采用单例模式
 */
public class RemoveExistUtils<T extends BaseRemoveExistProductBean> {
    private Set<Integer> existIdSet;

    /**
     * 去重list
     *
     * @param list 新增的集合
     * @return
     */
    public List<T> removeExistList(@NonNull List<T> list) {
        if (existIdSet == null) {
            existIdSet = new HashSet<>();
        }
        List<T> reserveList = new ArrayList<>();
        for (BaseRemoveExistProductBean baseRemoveExistProductBean : list) {
            int id = baseRemoveExistProductBean.getId();
            if (id == 0 || !existIdSet.contains(id)) {
                reserveList.add((T) baseRemoveExistProductBean);
                if (id > 0) {
                    existIdSet.add(id);
                }
            }
        }
        return reserveList;
    }

    /**
     * 刷新清除保存的Id
     */
    public void clearData() {
        if(existIdSet!=null){
            existIdSet.clear();
        }
    }
}
