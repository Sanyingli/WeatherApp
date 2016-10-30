package com.example.lsy.weatherproject;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
//import java.lang.Object;

import Util.Utils;
import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.Clouds;
import model.Weather;

public class MainActivity extends AppCompatActivity {

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

    Weather weather = new Weather();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        renderWeatherDate("Spokane,US");

    }

    public void renderWeatherDate(String city)
    {
        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city + "&appid=" + Utils.KEY_ID});
    }

    private class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return null;
        }
        
    }

    private class WeatherTask extends AsyncTask<String, Void, Weather>
    {
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
            Double tempC = tempF/10 - 17.205;
            String tempFormat = decimalFormat.format(tempC);

            cityName.setText(weather.place.getCity() + "," + weather.place.getCountry());
            temp.setText("" + tempFormat + "°C");
            humidity.setText("Humidity: " +weather.currentCondition.getHumidity()+"%");
            pressure.setText("Pressure: " +weather.currentCondition.getPressure()+"hPa");
            wind.setText("Wind: "+ weather.wind.getSpeed() + "mps");
            sunrise.setText("Sunrise: "+ sunriseDate);
            sunset.setText("Sunset: " + sunsetDate);
            description.setText("Condition: " + weather.currentCondition.getCondition()+ "(" + weather.currentCondition.getDescription()+ ")");
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
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }
}
