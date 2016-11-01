package data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import Util.Utils;

import static Util.Utils.ICON_URL;

/**
 * Created by Lsy on 16/9/27.
 */

public class WeatherHttpClient {
    public String getWeatherData(String zip)
    {
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = (HttpURLConnection) (new URL(Utils.FORECAST_URL +"zip="+ zip+"&units=metric"+ Utils.KEY_ID)).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            //read the response
            StringBuffer stringBuffer = new StringBuffer();
            inputStream = connection.getInputStream();
            //bufferReader is the only actually object can read this inputstream we get from web
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuffer.append(line + "\r\n");
            }

            inputStream.close();
            connection.disconnect();

            return stringBuffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getWeatherData(double lat, double lon)
    {
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = (HttpURLConnection) (new URL(Utils.FORECAST_URL + "lat="+lat +"&lon="+lon+"&units=metric"+Utils.KEY_ID)).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            //read the response
            StringBuffer stringBuffer = new StringBuffer();
            inputStream = connection.getInputStream();
            //bufferReader is the only actually object can read this inputstream we get from web
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuffer.append(line + "\r\n");
            }

            inputStream.close();
            connection.disconnect();

            return stringBuffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String getWeatherImage(String code) {

        String url = ICON_URL + code +".png";
        return url;
        /*
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = (HttpURLConnection) (new URL(ICON_URL + code + ".png")).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.connect();

            inputStream = connection.getInputStream();

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap != null)
            {
                System.out.println("GOT BITMAP");
            }
            else
            {
                System.out.println("NOT GOT BITMAP");
            }

            return bitmap;

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            connection.disconnect();
        }
        */
    }
}
