package com.example.flickrsearchengine;

import android.os.Parcel;
import android.os.Parcelable;

import Geo.Geo;
import Info.Info;
import Size.SizeExample;

public class Item implements Parcelable {


    protected Item(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        owner = in.readString();
        largeImg = in.readString();
        smallImg = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
    }

    public Item() {
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    private String id;

    private String title;
    private String description;
    private String owner;
    private String largeImg;
    private String smallImg;
    private  double lat, lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLargeImg() {
        return largeImg;
    }

    public void setLargeImg(String largeImg) {
        this.largeImg = largeImg;
    }

    public String getSmallImg() {
        return smallImg;
    }

    public void setSmallImg(String smallImg) {
        this.smallImg = smallImg;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(owner);
        parcel.writeString(largeImg);
        parcel.writeString(smallImg);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);

    }
}
