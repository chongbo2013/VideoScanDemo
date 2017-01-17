package com.mx.videoscanlib;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频和图片显示List
 * Created by xff on 2017/1/12.
 */

public class MergeRecyclerView extends RecyclerView {
    List<MediaBase> mMediaItems;
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

    void init() {
        mMergeAdapter = new MergeAdapter();
        setAdapter(mMergeAdapter);
    }

    /**
     * 设置媒体信息
     *
     * @param mMediaItems
     */
    public void setMediaData(VideoPhotoBaseActivity.Type type, List<MediaBase> mMediaItems) {

        this.mMediaItems = mMediaItems;
        mMergeAdapter.setType(type);
        mMergeAdapter.notifyDataSetChanged();
    }

    private int width = 0;

    public void setItemWidth(int width) {
        this.width = width;
    }


    public class MergeAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener {
        VideoPhotoBaseActivity.Type type = VideoPhotoBaseActivity.Type.VIDEO;
        //是否选择模式
        boolean isSelectMode = true;
        //选中的视频或者图片
        List<Integer> selectItems = new ArrayList<>();

        boolean isClickEffectMode = true;

        public void setType(VideoPhotoBaseActivity.Type type) {
            this.type = type;
        }

        public void enableSelectMode(boolean enable) {
            isSelectMode = enable;
        }

        public void enableClickEffectMode(boolean enable) {
            isClickEffectMode = enable;
        }

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
                mMergeViewHolder.refresh(position);
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

            if (isSelectMode) {
                if (selectItems.contains(adapter_pos)) {
                    int index = selectItems.indexOf(adapter_pos);
                    selectItems.remove(index);
                } else {

                    if(selectItems.size()>=maxSelect) {
                        Toast.makeText(getContext(), "最多选中5张图片", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    selectItems.add(adapter_pos);
                }
                notifyItemChanged(adapter_pos);
                if (_OnItemClickListener != null) {
                    _OnItemClickListener.onSelectCallBack(selectItems);
                }
//                notifyDataSetChanged();
            }

            if (_OnItemClickListener != null) {
                _OnItemClickListener.onItemClick(MergeAdapter.this, adapter_pos);
            }
        }

        private OnCustomItemClickListener _OnItemClickListener;

        public void setOnItemClickListener(OnCustomItemClickListener listener) {
            _OnItemClickListener = listener;
        }
        int maxSelect=5;
        public void setMaxSelect(int i) {
            this.maxSelect=i;
        }

        class MergeViewHolder extends ViewHolder {
            //视频缩略图或者图片
            ImageView draft_thumbnail;
            TextView draft_duration;
            ImageView iv_select;
            View select_bg;

            public MergeViewHolder(View itemView) {
                super(itemView);
                draft_thumbnail = (ImageView) itemView.findViewById(R.id.draft_thumbnail);
                draft_duration = (TextView) itemView.findViewById(R.id.draft_duration);
                iv_select = (ImageView) itemView.findViewById(R.id.iv_select);
                select_bg = itemView.findViewById(R.id.select_bg);
                itemView.setTag(this);
            }

            //刷新View
            public void refresh(int position) {
                if (type == VideoPhotoBaseActivity.Type.VIDEO) {
                    MediaItem mediaItem = (MediaItem) mMediaItems.get(position);
                    File file = mediaItem.getImageFile();
                    if (file != null) {
                        Glide.with(getContext())
                                .load(file)
                                .override(120, 120)
                                .into(draft_thumbnail);
                    }

                    long duration = mediaItem.getDuration();
                    if (duration == 0) {
                        draft_duration.setVisibility(View.GONE);
                    } else {
                        onMetaDataUpdate(draft_duration, duration);
                    }

                } else {
                    //照片显示多选状态
                    PhotoMediaItem mediaItem = (PhotoMediaItem) mMediaItems.get(position);
                    File file = mediaItem.getImageFile();
                    if (file != null) {
                        Glide.with(getContext())
                                .load(file)
                                .override(120, 120)
                                .into(draft_thumbnail);
                    }
                    draft_duration.setVisibility(View.GONE);
                }
                //选择模式
                if (isSelectMode) {
                    iv_select.setVisibility(View.VISIBLE);
                    if (selectItems.contains(position)) {
                        select_bg.setVisibility(View.VISIBLE);
                        iv_select.setBackgroundResource(R.drawable.mx_btn_selected);
                    } else {
                        select_bg.setVisibility(View.GONE);
                        iv_select.setBackgroundResource(R.drawable.mx_btn_unselected);
                    }
                } else {
                    iv_select.setVisibility(View.GONE);
                    select_bg.setVisibility(View.GONE);
                }

                if (itemView instanceof PressedFrameLayout) {
                    PressedFrameLayout pressedFrameLayout = (PressedFrameLayout) itemView;
                    pressedFrameLayout.setEnableEffect(type == VideoPhotoBaseActivity.Type.VIDEO ? true : false);
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

        void onSelectCallBack(List<Integer> selectItems );
    }
}
