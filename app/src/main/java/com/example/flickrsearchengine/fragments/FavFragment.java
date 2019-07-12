package com.example.flickrsearchengine.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.flickrsearchengine.R;
import com.example.flickrsearchengine.adapters.FavAdapter;
import com.example.flickrsearchengine.Models.itemObjects.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class FavFragment extends Fragment {
    FavAdapter favAdapter;
    View view;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ArrayList<Item> favItemList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fav, container, false);
        init();

        return view;
    }

    void init() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.favView);
        favItemList = new ArrayList<>();
        setFavItemList();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), VERTICAL, false);
        favAdapter = new FavAdapter(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(favAdapter);
    }

    public void setFavItemList() {
        db.collection(mAuth.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (mAuth.getCurrentUser()==null) return;
                        favItemList.clear();
                     for (QueryDocumentSnapshot document: queryDocumentSnapshots){
                         favItemList.add(new Item(document));
                     }
                     favAdapter.setFavItems(favItemList);
                    }
                });
    }

}
