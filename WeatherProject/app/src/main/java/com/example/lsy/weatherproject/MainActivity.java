package com.example.lsy.weatherproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
//import java.lang.Object;

import AsyncTask.WeatherLatLonParams;
import AsyncTask.WeatherTaskByLatLon;
import adapter.WeatherAdapter;
import data.JSONWeatherParser;
import data.LocationFinder;
import model.Weather;
import AsyncTask.WeatherTask;



public class MainActivity extends AppCompatActivity implements LocationFinder.LocationDetector {

    private TextView cityName;
    private ImageView iconView;
    private ArrayList<String> items;
    private ListView mListView;

    JSONWeatherParser jsonWeatherParser = new JSONWeatherParser();

    int forecastDate;
    boolean tempUnit;
    boolean type;

    WeatherTask weatherTask = new WeatherTask(getBaseContext());
    WeatherTaskByLatLon weatherTaskByLatLon = new WeatherTaskByLatLon(getBaseContext());


    private LocationFinder locationFinder;
    private Location mLocation;
    private double mLongitude;
    private double mLatitude;

    ArrayList<String> weatherList;
    ArrayList<String> imageList;

    String tempData = null;

    private final String LOG_TAG = "MainActivity";


    ProgressDialog progressDialog;


    Weather mWeather = new Weather();

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
        progressDialog.setTitle("hint");
        progressDialog.setMessage("APP is loading");
        progressDialog.show();

        cityName = (TextView) findViewById(R.id.cityText);
        //iconView = (ImageView) findViewById(R.id.thumbnailIcon);
        //Picasso.with(this).load("http://cdn.zonarutoppuden.com/ns/peliculas-naruto-shippuden.jpg").into(iconView);

        items = new ArrayList<>();
        items.add(getString(R.string.temp));
        items.add(getString(R.string.des));
        items.add(getString(R.string.windSpeed));
        items.add(getString(R.string.humidity));
        items.add(getString(R.string.pressure));


        //Get info from the SettingsActivity;
        SharedPreferences settings = getSharedPreferences("PrefFile", 0);
        String zipCode = settings.getString("zipCode", "20006");
        forecastDate = settings.getInt("forecastDate", 3);
        tempUnit = settings.getBoolean("tempUnit", true);
        type = settings.getBoolean("type",false);


        locationFinder = new LocationFinder(this,this);
        locationFinder.detectLocation();


        if(networkConnected()) {

            if(type==true) {

                progressDialog.cancel();


                weatherList = jsonWeatherParser.getForecastWeatherFromZipCode(items, zipCode, forecastDate, tempUnit);
                imageList = jsonWeatherParser.getImageViewFromZipCode(zipCode, forecastDate);

                mListView = (ListView) findViewById(R.id.weather_list_view);

                WeatherAdapter weatherAdapter = new WeatherAdapter(getBaseContext(), weatherList, imageList);

                mListView.setAdapter(weatherAdapter);

                try {
                    tempData = weatherTask.execute(zipCode).get();

                    mWeather = jsonWeatherParser.getWeather(tempData,1);

                    cityName.setText(mWeather.place.getCity()+", "+mWeather.place.getCountry());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


                //renderWeatherDataByCity("20006");

            }

        }
        else
        {
            Log.d(LOG_TAG, "no net work connection");
        }




        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public void renderWeatherDataByCity(String zipCode) throws ExecutionException, InterruptedException {
        //String temp;
        //WeatherTask weatherTask = new WeatherTask();
        //temp= weatherTask.execute(new String(city)).get();

        //mWeather = JSONWeatherParser.getWeather(temp,1);



        //System.out.println(mWeather.place.getCity());

        //cityName.setText(mWeather.place.getCity()+mWeather.place.getCountry());
        System.out.println("item"+ String.valueOf(items)+ "zipCode:"+ String.valueOf(zipCode) + "forecastDate:" + String.valueOf(forecastDate)+ "tempUnit:" + String.valueOf(tempUnit));

        if (items != null && zipCode != null) {
            weatherList = jsonWeatherParser.getForecastWeatherFromZipCode(items, zipCode, forecastDate, tempUnit);
        }
            mListView = (ListView) findViewById(R.id.weather_list_view);
            WeatherAdapter weatherAdapter = new WeatherAdapter(getBaseContext(), weatherList, imageList);

            mListView.setAdapter(weatherAdapter);


    }

/*
    public void renderWeatherDataByLatLon(double lat, double lon)
    {
        WeatherTask test = new WeatherTask();
        test.execute(new latLonWeatherParams(lat,lon));

    }
    */

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

        if(progressDialog !=null)
        {
            progressDialog.cancel();
        }

        if(mLocation != null) {
            mLatitude = mLocation.getLatitude();
            mLongitude = mLocation.getLongitude();
            System.out.println("latitude:" + mLatitude);
            System.out.println("longitude:" + mLongitude);

        }
            //renderWeatherDataByCity("20006");
            weatherList = jsonWeatherParser.getForecastWeatherFromLatandLon(items, mLatitude,mLongitude, forecastDate, tempUnit);
            imageList = jsonWeatherParser.getImageViewFromLatandLon(mLatitude,mLongitude, forecastDate);

            mListView = (ListView) findViewById(R.id.weather_list_view);

            WeatherAdapter weatherAdapter = new WeatherAdapter(getBaseContext(), weatherList, imageList);

            mListView.setAdapter(weatherAdapter);

        try {
            WeatherLatLonParams temp = new WeatherLatLonParams(mLatitude,mLongitude);
            tempData = weatherTaskByLatLon.execute(temp).get();

            mWeather = jsonWeatherParser.getWeather(tempData,1);

            cityName.setText(mWeather.place.getCity()+", "+mWeather.place.getCountry());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        }




    @Override
    public void locationNotFound(LocationFinder.FailureReason failureReason) {

        Log.d(LOG_TAG, "HAVE NOT FOUND LOCATION,WILL USE DEFAULT LOCATION");
        if(progressDialog !=null)
        {
            progressDialog.cancel();
        }

        if(type ==false)
        {
            weatherList = jsonWeatherParser.getForecastWeatherFromZipCode(items, "20006", forecastDate, tempUnit);
            imageList = jsonWeatherParser.getImageViewFromZipCode("20006", forecastDate);

            mListView = (ListView) findViewById(R.id.weather_list_view);

            WeatherAdapter weatherAdapter = new WeatherAdapter(getBaseContext(), weatherList, imageList);

            mListView.setAdapter(weatherAdapter);

            try {
                tempData = weatherTask.execute("20006").get();

                mWeather = jsonWeatherParser.getWeather(tempData,1);

                cityName.setText(mWeather.place.getCity()+", "+mWeather.place.getCountry());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }


        //System.out.println(weather.place.getCity());
    }
/*

    private class WeatherTask extends AsyncTask<String, Void, Weather> {
        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);


            DateFormat df = DateFormat.getTimeInstance();

            String sunriseDate = df.format(new Date(weather.place.getSunrise()));
            String sunsetDate = df.format(new Date(weather.place.getSunset()));
            String date = weather.place.getDate();

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
            updated.setText("Update time: " + date);

        }

        @Override
        protected Weather doInBackground(String... params) {

            String data = ((new WeatherHttpClient()).getWeatherData(params[0]));

            weather = JSONWeatherParser.getWeather(data,1);

            //Log.v("Data: ", String.valueOf(weather.place.getCity()));

            return weather;
        }

    }


    private class WeatherTaskByLatLon extends AsyncTask<latLonWeatherParams, Void, Weather> {
        @Override
        protected Weather doInBackground(latLonWeatherParams... params) {
            double lat = params[0].lat;
            double lon = params[0].lon;

            String data = ((new WeatherHttpClient()).getWeatherData(lat,lon));

            weather = JSONWeatherParser.getWeather(data,1);

            Log.v("Data: ", String.valueOf(weather.place.getCity()));

            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            DateFormat df = DateFormat.getTimeInstance();

            String sunriseDate = df.format(new Date(weather.place.getSunrise()));
            String sunsetDate = df.format(new Date(weather.place.getSunset()));
            String date = weather.place.getDate();

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
            updated.setText("Update time: " + date);
        }

    }

    */


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.setMenu)
        {
            Intent intent =new Intent(this, SettingActivity.class);
            this.startActivity(intent);
            return true;
        }
        return  super.onOptionsItemSelected(item);
    }

    private boolean networkConnected() {
        //a connectivity manager instance using the activity's context
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //networkInfo was created then isConnected is true
        return networkInfo != null && networkInfo.isConnected();
    }

}

