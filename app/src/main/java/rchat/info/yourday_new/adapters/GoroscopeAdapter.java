package rchat.info.yourday_new.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.activities.ChooseSign;
import rchat.info.yourday_new.containers.Goroscope;

public class GoroscopeAdapter extends BaseAdapter {

    boolean hasSign;
    Context ctx;
    Goroscope goroscope;

    public GoroscopeAdapter(Context ctx, boolean hasSign) {
        this.hasSign = hasSign;
        this.ctx = ctx;
        goroscope = null;
    }

    public GoroscopeAdapter(Context ctx, Goroscope goroscope) {
        this.ctx = ctx;
        this.goroscope = goroscope;
        hasSign = true;
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
            View va = View.inflate(ctx, R.layout.goroscope_header, null);
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
            if (!hasSign) {
                view = View.inflate(ctx, R.layout.goroscope_unidentified, null);
                view.findViewById(R.id.choose_goroscope).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ctx, ChooseSign.class);
                        ctx.startActivity(intent);
                    }
                });
                return view;
            } else {
                view = View.inflate(ctx, R.layout.goroscoperow, null);
                TextView sign = view.findViewById(R.id.sign);
                ((TextView) view.findViewById(R.id.container)).setText(goroscope.getPrediction());
                switch (goroscope.getSign()) {
                    case "Aries":
                        sign.setText("Овен");
                        break;
                    case "Taurus":
                        sign.setText("Телец");
                        break;
                    case "Gemini":
                        sign.setText("Близнецы");
                        break;
                    case "Cancer":
                        sign.setText("Рак");
                        break;
                    case "Leo":
                        sign.setText("Лев");
                        break;
                    case "Libra":
                        sign.setText("Весы");
                        break;
                    case "Scorpio":
                        sign.setText("Скорпион");
                        break;
                    case "Sagittarius":
                        sign.setText("Стрелец");
                        break;
                    case "Сapricorn":
                        sign.setText("Козерог");
                        break;
                    case "Aquarius":
                        sign.setText("Водолей");
                        break;
                    case "Pisces":
                        sign.setText("Рыбы");
                        break;
                    case "Virgo":
                        sign.setText("Дева");
                        break;
                }
                return view;
            }
        }
    }
}
