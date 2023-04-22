package com.example.weatherapp;

public class WeatherCurrent {

    public String datetimeEpoch;
    public String temp;
    public String feelsLike;
    public String humidity;
    public String windGust;
    public String windSpeed;
    public String windDir;
    public String visibility;
    public String cloudCover;
    public String uvIndex;
    public String conditions;
    public String icon;
    public String sunriseEpoch;
    public String sunsetEpoch;
    public String date;

    public WeatherCurrent(String datetimeEpoch, String temp, String feelsLike, String humidity, String windGust, String windSpeed, String windDir, String visibility, String cloudCover, String uvIndex, String conditions, String icon, String sunriseEpoch, String sunsetEpoch, String date) {
        this.datetimeEpoch = datetimeEpoch;
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
        this.windGust = windGust;
        this.windSpeed = windSpeed;
        this.windDir = windDir;
        this.visibility = visibility;
        this.cloudCover = cloudCover;
        this.uvIndex = uvIndex;
        this.conditions = conditions;
        this.icon = icon;
        this.sunriseEpoch = sunriseEpoch;
        this.sunsetEpoch = sunsetEpoch;
        this.date = date;
    }
}
