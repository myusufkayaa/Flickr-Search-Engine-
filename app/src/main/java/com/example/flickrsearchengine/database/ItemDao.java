package com.example.flickrsearchengine.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.flickrsearchengine.itemObjects.Item;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM fav_item")
   LiveData <List<Item>>getAllItems();
    @Query("SELECT * FROM fav_item WHERE photoId=:id")
    Item getItem(long id);
@Query("DELETE FROM fav_item WHERE photoId=:id")
    int delItem(long id);
    @Insert
    void insert(Item... items);
    @Update
    void update(Item... items);

    @Delete
    void delete(Item... items);


}
