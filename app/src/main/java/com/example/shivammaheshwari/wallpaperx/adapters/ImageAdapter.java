package com.example.shivammaheshwari.wallpaperx.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shivammaheshwari.wallpaperx.R;
import com.example.shivammaheshwari.wallpaperx.activity.DetailActivity;
import com.example.shivammaheshwari.wallpaperx.model.Walls;
import com.example.shivammaheshwari.wallpaperx.widget.WidgetService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageAdapterHolder> {
    private List<Walls> mWalls;
    private Context mContext;

    public ImageAdapter(Context context, List<Walls> walls) {
        mWalls = walls;
        mContext = context;
    }

    @Override
    public ImageAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layoutIdForListItem = R.layout.activity_display;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutIdForListItem, parent, false);

        final ImageAdapterHolder viewHolder = new ImageAdapterHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageAdapterHolder holder, int position) {

        final Walls walls = mWalls.get(position);
        String image = walls.getThumbnailUrl();

        Picasso.with(mContext)
                .load(image)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), DetailActivity.class);

                intent.putExtra("ParcelWallpapers", walls);
                v.getContext().startActivity(intent);
            }
        });

        //displaying widget
        final ArrayList<String> widgetList = new ArrayList<>();

        for (int i = 0; i < mWalls.size(); i++) {

            String mId = mWalls.get(i).getmId();
            String height = mWalls.get(i).getHeight();
            String width = mWalls.get(i).getWidth();
            String downloadUrl = mWalls.get(i).getImageUrl();

            //display only 10 widgets at max
            if (i == 10) {
                break;
            }

            widgetList.add("ID " + mId + "\n" + "Width: " + width + "      Height: " + height + "\n" + "URL: " + downloadUrl);

            WidgetService.startBakingService(mContext, widgetList);

        }




    }

    @Override
    public int getItemCount() {
        return mWalls.size();
    }

    public class ImageAdapterHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ImageAdapterHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.wallpaper_preview);

        }
    }

    public void setWallpaper(List<Walls> wallpaper) {
        mWalls = wallpaper;
        notifyDataSetChanged();
    }


}
