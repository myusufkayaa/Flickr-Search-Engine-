package com.example.flickrsearchengine.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.flickrsearchengine.itemObjects.Item;
import com.example.flickrsearchengine.repositories.FavItemRepository;

import java.util.List;

public class FavItemViewModel extends AndroidViewModel {
    private FavItemRepository repository;
    private LiveData<List<Item>> allItems;
    public FavItemViewModel(@NonNull Application application) {
        super(application);
        repository=new FavItemRepository(application);
        allItems=repository.getAllFavItems();
    }
    public  void insert(Item item){
        repository.insert(item);

    }
    public void update(Item item){
        repository.update(item);

    }
    public void delete(Item item){
        repository.delete(item);

    }
    public LiveData<List<Item>> getAllFavItems(){
        return allItems;
    }
}
