package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import Util.Utils;

/**
 * Created by Lsy on 16/9/27.
 */

public class WeatherHttpClient {
    public String getWeatherData(String place)
    {
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = (HttpURLConnection) (new URL(Utils.BASE_URL + place)).openConnection();
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
}
