package com.oshamahue.imagedisplayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Hadi on 12/10/2014.
 */
public class FlickrApiResponse implements Parcelable {
    @JsonProperty("photos")
    private Photos photos;
    @JsonProperty("stat")
    private String stat;

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.photos, flags);
        dest.writeString(this.stat);
    }

    public FlickrApiResponse() {
    }

    private FlickrApiResponse(Parcel in) {
        this.photos = in.readParcelable(Photos.class.getClassLoader());
        this.stat = in.readString();
    }

    public static final Parcelable.Creator<FlickrApiResponse> CREATOR = new Parcelable.Creator<FlickrApiResponse>() {
        public FlickrApiResponse createFromParcel(Parcel source) {
            return new FlickrApiResponse(source);
        }

        public FlickrApiResponse[] newArray(int size) {
            return new FlickrApiResponse[size];
        }
    };
}
