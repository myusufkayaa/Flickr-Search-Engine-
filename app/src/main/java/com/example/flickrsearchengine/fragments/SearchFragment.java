package com.example.flickrsearchengine.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flickrsearchengine.R;
import com.example.flickrsearchengine.adapters.PostAdapter;
import com.example.flickrsearchengine.adapters.SearchDialog;
import com.example.flickrsearchengine.itemObjects.Item;
import com.example.flickrsearchengine.search.Post;
import com.example.flickrsearchengine.service.ApiClient;
import com.example.flickrsearchengine.service.RestInterface;
import com.example.flickrsearchengine.viewModels.FavItemViewModel;
import com.example.flickrsearchengine.viewModels.SearchFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.*;


public class SearchFragment extends Fragment implements SearchDialog.SearchDialogListener {

    private Button openDialogButton;
    RestInterface restInterface;
    Post post;
    RecyclerView recyclerView;
    ArrayList<Item> itemList;
    ArrayList<Item> tempItemList;
    ProgressDialog progressDialog;
    int currentItems, totalItems, scrollOutItems;
    String searchWord;
    String tempSearchWord;
    PostAdapter postAdapter;
    ProgressBar progressBar;
    boolean isLoading = false;
    private View view;
    private FavItemViewModel favItemViewModel;
    private SearchFragmentViewModel searchFragmentViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_search, container, false);
            searchFragmentViewModel = ViewModelProviders.of(this).get(SearchFragmentViewModel.class);
            searchFragmentViewModel.init();
            searchFragmentViewModel.getItemList().observe(this, new Observer<List<Item>>() {
                @Override
                public void onChanged(List<Item> ıtems) {
                    tempItemList.clear();
                    for (Item ıtem : ıtems) {
                        tempItemList.add(ıtem);
                    }
                    callAdapter();
                }
            });

            init();
        }
        return view;
    }


    private void init() {
        openDialogButton = view.findViewById(R.id.openDialogButton);
        openDialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
        restInterface = ApiClient.getClient().create(RestInterface.class);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        itemList = new ArrayList<>();
        tempItemList = new ArrayList<>();
        post = new Post();
        favItemViewModel = ViewModelProviders.of(this).get(FavItemViewModel.class);
        favItemViewModel.getAllFavItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                postAdapter.setFavItemList(items);
            }
        });
        postAdapter = new PostAdapter(itemList,getActivity(),searchWord,favItemViewModel);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), VERTICAL, false);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && !isLoading) {
                    currentItems = layoutManager.getChildCount();
                    totalItems = layoutManager.getItemCount();
                    scrollOutItems = layoutManager.findFirstCompletelyVisibleItemPosition();
                    if (currentItems + scrollOutItems >= totalItems) {
                        searchFragmentViewModel.setIsBack(false, tempSearchWord);
                        progressBar.setVisibility(View.VISIBLE);
                        isLoading = true;
                        searchFragmentViewModel.search(tempSearchWord, (tempItemList.size() / 25) + 1);
                    }
                }
            }
        });
    }

    public void openDialog() {
        SearchDialog searchDialog = new SearchDialog();
        searchDialog.show(getFragmentManager(), "Arama Penceresi");
        searchDialog.setListener(this);
    }

    @Override
    public void applyTexts(String searchedWord) {
        searchFragmentViewModel.setIsBack(false,searchedWord);
        this.searchWord = searchedWord;
        tempItemList.clear();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Bekleyin...");
        progressDialog.setOnCancelListener(new Dialog.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                searchFragmentViewModel.setIsBack(true,tempSearchWord);
                searchWord = tempSearchWord;
                updateTmpLst();
            }
        });
        progressDialog.show();
        searchFragmentViewModel.search(searchWord, 1);
    }

    boolean itemControl(ArrayList<Item> itemList) {
        for (Item item : itemList) {
            if (!item.avaliable()) {
                return false;
            }
        }
        return true;
    }

    void callAdapter() {
        if (itemControl(tempItemList)) {
            if (tempItemList.size() > 25) {
                this.itemList.clear();
                itemList.addAll(tempItemList);
                postAdapter.setItemList(itemList);
                progressBar.setVisibility(View.GONE);
                isLoading = false;
            } else {
                isLoading = false;
                if (progressDialog != null && progressDialog.isShowing()) {
                    this.itemList.clear();
                   itemList.addAll(tempItemList);
                    postAdapter.setItemList(itemList);
                    recyclerView.setAdapter(postAdapter);
                    progressBar.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    tempSearchWord = searchWord;
                }
            }
        }
    }

    void updateTmpLst() {
        tempItemList.clear();
        tempItemList.addAll(itemList);
    }


    public void setPostAdapter(ArrayList<Item> list) {
        tempItemList.clear();
        tempItemList.addAll(list);
        searchFragmentViewModel.setTempItemList(tempItemList);
        callAdapter();

    }


}
