/*
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

package com.klinker.android.link_builder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.MovementMethod;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LinkBuilder {

    private static final int TYPE_TEXT = 1;
    private static final int TYPE_TEXT_VIEW = 2;
    private TextView clickTextView;
    private int maxLength;

    public static LinkBuilder from(Context context, String text) {
        return new LinkBuilder(TYPE_TEXT)
                .setContext(context)
                .setText(text);
    }

    public static LinkBuilder on(TextView tv) {
        return new LinkBuilder(TYPE_TEXT_VIEW)
                .setContext(tv.getContext())
                .setTextView(tv);
    }

    private static final String TAG = "LinkBuilder";

    private int type;

    private Context context;

    private TextView textView;
    private CharSequence text;

    private boolean findOnlyFirstMatch = false;

    private List<Link> links = new ArrayList<>();

    private SpannableStringBuilder spannable = null;

    /**
     * Construct a LinkBuilder object.
     *
     * @param type TYPE_TEXT or TYPE_TEXT_VIEW
     */
    private LinkBuilder(int type) {
        this.type = type;
    }

    @Deprecated
    public LinkBuilder(TextView textView) {
        if (textView == null) {
            throw new IllegalArgumentException("textView is null");
        }

        this.textView = textView;
        setText(textView.getText().toString());
    }

    public LinkBuilder setTextView(TextView textView) {
        this.textView = textView;
        return setText(textView.getText());
    }

    public LinkBuilder setText(CharSequence text) {
        this.text = text;
        return this;
    }

    public LinkBuilder setContext(Context context) {
        this.context = context;
        return this;
    }

    public LinkBuilder setFindOnlyFirstMatchesForAnyLink(boolean findOnlyFirst) {
        this.findOnlyFirstMatch = findOnlyFirst;
        return this;
    }

    public LinkBuilder maxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    /**
     * Add a single link to the builder.
     *
     * @param link the rule that you want to link with.
     */
    public LinkBuilder addLink(Link link) {
        if (link == null) {
            throw new IllegalArgumentException("link is null");
        }
        this.links.add(link);
        return this;
    }

    public LinkBuilder setClickTextView(TextView clickTextView) {
        this.clickTextView = clickTextView;
        return this;
    }

    /**
     * Add a list of links to the builder.
     *
     * @param links list of rules you want to link with.
     */
    public LinkBuilder addLinks(List<Link> links) {
        if (links == null) {
            throw new IllegalArgumentException("link list is null");
        }
        if (links.isEmpty()) {
            throw new IllegalArgumentException("link list is empty");
        }
        for (Link link : links) {
            if (link == null) {
                throw new IllegalArgumentException("At least one link is null");
            }
        }
        this.links.addAll(links);
        return this;
    }

    /**
     * Execute the rules to create the linked text.
     */
    public CharSequence build() {
        // we extract individual links from the patterns
        turnPatternsToLinks();

        // exit if there are no links
        if (links.size() == 0) {
            newLineCheck(text.toString());
            if (!TextUtils.isEmpty(text) && maxLength > 0 && text.length() >= maxLength) {
                textView.setText((text.subSequence(0, maxLength) + "..."));
            }
            return null;
        }

        // we need to apply this text before the links are created
        applyAppendedAndPrependedText();


        // add those links to our spannable text so they can be clicked
        for (Link link : links) {
            addLinkToSpan(link);
        }

        if (type == TYPE_TEXT_VIEW) {
            // set the spannable text
            if (maxLength > 0 && spannable.length() >= maxLength) {
                newLineCheck(spannable.toString());
                spannable.delete(maxLength, spannable.length());
                spannable.append("...");
            }
            textView.setText(spannable);
            // add the movement method so we know what actions to perform on the clicks
            addLinkMovementMethod();
//            addLinkTouch();
        }
        return spannable;
    }

    private void newLineCheck(String text) {
        Pattern pattern = Pattern.compile("\n");
        Matcher matcher = pattern.matcher(text);
        int i = 0;
        while (matcher.find()) {
            int endLength = matcher.end();
            if (i == 2&&endLength<maxLength) {
                maxLength = endLength-1;
            }
            i++;
        }
    }

    /**
     * Add the link rule and check if spannable text is created.
     *
     * @param link rule to add to the text.
     */
    private void addLinkToSpan(Link link) {
        // create a new spannable string if none exists
        if (spannable == null) {
            spannable = SpannableStringBuilder.valueOf(text);
        }

        // add the rule to the spannable string
        addLinkToSpan(spannable, link);
    }

    /**
     * Find the link within the spannable text
     *
     * @param s    spannable text that we are adding the rule to.
     * @param link rule to add to the text.
     */
    private void addLinkToSpan(SpannableStringBuilder s, Link link) {
        // get the current text
        Pattern pattern = Pattern.compile(Pattern.quote(link.getText()));
        Matcher matcher = pattern.matcher(text);

        // find one or more links inside the text
        while (matcher.find()) {

            // find the start and end point of the linked text within the TextView
            int start = matcher.start();

            //int start = text.indexOf(link.getText());
            if (start >= 0) {
                int end = start + link.getText().length();
                applyLink(link, new Range(start, end), s);
            }

            // if we are only looking for the first occurrence of this pattern,
            // then quit now and don't look any further

            break;
        }
    }

    /**
     * Add the movement method to handle the clicks.
     */
    private void addLinkMovementMethod() {
        MovementMethod m = textView.getMovementMethod();

        if ((m == null) || !(m instanceof TouchableMovementMethod)) {
            if (textView.getLinksClickable()) {
                textView.setMovementMethod(TouchableMovementMethod.getInstance());
            }
        }
    }

    /**
     * Set the link rule to the spannable text.
     *
     * @param link  rule we are applying.
     * @param range the start and end point of the link within the text.
     * @param text  the spannable text to add the link to.
     */
    private void applyLink(Link link, Range range, SpannableStringBuilder text) {
        TouchableSpan[] existingSpans = text.getSpans(range.start, range.end, TouchableSpan.class);
        if (existingSpans.length == 0) {
            if (link.isUrlReplace()) {
                replaceUrlShow(link, range, text);
            } else {
                TouchableSpan span = new TouchableSpan(context, link);
                text.setSpan(span, range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            boolean newSpanConsumesAllOld = true;
            for (TouchableSpan span : existingSpans) {
                int start = spannable.getSpanStart(span);
                int end = spannable.getSpanEnd(span);
                if (range.start > start || range.end < end) {
                    newSpanConsumesAllOld = false;
                    break;
                } else {
                    text.removeSpan(span);
                }
            }

            if (newSpanConsumesAllOld) {
                TouchableSpan span = new TouchableSpan(context, link);
                text.setSpan(span, range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        if (link.getBgColor() != 0) {
            RadiusBackgroundSpan radiusBackgroundSpan = new RadiusBackgroundSpan(link);
            text.setSpan(radiusBackgroundSpan, range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }

    private void replaceUrlShow(Link link, Range range, SpannableStringBuilder text) {
        String replaceUrl = "网页链接";
        Drawable drawable = context.getResources().getDrawable(R.drawable.find_url);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        int bitmapUrlEnd = range.start + 1;
        text.setSpan(new VerticalImageSpan(drawable), range.start, bitmapUrlEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.replace(bitmapUrlEnd, range.end, replaceUrl);
        TouchableSpan span = new TouchableSpan(context, link);
        text.setSpan(span, bitmapUrlEnd, bitmapUrlEnd + replaceUrl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        加空格是为了文本信息 跟 span text 长度保持一致
        String replaceUrlSpace = " 网页链接";
        this.text = text.toString().replace(text.toString().substring(
                range.start,
                bitmapUrlEnd + replaceUrl.length()),
                replaceUrlSpace);
    }

    /**
     * Find the links that contain patterns and convert them to individual links.
     */
    private void turnPatternsToLinks() {
        int size = links.size();
        int i = 0;
        while (i < size) {
            if (links.get(i).getPattern() != null) {
                addLinksFromPattern(links.get(i));

                links.remove(i);
                size--;
            }
            i++;

        }
    }

    /**
     * Add the appended and prepended text to the links and apply it to the TextView
     * so that we can create the SpannableString.
     */
    private void applyAppendedAndPrependedText() {
        for (int i = 0; i < links.size(); i++) {
            Link link = links.get(i);

            if (link.getPrependedText() != null) {
                String totalText = link.getPrependedText() + " " + link.getText();

                text = TextUtils.replace(text, new String[]{link.getText()}, new CharSequence[]{totalText});
                links.get(i).setText(totalText);
            }

            if (link.getAppendedText() != null) {
                String totalText = link.getText() + " " + link.getAppendedText();

                text = TextUtils.replace(text, new String[]{link.getText()}, new CharSequence[]{totalText});
                links.get(i).setText(totalText);
            }
        }
    }

    /**
     * Convert the pattern to individual links.
     *
     * @param linkWithPattern pattern we want to match.
     */
    private void addLinksFromPattern(Link linkWithPattern) {
        Pattern pattern = linkWithPattern.getPattern();
        Matcher m = pattern.matcher(text);

        while (m.find()) {
            links.add(new Link(linkWithPattern).setText(text.subSequence(m.start(), m.end()).toString()));

            // if we are only looking for the first occurrence of this pattern,
            // then quit now and don't look any further
            if (findOnlyFirstMatch) {
                break;
            }
        }
    }

    /**
     * Manages the start and end points of the linked text.
     */
    private static class Range {
        public int start;
        public int end;

        public Range(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
}
