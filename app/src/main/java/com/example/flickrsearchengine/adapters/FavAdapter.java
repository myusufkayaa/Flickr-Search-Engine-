package com.example.flickrsearchengine.adapters;

import android.app.Activity;
import android.content.Intent;
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

import com.example.flickrsearchengine.R;
import com.example.flickrsearchengine.activities.ShowActivity;
import com.example.flickrsearchengine.Models.itemObjects.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {
    ArrayList<Item> itemList;
    LayoutInflater layoutInflater;
    Activity context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void setFavItems(ArrayList<Item> itemList){
        this.itemList=itemList;
        notifyDataSetChanged();
    }

    public FavAdapter(Activity context) {
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.row_list, parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Item item=itemList.get(position);
        if (item!=null) {
                holder.likeButton.setText("DISLIKE");
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
                        intent.putParcelableArrayListExtra("favlist", itemList);
                        intent.putExtra("favposition",position);
                        context.startActivityForResult(intent,1);
                    }
                });
                holder.likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (holder.likeButton.getText().equals("LIKE")){
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
                                }else{
                                    db.collection(mAuth.getUid()).document(String.valueOf(item.getPhotoId())).delete().addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    });
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





    @Override
    public int getItemCount() {
        if (itemList==null) return 0;
        return itemList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtOwner,txtTitle,txtDescription;
        ImageView image;
        Button likeButton;
        ConstraintLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle=itemView.findViewById(R.id.slideTitle);
            txtOwner=itemView.findViewById(R.id.txtOwner);
            txtDescription=itemView.findViewById(R.id.txtDescription);
            layout=itemView.findViewById(R.id.layout);
            image=itemView.findViewById(R.id.slideImage);
            likeButton=itemView.findViewById(R.id.btnLike);

        }
    }


}
