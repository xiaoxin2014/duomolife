/*
 * Copyright 2014 Hieu Rocker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package emojicon;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import io.github.rockerhieu.emojicon.R;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com).
 */
public class EmojiconTextViewMore extends AppCompatTextView {
    private int mEmojiconSize;
    private int mEmojiconAlignment;
    private int mEmojiconTextSize;
    private int mTextStart = 0;
    private int mTextLength = -1;
    private boolean mUseSystemDefault = false;
    private int trimLines;
    private int colorClickableText;
    private ReadMoreClickableSpan viewMoreSpan;
    private String trimCollapsedText;
    private BufferType type;
    private SpannableStringBuilder builder;

    public EmojiconTextViewMore(Context context) {
        super(context);
        init(null);
    }

    public EmojiconTextViewMore(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EmojiconTextViewMore(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mEmojiconTextSize = (int) getTextSize();
        if (attrs == null) {
            mEmojiconSize = (int) getTextSize();
        } else {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Emojicon);
            mEmojiconSize = (int) a.getDimension(R.styleable.Emojicon_emojiconSize, getTextSize());
            mEmojiconAlignment = a.getInt(R.styleable.Emojicon_emojiconAlignment, DynamicDrawableSpan.ALIGN_BASELINE);
            mTextStart = a.getInteger(R.styleable.Emojicon_emojiconTextStart, 0);
            mTextLength = a.getInteger(R.styleable.Emojicon_emojiconTextLength, -1);
            mUseSystemDefault = a.getBoolean(R.styleable.Emojicon_emojiconUseSystemDefault, false);
            trimLines = a.getInt(R.styleable.Emojicon_trimLines, R.string.show_more);
            trimCollapsedText = getResources().getString(R.string.show_more);
            colorClickableText = a.getColor(R.styleable.Emojicon_colorClickableText,
                    ContextCompat.getColor(getContext(), R.color.accent));
            a.recycle();
        }
        viewMoreSpan = new ReadMoreClickableSpan();
        setText(getText());
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        this.type = type;
        if (!TextUtils.isEmpty(text)) {
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            EmojiconHandler.addEmojis(getContext(), builder, mEmojiconSize, mEmojiconAlignment, mEmojiconTextSize, mTextStart, mTextLength, mUseSystemDefault);
            if (this.getTag(R.id.baseline) == null || !(this.getTag(R.id.baseline).toString()).equals(text.toString())) {
                this.setTag(R.id.baseline, builder);
                onGlobalLayoutLineEndIndex();
            }
            text = builder;
        }
        super.setText(text, type);
    }

    /**
     * Set the size of emojicon in pixels.
     */
    public void setEmojiconSize(int pixels) {
        mEmojiconSize = pixels;
        super.setText(getText());
    }

    /**
     * Set whether to use system default emojicon
     */
    public void setUseSystemDefault(boolean useSystemDefault) {
        mUseSystemDefault = useSystemDefault;
    }

    private void onGlobalLayoutLineEndIndex() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                SpannableStringBuilder builder = (SpannableStringBuilder) EmojiconTextViewMore.this.getTag(R.id.baseline);
                if (builder != null) {
                    refreshLineEndIndex();
                }
            }
        });
    }

    private void refreshLineEndIndex() {
        Layout layout = getLayout();
        if (layout != null) {
            int lineCount = layout.getLineCount();
            if (lineCount > trimLines - 1) {
                super.setText(getDisplayableText(), type);
                setMovementMethod(LinkMovementMethod.getInstance());
                setHighlightColor(Color.TRANSPARENT);
            }
        }
    }

    private CharSequence getDisplayableText() {
        SpannableStringBuilder builder = (SpannableStringBuilder) EmojiconTextViewMore.this.getTag(R.id.baseline);
        int textLength = getLayout().getLineEnd(trimLines - 1);
        textLength -= ((textLength - getLayout().getLineEnd(trimLines - 2)) / 2);
        if (textLength <= builder.toString().length()) {
            builder.replace(0, builder.length(), builder.subSequence(0, textLength - 1));
            builder.append(trimCollapsedText);
            EmojiconTextViewMore.this.setTag(R.id.baseline, builder);
            return addClickableSpan(builder, trimCollapsedText);
        } else {
            return builder;
        }
    }

    private CharSequence addClickableSpan(SpannableStringBuilder s, CharSequence trimText) {
        s.setSpan(viewMoreSpan, s.length() - trimText.length(), s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

    private class ReadMoreClickableSpan extends ClickableSpan {
        @Override
        public void onClick(View widget) {
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(colorClickableText);
        }
    }

}
