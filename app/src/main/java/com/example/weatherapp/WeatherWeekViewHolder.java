package com.example.weatherapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class WeatherWeekViewHolder extends RecyclerView.ViewHolder{

    TextView day_date;
    TextView max_min;
    TextView description;
    TextView precipiration;
    TextView uvindex;
    TextView morning_temperature;
    TextView afternoon_temperature;
    TextView evening_temperature;
    TextView night_temperature;
    TextView morning_label;
    TextView afternoon_label;
    TextView evening_label;
    TextView night_label;
    ImageView weather_img;

    public WeatherWeekViewHolder(@NonNull View itemView) {
        super(itemView);

        day_date = itemView.findViewById(R.id.day_date);
        max_min = itemView.findViewById(R.id.max_min);
        description = itemView.findViewById(R.id.description);
        precipiration = itemView.findViewById(R.id.precipiration);
        uvindex = itemView.findViewById(R.id.uvindex);
        morning_temperature = itemView.findViewById(R.id.morning_temperature);
        afternoon_temperature = itemView.findViewById(R.id.afternoon_temperature);
        evening_temperature = itemView.findViewById(R.id.evening_temperature);
        night_temperature = itemView.findViewById(R.id.night_temperature);
        morning_label = itemView.findViewById(R.id.morning_label);
        afternoon_label = itemView.findViewById(R.id.afternoon_label);
        evening_label = itemView.findViewById(R.id.evening_label);
        night_label = itemView.findViewById(R.id.night_label);
        weather_img = itemView.findViewById(R.id.weather_img_week);
    }
}
