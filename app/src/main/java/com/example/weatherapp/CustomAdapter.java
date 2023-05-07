package com.example.weatherapp;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<Weather> arrayList;

    public CustomAdapter(Context context, ArrayList<Weather> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.listviews_elements, null);

        Weather weather = arrayList.get(i);
        TextView txtDate = (TextView) view.findViewById(R.id.textViewDay);
        TextView txtCondition = (TextView) view.findViewById(R.id.textViewConditions);
        TextView txtMaxTemp = (TextView) view.findViewById(R.id.textViewMaxTemp);
        TextView txtMinTemp = (TextView) view.findViewById(R.id.textViewMinTemp);
        ImageView imgCondition =  (ImageView) view.findViewById(R.id.imageViewConditions);

        txtDate.setText(weather.Day);
        txtCondition.setText(weather.Status);
        txtMaxTemp.setText(weather.MaxTemp + "°F");
        txtMinTemp.setText(weather.MinTemp + "°F");

        Picasso.get().load("https://openweathermap.org/img/wn/" + weather.Image + ".png").into(imgCondition);
        return view;
    }
}
