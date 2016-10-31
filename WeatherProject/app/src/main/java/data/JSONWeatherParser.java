package data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Util.Utils;
import model.Place;
import model.Weather;

/**
 * Created by Lsy on 16/9/27.
 */

public class JSONWeatherParser {

    public static Weather getWeather(String data /*, int forecastDate*/) {
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
                if (cityObj != null) {
                    System.out.println("cityObj not null");
                    System.out.println(String.valueOf(cityObj));

                    String test = Utils.getString("name", cityObj);
                    System.out.println(test);
                    place.setCity(test);
                }
                else
                {
                    System.out.println("cityObj is null");
                }

                weather.place = place;
                //JSONObject coordObj = Utils.getObject("coord", cityObj);
               // weather.place.setLat(Utils.getFloat("lat", coordObj));
                //weather.place.setLon(Utils.getFloat("lon", coordObj));


               //weather.place.setCountry(Utils.getString("country", jsonObject));

                /*
                //get the forecast info list
                JSONArray jsonArray2 = jsonObject.getJSONArray("list");

                JSONObject jsonList = jsonArray2.getJSONObject((forecastDate - 1) * 8);

                //get the main object

                JSONObject mainObj = Utils.getObject("main", jsonList);
                weather.currentCondition.setHumidity(Utils.getInt("humidity", mainObj));
                weather.currentCondition.setPressure(Utils.getInt("pressure", mainObj));
                weather.currentCondition.setMinTemp(Utils.getFloat("temp_min", mainObj));
                weather.currentCondition.setMaxTemp(Utils.getFloat("temp_max", mainObj));
                weather.currentCondition.setTemperature(Utils.getDouble("temp", mainObj));


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
                JSONObject dateObj = Utils.getObject("dt_txt", jsonList);
                weather.place.setDate(Utils.getString("dt_txt", dateObj));

*/
                return weather;


            } catch (JSONException e) {
                e.printStackTrace();

                System.out.println();

                return null;
            }
        }

}
