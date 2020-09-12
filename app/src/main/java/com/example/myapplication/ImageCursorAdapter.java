package com.example.myapplication;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class ImageCursorAdapter extends RecyclerView.Adapter<ImageCursorAdapter.ImageViewHolder> {
    public static final String TAG = "ImageCursorAdapter";
    private static int width;
    private Cursor mCursor;
    private Context mContext;

    public ImageCursorAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Log.e(TAG, "in onBindViewHolder");
        Glide.with(mContext)
                .load(getUriFromMediaStore(position))
                .placeholder(new ColorDrawable(Color.GRAY))
                //.override(96, 96)
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return (mCursor != null) ? mCursor.getCount() : 0;
    }

    public void swapCursor(Cursor cursor) {
        Cursor oldCursor = changeCursor(cursor);
        if (oldCursor != null) {
            oldCursor.close();
        }
    }

    private Cursor changeCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        mCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    private Uri getUriFromMediaStore(int position) {
        int columnIndexId = mCursor.getColumnIndex(MediaStore.Images.Media._ID);
        mCursor.moveToPosition(position);
        long id = mCursor.getLong(columnIndexId);
        return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
        }
    }
}