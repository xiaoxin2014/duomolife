package com.amkj.dmsh.find.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.utils.WindowUtils;

import static com.amkj.dmsh.dao.SoftApiDao.reportIllegal;

/**
 * Created by xiaoxin on 2019/7/15
 * Version:v4.1.0
 * ClassDescription :帖子评论举报弹窗
 */
public abstract class PostReplyPw extends PopupWindow {


    public PostReplyPw(final Activity activity, int objId) {
        super(LayoutInflater.from(activity).inflate(R.layout.pw_post_comment, null, false)
                , ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        setTouchable(true);
        // 设置背景颜色
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置弹出动画
        setAnimationStyle(R.style.pw_slide_center);
        TextView tvComment = getContentView().findViewById(R.id.tv_comment);
        TextView tvReport = getContentView().findViewById(R.id.tv_report);
        tvComment.setOnClickListener(v1 -> {
            WindowUtils.closePw(this);
            onCommentClick();
        });
        tvReport.setOnClickListener(v2 -> {
            WindowUtils.closePw(this);
            //举报评论
            reportIllegal(activity, objId, 2);
        });
    }

    public abstract void onCommentClick();

//    public abstract void onPostClick();


}
