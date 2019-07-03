package com.example.flickrsearchengine.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.flickrsearchengine.R;
import com.example.flickrsearchengine.adapters.FavAdapter;
import com.example.flickrsearchengine.itemObjects.FavItem;
import com.example.flickrsearchengine.viewModels.FavItemViewModel;

import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class FavFragment extends Fragment {
    FavAdapter favAdapter;
    View view;
    RecyclerView recyclerView;
    private FavItemViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_fav, container, false);
        init();

        return view;
    }
    void init(){
        recyclerView=view.findViewById(R.id.favView);
        viewModel= ViewModelProviders.of(this).get(FavItemViewModel.class);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        favAdapter=new FavAdapter(viewModel,getActivity());
        recyclerView.setAdapter(favAdapter);
        viewModel.getAllFavItems().observe(this, new Observer<List<FavItem>>() {
            @Override
            public void onChanged(List<FavItem> favItems) {
                favAdapter.setFavItems(favItems);
            }
        });





    }
}
