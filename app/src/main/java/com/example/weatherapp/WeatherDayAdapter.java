package com.example.weatherapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WeatherDayAdapter extends RecyclerView.Adapter<WeatherDayViewHolder> {

    private static final String TAG="WeatherDayAdapter";

    public ArrayList<WeatherHours> alw;
    public MainActivity mainAct;
    public String units_code;

    public WeatherDayAdapter(ArrayList<WeatherHours> alw, String units, MainActivity mainAct) {
        this.alw = alw;
        this.mainAct = mainAct;
        this.units_code = units;
    }

    @NonNull
    @Override
    public WeatherDayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_for_day, parent, false);

        return new WeatherDayViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherDayViewHolder holder, int position) {

        WeatherHours wHours = alw.get(position);
        String units = units_code == "us" ? "F" : "C";

        //ZoneId zoneid = ZoneId.of("america/chicago");

            Locale locale = new Locale("en", "US");
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", locale);
            Date resultTime = new Date(Long.parseLong(wHours.dateTimeEpoch) * 1000);

            holder.time.setText(sdf.format(resultTime));

            Format f = new SimpleDateFormat("EEEE");
            String str = f.format(new Date());

            SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE", locale);
            Date day_name = new Date(Long.parseLong(wHours.dateTimeEpoch) * 1000);
            String day_name_str = sdf2.format(day_name);

            if (str.equals(day_name_str))
                holder.today.setText(R.string.today);
            else {
                holder.today.setText(day_name_str);
            }

            String icon_name = wHours.icon.replace('-', '_');
            String uri = "@drawable/" + icon_name;
            int imgRes = mainAct.getResources().getIdentifier(uri, null, mainAct.getPackageName());
            holder.weather_img.setImageDrawable(mainAct.getResources().getDrawable(imgRes));

            int temp_dbl = (int) Math.round(Double.parseDouble(wHours.temp));
            String temp_disp = temp_dbl+"\u00B0"+units;
            holder.temp.setText(temp_disp);
            holder.condition.setText(wHours.conditions);
        }

    @Override
    public int getItemCount() {
        return alw.size();
    }
}
