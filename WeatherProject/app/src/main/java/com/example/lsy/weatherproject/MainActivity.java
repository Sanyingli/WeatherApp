package com.example.lsy.weatherproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
//import java.lang.Object;

import Util.Utils;
import data.JSONWeatherParser;
import data.WeatherHttpClient;
import data.LocationFinder;
import model.Clouds;
import model.Weather;

public class MainActivity extends AppCompatActivity implements LocationFinder.LocationDetector {

    private TextView cityName;
    private TextView temp;
    private ImageView iconView;
    private TextView description;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;
    private TextView sunrise;
    private TextView sunset;
    private TextView updated;

    private LocationFinder locationFinder;
    private Location mLocation;
    private double mLongtitude;
    private double mLatitude;

    private final String LOG_TAG = "MainActivity";

    ProgressDialog progressDialog;

    AlertDialog.Builder alertBuilder;
    AlertDialog alertDialog;

    Weather weather = new Weather();

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("title");
        progressDialog.setMessage("tbd");
        progressDialog.show();

        cityName = (TextView) findViewById(R.id.cityText);
        iconView = (ImageView) findViewById(R.id.thumbnailIcon);
        Picasso.with(this).load("http://cdn.zonarutoppuden.com/ns/peliculas-naruto-shippuden.jpg").into(iconView);

        temp = (TextView) findViewById(R.id.tempText);
        description = (TextView) findViewById(R.id.cloudText);
        humidity = (TextView) findViewById(R.id.humidText);
        pressure = (TextView) findViewById(R.id.pressureText);
        wind = (TextView) findViewById(R.id.windText);
        sunrise = (TextView) findViewById(R.id.riseText);
        sunset = (TextView) findViewById(R.id.setText);
        updated = (TextView) findViewById(R.id.updateText);

        if(networkConnected()) {

            progressDialog.cancel();

            getLocation();

            renderWeatherDate("Spokane,US");

        }
        else
        {
            Log.d(LOG_TAG, "no net work connection");
        }




        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public void renderWeatherDate(String city) {
        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city + Utils.KEY_ID});
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public void locationFound(Location location) {
        Log.d(LOG_TAG, "LOCATION FOUND");
        mLocation = location;

        if(alertDialog !=null)
        {
            alertDialog.cancel();
        }

        if(mLocation != null) {
            mLatitude = mLocation.getLatitude();
            mLongtitude = mLocation.getLongitude();
            System.out.println("latitude:" + mLatitude);
            System.out.println("longitude:" + mLongtitude);
        }

    }

    @Override
    public void locationNotFound(LocationFinder.FailureReason failureReason) {

        Log.d(LOG_TAG, "HAVE NOT FOUND LOCATION");
    }


    private class WeatherTask extends AsyncTask<String, Void, Weather> {
        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            DateFormat df = DateFormat.getTimeInstance();

            String sunriseDate = df.format(new Date(weather.place.getSunrise()));
            String sunsetDate = df.format(new Date(weather.place.getSunset()));
            String updateDate = df.format(new Date(weather.place.getLastupdate()));

            //switch Fahrenheit to Celsius,and set format
            DecimalFormat decimalFormat = new DecimalFormat("#.#");//run to only one decimal
            Double tempF = weather.currentCondition.getTemperature();
            Double tempC = tempF / 10 - 17.205;
            String tempFormat = decimalFormat.format(tempC);

            cityName.setText(weather.place.getCity() + "," + weather.place.getCountry());
            temp.setText("" + tempFormat + "°C");
            humidity.setText("Humidity: " + weather.currentCondition.getHumidity() + "%");
            pressure.setText("Pressure: " + weather.currentCondition.getPressure() + "hPa");
            wind.setText("Wind: " + weather.wind.getSpeed() + "mps");
            sunrise.setText("Sunrise: " + sunriseDate);
            sunset.setText("Sunset: " + sunsetDate);
            description.setText("Condition: " + weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescription() + ")");
            updated.setText("Update time: " + updateDate);
        }

        @Override
        protected Weather doInBackground(String... params) {

            String data = ((new WeatherHttpClient()).getWeatherData(params[0]));

            weather = JSONWeatherParser.getWeather(data);

            //Log.v("Data: ", String.valueOf(weather.currentCondition.getTemperature()));

            return weather;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private boolean networkConnected() {
        //a connectivity manager instance using the activity's context
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //networkInfo was created then isConnected is true
        return networkInfo != null && networkInfo.isConnected();
    }

    private void getLocation()
    {
        locationFinder = new LocationFinder(this,this);
        locationFinder.detectLocation();
    }
}
