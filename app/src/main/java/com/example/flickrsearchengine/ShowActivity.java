package com.example.flickrsearchengine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Geo.Geo;
import Info.Info;
import Size.SizeExample;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import Search.Photo;
import Search.Post;

public class ShowActivity extends AppCompatActivity implements OnMapReadyCallback {
GoogleMap map;
TextView slideTitle;
ArrayList<Item> itemList;
ViewPager viewPager;
SlideAdapter slideAdapter;
int position;
String searchWord;
RestInterface restInterface;
PostAdapter postAdapter;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        LatLng location=new LatLng(itemList.get(position).getLat(),itemList.get(position).getLng());
        map.addMarker(new MarkerOptions().position(location));
        map.moveCamera(CameraUpdateFactory.newLatLng(location));
        setViewPager();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        slideTitle=findViewById(R.id.slideTitle);
        SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.slideMap);
        mapFragment.getMapAsync(this);
        restInterface = ApiClient.getClient().create(RestInterface.class);
        getIncomingIntent();


    }
    private void getIncomingIntent(){
        if (getIntent().hasExtra("position")&&getIntent().hasExtra("list")&&getIntent().hasExtra("searchWord")){
            Bundle bundle=getIntent().getExtras();
            position=bundle.getInt("position",0);
            itemList=bundle.getParcelableArrayList("list");
            slideTitle.setText(itemList.get(position).getTitle());
            searchWord=bundle.getString("searchWord");

        }
    }



    public void setViewPager() {

        viewPager = findViewById(R.id.viewPager);
        slideAdapter = new SlideAdapter(ShowActivity.this, itemList, slideTitle, map);
        viewPager.setAdapter(slideAdapter);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                slideTitle.setText(itemList.get(position).getTitle());
                map.clear();
                LatLng location = new LatLng(itemList.get(position).getLat(), itemList.get(position).getLng());
                MarkerOptions a = new MarkerOptions().position(location);
                Marker m = map.addMarker(a);
                m.setPosition(location);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
                if ((position%25)==22){
                    calledPost(searchWord,(itemList.size()/25)+1);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
    public void calledGeo(final String pId) {
        Call<Geo> callGeo = restInterface.getGeo("flickr.photos.geo.getLocation", "c0b00b9ef6c886b9bc297f5e127d9f84", pId, "json", 1);
        callGeo.enqueue(new Callback<Geo>() {
            @Override
            public void onResponse(Call<Geo> call, Response<Geo> response) {
                boolean varMi = false;
                for (Item item : itemList) {

                    if (item.getId().equals(pId)) {

                        varMi = true;
                        item.setLat(response.body().getPhoto().getLocation().getLatitude());
                        item.setLng(response.body().getPhoto().getLocation().getLongitude());
                        callAdapter();

                        break;
                    }
                }
                if (!varMi) {
                    Item item = new Item();
                    item.setId(pId);
                    item.setLat(response.body().getPhoto().getLocation().getLatitude());
                    item.setLng(response.body().getPhoto().getLocation().getLongitude());
                    callAdapter();

                }

            }

            @Override
            public void onFailure(Call<Geo> call, Throwable t) {
                Log.d("test", "Geo Patladı");
            }

        });


    }

    public void calledInfo(final String pId) {


        Call<Info> callInfo = restInterface.getInfo("flickr.photos.getInfo", "c0b00b9ef6c886b9bc297f5e127d9f84", pId, "json", 1);
        callInfo.enqueue(new Callback<Info>() {
            @Override
            public void onResponse(Call<Info> call, Response<Info> response) {
                boolean varMi = false;
                for (Item item : itemList) {
                    if (item.getId().equals(pId)) {
                        varMi = true;
                        item.setDescription(response.body().getPhoto().getDescription().getContent());
                        item.setOwner(response.body().getPhoto().getOwner().getUsername());
                        item.setTitle(response.body().getPhoto().getTitle().getContent());
                        callAdapter();
                        calledGeo(pId);
                        break;
                    }

                }
                if (!varMi) {
                    Item item = new Item();
                    item.setId(pId);
                    item.setDescription(response.body().getPhoto().getDescription().getContent());
                    item.setOwner(response.body().getPhoto().getOwner().getUsername());
                    item.setTitle(response.body().getPhoto().getTitle().getContent());
                    callAdapter();
                    calledGeo(pId);
                }
            }

            @Override
            public void onFailure(Call<Info> call, Throwable t) {
                Log.d("test", "Info Patladı");
            }
        });
    }

    public void calledSize(final String pId) {

        Call<SizeExample> callSize = restInterface.getSize("flickr.photos.getSizes", "c0b00b9ef6c886b9bc297f5e127d9f84", pId, "json", 1);

        callSize.enqueue(new Callback<SizeExample>() {

            @Override
            public void onResponse(Call<SizeExample> call, Response<SizeExample> response) {
                boolean varMi = false;
                for (Item item : itemList) {
                    if (item.getId().equals(pId)) {
                        varMi = true;
                        item.setSmallImg(response.body().getSizes().getSize().get(1).getSource());
                        item.setLargeImg(response.body().getSizes().getSize().get(response.body().getSizes().getSize().size()-3).getSource());
                        callAdapter();
                        calledInfo(pId);
                        break;
                    }
                }
                if (!varMi) {
                    Item item = new Item();
                    item.setSmallImg(response.body().getSizes().getSize().get(1).getSource());
                    item.setLargeImg(response.body().getSizes().getSize().get(response.body().getSizes().getSize().size()-3).getSource());
                    callAdapter();
                    calledInfo(pId);
                }


            }

            @Override
            public void onFailure(Call<SizeExample> call, Throwable t) {
                Log.d("test", "Size Patladı");
            }
        });

    }

    public void calledPost(String searchedWord, int pageCount) {
        Call<Post> callPost = restInterface.getPost("flickr.photos.Search", "c0b00b9ef6c886b9bc297f5e127d9f84", searchedWord, 1, 25, pageCount, "json", 1);
        callPost.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                List<Photo> mPhoto;
                mPhoto = response.body().getPhotos().getPhoto();
                for (Photo p : mPhoto) {
                    Item item = new Item();
                    item.setId(p.getId());
                    itemList.add(item);
                    slideAdapter.notifyDataSetChanged();
                    calledSize(p.getId());
                }

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.d("test", "Post Patladı");
            }
        });
    }

    boolean itemControl() {
        for (Item item : itemList) {
            if (item.getId()==null || item.getDescription()==null|| item.getLargeImg()==null || item.getLat()==0 ||item.getLng()==0|| item.getOwner()==null||item.getSmallImg()==null||item.getTitle()==null) {
                return false;

            }
        }
        return true;
    }

    void callAdapter() {
        if (itemControl()) {
            slideAdapter.setItemList(itemList);
            slideAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onBackPressed() {

        Intent resultIntent=new Intent();
        resultIntent.putParcelableArrayListExtra("list",itemList);
        resultIntent.putExtra("count",itemList.size()/25);
        setResult(RESULT_OK,resultIntent);
        super.onBackPressed();
    }
}
