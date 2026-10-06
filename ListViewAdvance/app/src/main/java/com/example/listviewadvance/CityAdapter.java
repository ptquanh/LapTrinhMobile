package com.example.listviewadvance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.listviewadvance.model.City;

import java.util.List;

public class CityAdapter extends BaseAdapter {
    private Context context;

    private int layout;
    private List<City> cityList;

    public CityAdapter(Context context, int layout, List<City> cityList) {
        this.context = context;
        this.layout = layout;
        this.cityList = cityList;
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout,null);
        TextView txtTen = convertView.findViewById(R.id.textTen);
        TextView txtLink = convertView.findViewById(R.id.textLink);
        ImageView imgView = convertView.findViewById(R.id.imageView);

        City city = cityList.get(position);
        txtTen.setText(city.getNameCity());
        txtLink.setText(city.getLinkWiki());
        imgView.setImageResource(city.getHinh());

        return convertView;
    }
}
