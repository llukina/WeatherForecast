package com.example.android.weatherforecast.data;

import com.example.android.weatherforecast.model.Forecast;

import java.util.ArrayList;

public interface ForecastListAsyncResponse {
    void processFinished(ArrayList<Forecast> forecastArrayList);
}
