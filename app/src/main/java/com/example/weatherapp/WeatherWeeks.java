package com.example.weatherapp;

import java.util.ArrayList;

public class WeatherWeeks {

    public String address;
    public ArrayList<WeatherDays> wDays;

    public WeatherWeeks(String address, ArrayList<WeatherDays> wDays) {
        this.address = address;
        this.wDays = wDays;
    }
}
