package com.example.flickrsearchengine.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.flickrsearchengine.Models.itemObjects.Item;
import com.example.flickrsearchengine.R;
import com.example.flickrsearchengine.adapters.SlideAdapter;
import com.example.flickrsearchengine.viewModels.SearchFragmentViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShowActivity extends AppCompatActivity implements OnMapReadyCallback {
    ProgressDialog progressDialog;
    GoogleMap map;
    TextView slideTitle;
    List<Item> favItemList;
    ArrayList<Item> itemList;
    ViewPager viewPager;
    SlideAdapter slideAdapter;
    int position;
    String searchWord;
    Button likeButton;
    SearchFragmentViewModel searchFragmentViewModel;
    FirebaseFirestore db;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        searchFragmentViewModel = new SearchFragmentViewModel();
        searchFragmentViewModel.init();
        setContentView(R.layout.activity_show);
        slideTitle = findViewById(R.id.slideTitle);
        likeButton = findViewById(R.id.likeButton);
        progressDialog = new ProgressDialog(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.slideMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        getIncomingIntent();
    }

    private void setFavItemList() {
        db.collection(mAuth.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        favItemList.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            favItemList.add(new Item(document));
                            if (slideAdapter == null) return;
                            slideAdapter.setFavItemList(favItemList);
                        }
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (itemList != null) {
            LatLng location = new LatLng(itemList.get(position).getLat(), itemList.get(position).getLng());
            map.addMarker(new MarkerOptions().position(location));
            map.moveCamera(CameraUpdateFactory.newLatLng(location));
            setSearchViewPager();
        } else {
            LatLng location = new LatLng(favItemList.get(position).getLat(), favItemList.get(position).getLng());
            map.addMarker(new MarkerOptions().position(location));
            map.moveCamera(CameraUpdateFactory.newLatLng(location));
            setFavViewPager();
        }
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("position") && getIntent().hasExtra("list") && getIntent().hasExtra("searchWord") && getIntent().hasExtra("favlist")) {
            Bundle bundle = getIntent().getExtras();
            position = bundle.getInt("position", 0);
            itemList = bundle.getParcelableArrayList("list");
            searchWord = bundle.getString("searchWord");
            favItemList = bundle.getParcelableArrayList("favlist");
            slideTitle.setText(itemList.get(position).getTitle());
            likeButton.setText("LIKE");
            setButton(position);
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickLike(position);
                }
            });
        } else if (getIntent().hasExtra("favposition") && getIntent().hasExtra("favlist")) {
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
        searchFragmentViewModel = new SearchFragmentViewModel();
        searchFragmentViewModel.init();
        searchFragmentViewModel.getItemList().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                itemList.addAll(items);
                slideAdapter.setItemList(itemList);
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
        viewPager = findViewById(R.id.viewPager);
        slideAdapter = new SlideAdapter(ShowActivity.this, itemList, slideTitle, map);
        setFavItemList();
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
                if (position == itemList.size() - 1) {
                    searchFragmentViewModel.calledPost(searchWord, (itemList.size() / 25) + 1);
                    progressDialog.setMessage("Devamı Yükleniyor...");
                    progressDialog.show();
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
        if (itemList != null) {
            if (likeButton.getText().equals("LIKE")) {
                Map<String, Object> post = new HashMap<>();
                post.put("post", itemList.get(position));
                db.collection(mAuth.getUid()).document(String.valueOf(itemList.get(position).getPhotoId())).set(post);
                likeButton.setText("DISLIKE");
            } else {
                for (Item favItem : favItemList) {
                    if (itemList.get(position).getPhotoId() == favItem.getPhotoId()) {
                        db.collection(mAuth.getUid()).document(String.valueOf(favItem.getPhotoId())).delete();
                        likeButton.setText("LIKE");
                    }
                }
            }
            return;
        }

        if (likeButton.getText().equals("DISLIKE")) {
            db.collection(mAuth.getUid()).document(String.valueOf(favItemList.get(position).getPhotoId())).delete();
            favItemList.remove(position);
            slideAdapter.notifyDataSetChanged();
            if (favItemList.size() == 0) {
                finish();
            } else if (position == favItemList.size()) {
                position--;
            }
            if (favItemList.size() != 0) {
                slideTitle.setText(favItemList.get(position).getTitle());
            }
        }
    }

    public void setButton(int position) {
        if (favItemList == null)
            return;
        if (itemList == null) {
            likeButton.setText("DISLIKE");
            return;
        }
        likeButton.setText("LIKE");
        for (Item favItem : favItemList) {
            if (itemList.get(position).getPhotoId() == favItem.getPhotoId()) {
                likeButton.setText("DISLIKE");
            }
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
