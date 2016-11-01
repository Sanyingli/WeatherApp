package AsyncTask;

import android.content.Context;
import android.os.AsyncTask;

import data.WeatherHttpClient;

/**
 * Created by Lsy on 16/10/31.
 */

public class WeatherTaskByLatLon extends AsyncTask<WeatherLatLonParams, Void, String> {

    //Weather weather = new Weather();

    private Context mContext;

    public WeatherTaskByLatLon (Context context)
    {
        mContext=context;
    }
    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);

        System.out.println("weather task run");

    }

    @Override
    protected String doInBackground(WeatherLatLonParams... params) {

        double lat= params[0].lat;
        double lon= params[0].lon;

        String data = ((new WeatherHttpClient()).getWeatherData(lat,lon));

        return data;
    }

}