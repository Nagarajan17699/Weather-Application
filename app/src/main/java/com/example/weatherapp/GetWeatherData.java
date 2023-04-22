package com.example.weatherapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GetWeatherData {


    private static final String TAG = "WeatherDownloadRunnable";

    private static  MainActivity mainActivity;
    private static RequestQueue queue;
    private static WeatherCurrent weatherObj;

    private static final String weatherURL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
    private static final String APIKey = "7GF7HG2RZWXSPUDYYN929HXBJ";

        public static void downloadCurrentWeather(MainActivity mainActivityParam,
                String city, boolean imperial) {

            mainActivity = mainActivityParam;

            queue = Volley.newRequestQueue(mainActivity);
            String urlToUse = getURL(city, imperial);
            Log.d(TAG, "downloadCurrentWeather: "+urlToUse);

            Response.Listener<JSONObject> listener =
                    response -> parseJSON(response.toString());

            Response.ErrorListener error =
                    error1 -> {
                mainActivity.updateCurrentData(null);
                        Log.d(TAG, "downloadCurrentWeather: data download error ..."+ error1.getMessage());
            };

            // Request a string response from the provided URL.
            JsonObjectRequest jsonObjectRequest =
                    new JsonObjectRequest(Request.Method.GET, urlToUse,
                            null, listener, error);

            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest);
        }

        private static void parseJSON(String s) {

            try {
                JSONObject currWeather = new JSONObject(s);

                // "cloud"
                JSONObject currentCondition = currWeather.getJSONObject("currentConditions");
                String conditions = currentCondition.getString("conditions");
                String cloudCover = currentCondition.getString("cloudcover");
                String icon = currentCondition.getString("icon");
                String uvIndex = currentCondition.getString("uvindex");
                String visibility = currentCondition.getString("visibility");

                // "temperature"
                String temp = currentCondition.getString("temp");
                String humidity = currentCondition.getString("humidity");
                String feelsLike = currentCondition.getString("feelslike");


                // "wind"
                String windGust = currentCondition.getString("windgust");
                String windSpeed = currentCondition.getString("windspeed");
                String windDir = currentCondition.getString("winddir");

                // "dt" section
                String datetimeEpoch = currentCondition.getString("datetimeEpoch");
                long dtepoch = Long.parseLong(datetimeEpoch);
                Locale locale = new Locale("en", "US");
                String date = new SimpleDateFormat("E MMM dd hh:mm aa, yyyy", locale).format(new Date(dtepoch * 1000));

                // "sunrise" and "sunset"
                String sunriseEpoch = currentCondition.getString("sunriseEpoch");
                String sunsetEpoch =  currentCondition.getString("sunsetEpoch");


                weatherObj = new WeatherCurrent(datetimeEpoch,  temp,  feelsLike,  humidity,  windGust,  windSpeed,  windDir,  visibility,  cloudCover,  uvIndex,  conditions,  icon,  sunriseEpoch,  sunsetEpoch, date);

               // getIcon(icon);

                mainActivity.updateCurrentData(weatherObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void getWeatherWeekData(MainActivity main, String city, Boolean imperial){

            mainActivity = main;
            queue = Volley.newRequestQueue(mainActivity);

            String url = getURL(city, imperial);

            Response.Listener<JSONObject> listener =
                    response -> parseJSON_Week(response.toString());

            Response.ErrorListener error =
                    error1 -> mainActivity.updateWeekData(null);

            // Request a string response from the provided URL.
            JsonObjectRequest jsonObjectRequest =
                    new JsonObjectRequest(Request.Method.GET, url,
                            null, listener, error);

            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest);



        }

        private static void parseJSON_Week(String s) {
            try {
                JSONObject weekData = new JSONObject(s);
                String address = weekData.getString("address");
                JSONArray days = weekData.getJSONArray("days");
                ArrayList<WeatherDays> alwDays = new ArrayList<>();

                for(int i=0;i<days.length();i++){
                    JSONObject dayObj = days.getJSONObject(i);
                    String dateTimeEpoch = dayObj.getString("datetimeEpoch");
                    String tempmax = dayObj.getString("tempmax");
                    String tempmin = dayObj.getString("tempmin");
                    String precipProb = dayObj.getString("precipprob");
                    String uvIndex = dayObj.getString("uvindex");
                    String desc = dayObj.getString("description");
                    String icon = dayObj.getString("icon");

                    ArrayList<WeatherHours> alwHours = new ArrayList<>();
                    JSONArray wHours = dayObj.getJSONArray("hours");

                    for(int j=0;j<wHours.length();j++){
                        JSONObject wHoursObj = wHours.getJSONObject(j);
                        String dateTimeEpochHours = wHoursObj.getString("datetimeEpoch");
                        String tempHours = wHoursObj.getString("temp");
                        String conditionsHours = wHoursObj.getString("conditions");
                        String iconHours = wHoursObj.getString("icon");

                        alwHours.add(new WeatherHours(dateTimeEpochHours, tempHours, conditionsHours, iconHours));
                    }

                    alwDays.add(new WeatherDays(dateTimeEpoch, tempmax, tempmin, precipProb, uvIndex, desc, icon, alwHours));
                }

                WeatherWeeks wWeeks  = new WeatherWeeks(address, alwDays);
                mainActivity.updateWeekData(wWeeks);


            } catch (Exception e){
                e.printStackTrace();
            }


        }
        public static String getURL(String city, Boolean imperial){
            Uri.Builder buildURL = Uri.parse(weatherURL).buildUpon();
            buildURL.appendPath(city);
            buildURL.appendQueryParameter("unitGroup", (imperial ? "us" : "metric"));
            buildURL.appendQueryParameter("key", APIKey);
            String urlToUse = buildURL.build().toString();
            Log.d(TAG, "getURL: "+ urlToUse);
            return urlToUse;
        }

}
