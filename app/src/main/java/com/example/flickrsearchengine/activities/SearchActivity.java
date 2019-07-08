package com.example.flickrsearchengine.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.flickrsearchengine.fragments.FavFragment;
import com.example.flickrsearchengine.R;
import com.example.flickrsearchengine.fragments.SearchFragment;
import com.example.flickrsearchengine.itemObjects.Item;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity{
    BottomNavigationView navigation;
    SearchFragment searchFragment;
    FavFragment favFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        navigation=findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        searchFragment=new SearchFragment();
        favFragment=new FavFragment();



    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.navigation_search:
                    loadFragment(searchFragment);
                    return true;

                case R.id.navigation_favorite:
                    loadFragment(favFragment);
                    return true;
            }

            return false;
        }
    };

    void loadFragment(Fragment fragment){
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
                ArrayList<Item> list = data.getParcelableArrayListExtra("list");
                searchFragment.setPostAdapter(list);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
