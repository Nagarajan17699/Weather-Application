package com.example.weatherapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class WeatherWeekActivity extends AppCompatActivity {

    private static final String TAG = "WeatherWeekActivity";
    private RecyclerView recyclerView;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private WeatherWeekAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_for_week);

        Intent intent = getIntent();
        ArrayList<WeatherDays> wDays = (ArrayList<WeatherDays>) intent.getSerializableExtra("weatherweeks");
        String unit_code = intent.getStringExtra("unit_code");
        String city = intent.getStringExtra("city");
        Log.d(TAG, "onCreate: "+ unit_code+" "+city);
        this.setTitle(city.substring(0,1).toUpperCase()+city.substring(1)+", 15 Day");
        Log.d(TAG, "onCreate: "+wDays);

        recyclerView = findViewById(R.id.recyclerView_week);
        adapter = new WeatherWeekAdapter(wDays, this, unit_code);
        recyclerView.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


    }
}