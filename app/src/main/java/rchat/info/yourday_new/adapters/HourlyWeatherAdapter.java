package rchat.info.yourday_new.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.containers.weather.HourlyWeather;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder> {

    private List<HourlyWeather> list;
    private Context context;

    public HourlyWeatherAdapter(List<HourlyWeather> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HourlyWeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyWeatherAdapter.ViewHolder holder, int position) {
        int resId;
        resId = context.getResources().getIdentifier("ic_" + list.get(position).imgName, "drawable", context.getPackageName());
        if (resId == 0) {
            resId = context.getResources().getIdentifier("ic_" + list.get(position).imgName.substring(0, list.get(position).imgName.length() - 1), "drawable", context.getPackageName());
        }
        holder.icon.setImageResource(resId);
        holder.windDesc.setText(list.get(position).getWindDesc());
        holder.waterDesc.setText(list.get(position).getWaterDesc());
        holder.temp.setText(list.get(position).getTemp());
        holder.date.setText(list.get(position).date);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView windDesc;
        TextView waterDesc;
        TextView temp;
        TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            temp = itemView.findViewById(R.id.temp);
            icon = itemView.findViewById(R.id.item_img);
            windDesc = itemView.findViewById(R.id.wind_desc);
            waterDesc = itemView.findViewById(R.id.water_desc);
            date = itemView.findViewById(R.id.date);
        }
    }
}