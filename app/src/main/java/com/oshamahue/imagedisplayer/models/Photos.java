package com.oshamahue.imagedisplayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hadi on 12/10/2014.
 */
public class Photos implements Parcelable {

    @JsonProperty("page")
    private Integer page;
    @JsonProperty("pages")
    private Integer pages;
    @JsonProperty("perpage")
    private Integer perpage;
    @JsonProperty("total")
    private String total;
    @JsonProperty("photo")
    private List<Photo> photos = new ArrayList<Photo>();

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getPerpage() {
        return perpage;
    }

    public void setPerpage(Integer perpage) {
        this.perpage = perpage;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.page);
        dest.writeValue(this.pages);
        dest.writeValue(this.perpage);
        dest.writeString(this.total);
        dest.writeList(this.photos);
    }

    public Photos() {
    }

    private Photos(Parcel in) {
        this.page = (Integer) in.readValue(Integer.class.getClassLoader());
        this.pages = (Integer) in.readValue(Integer.class.getClassLoader());
        this.perpage = (Integer) in.readValue(Integer.class.getClassLoader());
        this.total = in.readString();
        this.photos = new ArrayList<Photo>();
        in.readList(this.photos, null);
    }

    public static final Parcelable.Creator<Photos> CREATOR = new Parcelable.Creator<Photos>() {
        public Photos createFromParcel(Parcel source) {
            return new Photos(source);
        }

        public Photos[] newArray(int size) {
            return new Photos[size];
        }
    };
}
