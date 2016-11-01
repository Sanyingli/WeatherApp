package data;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.TextView;

import com.example.lsy.weatherproject.MainActivity;
import com.example.lsy.weatherproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import AsyncTask.WeatherImageTask;
import AsyncTask.WeatherLatLonParams;
import AsyncTask.WeatherTask;
import AsyncTask.WeatherTaskByLatLon;
import Util.Utils;
import model.Place;
import model.Weather;

/**
 * Created by Lsy on 16/9/27.
 */

public class JSONWeatherParser {

    public Weather getWeather(String data , int forecastDate) {
        Weather weather = new Weather();

//        weather.place.setCity("test city name");

//        System.out.println(weather.place.getCity());

        if (data != null)
        {
            //System.out.println("get data:" + data);

        }
        else
        {
            System.out.println("no data get");
        }

            //create JsonObject from data
            try {
                JSONObject jsonObject = new JSONObject(data);

                Place place = new Place();

                //get the city obj
                JSONObject cityObj = Utils.getObject("city", jsonObject);
                place.setCity(Utils.getString("name",cityObj));

                JSONObject coordObj = Utils.getObject("coord", cityObj);
                place.setLat(Utils.getFloat("lat", coordObj));
                place.setLon(Utils.getFloat("lon", coordObj));


                place.setCountry(Utils.getString("country", cityObj));


                //System.out.println(weather.place.getCity());

                //get the forecast info list
                JSONArray jsonArray2 = jsonObject.getJSONArray("list");

                JSONObject jsonList = jsonArray2.getJSONObject((forecastDate - 1) * 8);

                System.out.println(String.valueOf(jsonList));

                //get the main object

                JSONObject mainObj = Utils.getObject("main",jsonList);
                weather.currentCondition.setHumidity(Utils.getInt("humidity", mainObj));
                weather.currentCondition.setPressure(Utils.getInt("pressure", mainObj));
                weather.currentCondition.setMinTemp(Utils.getFloat("temp_min", mainObj));
                weather.currentCondition.setMaxTemp(Utils.getFloat("temp_max", mainObj));
                weather.currentCondition.setTemperature(Utils.getDouble("temp", mainObj));

///*
                //get the weather info
                JSONArray jsonArray = jsonList.getJSONArray("weather");
                JSONObject jsonWeather = jsonArray.getJSONObject(0);
                weather.currentCondition.setWeatherId(Utils.getInt("id", jsonWeather));
                weather.currentCondition.setDescription(Utils.getString("description", jsonWeather));
                weather.currentCondition.setCondition(Utils.getString("main", jsonWeather));
                weather.currentCondition.setIcon(Utils.getString("icon", jsonWeather));

                //get the wind
                JSONObject windObj = Utils.getObject("wind", jsonList);
                weather.wind.setSpeed(Utils.getFloat("speed", windObj));
                weather.wind.setDeg(Utils.getFloat("deg", windObj));

                //get the clouds
                JSONObject cloudsObj = Utils.getObject("clouds", jsonList);
                weather.clouds.setPrecipitation(Utils.getInt("all", cloudsObj));

                //get the date
                place.setDate(Utils.getString("dt_txt", jsonList));

                weather.place = place;

                return weather;


            } catch (JSONException e) {
                e.printStackTrace();
                return null;

            }
        }

    public ArrayList<String> getForecastWeatherFromZipCode(ArrayList<String> arrayList, String zipCode, int forecastDate, boolean tempUnit) {

        ArrayList<String> daysWeather = new ArrayList<>();

        Weather weather = new Weather();
        MainActivity weatherActivity = new MainActivity();
        WeatherTask weatherTask = new WeatherTask(weatherActivity.getBaseContext());

        String dataWea= null;


        try {
            dataWea = weatherTask.execute(zipCode).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for(int i=1;i<forecastDate+1;i++){
            weather = this.getWeather(dataWea,i);
            String each;
            each = this.getItemsText(arrayList,weather,tempUnit).get(0)
                    + this.getItemsText(arrayList,weather,tempUnit).get(1)
                    + this.getItemsText(arrayList,weather,tempUnit).get(2)
                    +this.getItemsText(arrayList,weather,tempUnit).get(3)
                    +this.getItemsText(arrayList,weather,tempUnit).get(4)
                    + this.getItemsText(arrayList,weather,tempUnit).get(5);

            daysWeather.add(i-1,each);
        }
        return daysWeather;
    }

    public ArrayList<String> getItemsText(ArrayList<String> arrayList, Weather weather, boolean tempUnit){

        ArrayList<String> itemsEach = new ArrayList<>();
        itemsEach.add(weather.place.getDate() + "\n");

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        double tempResult;
        if (tempUnit) {
            tempResult = weather.currentCondition.getTemperature();
            String tempFormat = decimalFormat.format(tempResult);
            itemsEach.add(arrayList.get(0) + tempFormat+ "°C" + "\n");
        } else {
            tempResult = (weather.currentCondition.getTemperature() * 9 / 5) + 32;
            String tempFormat = decimalFormat.format(tempResult);
            itemsEach.add(arrayList.get(0) + tempFormat+ "°F" + "\n");
        }

        itemsEach.add(arrayList.get(1) + weather.currentCondition.getDescription() + "\n");
        itemsEach.add(arrayList.get(3) + weather.currentCondition.getHumidity()+ "%" + "\n");
        itemsEach.add(arrayList.get(4) + weather.currentCondition.getPressure()+ "hpa" + "\n");
        itemsEach.add(arrayList.get(2) + weather.wind.getSpeed() + "m/s" + "\n");

        return itemsEach;
    }

    public ArrayList<String> getImageViewFromZipCode(String zipCode, int forecastDate) {

        ArrayList<String> daysIcon = new ArrayList<>();

        String imageURL = null;

        Weather weather = new Weather();
        MainActivity weatherActivity = new MainActivity();
        WeatherTask weatherTask = new WeatherTask(weatherActivity.getBaseContext());

        String dataWea = null;

        try {
            dataWea = weatherTask.execute(zipCode).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for(int i=1;i<forecastDate+1;i++){
            weather = this.getWeather(dataWea, i);

            try {
                WeatherImageTask imageAsyncTask = new WeatherImageTask(weatherActivity.getBaseContext());
                imageURL=imageAsyncTask.execute(weather.currentCondition.getIcon()).get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            daysIcon.add(i-1,imageURL);
        }


        return daysIcon;
    }

    public ArrayList<String> getForecastWeatherFromLatandLon(ArrayList<String> arrayList, double lat, double lon , int forecastDate, boolean tempUnit) {

        ArrayList<String> daysWeather = new ArrayList<>();

        Weather weather = new Weather();
        MainActivity weatherActivity = new MainActivity();
        WeatherTaskByLatLon weatherTaskByLatLon = new WeatherTaskByLatLon(weatherActivity.getBaseContext());

        String dataWea= null;

         WeatherLatLonParams params = new WeatherLatLonParams(lat,lon);


        try {
            dataWea = weatherTaskByLatLon.execute(params).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for(int i=1;i<forecastDate+1;i++){
            weather = this.getWeather(dataWea,i);
            String each;
            each = this.getItemsText(arrayList,weather,tempUnit).get(0)
                    + this.getItemsText(arrayList,weather,tempUnit).get(1)
                    + this.getItemsText(arrayList,weather,tempUnit).get(2)
                    +this.getItemsText(arrayList,weather,tempUnit).get(3)
                    +this.getItemsText(arrayList,weather,tempUnit).get(4)
                    + this.getItemsText(arrayList,weather,tempUnit).get(5);

            daysWeather.add(i-1,each);
        }
        return daysWeather;
    }

    public ArrayList<String> getImageViewFromLatandLon(double lat,double lon, int forecastDate) {

        ArrayList<String> daysIcon = new ArrayList<>();

        String imageURL = null;

        Weather weather = new Weather();
        MainActivity weatherActivity = new MainActivity();
        WeatherTaskByLatLon weatherTaskByLatLon = new WeatherTaskByLatLon(weatherActivity.getBaseContext());

        String dataWea= null;

        WeatherLatLonParams params = new WeatherLatLonParams(lat,lon);


        try {
            dataWea = weatherTaskByLatLon.execute(params).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for(int i=1;i<forecastDate+1;i++){
            weather = this.getWeather(dataWea, i);

            try {
                WeatherImageTask imageAsyncTask = new WeatherImageTask(weatherActivity.getBaseContext());
                imageURL=imageAsyncTask.execute(weather.currentCondition.getIcon()).get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            daysIcon.add(i-1,imageURL);
        }

        return daysIcon;
    }


}
