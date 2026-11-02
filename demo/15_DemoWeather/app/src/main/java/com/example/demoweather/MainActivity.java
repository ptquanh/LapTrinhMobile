package com.example.demoweather;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.demoweather.adapter.WeatherAdapter;
import com.example.demoweather.model.WeatherForecastModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView txtCityName, txtTemperature, txtCityTemperature;
    EditText editTextTextPersonName;
    ImageView imgSearch, imgSeasonForecast;
    ListView lvWeatherForecast;
    List<WeatherForecastModel> weatherForecastModelList;
    WeatherAdapter weatherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtCityName = findViewById(R.id.txtCityName);
        txtTemperature = findViewById(R.id.txtTemperature);
        txtCityTemperature = findViewById(R.id.txtCityTemperature);
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        imgSearch = findViewById(R.id.imgSearch);
        imgSeasonForecast = findViewById(R.id.imgSeasonForecast);
        lvWeatherForecast = findViewById(R.id.lvWeatherForecast);

        weatherForecastModelList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(this, weatherForecastModelList);
        lvWeatherForecast.setAdapter(weatherAdapter);

        GetData("Hanoi");

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetData(editTextTextPersonName.getText().toString());
                //weatherAdapter.notifyDataSetChanged();
            }
        });
    }

    private void GetData(String cityName) {
        String url = "https://api.weatherapi.com/v1/forecast.json?key=fc8f4188ad3f48d8a10132707221212&q=" + cityName + "&days=1&aqi=no&alerts=no";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        Log.d("BBB", url.toString());
        txtCityName.setText(cityName);
        weatherForecastModelList.clear();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //String temperature = response.getJSONObject("current").getString("temp_c");
                        try {
                            String temperature = response.getJSONObject("current").getString("temp_c");
                            txtTemperature.setText(temperature + "°C");
                            String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                            String conditionicon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                            txtCityTemperature.setText(condition);

                            Picasso.get().load("https:" + conditionicon).into(imgSeasonForecast);
                            //Log.d("AAA",""+conditionicon);

                            JSONObject forecastObj = response.getJSONObject("forecast");
                            JSONObject forecast0 = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                            JSONArray hourArray = forecast0.getJSONArray("hour");
                            for (int i = 0; i < hourArray.length(); i++) {
                                JSONObject hourObj = hourArray.getJSONObject(i);
                                String time = hourObj.getString("time");
                                String temper = hourObj.getString("temp_c");
                                String img = hourObj.getJSONObject("condition").getString("icon");
                                String wind = hourObj.getString("wind_kph");
                                Log.d("AAA", " " + time + " " + temper + " " + img + " " + wind);
                                weatherForecastModelList.add(new WeatherForecastModel(time, temper, img, wind));
                            }
                            weatherAdapter.notifyDataSetChanged();
                            //Toast.makeText(MainActivity.this, "" + temperature , Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("AAA", "Có lỗi xảy ra" + url.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
