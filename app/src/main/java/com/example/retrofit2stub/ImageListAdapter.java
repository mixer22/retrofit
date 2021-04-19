package com.example.retrofit2stub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageListAdapter extends BaseAdapter {
    Context context;
    Hit[] hits;
    Picasso picasso;
    public ImageListAdapter(Context context, Hit[] hits){
        this.context = context;
        this.hits = hits;
        picasso = new Picasso.Builder(context).build();
    }

    @Override
    public int getCount() {
        return hits.length;
    }

    @Override
    public Object getItem(int i) {
        return hits[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.image_list_item, viewGroup,false);
        ImageView imageView = view.findViewById(R.id.imageView);
        picasso.load(hits[i].previewURL).into(imageView);
        return view;
    }
}
