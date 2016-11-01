package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lsy.weatherproject.R;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import model.WeatherAndImge;

/**
 * Created by Lsy on 16/10/31.
 */

public class WeatherAdapter extends BaseAdapter {

    private ImageView iconView;
    private TextView dWeather;

    //private ImageView testView;

    Context mContext;
    LayoutInflater mInflater;

    ArrayList<WeatherAndImge> WeatherList = new ArrayList<>();


    public WeatherAdapter(Context context,ArrayList<String> listWeather, ArrayList<String> listImage){
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int forecastDay = listWeather.size();
        for(int i =0;i<forecastDay;i++){
            WeatherList.add(i,new WeatherAndImge(listWeather.get(i),listImage.get(i)));
        }
    }

    @Override
    public int getCount() {
        return WeatherList.size();
    }

    @Override
    public Object getItem(int position) {
        return WeatherList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        WeatherAndImge weatherAndImge = WeatherList.get(position);

        if(convertView==null){
            convertView=mInflater.inflate(R.layout.list_weather, parent, false);
        }
        iconView = (ImageView) convertView.findViewById(R.id.imageIcon);
        Picasso.with(mContext).load(weatherAndImge.getUlr()).into(iconView);

        //testView = (ImageView) convertView.findViewById(R.id.testImage);
        //Picasso.with(mContext).load()

        dWeather = (TextView) convertView.findViewById(R.id.eachText);

        //iconView.setImageBitmap(weatherAndImge.getDayImage());
        dWeather.setText(weatherAndImge.getDayWeather());

        return convertView;
    }
}

