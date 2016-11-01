package model;

import android.graphics.Bitmap;

import java.net.URL;

/**
 * Created by Lsy on 16/10/31.
 */

public class WeatherAndImge {
    String weather;
    //Bitmap image;
    String imageURL;

    public String getDayWeather()
    {
        return weather;
    }
    public String getUlr(){return imageURL;}

  //  public Bitmap getDayImage()
  //  {
  //      return image;
  //  }
    public WeatherAndImge(String weather, String imageURL/*Bitmap imagemage */){
        this.weather = weather;
        this.imageURL = imageURL;

    }
}
