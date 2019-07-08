package com.example.flickrsearchengine.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.flickrsearchengine.database.ItemDao;
import com.example.flickrsearchengine.database.mDatabase;
import com.example.flickrsearchengine.itemObjects.Item;

import java.util.List;

public class FavItemRepository {
    private ItemDao itemDao;
    private LiveData<List<Item>> allFavItems;

    public FavItemRepository(Application application) {
        mDatabase database = (mDatabase) mDatabase.getInstance(application);
        itemDao = database.getItemDao();
        allFavItems = itemDao.getAllItems();
    }

    public void update(Item item) {
        new UpdateItemAsyncTask(itemDao).execute(item);


    }

    public void delete(Item item) {
        new DeleteItemAsyncTask(itemDao).execute(item);
    }

    public void insert(Item item) {
        new InsertItemAsyncTask(itemDao).execute(item);
    }




    public LiveData<List<Item>> getAllFavItems() {
        return allFavItems;
    }

    private static class InsertItemAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDao itemDao;

        private InsertItemAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDao.insert(items[0]);
            return null;
        }
    }
    private static class UpdateItemAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDao itemDao;

        private UpdateItemAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDao.update(items[0]);
            return null;
        }
    }
    private static class DeleteItemAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDao itemDao;

        private DeleteItemAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDao.delete(items[0]);
            return null;
        }
    }


}
