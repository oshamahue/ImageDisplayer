package com.oshamahue.imagedisplayer.adapter;

import android.content.Context;
import android.net.http.HttpResponseCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.oshamahue.imagedisplayer.R;
import com.oshamahue.imagedisplayer.models.Photo;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Hadi on 12/10/2014.
 */
public class FlickrAdapter extends ArrayAdapter<Photo> {
    private final List<Photo> objects;
    private final Picasso picasso;

    public FlickrAdapter(Context context, int resource, List<Photo> objects) {
        super(context, resource, objects);
        this.objects = objects;
        OkHttpClient okHttpClient = new OkHttpClient();
        picasso = new Picasso.Builder(context)
                .downloader(new OkHttpDownloader(okHttpClient))
                .loggingEnabled(true)
                .build();
        File httpCacheDir = new File(context.getExternalCacheDir(), "http");
        long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
        try {
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Photo getItem(int position) {
        return objects.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.view_grid_image, null);

        }
        final Photo photo = getItem(position);
        String url = getContext().getString(R.string.flikr_image_url, photo.getFarm(), photo.getServer(), photo.getId(), photo.getSecret());
        picasso.with(getContext()).load(url).placeholder(R.drawable.user_placeholder).into((ImageView) convertView);

        return convertView;

    }

    public void add(List<Photo> photos) {
        objects.addAll(photos);
        notifyDataSetChanged();
    }


}
