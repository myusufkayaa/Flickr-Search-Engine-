package com.example.flickrsearchengine;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SlideAdapter extends PagerAdapter {
    LayoutInflater inflater;
    Activity context;
    ArrayList<Item> itemList;
    TextView textView;
    GoogleMap map;
    ImageView imageView;


    public SlideAdapter(Activity context, ArrayList<Item> itemList, TextView textView, GoogleMap map) {
        this.context = context;
        this.itemList = itemList;
        this.textView = textView;
        this.map = map;

    }

    @Override
    public int getCount() {
        return itemList.size();
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



        try {
            Picasso.get()
                    .load(itemList.get(position).getLargeImg()).placeholder(R.drawable.ic_launcher_foreground)
                    .into(imageView);
        } catch (Exception ex) {
            Log.d("catch", "Hata var");
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
    public  void setItemList(ArrayList<Item> itemList){
        this.itemList=itemList;
    }
}
