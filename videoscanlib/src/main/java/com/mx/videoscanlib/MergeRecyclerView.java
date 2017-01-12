package com.mx.videoscanlib;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

/**
 * 视频和图片显示List
 * Created by xff on 2017/1/12.
 */

public class MergeRecyclerView extends RecyclerView {
    List<MediaItem> mMediaItems;
    MergeAdapter mMergeAdapter;

    public MergeRecyclerView(Context context) {
        super(context);
        init();
    }

    public MergeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MergeRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    void init(){
            mMergeAdapter = new MergeAdapter();
            setAdapter(mMergeAdapter);
    }
    /**
     * 设置媒体信息
     *
     * @param mMediaItems
     */
    public void setMediaData(List<MediaItem> mMediaItems) {

        this.mMediaItems = mMediaItems;
        mMergeAdapter.notifyDataSetChanged();
    }

    private int width = 0;

    public void setItemWidth(int width) {
        this.width = width;
    }


   public class MergeAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_photo_item_layout, parent, false);

            if (v.getLayoutParams() != null) {
                ViewGroup.LayoutParams lp = v.getLayoutParams();
                lp.width = width;
                lp.height = width;
                v.setLayoutParams(lp);
            }
            v.setOnClickListener(this);
            return new MergeViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (holder instanceof MergeViewHolder) {
                MergeViewHolder mMergeViewHolder = (MergeViewHolder) holder;
                mMergeViewHolder.refresh(mMediaItems.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return mMediaItems == null ? 0 : mMediaItems.size();
        }

        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
            int adapter_pos = holder.getAdapterPosition();
            if (_OnItemClickListener != null) {
                _OnItemClickListener.onItemClick(MergeAdapter.this,adapter_pos);
            }
        }

       private OnCustomItemClickListener _OnItemClickListener;

       public void setOnItemClickListener(OnCustomItemClickListener listener) {
           _OnItemClickListener = listener;
       }

        class MergeViewHolder extends ViewHolder {
            //视频缩略图或者图片
            ImageView draft_thumbnail;
            TextView draft_duration;

            public MergeViewHolder(View itemView) {
                super(itemView);
                draft_thumbnail = (ImageView) itemView.findViewById(R.id.draft_thumbnail);
                draft_duration = (TextView) itemView.findViewById(R.id.draft_duration);
                itemView.setTag(this);
            }

            //刷新View
            public void refresh(MediaItem mediaItem) {
                File file = mediaItem.getImageFile();
                if (file != null) {
                    Glide.with(getContext())
                            .load(file)
                            .override(360, 360)
                            .into(draft_thumbnail);
                }

                long duration = mediaItem.getDuration();
                if (duration == 0) {
                    draft_duration.setVisibility(View.GONE);
                } else {
                    onMetaDataUpdate(draft_duration, duration);
                }

            }

            void onMetaDataUpdate(TextView view, long duration) {
                if (duration == 0) {
                    return;
                }

                int sec = Math.round((float) duration / 1000);
                int min = sec / 60;
                sec %= 60;
                view.setText(String.format(String.format("%d:%02d", min, sec)));
            }
        }
    }

    public interface OnCustomItemClickListener {
        boolean onItemClick(MergeAdapter adapter, int adapter_position);
    }
}
