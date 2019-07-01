package com.example.flickrsearchengine;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import Geo.Geo;
import Info.Info;
import Search.Photo;
import Search.Post;
import Size.SizeExample;
import retrofit2.Call;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.app.Activity.RESULT_OK;
import static androidx.recyclerview.widget.RecyclerView.*;


public class SearchFragment extends Fragment  implements SearchDialog.SearchDialogListener {
    private Button openDialogButton;
    RestInterface restInterface;
    Post post;
    RecyclerView recyclerView;
    ArrayList<Item> itemList;
    ArrayList<Item> tempItemList;
    SearchFragment context;
    ProgressDialog progressDialog;
    int currentItems, totalItems, scrollOutItems;
    int pageCount = 1;
    String searchWord;
    String tempSearchWord;
    PostAdapter postAdapter;
    ProgressBar progressBar;
    boolean isLoading=false;
    boolean isBack=false;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_search, container, false);

        init();
        openDialogButton = view.findViewById(R.id.openDialogButton);
        openDialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
        restInterface = ApiClient.getClient().create(RestInterface.class);


        return view;
    }


    private void init() {
        recyclerView = view.findViewById(R.id.recyclerView);
        post = new Post();
        progressBar = view.findViewById(R.id.progressBar);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), VERTICAL,false);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemList = new ArrayList<>();
        tempItemList=new ArrayList<>();
        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (!isLoading) {
                        currentItems = layoutManager.getChildCount();
                        totalItems = layoutManager.getItemCount();
                        scrollOutItems = layoutManager.findFirstCompletelyVisibleItemPosition();
                        if (currentItems + scrollOutItems >= totalItems) {
                            isBack = false;
                            progressBar.setVisibility(View.VISIBLE);
                            pageCount++;
                            isLoading = true;
                            fetchData();
                        }
                    }
                }
            }
        });

    }

    private void fetchData() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                calledPost(tempSearchWord, (tempItemList.size()/25)+1);


            }
        }, 5000);
    }


    public void openDialog() {
        SearchDialog searchDialog = new SearchDialog();
        searchDialog.show(getFragmentManager(), "Arama Penceresi");
       searchDialog.setListener(this);



    }
    @Override
    public void applyTexts(String searchedWord) {
        isBack=false;
        this.searchWord = searchedWord;
        tempItemList.clear();
        pageCount = 1;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Bekleyin...");
        progressDialog.setOnCancelListener(new Dialog.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                isBack=true;
                isLoading=true;
                tempItemList.clear();
                for (Item item: itemList){
                    tempItemList.add(item);
                }
                isLoading = false;


            }
        });
        progressDialog.show();
        calledPost(searchWord,pageCount);
    }

    public void calledGeo(final String pId) {
        Call<Geo> callGeo = restInterface.getGeo("flickr.photos.geo.getLocation", "c0b00b9ef6c886b9bc297f5e127d9f84", pId, "json", 1);
        callGeo.enqueue(new Callback<Geo>() {
            @Override
            public void onResponse(Call<Geo> call, Response<Geo> response) {
                if (isBack){
                    setBack();
                }else {
                    boolean varMi = false;
                    for (Item item : tempItemList) {

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
                if (isBack){
                    setBack();
                }else {
                    boolean varMi = false;
                    for (Item item : tempItemList) {
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
                if (isBack){
                    setBack();
                }else {
                    boolean varMi = false;
                    for (Item item : tempItemList) {
                        if (item.getId().equals(pId)) {
                            varMi = true;
                            item.setSmallImg(response.body().getSizes().getSize().get(1).getSource());
                            item.setLargeImg(response.body().getSizes().getSize().get(response.body().getSizes().getSize().size() - 3).getSource());
                            callAdapter();
                            calledInfo(pId);
                            break;
                        }
                    }
                    if (!varMi) {
                        Item item = new Item();
                        item.setSmallImg(response.body().getSizes().getSize().get(1).getSource());
                        item.setLargeImg(response.body().getSizes().getSize().get(response.body().getSizes().getSize().size() - 3).getSource());
                        callAdapter();
                        calledInfo(pId);
                    }
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
                if (isBack) {
                    setBack();
                }else {
                    List<Photo> mPhoto;
                    mPhoto = response.body().getPhotos().getPhoto();
                    for (Photo p : mPhoto) {
                        Item item = new Item();
                        item.setId(p.getId());
                        tempItemList.add(item);
                        calledSize(p.getId());
                    }
                }

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.d("test", "Post Patladı");
            }
        });

    }

    boolean itemControl(ArrayList<Item> itemList) {
        for (Item item : itemList) {
            if (item.getId()==null || item.getDescription()==null|| item.getLargeImg()==null || item.getLat()==0 ||item.getLng()==0|| item.getOwner()==null||item.getSmallImg()==null||item.getTitle()==null) {
                return false;

            }
        }
        return true;
    }

    void callAdapter() {
        if (itemControl(tempItemList)) {
            if (tempItemList.size()>25){
                this.itemList.clear();
                for (Item item:tempItemList){
                    this.itemList.add(item);
                }
                postAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                isLoading=false;
            }else{
                Log.i("progress", "kapandı");
                isLoading=false;
                if (progressDialog != null && progressDialog.isShowing()) {
                    this.itemList.clear();
                    for (int i=0;i<tempItemList.size();i++){
                        this.itemList.add(tempItemList.get(i));
                    }

                    postAdapter = new PostAdapter(this.itemList, getActivity(),searchWord);
                    recyclerView.setAdapter(postAdapter);
                    progressBar.setVisibility(View.GONE);
                    postAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                    tempSearchWord=searchWord;


                }

            }
        }

    }

    void setBack() {
        searchWord=tempSearchWord;
        callAdapter();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==1){
            if (resultCode==RESULT_OK){
                itemList=data.getParcelableArrayListExtra("list");
                pageCount=data.getIntExtra("count", 1);
                postAdapter = new PostAdapter(itemList, getActivity() ,searchWord);
                recyclerView.setAdapter(postAdapter);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




}
