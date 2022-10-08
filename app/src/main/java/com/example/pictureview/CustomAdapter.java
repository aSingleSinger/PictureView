package com.example.pictureview;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<Bitmap> pictures;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_view_item, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        Uri uri = Uri.fromFile(new File(pictures[position]));
//        Bitmap bitmap = MediaStore.Images.Media.getBitmap()
        holder.getPicture().setImageBitmap(pictures.get(position));

    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView picture;


        public ViewHolder(View v) {
            super(v);

            picture = v.findViewById(R.id.pictureContainer);
        }

        public ImageView getPicture() {
            return picture;
        }


    }

    public CustomAdapter(List<Bitmap> pictures) {
        this.pictures = pictures;
    }

}
