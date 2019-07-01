package com.example.flickrsearchengine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    ArrayList<Item> itemList;
    LayoutInflater layoutInflater;
    Activity context;
    String searchWord;


    public  PostAdapter (ArrayList<Item> itemList, Activity context, String searchWord){
        this.itemList=itemList;
        this.context=context;
        this.searchWord=searchWord;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.row_list, parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item=itemList.get(position);
        if (item!=null) {
            if (item.getId()!=null && item.getDescription()!=null && item.getLargeImg()!=null && item.getLat() != 0 && item.getLng() != 0 && item.getOwner()!=null&& item.getSmallImg()!=null && item.getTitle()!=null) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtOwner.setText(item.getOwner());
                holder.txtDescription.setText(item.getDescription());
                Picasso.get().load(item.getSmallImg()).placeholder(R.drawable.ic_launcher_foreground).into(holder.image);
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
                        context.startActivityForResult(intent,1);

                    }
                });

            }
        }
    }


    @Override
    public int getItemCount() {
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
