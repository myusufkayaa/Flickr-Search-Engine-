package com.example.flickrsearchengine.Models.itemObjects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;

@Entity(tableName = "fav_item")
public class Item implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long photoId;
    private String title;
    private String description;
    private String owner;
    private String largeImg;
    private String smallImg;
    private  double lat, lng;

    public Item(long id, String title, String description, String owner, String largeImg, String smallImg, double lat, double lng) {
        this.photoId = id;
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.largeImg = largeImg;
        this.smallImg = smallImg;
        this.lat = lat;
        this.lng = lng;
    }

    public Item() {
    }



    public Item(Parcel in) {
        photoId = in.readLong();
        title = in.readString();
        description = in.readString();
        owner = in.readString();
        largeImg = in.readString();
        smallImg = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
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

    public Item(QueryDocumentSnapshot document) {
        photoId= (long) ((HashMap)document.getData().get("post")).get("photoId");
        title= (String) ((HashMap)document.getData().get("post")).get("title");
        description= (String) ((HashMap)document.getData().get("post")).get("description");
        owner= (String) ((HashMap)document.getData().get("post")).get("owner");
        largeImg= (String) ((HashMap)document.getData().get("post")).get("largeImg");
        smallImg= (String) ((HashMap)document.getData().get("post")).get("smallImg");
        lat= (double) ((HashMap)document.getData().get("post")).get("lat");
        lng= (double) ((HashMap)document.getData().get("post")).get("lng");
    }


    public long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(long photoId) {
        this.photoId = photoId;
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
        parcel.writeLong(photoId);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(owner);
        parcel.writeString(largeImg);
        parcel.writeString(smallImg);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
    }

    public boolean avaliable() {

        return getPhotoId() != 0 && getDescription() != null && getLargeImg() != null && getLat() != 0 && getLng() != 0 && getOwner() != null && getSmallImg() != null && getTitle() != null;

    }
}
