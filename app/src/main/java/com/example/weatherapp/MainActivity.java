package com.example.weatherapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public WeatherCurrent weatherCurrent;
    public static String units_for_url = "us";
    public static String city_for_url = "chicago";
    private static final String TAG = "MainActivity";
    public TextView date_time;
    public TextView temperature;
    public TextView feels_like;
    public ImageView weather_img;
    public TextView weather_desc;
    public TextView wind_desc;
    public TextView humidity;
    public TextView uvIndex;
    public TextView visibility;
    public TextView morning_temp;
    public TextView afternoon_temp;
    public TextView evening_temp;
    public TextView night_temp;
    public TextView morning_text;
    public TextView afternoon_text;
    public TextView evening_text;
    public TextView night_text;
    public TextView sunrise_time;
    public TextView sunset_time;
    public Menu menu_ref;
    public SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;

    private WeatherDayAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private WeatherWeeks weather_weeks;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: "+units_for_url+" "+city_for_url);
        swipeRefreshLayout = findViewById(R.id.swiper);
        recyclerView = findViewById(R.id.weatherByHour);
        date_time = findViewById(R.id.date_time);
        temperature = findViewById(R.id.temperature);
        feels_like = findViewById(R.id.feels_like);
        weather_desc = findViewById(R.id.weather_desc);
        wind_desc = findViewById(R.id.wind_desc);
        humidity = findViewById(R.id.humidity);
        uvIndex = findViewById(R.id.uvIndex);
        visibility = findViewById(R.id.visibility);
        morning_temp = findViewById(R.id.morning_temp);
        afternoon_temp = findViewById(R.id.afternoon_temp);
        evening_temp = findViewById(R.id.evening_temp);
        night_temp = findViewById(R.id.night_temp);
        morning_text = findViewById(R.id.morning_text);
        afternoon_text = findViewById(R.id.afternoon_text);
        evening_text = findViewById(R.id.evening_text);
        night_text = findViewById(R.id.night_text);
        sunrise_time = findViewById(R.id.sunrise_time);
        sunset_time = findViewById(R.id.sunset_time);
        weather_img = findViewById(R.id.current_image);

        if(hasNetworkConnection()) {
            Log.d(TAG, "onCreate: hasNetworkConnection(): "+hasNetworkConnection());
            if(!getCityUnits()){
                writeCityUnits();
            }
            Log.d(TAG, "onCreate: after getting"+units_for_url+" "+city_for_url);
            downloadData();
        }
        else{
            hideView();
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_LONG).show();
        }



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(hasNetworkConnection()) {
                    downloadData();
                    showView();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    hideView();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    public void updateCurrentData(WeatherCurrent w){

        Log.d(TAG, "updateCurrentData: "+ w);
        if (w == null) {
            Toast.makeText(this, "Please Enter a Valid City Name", Toast.LENGTH_SHORT).show();
            return;
        } else {

            String unit = isImperial() ? "F" : "C";
            String unit_wind = isImperial() ? "mph" : "kmph";
            String unit_visibility = isImperial() ? "mi" : "km";


            date_time.setText(w.date);

            int temp_int = (int) Math.round(Double.parseDouble(w.temp));
            String temp = (temp_int) + "\u00B0" + unit;
            temperature.setText(temp);

            int feelsLike_int = (int) Math.round(Double.parseDouble(w.feelsLike));
            String feelslike_t = "Feels Like " + String.valueOf(feelsLike_int) + " \u00B0" + unit;
            feels_like.setText(feelslike_t);

            String clCover = String.valueOf((int)Math.round(Double.parseDouble(w.cloudCover)));
            String weatherDesc = w.conditions + " (" + clCover + " % clouds)";
            weather_desc.setText(weatherDesc);

            String windDesc = "Winds: " + getDirection(Double.parseDouble(w.windDir)) + " at " + w.windSpeed + " " + unit_wind + ((w.windGust.equalsIgnoreCase("null")) ? "" : (" gushing to " + w.windGust + " " + unit_wind));
            Log.d(TAG, "updateCurrentData: windgust" + w.windGust);
            wind_desc.setText(windDesc);

            String humid = "Humidity: "+(int)Math.round(Double.parseDouble(w.humidity))+"%";
            humidity.setText(humid);

            String unIndex_t = "UV Index: " + w.uvIndex;
            uvIndex.setText(unIndex_t);

            String visibility_t = "Visibility: " + w.visibility +" "+unit_visibility;
            visibility.setText(visibility_t);

            Locale locale = new Locale("en", "US");

            long sunrise_time_val = Long.parseLong(w.sunriseEpoch);
            String sunrise = "Sunrise: " + new SimpleDateFormat("hh:mm aa", locale).format(new Date(sunrise_time_val * 1000));
            sunrise_time.setText(sunrise);

            long sunset_time_val = Long.parseLong(w.sunsetEpoch);
            String sunset = "Sunset: " + new SimpleDateFormat("hh:mm aa", locale).format(new Date(sunset_time_val * 1000));
            sunset_time.setText(sunset);

            String icon_name = w.icon.replace('-', '_');
            String uri = "@drawable/" + icon_name;
            Log.d(TAG, "updateCurrentData: icon Name" + w.icon);
            int imgRes = getResources().getIdentifier(uri, null, getPackageName());
            weather_img.setImageDrawable(getResources().getDrawable(imgRes));

        }
    }

    public void updateWeekData(WeatherWeeks wWeeks){

        weather_weeks = wWeeks;
        if(weather_weeks == null){
            Toast.makeText(this,"Enter correct city name", Toast.LENGTH_SHORT).show();
        } else {

            String addr = wWeeks.address;
            this.setTitle(addr.substring(0,1).toUpperCase()+addr.substring(1));
            String unit = isImperial() ? "F" : "C";
            Log.d(TAG, "updateWeekData: " + weather_weeks);
            String m_temp = String.valueOf((int) Math.round(Double.parseDouble(wWeeks.wDays.get(0).wHours.get(7).temp)) + "\u00B0" + unit);
            morning_temp.setText(m_temp);

            String af_temp = String.valueOf((int) Math.round(Double.parseDouble(wWeeks.wDays.get(0).wHours.get(12).temp)) + "\u00B0" + unit);
            afternoon_temp.setText(af_temp);

            String eve_temp = String.valueOf((int) Math.round(Double.parseDouble(wWeeks.wDays.get(0).wHours.get(16).temp)) + "\u00B0" + unit);
            evening_temp.setText(eve_temp);

            String n_temp = String.valueOf((int) Math.round(Double.parseDouble(wWeeks.wDays.get(0).wHours.get(22).temp)) + "\u00B0" + unit);
            night_temp.setText(n_temp);

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                morning_text.setText("8am");
                afternoon_text.setText("1pm");
                evening_text.setText("5pm");
                night_text.setText("11pm");
            } else {
                morning_text.setText("Morning");
                afternoon_text.setText("Afternoon");
                evening_text.setText("Evening");
                night_text.setText("Night");
            }


            ArrayList<WeatherHours> al3 = new ArrayList<>();
            al3.addAll(wWeeks.wDays.get(0).wHours);
            al3.addAll(wWeeks.wDays.get(1).wHours);
            al3.addAll(wWeeks.wDays.get(2).wHours);
            al3.addAll(wWeeks.wDays.get(3).wHours);

            adapter = new WeatherDayAdapter(al3, units_for_url, this);
            recyclerView.setAdapter(adapter);
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    public boolean onCreateOptionsMenu(@NotNull Menu menu){
        menu_ref = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);
        String uri = units_for_url.equals("us") ? "@drawable/units_f" : "@drawable/units_c";
        int imgRes = getResources().getIdentifier(uri, null, getPackageName());
        menu_ref.getItem(0).setIcon(imgRes);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuitm){

        if(!hasNetworkConnection()){
            Toast.makeText(this, "No Internet Connection, Cannot use the options !", Toast.LENGTH_SHORT).show();
        }
        else{
            if(menuitm.getItemId() == R.id.week_weather){
                Intent intent = new Intent(this, WeatherWeekActivity.class);
                intent.putExtra("weatherweeks", weather_weeks.wDays);
                intent.putExtra("unit_code",units_for_url);
                intent.putExtra("city", city_for_url);
                startActivity(intent);
            } else if(menuitm.getItemId() == R.id.units){
                Log.d(TAG, "onOptionsItemSelected: "+menuitm.getIcon().toString());
                if(units_for_url.equals("us")){
                    units_for_url = "metric";
                    String uri = "@drawable/units_c";
                    int imgRes = getResources().getIdentifier(uri, null, getPackageName());
                    menu_ref.getItem(0).setIcon(getResources().getDrawable(imgRes));
                    writeCityUnits();
                    downloadData();
                } else if(units_for_url.equals("metric")) {
                    units_for_url = "us";
                    String uri = "@drawable/units_f";
                    int imgRes = getResources().getIdentifier(uri, null, getPackageName());
                    menu_ref.getItem(0).setIcon(getResources().getDrawable(imgRes));
                    writeCityUnits();
                    downloadData();
                }
            }
            else if(menuitm.getItemId() == R.id.location){
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setTitle("Enter a Location");
                alertBuilder.setMessage("\n\nFor US Location, enter city or city, state \n\nFor international locations, enter as 'City, Country");
                final EditText inp = new EditText(this);
                inp.setInputType(InputType.TYPE_CLASS_TEXT);
                alertBuilder.setView(inp);
                alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        city_for_url = inp.getText().toString();
                        downloadData();
                        writeCityUnits();
                    }
                });

                alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                alertBuilder.show();
            }
        }

        return true;
    }


    private String getDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5 && degrees < 337.5)
            return "NW";
        return "X"; // We'll use 'X' as the default if we get a bad value
    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    public void downloadData(){
        Log.d(TAG, "downloadData: Downloading Data ...");
        GetWeatherData.downloadCurrentWeather(this, city_for_url,isImperial());
        GetWeatherData.getWeatherWeekData(this,city_for_url,isImperial());
    }

    public boolean isImperial(){

        Log.d(TAG, "isImperial: "+ units_for_url);
        return units_for_url.equals("us") ? true : false;
    }

    public void hideView(){
        date_time.setText("No Internet Connection");
        temperature.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        feels_like.setVisibility(View.INVISIBLE);
        weather_desc.setVisibility(View.INVISIBLE);
        wind_desc.setVisibility(View.INVISIBLE);
        humidity.setVisibility(View.INVISIBLE);
        uvIndex.setVisibility(View.INVISIBLE);
        visibility.setVisibility(View.INVISIBLE);
        morning_temp.setVisibility(View.INVISIBLE);
        afternoon_temp.setVisibility(View.INVISIBLE);
        evening_temp.setVisibility(View.INVISIBLE);
        night_temp.setVisibility(View.INVISIBLE);
        morning_text.setVisibility(View.INVISIBLE);
        afternoon_text.setVisibility(View.INVISIBLE);
        evening_text.setVisibility(View.INVISIBLE);
        night_text.setVisibility(View.INVISIBLE);
        sunrise_time.setVisibility(View.INVISIBLE);
        sunset_time.setVisibility(View.INVISIBLE);
        weather_img.setVisibility(View.INVISIBLE);
    }

    public void showView(){
        temperature.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        feels_like.setVisibility(View.VISIBLE);
        weather_desc.setVisibility(View.VISIBLE);
        wind_desc.setVisibility(View.VISIBLE);
        humidity.setVisibility(View.VISIBLE);
        uvIndex.setVisibility(View.VISIBLE);
        visibility.setVisibility(View.VISIBLE);
        morning_temp.setVisibility(View.VISIBLE);
        afternoon_temp.setVisibility(View.VISIBLE);
        evening_temp.setVisibility(View.VISIBLE);
        night_temp.setVisibility(View.VISIBLE);
        morning_text.setVisibility(View.VISIBLE);
        afternoon_text.setVisibility(View.VISIBLE);
        evening_text.setVisibility(View.VISIBLE);
        night_text.setVisibility(View.VISIBLE);
        sunrise_time.setVisibility(View.VISIBLE);
        sunset_time.setVisibility(View.VISIBLE);
        weather_img.setVisibility(View.VISIBLE);
    }

    private boolean getCityUnits(){

        try{
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name)+".json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            this.city_for_url = jsonObject.getString("city");
            Log.d(TAG, "getCityUnits: "+ jsonObject.getString("city"));
            this.units_for_url = jsonObject.getString("units");
            Log.d(TAG, "getCityUnits: Units: "+  jsonObject.getString("units"));
            return true;

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "No existing user preference", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    private void writeCityUnits(){

        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.file_name) + ".json", Context.MODE_PRIVATE);
            File file = getFileStreamPath(getString(R.string.file_name) + ".json");
            PrintWriter printWriter = new PrintWriter(fos);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("city", city_for_url);
            jsonObject.put("units", units_for_url);
            Log.d(TAG, "writeCityUnits: "+ jsonObject.toString());
            printWriter.write(String.valueOf(jsonObject));
            printWriter.println();
            printWriter.close();
            fos.close();
            //Toast.makeText(this, "JSON Saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(this, "JSON Not Saved. Error Occured", Toast.LENGTH_LONG).show();
        }

    }
}