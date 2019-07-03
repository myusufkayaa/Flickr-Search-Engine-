package com.example.flickrsearchengine.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flickrsearchengine.itemObjects.FavItem;
import com.example.flickrsearchengine.itemObjects.Item;
import com.example.flickrsearchengine.database.ItemDao;
import com.example.flickrsearchengine.R;
import com.example.flickrsearchengine.activities.ShowActivity;
import com.example.flickrsearchengine.viewModels.FavItemViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    ArrayList<Item> itemList;
    LayoutInflater layoutInflater;
    Activity context;
    String searchWord;
    FavItemViewModel viewModel;
  static   List<FavItem> favItemList;


    public  PostAdapter (ArrayList<Item> itemList, Activity context, String searchWord,FavItemViewModel viewModel){
        this.itemList=itemList;
        this.context=context;
        this.searchWord=searchWord;
        this.viewModel=viewModel;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.row_list, parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    public PostAdapter(FavItemViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Item item=itemList.get(position);
        if (item!=null) {
            if (item.getId()!=null && item.getDescription()!=null && item.getLargeImg()!=null && item.getLat() != 0 && item.getLng() != 0 && item.getOwner()!=null&& item.getSmallImg()!=null && item.getTitle()!=null) {
                holder.likeButton.setText("LIKE");
                if (favItemList!=null){
                    for (FavItem favItem:favItemList){
                        if (item.getId().equals(String.valueOf(favItem.getFavId()))){
                            holder.likeButton.setText("DISLIKE");
                        }
                    }
                }

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
                holder.likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (holder.likeButton.getText().equals("LIKE")){
                                    viewModel.insert(new FavItem(Long.parseLong(item.getId()),item.getTitle(),item.getDescription(),item.getOwner(),item.getLargeImg(),item.getSmallImg(),item.getLat(),item.getLng()));
                                    holder.likeButton.setText("DISLIKE");
                                }else{
                                    for (FavItem favItem: favItemList)
                                        if (item.getId().equals(String.valueOf(favItem.getFavId()))){
                                            viewModel.delete(favItem);
                                            holder.likeButton.setText("LIKE");
                                        }


                                }
                            }
                        }).start();

                    }
                });

            }
        }
    }

    public void setFavItemList(List<FavItem> favItemList){
        this.favItemList=favItemList;
        notifyDataSetChanged();
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