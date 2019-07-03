package com.example.flickrsearchengine.itemObjects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fav_item")
public class FavItem implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long favId;

    private String title;
    private String description;
    private String owner;
    private String largeImg;
    private String smallImg;
    private  double lat, lng;

    public FavItem(long id, String title, String description, String owner, String largeImg, String smallImg, double lat, double lng) {
        this.favId = id;
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.largeImg = largeImg;
        this.smallImg = smallImg;
        this.lat = lat;
        this.lng = lng;
    }

    public FavItem() {
    }

    protected FavItem(Parcel in) {
        favId = in.readLong();
        title = in.readString();
        description = in.readString();
        owner = in.readString();
        largeImg = in.readString();
        smallImg = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
    }

    public static final Creator<FavItem> CREATOR = new Creator<FavItem>() {
        @Override
        public FavItem createFromParcel(Parcel in) {
            return new FavItem(in);
        }

        @Override
        public FavItem[] newArray(int size) {
            return new FavItem[size];
        }
    };

    public long getFavId() {
        return favId;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getOwner() {
        return owner;
    }

    public String getLargeImg() {
        return largeImg;
    }

    public String getSmallImg() {
        return smallImg;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setFavId(long favId) {
        this.favId = favId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setLargeImg(String largeImg) {
        this.largeImg = largeImg;
    }

    public void setSmallImg(String smallImg) {
        this.smallImg = smallImg;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(favId);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(owner);
        parcel.writeString(largeImg);
        parcel.writeString(smallImg);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
    }
}
