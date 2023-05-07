package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    String cityName = "";

    ImageView imgClose;

    TextView txtName;

    ListView lv;

    CustomAdapter customAdapter;
    ArrayList<Weather> weathersArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Element();
        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
        if (city.equals("")) {
            cityName = "Fullerton";
            Get5DaysData(cityName);
            Log.d("Result", "Data: " + city);
        }
        else {
            cityName = city;
            Get5DaysData(cityName);
            Log.d("Result", "Data: " + city);
        }
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void Element() {
        imgClose = (ImageView) findViewById(R.id.close_btn);
        txtName = (TextView) findViewById(R.id.cityName);
        lv = (ListView) findViewById(R.id.tempContainer);
        weathersArray = new ArrayList<Weather>();
        customAdapter = new CustomAdapter(MainActivity2.this, weathersArray);
        lv.setAdapter(customAdapter);
    }

    private void Get5DaysData(String data) {
        String url = "https://api.openweathermap.org/data/2.5/forecast?q="+ data + "&units=imperial&appid=ffbf405f7887779ea3f000b2cd78420e";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity2.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectCity = jsonObject.getJSONObject("city");
                            String name = jsonObjectCity.getString("name");
                            txtName.setText(name);

                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");

                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                                String weekDays = jsonObjectList.getString("dt").substring(0, 10);

                                long changeDateFormat = Long.valueOf(weekDays);
                                Date date =  new Date(changeDateFormat * 1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE HH:mm");
                                String Day = simpleDateFormat.format(date);

                                JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("main");
                                String max = jsonObjectTemp.getString("temp_max");
                                String min = jsonObjectTemp.getString("temp_min");

                                Double changeMaxFormat = Double.valueOf(max);
                                Double changeMinFormat = Double.valueOf(min);
                                String intMaxFormat = String.valueOf(changeMaxFormat.intValue());
                                String intMinFormat = String.valueOf(changeMinFormat.intValue());

                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String description = jsonObjectWeather.getString("description");
                                String icon = jsonObjectWeather.getString("icon");

                                weathersArray.add(new Weather(Day, description, icon, intMaxFormat, intMinFormat));
                            }
                            customAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }
}