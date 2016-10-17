package com.example.dmitriyoschepkov.socialplant;

/**
 * Created by Dmitriy.Oschepkov on 15.10.2016.
 */
import java.util.ArrayList;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ListPlantsAdapter extends BaseAdapter{
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ListPlants> objects;
    SQLiteDatabase db;
    Picasso mPicasso;
    LayoutInflater mInflater;
    ListPlantsAdapter(Context context, ArrayList<ListPlants> listPlants){
        ctx = context;
        objects = listPlants;
        lInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater = LayoutInflater.from(context);
        mPicasso = Picasso.with(context);
    }
    @Override
    public int getCount(){
        return objects.size();
    }
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position, View contentView, ViewGroup parent){
        View view = contentView;
        if (view == contentView){
            view = lInflater.inflate(R.layout.main_cards_list_view, parent, false);
        }
        ListPlants l = getListPlants(position);
        ((TextView)view.findViewById(R.id.namePlant)).setText(l.name);
        ((TextView)view.findViewById(R.id.aboutPlant)).setText(l.about);
        mPicasso.load("file://" + l.image)
                .fit()
                .placeholder(R.drawable.plant)
                .error(R.drawable.plant)
                .centerCrop()
                .into((ImageView) view.findViewById(R.id.imageView2));
        return view;
    }
    ListPlants getListPlants(int position){
        return ((ListPlants)getItem(position));
    }

}
