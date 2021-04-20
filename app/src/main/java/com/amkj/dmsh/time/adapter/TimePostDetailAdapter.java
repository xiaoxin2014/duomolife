package com.amkj.dmsh.time.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.CommonPagerAdapter;
import com.amkj.dmsh.base.MyPagerAdapter;
import com.amkj.dmsh.base.ViewHolder;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dao.BaiChuanDao;
import com.amkj.dmsh.dao.SoftApiDao;
import com.amkj.dmsh.dominant.bean.PostCommentEntity.PostCommentBean;
import com.amkj.dmsh.find.bean.PostEntity.PostBean;
import com.amkj.dmsh.find.bean.RelatedGoodsBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.views.TextViewSuffixWrapper;
import com.amkj.dmsh.views.alertdialog.TimePostCommentPw;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.showImageActivity;
import static com.amkj.dmsh.constant.ConstantVariable.COMMENT_GROUP_TYPE;
import static com.amkj.dmsh.dao.AddClickDao.addTimePostClick;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.IMAGE_DEF;

/**
 * Created by xiaoxin on 2020/9/30
 * Version:v4.8.0
 * ClassDescription :团购商品-种草详情列表适配器
 */
public class TimePostDetailAdapter extends BaseQuickAdapter<PostBean, BaseViewHolder> {

    private final BaseActivity context;

    public TimePostDetailAdapter(BaseActivity baseActivity, @Nullable List<PostBean> data) {
        super(R.layout.item_time_post_detail, data);
        context = baseActivity;
    }

    @Override
    protected void convert(BaseViewHolder helper, PostBean item) {
        if (item == null) return;
        List<PostCommentBean> commList = item.getCommList();
        helper.setText(R.id.tv_like, item.getFavorNum() > 0 ? String.valueOf(item.getFavorNum()) : "赞")
                .setText(R.id.tv_comment, item.getComment() > 0 ? String.valueOf(item.getComment()) : "评论")
                .setGone(R.id.ll_comment, commList.size() > 0);

        //点赞
        TextView tvLike = helper.getView(R.id.tv_like);
        tvLike.setSelected(item.isFavor());
        helper.getView(R.id.tv_like).setOnClickListener(v -> SoftApiDao.favorPost(context, item, tvLike, COMMENT_GROUP_TYPE));

        //评论
        helper.getView(R.id.et_comment).setOnClickListener(v -> lookAllComment(item));
        helper.getView(R.id.tv_comment).setOnClickListener(v -> lookAllComment(item));

        //分享
        helper.getView(R.id.iv_share).setOnClickListener(v -> new UMShareAction(context
                , item.getCover()
                , item.getTitle()
                , item.getContent()
                , Url.BASE_SHARE_PAGE_TWO + "limit_time_template/article_info.html?id=" + item.getId(), item.getId()));

        //显示关联商品
        List<RelatedGoodsBean> productList = item.getProductList();
        ViewPager vpRelated = helper.getView(R.id.vp_related);
        if (productList != null && productList.size() > 0) {
            vpRelated.setVisibility(View.VISIBLE);
            vpRelated.setAdapter(new CommonPagerAdapter<RelatedGoodsBean>(context, productList, R.layout.item_time_post_related) {
                @Override
                public void convert(ViewHolder helper, int position, RelatedGoodsBean item) {
                    if (item == null) return;
                    GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_cover), item.getPictureUrl());
                    helper.setText(R.id.tv_title, item.getTitle());
                    helper.setText(R.id.tv_price, getStringsChNPrice(context, item.getPrice()));
                    helper.setText(R.id.tv_market_price, getStringsChNPrice(context, item.getMarketPrice()));
                    TextView tvMarketPrice = helper.getView(R.id.tv_market_price);
                    tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    tvMarketPrice.getPaint().setAntiAlias(true);
                    helper.itemView.setOnClickListener(v -> {
                        BaiChuanDao.skipAliBC(context, item.getThirdUrl(), "");
                        //统计点击量
                        addTimePostClick(context, item.getId(), 2);
                    });
                }
            });
            vpRelated.setPageMargin(AutoSizeUtils.mm2px(mAppContext, 22));
            vpRelated.setOffscreenPageLimit(productList.size() - 1);
        } else {
            vpRelated.setVisibility(View.GONE);
        }

        //显示图片
        String images = item.getImgs();
        TextView tvImgNum = helper.getView(R.id.tv_img_num);
        if (!TextUtils.isEmpty(images)) {
            ViewPager viewPager = helper.getView(R.id.viewPager);
            String[] split = images.split(",");
            tvImgNum.setVisibility(split.length > 1 ? View.VISIBLE : View.GONE);
            List<View> viewlist = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                ImageView imageView = new ImageView(context);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoSizeUtils.mm2px(context, 750));
                imageView.setLayoutParams(layoutParams);
                GlideImageLoaderUtil.loadSquareImg(context, imageView, split[i], "", AutoSizeUtils.mm2px(context, 750));
                int finalI = i;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showImageActivity(context, IMAGE_DEF, finalI, Arrays.asList(split));
                    }
                });
                viewlist.add(imageView);
            }
            tvImgNum.setText((1 + "/" + split.length));
            viewPager.setAdapter(new MyPagerAdapter(viewlist));
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    tvImgNum.setText((position + 1 + "/" + split.length));
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            tvImgNum.setVisibility(View.GONE);
        }

        //显示评论内容
        String description = item.getContent();
        if (!TextUtils.isEmpty(description)) {
            TextView mTvContent = helper.getView(R.id.tv_content);
            mTvContent.setText(getStrings(description));
            TextViewSuffixWrapper wrapper = new TextViewSuffixWrapper(mTvContent);
            wrapper.setMainContent(description);
            wrapper.setTargetLineCount(5);
            String suffix = "...查看全文";
            wrapper.setSuffix(suffix);
            wrapper.suffixColor("...".length(), suffix.length(), R.color.text_login_blue_z, v -> wrapper.toggle());
            wrapper.collapse(false);
        }

        //显示评论
        RecyclerView rvComment = helper.getView(R.id.rv_comment);
        rvComment.setLayoutManager(new LinearLayoutManager(context));
        BaseQuickAdapter<PostCommentBean, BaseViewHolder> baseQuickAdapter = new BaseQuickAdapter<PostCommentBean, BaseViewHolder>(R.layout.item_time_post_comment, item.getCommList().subList(0, Math.min(item.getCommList().size(), 2))) {
            @Override
            protected void convert(BaseViewHolder helper, PostCommentBean item) {
                if (item == null) return;
                String text = item.getNickname() + ":" + item.getContent();
                ((TextView) helper.itemView).setText(getSpannableString(text, 0, item.getNickname().length() + 1, -1, "#FF2F6A9F"));
            }
        };
        rvComment.setAdapter(baseQuickAdapter);
        TextView tvAll = helper.getView(R.id.tv_all_comment);
        tvAll.setText(getIntegralFormat(context, R.string.look_all_commnet, item.getComment()));
        tvAll.setVisibility(item.getComment() > 2 ? View.VISIBLE : View.GONE);

        //查看全部评论
        tvAll.setOnClickListener(v -> lookAllComment(item));
    }

    private void lookAllComment(PostBean item) {
        new XPopup.Builder(context)
                .hasShadowBg(false)
                .navigationBarColor(mContext.getResources().getColor(R.color.white))
                .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                .enableDrag(true)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asCustom(new TimePostCommentPw(context, item, COMMENT_GROUP_TYPE))
                .show();
    }
}
