package com.amkj.dmsh.find;


import com.amkj.dmsh.homepage.bean.CommentAttentionUser.CommentAttentionBean;

import java.util.Comparator;

/**
 * 没有首字母特殊处理
 *
 * @author Administrator
 */
public class PinyinComparator implements Comparator<CommentAttentionBean> {
    public int compare(CommentAttentionBean o1, CommentAttentionBean o2) {
        if (o1.getSortLetters() != null && o2.getSortLetters() != null) {
            if (o1.getSortLetters().equals("@")
                    || o2.getSortLetters().equals("#")) {
                return -1;
            } else if (o1.getSortLetters().equals("#")
                    || o2.getSortLetters().equals("@")) {
                return 1;
            } else {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        } else {
            return -1;
        }
    }

}
