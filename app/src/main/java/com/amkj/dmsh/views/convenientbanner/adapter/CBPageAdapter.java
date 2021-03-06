package com.amkj.dmsh.views.convenientbanner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amkj.dmsh.R;
import com.amkj.dmsh.views.JzVideo.JzVideoPlayerProduct;
import com.amkj.dmsh.views.convenientbanner.holder.CBViewHolderCreator;
import com.amkj.dmsh.views.convenientbanner.holder.Holder;
import com.amkj.dmsh.views.convenientbanner.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.jzvd.Jzvd;

/**
 * Created by Sai on 15/7/29.
 */
public class CBPageAdapter<T> extends RecyclerView.Adapter<Holder> {
    private List<T> datas = new ArrayList<>();
    private CBViewHolderCreator creator;
    private CBPageAdapterHelper helper;
    private boolean canLoop;
    private OnItemClickListener onItemClickListener;


    public CBPageAdapter(CBViewHolderCreator creator, List<T> datas, boolean canLoop) {
        this.creator = creator;
        this.datas.addAll(datas);
        this.canLoop = canLoop;
        helper = new CBPageAdapterHelper();
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = creator.getLayoutId();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        helper.onCreateViewHolder(parent, itemView);
        return creator.createHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        helper.onBindViewHolder(holder.itemView, position, getItemCount());
        int realPosition = position % datas.size();
        //防止数组下标越界
        if (realPosition >= datas.size()) realPosition = datas.size() - 1;
        holder.updateUI(datas.get(realPosition));

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new OnPageClickListener(realPosition));
        }
    }

    @Override
    public int getItemCount() {
        //根据模式决定长度
        if (datas == null || datas.size() == 0) return 0;
        return canLoop ? 3 * datas.size() : datas.size();
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
    }

    public int getRealItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public boolean isCanLoop() {
        return canLoop;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class OnPageClickListener implements View.OnClickListener {
        private int position;

        public OnPageClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(position);
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull Holder holder) {
        super.onViewDetachedFromWindow(holder);
        //item不可见时暂停视频播放
        JzVideoPlayerProduct jzVideo = holder.itemView.findViewById(R.id.jvp_ad_video_play);
        if (jzVideo != null && jzVideo.state == Jzvd.STATE_PLAYING) {
            jzVideo.startButton.performClick();
        }
    }
}
