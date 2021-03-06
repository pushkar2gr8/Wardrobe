package com.crowdfire.wardrobe;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class ImageLoaderAdapter extends RecyclerView.Adapter<ImageLoaderAdapter.ImageViewHolder>{

    private ArrayList<String> imageList;
    private Context mCtx;


    public ImageLoaderAdapter(Context mCtx, ArrayList<String> imageList) {
        this.imageList = imageList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.imageview,null);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Random rnd = new Random();
        int i = rnd.nextInt(imageList.size());

        Picasso.get()
                .load(new File(imageList.get(i)))
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(holder.img);

        //holder.img.setImageURI(Uri.parse(imageList.get(position)));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder{

        ImageView img;

        public ImageViewHolder(View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.user_data);
        }
    }
}
