package com.example.flickrsearchengine.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flickrsearchengine.Models.itemObjects.Item;
import com.example.flickrsearchengine.R;
import com.example.flickrsearchengine.activities.ShowActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    ArrayList<Item> itemList;
    LayoutInflater layoutInflater;
    Activity context;
    String searchWord;
    static List<Item> favItemList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public PostAdapter(ArrayList<Item> itemList, Activity context, String searchWord) {
        setFavItemList();
        favItemList=new ArrayList<>();
        this.itemList = itemList;
        this.context = context;
        this.searchWord = searchWord;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.row_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        setFavItemList();
        final Item item = itemList.get(position);
        if (item != null) {
            if (item.getPhotoId() != 0 && item.getDescription() != null && item.getLargeImg() != null && item.getLat() != 0 && item.getLng() != 0 && item.getOwner() != null && item.getSmallImg() != null && item.getTitle() != null) {
                holder.likeButton.setText("LIKE");
                if (itemList != null && favItemList != null) {
                    for (Item favItem : favItemList) {
                        if (item.getPhotoId() == favItem.getPhotoId()) {
                            holder.likeButton.setText("DISLIKE");
                        }
                    }
                }

                holder.txtTitle.setText(item.getTitle());
                holder.txtOwner.setText(item.getOwner());
                holder.txtDescription.setText(item.getDescription());
                Picasso.get().load(item.getSmallImg()).into(holder.image);
                holder.layout.setTag(holder);
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewHolder holder = (ViewHolder) view.getTag();
                        int position = holder.getAdapterPosition();
                        Intent intent = new Intent(context, ShowActivity.class);
                        intent.putExtra("position", position);
                        intent.putParcelableArrayListExtra("list", itemList);
                        intent.putExtra("searchWord", searchWord);
                        intent.putParcelableArrayListExtra("favlist", (ArrayList<? extends Parcelable>) favItemList);
                        context.startActivityForResult(intent, 1);


                    }
                });
                holder.likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (holder.likeButton.getText().equals("LIKE")) {
                                    Map<String, Object> post = new HashMap<>();
                                    post.put("post", item);
                                    db.collection(mAuth.getUid()).document(String.valueOf(item.getPhotoId())).set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                                        }
                                    });

                                    holder.likeButton.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            holder.likeButton.setText("DISLIKE");
                                        }
                                    });
                                } else {

                                    db.collection(mAuth.getUid()).document(String.valueOf(item.getPhotoId())).delete();

                                    holder.likeButton.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            holder.likeButton.setText("LIKE");
                                        }
                                    });


                                }
                            }
                        }).start();
                    }
                });
            }
        }
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

                    }
                });
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItemList(ArrayList<Item> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOwner, txtTitle, txtDescription;
        ImageView image;
        Button likeButton;
        ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.slideTitle);
            txtOwner = itemView.findViewById(R.id.txtOwner);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            layout = itemView.findViewById(R.id.layout);
            image = itemView.findViewById(R.id.slideImage);
            likeButton = itemView.findViewById(R.id.btnLike);

        }
    }



}
