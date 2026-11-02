package com.example.demoweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demoweather.R;
import com.example.demoweather.model.WeatherForecastModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WeatherAdapter extends BaseAdapter {
    private final Context context;
    private final List<WeatherForecastModel> weatherForecastModelList;

    public WeatherAdapter(Context context, List<WeatherForecastModel> weatherForecastModelList) {
        this.context = context;
        this.weatherForecastModelList = weatherForecastModelList;
    }

    @Override
    public int getCount() {
        return weatherForecastModelList != null ? weatherForecastModelList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return weatherForecastModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.layout_weather_items, viewGroup, false);
        }

        TextView txtTimeForecast = view.findViewById(R.id.txtTimeForecast);
        TextView txtTemperatureForecast = view.findViewById(R.id.txtTemperatureForecast);
        TextView txtWindForecast = view.findViewById(R.id.txtWindForecast);
        ImageView imgForecast = view.findViewById(R.id.imgForecast);

        WeatherForecastModel weatherForecastModel = weatherForecastModelList.get(i);
        txtTimeForecast.setText(formatDisplayTime(weatherForecastModel.getTime()));
        txtTemperatureForecast.setText(weatherForecastModel.getTemperature() + "°C");
        txtWindForecast.setText("Gió: " + weatherForecastModel.getWindSpeed() + " km/h");

        String iconUrl = weatherForecastModel.getFullIconUrl();
        if (iconUrl != null) {
            Picasso.get()
                    .load(iconUrl)
                    .placeholder(R.drawable.ic_weather_cloud_sun)
                    .error(R.drawable.ic_weather_cloud_sun)
                    .into(imgForecast);
        } else {
            imgForecast.setImageResource(R.drawable.ic_weather_cloud_sun);
        }

        return view;
    }

    private String formatDisplayTime(String rawTime) {
        if (rawTime == null) {
            return "";
        }
        return rawTime.trim().replaceFirst(" ", "\n");
    }
}
