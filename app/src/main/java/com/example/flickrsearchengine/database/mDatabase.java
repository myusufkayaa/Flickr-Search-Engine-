package com.example.flickrsearchengine.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.flickrsearchengine.itemObjects.FavItem;

@Database(entities = {FavItem.class}, version = 1,exportSchema = false)
public abstract class mDatabase extends RoomDatabase {
    private static final String DB_NAME="veri.db";

    private static volatile  mDatabase instance;
    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
    public static synchronized RoomDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    mDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();        }
        return instance;
    }



    public abstract ItemDao getItemDao();

    private  static RoomDatabase.Callback roomCallback= new RoomDatabase.Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private ItemDao itemDao;
        private PopulateDbAsyncTask(mDatabase db){
            itemDao=db.getItemDao();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}

