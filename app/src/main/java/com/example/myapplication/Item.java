package com.example.myapplication;

import android.graphics.Bitmap;

public class Item {
    private Bitmap mBitmap;
    private String mFileName;

    public Item(Bitmap bitmap, String fileName) {
        mBitmap = bitmap;
        mFileName = fileName;
    }

    public Bitmap getImageBitmap() {
        return mBitmap;
    }

    public String getFileName() {
        return mFileName;
    }

}