package com.oshamahue.imagedisplayer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.oshamahue.imagedisplayer.adapter.FlickrAdapter;
import com.oshamahue.imagedisplayer.models.FlickrApiResponse;
import com.oshamahue.imagedisplayer.models.Photo;
import com.oshamahue.imagedisplayer.util.JacksonConverter;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;


public class MainActivity extends ActionBarActivity {

    FlickrApi flickrApi;
    int page = 0;
    @InjectView(R.id.gridView)
    GridView mGridView;
    ProgressDialog progress;
    final static String MORE_TEXT = "more";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.flickr.com")
                .setClient(new OkClient(new OkHttpClient()))
                .setConverter(new JacksonConverter())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        flickrApi = restAdapter.create(FlickrApi.class);
        loadPhotos();

    }

    private void loadPhotos() {
        progress = ProgressDialog.show(this, "Loading", "please wait", true);
        flickrApi.getNaturePhotos(page, new Callback<FlickrApiResponse>() {
            @Override
            public void success(FlickrApiResponse flickrApiResponse, Response response) {
                progress.dismiss();
                if (mGridView.getAdapter() == null) {
                    final FlickrAdapter adapter = new FlickrAdapter(MainActivity.this, 0, flickrApiResponse.getPhotos().getPhotos());
                    mGridView.setAdapter(adapter);
                    mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            final Photo photo = adapter.getItem(position);
                            Dialog dialog = new Dialog(MainActivity.this);
                            ImageView imageView = new ImageView(MainActivity.this);
                            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            String url = getString(R.string.flikr_image_url, photo.getFarm(), photo.getServer(), photo.getId(), photo.getSecret());
                            dialog.setContentView(imageView);
                            Picasso.with(MainActivity.this).load(url).into(imageView);
                            dialog.show();
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            Window window = dialog.getWindow();
                            lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                            window.setAttributes(lp);

                        }
                    });
                } else {
                    ((FlickrAdapter) mGridView.getAdapter()).add(flickrApiResponse.getPhotos().getPhotos());
                }
                page++;
            }

            @Override
            public void failure(RetrofitError error) {
                progress.dismiss();
            }
        });
    }

    interface FlickrApi {
        @GET("/services/rest/?api_key=208c6f60ddf46fa081abb95f884cd536&method=flickr.photos.search&format=json&nojsoncallback=1&per_page=20&text=nature")
        void getNaturePhotos(@Query("page") int page, Callback<FlickrApiResponse> callback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.action_more) {
            loadPhotos();
            return true;
        }
        return false;
    }

}
