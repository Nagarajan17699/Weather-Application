package com.example.weatherapp;

import java.io.Serializable;

public class WeatherHours implements Serializable {

    public String dateTimeEpoch;
    public String temp;
    public String conditions;
    public String icon;

    public WeatherHours(String dateTimeEpoch, String temp, String conditions, String icon) {
        this.dateTimeEpoch = dateTimeEpoch;
        this.temp = temp;
        this.conditions = conditions;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "WeatherHours{" +
                "dateTimeEpoch='" + dateTimeEpoch + '\'' +
                ", temp='" + temp + '\'' +
                ", conditions='" + conditions + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
