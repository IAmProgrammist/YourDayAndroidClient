package rchat.info.yourday_new.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.activities.ChooseCityActivity;
import rchat.info.yourday_new.containers.weather.Weather;
import rchat.info.yourday_new.others.SaveSharedPreferences;

public class WeatherAdapter extends BaseAdapter {

    boolean hasCity;
    Context ctx;
    Weather weather;

    public WeatherAdapter(Context ctx, boolean hasCity) {
        this.hasCity = hasCity;
        this.ctx = ctx;
        weather = null;
    }

    public WeatherAdapter(Context ctx, Weather weather) {
        this.ctx = ctx;
        this.weather = weather;
        hasCity = true;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (position == 0) {
            DisplayMetrics display = ctx.getResources().getDisplayMetrics();
            int height = display.heightPixels;
            View v = View.inflate(ctx, R.layout.empty, null);
            v.setEnabled(false);
            v.setClickable(false);
            v.setPadding(0, 0, 0, ((int) height / 6));
            return v;
        } else if (position == 1) {
            View va = View.inflate(ctx, R.layout.weather_header, null);
            va.setEnabled(false);
            va.setClickable(false);
            return va;
        } else if (position == 3) {
            View vs = View.inflate(ctx, R.layout.empty, null);
            vs.setEnabled(false);
            vs.setClickable(false);
            vs.setPadding(0, 0, 0, 100);
            return vs;
        } else {
            if (!hasCity) {
                view = View.inflate(ctx, R.layout.weather_unidentified, null);
                view.findViewById(R.id.choose_city).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ctx, ChooseCityActivity.class);
                        ctx.startActivity(intent);
                    }
                });
                return view;
            } else {
                view = View.inflate(ctx, R.layout.weatherrow, null);
                TextView temp = view.findViewById(R.id.temp);
                TextView feels_like = view.findViewById(R.id.feels_like);
                TextView windDesc = view.findViewById(R.id.wind_desc);
                TextView cloudsDesc = view.findViewById(R.id.clouds_desc);
                TextView waterDesc = view.findViewById(R.id.water_desc);
                TextView pressureDesc = view.findViewById(R.id.pressure_desc);
                try {
                    temp.setText(weather.currentWeather.getTemp());
                    feels_like.setText(weather.currentWeather.getFeelsLike());
                    windDesc.setText(weather.currentWeather.getWindDesc());
                    cloudsDesc.setText(weather.currentWeather.getCloudsDesc());
                    waterDesc.setText(weather.currentWeather.getWaterDesc());
                    pressureDesc.setText(weather.currentWeather.getPressure());
                    int resId = 0;
                    try {
                        resId = ctx.getResources().getIdentifier("ic_w" + weather.currentWeather.weather_ids.get(0), "drawable", ctx.getPackageName());
                        if (resId == 0) {
                            resId = ctx.getResources().getIdentifier("ic_w" + weather.currentWeather.weather_ids.get(0).substring(0, weather.currentWeather.weather_ids.get(0).length() - 1), "drawable", ctx.getPackageName());
                        }
                    } catch (Exception e) {

                    }

                    ImageView imageView = view.findViewById(R.id.weather);
                    imageView.setImageResource(resId);
                    ((TextView) view.findViewById(R.id.cityName)).setText(SaveSharedPreferences.getCityName(ctx));
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
                    final RecyclerView myList = (RecyclerView) view.findViewById(R.id.recycler);
                    myList.setLayoutManager(layoutManager);
                    Switch swith = view.findViewById(R.id.switcher);
                    swith.setChecked(false);
                    myList.setAdapter(new HourlyWeatherAdapter(weather.hourly, ctx));
                    swith.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                myList.setAdapter(new DailyWeatherAdapter(weather.daily, ctx));
                            } else {
                                myList.setAdapter(new HourlyWeatherAdapter(weather.hourly, ctx));
                            }
                        }
                    });
                } catch (NullPointerException e) {

                }
                return view;
            }
        }
    }
}
