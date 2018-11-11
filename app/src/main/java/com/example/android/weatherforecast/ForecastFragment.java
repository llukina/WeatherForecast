package com.example.android.weatherforecast;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.weatherforecast.model.Forecast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {


    public ForecastFragment() {
        // Required empty public constructor
    }

    public static final ForecastFragment newInstance(Forecast forecast) {
        ForecastFragment forecastFragment = new ForecastFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("forecast", forecast);

        forecastFragment.setArguments(bundle);

        //Toast.makeText(getActivity(), "Test", Toast.LENGTH_SHORT).show();
        return forecastFragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View forecastView = inflater.inflate(R.layout.fragment_forecast, container, false);

//        TextView forecastTemp = (TextView)forecastView.findViewById(R.id.forecast_temp);
        TextView forecastDate = forecastView.findViewById(R.id.forecast_date_text);
        TextView forecastHigh = forecastView.findViewById(R.id.forecast_high_text);
        TextView forecastLow = forecastView.findViewById(R.id.forecast_low_text);
        TextView forecastDescription = forecastView.findViewById(R.id.forecast_description_text_view);

        Forecast forecast = (Forecast) getArguments().getSerializable("forecast");

//        forecastTemp.setText(forecast.getCurrentTemperature());
        forecastDate.setText(forecast.getForecastDate());
        forecastHigh.setText(forecast.getForecastHighTemp());
        forecastLow.setText(forecast.getForecastLowTemp());
        forecastDescription.setText(forecast.getForecastWeatherDescription());


        return forecastView;
    }

    private String getImageUrl(String html) {

        String imgRegex = "(?i)<img[^>]+?src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
        String imgSrc = null;

        Pattern p = Pattern.compile(imgRegex);
        Matcher m = p.matcher(html);

        while (m.find()) {
            imgSrc = m.group(1);
        }
        return imgSrc;
    }

}
