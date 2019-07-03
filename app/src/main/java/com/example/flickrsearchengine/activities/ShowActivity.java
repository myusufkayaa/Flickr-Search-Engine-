package com.example.flickrsearchengine.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.flickrsearchengine.itemObjects.FavItem;
import com.example.flickrsearchengine.service.ApiClient;
import com.example.flickrsearchengine.itemObjects.Item;
import com.example.flickrsearchengine.R;
import com.example.flickrsearchengine.service.RestInterface;
import com.example.flickrsearchengine.adapters.SlideAdapter;
import com.example.flickrsearchengine.viewModels.FavItemViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import com.example.flickrsearchengine.geo.Geo;
import com.example.flickrsearchengine.info.Info;
import com.example.flickrsearchengine.size.SizeExample;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.flickrsearchengine.search.Photo;
import com.example.flickrsearchengine.search.Post;

public class ShowActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap map;
    TextView slideTitle;
    ArrayList<Item> itemList;
    ViewPager viewPager;
    SlideAdapter slideAdapter;
    int position;
    String searchWord;
    RestInterface restInterface;
    List<FavItem> favItemList;
    Button likeButton;
    private FavItemViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (itemList != null) {
            map = googleMap;
            LatLng location = new LatLng(itemList.get(position).getLat(), itemList.get(position).getLng());
            map.addMarker(new MarkerOptions().position(location));
            map.moveCamera(CameraUpdateFactory.newLatLng(location));
            setSearchViewPager();
        } else {
            map = googleMap;
            LatLng location = new LatLng(favItemList.get(position).getLat(), favItemList.get(position).getLng());
            map.addMarker(new MarkerOptions().position(location));
            map.moveCamera(CameraUpdateFactory.newLatLng(location));
            setFavViewPager();
        }

    }

    public void init() {
        setContentView(R.layout.activity_show);
        slideTitle = findViewById(R.id.slideTitle);
        likeButton = findViewById(R.id.likeButton);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.slideMap);
        mapFragment.getMapAsync(this);
        restInterface = ApiClient.getClient().create(RestInterface.class);
        getIncomingIntent();

    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("position") && getIntent().hasExtra("list") && getIntent().hasExtra("searchWord")) {
            Bundle bundle = getIntent().getExtras();
            position = bundle.getInt("position", 0);
            itemList = bundle.getParcelableArrayList("list");
            slideTitle.setText(itemList.get(position).getTitle());
            searchWord = bundle.getString("searchWord");
            likeButton.setText("LIKE");
            viewModel = ViewModelProviders.of(this).get(FavItemViewModel.class);
            viewModel.getAllFavItems().observe(this, new Observer<List<FavItem>>() {
                @Override
                public void onChanged(List<FavItem> favItems) {
                    favItemList = favItems;
                }
            });
            setButton(position);
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickLike(position);
                }
            });
        } else if (getIntent().hasExtra("favlist") && getIntent().hasExtra("favposition")) {
            viewModel = ViewModelProviders.of(this).get(FavItemViewModel.class);
            favItemList = getIntent().getParcelableArrayListExtra("favlist");
            position = getIntent().getIntExtra("favposition", 0);
            slideTitle.setText(favItemList.get(position).getTitle());
            likeButton.setText("DISLIKE");
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickLike(position);
                }
            });
        }
    }

    public void setSearchViewPager() {
        viewPager = findViewById(R.id.viewPager);
        slideAdapter = new SlideAdapter(ShowActivity.this, itemList, slideTitle, map);
        viewPager.setAdapter(slideAdapter);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                likeButton.setText("LIKE");
                setButton(position);
                likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickLike(position);
                    }
                });
                slideTitle.setText(itemList.get(position).getTitle());
                map.clear();
                LatLng location = new LatLng(itemList.get(position).getLat(), itemList.get(position).getLng());
                MarkerOptions a = new MarkerOptions().position(location);
                Marker m = map.addMarker(a);
                m.setPosition(location);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
                if ((position % 25) == 22) {
                    calledPost(searchWord, (itemList.size() / 25) + 1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void setFavViewPager() {
        viewPager = findViewById(R.id.viewPager);
        slideAdapter = new SlideAdapter(ShowActivity.this, favItemList, slideTitle, map);
        viewPager.setAdapter(slideAdapter);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                likeButton.setText("DISLIKE");
                likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickLike(position);
                    }
                });
                slideTitle.setText(favItemList.get(position).getTitle());
                map.clear();
                LatLng location = new LatLng(favItemList.get(position).getLat(), favItemList.get(position).getLng());
                MarkerOptions a = new MarkerOptions().position(location);
                Marker m = map.addMarker(a);
                m.setPosition(location);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void clickLike(int position) {
        if (itemList!=null){
            if (likeButton.getText().equals("LIKE")) {
                viewModel.insert(new FavItem(Long.parseLong(itemList.get(position).getId()), itemList.get(position).getTitle(), itemList.get(position).getDescription(), itemList.get(position).getOwner(), itemList.get(position).getLargeImg(), itemList.get(position).getSmallImg(), itemList.get(position).getLat(), itemList.get(position).getLng()));
                likeButton.setText("DISLIKE");
            } else {
                for (FavItem favItem : favItemList) {
                    if (itemList.get(position).getId().equals(String.valueOf(favItem.getFavId()))) {
                        viewModel.delete(favItem);
                        likeButton.setText("LIKE");
                    }
                }
            }
        }else{
            if (likeButton.getText().equals("DISLIKE")){
                viewModel.delete(favItemList.get(position));
                favItemList.remove(position);
                slideAdapter.notifyDataSetChanged();
                if (favItemList.size()==0){
                    this.finish();
                }else if (position==favItemList.size()){
                    position--;
                    slideTitle.setText(favItemList.get(position).getTitle());

                }else
                    slideTitle.setText(favItemList.get(position).getTitle());
            }
        }

    }

    public void setButton(int position) {
        if (favItemList != null) {
            if (itemList != null) {
                likeButton.setText("LIKE");
                for (FavItem favItem : favItemList) {
                    if (itemList.get(position).getId().equals(String.valueOf(favItem.getFavId()))) {
                        likeButton.setText("DISLIKE");
                    }
                }
            } else {
                likeButton.setText("DISLIKE");
            }

        }
    }

    public void calledGeo(final String pId) {
        Call<Geo> callGeo = restInterface.getGeo("flickr.photos.geo.getLocation", "c0b00b9ef6c886b9bc297f5e127d9f84", pId, "json", 1);
        callGeo.enqueue(new Callback<Geo>() {
            @Override
            public void onResponse(Call<Geo> call, Response<Geo> response) {
                for (Item item : itemList) {
                    if (item.getId().equals(pId)) {
                        item.setLat(response.body().getPhoto().getLocation().getLatitude());
                        item.setLng(response.body().getPhoto().getLocation().getLongitude());
                        callAdapter();

                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Geo> call, Throwable t) {
                Log.d("test", "com.example.flickrsearchengine.Geo Patladı");
            }
        });
    }

    public void calledInfo(final String pId) {
        Call<Info> callInfo = restInterface.getInfo("flickr.photos.getInfo", "c0b00b9ef6c886b9bc297f5e127d9f84", pId, "json", 1);
        callInfo.enqueue(new Callback<Info>() {
            @Override
            public void onResponse(Call<Info> call, Response<Info> response) {
                for (Item item : itemList) {
                    if (item.getId().equals(pId)) {
                        item.setDescription(response.body().getPhoto().getDescription().getContent());
                        item.setOwner(response.body().getPhoto().getOwner().getUsername());
                        item.setTitle(response.body().getPhoto().getTitle().getContent());
                        callAdapter();
                        calledGeo(pId);
                        break;
                    }

                }
            }

            @Override
            public void onFailure(Call<Info> call, Throwable t) {
                Log.d("test", "com.example.flickrsearchengine.Info Patladı");
            }
        });
    }

    public void calledSize(final String pId) {

        Call<SizeExample> callSize = restInterface.getSize("flickr.photos.getSizes", "c0b00b9ef6c886b9bc297f5e127d9f84", pId, "json", 1);

        callSize.enqueue(new Callback<SizeExample>() {

            @Override
            public void onResponse(Call<SizeExample> call, Response<SizeExample> response) {
                for (Item item : itemList) {
                    if (item.getId().equals(pId)) {
                        item.setSmallImg(response.body().getSizes().getSize().get(1).getSource());
                        item.setLargeImg(response.body().getSizes().getSize().get(response.body().getSizes().getSize().size() - 3).getSource());
                        callAdapter();
                        calledInfo(pId);
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<SizeExample> call, Throwable t) {
                Log.d("test", "com.example.flickrsearchengine.Size Patladı");
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
            if (item.getId() == null || item.getDescription() == null || item.getLargeImg() == null || item.getLat() == 0 || item.getLng() == 0 || item.getOwner() == null || item.getSmallImg() == null || item.getTitle() == null) {
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
        if (itemList != null) {
            Intent resultIntent = new Intent();
            resultIntent.putParcelableArrayListExtra("list", itemList);
            resultIntent.putExtra("count", itemList.size() / 25);
            setResult(RESULT_OK, resultIntent);

        }
        super.onBackPressed();
    }
}
