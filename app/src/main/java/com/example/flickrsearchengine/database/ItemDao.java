package com.example.flickrsearchengine.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.flickrsearchengine.itemObjects.FavItem;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM fav_item")
   LiveData <List<FavItem>>getAllItems();
    @Query("SELECT * FROM fav_item WHERE favId=:id")
    FavItem getItem(long id);
@Query("DELETE FROM fav_item WHERE favId=:id")
    int delItem(long id);
    @Insert
    void insert(FavItem... items);
    @Update
    void update(FavItem... items);

    @Delete
    void delete(FavItem... items);


}
