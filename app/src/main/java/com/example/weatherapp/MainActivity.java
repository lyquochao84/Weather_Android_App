package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText editSearch;
    Button btnSearch, btnChangeActivity;
    TextView txtCityName, txtTemp, txtStatus, txtHumidity, txtCloud, txtWind, txtRain, txtDay;
    ImageView weatherImage;
    String City = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Element();
        String lastLocation = loadLastLocation();
        GetCurrentWeatherData(lastLocation);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = editSearch.getText().toString();
                if (city.equals("")) {
                    City = "Fullerton";
                    GetCurrentWeatherData(City);
                } else {
                    City = city;
                    GetCurrentWeatherData(City);
                    saveLastLocation(City);
                }
            }
        });

        btnChangeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                String city = editSearch.getText().toString();

                intent.putExtra("name", city);
                startActivity(intent);
            }
        });
    }


    public void GetCurrentWeatherData(String data) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + data + "&appid=ffbf405f7887779ea3f000b2cd78420e&units=imperial&";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String day = jsonObject.getString("dt");
                            String name = jsonObject.getString("name");
                            txtCityName.setText("City Name: " + name);

                            long changeDateFormat = Long.valueOf(day);
                            Date date = new Date(changeDateFormat * 1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd");
                            String Day = simpleDateFormat.format(date);
                            txtDay.setText(Day);

                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String status = jsonObjectWeather.getString("main");
                            String icon = jsonObjectWeather.getString("icon");
                            Picasso.get().load("https://openweathermap.org/img/wn/" + icon + ".png").into(weatherImage);
                            txtStatus.setText(status);

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String temperature = jsonObjectMain.getString("temp");
                            String humidity = jsonObjectMain.getString("humidity");

                            Double changeTempFormat = Double.valueOf(temperature);
                            String Temperature = String.valueOf(changeTempFormat.intValue());
                            txtTemp.setText(Temperature + "°F");
                            txtHumidity.setText(humidity + "%");

                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String wind = jsonObjectWind.getString("speed");
                            txtWind.setText(wind + "m/s");

                            JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                            String cloud = jsonObjectCloud.getString("all");
                            txtCloud.setText(cloud + "%");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Please enter a valid city", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(stringRequest);
    }

    private void Element() {
        editSearch = (EditText) findViewById(R.id.search_city_et);
        btnSearch = (Button) findViewById(R.id.search_btn);
        btnChangeActivity = (Button) findViewById(R.id.buttonChangeActivity);
        txtCityName = (TextView) findViewById(R.id.cityName);
        txtTemp = (TextView) findViewById(R.id.weather_info_tv);
        txtStatus = (TextView) findViewById(R.id.weather_info_condition);
        txtCloud = (TextView) findViewById(R.id.cloudy_text);
        txtHumidity = (TextView) findViewById(R.id.humidity_text);
        txtWind = (TextView) findViewById(R.id.wind_text);
        weatherImage = (ImageView) findViewById(R.id.weatherImage);
        txtDay = (TextView) findViewById(R.id.weather_date);
    }

    private void saveLastLocation(String location) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("last_location", location);
        editor.apply();
    }

    private String loadLastLocation() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString("last_location", "Fullerton");
    }

}