package com.example.flickrsearchengine.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.flickrsearchengine.itemObjects.FavItem;

import java.util.List;

public class FavItemRepository {
    private ItemDao itemDao;
    private LiveData<List<FavItem>> allFavItems;

    public FavItemRepository(Application application) {
        mDatabase database = (mDatabase) mDatabase.getInstance(application);
        itemDao = database.getItemDao();
        allFavItems = itemDao.getAllItems();
    }

    public void update(FavItem item) {
        new UpdateItemAsyncTask(itemDao).execute(item);


    }

    public void delete(FavItem item) {
        new DeleteItemAsyncTask(itemDao).execute(item);
    }

    public void insert(FavItem item) {
        new InsertItemAsyncTask(itemDao).execute(item);
    }




    public LiveData<List<FavItem>> getAllFavItems() {
        return allFavItems;
    }

    private static class InsertItemAsyncTask extends AsyncTask<FavItem, Void, Void> {
        private ItemDao itemDao;

        private InsertItemAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(FavItem... favItems) {
            itemDao.insert(favItems[0]);
            return null;
        }
    }
    private static class UpdateItemAsyncTask extends AsyncTask<FavItem, Void, Void> {
        private ItemDao itemDao;

        private UpdateItemAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(FavItem... favItems) {
            itemDao.update(favItems[0]);
            return null;
        }
    }
    private static class DeleteItemAsyncTask extends AsyncTask<FavItem, Void, Void> {
        private ItemDao itemDao;

        private DeleteItemAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(FavItem... favItems) {
            itemDao.delete(favItems[0]);
            return null;
        }
    }


}
