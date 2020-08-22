package com.example.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {
    public static int REQUEST_IMAGE_CAPTURE = 1;
    static Toast toast = null;
    ExampleAdapter mAdapter;
    ArrayList<Item> items;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    Bitmap mImageBitmap;
    String mFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAdapter();
        Environment.getExternalStorageState();
        setAddButton();
    }

    private void itemAdded() {
        items.add(0, new Item(mImageBitmap, mFileName));
        mAdapter.notifyItemInserted(0);
        mAdapter.notifyDataSetChanged();
    }

    private void setAddButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(MainActivity.this, "Fab Clicked", LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void setAdapter() {
        items = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(items, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove_all:
                items.clear();
                mAdapter.notifyDataSetChanged();
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(MainActivity.this, "All Items Removed", LENGTH_SHORT);
                toast.show();
                break;
            case R.id.camera:
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                    dispatchTakePictureIntent();
                } else {
                    if (toast != null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(MainActivity.this, "You don't have any Camera", LENGTH_SHORT);
                    toast.show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = null;
            if (data != null) {
                extras = data.getExtras();
            }
            if (extras != null) {
                mImageBitmap = (Bitmap) extras.get("data");
            }
            setFileName();
            itemAdded();
        }
    }

    private void setFileName() {
        mFileName = new SimpleDateFormat("ddMMyyyy_hhmmss", Locale.US).format(new Date());
    }
}