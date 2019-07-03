package com.example.flickrsearchengine.activities;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.flickrsearchengine.fragments.FavFragment;
import com.example.flickrsearchengine.itemObjects.Item;
import com.example.flickrsearchengine.R;
import com.example.flickrsearchengine.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity{
    BottomNavigationView navigation;
    Fragment searchFragment;
    Fragment favFragment;

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

}
