package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ImagePickerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int IMAGE_PICKER_LOADER = 0;
    private static final String TAG = "ImagePickerActivity";
    ImageCursorAdapter mImageCursorAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    RelativeLayout mRelativeLayout;
    View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);
        setAdapter();
        emptyView.setVisibility(View.GONE);
    }

    private void setAdapter() {
        int spanCount = calculateNoOfColumns(120);
        mImageCursorAdapter = new ImageCursorAdapter(this);
        mLayoutManager = new GridLayoutManager(this, spanCount);
        mRecyclerView = findViewById(R.id.image_recycler_view);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, 2, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mImageCursorAdapter);

        emptyView = findViewById(R.id.empty_view_images);
        mRelativeLayout = findViewById(R.id.item_image_relative);

        LoaderManager.getInstance(this).initLoader(IMAGE_PICKER_LOADER, null, this);

    }

    public int calculateNoOfColumns(float columnWidthDp) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (screenWidthDp / columnWidthDp + 0.5);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.camera:

                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_ADDED
        };

        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";
        return new CursorLoader(
                this,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
        );
    }


    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mImageCursorAdapter.swapCursor(data);
        mRecyclerView.setVisibility((data.getCount() == 0) ? View.GONE : View.VISIBLE);
        emptyView.setVisibility((data.getCount() == 0) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mImageCursorAdapter.swapCursor(null);
    }
}