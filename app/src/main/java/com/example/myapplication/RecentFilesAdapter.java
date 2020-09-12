package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.example.myapplication.MainActivity.toast;

public class RecentFilesAdapter extends RecyclerView.Adapter<RecentFilesAdapter.RecentFilesViewHolder> {
    ArrayList<Item> items;
    Context context;

    public RecentFilesAdapter(ArrayList<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }


    @NonNull
    @Override
    public RecentFilesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_list_view, parent, false);
        return new RecentFilesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecentFilesViewHolder holder, final int position) {
        Item currentItem = items.get(position);
        final int currentPosition = holder.getAdapterPosition();

        holder.fileImage.setImageBitmap(currentItem.getImageBitmap());
        holder.fileName.setText(currentItem.getFileName());

        holder.itemMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.itemMenu);
                popupMenu.inflate(R.menu.item_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.share:
                                if (toast != null) {
                                    toast.cancel();
                                }
                                toast = Toast.makeText(context, "Share Clicked " + currentPosition, Toast.LENGTH_SHORT);
                                toast.show();
                                return true;
                            case R.id.file_info:
                                if (toast != null) {
                                    toast.cancel();
                                }
                                toast = Toast.makeText(context, "File Info...", Toast.LENGTH_SHORT);
                                toast.show();
                                return true;
                            case R.id.remove:
                                items.remove(currentPosition);
                                notifyItemRemoved(currentPosition);
                                notifyItemRangeChanged(currentPosition, getItemCount());
                                if (toast != null) {
                                    toast.cancel();
                                }
                                toast = Toast.makeText(context, "Item Removed " + position, Toast.LENGTH_SHORT);
                                toast.show();
                                return true;
                            case R.id.delete:
                                items.remove(position);
                                notifyItemRemoved(position);
                                notifyItemChanged(position);
                                if (toast != null) {
                                    toast.cancel();
                                }
                                toast = Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT);
                                toast.show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class RecentFilesViewHolder extends RecyclerView.ViewHolder {

        public ImageView fileImage;
        public TextView fileName;
        public ImageView itemMenu;

        public RecentFilesViewHolder(@NonNull View itemView) {

            super(itemView);

            fileImage = itemView.findViewById(R.id.image);
            fileName = itemView.findViewById(R.id.filename);
            itemMenu = itemView.findViewById(R.id.item_menu);

        }
    }
}
