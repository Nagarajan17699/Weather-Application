package com.example.weatherapp;

import java.io.Serializable;
import java.util.ArrayList;

public class WeatherDays implements Serializable {

    public String dateTimeEpoch;
    public String tempMax;
    public String tempMin;
    public String precipProb;
    public String uvIndex;
    public String description;
    public String icon;
    public ArrayList<WeatherHours> wHours;

    public WeatherDays(String dateTimeEpoch, String tempMax, String tempMin, String precipProb, String unIndex, String description, String icon, ArrayList<WeatherHours> wHours) {
        this.dateTimeEpoch = dateTimeEpoch;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.precipProb = precipProb;
        this.uvIndex = unIndex;
        this.description = description;
        this.icon = icon;
        this.wHours = wHours;
    }

    @Override
    public String toString() {
        return "WeatherDays{" +
                "dateTimeEpoch='" + dateTimeEpoch + '\'' +
                ", tempMax='" + tempMax + '\'' +
                ", tempMin='" + tempMin + '\'' +
                ", precipProb='" + precipProb + '\'' +
                ", unIndex='" + uvIndex + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", wHours=" + wHours.size() +
                '}';
    }
}
