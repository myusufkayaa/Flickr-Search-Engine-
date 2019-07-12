package com.example.flickrsearchengine.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.flickrsearchengine.Models.geo.Geo;
import com.example.flickrsearchengine.Models.info.Info;
import com.example.flickrsearchengine.Models.itemObjects.Item;
import com.example.flickrsearchengine.Models.search.Photo;
import com.example.flickrsearchengine.Models.search.Post;
import com.example.flickrsearchengine.service.ApiClient;
import com.example.flickrsearchengine.service.RestInterface;
import com.example.flickrsearchengine.Models.size.SizeExample;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragmentViewModel extends ViewModel {

    RestInterface restInterface = ApiClient.getClient().create(RestInterface.class);

    String searchWord;
    boolean isBack = false;

    private ArrayList<Item> tempItemList = new ArrayList<>();

    private MutableLiveData<List<Item>> mItemList;
    public LiveData<List<Item>> getItemList() {
        return mItemList;
    }

    public void init() {
        if (mItemList == null)
            mItemList = new MutableLiveData<>();
    }

    public void setTempItemList(ArrayList<Item> tempItemList) {
       this.tempItemList.clear();
       for (Item item:tempItemList){
           this.tempItemList.add(item);
       }
    }

    public void setIsBack(boolean bool, String tmpWord) {
        isBack = bool;
        this.searchWord=tmpWord;

    }

    public void search(String word, int page) {
        if (searchWord == null || searchWord.equals(word)) {
            searchWord = word;
            calledPost(word, page);
        } else {
            searchWord = word;
            tempItemList.clear();
            calledPost(searchWord, page);
        }

    }


    public void calledGeo(final String pId) {
        Call<Geo> callGeo = restInterface.getGeo("flickr.photos.geo.getLocation", "c0b00b9ef6c886b9bc297f5e127d9f84", pId, "json", 1);
        callGeo.enqueue(new Callback<Geo>() {
            @Override
            public void onResponse(Call<Geo> call, Response<Geo> response) {
                    if (!isBack) {
                    for (Item item : tempItemList) {
                        if (item.getPhotoId() == Long.parseLong(pId)) {
                            item.setLat(response.body().getPhoto().getLocation().getLatitude());
                            item.setLng(response.body().getPhoto().getLocation().getLongitude());
                            setLiveDataList();
                            break;
                        }
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
                if (!isBack) {
                    for (Item item : tempItemList) {
                        if (item.getPhotoId() == Long.parseLong(pId)) {
                            item.setDescription(response.body().getPhoto().getDescription().getContent());
                            item.setOwner(response.body().getPhoto().getOwner().getUsername());
                            item.setTitle(response.body().getPhoto().getTitle().getContent());
                            setLiveDataList();
                            calledGeo(pId);
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<Info> call, Throwable t) {
                Log.d("test", "com.example.flickrsearchengine.Info Patladı"+t.getMessage());
            }
        });
    }

    public void calledSize(final String pId) {
        Call<SizeExample> callSize = restInterface.getSize("flickr.photos.getSizes", "c0b00b9ef6c886b9bc297f5e127d9f84", pId, "json", 1);
        callSize.enqueue(new Callback<SizeExample>() {
            @Override
            public void onResponse(Call<SizeExample> call, Response<SizeExample> response) {
                if (!isBack) {
                    for (Item item : tempItemList) {
                        if (item.getPhotoId() == Long.parseLong(pId)) {
                            item.setSmallImg(response.body().getSizes().getSize().get(1).getSource());
                            item.setLargeImg(response.body().getSizes().getSize().get(response.body().getSizes().getSize().size() - 3).getSource());
                            setLiveDataList();
                            calledInfo(pId);
                        }
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
                if (!isBack) {
                    List<Photo> mPhoto;
                    mPhoto = response.body().getPhotos().getPhoto();
                    for (Photo p : mPhoto) {
                        Item item = new Item();
                        item.setPhotoId(Long.parseLong(p.getId()));
                        tempItemList.add(item);
                        calledSize(p.getId());
                    }
                }

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.d("test", "Post Patladı"+t.getMessage());
            }
        });

    }

    boolean itemControl(ArrayList<Item> itemList) {
        for (Item item : itemList) {
            if (item.getPhotoId() == 0 || item.getDescription() == null || item.getLargeImg() == null || item.getLat() == 0 || item.getLng() == 0 || item.getOwner() == null || item.getSmallImg() == null || item.getTitle() == null) {
                return false;
            }
        }
        return true;
    }

    public void setLiveDataList() {
        if (itemControl(tempItemList)) {
            mItemList.setValue(tempItemList);
        }
    }

}
