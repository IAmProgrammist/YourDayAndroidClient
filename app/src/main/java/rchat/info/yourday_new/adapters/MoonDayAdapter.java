package rchat.info.yourday_new.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.containers.moon_calend.MoonDay;
import rchat.info.yourday_new.containers.moon_calend.Tip;

import static rchat.info.yourday_new.containers.moon_calend.TipTypes.BAD;
import static rchat.info.yourday_new.containers.moon_calend.TipTypes.GOOD;
import static rchat.info.yourday_new.containers.moon_calend.TipTypes.NEUTRAL;

public class MoonDayAdapter extends BaseAdapter {

    MoonDay moonDay;
    Context ctx;
    Dialog dialog;

    public MoonDayAdapter(Context ctx, MoonDay moonDay) {
        this.moonDay = moonDay;
        this.ctx = ctx;
        dialog = new Dialog(ctx);
    }

    @Override
    public int getCount() {
        return 8;
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
    public View getView(final int position, View view, ViewGroup parent) {
        if (position == 0) {
            DisplayMetrics display = ctx.getResources().getDisplayMetrics();
            int height = display.heightPixels;
            View v = View.inflate(ctx, R.layout.empty, null);
            v.setEnabled(false);
            v.setClickable(false);
            v.setPadding(0, 0, 0, ((int) height / 6));
            return v;
        } else if (position == 1) {
            View va = View.inflate(ctx, R.layout.moon_header, null);
            va.setEnabled(false);
            va.setClickable(false);
            return va;
        } else if (position == 7) {
            View vs = View.inflate(ctx, R.layout.empty, null);
            vs.setEnabled(false);
            vs.setClickable(false);
            vs.setPadding(0, 0, 0, 100);
            return vs;
        } else {
            if (position == 2) {
                view = View.inflate(ctx, R.layout.moonrow_main, null);
                ((TextView) view.findViewById(R.id.date)).setText(moonDay.description.date);
                ((TextView) view.findViewById(R.id.moonName)).setText(moonDay.description.moonName);
                ((TextView) view.findViewById(R.id.phaseName)).setText(moonDay.description.phaseName);
                ((TextView) view.findViewById(R.id.dawn)).setText(moonDay.description.dawn);
                ((TextView) view.findViewById(R.id.visibility)).setText(moonDay.description.visibility);
                ((TextView) view.findViewById(R.id.fromAndTo)).setText(moonDay.description.fromAndTo);
                if (moonDay.description.sunset.equals("")) {
                    ((TextView) view.findViewById(R.id.sunset)).setVisibility(View.GONE);
                } else {
                    ((TextView) view.findViewById(R.id.sunset)).setText(moonDay.description.sunset);
                }
                int res = 0;
                switch (moonDay.description.imgType) {
                    case 1:
                        res = R.drawable.moon_value_1;
                        break;
                    case 2:
                        res = R.drawable.moon_value_2;
                        break;
                    case 3:
                        res = R.drawable.moon_value_3;
                        break;
                    case 4:
                        res = R.drawable.moon_value_4;
                        break;
                    case 5:
                        res = R.drawable.moon_value_5;
                        break;
                    case 6:
                        res = R.drawable.moon_value_6;
                        break;
                    case 7:
                        res = R.drawable.moon_value_7;
                        break;
                    case 8:
                        res = R.drawable.moon_value_8;
                        break;
                    case 9:
                        res = R.drawable.moon_value_9;
                        break;
                }
                ((ImageView) view.findViewById(R.id.imageView5)).setImageResource(res);
            } else {
                int pos = position - 3;
                Tip tip = moonDay.tips.get(pos);
                view = View.inflate(ctx, R.layout.moon_item_row, null);
                if (tip.type == GOOD) {
                    view.findViewById(R.id.bg).setBackgroundResource(R.drawable.goodd);
                } else if (tip.type == BAD) {
                    view.findViewById(R.id.bg).setBackgroundResource(R.drawable.badd);
                } else if (tip.type == NEUTRAL) {
                    view.findViewById(R.id.bg).setBackgroundResource(R.drawable.neutrall);
                }
                ((TextView) view.findViewById(R.id.title)).setText(tip.heading);
                ((TextView) view.findViewById(R.id.desc)).setText(tip.descritpion);
                final Tip tipp = tip;
                view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopup(tipp);
                    }
                });
            }
        }
        return view;
    }

    public void showPopup(Tip t) {
        if (Build.VERSION.SDK_INT >= 21) {
            TextView presentName;
            ListView list;
            Button close;
            dialog.setContentView(R.layout.popup_present);
            presentName = dialog.findViewById(R.id.presentName);
            list = dialog.findViewById(R.id.list);
            list.setDivider(null);
            close = dialog.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            presentName.setText(t.heading);
            PresentDescriptionAdapter adapter = new PresentDescriptionAdapter(ctx, t.fullDesc);
            list.setAdapter(adapter);
            dialog.show();
            Display display = ((WindowManager) ctx.getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            dialog.getWindow().setLayout((int) (width * 0.95), (int) (height * 0.75));
        } else {
            TextView presentName;
            ListView list;
            Button close;
            dialog.setContentView(R.layout.popup_present);
            presentName = dialog.findViewById(R.id.presentName);
            list = dialog.findViewById(R.id.list);
            list.setDivider(null);
            close = dialog.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            presentName.setVisibility(View.GONE);
            PresentDescriptionAdapter adapter = new PresentDescriptionAdapter(ctx, t.fullDesc);
            list.setAdapter(adapter);
            dialog.setTitle(t.heading);
            dialog.findViewById(R.id.back).setBackgroundResource(R.color.white);
            dialog.show();
            Display display = ((WindowManager) ctx.getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            dialog.getWindow().setLayout((int) (width * 0.95), (int) (height * 0.75));
        }
    }
}
