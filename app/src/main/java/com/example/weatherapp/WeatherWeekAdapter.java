package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WeatherWeekAdapter extends RecyclerView.Adapter<WeatherWeekViewHolder>{

    public ArrayList<WeatherDays> ald;
    public WeatherWeekActivity wWeekAct;
    public String unit_code;
    private static final String TAG = "WeatherWeekAdapter";

    public WeatherWeekAdapter(ArrayList<WeatherDays> ald, WeatherWeekActivity wWeekAct, String unit_code) {
        this.ald = ald;
        this.wWeekAct = wWeekAct;
        this.unit_code = unit_code;
    }

    @NonNull
    @Override
    public WeatherWeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_for_week, parent, false);

        return new WeatherWeekViewHolder(itemView);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WeatherWeekViewHolder holder, int position) {

        WeatherDays wDays = ald.get(position);
        Log.d(TAG, "onBindViewHolder: "+ isImperial());
        String unit = isImperial() ? "F" : "C";
        Locale locale = new Locale("en", "US");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MM/dd", locale);
        Date resultTime = new Date(Long.parseLong(wDays.dateTimeEpoch) * 1000);
        holder.day_date.setText("\t"+sdf.format(resultTime));


        String max_min = (int)Math.round(Double.parseDouble(wDays.tempMax))+"\u00B0"+unit+"/"+(int)Math.round(Double.parseDouble(wDays.tempMin))+"\u00B0"+unit;
        holder.max_min.setText(max_min);

        holder.description.setText(wDays.description);

        String precip = "("+wDays.precipProb+"% precip.)";
        holder.precipiration.setText(precip);

        String uv_string = "UV Index: "+wDays.uvIndex;
        holder.uvindex.setText(uv_string);

        String icon_name = wDays.icon.replace('-', '_');
        String uri = "@drawable/" + icon_name;
        int imgRes = wWeekAct.getResources().getIdentifier(uri, null, wWeekAct.getPackageName());
        holder.weather_img.setImageDrawable(wWeekAct.getResources().getDrawable(imgRes));

        String m_temp = (int) Math.round(Double.parseDouble(wDays.wHours.get(7).temp)) + "\u00B0"+unit;
        holder.morning_temperature.setText(m_temp);

        String af_temp = (int) Math.round(Double.parseDouble(wDays.wHours.get(12).temp)) + "\u00B0"+unit;
        holder.afternoon_temperature.setText(af_temp);

        String eve_temp = (int) Math.round(Double.parseDouble(wDays.wHours.get(16).temp)) + "\u00B0"+unit;
        holder.evening_temperature.setText(eve_temp);

        String n_temp = (int) Math.round(Double.parseDouble(wDays.wHours.get(22).temp)) + "\u00B0"+unit;
        holder.night_temperature.setText(n_temp);

        holder.morning_label.setText(wWeekAct.getString(R.string.morning));
        holder.afternoon_label.setText(wWeekAct.getString(R.string.afternoon));
        holder.evening_label.setText(wWeekAct.getString(R.string.evening));
        holder.night_label.setText(wWeekAct.getString(R.string.night));

    }

    public boolean isImperial(){
        return unit_code.equals("us") ? true : false;
    }

    @Override
    public int getItemCount() {
        return ald.size();
    }


}
