package com.example.flickrsearchengine;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM fav_item")
    ArrayList<Item> getAllItems();

    @Query("SELECT * FROM fav_item WHERE id=:id")
    Item getItem(String id);
    @Insert
    void insert(Item... items);
    @Update
    void update(Item... items);

    @Delete
    void delete(Item... items);


}
