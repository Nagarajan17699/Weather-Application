package com.example.weatherapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeatherDayViewHolder extends RecyclerView.ViewHolder{

    TextView today;
    TextView time;
    ImageView weather_img;
    TextView condition;
    TextView temp;


    public WeatherDayViewHolder(@NonNull View itemView) {
        super(itemView);

        today = itemView.findViewById(R.id.today);
        time = itemView.findViewById(R.id.time);
        weather_img = itemView.findViewById(R.id.weather_img);
        condition = itemView.findViewById(R.id.condition);
        temp = itemView.findViewById(R.id.temp);

    }
}
