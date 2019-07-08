package com.example.flickrsearchengine.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.flickrsearchengine.itemObjects.Item;
import com.example.flickrsearchengine.R;
import com.google.android.gms.maps.GoogleMap;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SlideAdapter extends PagerAdapter {
    LayoutInflater inflater;
    Activity context;
    ArrayList<Item> itemList;
    TextView textView;
    GoogleMap map;
    ImageView imageView;
    List<Item> favItemList;


    public SlideAdapter(Activity context, ArrayList<Item> itemList, TextView textView, GoogleMap map) {
        this.context = context;
        this.itemList = itemList;
        this.textView = textView;
        this.map = map;

    }
    public SlideAdapter(Activity context, List<Item> itemList, TextView textView, GoogleMap map) {
        this.context = context;
        this.favItemList = itemList;
        this.textView = textView;
        this.map = map;

    }

    @Override
    public int getCount() {
       if (itemList!=null)
        return itemList.size();
       else return favItemList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position){
        Log.d("Viewpager","position: "+position);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.slider, container,false);
        imageView = itemView.findViewById(R.id.adapterImage);
        imageView.setImageResource(R.drawable.com_facebook_button_background);
        if (itemList!=null){
            try {
                Picasso.get()
                        .load(itemList.get(position).getLargeImg()).placeholder(R.drawable.ic_launcher_foreground)
                        .into(imageView);
            } catch (Exception ex) {
                Log.d("catch", "Hata var");
            }
        }else{
            try {
                Picasso.get()
                        .load(favItemList.get(position).getLargeImg()).placeholder(R.drawable.ic_launcher_foreground)
                        .into(imageView);
            } catch (Exception ex) {
                Log.d("catch", "Hata var");
            }
        }
        container.addView(itemView);
        return itemView;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }
    public  void setFavItemList(List<Item> itemList){
        this.favItemList=itemList;
        notifyDataSetChanged();
    }
    public void setItemList(ArrayList<Item> itemList){
        this.itemList=itemList;
        notifyDataSetChanged();
    }
}
