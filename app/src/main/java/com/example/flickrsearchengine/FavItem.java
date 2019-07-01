package com.example.flickrsearchengine;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fav_item")
public class FavItem {
    @PrimaryKey(autoGenerate = true)
    private String id;
    private String title;
    private String description;
    private String owner;
    private String largeImg;
    private String smallImg;
    private  double lat, lng;

    public FavItem(String id, String title, String description, String owner, String largeImg, String smallImg, double lat, double lng) {
        this.id = id;
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

    public String getId() {
        return id;
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
}
