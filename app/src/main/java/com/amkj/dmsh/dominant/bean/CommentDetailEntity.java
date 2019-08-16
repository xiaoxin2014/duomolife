package com.amkj.dmsh.dominant.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;
import com.amkj.dmsh.dominant.bean.PostCommentEntity.PostCommentBean;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/3
 * class description:请输入类描述
 */

public class CommentDetailEntity extends BaseEntity {

    @SerializedName("result")
    private PostCommentBean commentDetailBean;

    public PostCommentBean getCommentDetailBean() {
        return commentDetailBean;
    }
}
