package com.amkj.dmsh.views;

import android.content.Context;
import android.support.text.emoji.widget.EmojiTextView;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.klinker.android.link_builder.TouchableBaseSpan;
import com.klinker.android.link_builder.TouchableMovementMethod;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/1/4
 * version 3.2.0
 * class description:emoji Link
 */
public class EmojiTextViewLink extends EmojiTextView {

    public EmojiTextViewLink(Context context) {
        super(context);
    }

    public EmojiTextViewLink(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmojiTextViewLink(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        MovementMethod movementMethod = getMovementMethod();

        if (movementMethod instanceof TouchableMovementMethod) {
            TouchableBaseSpan span = ((TouchableMovementMethod) movementMethod).getPressedSpan();
            if (span != null) {
                return true;
            }
        }

        return false;
    }
}
