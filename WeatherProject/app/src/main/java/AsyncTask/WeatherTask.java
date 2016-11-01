package AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.renderscript.Sampler;
import android.util.Log;

import data.WeatherHttpClient;
import model.Weather;


/**
 * Created by Lsy on 16/10/31.
 */

public class WeatherTask extends AsyncTask<String, Void, String> {


    private Context mContext;
    //private WeatherTask

    public WeatherTask(Context context)
    {
        mContext=context;
    }


        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);

            System.out.println("weather task run");

            //System.out.println(weather.place.getCity());

        }

        @Override
        protected String doInBackground(String... params) {

            String data = ((new WeatherHttpClient()).getWeatherData(params[0]));

            //weather = JSONWeatherParser.getWeather(data,1);

            //System.out.println(String.valueOf(weather)+"GGGGGGGGGGGGGGG");

            //Log.v("Data: ", String.valueOf(weather.place.getCity())+"GGGGGGGGGGG");

            return data;
        }

    }


