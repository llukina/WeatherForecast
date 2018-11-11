package com.example.android.weatherforecast;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.weatherforecast.data.ForecastData;
import com.example.android.weatherforecast.data.ForecastListAsyncResponse;
import com.example.android.weatherforecast.data.ForecastViewPagerAdapter;
import com.example.android.weatherforecast.model.Forecast;
import com.example.android.weatherforecast.util.Pref;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private ForecastViewPagerAdapter forecastViewPagerAdapter;
    private ViewPager viewPager;
    private TextView locationText;
    private TextView currentTempText, currentDate;
    private EditText userLocationText;
    private String userEnteredString;
    private ImageView icon;
    private LinearLayout errorLayout;
    private TextView errorText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationText = findViewById(R.id.location_text_view);
        currentTempText = findViewById(R.id.current_temp);
        currentDate = findViewById(R.id.current_date);

        errorLayout = findViewById(R.id.error_layout);
        errorText = findViewById(R.id.error_text);

        icon = findViewById(R.id.weather_icon);

        final Pref pref = new Pref(this);
        String prefsLocation = pref.getLocation();
        userEnteredString = prefsLocation;
        getWeather(userEnteredString);


        userLocationText = findViewById(R.id.location_name);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        userLocationText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keycode == KeyEvent.KEYCODE_ENTER)) {
                    errorLayout.setVisibility(View.GONE);

                    userEnteredString = userLocationText.getText().toString().trim();
                    pref.setLocation(userEnteredString);
                    getWeather(userEnteredString);

                    return true;

                }
                return false;
            }
        });

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

    private void getWeather(String currentLocation) {

        forecastViewPagerAdapter = new ForecastViewPagerAdapter(getSupportFragmentManager(),
                getFragments(currentLocation));

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(forecastViewPagerAdapter);
    }


    private List<Fragment> getFragments(String locationString) {
        final List<Fragment> fragmentList = new ArrayList<>();
        new ForecastData().getForecast(new ForecastListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Forecast> forecastArrayList) {
                try {
                    fragmentList.clear();
                    String html = forecastArrayList.get(0).getDescriptionHtml();

                    Picasso.get()
                            .load(getImageUrl(html)).into(icon);

                    locationText.setText(String.format("%s,\n%s",
                            forecastArrayList.get(0).getCity(), forecastArrayList.get(0).getRegion()));
                    currentTempText.setText(
                            String.format("%s°C", forecastArrayList.get(0).getCurrentTemperature()));
                    String[] date = forecastArrayList.get(0).getDate().split(" ");
                    String splitDate = date[0] + " " + date[1] + " " + date[2] + " " + date[3];
                    currentDate.setText(splitDate);


                    for (int i = 0; i < forecastArrayList.size(); i++) {
                        ForecastFragment forecastFragment = ForecastFragment.newInstance(forecastArrayList.get(i));
                        fragmentList.add(forecastFragment);
                    }

                    forecastViewPagerAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    errorLayout.setVisibility(View.VISIBLE);
                    if (!internetConnection()) {
                        errorText.setText(R.string.error_no_internet);
                    } else {
                        errorText.setText(String.format(getString(R.string.error_default), userEnteredString, userEnteredString));
                    }
                        e.printStackTrace();
                }
            }
        }, locationString);
        return fragmentList;
    }

    private boolean internetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
