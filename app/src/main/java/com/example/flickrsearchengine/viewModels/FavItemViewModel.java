package com.example.flickrsearchengine.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.flickrsearchengine.database.FavItemRepository;
import com.example.flickrsearchengine.itemObjects.FavItem;

import java.util.List;

public class FavItemViewModel extends AndroidViewModel {
    private FavItemRepository repository;
    private LiveData<List<FavItem>> allItems;
    public FavItemViewModel(@NonNull Application application) {
        super(application);
        repository=new FavItemRepository(application);
        allItems=repository.getAllFavItems();
    }
    public  void insert(FavItem favItem){
        repository.insert(favItem);

    }
    public void update(FavItem favItem){
        repository.update(favItem);

    }
    public void delete(FavItem favItem){
        repository.delete(favItem);

    }
    public LiveData<List<FavItem>> getAllFavItems(){
        return allItems;
    }
}
