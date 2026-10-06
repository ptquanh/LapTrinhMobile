package com.example.demolistviewnangcao;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demolistviewnangcao.model.City;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvCity;
    ArrayList<City> cityArrayList = new ArrayList<>();
    CityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvCity = findViewById(R.id.lvCity);

        cityArrayList.add(new City("New York", R.drawable.bien_giga_chad, "https://en.wikipedia.org/wiki/New_York_City"));
        cityArrayList.add(new City("Biển Surprise", R.drawable.bien_surprise, "https://en.wikipedia.org/wiki/Surprise"));
        cityArrayList.add(new City("Tokyo", R.drawable.bien_giga_chad, "https://en.wikipedia.org/wiki/Tokyo"));
        cityArrayList.add(new City("Paris", R.drawable.bien_surprise, "https://en.wikipedia.org/wiki/Paris"));

        adapter = new CityAdapter(this, R.layout.dong_thanh_pho, cityArrayList);
        lvCity.setAdapter(adapter);
    }
}