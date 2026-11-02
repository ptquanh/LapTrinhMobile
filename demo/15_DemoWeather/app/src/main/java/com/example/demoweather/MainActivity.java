package com.example.demoweather;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.demoweather.adapter.WeatherAdapter;
import com.example.demoweather.data.WeatherFallbackProvider;
import com.example.demoweather.model.WeatherForecastModel;
import com.example.demoweather.receiver.NetworkChangeReceiver;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DemoWeather";
    private static final String REQUEST_TAG = "WEATHER_REQUEST";
    private static final String DEFAULT_CITY = "Hanoi";

    private TextView txtCityName;
    private TextView txtTemperature;
    private TextView txtCityTemperature;
    private EditText editTextTextPersonName;
    private ImageView imgSearch;
    private ImageView imgSeasonForecast;
    private ListView lvWeatherForecast;

    private LinearLayout layoutNetworkBanner;
    private TextView tvNetworkStatus;
    private TextView tvDataSourceBadge;
    private ProgressBar progressLoading;

    private List<WeatherForecastModel> weatherForecastModelList;
    private WeatherAdapter weatherAdapter;
    private RequestQueue requestQueue;

    private NetworkChangeReceiver networkReceiver;
    private boolean isReceiverRegistered = false;
    private boolean isShowingFallbackData = false;
    private boolean wasDisconnected = false;
    private String currentCityQuery = DEFAULT_CITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        applySystemBarInsets();
        initViews();
        initAdapterAndQueue();
        initSearchActions();
        initNetworkReceiver();

        GetData(DEFAULT_CITY);
    }

    private void applySystemBarInsets() {
        View root = findViewById(R.id.main);
        final int baseLeft = root.getPaddingLeft();
        final int baseTop = root.getPaddingTop();
        final int baseRight = root.getPaddingRight();
        final int baseBottom = root.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    baseLeft + systemBars.left,
                    baseTop + systemBars.top,
                    baseRight + systemBars.right,
                    baseBottom + systemBars.bottom
            );
            return insets;
        });
    }

    private void initViews() {
        txtCityName = findViewById(R.id.txtCityName);
        txtTemperature = findViewById(R.id.txtTemperature);
        txtCityTemperature = findViewById(R.id.txtCityTemperature);
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        imgSearch = findViewById(R.id.imgSearch);
        imgSeasonForecast = findViewById(R.id.imgSeasonForecast);
        lvWeatherForecast = findViewById(R.id.lvWeatherForecast);

        layoutNetworkBanner = findViewById(R.id.layoutNetworkBanner);
        tvNetworkStatus = findViewById(R.id.tvNetworkStatus);
        tvDataSourceBadge = findViewById(R.id.tvDataSourceBadge);
        progressLoading = findViewById(R.id.progressLoading);
    }

    private void initAdapterAndQueue() {
        weatherForecastModelList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(this, weatherForecastModelList);
        lvWeatherForecast.setAdapter(weatherAdapter);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    private void initSearchActions() {
        imgSearch.setOnClickListener(view -> triggerCitySearch());

        editTextTextPersonName.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            boolean isEnterPressed = event != null
                    && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                    && event.getAction() == KeyEvent.ACTION_DOWN;
            if (actionId == EditorInfo.IME_ACTION_SEARCH || isEnterPressed) {
                triggerCitySearch();
                return true;
            }
            return false;
        });
    }

    private void triggerCitySearch() {
        String inputCity = editTextTextPersonName.getText().toString().trim();
        if (inputCity.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên thành phố cần tra cứu!", Toast.LENGTH_SHORT).show();
            return;
        }
        hideKeyboard();
        GetData(inputCity);
    }

    private void initNetworkReceiver() {
        networkReceiver = new NetworkChangeReceiver((isConnected, networkType) -> {
            updateNetworkBanner(isConnected, networkType);
            if (!isConnected) {
                wasDisconnected = true;
                return;
            }
            if (wasDisconnected) {
                wasDisconnected = false;
                if (isShowingFallbackData) {
                    Toast.makeText(this, "Đã có mạng trở lại — tự động cập nhật dữ liệu mới!", Toast.LENGTH_SHORT).show();
                    GetData(currentCityQuery);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isReceiverRegistered) {
            IntentFilter networkFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(networkReceiver, networkFilter, Context.RECEIVER_NOT_EXPORTED);
            } else {
                registerReceiver(networkReceiver, networkFilter);
            }
            isReceiverRegistered = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isReceiverRegistered) {
            unregisterReceiver(networkReceiver);
            isReceiverRegistered = false;
        }
    }

    @Override
    protected void onDestroy() {
        if (requestQueue != null) {
            requestQueue.cancelAll(REQUEST_TAG);
        }
        super.onDestroy();
    }

    private void GetData(String cityName) {
        currentCityQuery = cityName.trim();

        if (!NetworkChangeReceiver.isNetworkAvailable(this)) {
            wasDisconnected = true;
            updateNetworkBanner(false, "Không có mạng");
            applyFallbackData(currentCityQuery, "Không có kết nối Internet. Đang hiển thị dữ liệu mẫu dự phòng.");
            return;
        }

        String url = WeatherFallbackProvider.buildForecastUrl(
                WeatherFallbackProvider.DEFAULT_API_KEY,
                currentCityQuery
        );
        Log.d(TAG, "Request URL: " + url);

        progressLoading.setVisibility(View.VISIBLE);
        requestQueue.cancelAll(REQUEST_TAG);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleWeatherResponse,
                error -> handleWeatherError(error, currentCityQuery)
        );
        jsonObjectRequest.setTag(REQUEST_TAG);
        requestQueue.add(jsonObjectRequest);
    }

    private void handleWeatherResponse(JSONObject response) {
        progressLoading.setVisibility(View.GONE);
        try {
            JSONObject locationObj = response.optJSONObject("location");
            String resolvedCity = locationObj != null
                    ? locationObj.optString("name", currentCityQuery)
                    : currentCityQuery;
            txtCityName.setText(resolvedCity);

            JSONObject currentObj = response.getJSONObject("current");
            String temperature = currentObj.getString("temp_c");
            txtTemperature.setText(temperature + "°C");

            JSONObject conditionObj = currentObj.getJSONObject("condition");
            String condition = conditionObj.getString("text");
            String conditionIcon = conditionObj.getString("icon");
            txtCityTemperature.setText(condition);

            Picasso.get()
                    .load("https:" + conditionIcon)
                    .placeholder(R.drawable.ic_weather_cloud_sun)
                    .error(R.drawable.ic_weather_cloud_sun)
                    .into(imgSeasonForecast);

            weatherForecastModelList.clear();
            JSONObject forecastObj = response.getJSONObject("forecast");
            JSONObject forecast0 = forecastObj.getJSONArray("forecastday").getJSONObject(0);
            JSONArray hourArray = forecast0.getJSONArray("hour");

            for (int i = 0; i < hourArray.length(); i++) {
                JSONObject hourObj = hourArray.getJSONObject(i);
                String time = hourObj.getString("time");
                String temper = hourObj.getString("temp_c");
                String img = hourObj.getJSONObject("condition").getString("icon");
                String wind = hourObj.getString("wind_kph");

                weatherForecastModelList.add(new WeatherForecastModel(time, temper, img, wind));
            }

            isShowingFallbackData = false;
            weatherAdapter.notifyDataSetChanged();
            lvWeatherForecast.smoothScrollToPosition(0);
            updateNetworkBanner(true, NetworkChangeReceiver.getNetworkType(this));
        } catch (JSONException e) {
            Log.e(TAG, "Lỗi phân tích JSON WeatherAPI", e);
            applyFallbackData(currentCityQuery, "Dữ liệu trả về không đúng định dạng. Đã chuyển sang dữ liệu mẫu.");
        }
    }

    private void handleWeatherError(VolleyError error, String requestedCity) {
        progressLoading.setVisibility(View.GONE);
        NetworkResponse networkResponse = error.networkResponse;

        if (networkResponse != null && networkResponse.statusCode == 400) {
            Toast.makeText(
                    this,
                    "Không tìm thấy thành phố \"" + requestedCity + "\". Vui lòng kiểm tra lại tên!",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        String reason;
        if (networkResponse != null && (networkResponse.statusCode == 401 || networkResponse.statusCode == 403)) {
            reason = "API key hết hạn hoặc vượt giới hạn (HTTP " + networkResponse.statusCode + "). Đang dùng dữ liệu mẫu dự phòng.";
        } else {
            reason = "Không thể kết nối máy chủ WeatherAPI. Đang dùng dữ liệu mẫu dự phòng.";
        }
        Log.w(TAG, reason, error);
        applyFallbackData(requestedCity, reason);
    }

    private void applyFallbackData(String cityName, String noticeMessage) {
        WeatherFallbackProvider.FallbackWeather fallback =
                WeatherFallbackProvider.createSampleWeather(cityName);

        txtCityName.setText(fallback.getCityName());
        txtTemperature.setText(fallback.getCurrentTemp() + "°C");
        txtCityTemperature.setText(fallback.getConditionText());
        imgSeasonForecast.setImageResource(R.drawable.ic_weather_cloud_sun);

        weatherForecastModelList.clear();
        weatherForecastModelList.addAll(fallback.getHourlyList());
        weatherAdapter.notifyDataSetChanged();

        isShowingFallbackData = true;
        boolean online = NetworkChangeReceiver.isNetworkAvailable(this);
        updateNetworkBanner(online, NetworkChangeReceiver.getNetworkType(this));

        Toast.makeText(this, noticeMessage, Toast.LENGTH_LONG).show();
    }

    private void updateNetworkBanner(boolean isConnected, String networkType) {
        if (!isConnected) {
            layoutNetworkBanner.setBackgroundResource(R.drawable.bg_status_offline);
            int offlineColor = ContextCompat.getColor(this, R.color.status_offline_text);
            tvNetworkStatus.setText("Mất kết nối mạng");
            tvNetworkStatus.setTextColor(offlineColor);
            tvDataSourceBadge.setText(R.string.badge_fallback);
            tvDataSourceBadge.setTextColor(offlineColor);
            return;
        }

        if (isShowingFallbackData) {
            layoutNetworkBanner.setBackgroundResource(R.drawable.bg_status_fallback);
            int fallbackColor = ContextCompat.getColor(this, R.color.status_fallback_text);
            tvNetworkStatus.setText("Đã kết nối: " + networkType);
            tvNetworkStatus.setTextColor(fallbackColor);
            tvDataSourceBadge.setText(R.string.badge_fallback);
            tvDataSourceBadge.setTextColor(fallbackColor);
            return;
        }

        layoutNetworkBanner.setBackgroundResource(R.drawable.bg_status_online);
        int onlineColor = ContextCompat.getColor(this, R.color.status_online_text);
        tvNetworkStatus.setText("Đã kết nối: " + networkType);
        tvNetworkStatus.setTextColor(onlineColor);
        tvDataSourceBadge.setText(R.string.badge_live_api);
        tvDataSourceBadge.setTextColor(onlineColor);
    }

    private void hideKeyboard() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }
    }
}
