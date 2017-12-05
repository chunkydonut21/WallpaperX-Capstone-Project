package com.example.shivammaheshwari.wallpaperx.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shivammaheshwari.wallpaperx.R;
import com.example.shivammaheshwari.wallpaperx.activity.DetailActivity;
import com.example.shivammaheshwari.wallpaperx.data.WallpaperContract;
import com.example.shivammaheshwari.wallpaperx.model.Walls;

public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.TaskViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public CustomCursorAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.cursor_layout, parent, false);

        return new TaskViewHolder(view);
    }


    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {


        int idColumnIndex = mCursor.getColumnIndex(WallpaperContract.ImagesContract._ID);
        int imgWidth = mCursor.getColumnIndex(WallpaperContract.ImagesContract.IMAGE_WIDTH);
        int imgHeight = mCursor.getColumnIndex(WallpaperContract.ImagesContract.IMAGE_HEIGHT);
        int imgPhoto = mCursor.getColumnIndex(WallpaperContract.ImagesContract.IMAGEURL);
        int imgPic = mCursor.getColumnIndex(WallpaperContract.ImagesContract.IMG_PAGEURL);
        int thumbnailPic = mCursor.getColumnIndex(WallpaperContract.ImagesContract.IMG_THUMBNAIL);
        int imgId = mCursor.getColumnIndex(WallpaperContract.ImagesContract.IMAGE_ID);

        mCursor.moveToPosition(position);

        final int id = mCursor.getInt(idColumnIndex);
        final String mWidth = mCursor.getString(imgWidth);
        final String mHeight = mCursor.getString(imgHeight);
        final String thumbUrl = mCursor.getString(thumbnailPic);
        final String mImageUrl = mCursor.getString(imgPhoto);
        final String pageUrl = mCursor.getString(imgPic);
        final String mImageId = mCursor.getString(imgId);


        final Walls wallsParcel = new Walls(mImageId, mWidth, mHeight, mImageUrl, thumbUrl, pageUrl);

        Glide.with(mContext)
                .load(thumbUrl)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_placeholder)
                        .centerCrop()
                        .error(R.drawable.ic_error))
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), DetailActivity.class);

                intent.putExtra("ParcelWallpapers", wallsParcel);

                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


    // Inner class for creating ViewHolders
    class TaskViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public TaskViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageFavourite);

        }
    }
}