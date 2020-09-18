package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {
    private static final int EXTERNAL_STORAGE_PERMISSION_CODE = 0;
    private static final String TAG = "MainActivity";
    public static int REQUEST_IMAGE_CAPTURE = 1;
    static Toast toast = null;
    RecentFilesAdapter mAdapter;
    ArrayList<Item> items;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    Bitmap mImageBitmap;
    String mFileName;
    View emptyView;
    long lastPress;
    Toast backPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAdapter();
        setAddButton();
        setVisibility();
    }

    private void itemAdded() {
        items.add(0, new Item(mImageBitmap, mFileName));
        mAdapter.notifyItemInserted(0);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibility();
    }

    private void setAddButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(MainActivity.this, ImagePickerActivity.class);
                        startActivity(intent);
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
                    }
                } else {
                    Intent intent = new Intent(MainActivity.this, ImagePickerActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void setAdapter() {
        items = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecentFilesAdapter(items, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        emptyView = findViewById(R.id.empty_view_files);
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
                setVisibility();
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
        mFileName = "IMG_";
        mFileName += new SimpleDateFormat("yyyMMdd_HHmmss", Locale.US).format(new Date());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MainActivity.this, ImagePickerActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPress > 5000) {
            backPress = Toast.makeText(this, "Press back again to leave", Toast.LENGTH_SHORT);
            backPress.show();
            lastPress = currentTime;
        } else {
            if (backPress != null) backPress.cancel();
            super.onBackPressed();
        }
    }

    public void setVisibility() {
        mRecyclerView.setVisibility((items.isEmpty()) ? View.GONE : View.VISIBLE);
        emptyView.setVisibility((items.isEmpty()) ? View.VISIBLE : View.GONE);
    }
}