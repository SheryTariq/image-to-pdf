package com.example.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AutoFitGridRecyclerView extends RecyclerView {
    public GridLayoutManager gridLayoutManager;
    private static final String TAG = "AutoFitGridRecyclerView";
    private int columnWidth = -1;
    ItemDecoration itemDecoration = new ItemDecoration() {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull State state) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            assert layoutManager != null;
            int span = layoutManager.getSpanCount();
            int position = parent.getChildAdapterPosition(view);
            int column = position % span;
            outRect.left = column * 2 / span;
            outRect.right = 2 - (column + 1) * 2 / span;
            if (position >= span) {
                outRect.top = 2; // item top
            }
        }
    };

    public AutoFitGridRecyclerView(@NonNull Context context) {
        super(context);
        initialization(context, null);
    }

    public AutoFitGridRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialization(context, attrs);
    }

    public AutoFitGridRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialization(context, attrs);
    }

    @Override
    public void addItemDecoration(@NonNull ItemDecoration decor) {
        super.addItemDecoration(itemDecoration);
    }


    private void initialization(Context context, AttributeSet attrs) {
        try {
            if (attrs != null) {
                // list the attributes we want to fetch
                int[] attrsArray = {
                        android.R.attr.columnWidth
                };
                TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
                //retrieve the value of the 0 index, which is columnWidth
                columnWidth = array.getDimensionPixelSize(0, -1);
                array.recycle();
            }
            gridLayoutManager = new GridLayoutManager(context, 1);
            setLayoutManager(gridLayoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        try {
            if (columnWidth > 0) {
                int spanCount = Math.max(1, getMeasuredWidth() / columnWidth);
                gridLayoutManager.setSpanCount(spanCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
