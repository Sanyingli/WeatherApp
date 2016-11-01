package AsyncTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import data.WeatherHttpClient;

/**
 * Created by Lsy on 16/11/1.
 */


public class WeatherImageTask extends AsyncTask<String,Void,String> {

    private Context mContext;

    public WeatherImageTask(Context context){
        mContext = context;
    }

    @Override
    protected String doInBackground(String... code) {
        return WeatherHttpClient.getWeatherImage(code[0]);
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
    }
}

