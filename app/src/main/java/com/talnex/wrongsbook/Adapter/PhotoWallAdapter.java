package com.talnex.wrongsbook.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.talnex.wrongsbook.R;
import com.talnex.wrongsbook.Utils.DisplayUtil;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;


public class PhotoWallAdapter extends RecyclerView.Adapter<PhotoWallAdapter.MyViewHolder> {

    Context mContext;
    List<String> mDatas;
    int mMaxNum;
    LayoutInflater mInflater;
    boolean mIsDelete = false;
    OnItemClickListener mOnItemClickListener;

    public PhotoWallAdapter(Context context, List<String> datas, int maxNum) {
        mContext = context;
        mDatas = datas;
        mMaxNum = maxNum;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.item_recycler, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (mDatas.size() < mMaxNum) {
            if (mDatas.size() == 0) {
                holder.mIvAddPhoto.setVisibility(View.VISIBLE);
                holder.mIvDelete.setVisibility(View.GONE);
                holder.mIvDisPlayItemPhoto.setVisibility(View.GONE);
                holder.mIvUploadingBg.setVisibility(View.GONE);

            } else {
                //判断是不是最后一张，最后一第为添加的图片
                if (position < mDatas.size()) {
                    //获取路径
                    String filePath = mDatas.get(position);
                    holder.mIvAddPhoto.setVisibility(View.GONE);
                    holder.mIvDisPlayItemPhoto.setVisibility(View.VISIBLE);
                    Picasso.with(mContext).load(new File(filePath)).centerCrop().resize(DisplayUtil.dip2px(mContext, 120), DisplayUtil.dip2px(mContext, 120))
                            .error(R.mipmap.pictures_no).into(holder.mIvDisPlayItemPhoto);

                    if (mIsDelete) {
                        holder.mIvDelete.setVisibility(View.VISIBLE);
                        holder.mIvUploadingBg.setVisibility(View.VISIBLE);
                    } else {
                        holder.mIvDelete.setVisibility(View.GONE);
                        holder.mIvUploadingBg.setVisibility(View.GONE);
                    }

                } else {

                    holder.mIvAddPhoto.setVisibility(View.VISIBLE);
                    holder.mIvDelete.setVisibility(View.GONE);
                    holder.mIvDisPlayItemPhoto.setVisibility(View.GONE);
                    holder.mTvProgress.setVisibility(View.GONE);
                    holder.mIvError.setVisibility(View.GONE);
                    holder.mIvUploadingBg.setVisibility(View.GONE);

                }

            }

        } else {
            String filePath = (String) mDatas.get(position);
            holder.mIvDisPlayItemPhoto.setVisibility(View.VISIBLE);
            holder.mIvAddPhoto.setVisibility(View.GONE);

            Picasso.with(mContext).load(new File(filePath)).centerCrop().resize(DisplayUtil.dip2px(mContext, 120), DisplayUtil.dip2px(mContext, 120))
                    .error(R.mipmap.pictures_no).into(holder.mIvDisPlayItemPhoto);
            if (mIsDelete) {
                holder.mIvDelete.setVisibility(View.VISIBLE);
                holder.mIvUploadingBg.setVisibility(View.VISIBLE);
            } else {
                holder.mIvDelete.setVisibility(View.GONE);
                holder.mIvUploadingBg.setVisibility(View.GONE);
            }

        }

    }

    @Override
    public int getItemCount() {
        if (mDatas == null || mDatas.size() == 0) {
            return 1;
        } else if (mDatas.size() < mMaxNum) {
            return mDatas.size() + 1;
        } else {
            return mDatas.size();
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivDisPlayItemPhoto)
        ImageView mIvDisPlayItemPhoto;
        @BindView(R.id.ivAddPhoto)
        ImageView mIvAddPhoto;
        @BindView(R.id.ivUploadingBg)
        ImageView mIvUploadingBg;
        @BindView(R.id.ivError)
        ImageView mIvError;
        @BindView(R.id.tvProgress)
        TextView mTvProgress;
        @BindView(R.id.ivDelete)
        ImageView mIvDelete;
        @BindView(R.id.rootView)
        View mRootView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //initListener(itemView);
        }

        private void initListener(View itemView) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemLongClick(v, getAdapterPosition());
                    }
                    return true;
                }
            });
        }


        @OnClick({R.id.ivAddPhoto, R.id.ivDelete, R.id.ivDisPlayItemPhoto})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivAddPhoto:
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, getAdapterPosition());
                    }
                    break;
                case R.id.ivDelete:
                    //删除
                    mDatas.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    break;
                case R.id.ivDisPlayItemPhoto:
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, getAdapterPosition());
                    }
                    break;
            }
        }

        @OnLongClick({R.id.ivDisPlayItemPhoto})
        public boolean onLongClick(View v) {
            if (v.getId() == R.id.ivDisPlayItemPhoto) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemLongClick(v, getAdapterPosition());
                }
            }
            return true;
        }


    }


    public interface OnItemClickListener {

        void onItemClick(View v, int position);

        void onItemLongClick(View v, int position);

    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setIsDelete(boolean isDelete) {
        mIsDelete = isDelete;
    }

}
